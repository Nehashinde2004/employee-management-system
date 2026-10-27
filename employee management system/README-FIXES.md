# Review notes

See `README.md` for setup and current API access rules.

Updates in this revision:
- Added an Angular 19 starter dashboard in `frontend/` with JWT login and employee CRUD UI.
- Added CORS support for the local Angular development server (`localhost:4200`).
- Public registration now always assigns `ROLE_EMPLOYEE` and does not let the caller assign arbitrary role IDs or employee IDs.
- Restricted role/user management and administrative write endpoints to `ROLE_ADMIN`.
- Added request validation and date/status checks for leave requests; new leave requests always start as `PENDING`.
- Added transaction boundaries to service methods that convert entities with lazy relationships.
- Made task deletion return a not-found error instead of silently succeeding for an unknown ID.
- Removed the default database password from `application.properties`.
- Added an H2-backed Spring context test profile.

Verification limitation: Maven Wrapper could not download Maven in the current environment, so the backend test suite and Angular production build have not been executed here. Run the commands in `README.md` locally.
