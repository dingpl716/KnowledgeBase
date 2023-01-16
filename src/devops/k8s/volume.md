
## Persistent Volume, PV
- like a cluster resource, such as CPU or RAM
- needs actual physical storage
- it's like a external plugin to your cluster
- 

## Persistent Volume Claim, PVC
- PV is used to create volume for a cluster, PVC is used to claim a PV, saying "Hey, I am a POD, I need some volume that matches some criteria."

## Storage Class, SC
- SC provisions PV dynamically when PVC claims it.
- 

