CREATE TABLE IF NOT EXISTS departments (
    _id UUID PRIMARY KEY NOT NULL,
    department VARCHAR(100) NOT NULL
);

CREATE TYPE GENDER AS ENUM ('MALE', 'FEMALE', 'OTHER');

CREATE TABLE IF NOT EXISTS students (
    _id UUID PRIMARY KEY NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    other_name VARCHAR(100) NOT NULL,
    gender GENDER NOT NULL,
    dob DATE NOT NULL,
    created_at DATE NOT NULL,
    matric_number VARCHAR(100) NOT NULL,
    department_id UUID NOT NULL,
    FOREIGN KEY ( department_id ) REFERENCES  departments  ( _id )
);
