function copyEnvVarsToApplicationProperties {
    APPLICATION_PROPERTIES=$HOME"/java-facebook-chatbot/src/main/resources/application.properties"
    export APPLICATION_PROPERTIES
    echo "Application Properties should exist at $APPLICATION_PROPERTIES"
    sed -i 's/^validation.token=.*/validation.token=$VALIDATION_TOKEN/g' $APPLICATION_PROPERTIES
    sed -i 's/^app.secret=.*/app.secret=$APP_SECRET/g' $APPLICATION_PROPERTIES
    sed -i 's/^page.access.token=.*/age.access.token=$PAGE_ACCESS_TOKEN/g' $APPLICATION_PROPERTIES
}