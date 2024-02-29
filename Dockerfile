# open jdk 17 버전의 환경을 구성
FROM amd64/amazoncorretto:17

# build가 되는 시점에 JAR_FILE이라는 변수 명에 build/libs/*.jar 선언
# build/libs - gradle로 빌드했을 때 jar 파일이 생성되는 경로
ARG JAR_FILE=build/libs/*.jar

# JAR_FILE을 app.jar로 복사
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar",  "/app.jar"]