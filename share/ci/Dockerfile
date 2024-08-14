FROM openjdk:17-alpine

COPY ./backend/*.jar blog.jar

ENTRYPOINT ["sh", "-c", "java -jar -Djasypt=${jasypt} blog.jar"]
