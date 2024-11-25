FROM 535324349245.dkr.ecr.ap-southeast-1.amazonaws.com/base:jdk-11.0.17

RUN mkdir /data

COPY bb-order/target/bb-order-2.0.0.jar /data/
COPY start.sh /data/

WORKDIR /data

EXPOSE 7015

CMD ["/bin/sh",  "start.sh"]
#CMD ["/bin/bash", "-c", "java -jar -Dspring.profiles.active=test -Dbbex.service=bb-shard.bbex -DshardId=1001 -Dbbex.instanceName=bb-shard-1 broker-api-1.0.0.BHPC-SNAPSHOT.jar"]
