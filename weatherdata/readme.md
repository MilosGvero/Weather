## Docker image

Steps to create a docker image:
* execute `mvn clean package`
* execute `docker build . -t weather:1 -f docker/Dockerfile`