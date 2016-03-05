-- Meant for PostgreSQL --

DROP TABLE product;
DROP TABLE product_category;
DROP TABLE employee_department;
DROP TABLE department;
DROP TABLE employee;
DROP TABLE company;


CREATE TABLE company (
  pid SERIAL PRIMARY KEY,
  name VARCHAR(40) NOT NULL,
  address VARCHAR(128)
);

CREATE TABLE employee (
  pid SERIAL PRIMARY KEY,
  company_pid INTEGER NOT NULL REFERENCES company (pid) ON DELETE CASCADE,
  name varchar(50) NOT NULL,
  surname varchar(50) NOT NULL,
  mail varchar(50),
  salary numeric(10,2) CHECK (salary > 0)
);

CREATE TABLE department (
  pid SERIAL PRIMARY KEY,
  name varchar(50) NOT NULL
);

CREATE TABLE employee_department (
  employee_pid INTEGER NOT NULL REFERENCES employee (pid) ON DELETE CASCADE,
  department_pid INTEGER NOT NULL REFERENCES department (pid) ON DELETE CASCADE,
  UNIQUE (employee_pid, department_pid)
);

CREATE TABLE product_category (
  pid SERIAL PRIMARY KEY,
  category varchar(50) NOT NULL
);

CREATE TABLE product (
  pid SERIAL PRIMARY KEY,
  product_category_pid INTEGER NOT NULL REFERENCES product_category (pid) ON DELETE CASCADE,
  name varchar(50) NOT NULL,
  description varchar(50) NOT NULL,
  row_status char(1) DEFAULT 'A',
  price numeric(10,2) CHECK (price > 0)
);

INSERT INTO product_category (pid, category) values (1, 'Hardware');
INSERT INTO product_category (pid, category) values (2, 'Software');
INSERT INTO product_category (pid, category) values (3, 'Middleware');

INSERT INTO public.company (name, address) VALUES('CleverGang', 'Chotikov 14, Chotikov 330 17');
INSERT INTO public.company (name, address) VALUES('Querity', 'Manesova 44, Plzen');


INSERT INTO public.employee (company_pid, name, surname, mail, salary)
VALUES(1, 'Břetislav', 'Wajtr', 'bretislav.wajtr@clevergang.com', 35000);
INSERT INTO public.employee (company_pid, name, surname, mail, salary)
VALUES(1, 'Viktor', 'Janukovic', 'viktor.janukovic@clevergang.com', 10000);
INSERT INTO public.employee (company_pid, name, surname, mail, salary)
VALUES(1, 'Olga', 'Pravcová', 'olga.wajtrova@clevergang.com', 35000);
INSERT INTO public.employee (company_pid, name, surname, mail, salary)
VALUES(2, 'Jiri', 'Kiml', 'jiri.kiml@querity.cz', 49000);


INSERT INTO public.department (name) VALUES('Development');
INSERT INTO public.department (name) VALUES('Sales');


INSERT INTO public.employee_department (employee_pid, department_pid) VALUES(1, 1);
INSERT INTO public.employee_department (employee_pid, department_pid) VALUES(2, 1);
INSERT INTO public.employee_department (employee_pid, department_pid) VALUES(3, 1);
INSERT INTO public.employee_department (employee_pid, department_pid) VALUES(3, 2);

