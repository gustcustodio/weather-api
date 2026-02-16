# Weather API Wrapper Service

This project is a RESTful API built with Spring Boot that serves as a wrapper for the Visual Crossing Weather API. It was developed as part of the [roadmap.sh](https://roadmap.sh/projects/weather-api-wrapper-service) projects.
## Features
- **Fetch Weather Data**: Get current weather conditions for any city.
- **Redis Caching**: Automatically caches weather results for 12 hours to improve performance and reduce API costs.
- **Global Error Handling**: Provides clear and meaningful error messages for various failure scenarios.
- **JSON Serialization**: Configured to store readable JSON data in the Redis instance.
## Technologies

- **Java 21**
- **Spring Boot 3**
- **Spring Data Redis**
- **Docker** (for Redis containerization)
- **RestTemplate** (for external API communication)
## Configuration
Before running the application, ensure you have a Redis instance available and an API key from [Visual Crossing’s API](https://www.visualcrossing.com/weather-api/).

1. **Start Redis**:
   Bash
```
docker run --name weather-redis -p 6379:6379 -d redis
```
2. **Update application.properties**:
   Properties
```
api.weather.key=your_api_key_here
spring.data.redis.host=localhost
spring.data.redis.port=6379
```
## API Endpoints

### Get Weather

- **URL**: `/weather/{city}`
- **Method**: `GET`
- **Success Response**:
    - **Code**: 200 OK
    - **Content**:
      JSON

 ```bash
   {
	    "city": "London",
	    "temp": 12.5,
	    "description": "Partially cloudy"
     }
 ```

### Error Responses

The API uses a Global Exception Handler to return standardized errors:

- **400 Bad Request**: Invalid city name or parameters.
- **401 Unauthorized**: Invalid API Key.
- **429 Too Many Requests**: API rate limit exceeded.
- **500 Internal Server Error**: External API or Redis connection issues.
## Caching Strategy
The application follows the **Cache-Aside** pattern:

1. Check if the weather data for the requested city exists in Redis.
2. If found (Cache Hit), return the data immediately.
3. If not found (Cache Miss), fetch data from the Visual Crossing API.
4. Store the result in Redis with a TTL (Time To Live) of 12 hours.
## Running the Project

1. Clone the repository.
2. Ensure Docker and Redis are running.
3. Execute the Spring Boot application.