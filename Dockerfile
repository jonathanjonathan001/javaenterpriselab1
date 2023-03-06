FROM maven:3.9.0-eclipse-temurin-19 AS build
# Copy src folder into this image
COPY src/ /app/src
COPY pom.xml /app/
# Run mvn package
RUN mvn --file /app/pom.xml clean package

FROM quay.io/wildfly/wildfly:27.0.1.Final-jdk19
ENV WILDFLY_CLI /opt/jboss/wildfly/bin/jboss-cli.sh
ENV WILDFLY_HOME /opt/jboss/wildfly
ENV WILDFLY_USER admin
ENV WILDFLY_PASS adminpw
ARG DB_USER
ARG DB_PASS
ENV DB_URI host.docker.internal:3306
ENV DB_NAME demo

RUN $WILDFLY_HOME/bin/add-user.sh --silent=true $WILDFLY_USER $WILDFLY_PASS ManagementRealm
#Start wildfly
RUN bash -c '/opt/jboss/wildfly/bin/standalone.sh &' && \
bash -c 'until `$WILDFLY_CLI -c ":read-attribute(name=server-state)" 2> /dev/null | grep -q running`; do echo `$WILDFLY_CLI -c ":read-attribute(name=server-state)" 2> /dev/null`; sleep 1; done'

#Download mysql jar driver file
RUN bash -c '$WILDFLY_HOME/bin/standalone.sh &' && \
      bash -c 'until `$WILDFLY_CLI -c ":read-attribute(name=server-state)" 2> /dev/null | grep -q running`; do echo `$WILDFLY_CLI -c ":read-attribute(name=server-state)" 2> /dev/null`; sleep 1; done' && \
      curl --location --output /tmp/mysql-connector-j-8.0.32.jar --url https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.32/mysql-connector-j-8.0.32.jar && \
      $WILDFLY_CLI --connect --command="module add --name=com.mysql --resources=/tmp/mysql-connector-j-8.0.32.jar --dependencies=javax.api,javax.transaction.api" && \
      $WILDFLY_CLI --connect --command="/subsystem=datasources/jdbc-driver=mysql:add(driver-name=mysql,driver-module-name=com.mysql,driver-class-name=com.mysql.cj.jdbc.Driver)" && \
      $WILDFLY_CLI --connect --command="data-source add \
            --name=MySqlDS \
            --jndi-name=java:/MySQLDS \
            --user-name=${DB_USER} \
            --password=${DB_PASS} \
            --driver-name=mysql \
            --connection-url=jdbc:mysql://${DB_URI}/${DB_NAME} \
            --use-ccm=false \
            --max-pool-size=25 \
            --blocking-timeout-wait-millis=5000 \
            --enabled=true" && \
    $WILDFLY_CLI --connect --command=":shutdown" && \
      rm -rf $WILDFLY_HOME/standalone/configuration/standalone_xml_history/ $WILDFLY_HOME/standalone/log/* && \
      rm -f /tmp/*.jar

COPY --from=build /app/target/jakartalab.war /opt/jboss/wildfly/standalone/deployments/
# CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
