FROM openjdk:13-jdk-slim
VOLUME /tmp
RUN apt-get update \
  && apt-get -y install vim \
  && apt-get -y install iproute2
COPY target/edao-0.0.1-SNAPSHOT.jar edao.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-agentlib:jdwp=transport=dt_socket,address=0.0.0.0:8104,server=y,suspend=n","-jar","/edao.jar", "-debug"]
