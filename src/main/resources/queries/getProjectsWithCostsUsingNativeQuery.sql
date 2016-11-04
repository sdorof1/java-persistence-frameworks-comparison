WITH
project_info AS (
    SELECT project.pid project_pid, project.name project_name, salary monthly_cost, company.name company_name
    FROM project
      JOIN projectemployee ON project.pid = projectemployee.project_pid
      JOIN employee ON projectemployee.employee_pid = employee.pid
      LEFT JOIN department ON employee.department_pid = department.pid
      LEFT JOIN company ON department.company_pid = company.pid
),
project_cost AS (
    SELECT project_pid, sum(monthly_cost) total_cost
    FROM project_info GROUP BY project_pid
)
SELECT project_name, total_cost, company_name, sum(monthly_cost) company_cost FROM project_info
  JOIN project_cost USING (project_pid)
WHERE total_cost > ?
GROUP BY project_name, total_cost, company_name