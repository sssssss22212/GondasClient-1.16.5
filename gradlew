#!/bin/bash
APP_BASE_NAME=${0##*/}
APP_HOME=$( cd "$( dirname "$0" )" >/dev/null 2>&1 && pwd )
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'
CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar
JAVACMD=java
eval set -- $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS "\"-Dorg.gradle.appname=$APP_BASE_NAME\"" -classpath "\"$CLASSPATH\"" org.gradle.wrapper.GradleWrapperMain "$@"
exec "$JAVACMD" "$@"
