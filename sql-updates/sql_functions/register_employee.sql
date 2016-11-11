-- Procedure which allows to register new employee and assign it to department and company.
-- Reason why it's in stored procedure is, that we want to allow to register the employee even if
-- the department and/or company does not exist in DB yet - in such case we would create such
-- company/department records along with the employee record. It would mean to do lot of DB
-- roundtrips if we want do such thing in java code.

CREATE OR REPLACE FUNCTION register_employee(_name   TEXT, _surname TEXT, _email TEXT,
                                            _salary NUMERIC(10, 2), _department_name TEXT, _company_name TEXT)
  returns TABLE  (
    employee_id integer,
    department_id integer,
    company_id integer
  )
AS
$$
DECLARE
  _employee_id integer;
  _department_id integer;
  _company_id integer;
BEGIN
  -- check if company exists; create it if it doesn't
  select pid into _company_id from company where company.name = _company_name;
  IF (_company_id is null) THEN
    INSERT INTO company (name)
    values (_company_name) RETURNING PID INTO _company_id;
    raise notice 'Created new company with pid %', _company_id;
  ELSE
    raise notice 'Found existing company with pid %', _company_id;
  END IF;

  -- check if department exists; create it if it doesn't
  select pid into _department_id from department where department.name = _department_name and department.company_pid = _company_id;
  IF (_department_id is null) THEN
    INSERT INTO department (company_pid, name)
    values (_company_id, _department_name) RETURNING PID INTO _department_id;
    raise notice 'Created new department with pid %', _department_id;
  ELSE
    raise notice 'Found existing department with pid %', _department_id;
  END IF;

  -- now we have company and department, finally insert the employee record
  INSERT INTO employee (department_pid, name, surname, email, salary) VALUES
    (_department_id, _name, _surname, _email, _salary) RETURNING PID into _employee_id;
  raise notice 'Inserted new employee record with PID %', _employee_id;

  --return out what happened here with relevant data
  return query
  select _employee_id, _department_id, _company_id;

END
$$ LANGUAGE plpgsql;