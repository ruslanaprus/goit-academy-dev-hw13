# Travel Management System

The Travel Management System is a Java-based web application that manages client and planet records using PostgreSQL database. It uses Hibernate ORM for data access, Flyway for database migration, and Thymeleaf for rendering HTML templates. The primary purpose of this project is to provide a backend service for managing clients and planets, allowing CRUD operations through HTTP requests.

## Table of Contents
1. [Project Structure](#project-structure)
2. [Tools and Libraries](#tools-and-libraries)
3. [Functionalities](#functionalities)
3. [Setup Instructions](#setup-instructions)
4. [Usage Examples](#usage-examples)

## Project Structure

The project is structured as follows:

- **`src/main/java/org/example/`**: Contains the Java source code.
    -  **`config/`**: Configuration classes for Hibernate (`HibernateConfig.java`) and Thymeleaf (`TemplateConfig.java`).
    - **`model/`**: Entity classes (`Client.java`, `Planet.java`) representing the database tables.
    - **`dao/`**: Contains the `GenericDao` class for common database operations.
    - **`service/`**: Business logic classes (`ClientService.java`, `PlanetService.java`) to handle client and planet operations.
    - **`controller/`**: Servlet classes (`TravelServlet.java`, `PlanetServlet.java`) that handle HTTP requests and map them to appropriate service methods.
- **`src/main/resources/`**: Contains the database migration scripts in the `db/migration` folder. Contains templates used by Thymeleaf for rendering HTML pages in the `templates` folder.
- **`build.gradle`**: Configuration file for Gradle build and dependencies.
- **`hibernate.properties`**: Contains Hibernate configuration properties.

## Tools and Libraries

- **Java 21**: Core programming language for the application.
- **Hibernate ORM**: For object-relational mapping and database interactions.
- **HikariCP**: A high-performance JDBC connection pool.
- **Flyway**: Database migration tool, configured for PostgreSQL.
- **Thymeleaf**: For rendering dynamic HTML content.
- **Jakarta Servlet & JSP API**: For building servlets and JSPs.
- **Tomcat 10**: A servlet container to deploy and run the web application.
- **PostgreSQL**: Database for storing client and planet data.
- **SLF4J and Logback**: For logging.
- **Jackson**: For JSON parsing in request payloads.
- **Lombok**: To reduce boilerplate code (e.g., getters, setters).
- **Gradle**: Used for dependency management and building the project as a WAR (Web Application Archive) file.
- **Docker**: To build the application container for easy deployment.
## Functionalities

1. **Client Management**:
    - Create, read, update, and delete clients.
    - Search for a client by ID.
    - Retrieve a list of all clients.
2. **Planet Management**:
    - Create, read, update, and delete planets.
    - Search for a planet by ID.
    - Retrieve a list of all planets.
3. **Database Integration**:
    - Uses Hibernate ORM to interact with PostgreSQL database.
    - Flyway is used for version control and managing database migrations.
4. **HTML Templating**:
    - Uses Thymeleaf templates for rendering client and planet management forms and views.

## Setup Instructions

### Prerequisites

- **Java 21** installed on your machine.
- **Gradle** installed or use the provided Gradle wrapper (`gradlew`).
- **PostgreSQL** Database
- A **Java servlet container** like Apache Tomcat 10.

### Setup and Configuration

1. **Clone the repository**:
```shell
git clone git@github.com:ruslanaprus/goit-academy-dev-hw12.git
cd goit-academy-dev-hw12
```
2. **Database Configuration**: Copy the `.env.example` file into `.env`, and populate it with your DB details (you might need to change the GOIT_DB_URL if yours is different).


3. You will also need to copy your database credentials into either:
    1. Your environment variables on your computer (keys: [GOIT_DB_URL, GOIT_DB_USER, GOIT_DB_PASS])
    2. The `build.gradle` file in the flyway section


4. **Run Flyway Migration**: To apply database migrations, run:
```shell
gradle flywayMigrate
```

5. **Build the Project**: Compile the project and generate a WAR file:
```
./gradlew build
```
6. **Deploy the WAR File**:

Build the docker image
```shell
docker build -t travel-servlet:1.0 .
```

Once the image is built, run the image (Note this will use the .env file we setup earlier):
```shell
docker run -d -p 8080:8080 --name travel-servlet --env-file .env travel-servlet:1.0
```

7. You can then visit the website at http://localhost:8080/ 

Other:

docker PS
```shell
docker ps
```

docker STOP RM
```shell
docker stop travel-servlet && docker rm travel-servlet
```

LOGS
```bash
docker logs travel-servlet
```

## Usage Examples:
**Client Operations**:

- Create a new client: Navigate to `/createClientForm` and fill out the form.
- Find a client by ID: Use the form at `/findClientByIdForm` and submit the ID.
- Update a client: Use the form at `/updateClientForm` with the client ID and new details.
- Delete a client: Use the form at `/deleteClientByIdForm` and submit the ID.
- List all clients: Access the page at `/findAllClients`.

**Planet Operations**:

- Create a new planet: Navigate to `/createPlanetForm` and fill out the form.
- Find a planet by ID: Use the form at `/findPlanetByIdForm` and submit the ID.
- Update a planet: Use the form at `/updatePlanetForm` with the planet ID and new details.
- Delete a planet: Use the form at `/deletePlanetByIdForm` and submit the ID.
- List all planets: Access the page at `/findAllPlanets`.