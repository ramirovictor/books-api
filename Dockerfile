FROM openjdk:17-jdk-oracle

WORKDIR /usr/src/back
COPY . /usr/src/back

EXPOSE 8080
EXPOSE 35729

RUN curl -fSL https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz | tar -xzv

ENV MAVEN_HOME=/usr/src/back/apache-maven-3.9.6
ENV PATH=${MAVEN_HOME}/bin:${PATH}

RUN mvn clean install -DskipTests

CMD [ "mvn", "spring-boot:run", "-Dspring-boot.run.profiles=dev" ]