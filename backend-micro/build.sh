cd ./service-discovery
mvn package -Dmaven.test.skip=true -U -e -X -B
cd ..

cd ./user-service
mvn package -Dmaven.test.skip=true -U -e -X -B
cd ..

cd ./book-service
mvn package -Dmaven.test.skip=true -U -e -X -B
cd ..

cd ./mail-service
mvn package -Dmaven.test.skip=true -U -e -X -B
cd ..

cd ./borrow-service
mvn package -Dmaven.test.skip=true -U -e -X -B
cd ..

cd ./comment-service
mvn package -Dmaven.test.skip=true -U -e -X -B
cd ..

cd ./logger-service
mvn package -Dmaven.test.skip=true -U -e -X -B
cd ..
