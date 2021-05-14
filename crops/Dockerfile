FROM java:8-jre

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
RUN mkdir /app
RUN mkdir -p /data/crops
ADD crops-1.0-SNAPSHOT.jar /app/
CMD ["java", "-Dfile.encoding=UTF-8", "-jar", "-Dspring.profiles.active=prod", "/app/crops-1.0-SNAPSHOT.jar"]

EXPOSE 5050
