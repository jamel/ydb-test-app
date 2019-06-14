### Example of usage YDB Java SDK

##### Install prerequisites

```
$ sudo apt-get update
$ sudo apt-get install git openjdk-11-jdk
```

##### Build

```
$ git clone https://github.com/jamel/ydb-test-app.git
$ cd ydb-test-app
$ ./mvnw package
```

##### Run

```
$ ./mvnw -q exec:java -Dexec.args="<accountId> <keyId> <private_key_file_path> <endpoint> <database>"
```

Where: 
  * accountId             - identificator of service account
  * keyId                 - identificator of generated key
  * private_key_file_path - path to a file where private part of the key is located
  * endpoint              - YDB endpoint
  * database              - database name


After run the program will output something like this: 
```
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by com.google.protobuf.UnsafeUtil (file:/home/jamel/.m2/repository/com/google/protobuf/protobuf-java/3.6.1/protobuf-java-3.6.1.jar) to field java.nio.Buffer.address
WARNING: Please consider reporting this to the maintainers of com.google.protobuf.UnsafeUtil
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
result: 42
```
