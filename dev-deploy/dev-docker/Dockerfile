FROM mhus/apache-karaf:4.2.6_04
MAINTAINER Mike Hummel <mh@mhus.de>

#ENV KARAF_UID 0
ENV JAVA_MAX_MEM 2048m

USER $APP_UID
COPY target/assembly.tar.gz /docker/assembly.tar.gz
COPY profiles /docker/profiles

# Start once - prepare for a faster start
RUN /docker/assembly.sh
RUN export DEPLOY_FORCE=1 && /docker/deploy.sh
# Need this option for kubernetes SSL handshake - it's a bug in OpenJDK
# https://github.com/kubernetes-client/java/issues/854
RUN echo "export EXTRA_JAVA_OPTS=\"-Djdk.tls.client.protocols=TLSv1.2\"" > /opt/karaf/bin/setenv
USER 0