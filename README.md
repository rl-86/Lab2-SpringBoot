# Lab2 Spring Boot Rest API with OAuth2 security and MYSQL database
To run this project, you will need to follow these steps:
1. Clone the repository
2. Run:
````
docker compose up -d
````
5. Start application, run: Lab2SpringbootApplication

8. Test the API using Postman, Insomnia or any other tool.

### Authentication Endpoints

POST http://localhost:8080/auth/register:
````
{
  "username": "testuser1",
  "password": "password123"
}
````
or for admin:

````
{
  "username": "admin2",
  "password": "admin123"
}
````
POST http://localhost:8080/auth/login:

````
{
  "username": "testuser1",
  "password": "password123"
}
````
Returns JWT token:
````
{
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
````
### Category Endpoints
GET http://localhost:8080/categories

Returns list of all categories (no authentication required).

POST http://localhost:8080/categories (Admin only - requires JWT token in header)
````
Authorization: Bearer YOUR_JWT_TOKEN_HERE
````
````
{
    "name": "Sevärdheter",
    "symbol": "🏛️",
    "description": "Turistattraktioner och sevärdheter"
}
````

### Place Endpoints
GET http://localhost:8080/places

Returns all public places + your own private places
Optional query parameters:
lat=59.3293 - latitude for geographic search
lon=18.0686 - longitude for geographic search
radius=1000 - search radius in meters

POST http://localhost:8080/places (requires JWT token).
````
Authorization: Bearer YOUR_JWT_TOKEN_HERE
````
````
{
  "name": "Awesome Café",
  "description": "Great coffee and pastries",
  "latitude": 59.3293,
  "longitude": 18.0686,
  "categoryId": 1,
  "isPublic": true
}
````

PUT http://localhost:8080/places/{id} (requires JWT token, can only update your own places)
Headers:
````
Authorization: Bearer YOUR_JWT_TOKEN_HERE
{
  "name": "Updated Café Name",
  "description": "Updated description",
  "latitude": 59.3300,
  "longitude": 18.0700,
  "categoryId": 2,
  "isPublic": false
}
````


DELETE http://localhost:8080/places/{id} (requires JWT token, can only delete your own places)
````
Authorization: Bearer YOUR_JWT_TOKEN_HERE
````

### User Roles
USER: Can create, read, update and delete their own places
ADMIN: Can create categories + all USER permissions
Notes
All authenticated requests require the JWT token in the Authorization header
Private places are only visible to their owners
Public places are visible to everyone
Geographic search finds places within specified radius

