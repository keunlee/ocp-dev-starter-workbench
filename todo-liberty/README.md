# Openshift Development & Deployment

## I. Create Liberty Application Component - odo - Devfile

```bash
odo create java-openliberty todo-lbrty-odo-devfile --context ./

odo config set --env POSTGRES_HOST=database
odo config set --env POSTGRES_DATABASE=todo
odo config set --env POSTGRES_USER=postgres
odo config set --env POSTGRES_PASSWORD=password

odo push --show-log
```

clean up

```bash
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

Import source builder images from docker hub (do this once)

```bash
oc import-image open-liberty-s2i --from=docker.io/openliberty/open-liberty-s2i --all --confirm 
```

Create a new openshift app

```bash
oc new-app image-registry.openshift-image-registry.svc:5000/odo-todo/open-liberty-s2i:20.0.0.9-java11~/. --name=todo-lbrty-s2i
oc logs -f buildconfig/todo-lbrty-s2i
```

Start a build of the application

```bash
oc start-build todo-lbrty-s2i --from-dir .
oc logs -f buildconfig/todo-lbrty-s2i
oc expose service todo-lbrty-s2i
```

to create just the build config: 

```bash
oc new-build . --image-stream=open-liberty-s2i:20.0.0.9-java11 --name=todo-lbrty-s2i  --dry-run -o yaml
```

### B. Websphere Liberty

Import source builder images from docker hub (do this once)

```bash
oc import-image websphere-liberty-s2i --from=docker.io/ibmcom/websphere-liberty-s2i --all --confirm
```

Create a new openshift app

```bash
oc new-app image-registry.openshift-image-registry.svc:5000/odo-todo/websphere-liberty-s2i:20.0.0.12-java11~/. --name=was-liberty-s2i
oc logs -f buildconfig/was-liberty-s2i
```

Start a build of the application

```bash
oc start-build was-liberty-s2i --from-dir .
oc logs -f buildconfig/was-liberty-s2i
oc expose service was-liberty-s2i
```

to create just the build config:

```bash
oc new-build . --image-stream=websphere-liberty-s2i:20.0.0.12-java11 --name=was-liberty-s2i  --dry-run -o yaml
```

## V. Create Liberty Application Component - oc - Binary to Image

Unavailable
