# Shared AOP Logging Configuration

As part of **Developer 3's Phase 4**, the AOP logging configuration has been extracted into a shared library called `common-logging`. This ensures that all microservices have consistent logging for method execution times, request bodies, and responses without code duplication.

## How to add AOP Logging to your microservice:

**Step 1:** Include the dependency in your microservice's `pom.xml`:

```xml
<dependency>
    <groupId>com.petadopt</groupId>
    <artifactId>common-logging</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

**Step 2:** Ensure your package names match the pointcut!
The shared aspect automatically triggers on any public method within packages ending in `.controllers` or `.services` that start with `com.petadopt`. For example:
*   `com.petadopt.authservice.controllers.AuthController`
*   `com.petadopt.adoptionservice.services.AdoptionService`

That's it! Because the shared library uses Spring Boot AutoConfiguration (`AutoConfiguration.imports`), the `@Aspect` is automatically registered as a bean in your microservice as soon as you include the dependency.

### How to Build
If you are running the project locally through IntelliJ or Eclipse, make sure to load the aggregator POM (`backend/pom.xml`) or run `mvn install` on the `common-logging` project so your local `.m2` repository has the jar file available for the other microservices.
