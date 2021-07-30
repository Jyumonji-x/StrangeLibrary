## 王海伟 lab5实验总结

### 1、负责任务

（1）微服务初期框架打通

（2）微服务架构docker设计

（3）自动化部署

### 2、微服务框架

在lab4当中没有调通框架，最后直接放弃选择推进业务，在这次lab当中，我依旧是选择了Eureka，在迭代初期构建了一个简单的demo，选择了Eureka+RestTemplate，完善了idea开发流程，方便后续移植标准化。粗略的完成了user-service的移植尝试，并由其他同学完成后续的开发

### 3、微服务架构docker设计

在微服务的开发当中，由于后端大量的RestTemplate调用，各个服务都需要和数据库接洽，和前端对后端调用的端口和接口复杂化，原本旧的docker框架（前段，后端，数据库）在部署的时候需要改变后端yml文件设置，前端nginx调用转发，确保容器之间的联通，才可以正确部署。

但是在微服务的docker部署上，这样的方式并不是合适了：

1. 后端的RestTemplate调用在本地开发的时候是通过不同端口来调用不同服务，要修改localhost的调用为服务相应的docker名字

2. 各个后端服务的sql的url配置都需要改变

3. 前端的调用也需要根据不同的服务修改localhost的调用为服务相应的docker名字

如果按照原有方式部署的话，在每次编译部署前都需要做出这些修改，并且push到devcloud后编译，本地测试又需要改回来，相当复杂。

所以这里有一个需求，docker之间需要可以通过localhost访问到其他的容器

我当时是想了几种方案

第一种是让docker之间互联，直接使用links，但是docker各自依旧使用不同的ip，对localhost依旧不成功

第二种是尝试固定ip，但是在bridge（docker默认网络模式）下无法享有同一个ip

第三种方案是使用host网络模式

```
host mode networking can be useful to optimize performance, and in situations where a container needs to handle a large range of ports, as it does not require network address translation (NAT), and no “userland-proxy” is created for each port.
```

直接让docker享有host的ip，抢占host的端口，需要注意服务器的端口占用问题

值得注意的是，host模式只支持linux上的docker engine

```
The host networking driver only works on Linux hosts, and is not supported on Docker Desktop for Mac, Docker Desktop for Windows, or Docker EE for Windows Server.
```

这就导致了在本地测试docker是没有意义的（我们会有9个docker容器，本地跑电脑也受不了。。。），所以我也单独加了一个sql的docker compose方便本地测试

### 4、自动化部署

完成了docker的架构设计就直接部署了，需要在远程端口测试。

首先是预备工作，我在本地搭建了一个docker所需要的文件夹框架，大致是项目根目录下有compose文件，前端文件，数据库文件夹，后端文件夹（包含所有服务），以及各个dockerfile和需要的配置文件，直接scp上传到服务器

然后华为云上设置流水线，这部分和以前差不多，但是发现了一个以前没有注意的问题，前端在部署的时候，由于devcloud不支持传送文件夹，传输的是单个包，导致前端的打包出的文件夹需要压缩传输，然后shell脚本来解压缩，这里的问题是解压不会覆盖原来的文件夹，所以这里在解压前需要加一个`rm -rf ./piblic`（写成``rm -rf /`就好玩了。。。）

最后我在远程端写了两个脚本，一个是直接看得到所有docker后台的脚本，一个是直接启动（不放心华为云），看后台方便调试。

然后就是需要重写nginx脚本，感谢前端同学许同樵基本写完了（前端为了区分不同的服务有自己的接口代理模式），我接手之后改了几个错误就可以顺利运行

最后，感谢几位同学的幸苦付出
