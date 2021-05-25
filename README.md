# Openshift Developer Starter Workbench 

## Pre-requisites

- odo v2.2.0

## Login and Create a Project

```bash
odo login -u <user> -p <password>
odo project create odo-todo
```

## Install Postgress Operator 

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

## Create Database Instance

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

NOTE: This option will only work on odo projects that meet the following criteria: 

- an odo `devfile.yaml` must present 
- the build must NOT be an s2i (source to image) build.

1. Store the yaml of the service in a file: 

```bash
odo service create postgresql-operator.v0.1.1/Database --dry-run > db.yaml
```

2. Modify and add following values under metadata: section in the db.yaml file:

```yaml
  name: sampledatabase
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
```

## Create Database Schema

create database

```bash
# list pods and look for the database pod
oc get po

# where pod for database is: database-5c5c84dd58-6kqf8
oc rsh database-5c5c84dd58-6kqf8 createdb todo
oc rsh database-5c5c84dd58-6kqf8 psql -c "CREATE SEQUENCE sequence START 1 INCREMENT 1"
```