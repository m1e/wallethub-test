## Running the application

To run the application you will need: 

1. Java 1.8+ installed and appended to you $PATH variable
2. MySQL 5.6+ installed and running on 3306 port, with `root:rootpassword` user
3. MySQL Existing schema named `logs` with `utf8` charset
4. Create corresponding database tables, please, find scripts in `/src/main/resources/schema.sql`

You may find an executable .jar under `/distrubution` directory.

To run the app:

  1. Open an instance of CMD windows (Windows) or Terminal (MacOS, Linux) in the source folder
  2. Run the app using `java -jar distribution/parser.jar --accesslog=distribution/access.log --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100` 


If your MySQL has different configuration, please, change `src/main/resources/application.yml`, 
rebuild the app with `Maven` or `Gradle` and run it from new executable .jar

## Building and running with Maven 

To build the app with maven you will need Maven 3.5.0+ installed and appended to your $PATH variable

To build and run the app follow the next steps:

 1. Open an instance of CMD windows (Windows) or Terminal (MacOS, Linux) in the source folder
 2. Build the application using `mvn clean install`
 3. Run the app using `java -jar target/log-parser-1.0-SNAPSHOT.jar --accesslog=distribution/access.log --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100` 
 
## Building and running with Gradle 

To build and run the app follow the next steps:

 1. Open an instance of CMD windows (Windows) or Terminal (MacOS, Linux) in the source folder
 2. Build the application using `./gradlew clean build`
 3. Run the app using `java -jar build/libs/log-parser-1.0-SNAPSHOT.jar --accesslog=distribution/access.log --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100` 

 
 
