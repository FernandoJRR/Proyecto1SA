import type { Entity } from "../utils/entity";
import type { EmployeeType } from "./employee-type";

const CURRENT_EMPLOYEE_URI = "/v1/employees";
const CURRENT_EXTRA_PAYMENT_URI = "/v1/extra-payments";

export interface HistoryType {
  type: string;
}

export interface EmployeeHistory {
  historyType: HistoryType;
  commentary: string;
  historyDate: string;
}

export interface Employee extends Entity {
  firstName: string;
  lastName: string;
  cui:string,
  salary: number;
  establishmentId: string;
  establishmentType: string;
  employeeType: EmployeeType;
  desactivatedAt: Date | null;
}

export interface VacationEmployee {
  periodYear: number,
  beginDate: Date,
  endDate: Date,
  wasUsed: boolean
}

export interface EmployeeResponse {
  employeeResponseDTO: Employee;
  username: string;
}

export interface UserPayload {
  username: string;
  password: string;
}

export interface EmployeeHistoryDatePayload {
  historyDate: string;
}

export interface EmployeePayload {
  firstName: string;
  lastName: string;
  cui: number;
  salary: number;
  establishmentId?: string;
  establishmentType?: string;
  employeeTypeId: { id: string };
  createUserRequestDTO: UserPayload;
  employeeHistoryDateRequestDTO: EmployeeHistoryDatePayload;
}

export interface EmployeeUpdatePayload {
  firstName: string;
  lastName: string;
  cui:string,
  salary: number;
  igssPercentage: number | null;
  irtraPercentage: number | null;
  employeeTypeId: { id: string };
}

export interface EmployeeSalaryUpdatePayload {
  salary: number;
  salaryDate: Date;
}

export interface EmployeeDeactivationPayload {
  deactivationDate: Date;
  historyTypeId: { id: string };
}

export interface EmployeeReactivationPayload {
  reactivationDate: Date;
}

export interface PaymentType {
  id: string,
  type: string
}

export interface ExtraPaymentPayload {
  paymentTypeId: string,
  reason: string,
  amount: number,
  description: string,
  employeesIds: string[]
}

export interface ExtraPayment {
  type: PaymentType,
  amount: number,
  description: string,
  reason: string,
  employees: Employee[]
}

export async function getAllEmployees(params?: {}) {
  return await $api<Employee[]>(`${CURRENT_EMPLOYEE_URI}/`, {
    params,
  });
}

export async function getEmployeeById(employee_id: string) {
  return await $api<Employee>(`${CURRENT_EMPLOYEE_URI}/${employee_id}`);
}

export const createEmployee = async (data: EmployeePayload) => {
  const response = await $api<Employee>(`${CURRENT_EMPLOYEE_URI}`, {
    method: "POST",
    body: data,
  });
  return response;
};

export const updateEmployee = async (
  data: EmployeeUpdatePayload,
  employeeId: string
) => {
  const response = await $api<Employee>(
    `${CURRENT_EMPLOYEE_URI}/${employeeId}`,
    {
      method: "PATCH",
      body: data,
    }
  );
  return response;
};

export const updateEmployeeSalary = async (
  data: EmployeeSalaryUpdatePayload,
  employeeId: string
) => {
  const response = await $api<Employee>(
    `${CURRENT_EMPLOYEE_URI}/${employeeId}/salary`,
    {
      method: "PATCH",
      body: data,
    }
  );
  return response;
};

export const deactivateEmployee = async (
  data: EmployeeDeactivationPayload,
  employeeId: string
) => {
  const response = await $api<Employee>(
    `${CURRENT_EMPLOYEE_URI}/${employeeId}/desactivate`,
    {
      method: "PATCH",
      body: data,
    }
  );
  return response;
};