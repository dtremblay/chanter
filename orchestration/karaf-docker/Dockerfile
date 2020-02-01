#OpenJ9 may be fine for production - it doesn't support debugging
#FROM adoptopenjdk/maven-openjdk11-openj9:latest
FROM adoptopenjdk/openjdk11:alpine
ADD  apache-karaf-4.2.7.tar.gz /usr/local/
COPY chanter-branding-0.0.3-SNAPSHOT.jar /usr/local/apache-karaf-4.2.7/lib/
COPY jre.properties /usr/local/apache-karaf-4.2.7/etc/
COPY org.ops4j.pax.url.mvn.cfg /usr/local/apache-karaf-4.2.7/etc/
EXPOSE 8181
EXPOSE 5005
CMD  /usr/local/apache-karaf-4.2.7/bin/karaf debug


