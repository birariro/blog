FROM openjdk:17-alpine

COPY ./build/libs/*.jar blog.jar

ENTRYPOINT ["sh", "-c", "java -jar -Djasypt=${jasypt} blog.jar"]
