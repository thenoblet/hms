-- Clear existing data (optional, this is not ideal prod. Careful, son! Ha!)
TRUNCATE TABLE patient_admission, ward, nurse, doctor, department, employee, hospital RESTART IDENTITY CASCADE;

-- Seed Hospitals
INSERT INTO hospital (name, branch, address, city, state) VALUES
                                                              ('NONA Hospital', 'Airport Branch', '123 Medical Drive', 'Accra', 'Greater Accra'),
                                                              ('ZENDAYA Hospital', 'Downtown', '456 Health Avenue', 'Kumasi', 'Asante City State');

-- Seed Employees (Doctors and Nurses)
INSERT INTO employee (employee_number, first_name, middle_name, last_name, employee_type) VALUES
                                                                                 (1001, 'Patrick', 'Appiah', 'Noblet', 'doctor'),
                                                                                 (1002, 'Emily', 'Johnson', 'Dwayne','doctor'),
                                                                                 (2001, 'Sarah', 'Williams', 'Boateng', 'nurse'),
                                                                                 (2002, 'Nancy', 'Emily', 'Noblet','nurse');

-- Seed Doctors (specialties)
INSERT INTO doctor (id, specialty) VALUES
                                       ((SELECT id FROM employee WHERE employee_number = 1001), 'Cardiology'),
                                       ((SELECT id FROM employee WHERE employee_number = 1002), 'Neurology');

-- Seed Departments
INSERT INTO department (department_name, department_code, number_of_wards, building, hospital_id, director_id) VALUES
                                                                                                                   ('Cardiology', 'CARD', 3, 'North Wing',
                                                                                                                    (SELECT id FROM hospital WHERE name = 'NONA Hospital'),
                                                                                                                    (SELECT id FROM employee WHERE employee_number = 1001)),
                                                                                                                   ('Neurology', 'NEURO', 2, 'South Wing',
                                                                                                                    (SELECT id FROM hospital WHERE name = 'NONA Hospital'),
                                                                                                                    (SELECT id FROM employee WHERE employee_number = 1002));

-- Seed Nurses (assign to departments)
INSERT INTO nurse (id, rotation, salary, department_id) VALUES
                                                            ((SELECT id FROM employee WHERE employee_number = 2001), 'Day Shift', 65000.00,
                                                             (SELECT id FROM department WHERE department_code = 'CARD')),
                                                            ((SELECT id FROM employee WHERE employee_number = 2002), 'Night Shift', 68000.00,
                                                             (SELECT id FROM department WHERE department_code = 'NEURO'));

-- Seed Wards
INSERT INTO ward (ward_number, number_of_beds, department_id, supervisor_id) VALUES
                                                                                 (1, 20,
                                                                                  (SELECT id FROM department WHERE department_code = 'CARD'),
                                                                                  (SELECT id FROM employee WHERE employee_number = 2001)),
                                                                                 (2, 15,
                                                                                  (SELECT id FROM department WHERE department_code = 'NEURO'),
                                                                                  (SELECT id FROM employee WHERE employee_number = 2002));

-- Seed Patients
INSERT INTO patient (patient_number, first_name, last_name, telephone_number) VALUES
                                                                                  (10001, 'Robert', 'Wilson', '555-0101'),
                                                                                  (10002, 'Lisa', 'Adanye', '555-0102'),
                                                                                  (10003, 'David', 'Atabili', '555-0103');

-- Seed Patient Admissions
INSERT INTO patient_admission (patient_id, ward_id, bed_number, admission_date, treating_doctor_id) VALUES
                                                                                                        ((SELECT id FROM patient WHERE patient_number = 10001),
                                                                                                         (SELECT id FROM ward WHERE ward_number = 1 AND department_id = (SELECT id FROM department WHERE department_code = 'CARD')),
                                                                                                         5, CURRENT_TIMESTAMP - INTERVAL '2 days',
                                                                                                         (SELECT id FROM employee WHERE employee_number = 1001)),

                                                                                                        ((SELECT id FROM patient WHERE patient_number = 10002),
                                                                                                         (SELECT id FROM ward WHERE ward_number = 2 AND department_id = (SELECT id FROM department WHERE department_code = 'NEURO')),
                                                                                                         8, CURRENT_TIMESTAMP - INTERVAL '1 day',
                                                                                                         (SELECT id FROM employee WHERE employee_number = 1002));