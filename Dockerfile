FROM eclipse-temurin
COPY target/*.jar price-comparison.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "price-comparison.jar"]