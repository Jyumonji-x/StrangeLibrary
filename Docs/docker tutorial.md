### 第一步 打包

首先进入frontend_vue2文件夹

`npm install`

`npm run build`

build可能会失败，再来一次一般就好了

然后进入backend文件夹

`mvn package -Dmaven.test.skip=true -U -e -X -B`

全部打包完成即可

### 第二步 docker compose

注意在这一步之前要启动docker desktop

回到项目根目录，使用docker-compose打包镜像
`docker-compose build`

然后启动

`docker-compose up -d`

停止
`docker-compose down`

### 注意事项

#### mysql8 修改密码

参考[(27条消息) Ubunto20.04安装MySQL并修改root用户密码（Linux安装mysql root用户无法登陆）_itjiangpo的博客-CSDN博客](https://blog.csdn.net/qq_26164609/article/details/106881079)

#### mysql访问权限

新建sql容器时候 需要在容器内操作，更改mysql的root用户访问权限

`mysql -uroot -p`

`use mysql;`

`select user,host from user;`

`update user set host = '%' where user = 'root';`

`flush privileges;`

如果在本地调试的时候，记得停止前端和后端的docker，防止端口占用

mysql这些注意事项讲道理这些不用管了。。。都写成脚本了



# 以上是微服务之前的方案

### 1.新增加单独的mysql的docker-compose

怕你们记不住命令行。。。。就也写成单独的docker-compose了

直接去database下面执行`docker-compose build`，打包镜像

(之后几个命令都可以不用记忆，可以在docker desktop里面管理)

然后启动

`docker-compose up -d`

停止
`docker-compose stop`

删除

`docker-compose rm -f`

