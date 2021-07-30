# 设置基础镜像
FROM nginx
# 定义作者
LABEL maintainer="haiweiwang1109@163.com"
# 将dist文件中的内容复制到 /usr/share/nginx/html/ 这个目录下面
COPY public/  /usr/share/nginx/html/
COPY ./nginx/nginx.conf /etc/nginx/conf.d/default.conf
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN echo "-------------------- web Environmental configuration --------------------"
EXPOSE 80
