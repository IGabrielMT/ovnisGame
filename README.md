# Proyecto de Simulación de Ovnis

Este proyecto es una simulación de ovnis desarrollada en Java 21 por Ian Gabriel Martinez Torres. Utiliza Maven como herramienta de construcción y gestión de dependencias.

## Especificaciones del Proyecto

- **Lenguaje**: Java 21
- **Herramienta de Construcción**: Maven
- **IDE Recomendado**: IntelliJ IDEA 2024.1.2

## Requisitos

- **Java**: JDK 21 o superior
- **Maven**: 3.6.0 o superior

## Instalación

1. **Clonar el repositorio**:
    ```sh
    git clone <URL_DEL_REPOSITORIO>
    cd <NOMBRE_DEL_REPOSITORIO>
    ```

2. **Compilar el proyecto**:
    ```sh
    mvn clean install
    ```

## Ejecución

Para ejecutar la aplicación, puedes usar el siguiente comando de Maven:

```sh
mvn exec:java -Dexec.mainClass="co.edu.uptc.Main"
```

O también puedes usar el siguiente comando de Java desde el directorio en que se encuentre el jar:

```sh
java -jar target/simulacion-ovnis-1.0-SNAPSHOT.jar
```

