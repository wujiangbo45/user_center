#!/usr/bin/env bash
docker build -t opentsp-user .
docker tag opentsp-user wddocker.mapbar.com/opentsp-user
docker push wddocker.mapbar.com/opentsp-user