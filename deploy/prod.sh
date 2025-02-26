#!/usr/bin/env bash
REPOSITORY=/home/ubuntu
PROJECT_NAME=ourfit-backend

if [ "$(id -u)" -ne 0 ]; then
  echo "> This script must be run as root or with sudo previleges"
  exit 1
fi

echo "> Check if application is already running..."
if pgrep -f ${PROJECT_NAME}.*.jar > /dev/null; then
  echo ">> Found running applications, stopping..."
  pkill -15 -f "${PROJECT_NAME}.*.jar"
  echo ">> Sent termination signal to all instances, waiting 5 seconds..."
  sleep 5
  echo ">>> Done"
else
  echo ">> No running application found"
fi

echo "> Pulling changes from remote repository..."
if [ -d "$REPOSITORY/$PROJECT_NAME" ]; then
  cd $REPOSITORY/$PROJECT_NAME || { echo "> Failed CD to project root directory"; exit 1; }
  git pull
  echo ">> Done"
fi

echo "> Remove old artifact"
find "$REPOSITORY" -type f -name "*.jar" -delete
find "$REPOSITORY/$PROJECT_NAME/build/libs" -type f -name "*.jar" -delete
echo ">> Done"

echo "> Building artifact with Gradle"
./gradlew build -x test
echo ">> Done"

echo "> Copying new artifact to the repository root directory"
ARTIFACT=$(find $REPOSITORY/$PROJECT_NAME/build/libs -type f -name "*.jar")
if [ -z "$ARTIFACT" ]; then
  echo ">> No valid artifact found in build paths"
  exit 1
fi
cp "$ARTIFACT" $REPOSITORY/
cd $REPOSITORY || { echo "> Failed CD to repository root directory"; exit 1; }
echo ">> Done"

echo "> Deploy new application"
JAR_NAME=$(basename "$ARTIFACT")
sudo nohup java -jar $REPOSITORY/"$JAR_NAME" --spring.profiles.active=prod 2>&1 &
echo ">> Deploy process has been completed!"
