-- Enable UUID extension if not exists
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create custom types
CREATE TYPE employee_type AS ENUM ('doctor', 'nurse');

-- 1. Hospital table (independent)
CREATE TABLE IF NOT EXISTS hospital (
                                        id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                        name VARCHAR(100) NOT NULL,
                                        branch VARCHAR(100),
                                        address VARCHAR(254),
                                        city VARCHAR(50),
                                        state VARCHAR(50),
                                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. Employee table (base)
CREATE TABLE IF NOT EXISTS employee (
                                        id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                        employee_number INT NOT NULL UNIQUE,
                                        first_name VARCHAR(100) NOT NULL,
                                        middle_name VARCHAR(100),
                                        last_name VARCHAR(100) NOT NULL,
                                        address VARCHAR(254),
                                        phone_number VARCHAR(20),
                                        employee_type employee_type NOT NULL,
                                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 3. Department (depends on hospital)
CREATE TABLE IF NOT EXISTS department (
                                          id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                          department_name VARCHAR(100) NOT NULL,
                                          department_code VARCHAR(20) NOT NULL UNIQUE,
                                          number_of_wards INT NOT NULL,
                                          building VARCHAR(50),
                                          hospital_id UUID NOT NULL REFERENCES hospital(id),
                                          director_id UUID, -- Will be set after doctor table exists
                                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 4. Doctor (subtype of employee)
CREATE TABLE IF NOT EXISTS doctor (
                                      id UUID PRIMARY KEY REFERENCES employee(id),
                                      specialty VARCHAR(100) NOT NULL,
                                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 5. Now set the department director FK
ALTER TABLE department
    ADD CONSTRAINT fk_department_director
        FOREIGN KEY (director_id) REFERENCES doctor(id);

-- 6. Nurse (subtype with department relation)
CREATE TABLE IF NOT EXISTS nurse (
                                     id UUID PRIMARY KEY REFERENCES employee(id),
                                     rotation VARCHAR(50),
                                     salary DECIMAL(10, 2),
                                     department_id UUID NOT NULL REFERENCES department(id),
                                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 7. Ward (depends on department and nurse)
CREATE TABLE IF NOT EXISTS ward (
                                    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                    ward_number INT NOT NULL,
                                    number_of_beds INT NOT NULL,
                                    department_id UUID NOT NULL REFERENCES department(id),
                                    supervisor_id UUID NOT NULL REFERENCES nurse(id),
                                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    UNIQUE (department_id, ward_number) -- Ward numbers unique per department
);

-- 8. Patient (independent)
CREATE TABLE IF NOT EXISTS patient (
                                       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                       patient_number INT NOT NULL UNIQUE,
                                       first_name VARCHAR(100) NOT NULL,
                                       middle_name VARCHAR(100),
                                       last_name VARCHAR(100) NOT NULL,
                                       address VARCHAR(254),
                                       telephone_number VARCHAR(20),
                                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 9. Patient Admission (junction with relationships)
CREATE TABLE IF NOT EXISTS patient_admission (
                                                 id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                                 patient_id UUID NOT NULL REFERENCES patient(id),
                                                 ward_id UUID NOT NULL REFERENCES ward(id),
                                                 bed_number INT NOT NULL,
                                                 diagnosis VARCHAR(500),
                                                 treating_doctor_id UUID REFERENCES doctor(id),
                                                 admission_date TIMESTAMP NOT NULL,
                                                 discharge_date TIMESTAMP,
                                                 is_current BOOLEAN NOT NULL DEFAULT TRUE,
                                                 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                 updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_employee_number ON employee(employee_number);
CREATE INDEX IF NOT EXISTS idx_patient_number ON patient(patient_number);
CREATE INDEX IF NOT EXISTS idx_department_code ON department(department_code);
CREATE INDEX IF NOT EXISTS idx_ward_department ON ward(department_id);
CREATE INDEX IF NOT EXISTS idx_admission_patient ON patient_admission(patient_id);
CREATE INDEX IF NOT EXISTS idx_admission_current ON patient_admission(is_current);