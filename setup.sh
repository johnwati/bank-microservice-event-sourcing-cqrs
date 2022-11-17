sudo docker network create --attachable -d bridge techbankNet

# //run MongoDB om docker
docker run -it -d --name mongo-containet -p 27017:27017 --network techbanknet --restart always -v mongo_data_container:/data/db mongo:latest

# //run docker mysql
docker run -it -d --name mysql-container -p 3306:3306 --network techbanknet -e MYSQL_ROOT_PASSWORD=techbanknetRootPsw --restart always -v mysql_data_conatiner:/var/lib/mysql mysql:latest

# //run mysql client tool
docker run -it -d --name adminer -p 8080:8080 --network techbanknet -e ADMINER_DEFAULT_SERVER=mysql-container --restart always  adminer:latest


# docker pull adminer


docker run -p 86:8080 -e bootstrapServers="127.0.0.1:9092" consdata/kouncil:latest