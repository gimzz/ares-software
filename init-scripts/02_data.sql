-- =========================================================
-- DATOS INICIALES DE CONFIGURACIÓN
-- =========================================================

-- Tipos de Cliente
INSERT INTO configuracion.TiposCliente (nombre, descripcion) VALUES
('Corporativo', 'Cliente empresa (J, G)'),
('Individual', 'Cliente persona natural (V, E, P)');

-- Condiciones de Pago
INSERT INTO configuracion.CondicionesPago (nombre, descripcion) VALUES
('Contado', 'Pago inmediato'),
('Crédito 30 días', 'Pago diferido a 30 días'),
('Crédito 60 días', 'Pago diferido a 60 días');

-- Tipos de Identificación
INSERT INTO configuracion.TiposIdentificacion (nombre, descripcion) VALUES
('V', 'Persona natural venezolana'),
('E', 'Persona natural extranjera'),
('J', 'Persona jurídica'),
('G', 'Entidad gubernamental'),
('P', 'Pasaporte');


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
('Unidad', 'UND'),
('Litro', 'L'),
('Mililitro', 'ML'),
('Kilogramo', 'KG');

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
('General', 'Lista estándar'),
('Mayorista', 'Precios por volumen');

-- Monedas
INSERT INTO precios.Monedas (codigo, nombre, simbolo) VALUES
('USD', 'Dólar Estadounidense', '$'),
('VES', 'Bolívar Venezolano', 'Bs'),
('EUR', 'Euro', '€'); 

INSERT INTO precios.TasasCambio (id_moneda, cambio) VALUES
(1, 1.0000),   
(3, 1.1000),   
(2, 36.50);    

-- =========================================================
-- DATOS INICIALES DE SEGURIDAD
-- =========================================================

-- Roles
INSERT INTO seguridad.Roles (nombre, descripcion) VALUES
('Administrador', 'Acceso total'),
('Vendedor', 'Acceso al módulo de ventas'),
('Contador', 'Acceso contable');

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

-- ADMIN → FULL ACCESS
INSERT INTO seguridad.RolesPermisos (id_rol, id_modulo, permiso) VALUES
(1, 1, 'ver'), (1, 1, 'crear'), (1, 1, 'editar'), (1, 1, 'eliminar'),
(1, 2, 'ver'), (1, 2, 'crear'), (1, 2, 'editar'), (1, 2, 'eliminar'),
(1, 3, 'ver'), (1, 3, 'crear'), (1, 3, 'editar'), (1, 3, 'eliminar'),

-- VENDEDOR → SOLO VENTAS (VER + CREAR)
(2, 1, 'ver'), 
(2, 1, 'crear'),

-- CONTADOR → SOLO CONTABILIDAD
(3, 3, 'ver'),
(3, 3, 'editar');


-- =========================================================
-- DATOS INICIALES DE DOCUMENTOS
-- =========================================================

-- Tipos de Documento
INSERT INTO documentos.TiposDocumento (nombre, descripcion) VALUES
('Factura', 'Documento de venta'),
('Nota de Crédito', 'Ajuste negativo'),
('Nota de Débito', 'Ajuste positivo'),
('Compra', 'Documento de compra');


-- Series de Documentos
INSERT INTO documentos.SeriesDocumentos (codigo, descripcion, id_tipo_documento, prefijo) VALUES
('F001', 'Serie de facturas fiscales', 1, 'FAC'),
('NC01', 'Serie notas de crédito', 2, 'NC'),
('ND01', 'Serie notas de débito', 3, 'ND'),
('C001', 'Serie documentos de compra', 4, 'COM');


-- Métodos de Pago
INSERT INTO documentos.MetodosPago (nombre, descripcion) VALUES
('Efectivo', 'Pago en efectivo'),
('Transferencia', 'Transferencia bancaria'),
('Pago Móvil', 'Pago móvil'),
('Tarjeta', 'Tarjeta de débito/crédito');


-- =========================================================
-- DATOS INICIALES DE CONTABILIDAD
-- =========================================================

-- Cuentas Contables
INSERT INTO contabilidad.CuentasContables (codigo, nombre, tipo, descripcion) VALUES
('1105', 'Caja', 'Activo', 'Efectivo disponible'),
('1110', 'Bancos', 'Activo', 'Cuentas bancarias'),
('2105', 'Proveedores', 'Pasivo', 'Cuentas por pagar'),
('1305', 'Clientes', 'Activo', 'Cuentas por cobrar'),
('4105', 'Ingresos por Ventas', 'Ingreso', 'Ventas de productos'),
('5105', 'Costo de Ventas', 'Gasto', 'Costo de mercancía vendida');

-- Impuestos
INSERT INTO contabilidad.Impuestos (nombre, porcentaje, aplica_en) VALUES
('IVA 16%', 16.00, 'venta'),
('IVA 16% Compra', 16.00, 'compra'),
('Retención IVA', 75.00, 'compra'),     -- 75% del IVA generalmente
('Retención ISLR', 2.00, 'compra');

