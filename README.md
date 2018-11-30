<!--
       Copyright 2017 IBM Corp All Rights Reserved

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
-->


__-------------------------------------------------------------------------------__

__WORK IN PROGRESS__

*Note:* This is a work in progress Spring port of the orginal portfolio service. 

__-------------------------------------------------------------------------------__

This service manages a *stock portfolio*.  The data is backed by two **DB2** tables, communicated with
via *JDBC*.  The following operations are available:

`GET /` - [gets summary data for all portfolios](docs/getPorfolios.md)

`POST /{owner}` - [creates a new portfolio for the specified owner](docs/createPortfolio.md)

`GET /{owner}` - [gets details for the specified owner](docs/getFullPortfolio.md)

`PUT /{owner}` - [updates the portfolio for the specified owner (by adding a stock)](docs/updatePortfolio.md)

`DELETE /{owner}` - [removes the portfolio for the specified owner](docs/deletePortfolio.md)

`POST /{owner}/feedback` - [submits feedback (to the Watson Tone Analyzer)](docs/submitFeedback.md)

## Building

### Adding db2jcc4.jar to the project

The DB2 JDBC Driver (JCC) is sadly not available in Maven, so you have to download it directly from from IBM, and add it to a local maven repository. 

- Select and download the appropriate driver archive from http://www-01.ibm.com/support/docview.wss?uid=swg21363866
- Unpack the archive, and save the `db2jcc4.jar` file to new `lib` directory on the root of
 your project
- Add the Jar as a Maven package from the `lib` directory (change the version appropriately with the archive you downloaded)
  ```
  mvn install:install-file -DlocalRepositoryPath=lib -DcreateChecksum=true -Dpackaging=jar -Dfile=./lib/db2jcc4.jar -DgroupId=com.ibm.db2.jcc -DartifactId=db2jcc4 -Dversion=4.24.92
  ```

The pom.xml is already configured to expect to find the db2 jar in that location.

### Building

`mvn package` Will produce an executable jar in the target folder.

`mvn package boost:docker-build` Will create a docker image `portfolio-spring:latest` that uses OpenLiberty to execute the application.

## Configuration

The application/container expects the following environment variables to be populated and carry configuration forthe application.

| Env Var | Purpose |
|---------|---------|
|`MQ_QUEUE_MANAGER` | The queue manager to connect to. eg. `QM1`|
|`MQ_CHANNEL` | The channel to connect to. eg `DEV.ADMIN.SVRCONN`|
|`MQ_HOST` | The host for MQ.|
|`MQ_PORT` | The port for MQ.|
|`MQ_ID` | The userid for MQ.|
|`MQ_PASSWORD` | The password for MQ.|
|`LOYALTY_URL` | The url of the loyalty service, eg. `http://192.168.18.100:31422/DecisionService/rest/v1/ICP_Trader_Dev_1/determineLoyalty` |
|`LOYALTY_ID` (or `ODM_ID`)| The user id for the loyalty service|
|`LOYALTY_PWD` (or `ODM_PWD`)| The password for the loyalty service|
|`JDBC_HOST` | The host for the jdbc connection |
|`JDBC_PORT` | The port for the jdbc connection | 
|`JDBC_ID` | The userid for the jdbc connection |
|`JDBC_PASSwORD` | The password for the jdbc connection |
|`JDBC_DB` | The database to user with the jdbc connection | 
|`WATSON_APIKEY` (formerly `WATSON_ID` and `WATSON_PWD`)| The api key used to access Watson Tone Analyzer service |
|`WATSON_URL` | Url for Watson Tone Analyzer service |
|`STOCK_QUOTE_URL` | Url for Stock Quote service | 
|`JWT_AUDIENCE` | The expected audience for jwt authentication |
|`JWT_ISSUER` | The expected isser for jwt authentication | 
|`JWT_KEY` (note : *temporary*) | shared key for jwt authentication |

