# AS24 Reporting

AS24 Reporting is a reporting application which creates statistical report from listing and contact data.

Features:
1. Simple Web UI to display reports _[Implemented]_
2. Upload your own file to display reports _[Implemented]_
3. API endpoint to access aggregation functionality _[Not Implemented Yet]_

## Technology and Requirements

Developed using Java Programming Language and Play Framework. 

Prerequisites include:

* Java Software Developer's Kit (SE) 11 or higher
* sbt 0.13.15 or higher

To check your Java version, enter the following in a command window:

`java -version`

To check your sbt version, enter the following in a command window:

`sbt sbtVersion`

## Build and run the project

To build and run the project:

1. Use a command window to change into the example project directory, for example: `cd as24reporting`

2. Build the project. Enter: `sbt run`. The project builds and starts the embedded HTTP server. Since this downloads libraries and dependencies, the amount of time required depends partly on your connection's speed.

3. After the message `Server started, ...` displays, enter the following URL in a browser: <http://localhost:9000>

## Test the project

To run tests:

1. Use a command window to change into the example project directory, for example: `cd as24reporting`

2. Enter: `sbt test` to run all tests

3. After the execution of the test console will display the results




