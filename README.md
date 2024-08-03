# Price Comparison
> A RESTful API built with Spring Boot for comparing product prices from various e-commerce websites.

## Table of Contents
* [General Info](#general-information)
* [Technologies Used](#technologies-used)
* [Features](#features)
* [Testing](#testing)


## General Information
This project is designed as a RESTful API that enables comparing prices across various e-commerce websites, incorporating user authentication, web scraping, and historical price analysis.

## Technologies Used
- Java programming language
- Spring Boot
- Spring Data JPA
- Spring Security
- Jsoup
- PostgreSQL
- Maven
- JUnit

## Features
- User Authentication and Authorization:
  - Secure login and registration processes
  - JWT-based authentication
  - Email confirmation with verification links
- Price Comparison:
  - Connecting to external APIs for real-time pricing data
  - Web scraping functionality to gather product pricing information
- Reporting:
  - Generating reports illustrating historical price fluctuations
  - Excel chart generation for visual representation of data

## Testing
The application includes comprehensive unit and integration tests designed to validate functionality and ensure performance.

Key aspects of testing include:
- Unit Tests: Focused on individual components and methods to ensure they perform as expected.
- Integration Tests: Validate the interaction between different modules and external services, ensuring that the entire system works cohesively.
