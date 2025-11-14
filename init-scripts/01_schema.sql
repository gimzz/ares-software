CREATE SCHEMA IF NOT EXISTS configuracion;
CREATE SCHEMA IF NOT EXISTS productos;
CREATE SCHEMA IF NOT EXISTS precios;
CREATE SCHEMA IF NOT EXISTS seguridad;
CREATE SCHEMA IF NOT EXISTS documentos;
CREATE SCHEMA IF NOT EXISTS contabilidad;

-- Tabla de tipos de clientes: mayoristas, minoristas, frecuencia, etc.
CREATE TABLE configuracion.TiposCliente (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,  -- Ej: "Mayorista", "Final", "VIP"
    descripcion TEXT
);

-- Condiciones de pago: Crédito 30 días, Contado, etc.
CREATE TABLE configuracion.CondicionesPago (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,  -- Ej: "Crédito 30 días", "Contado"
    descripcion TEXT
);

-- Tipos de identificación fiscal del RIF (Venezolano)
CREATE TABLE configuracion.TiposIdentificacion (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(5) NOT NULL UNIQUE,     -- V, E, J, G, P
    descripcion TEXT                       -- Ej: "Persona Jurídica"
);

-- Entidades: clientes, proveedores, empleados, bancos, etc.
CREATE TABLE configuracion.Entidades (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,          -- Razón social o nombre
    apellido VARCHAR(150),                 -- Solo para personas naturales
    id_tipo_identificacion INT REFERENCES configuracion.TiposIdentificacion(id),
    numero_identificacion VARCHAR(50) UNIQUE,  -- Número del RIF: J123456789
    telefono_celular VARCHAR(20),
    direccion TEXT,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Relación 1:1 Cliente → Entidad
CREATE TABLE configuracion.Clientes (
    id SERIAL PRIMARY KEY,
    id_entidad INT UNIQUE REFERENCES configuracion.Entidades(id),
    id_tipo INT REFERENCES configuracion.TiposCliente(id),
    id_condicion_pago INT REFERENCES configuracion.CondicionesPago(id)
);

-- Proveedores también son Entidades (1:1)
CREATE TABLE configuracion.Proveedores (
    id SERIAL PRIMARY KEY,
    id_entidad INT UNIQUE REFERENCES configuracion.Entidades(id),
    id_condicion_pago INT REFERENCES configuracion.CondicionesPago(id)
);

-- Contactos adicionales de la entidad (dirección de envío, facturación)
CREATE TABLE configuracion.EntidadContactos (
    id SERIAL PRIMARY KEY,
    id_entidad INT REFERENCES configuracion.Entidades(id),
    nombre VARCHAR(150),
    email VARCHAR(100),
    telefono VARCHAR(50),
    cargo VARCHAR(100),
    es_facturacion BOOLEAN DEFAULT FALSE,  -- Dirección o contacto para facturas
    es_envio BOOLEAN DEFAULT FALSE         -- Dirección de entrega
);

-- Módulos del sistema: Ventas, Compras, Inventario, Contabilidad...
CREATE TABLE seguridad.Modulos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,   -- Ej: 'Ventas'
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE
);

-- Roles: Administrador, Facturador, Caja, Inventario...
CREATE TABLE seguridad.Roles (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT
);

-- Permisos por módulo: leer, crear, anular, editar...
CREATE TABLE seguridad.RolesPermisos (
    id SERIAL PRIMARY KEY,
    id_rol INT REFERENCES seguridad.Roles(id),
    id_modulo INT REFERENCES seguridad.Modulos(id),
    permiso VARCHAR(50)                      -- Ej: "CREAR", "ANULAR"
);

-- Usuarios del sistema
CREATE TABLE seguridad.Usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    usuario VARCHAR(100) NOT NULL UNIQUE,
    contraseña VARCHAR(255) NOT NULL,
    id_rol INT REFERENCES seguridad.Roles(id),
    estado VARCHAR(50),       -- 'Activo', 'Suspendido'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);


-- Departamentos generales: Alimentos, Ferretería...
CREATE TABLE productos.Departamentos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT
);

CREATE TABLE productos.Categorias (
    id SERIAL PRIMARY KEY,
    id_departamento INT REFERENCES productos.Departamentos(id),
    nombre VARCHAR(100) NOT NULL,       -- Ej: "Bebidas", "Harinas"
    descripcion TEXT
);

CREATE TABLE productos.Marcas (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE, -- Ej: "Polar", "Mavesa"
    descripcion TEXT
);

CREATE TABLE productos.UnidadesMedida (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE, -- Ej: Unidad, Litro
    abreviacion VARCHAR(10),            -- Ej: "UND", "L"
    activo BOOLEAN DEFAULT TRUE
);

-- Productos
CREATE TABLE productos.Productos (
    id SERIAL PRIMARY KEY,
    id_categoria INT REFERENCES productos.Categorias(id),
    id_marca INT REFERENCES productos.Marcas(id),
    id_unidad INT REFERENCES productos.UnidadesMedida(id),
    nombre VARCHAR(150) NOT NULL,        -- Ej: "Harina Pan 1Kg"
    descripcion TEXT
);

CREATE TABLE productos.Bodegas (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE, -- Ej: "Depósito Principal"
    ubicacion TEXT
);

-- Stock por bodega
CREATE TABLE productos.Inventario (
    id SERIAL PRIMARY KEY,
    id_producto INT REFERENCES productos.Productos(id),
    id_bodega INT REFERENCES productos.Bodegas(id),
    stock_actual INT DEFAULT 0,
    stock_minimo INT,
    stock_maximo INT
);

CREATE UNIQUE INDEX ux_inventario_producto_bodega
    ON productos.Inventario (id_producto, id_bodega);

-- Movimientos de inventario (entrada, salida, ajuste)
CREATE TABLE productos.MovimientosInventario (
    id SERIAL PRIMARY KEY,
    id_producto INT REFERENCES productos.Productos(id),
    id_bodega INT REFERENCES productos.Bodegas(id),
    tipo_movimiento VARCHAR(20) NOT NULL,  -- 'entrada', 'salida', 'ajuste'
    cantidad DECIMAL(10,2) NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    referencia VARCHAR(100),               -- Ej: "F001-000152"
    id_usuario INT REFERENCES seguridad.Usuarios(id),
    motivo TEXT                            -- Ej: "Ajuste por auditoría"
);

CREATE TABLE precios.ListasPrecios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100),  -- Ej: "General", "Mayorista"
    descripcion TEXT
);

CREATE TABLE precios.PreciosProductos (
    id SERIAL PRIMARY KEY,
    id_producto INT REFERENCES productos.Productos(id),
    id_lista INT REFERENCES precios.ListasPrecios(id),
    precio_costo DECIMAL(10,2),
    precio_venta DECIMAL(10,2),
    fecha_inicio DATE,
    fecha_fin DATE
);

CREATE INDEX ix_precios_producto_lista_fechas
    ON precios.PreciosProductos (id_producto, id_lista, fecha_inicio, fecha_fin);

CREATE TABLE precios.Monedas (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(10) NOT NULL UNIQUE,  -- "USD", "VES"
    nombre VARCHAR(50) NOT NULL,
    simbolo VARCHAR(5),
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE precios.TasasCambio (
    id SERIAL PRIMARY KEY,
    id_moneda INT REFERENCES precios.Monedas(id),
    cambio DECIMAL(10,4),                   -- Ej: 36.25
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE
);


-- Tipos de documentos fiscales
CREATE TABLE documentos.TiposDocumento (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,   -- "Factura", "Nota Crédito", "Compra"
    descripcion TEXT
);

-- Series o folios
CREATE TABLE documentos.SeriesDocumentos (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,   -- Ej: "F001"
    descripcion TEXT,
    id_tipo_documento INT REFERENCES documentos.TiposDocumento(id),
    prefijo VARCHAR(20),                  -- Ej: "FAC"
    ultimo_numero INT DEFAULT 0,          -- Control del correlativo
    reinicio_anual BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE documentos.MetodosPago (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,  -- "Transferencia", "Efectivo", "Pago Móvil"
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE
);

-----------------------------
--        VENTAS
-----------------------------

CREATE TABLE documentos.Ventas (
    id SERIAL PRIMARY KEY,
    serie_id INT REFERENCES documentos.SeriesDocumentos(id),
    numero_secuencia INT,                 -- Correlativo interno
    numero_documento VARCHAR(100) NOT NULL UNIQUE, -- F001-00001512
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(50),                   -- "Pendiente", "Pagada"
    id_cliente INT REFERENCES configuracion.Clientes(id),
    id_tipo_documento INT REFERENCES documentos.TiposDocumento(id),
    id_usuario INT REFERENCES seguridad.Usuarios(id),
    id_moneda INT REFERENCES precios.Monedas(id),
    tipo_cambio DECIMAL(18,6),
    total_base DECIMAL(18,2),
    total_impuestos DECIMAL(18,2),
    total DECIMAL(18,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ix_ventas_numero_documento
    ON documentos.Ventas (numero_documento);

-- Detalle de la venta
CREATE TABLE documentos.DetalleVenta (
    id SERIAL PRIMARY KEY,
    id_venta INT REFERENCES documentos.Ventas(id),
    id_producto INT REFERENCES productos.Productos(id),
    cantidad DECIMAL(10,2) NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    descuento DECIMAL(10,2) DEFAULT 0,
    subtotal DECIMAL(12,2) NOT NULL
);

-----------------------------
--        COMPRAS
-----------------------------

CREATE TABLE documentos.Compras (
    id SERIAL PRIMARY KEY,
    serie_id INT REFERENCES documentos.SeriesDocumentos(id),
    numero_secuencia INT,
    numero_documento VARCHAR(100) NOT NULL UNIQUE,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(50),
    id_proveedor INT REFERENCES configuracion.Proveedores(id),
    id_tipo_documento INT REFERENCES documentos.TiposDocumento(id),
    id_usuario INT REFERENCES seguridad.Usuarios(id),
    id_moneda INT REFERENCES precios.Monedas(id),
    tipo_cambio DECIMAL(18,6),
    total_base DECIMAL(18,2),
    total_impuestos DECIMAL(18,2),
    total DECIMAL(18,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ix_compras_numero_documento
    ON documentos.Compras (numero_documento);

CREATE TABLE documentos.DetalleCompra (
    id SERIAL PRIMARY KEY,
    id_compra INT REFERENCES documentos.Compras(id),
    id_producto INT REFERENCES productos.Productos(id),
    cantidad DECIMAL(10,2) NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    descuento DECIMAL(10,2) DEFAULT 0,
    subtotal DECIMAL(12,2) NOT NULL
);

-----------------------------
--      PAGOS Y COBROS
-----------------------------

CREATE TABLE documentos.Pagos (
    id SERIAL PRIMARY KEY,
    fecha_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_entidad INT REFERENCES configuracion.Entidades(id), -- Cliente o proveedor
    monto DECIMAL(18,2),
    id_moneda INT REFERENCES precios.Monedas(id),
    tipo_cambio DECIMAL(18,6),
    id_metodo INT REFERENCES documentos.MetodosPago(id),
    referencia VARCHAR(100),           -- Ej: Nº de transferencia
    created_by INT REFERENCES seguridad.Usuarios(id)
);

CREATE INDEX ix_pagos_entidad_fecha
    ON documentos.Pagos (id_entidad, fecha_pago);

-- Aplicación del pago a ventas o compras
CREATE TABLE documentos.PagoAplicaciones (
    id SERIAL PRIMARY KEY,
    id_pago INT REFERENCES documentos.Pagos(id),
    origen_tipo VARCHAR(20),           -- "venta" o "compra"
    id_origen INT,                     -- ID de la venta o compra
    monto_aplicado DECIMAL(18,2),
    id_moneda INT REFERENCES precios.Monedas(id),
    tipo_cambio DECIMAL(18,6),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE contabilidad.CuentasContables (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,   -- "1105", "4101"
    nombre VARCHAR(200) NOT NULL,
    tipo VARCHAR(50),                     -- Activo, Pasivo, Ingreso...
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE contabilidad.Asientos (
    id SERIAL PRIMARY KEY,
    numero VARCHAR(50) NOT NULL UNIQUE,   -- Ej: "JV-000015"
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    descripcion TEXT,
    origen_tipo VARCHAR(50),              -- Venta, Compra, Ajuste
    origen_id INT,
    creado_por INT REFERENCES seguridad.Usuarios(id),
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    aprobado BOOLEAN DEFAULT TRUE
);

CREATE TABLE contabilidad.AsientoLineas (
    id SERIAL PRIMARY KEY,
    id_asiento INT REFERENCES contabilidad.Asientos(id),
    id_cuenta INT REFERENCES contabilidad.CuentasContables(id),
    importe_debe DECIMAL(18,2) DEFAULT 0,
    importe_haber DECIMAL(18,2) DEFAULT 0,
    id_entidad INT REFERENCES configuracion.Entidades(id),
    descripcion TEXT
);

CREATE TABLE contabilidad.Impuestos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100),             -- IVA, Retención IVA...
    porcentaje DECIMAL(8,4),
    aplica_en VARCHAR(20),           -- Venta, Compra, Ambos
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE contabilidad.DetalleVentaImpuestos (
    id SERIAL PRIMARY KEY,
    id_detalle_venta INT REFERENCES documentos.DetalleVenta(id),
    id_impuesto INT REFERENCES contabilidad.Impuestos(id),
    monto DECIMAL(18,2)
);
