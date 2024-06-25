# Traffic Info System

The `Traffic Info System` is a public transportation information system developed using Java and Spring Boot. The primary goal of this project is to provide a convenient way for users to access real-time public transportation operation information, including but not limited to the status of trains, buses, and subways.

**API**: This system utilizes the [ODPT (Open Data for Public Transportation in Tokyo)](https://developer.odpt.org/ja/info) API to fetch open data for public transportation in the Tokyo area. 

## Backend Technology Stack

1. Java: A widely used programming language, known for its object-oriented features and cross-platform capabilities.
2. Spring Boot: An open-source Java framework for creating standalone, production-grade Spring applications. It aims to simplify the initial setup and development process of Spring applications.
3. Maven: A project management and comprehension tool. It provides developers with a complete lifecycle framework. Development teams can automatically complete the basic tool setup of the project, create a new project, or change the structure of an existing project.
4. Aspect-Oriented Programming (AOP): A programming paradigm that aims to improve modular programming, especially the separation of concerns.
5. MongoDB: A source-available cross-platform document-oriented database program. Classified as a NoSQL database program, MongoDB uses JSON-like documents with optional schemas.
6. JWT (JSON Web Token): A way to transmit claims as a JSON object over the network. These claims can be used to verify user identity.
7. Lombok: A Java library that can automatically insert into editors and build tools, providing a way to make Java code more concise, especially for simple Java Beans.
8. SLF4J (Simple Logging Facade for Java): A simple unified interface provided for various logging APIs, allowing end users to use their desired logging APIs implementation when deploying applications.

## Features

1. Encrypt user passwords before saving them in the database to enhance security.
   - We use a robust encryption algorithm to protect user passwords. When a user registers or changes their password, we first encrypt the password, then save the encrypted password in the database. This way, even if the database is compromised, attackers cannot directly access the user's original password, thereby enhancing system security.
   - ![6d8d6c0544da928613179332b24d82b7](https://github.com/Athash1/traffic-demo/assets/125422104/7a339661-69f3-43b2-9b24-17fe23cf89a0)


2. Track the number of times a user uses the service and generate charts in MongoDB Charts.
   - We record information every time a user uses the service and save this information in the database. We then use MongoDB Charts to generate charts from this data. These charts clearly show the frequency and timing of service usage by users. This is very helpful for understanding user habits and optimizing the way we provide services.
   - ![f8814326505aadd6c77070a61cbbf9ed](https://github.com/Athash1/traffic-demo/assets/125422104/8545512d-508e-4e08-8bc4-059c099044d6)

