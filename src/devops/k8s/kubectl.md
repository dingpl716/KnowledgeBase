```

kubectl get pod -o wide -n kube-system


# Check member pods of a service.
# NAME denotes name of services
# ENDPOINTS denotes pod IP and port
$ kubectl get endpoints

NAME            ENDPOINTS                           AGE
kubernetes      10.0.8.229:6443                     4h32m
mongodb-service 10.244.0.1:27017,10.244.3.2:27017   4h

```
