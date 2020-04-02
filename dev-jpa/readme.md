

# Karaf Example

```
docker run -it \
 -v ~/.m2:/home/user/.m2\
 -p 15006:5005\
 -p 18182:8181\
 --name karaf1\
 mhus/apache-karaf:4.2.6_04 debug

```

Install

```
feature:repo-add mvn:org.apache.shiro/shiro-features/1.5.1/xml/features
feature:repo-add mvn:de.mhus.osgi/mhus-features/7.0.0-SNAPSHOT/xml/features
feature:repo-add mvn:de.mhus.osgi/dev-features/7.0.0-SNAPSHOT/xml/features

feature:install dev-ds-h2
feature:install dev-jpa

dev-res -y cp default

```

Try it

```
dev-jpa .*hib.* test01
```

Show tables:

```
jdbc:metadata h2 tables "" table
```

Trace Datasource:

```
createdbtrace -o h2 trace
dev-jpa * ds trace
```

## Test Adb

Install

```
# start adb api and db delegate
dev-res -y cp dev-adb 

xdb:list

```

# Benchmark

```

docker run -d --name karaf-mysql \
 -e MYSQL_ROOT_PASSWORD=nein \
 mariadb:10.3

docker exec -it karaf-mysql mysql -pnein

CREATE DATABASE db_test;
CREATE OR REPLACE USER 'usr_test'@'%' IDENTIFIED BY 'nein';
GRANT ALL PRIVILEGES ON db_test.* TO 'usr_test'@'%';
quit

docker run -it \
 -v ~/.m2:/home/user/.m2\
 --link karaf-mysql:dbserver \
 -p 15006:5005\
 -p 18182:8181\
 --name karaf1\
 mhus/apache-karaf:4.2.6_04 debug

feature:repo-add mvn:org.apache.shiro/shiro-features/1.5.1/xml/features
feature:repo-add mvn:de.mhus.osgi/mhus-features/7.0.0-SNAPSHOT/xml/features
feature:repo-add mvn:de.mhus.osgi/dev-features/7.0.0-SNAPSHOT/xml/features

feature:install dev-ds-h2
feature:install dev-jpa

install -s mvn:mysql/mysql-connector-java/5.1.18
dev-res -y cp default
dev-res -y cp dev-benchmark-mysql

dev-jpa ds adb/common org.hibernate.dialect.MariaDB103Dialect

dev-jpa benchmark 10000 100

```


