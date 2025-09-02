-- Eliminar la base de datos si existe
DROP DATABASE IF EXISTS p1_sa_finances_db;

-- Crear la base de datos
CREATE DATABASE p1_sa_finances_db;

-- Usar la base de datos
USE p1_sa_finances_db;

-- Verificar si el usuario no existe y crearlo
CREATE USER IF NOT EXISTS 'p1_sa_finances_user'@'localhost' IDENTIFIED BY 'finances_service';

-- Otorgar permisos específicos sobre la base de datos
GRANT ALL PRIVILEGES ON p1_sa_finances_db.* TO 'p1_sa_finances_user'@'localhost';

-- Aplicar cambios de permisos
FLUSH PRIVILEGES;