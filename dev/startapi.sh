git config --global core.autocrlf false

docker-compose -f docker-compose-api.yml down

docker image rm compreface-api

docker-compose -f docker-compose-api.yml up --build &