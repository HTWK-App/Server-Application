FROM dockerfile/java
MAINTAINER hknochi@gmail.com

# Add project files and NewRelic Agent
ADD . /opt/server-app/
ADD newrelic*.zip /opt/docker/newrelic.zip

WORKDIR /opt/docker

# Install NewRelic Agent
RUN unzip newrelic.zip
ADD license /opt/docker/newrelic/
RUN cd newrelic && sed -i -e "s/My Application/HTWK-Server/g" newrelic.yml

WORKDIR /opt/server-app/
RUN ./gradlew clean build 

# Production settings
EXPOSE 8080
CMD ["java","-javaagent:/opt/docker/newrelic/newrelic.jar","-Djava.security.egd=file:/dev/./urandom","-jar","/opt/server-app/build/libs/server-app.jar"]