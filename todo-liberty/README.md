# Openshift Development & Deployment

## I. Create Liberty Application Component - odo - Devfile

1. Create a spring boot odo application

```bash
odo create java-openliberty todo-lbrty-odo-devfile --context ./
```

2. Create the database instance by following ["Option 2"](../README.md#option-2-create-database-instance-with-odo) of ["IV. Create Database Instance"](../README.md#iv-create-database-instance)

3. Create the database schema by following ["V. Create Database Schema"](../README.md#v-create-database-schema)

4. Link the application to the database created in the previous step

```bash
odo service list

odo link Database/database 

odo push -f --show-log
```

clean up

```bash
odo unlink Database/database
odo push
odo service delete Database/database
odo delete todo-liberty-odo-devfile
rm devfile.yaml
rm -rf .odo
```

## II. Create Liberty Application Component - odo - Source to Image

Unavailable

## III. Create Liberty Application Component - odo - Binary to Image

Unavailable

## IV. Create Liberty Application Component - oc - Source to Image

- **works with oc source to image with the given builder images**
- **does NOT work with oc binary to image with the given builder images**

### A. Open Liberty

1. Import source builder images from docker hub (do this once)

```bash
oc import-image open-liberty-s2i --from=docker.io/openliberty/open-liberty-s2i --all --confirm 
```

2. Create a new openshift app

```bash
oc new-app image-registry.openshift-image-registry.svc:5000/odo-todo/open-liberty-s2i:20.0.0.9-java11~/. --name=todo-lbrty-s2i
oc logs -f buildconfig/todo-lbrty-s2i
```

3. Start a build of the application

```bash
oc start-build todo-lbrty-s2i --from-dir .
oc logs -f buildconfig/todo-lbrty-s2i
oc expose service todo-lbrty-s2i
```

to clean up: 

```bash
oc delete deployment todo-lbrty-s2
oc delete route todo-lbrty-s2
oc delete service todo-lbrty-s2
oc delete is todo-lbrty-s2
oc delete bc todo-lbrty-s2
```

### B. Websphere Liberty

1. Import source builder images from docker hub (do this once)

```bash
oc import-image websphere-liberty-s2i --from=docker.io/ibmcom/websphere-liberty-s2i --all --confirm
```

2. Create a new openshift app

```bash
oc new-app image-registry.openshift-image-registry.svc:5000/odo-todo/websphere-liberty-s2i:20.0.0.12-java11~/. --name=was-liberty-s2i
oc logs -f buildconfig/was-liberty-s2i
```

3. Start a build of the application

```bash
oc start-build was-liberty-s2i --from-dir .
oc logs -f buildconfig/was-liberty-s2i
oc expose service was-liberty-s2i
```

to clean up: 

```bash
oc delete deployment was-liberty-s2i
oc delete route was-liberty-s2i
oc delete service was-liberty-s2i
oc delete is was-liberty-s2i
oc delete bc was-liberty-s2i
```

## V. Create Liberty Application Component - oc - Binary to Image

Unavailable
