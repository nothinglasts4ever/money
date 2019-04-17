# Money Transfer application
Test task to implement money transfer functionality

## Stack
* Java 12
* Spark Java
* Guice
* Lombok
* Log4j
* jOOQ
* In-memory H2
* Flyway
* Spock

## Instruction
```
git clone
mvn clean install
java -jar target/money-0.1-SNAPSHOT-jar-with-dependencies.jar
```

## Documentation
See tests in `com.github.nl4.money.e2e` package

## Items to improve
* Replace Spark Java with async web framework in order to work with jOOQ `transactionResultAsync()` method
* Implement load testing
* Move required things to .properties
