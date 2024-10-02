FROM openjdk:17-jdk-alpine AS build
WORKDIR /code
COPY . .
RUN chmod 775 mvnw
RUN sed -i 's/\r$//' mvnw
RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine AS run
COPY --from=build /code/target/*.jar /backend.jar
ENTRYPOINT ["java","-jar","/backend.jar"]
