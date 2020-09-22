# qifu3
Backend Admin WEB base on JAVA &amp; Spring / Spring-boot &amp; MyBatis &amp; Bootstrap

Database table doc:
https://github.com/billchen198318/qifu3/blob/master/doc/qifu3-table-doc.odt

Database source script:
https://github.com/billchen198318/qifu3/blob/master/doc/qifu3.sql


<br>
<br>

### Create database

```
MYSQL> create database qifu3;
MYSQL> exit;

# mysql qifu3 -u root -p < qifu3.sql

```

### Config datasource ( core-app/resources/db1-config.properties )

```
db1.datasource.jdbcUrl=jdbc:mysql://localhost/qifu3?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db1.datasource.username=MYSQL-ACCOUNT
db1.datasource.password=MYSQL-PASSWORD
```

<br>
<br>

### Login page:
```
http://[YOU-SERVER]:[PORT]/
```

```
http://127.0.0.1:8088/
```

<br>
<br>

### Administrator account: 
account: admin <br>
passowrd: admin99 <br>


<br>
<br>

### Screenshot: 

<img alt="demo1" src="https://raw.githubusercontent.com/billchen198318/qifu3/master/doc/pic/qifu3-001.png">


