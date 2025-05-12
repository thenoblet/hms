# Hospital Information System

```mermaid
erDiagram
    Hospital ||--o{ Department : has
    Department ||--o{ Ward : contains
    Department }o--|| Doctor : "director"
    Ward }o--|| Nurse : "supervisor"
    Employee }|--|| Doctor : "is_a"
    Employee }|--|| Nurse : "is_a"
    PatientAdmission }o--|| Patient : "for"
    PatientAdmission }o--|| Ward : "in"
    PatientAdmission }o--|| Doctor : "treated_by"
    Nurse }o--|| Department : "assigned_to"

    Hospital {
        uuid id PK
        string name
        string branch
        string address
        string city
        string state
        datetime createdAt
        datetime updatedAt
    }

    Employee {
        uuid id PK
        int employeeNumber
        string firstName
        string middleName
        string lastName
        string address
        string phoneNumber
        enum employeeType
        datetime createdAt
        datetime updatedAt
    }

    Department {
        uuid id PK
        string departmentName
        string departmentCode
        int numberOfWards
        string building
        uuid hospitalId FK
        uuid directorId FK
        datetime createdAt
        datetime updatedAt
    }

    Doctor {
        uuid id PK,FK
        string specialty
        datetime createdAt
        datetime updatedAt
    }

    Nurse {
        uuid id PK,FK
        string rotation
        float salary
        uuid departmentId FK
        datetime createdAt
        datetime updatedAt
    }

    Ward {
        uuid id PK
        int wardNumber
        int numberOfBeds
        uuid departmentId FK
        uuid supervisorId FK
        datetime createdAt
        datetime updatedAt
    }

    Patient {
        uuid id PK
        int patientNumber
        string firstName
        string middleName
        string lastName
        string address
        string telephoneNumber
        datetime createdAt
        datetime updatedAt
    }

    PatientAdmission {
        uuid id PK
        uuid patientId FK
        uuid wardId FK
        int bedNumber
        string diagnosis
        uuid treatingDoctorId FK
        datetime admissionDate
        datetime dischargeDate
        boolean isCurrent
        datetime createdAt
        datetime updatedAt
    }

```

## Project Overview

This project implements a comprehensive Hospital Information System database with Java JDBC connectivity. The system models hospital operations including employee management, department/ward organization, and patient care tracking.

## Key Features

- **Relational Database Design**: Properly normalized schema up to 3NF
- **JDBC Connectivity**: Secure Java database connection
- **CRUD Operations**: Full Create, Read, Update, Delete functionality
- **Entity Relationships**: Models complex hospital organizational structure

## Database Schema

### Main Entities

- **Employees** (Doctors and Nurses)
- **Departments** (with director doctors)
- **Wards** (with supervising nurses)
- **Patients** (with hospitalization details)

### Relationships

- One-to-many: Department → Wards
- Many-to-one: Nurse → Department
- Many-to-many: Doctors ↔ Patients (through treatments)

## Technologies Used

- Database: MySQL/PostgreSQL
- Backend: Java JDBC
- Modeling Tool: *(Specify your ERD tool here)*

## Getting Started

### Prerequisites

- Java JDK 8+
- MySQL/PostgreSQL installed
- JDBC driver

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/thenoblet/hms.git
   ```

2. Import the SQL schema:
   ```bash
   mysql -u username -p < hospital_schema.sql
   ```

3. Configure database connection in `src/main/java/gtp/hms/util/DatabaseConnection.java`



## ERD Diagram

![hospital management (1)](https://github.com/user-attachments/assets/683dfdb1-505f-4831-a8dd-b0494c8d45ca)<?xml version="1.0" standalone="no"?><svg version="1.1" xmlns="http://www.w3.org/2000/svg" width="1280.3385593985417" height="1312">
