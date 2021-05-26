# Openshift Development & Deployment

## I. Create Spring Boot Application Component - odo - Devfile

1. Create a spring boot odo application

```bash
odo create java-springboot todo-springboot-odo-devfile --context ./
```

2. Create the database instance by following ["Option 2"](../README.md) of the database setup.

clean up

```bash
odo delete todo-springboot-devfile
rm devfile.yaml
rm -rf .odo
```

## II. Create Spring Boot Application Component - odo - Source to Image

```bash
odo create java todo-springboot-odo-s2i --s2i --context ./

odo config set --env POSTGRES_HOST=database
odo config set --env POSTGRES_DATABASE=todo
odo config set --env POSTGRES_USER=postgres
odo config set --env POSTGRES_PASSWORD=password

odo push --show-log
```

```bash
odo delete todo-springboot-odo-s2i
rm devfile.yaml
rm -rf .odo
```

## III. Create Spring Boot Application Component - odo - Binary to Image

```bash
mvn clean compile package -DskipTests
s
odo create java todo-springboot-odo-b2i --s2i --binary target/todo-0.0.1-SNAPSHOT.jar 

odo url create --port 8080

odo config set --env POSTGRES_HOST=database
odo config set --env POSTGRES_DATABASE=todo
odo config set --env POSTGRES_USER=postgres
odo config set --env POSTGRES_PASSWORD=password

odo push --show-log
```

clean up

```bash
odo delete todo-springboot-odo-b2i
rm devfile.yaml
rm -rf .odo
```

## IV. Create Spring Boot Application Component - oc - Source to Image

todo

## V. Create Spring Boot Application Component - oc - Binar to Image

todo