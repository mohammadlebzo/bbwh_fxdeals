# ClusterData Warehouse

Suppose you are part of a scrum team developing data warehouse for Bloomberg to analyze FX deals. One of customer stories is to accept deals details from and presist them into DB

## Request logic as following:

- Request Fields(Deal Unique Id, From Currency ISO Code "Ordering Currency", To Currency ISO Code, Deal timestamp, Deal Amount in ordering currency).
- Validate row structure.(e.g: Missing fields, Type format..etc. We do not expect you to cover all possible cases but we'll look to how you'll implement validations)
- System should not import same request twice.
- No rollback allowed, what every rows imported should be saved in DB.

## Deliverables should be ready to work including:

- Use Actual Db, you can select between (Postgres, MySql or MongoDB)
- Workable deployment including sample file (Docker Compose).
- Maven or Gradle project is required for full source code.
- Proper error/exception handling.
- Proper Logging.
- Proper unit testing with respected Coverage.
- Proper documentation using md.
- Delivered over Githhub.com.
- Makefile to streamline running application (plus).

**Note**: The requirement of "No rollback allowed" was not met, and somethings might be found lacking as working 9+ hrs + having 2 days for an assignement proved to not be a great combo.

## Prerequisites

Ensure you have the following installed on your machine:

- Docker
- Docker Compose

#### Make sure Docker Compose is installed on your machine. Use the following command to start the application:

`MYSQL_DATABASE=deals MYSQL_PASSWORD=raven docker-compose up`

## Testing the Application

**Send a POST Request to Create a Deal**

- Set the request type to `POST`.
- Enter the endpoint URL: `http://localhost:8080/add-deals`.
- In the request body tab, select `raw` and choose `JSON` from the dropdown.
- Paste the following JSON request example:

  ```json
  {
    "id": 621115,
    "fromCurrency": "JOD",
    "toCurrency": "EUR",
    "timestamp": "2024-06-25T10:30:00",
    "amount": 621
  }
  ```
