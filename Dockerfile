FROM tomcat:8.0
MAINTAINER hieupham

COPY /target/java-facebook-chatbot-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/java-fb-chatbot.war
CMD ["catalina.sh", "run"]