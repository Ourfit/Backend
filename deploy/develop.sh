#!/usr/bin/env bash
REPOSITORY=/home/ubuntu
PROJECT_NAME=ourfit-backend

if [ "$(id -u)" -ne 0 ]; then
  echo "> This script must be run as root or with sudo previleges"
  exit 1
fi

echo "> Check if application is already running..."
CURRENT_PID=$(pgrep -rf ${PROJECT_NAME}.*.jar)
if [ -z "$CURRENT_PID" ]; then
  echo ">> No running application found"
else
  echo ">> Found a running application (PID: $CURRENT_PID)"
  kill -15 "$CURRENT_PID"
  echo ">> Sent termination signal to the application and wait 5 seconds..."
  sleep 5
  echo ">>> Done"
fi

echo "> Pulling changes from remote repository..."
if [ -d "$REPOSITORY/$PROJECT_NAME" ]; then
  cd $REPOSITORY/$PROJECT_NAME || { echo "> Failed CD to project root directory"; exit 1; }
  git pull
  echo ">> Done"
fi

echo "> Building artifact with Gradle"
./gradlew build -x test
echo ">> Done"

echo "> Copying artifact to the repository root directory"
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
sudo nohup java -jar $REPOSITORY/"$JAR_NAME" --spring.profiles.active=develop 2>&1 &
echo ">> Deploy process has been completed!"
