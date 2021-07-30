FROM mysql/mysql-server
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY sql/lab2.sql /docker-entrypoint-initdb.d/