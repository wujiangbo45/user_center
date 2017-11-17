#!/bin/bash

echo "List of modules for deploying: "  ${LIST_OF_MODULES}
echo "Current module:  "  $2
export DOCKER_HOST=tcp://10.0.1.237:2375
export DOCKER_REPO=wddocker.mapbar.com

function deploy
{
        echo "Try to deploy to docker"
#        cd target
        docker build -t ${DOCKER_REPO}/$1.$2:$3 .
        docker tag -f ${DOCKER_REPO}/$1.$2:$3 ${DOCKER_REPO}/$1.$2:latest
        docker push ${DOCKER_REPO}/$1.$2

}

if [ -z ${LIST_OF_MODULES} ]
then
    deploy $1 $2 $3
    exit
else

    for i in $(echo ${LIST_OF_MODULES} | tr "," "\n")
    do
        echo "Compare modules: " $i $2
            if [ "$i" = "$2" ]
            then
                deploy $1 $2 $3 
                exit
            fi
    done

    echo "Skip deploying to docker"
    exit
    
fi

