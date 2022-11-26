# Kubernetes DNS

## Trouble Shooting DNS problem

First you should follow the [official doc](https://kubernetes.io/docs/tasks/administer-cluster/dns-debugging-resolution/) and deploy a `dnsutils` pod.

### Possible solutions
- Run command `kubectl logs -f --namespace=kube-system -l k8s-app=kube-dns` to stream the log. Add the `-f` option is very important to make sure you are really looking at latest logs.

- Add following line to `/etc/systemd/system/kubelet.service.d/10-kubeadm.conf` on each node, and restart systemd service
```
Environment="KUBELET_DNS_ARGS=--cluster-dns=10.96.0.10 --cluster-domain=cluster.local"

ExecStart=/usr/bin/kubelet $KUBELET_KUBECONFIG_ARGS $KUBELET_SYSTEM_PODS_ARGS $KUBELET_DNS_ARGS $KUBELET_EXTRA_ARGS
```

```
sudo systemctl daemon-reload
sudo systemctl restart kubelet
```

- Switching `flannel` to `host-gw` instead of `vxlan`
```shell
kubectl edit cm -n kube-system kube-flannel-cfg
```

  - replace `vxlan` with `host-gw`

  - save

  - not sure if needed, but I did it anyway: `kubectl delete pods -l app=flannel -n kube-system`

- Delete `coredns` pods

## How to reduce CoreDNS errors?

The `resolver.conf` of a pod would look like this:

```shell
$ cat /etc/resolver.conf

nameserver 10.100.0.10
search default.svc.cluster.local svc.cluster.local cluster.local us-west-2.compute.internal
options ndots:5
```

If you want to resolve domain name `xxxx`, then the resolver will try following in order:
```
xxx.default.svc.cluster.local
xxx.svc.cluster.local 
xxx.cluster.local 
xxx.us-west-2.compute.internal
```

So if the FQDN of your service is `my-service.default.svc.cluster.local`, then in the K8S or Envoy configs you should put `my-service` instead of `my-service.default` as the domain name, since the resolver will succeed in the first round by automatically appending `default.svc.cluster.local` to `my-service`. However, if your service's FQDN is `my-service.my-namespace.svc.cluster.local`, then you should put `my-service.my-namespace` as domain name and it will succeed in the second round.
