export type RoleName = string | undefined

export function getRoleNameFromEmployee(
  employee?: { employeeType?: { name?: string } } | null
): RoleName {
  return employee?.employeeType?.name
}

export function isAdmin(role: RoleName): boolean {
  return role === 'Admin'
}

export function hasAnyRole(role: RoleName, allowed: string[]): boolean {
  return !!role && allowed.includes(role)
}

export function canAccessAdmin(role: RoleName): boolean {
  return isAdmin(role)
}

export function canAccessReservaciones(role: RoleName): boolean {
  return isAdmin(role) || role === 'Staff Hotel'
}

export function canAccessOrdenes(role: RoleName): boolean {
  return isAdmin(role) || role === 'Staff Restaurante'
}

export function canAccessReportes(role: RoleName): boolean {
  return isAdmin(role) || role === 'Contador'
}
