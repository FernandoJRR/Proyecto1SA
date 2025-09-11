package com.sa.employee_service.employees.domain.enums;

import com.sa.employee_service.employees.domain.Permission;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SystemPermissionEnum {

    // PERMISOS DE EMPLEADOS
    CREATE_EMPLOYEE(new Permission(null,"Crear empleado", "CREATE_EMPLOYEE")),
    EDIT_EMPLOYEE(new Permission(null,"Editar empleado", "EDIT_EMPLOYEE")),
    DESACTIVATE_EMPLOYEE(new Permission(null, "Desactivar empleado", "DESACTIVATE_EMPLOYEE")),
    RESACTIVATE_EMPLOYEE(new Permission(null,"Reactivar empleado", "RESACTIVATE_EMPLOYEE")),
    UPDATE_EMPLOYEE_SALARY(new Permission(null,"Actualiza el sueldo de un empleado", "UPDATE_EMPLOYEE_SALARY")),

    // PARA TIPOS DE EMPLEADO
    CREATE_EMPLOYEE_TYPE(new Permission(null,"Crear tipo de empleado", "CREATE_EMPLOYEE_TYPE")),
    EDIT_EMPLOYEE_TYPE(new Permission(null,"Editar tipo de empleado", "EDIT_EMPLOYEE_TYPE")),
    DELETE_EMPLOYEE_TYPE(new Permission(null,"Eliminar tipo de empleado", "DELETE_EMPLOYEE_TYPE")),

    // Permisos de habitaciones
    CREATE_ROOM(new Permission(null,"Crear habitacion", "CREATE_ROOM")),
    EDIT_ROOM(new Permission(null,"Editar habitacion", "EDIT_ROOM")),
    DELETE_ROOM(new Permission(null,"Eliminar habitacion", "DELETE_ROOM")),

    // Permisos de facturacion
    CREATE_INVOICE(new Permission(null,"Crear factura", "CREATE_INVOICE")),

    // Permisos de reservas
    CREATE_RESERVATION(new Permission(null,"Crear reserva", "CREATE_RESERVATION")),
    CANCEL_RESERVATION(new Permission(null,"Cancelar reserva", "CANCEL_RESERVATION")),
    PAY_RESERVATION(new Permission(null,"Pagar reserva", "PAY_RESERVATION")),
    DELETE_RESERVATION(new Permission(null,"Eliminar reserva", "DELETE_RESERVATION")),

    // Permisos de platillos
    CREATE_DISH(new Permission(null,"Crear platillo", "CREATE_DISH")),
    EDIT_DISH(new Permission(null,"Editar platillo", "EDIT_DISH")),
    DELETE_DISH(new Permission(null,"Eliminar platillo", "DELETE_DISH")),

    // Permisos de ordenes
    CREATE_ORDER(new Permission(null,"Crear orden", "CREATE_ORDER")),

    // Permisos sobre promociones
    CREATE_PROMOTION(new Permission(null,"Crear promocion", "CREATE_PROMOTION")),
    EDIT_PROMOTION(new Permission(null,"Editar promocion", "EDIT_PROMOTION")),
    DELETE_PROMOTION(new Permission(null,"Activar o desactivar promocion", "TOOGLE_PROMOTION")),

    // Permisos sobre pagos
    CREATE_PAYMENT(new Permission(null,"Crear pago", "CREATE_PAYMENT")),

    // Permisos de stock
    MODIFY_STOCK(new Permission(null,"Modificar stock", "MODIFY_STOCK")),

    // Permisos de hoteles
    CREATE_HOTEL(new Permission(null,"Crear hotel", "CREATE_HOTEL")),
    EDIT_HOTEL(new Permission(null,"Editar hotel", "EDIT_HOTEL")),
    DELETE_HOTEL(new Permission(null,"Activar o desactivar hotel", "TOOGLE_HOTEL")),

    // Permisos de restaurantes
    CREATE_RESTAURANT(new Permission(null,"Crear restaurante", "CREATE_RESTAURANT")),
    EDIT_RESTAURANT(new Permission(null,"Editar restaurante", "EDIT_RESTAURANT")),
    DELETE_RESTAURANT(new Permission(null,"Activar o desactivar restaurante", "TOOGLE_RESTAURANT")),

    // Permisos de clientes
    CREATE_CLIENT(new Permission(null,"Crear client", "CREATE_CLIENT")),
    EDIT_CLIENT(new Permission(null,"Editar client", "EDIT_CLIENT")),
    DELETE_CLIENT(new Permission(null,"Activar o desactivar client", "TOOGLE_CLIENT")),

    // Permisos de reviews
    CREATE_REVIEW(new Permission(null,"Crear review", "CREATE_REVIEW")),
    ;

    private final Permission permission;
}
