#!/usr/bin/env bash
mkdir -pv /mapbar/app/opentsp-user/logs
mkdir -pv /mapbar/app/opentsp-user/config
docker run -d -p 8888:8888 --name opentsp-user-service -v /mapbar/app/opentsp-user/logs:/opentsp-user/log -v /mapbar/app/opentsp-user/config:/opentsp-user/config opentsp-user
docker logs --tail -100 -f opentsp-user-service