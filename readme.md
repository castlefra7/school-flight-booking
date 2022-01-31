# Simple Flight booking system
## Context
At our university there is a special assessment that we shall pass before we can start an internship. The assessment last around two (2) to three (3) days during which students are given a project to work on. Students are free to choose whatever technology stack suits best to them, the only constraint is that it shall be a web technology stack. 
The assessment's goal is to check students'knowledge and skills if they are able to start tackling real world projects. Besides, it helps students to gain confidence with their capabilities. Indeed students tackle every part of the project, from the user interface development to the database design. 
Students are also required to provide a to-do list so that:
- they have an estimate for the project 
- they know the actual hours they worked on it and
- they can keep track of their progress

Hope you can find something for you here.
Enjoy.

## About the project
This project is a simple flight booking system. Our teacher gave us this project to prepare us for the real assessment. Customer can search for a flight based on its criteria and choose the flights that best suits them. The administrator user can see an interface where information about the flights are displayed.

## Technology
- Java
    - A general-purpose programming language. A popular object oriented language amoung big companies. Object oriented is a very important concept for our source code to be more maintainable and readable.
- Spring Boot Framework
    - This is a powerful framework developed with Java that let us build industrial-grade software. It allows software developers to delegate many repetitive and daunting tasks to the framework and thus focus more on the most critical and important parts of a project. 
- Thymeleaf
    - A popular template engine that works seamlessly with Spring Framework. It offers a wide range of useful tags.
- PostgreSQL
    - A robust relational database management. There is no specific reason to why choosing this for this particular project but to get used to working with it.
- JavaScript:
    - It is a programming language that we can rarely avoid as a web developer.

## How to run
1. Clone this repository
2. Change PostgreSQL credentials in ** ConnGen ** class
3. Executes the script in a PostgreSQL Database
4. Open a command line and executes the following command inside this repository: ** mvnw spring-boot:run **

## User Interface
> "Quick start a project by designing its UI first"

Here are the most important user interfaces of this project.

- Flights Search: It is the main page of this project. Here customer can look for available flights for specific dates and towns.
![Flights search](/docs/ui_images/flights_search.PNG)

- Choosing desired flights: Here the customer can choose one flight among the one-way flights and another one among the return flights. The customer can also see the total price of the flights.
![Choosing flights 1](/docs/ui_images/choose_flight_1.PNG)
![Choosing flights 2](/docs/ui_images/choose_flight_2.PNG)

- Passenger details: When customer clicks on the booking button, he needs to fill all passenger details
![Passenger details](/docs/ui_images/passenger_details.PNG)

- Buyer details: This form is filled with the one who is gonna buy the tickets
![Buyer details](/docs/ui_images/buyer_details.PNG)

- Booking success: We show a message to the customer that the booking was successful and display the booking code for the payments.
![Booking success](/docs/ui_images/booking_success.PNG)

- Payment: Here the customer can proceed to the payment
  - One-way flight payment
  ![Payment one-way](/docs/ui_images/payment_flight_go.PNG)

  - return flight payment
  ![Payment return](/docs/ui_images/payment_flight_back.PNG)

- Tickets: After the payment, the customer can get their tickets
  - One-way flight tickets
  ![One-way ticket 1](/docs/ui_images/tickets_go_1.PNG)
  ![One-way ticket 2](/docs/ui_images/tickets_go_1.PNG)
  - Return flight tickets
  ![Return ticket 1](/docs/ui_images/tickets_back_1.PNG)
  ![Return ticket 2](/docs/ui_images/tickets_back_1.PNG)

- Pricings: All the flight pricings
![Pricings](/docs/ui_images/pricings.PNG)

- Statistics: It is the daily payments statistics
![Statistic](/docs/ui_images/statistic_payments.PNG)

- Crud flights
![Crud flights](/docs/ui_images/crud_flights.PNG)

- Flights informations
  - "Chaises total" means Total seats
  - "Chaises restantes" means Available seats
  ![Flights list](/docs/ui_images/list_flights_admin.PNG)
  - Here is the list of passengers of one specific flight
  ![Flight passengers](/docs/ui_images/flight_details_admin.PNG)

- Crud users
![Crud users](/docs/ui_images/crud_users.PNG)

## Database Schema
> "Simplify your life as a developer by spending more time designing your database"

As much as we can, we should put effort to design the database to avoid unnecessary lengthy code at the application layer of our software.

![Conceptual Data Model](/docs/ui_images/cdm.png)

## Other documents
- To-do list: /docs/compagnie_aerienne.ods (in French)

## Keywords