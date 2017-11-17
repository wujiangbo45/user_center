FROM wddocker.mapbar.com/java:1.0s
MAINTAINER wupeng@mapbar.com

RUN mkdir /opentsp-user
WORKDIR /opentsp-user

ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV PROFILE release
ENV JAVA_OPTS "-Xmx2048m -Xms1024m -XX:+UseConcMarkSweepGC"

RUN export LANG=en_US.UTF-8 && export LANGUAGE=en_US:en && rm -rf /etc/localtime

EXPOSE 8888

VOLUME /opentsp-user/config
VOLUME /opentsp-user/log
VOLUME /etc/localtime

COPY target/opentsp-user-web-*-boot.jar ./opentsp-user-web.jar

ENTRYPOINT java -server -jar -Dspring.profiles.active=$PROFILE $JAVA_OPTS opentsp-user-web.jar