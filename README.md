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

