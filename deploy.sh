echo "stopping running application"
ssh $DEPLOY_USER@$DEPLOY_HOST 'docker stop chatbot'
ssh $DEPLOY_USER@$DEPLOY_HOST 'docker rm chatbot'

echo "pulling latest version of the code"
ssh $DEPLOY_USER@$DEPLOY_HOST 'docker pull hieupham/java-fb-chat'

echo "starting the new version"
ssh $DEPLOY_USER@$DEPLOY_HOST 'docker run -d --name chatbot -p 8080:8080 hieupham/java-fb-chat'

echo "success!"

exit 0