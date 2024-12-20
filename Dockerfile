# Dockerfile
# jdk17 Image Start
FROM openjdk:17

# 환경 변수 설정
ARG DB_NAME
ARG MYSQL_USERNAME
ARG MYSQL_PASSWORD
ARG DB_IP
ARG UPLOAD_ROOT
ARG KAKAO_REDIRECT_URI
ARG KAKAO_CLIENT_ID
ARG KAKAO_CLIENT_SECRET
ARG JWT_SECRET
ARG ALLOW_ORIGIN

ENV DB_NAME=${DB_NAME}
ENV MYSQL_USERNAME=${MYSQL_USERNAME}
ENV MYSQL_PASSWORD=${MYSQL_PASSWORD}
ENV DB_IP=${DB_IP}
ENV UPLOAD_ROOT=${UPLOAD_ROOT}
ENV KAKAO_REDIRECT_URI=${KAKAO_REDIRECT_URI}
ENV KAKAO_CLIENT_ID=${KAKAO_CLIENT_ID}
ENV KAKAO_CLIENT_SECRET=${KAKAO_CLIENT_SECRET}
ENV JWT_SECRET=${JWT_SECRET}
ENV ALLOW_ORIGIN=${ALLOW_ORIGIN}

# jar 파일 복제
COPY build/libs/familytravel-0.0.1-SNAPSHOT.jar app.jar

RUN mkdir /image
VOLUME ["/image"]

EXPOSE 8080
EXPOSE 443
EXPOSE 80

# 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar", "-Duser.timezone=Asia/Seoul"]
