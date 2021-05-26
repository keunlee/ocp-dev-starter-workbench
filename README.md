# Openshift Developer Starter Workbench 

## I. Pre-requisites

- odo v2.2.0

## II. Login and Create a Project

```bash
odo login -u <user> -p <password>
odo project create odo-todo
```

## III. Install Operators 

### Postgress Operator 

Add the operator to the project:

PostgreSQL Operator by Dev4Ddevs.com
0.1.1 provided by Dev4Devs.com

install operator

```bash
cat <<EOF | oc apply -f -
apiVersion: operators.coreos.com/v1alpha1
kind: Subscription
metadata:
  name: postgresql-operator-dev4devs-com
  namespace: odo-todo
  labels:
    operators.coreos.com/postgresql-operator-dev4devs-com.odo-todo: ''

spec:
  channel: alpha
  installPlanApproval: Automatic
  name: postgresql-operator-dev4devs-com
  source: community-operators
  sourceNamespace: openshift-marketplace
EOF

```

### Service Binding Operator

Add the operator to the project: 

Service Binding Operator
0.7.1 provided by Red Hat

install operator

```bash
cat <<EOF | oc apply -f -
apiVersion: operators.coreos.com/v1alpha1
kind: Subscription
metadata:
  name: rh-service-binding-operator
  namespace: openshift-operators
  labels:
    operators.coreos.com/rh-service-binding-operator.openshift-operators: ''
spec:
  channel: preview
  installPlanApproval: Automatic
  name: rh-service-binding-operator
  source: redhat-operators
  sourceNamespace: openshift-marketplace
```

## IV. Create Database Instance

### Option 1: Create Database Instance with `oc` CLI

```bash
cat <<EOF | oc apply -f -
apiVersion: postgresql.dev4devs.com/v1alpha1
kind: Database
metadata:
  name: database
  annotations:
    service.binding/db.name: 'todo'
    service.binding/db.password: 'password'
    service.binding/db.user: 'postgres'
spec:
  databaseCpu: 30m
  databaseCpuLimit: 60m
  databaseMemoryLimit: 512Mi
  databaseMemoryRequest: 128Mi
  databaseName: todo
  databaseNameKeyEnvVar: POSTGRESQL_DATABASE
  databasePassword: password
  databasePasswordKeyEnvVar: POSTGRESQL_PASSWORD
  databaseStorageRequest: 1Gi
  databaseUser: postgres
  databaseUserKeyEnvVar: POSTGRESQL_USER
  image: centos/postgresql-96-centos7
  size: 1
EOF
```

### Option 2: Create Database Instance with `odo`

NOTE: As of odo v2.2.0, this option will only work on odo projects that meet the following criteria: 

- an odo `devfile.yaml` must be present, which means you must create an odo project before running this
- the build must NOT be an odo s2i (source to image) build.

1. Store the yaml of the service in a file: 

```bash
odo service create postgresql-operator.v0.1.1/Database --dry-run > db.yaml
```

2. Modify and add following values under metadata: section in the db.yaml file:

```yaml
  name: database
  annotations:
    service.binding/db_name: 'path={.spec.databaseName}'
    service.binding/db_password: 'path={.spec.databasePassword}'
    service.binding/db_user: 'path={.spec.databaseUser}'
```

3. Change the following values under `spec`: section of the YAML file:

```yaml
  databaseName: "todo"
  databasePassword: "postgres"
  databaseUser: "password"
```

4. Create the database from the YAML file: 

```bash
odo service create --from-file db.yaml
odo push --show-log
```

## V. Create Database Schema

Wait until the database pod is running, before moving on. (i.e. `watch oc get po`)

```bash
# list pods and look for the database pod
oc get po

# where pod for database is: database-5c5c84dd58-6kqf8
oc rsh database-5c5c84dd58-6kqf8 createdb todo
oc rsh database-5c5c84dd58-6kqf8 psql -c "CREATE SEQUENCE sequence START 1 INCREMENT 1"
```