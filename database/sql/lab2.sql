create database `book` default character set utf8 collate utf8_general_ci;
use mysql;
select user,host from user;
update user set host = '%' where user = 'root';
flush privileges;