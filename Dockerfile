# Usa una imagen oficial de OpenJDK con Maven
FROM eclipse-temurin:23-jdk


# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia Maven Wrapper correctamente
COPY .mvn/ .mvn/
COPY mvnw mvnw.cmd pom.xml ./

# Da permisos de ejecución al wrapper de Maven
RUN chmod +x mvnw

# Descarga las dependencias para mejorar la cacheabilidad
RUN ./mvnw dependency:resolve dependency:go-offline

# Copia el código fuente
COPY src/ src/


# Verifica que el JAR se haya generado
RUN ls -l target/

# Expone el puerto 8080
EXPOSE 8080

# Ejecuta la aplicación
CMD ["java", "-jar", "target/DI-AD-P1-Backend-0.0.1-SNAPSHOT.jar"]

