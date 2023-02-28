#OpenJ9 may be fine for production - it doesn't support debugging
#FROM adoptopenjdk/maven-openjdk11-openj9:latest
#FROM adoptopenjdk/openjdk11:alpine
FROM adoptopenjdk/openjdk11:jdk-11.0.9.1_1
ADD  apache-karaf-4.2.8.tar.gz /usr/local/
COPY chanter-branding-0.0.4-SNAPSHOT.jar /usr/local/apache-karaf-4.2.8/lib/
COPY jre.properties /usr/local/apache-karaf-4.2.8/etc/
COPY org.ops4j.pax.url.mvn.cfg /usr/local/apache-karaf-4.2.8/etc/
COPY users.properties /usr/local/apache-karaf-4.2.8/etc/
EXPOSE 8181
EXPOSE 5005
CMD  /usr/local/apache-karaf-4.2.8/bin/karaf debug


