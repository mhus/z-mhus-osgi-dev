
```
docker run --name karaf-redis -d redis
```


```
docker run -it \
 -v ~/.m2:/home/user/.m2\
 --link karaf-redis:redisserver\
 -p 15006:5005\
 -p 18182:8181\
 --name karaf1\
 mhus/apache-karaf:4.2.6_04 debug

```

```

install -s mvn:com.fasterxml.jackson.core/jackson-annotations/2.10.3
install -s mvn:com.fasterxml.jackson.core/jackson-core/2.10.3
install -s mvn:com.fasterxml.jackson.core/jackson-databind/2.10.3
install -s mvn:org.yaml/snakeyaml/1.26
install -s mvn:com.fasterxml.jackson.dataformat/jackson-dataformat-yaml/2.10.3
install -s 'wrap:mvn:io.netty/netty-all/4.1.48.Final$Bundle-SymbolicName=netty-all&Bundle-Version=4.1.48&Export-Package=io.netty.*;version=4.1.48'
install -s mvn:org.reactivestreams/reactive-streams/1.0.3
install -s mvn:io.reactivex.rxjava2/rxjava/2.2.19
install -s mvn:javax.cache/cache-api/1.1.1
install -s 'wrap:mvn:org.jodd/jodd-bean/5.1.4$Bundle-SymbolicName=jodd-bean&Bundle-Version=5.1.4&Export-Package=jodd.bean.*;version=5.1.4'
install -s mvn:net.bytebuddy/byte-buddy/1.10.9
install -s mvn:org.objenesis/objenesis/2.5.1
install -s 'wrap:mvn:de.ruedigermoeller/fst/2.57$Import-Package=*;resolution:=optional&overwrite=MERGE'
install -s 'wrap:mvn:org.redisson/redisson/3.12.3$Import-Package=*;resolution:=optional&overwrite=MERGE'

feature:repo-add mvn:org.apache.shiro/shiro-features/1.5.1/xml/features
feature:repo-add mvn:de.mhus.osgi/karaf-features/7.0.0-SNAPSHOT/xml/features
feature:install mhu-dev

mhus:dev cp default

install -s mvn:de.mhus.osgi/dev-cache/7.0.0-SNAPSHOT

```


```
dev-redis x
```