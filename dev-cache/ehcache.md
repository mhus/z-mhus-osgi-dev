

### Start terracotta

https://hub.docker.com/r/terracotta/terracotta-server-oss

```
docker run --name karaf-tc -p 9410:9410 -d terracotta/terracotta-server-oss:5.6.4
```

### Start karaf 1:

```
docker run -it \
 -v ~/.m2:/home/user/.m2\
 --link karaf-tc:tcserver\
 -p 15006:5005\
 -p 18182:8181\
 --name karaf1\
 mhus/apache-karaf:4.2.6_04 debug
```

### Start karaf 2:

```
docker run -it \
 -v ~/.m2:/home/user/.m2\
 --link karaf-tc:tcserver\
 -p 15007:5005\
 -p 18183:8181\
 --name karaf2\
 mhus/apache-karaf:4.2.6_04 debug
```

### Install

And install software in both environments

```

feature:repo-add mvn:org.apache.shiro/shiro-features/1.5.1/xml/features
feature:repo-add mvn:de.mhus.osgi/karaf-features/7.0.0-SNAPSHOT/xml/features
feature:install mhu-dev

install -s mvn:javax.cache/cache-api/1.1.1
install -s mvn:org.ehcache/ehcache/3.8.1
install -s mvn:org.ehcache/ehcache-clustered/3.8.1

install -s mvn:de.mhus.osgi/dev-cache/7.0.0-SNAPSHOT

dev-res cp default

# dev-res -y cp jms ENV_JMS_SOP_USER=admin ENV_JMS_SOP_PASS=nein ENV_JMS_SERVER=jmsserver:61616

bundle:persistentwatch add .*
```

