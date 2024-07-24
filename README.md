# TransUnion Data Generator

This application will generate sample data based on TransUnion's account history table.

## Running

First, set the following environment variables:

`YB_URL` The jdbc url for Yugabyte, defaults to `jdbc:yugabytedb://localhost:5433/yugabyte`

`YB_USER` - The username for Yugabyte, defaults to `yugabyte`

`YB_PASSWORD` = The password the Yugabyte user, defaults to `password`

Build the application with Maven:

`mvn clean package`

Then run an action. To insert initial data:

```
cd target
java -jar transunion-data-generator*.jar insert 5000000
```

Where the first argument is `insert` and the second argument is the number of records to insert.

To update data:

```
cd target
java -jar transunion-data-generator*.jar update
```
Which will continue to update all of the records until the application is stopped.
