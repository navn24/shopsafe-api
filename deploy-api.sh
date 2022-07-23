echo "Param passed: " $1
echo "Docker Login"

echo "***********" |sudo docker login https://docker.pkg.github.com -u navn24 --password-stdin

echo "check if docker container is running "

containerId=`sudo docker ps |grep docker.pkg.github.com/*********** |cut -d' ' -f1`

echo "containerId : "$containerId
if [ -z "$containerId" ]
then
      echo "Docker container is NOT running before deployment. New container with latest image will be attampted to start" 
else
      echo "Docker container is running. will stop the existing to deploy new" 
	  sudo docker stop $containerId
fi

echo "Starting docker container prune"
sudo docker container prune  -f

echo "Starting docker image prune"
sudo docker image prune  -a -f

echo "Starting docker  images pull"
sudo docker pull docker.pkg.github.com/***********

echo "Starting docker container run"

sudo  docker run -d -p 8080:8090 --env SPRING_PROFILE=$1 -t docker.pkg.github.com/***********:$1
