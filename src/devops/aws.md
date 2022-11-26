# AWS

## VPC / Subnet

- Each AWS account has a default VPC associated with it.
- One VPC can only be inside one region, but spans multiple availability zones of that region
- One VPC could have multiple subnet in it.
- One subnet can only be inside one AZ
- Security Group applies to instance level, but it attaches to vpc, and you can choose which SG to use for instances running inside a VPC
- Network Access Control List works at subnet level, NACL can be used along with SG to control what traffic is allowed in, what is not.
- Route Table, control which traffic is allowed to leave VPC
- Public subnets have route table entry to the internet through the internet gateway, private subnets don't.
- Traffic within public subnets is routed through an internet gateway.
- If instances in private network want to access public internet, e.g. update version, you can use Route Table redirect outgoing request to Network Address Translation (NAT) Gateway 
