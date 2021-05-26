# Openshift Development & Deployment

## I. Create Spring Boot Application Component - odo - Devfile

1. Create a spring boot odo application

```bash
odo create java-springboot todo-springboot-odo-devfile --context ./
```

2. Create the database instance by following ["Option 2"](../README.md#option-2-create-database-instance-with-odo) of ["IV. Create Database Instance"](../README.md#iv-create-database-instance)

3. Create the database schema by following ["V. Create Database Schema"](../README.md#v-create-database-schema)

4. link the application to the database created in the previous step

```bash
odo service list

odo link Database/database 

odo push -f --show-log
```

access the app url with context: `http://exposed-route/`

clean up

```bash
odo unlink Database/database
odo push
odo service delete Database/database
odo delete todo-springboot-devfile
rm devfile.yaml
rm -rf .odo
```

## II. Create Spring Boot Application Component - odo - Source to Image

**skip 1 and 2 if database and schema are setup already**

1. Create database instance from ["Option 1"](../README.md#option-1-create-database-instance-with-oc-cli) in ["IV. Create Database Instance"](../README.md#iv-create-database-instance)

2. Create the database schema by following ["V. Create Database Schema"](../README.md#v-create-database-schema)

3. Create application: 

```bash
odo create java todo-springboot-odo-s2i --s2i --context ./

odo config set --env DATABASE_SERVICE_HOST=database
odo config set --env DATABASE_DB_NAME=todo
odo config set --env DATABASE_DB_USER=postgres
odo config set --env DATABASE_DB_PASSWORD=password

odo push --show-log
```
access the app url with context: `http://exposed-route/`

clean up

```bash
odo delete todo-springboot-odo-s2i
rm devfile.yaml
rm -rf .odo
```

## III. Create Spring Boot Application Component - odo - Binary to Image

**skip 1 and 2 if database and schema are setup already**

1. Create database instance from ["Option 1"](../README.md#option-1-create-database-instance-with-oc-cli) in ["IV. Create Database Instance"](../README.md#iv-create-database-instance)

2. Create the database schema by following ["V. Create Database Schema"](../README.md#v-create-database-schema)

3. Create application: 

```bash
mvn clean compile package -DskipTests

odo create java todo-springboot-odo-b2i --s2i --binary target/todo-0.0.1-SNAPSHOT.jar 

odo url create --port 8080

odo config set --env DATABASE_SERVICE_HOST=database
odo config set --env DATABASE_DB_NAME=todo
odo config set --env DATABASE_DB_USER=postgres
odo config set --env DATABASE_DB_PASSWORD=password

odo push --show-log
```

access the app url with context: `http://exposed-route/`


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