FROM gradle:jdk
COPY . /home/gradle/src/
WORKDIR /home/gradle/src

RUN sed -i 's/jdbc:derby:DEMODB;create=true/jdbc:mariadb:\/\/mariadb:3306\/appdb/g' src/main/resources/registry.properties
RUN sed -i 's/jdbcusername=/jdbcusername=root/g' src/main/resources/registry.properties
RUN sed -i 's/jdbcpassword=/jdbcpassword=root/g' src/main/resources/registry.properties
RUN sed -i 's/org.apache.derby.jdbc.EmbeddedDriver/org.mariadb.jdbc.Driver/g' src/main/java/fr/univartois/ili/jai/servlet/MainListener.java
RUN sed -i 's/jdbc:derby:DEMODB/jdbc:mariadb:\/\/mariadb:3306\/appdb/g' src/main/java/fr/univartois/ili/jai/servlet/MainListener.java
RUN for file in src/main/java/fr/univartois/ili/jai/persistance/TeamTDG.java src/main/java/fr/univartois/ili/jai/persistance/PlayerTDG.java src/main/java/fr/univartois/ili/jai/persistance/GameTDG.java; do \
    sed -i 's/GENERATED ALWAYS AS IDENTITY/AUTO_INCREMENT/g' $file; \
    done
RUN gradle build -x test

FROM tomcat:10.0
COPY --from=0 /home/gradle/src/build/libs/*.war /usr/local/tomcat/webapps/app1.war
