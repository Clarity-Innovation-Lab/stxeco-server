#!/bin/bash -e
#
############################################################

export SERVICE=$1
export PORT=7019
export SERVER=chomsky.brightblock.org
export DOCKER_ID_USER='mijoco'
export DOCKER_COMPOSE_CMD='docker-compose'
export DOCKER_CMD='docker'

if [ -z "${SERVICE}" ]; then
  mvn -f ./stxeco-api/pom.xml -Dmaven.test.skip=true clean install
	docker-compose build
	$DOCKER_CMD tag mijoco/stacks_voice_express mijoco/stacks_voice_express
	$DOCKER_CMD tag mijoco/stxeco_express mijoco/stxeco_express
	$DOCKER_CMD tag mijoco/stxeco_api mijoco/stxeco_api

	$DOCKER_CMD push mijoco/stacks_voice_express:latest
	$DOCKER_CMD push mijoco/stxeco_express:latest
	$DOCKER_CMD push mijoco/stxeco_api:latest
fi
if [ "$SERVICE" == "stxeco_express" ]; then
	docker-compose build stxeco_express
	$DOCKER_CMD tag mijoco/stxeco_express mijoco/stxeco_express
	$DOCKER_CMD push mijoco/stxeco_express:latest
fi
if [ "$SERVICE" == "stxeco_api" ]; then
  mvn -f ./stxeco-api/pom.xml -Dmaven.test.skip=true clean install
	docker-compose build
	$DOCKER_CMD tag mijoco/stxeco_api  mijoco/stxeco_api
	$DOCKER_CMD push mijoco/stxeco_api:latest
fi
if [ "$SERVICE" == "stacks_voice_express" ]; then
  mvn -f ./stxeco-api/pom.xml -Dmaven.test.skip=true clean install
	docker-compose build stacks_voice_express
	$DOCKER_CMD tag mijoco/stacks_voice_express  mijoco/stacks_voice_express
	$DOCKER_CMD push mijoco/stacks_voice_express:latest
fi

echo --- stxeco:copying to [ $PATH_DEPLOY ] --------------------------------------------------------------------------------;
printf "\n\n Connectiong to $SERVER.\n"

if [ -z "${SERVICE}" ]; then
ssh -i ~/.ssh/id_rsa -p $PORT bob@$SERVER "
  cd /home/bob/hubgit/stxeco-server
  # git pull
  # cp .env.production .env
  cat .env
  docker login
  . ~/.profile
  $DOCKER_COMPOSE_CMD -f docker-compose-images.yml pull
  $DOCKER_COMPOSE_CMD -f docker-compose-images.yml down
  $DOCKER_COMPOSE_CMD -f docker-compose-images.yml up -d
";
else
ssh -i ~/.ssh/id_rsa -p $PORT bob@$SERVER "
  cd /home/bob/hubgit/stxeco-server
  cat .env
  docker login
  . ~/.profile
  $DOCKER_COMPOSE_CMD -f docker-compose-images.yml pull  
  $DOCKER_COMPOSE_CMD -f docker-compose-images.yml stop $SERVICE
  $DOCKER_COMPOSE_CMD -f docker-compose-images.yml rm $SERVICE 
  $DOCKER_COMPOSE_CMD -f docker-compose-images.yml create $SERVICE
  $DOCKER_COMPOSE_CMD -f docker-compose-images.yml start $SERVICE
";
fi

printf "Finished....\n"
printf "\n-----------------------------------------------------------------------------------------------------\n";

exit 0;
