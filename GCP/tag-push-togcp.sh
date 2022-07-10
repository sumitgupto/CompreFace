docker tag compreface-postgres-db asia.gcr.io/test-facegate/compreface-postgres-db:v1

docker tag compreface-api asia.gcr.io/test-facegate/compreface-api:v1

docker tag compreface-admin asia.gcr.io/test-facegate/compreface-admin:v1

docker tag compreface-core asia.gcr.io/test-facegate/compreface-core:v1

docker tag compreface-ui asia.gcr.io/test-facegate/compreface-ui:v2

#docker images

docker push asia.gcr.io/test-facegate/compreface-postgres-db:v1

docker push asia.gcr.io/test-facegate/compreface-api:v1

docker push asia.gcr.io/test-facegate/compreface-admin:v1

docker push asia.gcr.io/test-facegate/compreface-core:v1

docker push asia.gcr.io/test-facegate/compreface-ui:v2