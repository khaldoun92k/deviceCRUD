# Device CRUD Application

This is a Spring Boot application for managing devices;

Simple Crud and search by brand feature exposed via REST Controller.

## Dependencies

To run this application, the following dependencies are required:

- **Java 23** (the application is built using JDK 23)
- **Maven** 3.9.9 (for building the project)
- **Docker** 4.34.3 (for containerization)

The application uses the following dependencies:

- **Spring Boot** (version 3.4.1)
- **Spring Boot Starter Data JPA** (for interacting with databases)
- **H2 Database** (for runtime database storage)
- **Spring Boot Starter Hateoas** (for HATEOAS support)
- **Spring Boot Starter Web** (for building web applications)
- **JUnit Jupiter API** (for unit testing)

## Prerequisites

Before running the application, ensure the following are installed on your machine:

- **Docker**: Ensure Docker is installed and running.
- **Maven**: Maven is used for building.

## Building and Running with Docker

Follow these steps to build and run the application using Docker.

Build the Docker image
docker build -t device-app .

```
docker build -t device-app .
```

Run the Docker container

```
docker run -p 8080:8080 device-app
```

## Endpoints

| **Method** | **Endpoint**          | **Description**                | **Request Body**                                                                          | **Response**                              |
|------------|-----------------------|--------------------------------|-------------------------------------------------------------------------------------------|-------------------------------------------|
| **POST**   | `/api/devices`        | Save a new device              | `{ "name": "Device Name", "brand": "Brand", "specifications": "Specs" }`                  | `201 Created`: Saved device.              |
| **GET**    | `/api/devices/{id}`   | Get a device by ID             | N/A                                                                                       | `200 OK`: Device details, `404 Not Found` |
| **GET**    | `/api/devices`        | Get all devices                | N/A                                                                                       | `200 OK`: List of devices.                |
| **PUT**    | `/api/devices/{id}`   | Update all details of a device | `{ "name": "Updated Name", "brand": "Updated Brand", "specifications": "Updated Specs" }` | `200 OK`: Updated device.                 |
| **PATCH**  | `/api/devices/{id}`   | Partially update a device      | `{ "field": "value" }`                                                                    | `200 OK`: Updated device.                 |
| **DELETE** | `/api/devices/{id}`   | Delete a device by ID          | N/A                                                                                       | `204 No Content`: Deleted.                |
| **GET**    | `/api/devices/search` | Search devices by brand        | Query param: `?brand=Brand`                                                               | `200 OK`: List of devices.                |


### Access
Accessing the API via curl commands or any API Client (Postman...)