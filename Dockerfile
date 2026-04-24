# ---- Stage 1: extract layers from Spring Boot jar ----
FROM eclipse-temurin:21-jre as builder

WORKDIR /extracted

# копируем fat-jar
COPY build/libs/*.jar app.jar

# распаковываем layered jar
RUN java -Djarmode=layertools -jar app.jar extract


# ---- Stage 2: runtime image ----
FROM eclipse-temurin:21-jre

WORKDIR /app

# копируем слои по отдельности (ключевая оптимизация)
COPY --from=builder /extracted/dependencies/ ./
COPY --from=builder /extracted/spring-boot-loader/ ./
COPY --from=builder /extracted/snapshot-dependencies/ ./
COPY --from=builder /extracted/application/ ./

EXPOSE 9091

# запуск через Spring Boot loader
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
