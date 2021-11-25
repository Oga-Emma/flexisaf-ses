docker run --name student_enrolment_system -e POSTGRES_PASSWORD=password -p 5432:5432 -d postgres:alpine

Run the bash shell in the docker container running the postgres instance
docker exec -it s26 /bin/bash

start the postgres shell
psql -U postgres -W (when prompted enter the password, in this case we used 'password')

run the command below to create a database
CREATE DATABASE student_enrolment_system;

connect to database
\c student_enrolment_system
enter the password = 'password'
