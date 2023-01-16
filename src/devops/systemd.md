## 配置文件放置地址

最粗暴的方式就是直接放在 /etc/systemd/system目录下。因为此目录是systemd读取配置文件的默认目录
其次，可以不直接在这个目录下放配置文件，而只放一个连接文件。

### 一个配置文件长这样

```
[Unit]
Description=Forge Swap Service

[Service]
Type=simple
User=deepak
Group=admin
ExecStart=/home/peding/forge-swap/_build/staging/rel/forge_swap/bin/forge_swap start
ExecStop=/home/peding/forge-swap/_build/staging/rel/forge_swap/bin/forge_swap stop
Environment=HOME=/home/peding/
Environment=FORGE_SWAP_CONFIG=/home/peding/swap/integration.toml
SyslogIdentifier=forge_swap

[Install]
WantedBy=multi-user.target

```

#### Unit 区块

Unit区块通常是配置文件的第一个区块，用来定义 Unit 的元数据，以及配置与其他 Unit 的关系
```
[Unit]
Description：简短描述
Documentation：文档地址
Requires：当前 Unit 依赖的其他 Unit，如果它们没有运行，当前 Unit 会启动失败
Wants：与当前 Unit 配合的其他 Unit，如果它们没有运行，当前 Unit 不会启动失败
BindsTo：与Requires类似，它指定的 Unit 如果退出，会导致当前 Unit 停止运行
Before：如果该字段指定的 Unit 也要启动，那么必须在当前 Unit 之后启动
After：如果该字段指定的 Unit 也要启动，那么必须在当前 Unit 之前启动
Conflicts：这里指定的 Unit 不能与当前 Unit 同时运行
Condition...：当前 Unit 运行必须满足的条件，否则不会运行
Assert...：当前 Unit 运行必须满足的条件，否则会报启动失败
```

#### Service区块

该区块用来配置服务，只有 Service 类型的 Unit 才有这个区块
```
[Service]
Type：定义启动时的进程行为。它有以下几种值。
     Type=simple：默认值，执行ExecStart指定的命令，启动主进程。In this mode systemd does not wait for the processes to finish starting up (as it has no way of know when this has happened) and so continues executing and dependent services straight away.。所以说通过 start 或者 start_iex启动elixir程序就应该陪成这种模式。
     Type=forking：以 fork 方式从父进程创建子进程，创建后父进程会立即退出，选了这种type的实际意义是Systemd is waiting for the process to fork itself and for the parent process to end, which it takes as an indication that the process has started successfully. So, if your process did not fork itself and remained in the foreground then systemctl start will hang indefinitely or until the processes crashes. 所以说通过 daemon 或者 daemon_iex启动elixir程序就应该陪成这种模式
     Type=oneshot：一次性进程，Systemd 会等当前服务退出，再继续往下执行
     Type=dbus：当前服务通过D-Bus启动
     Type=notify：当前服务启动完毕，会通知Systemd，再继续往下执行
     Type=idle：若有其他任务执行完毕，当前服务才会运行
User=deepak
Group=admin
ExecStart：启动当前服务的命令
ExecStartPre：启动当前服务之前执行的命令
ExecStartPost：启动当前服务之后执行的命令
ExecReload：重启当前服务时执行的命令
ExecStop：停止当前服务时执行的命令
ExecStopPost：停止当其服务之后执行的命令
RestartSec：自动重启当前服务间隔的秒数
Restart：定义何种情况 Systemd 会自动重启当前服务，可能的值包括
    always（总是重启
    on-success
    on-failure
    on-abnormal
    on-abort
    on-watchdog
TimeoutSec：定义 Systemd 停止当前服务之前等待的秒数
Environment：指定环境变量
```

#### Install区块

该区块通常是配置文件的最后一个区块，用来定义如何启动，以及是否开机启动

```
[Install]
WantedBy：它的值是一个或多个 Target，当前 Unit 激活时（enable）符号链接会放入/etc/systemd/system目录下面以 Target 名 + .wants后缀构成的子目录中
RequiredBy：它的值是一个或多个 Target，当前 Unit 激活时，符号链接会放入/etc/systemd/system目录下面以 Target 名 + .required后缀构成的子目录中
Alias：当前 Unit 可用于启动的别名
Also：当前 Unit 激活（enable）时，会被同时激活的其他 Unit
```
