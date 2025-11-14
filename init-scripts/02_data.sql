-- =========================================================
-- DATOS INICIALES DE CONFIGURACIÓN
-- =========================================================

-- Tipos de Cliente
INSERT INTO configuracion.TiposCliente (nombre, descripcion) VALUES
('Corporativo', 'Cliente empresa'),
('Individual', 'Cliente persona natural');

-- Condiciones de Pago
INSERT INTO configuracion.CondicionesPago (nombre, descripcion) VALUES
('Contado', 'Pago inmediato'),
('Crédito 30 días', 'Pago diferido a 30 días'),
('Crédito 60 días', 'Pago diferido a 60 días');

-- Tipos de Identificación
INSERT INTO configuracion.TiposIdentificacion (nombre, descripcion) VALUES
('PA', 'Número de Libreta'),
('CI', 'Cédula de Identidad'),
('RIF', 'Registro de Información Fiscal');

-- =========================================================
-- DATOS INICIALES DE PRODUCTOS
-- =========================================================

-- Departamentos
INSERT INTO productos.Departamentos (nombre, descripcion) VALUES
('Bebidas', 'Productos líquidos'),
('Alimentos', 'Productos comestibles'),
('Electrónica', 'Dispositivos electrónicos');

-- Categorías
INSERT INTO productos.Categorias (id_departamento, nombre, descripcion) VALUES
(1, 'Gaseosas', 'Bebidas carbonatadas'),
(1, 'Jugos', 'Bebidas naturales'),
(2, 'Snacks', 'Comida rápida'),
(3, 'Celulares', 'Teléfonos móviles');

-- Marcas
INSERT INTO productos.Marcas (nombre, descripcion) VALUES
('Coca-Cola', 'Marca de bebidas'),
('Pepsi', 'Marca de bebidas'),
('Samsung', 'Electrónica'),
('Apple', 'Electrónica');

-- Unidades de Medida
INSERT INTO productos.UnidadesMedida (nombre, abreviacion) VALUES
('Unidad', 'und'),
('Litro', 'L'),
('Mililitro', 'ml'),
('Kilogramo', 'kg');

-- Bodegas
INSERT INTO productos.Bodegas (nombre, ubicacion) VALUES
('Central', 'Av. Principal 123'),
('Sucursal Norte', 'Calle 45 67'),
('Sucursal Sur', 'Av. Bolívar 89');

-- =========================================================
-- DATOS INICIALES DE PRECIOS Y MONEDAS
-- =========================================================

-- Listas de Precios
INSERT INTO precios.ListasPrecios (nombre, descripcion) VALUES
('General', 'Lista de precios estándar'),
('Mayorista', 'Lista de precios para ventas al por mayor');

-- Monedas
INSERT INTO precios.Monedas (codigo, nombre, simbolo) VALUES
('USD', 'Dólar Estadounidense', '$'),
('EUR', 'Euro', '€'),
('VES', 'Bolívar Venezolano', 'Bs.');

INSERT INTO precios.TasasCambio (id_moneda, cambio) VALUES
(1, 1.0000), -- USD base
(2, 1.1000), -- EUR
(3, 235); -- VES

-- =========================================================
-- DATOS INICIALES DE SEGURIDAD
-- =========================================================

-- Roles
INSERT INTO seguridad.Roles (nombre, descripcion) VALUES
('Administrador', 'Acceso total al sistema'),
('Vendedor', 'Acceso al módulo de ventas'),
('Contador', 'Acceso al módulo contable');

-- Módulos
INSERT INTO seguridad.Modulos (nombre, descripcion) VALUES
('Ventas', 'Gestión de ventas'),
('Inventario', 'Gestión de inventario'),
('Contabilidad', 'Gestión contable');

-- Usuarios iniciales (contraseñas en texto plano, reemplazar por hash en producción)
INSERT INTO seguridad.Usuarios (nombre, usuario, contraseña, id_rol, estado) VALUES
('Admin General', 'admin', 'admin', 1, 'activo'),
('Usuario Ventas', 'ventas', 'ventas', 2, 'activo'),
('Usuario Contador', 'contador', 'contador', 3, 'activo');

-- Permisos básicos
INSERT INTO seguridad.RolesPermisos (id_rol, id_modulo, permiso) VALUES
-- Admin con acceso total
(1, 1, 'ver'), (1, 1, 'crear'), (1, 1, 'editar'), (1, 1, 'eliminar'), -- Ventas
(1, 2, 'ver'), (1, 2, 'crear'), (1, 2, 'editar'), (1, 2, 'eliminar'), -- Inventario
(1, 3, 'ver'), (1, 3, 'crear'), (1, 3, 'editar'), (1, 3, 'eliminar'), -- Contabilidad

-- Vendedor solo en Ventas
(2, 1, 'ver'), (2, 1, 'crear'),

-- Contador solo en Contabilidad
(3, 3, 'ver'), (3, 3, 'editar');
                   -- Contador en Contabilidad

-- =========================================================
-- DATOS INICIALES DE DOCUMENTOS
-- =========================================================

-- Tipos de Documento
INSERT INTO documentos.TiposDocumento (nombre, descripcion) VALUES
('Factura', 'Documento de venta'),
('Nota de Crédito', 'Documento de ajuste'),
('Orden de Compra', 'Documento de compra');

-- Series de Documentos
INSERT INTO documentos.SeriesDocumentos (codigo, descripcion, id_tipo_documento, prefijo) VALUES
('FAC-2025', 'Serie de facturas 2025', 1, 'FAC'),
('NC-2025', 'Serie de notas de crédito 2025', 2, 'NC'),
('OC-2025', 'Serie de órdenes de compra 2025', 3, 'OC');

-- Métodos de Pago
INSERT INTO documentos.MetodosPago (nombre, descripcion) VALUES
('Efectivo', 'Pago en efectivo'),
('Transferencia', 'Pago por transferencia bancaria'),
('Tarjeta', 'Pago con tarjeta de crédito/débito');

-- =========================================================
-- DATOS INICIALES DE CONTABILIDAD
-- =========================================================

-- Cuentas Contables
INSERT INTO contabilidad.CuentasContables (codigo, nombre, tipo, descripcion) VALUES
('1105', 'Caja', 'Activo', 'Dinero en efectivo'),
('1110', 'Bancos', 'Activo', 'Dinero en cuentas bancarias'),
('2105', 'Proveedores', 'Pasivo', 'Cuentas por pagar a proveedores'),
('2205', 'Clientes', 'Activo', 'Cuentas por cobrar a clientes'),
('4105', 'Ingresos por Ventas', 'Ingreso', 'Ventas de productos'),
('5105', 'Costo de Ventas', 'Gasto', 'Costo de los productos vendidos');

-- Impuestos
INSERT INTO contabilidad.Impuestos (nombre, porcentaje, aplica_en) VALUES
('IVA', 16.0000, 'venta'),
('Retención ISLR', 2.0000, 'compra');
