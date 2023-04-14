#!/bin/sh

docker-compose -f docker-compose-backing-services.yml pull
docker-compose -f docker-compose-backing-services.yml up -d

scripts/wait-and-setup-backing-services.sh
