# Specify base image
FROM fabric8/java-alpine-openjdk8-jdk
# Maintainer information
LABEL maintainer="haiweiwang1109@163.com"
COPY target/*.jar /app/app.jar 
# 存在问题


WORKDIR /app

RUN echo "-------------------- user service Environmental configuration --------------------"

EXPOSE 9090

# Set environment code UTF-8
ENV LANG C.UTF-8
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Run - configures the container to be executable
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar -D user.timezone=GMT+08"]


