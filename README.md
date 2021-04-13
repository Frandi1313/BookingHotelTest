# Tech stack

    - Java 8
    - Spring boot 
    - Maven 4.0.0
    - Lombok
    - H2

# Shortcut Explication:

- Spring boot is a framework that helps by accelerating the application
  development. [see](https://spring.io/guides/gs/spring-boot/)
- Lombok is a java library that automatically plugs into your editor and build tools, spicing up your java.
  [see](https://projectlombok.org)

# Challenge

Post-Covid scenario:
People are now free to travel everywhere but because of the pandemic, a lot of hotels went bankrupt. Some former famous
travel places are left with only one hotel. You’ve been given the responsibility to develop a booking API for the very
last hotel in Cancun.

The requirements are:
- API will be maintained by the hotel’s IT department.
- As it’s the very last hotel, the quality of service must be 99.99 to 100%
- For the purpose of the test, we assume the hotel has only one room available
- To give a chance to everyone to book the room, the stay can’t be longer than 3 days and can’t be reserved more than 30
  days in advance.
- All reservations start at least the next day of booking,
- To simplify the use case, a “DAY’ in the hotel room starts from 00:00 to 23:59:59.
- Every end-user can check the room availability, place a reservation, cancel it or modify it.
- To simplify the API is insecure.

# IT Team Instructions

This application should be deployed into a cloud repository such as AWS, AZURE, GOOGLE CLOUD. This provides a console
that allows you to maintain easily the application and allows you to escalate the application in certain moment.

You can choose whatever database, in my case to simplify it was an H2.

# Explanation

For this first version it was created 2 main entities, the first one is "Customer", and the second one is "Booking"

The Customer entity has these fields:

- id
- name
- cif (national document number, passport id, driver licence id, ...)

The customer controller maps a GET and a POST endpoint.

The Booking entity has these fields:

- id
- initialDate (LocalDate with format YYYY-MM-dd)
- finalDate (LocalDate with format YYYY-MM-dd)
- customerId
- isActive (boolean that is truen when is active and false when is cancelled)

For the BookingService there are CRUD methods and an extra method to know if the hotel is already booked for the
requested time period.

# API

## - Customer

The classic behaviour is to create a customer entity first and then create the booking entity for that user. In this
case it was created 2 separated endpoints in order to make it restFull.

### POST

    URL: {{routePath}}/customer
    BODY:
      {
        "customerName": "MyName",
        "customerCif" : "MyPersonalNumber" ==> (passport, ID,...)
      }

#### OK cases:

    201 created 
      { 
        "id": "ApplicationInternalID", 
        "customerName": "MyName", 
        "customerCif" : "myPersonalNumber"
      }

#### NOK Cases

    400 bad request when some field is null

### GET

    URL: {{routePath}}/customer/{customerId}

#### OK cases:

    200 OK
    {
      "id": "personId", 
      "customerName": "name", 
      "customerCif" : "cif"
    }

    404 Not found when the customer not exists

## - Booking

### POST

    URL: {{routePath}}/customer/{customerId}/booking 
    Body: 
    { 
      "initialDate": "2021-04-15", 
      "finalDate" : "2021-04-16" 
    }

##### OK cases:

    201 created 
    { 
      "bookingId": "2", 
      "customerId": "1", 
      "initialDate": "2021-04-15", 
      "finalDate" : "2021-04-16" 
    }

##### NOK Cases
    400 bad request when some field is null
    409 when the dates for the booking are busy

### GET

    URL: {{routePath}}/customer/{customerId}/booking/{bookingId} 

##### OK cases:

    200 OK 
      { 
        "bookingId": "2", 
        "customerId": "2", 
        "initialDate": "2021-04-15", 
        "finalDate" : "2021-04-15" 
      }

##### NOK cases:

    404 Not found when the booking don't exist

### PUT

    URL: {{routePath}}/customer/{customerId}/booking/{bookingId} 
    Body: 
    {
      "initialDate": "2021-04-15", 
      "finalDate" : "2021-04-15"
    }

##### OK cases:

    204 NO CONTENT

##### NOK cases:

    409 CONFLICT when the room is already booked for the new dates

### DELETE

    URL: {{routePath}}/customer/{customerId}/booking/{bookingId}

##### OK cases:

    200 OK

##### NOK cases:

    404 NOT FOUND when the booking don't exists

# - Hotel Available API

### GET

    URL:{{routePath}}/availability/check/{initialDate}/{finalDate}

##### OK cases:

    200 OK

##### NOK cases:

    404 NOT FOUND when the hotel is already booked 

# IntegrationTest

There are some integration test with the main test cases.

# How to run?

`./mvnw spring-boot:run`

# Extra

There are in the project a postman collection to help you testing the application.

`Hotel Booking.postman_collection.json`

You can import into your postman app and lunch it