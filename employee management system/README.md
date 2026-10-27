# Employee Management System

A Spring Boot + MySQL REST API with JWT authentication and an Angular 19 starter UI.

## Project folders

- `src/` — Spring Boot backend
- `frontend/` — Angular employee dashboard (login, list, create, edit, delete)
- `pom.xml` — backend Maven configuration

## Backend setup

Requirements: Java 17+ (use a JDK compatible with your installed Spring Boot version), MySQL 8+, Maven.

1. Create the database:
   ```sql
   CREATE DATABASE employeedata;
   ```
2. Configure environment variables before running the backend. **Do not commit real credentials or JWT secrets.**

   Windows PowerShell:
   ```powershell
   $env:DB_URL="jdbc:mysql://localhost:3306/employeedata"
   $env:DB_USERNAME="root"
   $env:DB_PASSWORD="YOUR_MYSQL_PASSWORD"
   $env:JWT_SECRET="YOUR_BASE64_32_BYTE_SECRET"
   .\mvnw.cmd spring-boot:run
   ```

   Generate a JWT secret in PowerShell:
   ```powershell
   $bytes = New-Object byte[] 32
   [System.Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
   [Convert]::ToBase64String($bytes)
   ```
   Keep the generated value private. For Eclipse, add the four variables under **Run Configurations → Environment**.

3. Backend base URL: `http://localhost:8080`.

## Initial admin account

Public registration (`POST /users/register`) creates an `ROLE_EMPLOYEE` account and intentionally ignores client-supplied role IDs and employee IDs. This prevents public users from granting themselves administrator access.

To prepare roles, run:
```sql
INSERT INTO roles (role_name)
SELECT 'ROLE_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE role_name = 'ROLE_ADMIN');

INSERT INTO roles (role_name)
SELECT 'ROLE_EMPLOYEE'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE role_name = 'ROLE_EMPLOYEE');
```

Register your account using `POST http://localhost:8080/users/register`:
```json
{
  "username": "adminuser",
  "password": "ChangeThisPassword123!",
  "email": "admin@example.com"
}
```

Then use SQL to grant that account the admin role. Replace the username/email as needed:
```sql
INSERT INTO user_roles (user_id, role_id)
SELECT u.user_id, r.role_id
FROM users u
JOIN roles r ON r.role_name = 'ROLE_ADMIN'
WHERE u.username = 'adminuser'
  AND NOT EXISTS (
    SELECT 1 FROM user_roles ur
    WHERE ur.user_id = u.user_id AND ur.role_id = r.role_id
  );
```
Use a real email and a unique, strong password. Do not use the example password in a real environment.

## Angular frontend setup

Requirements: Node.js 20.19+ or 22.12+ and npm.

```powershell
cd frontend
npm install
npm start
```

Open `http://localhost:4200`. Sign in using an account that has `ROLE_ADMIN` to create, edit, or delete employees. The frontend stores the JWT in browser local storage and sends it as a Bearer token. Log out when finished, especially on shared computers.

The initial UI includes:
- JWT login
- Employee directory
- Create, edit, and delete employee
- API error messages and refresh
- Responsive layout

Employee creation/update uses existing department, address and project IDs. An address can be assigned to only one employee. The UI accepts comma-separated project IDs.

## Main API endpoints

| Feature | Endpoint |
|---|---|
| Login | `POST /api/auth/login` |
| Register user | `POST /users/register` |
| Employees | `GET /employees`, `GET /employees/{id}`, `POST /employees`, `PUT /employees/{id}`, `DELETE /employees/{id}` |
| Addresses | `/addresses` |
| Projects | `/projects` |
| Leaves | `/leaves` |
| Attendance | `/attendance` |
| Tasks | `/api/tasks` |
| Roles | `/roles` |
| Departments | `GET /findalldep`, `POST /savedep`, `GET /findbyid/{id}`, `PUT /updatedep/{id}`, `DELETE /deletedep/{id}` |

Most endpoints require `Authorization: Bearer <token>`. Administrative write operations and user/role management require `ROLE_ADMIN`. Leave creation is admin-only until ownership-aware employee leave requests are implemented. Public registration and login are exceptions.

## Tests and verification

- A test profile uses an in-memory H2 database so the context test does not require your local MySQL password.
- Run backend tests with `.\mvnw.cmd test` on Windows or `bash mvnw test` where the wrapper can download Maven.
- Build/run verification in this environment was not possible because Maven Wrapper could not download Maven from Maven Central. Please run the commands locally and share the first error if one appears.

## Important notes

- Existing database data is not included in this ZIP.
- If the existing `users` table contains plaintext passwords from earlier development, reset those accounts; this code does not automatically migrate old passwords.
- `spring.jpa.hibernate.ddl-auto=update` is convenient for local development. Use managed schema migrations and review security/configuration before production deployment.
