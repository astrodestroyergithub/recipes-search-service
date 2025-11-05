# Recipes Search Service

## Overview
The Recipes Search Service is a robust Spring Boot-based REST API that provides a comprehensive solution for managing and searching recipes. The service allows users to store, retrieve, and search for recipes using various criteria such as ingredients, cuisine type, and cooking time. It features full-text search capabilities powered by Hibernate Search with Lucene, making it efficient to search through recipe content. The service also integrates with external recipe APIs to provide a rich database of recipes while maintaining them in a local H2 database for quick access.

## Features
- Full-text search capabilities for recipes
- CRUD operations for recipe management
- Integration with external recipe APIs
- Automatic recipe indexing
- RESTful API endpoints with OpenAPI/Swagger documentation
- In-memory H2 database for development
- Actuator endpoints for monitoring

## Technology Stack
- Java 21
- Spring Boot 2.7.18
- Spring Data JPA
- Hibernate Search 6.2.2.Final
- H2 Database
- Lombok
- SpringDoc OpenAPI UI
- Maven

## Prerequisites
Before you begin, ensure you have the following installed:
- JDK 21
- Maven 3.6+ or use the included Maven wrapper
- Git

## Local Setup

1. Clone the repository:
```bash
git clone https://github.com/astrodestroyergithub/recipes-search-service.git
cd recipes-search-service
```

2. Build the project:
```bash
# Using Maven wrapper (recommended)
./mvnw clean install

# Or using Maven if installed globally
mvn clean install
```

3. Run the application:
```bash
./mvnw spring-boot:run
```

The server will start on port 8080 by default.

## API Documentation
Once the application is running, you can access the API documentation at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Database
The application uses H2 in-memory database by default. You can access the H2 console at:
- H2 Console: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:recipesdb
- Username: sa
- Password: (leave empty)

## Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/homosapiens/recipes/recipessearchservice/
│   │       ├── config/        # Configuration classes
│   │       ├── controller/    # REST controllers
│   │       ├── model/         # Domain models
│   │       ├── repository/    # Data repositories
│   │       ├── service/       # Business logic
│   │       └── exception/     # Exception handlers
│   └── resources/
│       └── application.properties  # Application configuration
└── test/
    └── java/                  # Test classes
```

## Key Dependencies
The project uses the following key dependencies:
```xml
<!-- Spring Boot Starters -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<!-- Database -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Search Engine -->
<dependency>
    <groupId>org.hibernate.search</groupId>
    <artifactId>hibernate-search-mapper-orm</artifactId>
    <version>${hibernate.search.version}</version>
</dependency>

<!-- API Documentation -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>${springdoc.version}</version>
</dependency>
```

## Available Endpoints
- `GET /api/recipes` - Get all recipes
- `GET /api/recipes/{id}` - Get a specific recipe
- `POST /api/recipes` - Create a new recipe
- `PUT /api/recipes/{id}` - Update an existing recipe
- `DELETE /api/recipes/{id}` - Delete a recipe
- `GET /api/recipes/search` - Search recipes using various criteria

## Health Check
The application includes Spring Boot Actuator endpoints for monitoring:
- Health check: http://localhost:8080/actuator/health
- Info: http://localhost:8080/actuator/info

## Contributing
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License
This project is licensed under the MIT License - see the LICENSE file for details.