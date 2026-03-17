#!/bin/bash

# Build script for GondasClient
# Requires: Java 8 JDK (recommended: AdoptOpenJDK or Zulu JDK 8)

echo "=========================================="
echo "  GondasClient v2.0.0 Build Script"
echo "  For Minecraft 1.16.5 Forge"
echo "=========================================="

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" != "1" ]; then
    echo "WARNING: Java 8 is required for Minecraft 1.16.5 Forge!"
    echo "Current Java version: $(java -version 2>&1 | head -n 1)"
    echo ""
    echo "Please set JAVA_HOME to Java 8 JDK or install Java 8."
    echo "Example: export JAVA_HOME=/path/to/java8"
    echo ""
    read -p "Continue anyway? (y/n): " CONTINUE
    if [ "$CONTINUE" != "y" ]; then
        exit 1
    fi
fi

# Download Gradle wrapper if not exists
if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ]; then
    echo "Downloading Gradle wrapper..."
    mkdir -p gradle/wrapper
    curl -L -o gradle/wrapper/gradle-wrapper.jar "https://github.com/gradle/gradle/raw/v7.6.0/gradle/wrapper/gradle-wrapper.jar"
fi

# Create gradlew script
echo "Creating gradlew script..."
cat > gradlew << 'GRADLEW'
#!/bin/sh
APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Use JAVA_HOME if set
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME"
    fi
else
    JAVACMD="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH."
fi

# Escape application args
save () {
    for i do printf %s\\n "$i" | sed "s/'/'\\\\''/g;1s/^/'/;\$s/\$/' \\\\/" ; done
    echo " "
}
APP_ARGS=`save "$@"`

# Collect all arguments for the java command
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'
JVM_OPTS=${JVM_OPTS:-$DEFAULT_JVM_OPTS}

eval set -- $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS "\"-Dorg.gradle.appname=$APP_BASE_NAME\"" -classpath "\"$APP_HOME/gradle/wrapper/gradle-wrapper.jar\"" org.gradle.wrapper.GradleWrapperMain "$APP_ARGS"

exec "$JAVACMD" "$@"
GRADLEW
chmod +x gradlew

# Build the project
echo ""
echo "Building project..."
./gradlew build --no-daemon --info

# Check if build succeeded
if [ -f "build/libs/GondasClient-1.16.5-2.0.0.jar" ]; then
    echo ""
    echo "=========================================="
    echo "  BUILD SUCCESSFUL!"
    echo "=========================================="
    echo ""
    echo "Output: build/libs/GondasClient-1.16.5-2.0.0.jar"
    echo ""
    echo "Installation:"
    echo "1. Copy the JAR file to your Minecraft mods folder"
    echo "2. For PojavLauncher: /games/PojavLauncher/mods/"
    echo "3. For MCLauncher: /games/mclauncher/mods/"
    echo ""
else
    echo ""
    echo "Build failed. Check the output above for errors."
    echo "Make sure you have Java 8 JDK installed."
fi
