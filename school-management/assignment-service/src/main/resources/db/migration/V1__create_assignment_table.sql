CREATE TABLE assignment (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   title VARCHAR(255) NOT NULL,
   description VARCHAR(255),
   assigned_by VARCHAR(255),
   grade INTEGER,
   start_date date,
   end_date date,
   status VARCHAR(255),
   CONSTRAINT pk_assignment PRIMARY KEY (id)
);