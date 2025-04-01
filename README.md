# RollenXD - Music Sharing and Listening Platform

RollenXD is a full-stack music sharing and listening platform. Users can upload, and listen to music, create playlists, and comment on songs. The frontend is built with React.js (Vite), while the backend is developed using Spring Boot with PostgreSQL as the database.

## Table of Contents
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation (Manual Setup)](#installation-manual-setup)
  - [Database Setup](#database-setup)
  - [Running with Docker](#running-with-docker)
  - [Running Tests](#running-tests)
- [Contributing](#contributing)
- [License](#license)

---

## Technologies Used

### Frontend
- ![React](https://img.shields.io/badge/React-20232A?style=flat&logo=react&logoColor=61DAFB) React.js (Vite)
- ![TailwindCSS](https://img.shields.io/badge/TailwindCSS-38B2AC?style=flat&logo=tailwind-css&logoColor=white) Tailwind CSS
- ![Axios](https://img.shields.io/badge/Axios-671DDF?style=flat) Axios
- ![React Query](https://img.shields.io/badge/React_Query-FF4154?style=flat) React Query (useQuery)
- ![Audio Player](https://img.shields.io/badge/Audio_Player-2C8EBB?style=flat) React H5 Audio Player

### Backend
- ![Java](https://img.shields.io/badge/Java_21-ED8B00?style=flat&logo=openjdk&logoColor=white) Java 21 (Spring Boot)
- ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat&logo=spring-boot&logoColor=white) Spring Boot
- ![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=flat) Spring Data JPA (Hibernate)
- ![JWT](https://img.shields.io/badge/JWT-000000?style=flat&logo=JSON%20web%20tokens) JWT Authentication
- ![Lombok](https://img.shields.io/badge/Lombok-CA4245?style=flat) Lombok
- ![Apache Tika](https://img.shields.io/badge/Apache_Tika-EC2025?style=flat) Apache Tika (for extracting song metadata)
- ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=flat&logo=postgresql&logoColor=white) PostgreSQL
- ![JUnit](https://img.shields.io/badge/JUnit-25A162?style=flat) JUnit, Testcontainers, Mockito, H2 (for testing)

### End-to-End Testing
- ![Java](https://img.shields.io/badge/Java_23-ED8B00?style=flat&logo=openjdk&logoColor=white) Java 23
- ![Selenium](https://img.shields.io/badge/Selenium-43B02A?style=flat&logo=selenium&logoColor=white) Selenium
- ![JUnit](https://img.shields.io/badge/JUnit_5-25A162?style=flat) JUnit 5

---

## Getting Started

### Prerequisites

Ensure you have the following installed on your system:

- [Node.js](https://nodejs.org/) (for frontend)
- [Java 21](https://adoptium.net/) (for backend)
- [PostgreSQL](https://www.postgresql.org/) (for database)
- [Docker](https://www.docker.com/) (optional for containerized setup)

---

## Installation (Manual Setup)

### 1. Clone the Repository
```sh
git clone https://github.com/bothbartos/rollenXd.git
cd RollenXD
```

### 2. Setup the Backend

#### a) Navigate to the backend folder:
```sh
cd backend
```

#### b) Set up environment variables:
Set the following environment variables in your system (or in IntelliJ Run/Debug configurations menu):
```sh
DATABASE_URL=your_database_url
DATABASE_USERNAME=your_database_username
DATABASE_PASSWORD=your_database_password
```

#### c) Run the backend:
```sh
./mvnw spring-boot:run
```

---

### 3. Setup the Frontend

#### a) Navigate to the frontend folder:
```sh
cd frontend
```

#### b) Install dependencies:
```sh
npm install
```

#### c) Start the frontend server:
```sh
npm run dev
```

---

## Database Setup

### 1. Create an Empty PostgreSQL Database

Before running the backend, you need to create an empty PostgreSQL database:

#### a) Open PostgreSQL and connect to your instance:
```sh
psql -U your_database_username
```

#### b) Create the database:
```sql
CREATE DATABASE rollenxd;
```

#### c) Connect to the new database:
```sql
\c rollenxd;
```

Your database is now ready, and Hibernate will automatically create the necessary tables.

### 2. Populate the Database (Optional, for Reset & Testing)
```sh
cd backend/populate
python create_table.py   # Creates or resets the tables
python populate.py       # Populates the database with sample data
```

---

## Running with Docker
```sh
docker-compose up --build
```
To stop the running containers:
```sh
docker-compose down
```

---

## Running Tests

### Backend Unit & Integration Tests
```sh
cd backend
./mvnw test
```

### End-to-End Tests (Selenium)
```sh
cd E2E-Test-RollenXD
mvn test
```

### Running Test Mode Application for E2E Tests
```sh
cd backend
./mvnw test -Dtest=ZsomlexdApplicationTestMode
```

Ensure the backend and frontend are running before executing tests.


