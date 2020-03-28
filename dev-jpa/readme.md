

# Karaf Example

docker run -it \
 -v ~/.m2:/home/user/.m2\
 -p 15006:5005\
 -p 18182:8181\
 --name karaf1\
 mhus/apache-karaf:4.2.6_04 debug

feature:repo-add mvn:org.apache.karaf.examples/karaf-jpa-example-features/LATEST/xml

feature:install karaf-jpa-example-datasource

feature:install karaf-jpa-example-command

#feature:install karaf-jpa-example-provider-ds-eclipselink

#feature:install karaf-jpa-example-provider-ds-hibernate     

---


feature:repo-add mvn:org.apache.shiro/shiro-features/1.5.1/xml/features
feature:repo-add mvn:de.mhus.osgi/karaf-features/7.0.0-SNAPSHOT/xml/features
feature:install mhu-dev
install -s mvn:de.mhus.osgi/dev-jpa/7.0.0-SNAPSHOT

mhus:dev -y cp default



dev-jpa .*hiber.* x

