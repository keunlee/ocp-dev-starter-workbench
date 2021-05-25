# Openshift Developer Starter Workbench 

## Login and Create a Project

```bash
odo login -u <user> -p <password>
odo project create odo-todo
```

## Install Postgress Operator and Create Database Instance

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

create database instance: 

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

create database

```bash
# list pods and look for the database pod
oc get po

# where pod for database is: database-5c5c84dd58-6kqf8
oc rsh database-5c5c84dd58-6kqf8 createdb todo
oc rsh database-5c5c84dd58-6kqf8 psql -c "CREATE SEQUENCE sequence START 1 INCREMENT 1"
```