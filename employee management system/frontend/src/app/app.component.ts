import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { AuthService } from './auth.service';

interface Employee {
  empid: number;
  name: string;
  departmentId: number | null;
  addressId: number | null;
  projectIds: number[];
}

interface EmployeePayload {
  name: string;
  departmentId: number | null;
  addressId: number | null;
  projectIds: number[];
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  private readonly http = inject(HttpClient);
  readonly auth = inject(AuthService);
  private readonly api = 'http://localhost:8080';

  username = '';
  password = '';
  loginError = '';
  pageError = '';
  successMessage = '';
  loading = false;

  employees: Employee[] = [];
  editingId: number | null = null;
  form: EmployeePayload = this.emptyForm();
  projectIdsText = '';

  ngOnInit(): void {
    if (this.auth.isLoggedIn()) this.loadEmployees();
  }

  login(): void {
    this.loginError = '';
    this.loading = true;
    this.auth.login(this.username.trim(), this.password).subscribe({
      next: () => {
        this.password = '';
        this.loading = false;
        this.loadEmployees();
      },
      error: (error: HttpErrorResponse) => {
        this.loading = false;
        this.loginError = error.status === 401
          ? 'Invalid username or password.'
          : 'Login failed. Check that the backend is running on port 8080.';
      }
    });
  }

  loadEmployees(): void {
    this.pageError = '';
    this.http.get<Employee[]>(`${this.api}/employees`).subscribe({
      next: (items) => this.employees = items,
      error: (error: HttpErrorResponse) => {
        this.pageError = error.status === 403
          ? 'This account does not have ADMIN access. Sign in with an admin account to manage employees.'
          : error.status === 401
            ? 'Your session expired. Please sign in again.'
            : 'Could not load employees. Check the backend and database.';
        if (error.status === 401) this.logout();
      }
    });
  }

  saveEmployee(): void {
    this.pageError = '';
    this.successMessage = '';
    const payload: EmployeePayload = {
      name: this.form.name.trim(),
      departmentId: this.form.departmentId,
      addressId: this.form.addressId,
      projectIds: this.projectIdsText
        .split(',')
        .map(value => Number(value.trim()))
        .filter(value => Number.isInteger(value) && value > 0)
    };

    if (!payload.name) {
      this.pageError = 'Employee name is required.';
      return;
    }

    const request = this.editingId === null
      ? this.http.post<Employee>(`${this.api}/employees`, payload)
      : this.http.put<Employee>(`${this.api}/employees/${this.editingId}`, payload);

    request.subscribe({
      next: () => {
        this.successMessage = this.editingId === null
          ? 'Employee created successfully.'
          : 'Employee updated successfully.';
        this.cancelEdit();
        this.loadEmployees();
      },
      error: (error: HttpErrorResponse) => {
        this.pageError = error.error?.error
          || error.error?.message
          || 'Could not save employee. Verify department, address and project IDs.';
      }
    });
  }

  editEmployee(employee: Employee): void {
    this.editingId = employee.empid;
    this.form = {
      name: employee.name,
      departmentId: employee.departmentId,
      addressId: employee.addressId,
      projectIds: employee.projectIds ?? []
    };
    this.projectIdsText = (employee.projectIds ?? []).join(', ');
    this.successMessage = '';
    this.pageError = '';
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  deleteEmployee(employee: Employee): void {
    if (!window.confirm(`Delete employee "${employee.name}" (ID ${employee.empid})?`)) return;
    this.pageError = '';
    this.successMessage = '';
    this.http.delete(`${this.api}/employees/${employee.empid}`, { responseType: 'text' })
      .subscribe({
        next: () => {
          this.successMessage = 'Employee deleted successfully.';
          this.loadEmployees();
        },
        error: (error: HttpErrorResponse) => {
          this.pageError = error.error?.error || 'Could not delete employee. Check linked records.';
        }
      });
  }

  cancelEdit(): void {
    this.editingId = null;
    this.form = this.emptyForm();
    this.projectIdsText = '';
  }

  logout(): void {
    this.auth.logout();
    this.employees = [];
    this.cancelEdit();
  }

  private emptyForm(): EmployeePayload {
    return { name: '', departmentId: null, addressId: null, projectIds: [] };
  }
}
