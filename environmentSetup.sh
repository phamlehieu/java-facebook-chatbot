function copyEnvVarsToApplicationProperties {
    APPLICATION_PROPERTIES=$HOME"/src/main/resources/application.properties"
    export APPLICATION_PROPERTIES
    echo "Application Properties should exist at $APPLICATION_PROPERTIES"
    sed 's/^validation.token=.*/validation.token=$VALIDATION_TOKEN/g' $APPLICATION_PROPERTIES
    sed 's/^app.secret=.*/app.secret=$APP_SECRET/g' $APPLICATION_PROPERTIES
    sed 's/^page.access.token=.*/age.access.token=$PAGE_ACCESS_TOKEN/g' $APPLICATION_PROPERTIES
}