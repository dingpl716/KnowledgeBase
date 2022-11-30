# K8S中Ingress, Service, Envoy, GRPC的几种配法

## Ingress + GRPC server

- 此时的GRPC server配置成ClusterIP即可
- Ingress service可以是NodePort也可以是LoadBalancer
- `backend-protocol`一定要设成GRPC

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  annotations:
    nginx.ingress.kubernetes.io/ssl-redirect: "true"
    nginx.ingress.kubernetes.io/backend-protocol: "GRPC"
    nginx.ingress.kubernetes.io/enable-cors: "true"
    nginx.ingress.kubernetes.io/cors-allow-origin: "*,null"
    nginx.ingress.kubernetes.io/cors-allow-methods: "GET, PUT, DELETE, POST, OPTIONS"
    nginx.ingress.kubernetes.io/cors-allow-headers: "keep-alive,user-agent,cache-control,content-type,content-transfer-encoding,custom-header-1,x-accept-content-transfer-encoding,x-accept-response-streaming,x-user-agent,x-grpc-web,grpc-timeout"
    nginx.ingress.kubernetes.io/cors-max-age: "1728000"
    nginx.ingress.kubernetes.io/cors-expose-headers: "*,X-CustomResponseHeader,grpc-status,grpc-message"
  name: my-service-ingress
  namespace: default
spec:
  ingressClassName: nginx
  rules:
  - host: grpctest.dev.mydomain.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: my-service
            port:
              number: 12345
  tls:
  # This secret must exist beforehand
  # The cert must also contain the subj-name grpctest.dev.mydomain.com
  # https://github.com/kubernetes/ingress-nginx/blob/master/docs/examples/PREREQUISITES.md#tls-certificates
  - secretName: tls-secret
    hosts:
      - grpctest.dev.mydomain.com
```


## Envoy + Headless GRPC server

- GRPC server 必须设置成Headless
- Envoy可以是NodePort也可以是LoadBalancer

```yaml
# Envoy POD
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: envoy
spec:
  replicas: 1
  selector:
    matchLabels:
      app: envoy
  template:
    metadata:
      labels:
        app: envoy
    spec:
      containers:
      - name: envoy
        image: envoyproxy/envoy:v1.18.3
        ports:
        - name: https
          containerPort: 8443
        volumeMounts:
        - name: config
          mountPath: /etc/envoy
      volumes:
      - name: config
        configMap:
          name: envoy-conf

# Envoy K8S Service
---
apiVersion: v1
kind: Service
metadata:
  name: envoy
spec:
  type: NodePort
  selector:
    app: envoy
  ports:
  - name: https
    protocol: TCP
    port: 8443
    targetPort: 8443
    nodePort: 30008 # The port of the node machine, range is predefined 30000 - 32767

# Envoy ConfigMap
---
apiVersion: v1
kind: ConfigMap
metadata:
  name: envoy-conf
data:
  envoy.yaml: |
    admin:
      access_log_path: /dev/stdout
      address:
        socket_address:
          address: 127.0.0.1
          port_value: 8090    
    static_resources:
      listeners:
        - name: listener_0
          address:
            socket_address:
              address: 0.0.0.0
              port_value: 8443
          filter_chains:
              - filters:
                - name: envoy.filters.network.http_connection_manager
                  typed_config:
                    "@type": type.googleapis.com/envoy.extensions.filters.network.http_connection_manager.v3.HttpConnectionManager
                    access_log:
                      - name: envoy.access_loggers.stdout
                        typed_config:
                          "@type": type.googleapis.com/envoy.extensions.access_loggers.stream.v3.StdoutAccessLog
                    codec_type: AUTO
                    stat_prefix: ingress_https
                    route_config:
                      name: local_route
                      virtual_hosts:
                        - name: https
                          domains:
                            - "*"
                          routes:
                            - match:
                                prefix: "/"
                              route:
                                cluster: grpc-server
                                timeout: 0s
                                max_stream_duration:
                                  grpc_timeout_header_max: 0s                            
                          cors:
                            allow_origin_string_match:
                              - safe_regex:
                                  google_re2: {}
                                  regex: \*
                            allow_methods: GET, PUT, DELETE, POST, OPTIONS
                            allow_headers: keep-alive,user-agent,cache-control,content-type,content-transfer-encoding,custom-header-1,x-accept-content-transfer-encoding,x-accept-response-streaming,x-user-agent,x-grpc-web,grpc-timeout
                            max_age: "1728000"
                            expose_headers: custom-header-1,grpc-status,grpc-message
                            filter_enabled:
                              default_value: {numerator: 100, denominator: HUNDRED}
                              runtime_key: cors.www.enabled
                    http_filters:
                      - name: envoy.filters.http.grpc_web
                        typed_config:
                          "@type": type.googleapis.com/envoy.extensions.filters.http.grpc_web.v3.GrpcWeb
                      - name: envoy.filters.http.cors
                        typed_config:
                          "@type": type.googleapis.com/envoy.extensions.filters.http.cors.v3.Cors
                      - name: envoy.filters.http.router
                        typed_config:
                          "@type": type.googleapis.com/envoy.extensions.filters.http.router.v3.Router
      clusters:
        - name: grpc-server
          connect_timeout: 0.5s
          type: STRICT_DNS
          dns_lookup_family: V4_ONLY
          lb_policy: ROUND_ROBIN
          http2_protocol_options: {}
          load_assignment:
            cluster_name: grpc-server
            endpoints:
              - lb_endpoints:
                - endpoint:
                    address:
                      socket_address:
                        address: grpc-service-headless
                        port_value: 50051

```

## Ingress + ClusterIP Envoy + Headless GRPC
- 此时Ingress既可以是NodePort，也可以是LoadBalancer
- Envoy必须设置成ClusterIP
- GRPC server仍然要设置成Headless

```yaml
apiVersion: v1	
kind: Service	
metadata:	
  labels:	
    app.kubernetes.io/component: controller	
    app.kubernetes.io/instance: ingress-nginx	
    app.kubernetes.io/name: ingress-nginx	
    app.kubernetes.io/part-of: ingress-nginx	
    app.kubernetes.io/version: 1.5.1	
  name: ingress-nginx-controller	
  namespace: ingress-nginx	
spec:	
  ipFamilies:	
  - IPv4	
  ipFamilyPolicy: SingleStack	
  ports:	
  - appProtocol: http	
    name: http	
    port: 80	
    protocol: TCP	
    targetPort: http	
  - appProtocol: https	
    name: https	
    port: 443	
    protocol: TCP	
    targetPort: https	
  selector:	
    app.kubernetes.io/component: controller	
    app.kubernetes.io/instance: ingress-nginx	
    app.kubernetes.io/name: ingress-nginx	
  type: NodePort	
---	
apiVersion: v1	
kind: Service	
metadata:	
  labels:	
    app.kubernetes.io/component: controller	
    app.kubernetes.io/instance: ingress-nginx	
    app.kubernetes.io/name: ingress-nginx	
    app.kubernetes.io/part-of: ingress-nginx	
    app.kubernetes.io/version: 1.5.1	
  name: ingress-nginx-controller-admission	
  namespace: ingress-nginx	
spec:	
  ports:	
  - appProtocol: https	
    name: https-webhook	
    port: 443	
    targetPort: webhook	
  selector:	
    app.kubernetes.io/component: controller	
    app.kubernetes.io/instance: ingress-nginx	
    app.kubernetes.io/name: ingress-nginx	
  type: ClusterIP	
---	
apiVersion: apps/v1	
kind: Deployment	
metadata:	
  labels:	
    app.kubernetes.io/component: controller	
    app.kubernetes.io/instance: ingress-nginx	
    app.kubernetes.io/name: ingress-nginx	
    app.kubernetes.io/part-of: ingress-nginx	
    app.kubernetes.io/version: 1.5.1	
  name: ingress-nginx-controller	
  namespace: ingress-nginx	
spec:	
  minReadySeconds: 0	
  revisionHistoryLimit: 10	
  selector:	
    matchLabels:	
      app.kubernetes.io/component: controller	
      app.kubernetes.io/instance: ingress-nginx	
      app.kubernetes.io/name: ingress-nginx	
  template:	
    metadata:	
      labels:	
        app.kubernetes.io/component: controller	
        app.kubernetes.io/instance: ingress-nginx	
        app.kubernetes.io/name: ingress-nginx	
    spec:	
      containers:	
      - args:	
        - /nginx-ingress-controller	
        - --election-id=ingress-nginx-leader	
        - --controller-class=k8s.io/ingress-nginx	
        - --ingress-class=nginx	
        - --configmap=$(POD_NAMESPACE)/ingress-nginx-controller	
        - --validating-webhook=:8443	
        - --validating-webhook-certificate=/usr/local/certificates/cert	
        - --validating-webhook-key=/usr/local/certificates/key	
        env:	
        - name: POD_NAME	
          valueFrom:	
            fieldRef:	
              fieldPath: metadata.name	
        - name: POD_NAMESPACE	
          valueFrom:	
            fieldRef:	
              fieldPath: metadata.namespace	
        - name: LD_PRELOAD	
          value: /usr/local/lib/libmimalloc.so	
        image: registry.k8s.io/ingress-nginx/controller:v1.5.1@sha256:4ba73c697770664c1e00e9f968de14e08f606ff961c76e5d7033a4a9c593c629	
        imagePullPolicy: IfNotPresent	
        lifecycle:	
          preStop:	
            exec:	
              command:	
              - /wait-shutdown	
        livenessProbe:	
          failureThreshold: 5	
          httpGet:	
            path: /healthz	
            port: 10254	
            scheme: HTTP	
          initialDelaySeconds: 10	
          periodSeconds: 10	
          successThreshold: 1	
          timeoutSeconds: 1	
        name: controller	
        ports:	
        - containerPort: 80	
          name: http	
          protocol: TCP	
        - containerPort: 443	
          name: https	
          protocol: TCP	
        - containerPort: 8443	
          name: webhook	
          protocol: TCP	
        readinessProbe:	
          failureThreshold: 3	
          httpGet:	
            path: /healthz	
            port: 10254	
            scheme: HTTP	
          initialDelaySeconds: 10	
          periodSeconds: 10	
          successThreshold: 1	
          timeoutSeconds: 1	
        resources:	
          requests:	
            cpu: 100m	
            memory: 90Mi	
        securityContext:	
          allowPrivilegeEscalation: true	
          capabilities:	
            add:	
            - NET_BIND_SERVICE	
            drop:	
            - ALL	
          runAsUser: 101	
        volumeMounts:	
        - mountPath: /usr/local/certificates/	
          name: webhook-cert	
          readOnly: true	
      dnsPolicy: ClusterFirst	
      nodeSelector:	
        kubernetes.io/os: linux	
      serviceAccountName: ingress-nginx	
      terminationGracePeriodSeconds: 300	
      volumes:	
      - name: webhook-cert	
        secret:	
          secretName: ingress-nginx-admission
---
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: my-ingress
spec:
  rules:
  - host: example.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: envoy
            port:
              number: 8444
  ingressClassName: nginx
# Envoy K8S Service
---
apiVersion: v1
kind: Service
metadata:
  name: envoy
spec:
  type: ClusterIP
  selector:
    app: envoy
  ports:
  - name: https
    protocol: TCP
    port: 8444
    targetPort: 8443
```

