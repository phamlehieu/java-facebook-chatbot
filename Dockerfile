FROM tomcat:8.0
MAINTAINER hieupham

COPY /target/java-facebook-chatbot-0.0.1-SNAPSHOTwar /usr/local/tomcat/webapps/java-fb-chatbot.war
CMD ["catalina.sh", "run"]