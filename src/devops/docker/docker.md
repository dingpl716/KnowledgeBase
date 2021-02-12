## Docker

### 基本概念

- 一个OS由两部分组成：1）最最基础的OS内核，2）在这个内核上添加一些软件和库。最终成为一个OS的发型版本
- 在Docker里，我们从一个OS的内核开始，然后添加我们想要的软件和库，最后形成一个image。
- 所以一个image和HOST OS的关系可以理解为
  - HOST OS = HOST OS Kernel + Additional Software/libs installed on HOST OS
  - image = HOST OS Kernel + Additional Software/libs installed on image
- 正因为image和HOST OS都使用一个内核，所以在Linux上只能跑Linux的image，而Mac和Windows无法直接运行Linux image，除非在Mac和Windows上先安装一个Linux虚拟机，然后再在其之上运行image。

### Build Docker Image

#### 一个Dockerfile的例子

```dockerfile
# 指定从哪个image作为基础来build
FROM universalresolver/base-ubuntu

# 声明两个build time的变量，之后我们可以在build的时候给这两个变量赋值
ARG GITHUB_READ_PACKAGES_OWNER
ARG GITHUB_READ_PACKAGES_TOKEN

# 将本地目录里面的文件加入到image里面
ADD ./settings.xml /root/.m2/
ADD . /opt/driver-did-abt

# 将image的8080端口打开
EXPOSE 8080

# 在image里面执行命令
RUN cd /opt/driver-did-abt && mvn clean install package -N -DskipTests
RUN chmod a+rx /opt/driver-did-abt/docker/run-driver-did-abt.sh

# 指定image的默认启动命令
CMD "/opt/driver-did-abt/docker/run-driver-did-abt.sh"
```
#### Build命令

执行如下命令来build这个docker image

```
docker build -f ./docker/Dockerfile -t arcblock/driver-did-abt --build-arg GITHUB_READ_PACKAGES_TOKEN=$(GITHUB_READ_PACKAGES_TOKEN) --build-arg GITHUB_READ_PACKAGES_OWNER=dingpl716 .
```

- `-f ./docker/Dockerfile` 用来指定Dockerfile的路径
- `-t arcblock/driver-did-abt` 用来为build出来的image打上一个tag。其中`arcblock`为repo owner的名字，`driver-did-abt`为repo的名字
- `--build-arg` 用来为变量赋值
- `.` 表示以当前目录作为基准build path

#### 概念

- 在Dockerfile里面的每一行命令都会产生一个Layer。
- 每一个Layer都是只读的，它们会被缓存起来，在build不同image时我们会重用这些Layer，已达到加速的目的。
- 测试例子参见[Layer  Caching Test](./LayerCachingTest/README.md)

### Run Docker Image

#### 基础用法

`docker run --rm -p 8080:8081 -v $(pwd)/src:/usr/src/app -e ENV=value jim-example`

- `--rm` Automatically remove container when it exists.
- `-p 8080:8081` 把HOST OS的8080端口映射到Container的8081端口
- `-v $(pwd)/src:/usr/src/app` 表示把HOST OS的`$(pwd)/src`挂载到container的`/usr/src/app`下面。因为当Container被删除之后，里面的数据也就都没有了，所以需要用这种方法来保存数据。
- `-e ENV=value` 传入运行时需要的环境变量

`docker run -it `

- i 表示interactive mode
- t 表示把host的terminal attache到docker的terminal上

`docker run -d`

- 将conatiner运行在后台。

#### CMD & Entrypoint

##### Case 1: CMD做为默认运行命令

```dockerfile
CMD "/opt/driver-did-abt/docker/run-driver-did-abt.sh"
```

在这种模式下，运行image的时候会默认执行这个`.sh`脚本

##### Case 2: CMD作为默认运行命令，并带参数

```dockerfile
CMD ["echo", "5"]
```

在这种模式下，运行image会直接打印出5。注意，这种模式必须使用Json格式，不能写成`"echo 5"`，因为这样会让docker把整个字符串作为一个命令来看待。

##### Case 3: Entrypoint作为默认运行命令

```dockerfile
ENTRYPOINT "echo"
# OR ENTRYPOINT ["echo"]
```

这种模式可以让我们在运行image时，省去输入需要运行的命令，但是必须传入一个参数

```
docker run repo/imagename 5
```

##### Case 4: Entrypoint作为默认运行命令, CMD作为参数默认值

```dockerfile
ENTRYPOINT ["echo"]
CMD ["5"]
```
必须使用JSON格式。这种模式可以让我们在运行image时，省去输入需要运行的命令，并提供默认值

```bash
$ docker run repo/imagename
5
```

```bash
$ docker run repo/imagename 3
3
```

#### -net host 选项

`-net host` 选项可以将docker的网络端口一对一的map到host机器上，如果不指定这个选项，docker应该是默认没有网络访问能力的。比如在Jenkins上想运行一个docker image的话，就应该这样写：
```bash
# For Jenkins use, compile and test project
jenkins-test:
	@echo "Building and testing the project..."
	@ls -al $(PWD)
	@docker run --net host -v $(PWD):/mnt/home --rm docker.particlenews.com:5000/elixir:1.11.3 /bin/bash -c "cd /mnt/home && make test"
```

但是，在MacOS上，docker并不支持这个选项，所以我们明确的，显式的定义端口映射，比如：
```bash
docker run -d \
--name website-nginx \
-p 8326:8326 \
-e FPM_HOST=host.docker.internal \
registry2.nb.com/particle/nginx-dev:599e48e
```

或者直接映射所有端口：
```bash
docker run -d \
--name website-nginx \
-P \
-e FPM_HOST=host.docker.internal \
registry2.nb.com/particle/nginx-dev:599e48e
```

### Best Practice

1. One process a container.
    - 更好维护
    - ECS, Kubernetes are built around this assumption

2. The process in docker should block and log to stdout。
    - The CMD you run should block the process from exiting.
    - The process should not run in background. It should run foreground and block anything else.
    - As it runs foreground, it sends log to stdout, 而不是disk上
	
3. Small cached images
    - Union file system -- immutable FS
    - 更容易变化的代码放在后面，不容易变化的代码放在前面
    - 一个RUN里面执行多条代码，比如
        ```dockerfile
        RUN apt-get update && \
            apt-get install -y nodejs && \
            rm -rf /var/lib/apt/lists/*
        ```
      这个的意思就是，在RUN里面我们运行了三条命令，但是整个这个RUN只会作为一个layer来cache下来。并且在第三天命令里面，我们把不需要的东西都删除了，保证在这条RUN执行完之后，我们只装了nodejs
