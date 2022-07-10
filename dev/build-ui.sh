#sed -i "s|%COMMIT_HASH%|$(git rev-parse --short HEAD)|g" ui/src/app/features/footer/footer.component.html

docker rmi $(docker images | grep 'compreface-ui')

docker images

docker system prune

docker images

docker-compose -f docker-compose-ui.yml build --force-rm --no-cache --pull &
