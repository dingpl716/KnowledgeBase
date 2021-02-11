### Distribution History

```
Debian -->  Ubuntu
Redhat --> Redhat Enterprise --> CentOS
       |                     |
       |                     --> Amazon Linux, Oracle Linux
       --> Fedora 
              
```

### 设置一个环境变量
```
export NODEJS_HOME=/usr/lib/nodejs
更改现有的PATH环境变量, 以分好为间隔符
echo $PATH
export PATH=$NODEJS_HOME/bin:$PATH
加载当前用户的profile文件，这样可以使上面对环境变量做的更改立即在当前terminal生效
source ~/.profile
让上面的更改永久生效：把上面的两个export加入到~/.profile文件里面
```

### 滚动式的显示log里面的内容
```
console 2>>fileName.log
tail -f fileName.log 
```

### 用openssl获取certificate：
```
openssl s_client -showcerts -connect github.com:443
```

### 生成sshkey
```
ssh-keygen -C peding@arcblock.io
cat ~/.ssh/id_rsa.pub
```

### List the block devices attached to your instance
```
lsblk
```

### List current disk usage.
```
df -h
```

### List the file size in a dir
```
du -sh file/path
```

### SSH Copy
```
scp <source> <destination>
                            
To copy a file from B to A while logged into B: 
scp /path/to/file username@a:/path/to/destination
                                                 
To copy a file from B to A while logged into A: 
scp username@b:/path/to/file /path/to/destination
```

### 压缩、解压
```
单个文件压缩打包 tar czvf my.tar file1 
多个文件压缩打包 tar czvf my.tar file1 file2,... 
单个目录压缩打包 tar czvf my.tar dir1 
多个目录压缩打包 tar czvf my.tar dir1 dir2 
解包至当前目录：tar xzvf my.tar
```

### 查看文件的二进制数据
```
od -x --endian=big -N 297 -An blk00000.dat
```

### 开启Swap
```
sudo fallocate -l 32G /swapfile
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
```

### Sed替换文件内容
```
sed -ri 's/("total"|"fees"|"nonce"|"value"):([0-9]+)/\1:"\2"/g' */*/*/*.json
    -r extended regular expression
    -i change file in place
    s   substitute 命令
    g   global, 这样就不会只替换第一出现的了
```

### 查看文档行数
```
wc -l file.name
```

### Mount Disk Volume
```
lsblk
NAME          MAJ:MIN RM  SIZE RO TYPE MOUNTPOINT
nvme0n1       259:0    0    1T  0 disk
├─nvme0n1p1   259:1    0 1024G  0 part /
└─nvme0n1p128 259:2    0    1M  0 part
nvme1n1       259:3    0  4.9T  0 disk
└─nvme1n1p1   259:4    0  4.9T  0 part

sudo mount /dev/nvme1n1p1 /mount_point
```