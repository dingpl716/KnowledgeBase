### 对Layer缓存的一个测试

#### 测试说明

- `test1/Dockerfile`和`test2/Dockerfile`用来分别build出两个不同的image
- 这两个image具有相同的OS，都是Ubuntu，并且都复制了`lib/dummylib`文件到image上，来模拟拷贝lib的过程
- 这两个image唯一不同的地方是，在最后一步我们copy不同的文件到image上

#### 分别build两个image:

Build Image 1:

```
 $ docker build -f test1/Dockerfile -t peiling/test1 .
Sending build context to Docker daemon   7.68kB
Step 1/3 : FROM ubuntu
 ---> 4e5021d210f6
Step 2/3 : ADD ./lib /opt/
 ---> 4e55af795bcf              <------  拷贝lib所产生的layer，在下面会被直接重用
Step 3/3 : ADD ./test1.txt /opt/
 ---> 89c783e33330
Successfully built 89c783e33330
Successfully tagged peiling/test1:latest
```

Build Image 2:

```
 $ docker build -f test2/Dockerfile -t peiling/test2 .
Sending build context to Docker daemon   7.68kB
Step 1/3 : FROM ubuntu
 ---> 4e5021d210f6
Step 2/3 : ADD ./lib /opt/
 ---> Using cache
 ---> 4e55af795bcf                <------  此处表面我们利用了之前build出来的Layer
Step 3/3 : ADD ./test2.txt /opt/
 ---> 9a5a9b964b86
Successfully built 9a5a9b964b86
Successfully tagged peiling/test2:latest
```

#### Inspect两个Image

```
 $ docker inspect peiling/test1
        "RootFS": {
          "Type": "layers",
          "Layers": [
              "sha256:c8be1b8f4d60d99c281fc2db75e0f56df42a83ad2f0b091621ce19357e19d853",
              "sha256:977183d4e9995d9cd5ffdfc0f29e911ec9de777bcb0f507895daa1068477f76f",
              "sha256:6597da2e2e52f4d438ad49a14ca79324f130a9ea08745505aa174a8db51cb79d",
              "sha256:16542a8fc3be1bfaff6ed1daa7922e7c3b47b6c3a8d98b7fca58b9517bb99b75",
              "sha256:e837d985f52d27479e4b988e7d5580a77809bb1528b45e03b67dc46a885975f4",
              "sha256:59df73f668af686d411afcab64122aef710f6fc16d2767e0b419aa8a71f79e84"
          ]
      }
```

```
 $ docker inspect peiling/test2
         "RootFS": {
            "Type": "layers",
            "Layers": [
                "sha256:c8be1b8f4d60d99c281fc2db75e0f56df42a83ad2f0b091621ce19357e19d853",
                "sha256:977183d4e9995d9cd5ffdfc0f29e911ec9de777bcb0f507895daa1068477f76f",
                "sha256:6597da2e2e52f4d438ad49a14ca79324f130a9ea08745505aa174a8db51cb79d",
                "sha256:16542a8fc3be1bfaff6ed1daa7922e7c3b47b6c3a8d98b7fca58b9517bb99b75",
                "sha256:e837d985f52d27479e4b988e7d5580a77809bb1528b45e03b67dc46a885975f4",
                "sha256:64a37003b6f7f89d5e51f93f61cd0c9e85e551504a8db5f475a7523d1946a96f"
            ]
        },
```

可以看到，两个image的前五个Layer都相同，只有最后一个不同。