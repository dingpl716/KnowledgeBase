# Kubernetes Components

This section describes components of Kubernetes.

- How request flow
  - Requests reach Ingress, there they will be routed to different Services
  - Services have their own IP addresses, and use `spec.ports.port` to denote their ports that they are listening to.
  - Services then forward requests to different pods
  - Services use `spec.selector` to identify Pods.
  - Services use `spec.ports.targetPort` to identify Pods' port to forward request.

- Pod
  - minimal unit of K8S, each pod has its own IP address.
  - but pod is ephemeral, so pod will get new IP addresses when it's recreated
  - so using pod's IP address to access pod is unstable, so we need Service

- Service
  - Service is abstraction of a serious of pods.
  - Stable IP address
  - Load balancing 
  - loose coupling
  - within & outside cluster


## Service

- ClusterIP Services, default type
- Headless
- LoadBalancer
- 

### ClusterIP Service

```yaml
spec:
  # You can also omit this. 
  type: ClusterIP
```

### Headless Service

- Defined by `spec.clusterIP` field
  ```yaml
  spec:
    clusterIP: None
  ```
- Unlike ClusterIP Server that randomly forward request to pod, Headless Service let customer to talk to a specific pod.
- You would use Headless Service to access stateful application, DB pods for example. Saying that there is a master DB pod and a slave DB pod, since you should only write on master DB pod (data will be synced to slave pod), you need to access master pod directly.
- Means we are not relying on K8S to do load balancing.
- When you create Headless Service, a corresponding ClusterIP Service will be created.
  
### NodePort Service

- This service allows external request to talk to service directly by using `node.ip.address:nodePort`. 
- Traffics will first hit `nodePort 30008` then be forwarded to `targetPort 3000`
- Similar to Headless, you can and must talk to a specific node, but a service could have multiple pods running on different nodes.
- When you create NodePort Service, a corresponding ClusterIP Service will be created.

```yaml
spec:
  type: NodePort
  ports:
    - protocol: TCP
      port: 3200
      targetPort: 3000
      nodePort: 30008 # The port of the node machine, range is predefined 30000 - 32767
```



### LoadBalancer

- This type makes a service becomes externally through **cloud providers LoadBalancer**
- This means external traffic **cannot** talk to service directly, they must reach cloud providers' Load Balancer, then be forwarded to service.
- So this service is a little bit safer than NodePort Service.
- When you create a LoadBalancer Service, a corresponding NodePort and ClusterIP Service will be created automatically.

```yaml
spec:
  type: LoadBalancer
  ports:
    - protocol: TCP
      port: 3200
      targetPort: 3000
      nodePort: 30008 # The port of the node machine, range is predefined 30000 - 32767
```
