CREATE SCHEMA IF NOT EXISTS configuracion;
CREATE SCHEMA IF NOT EXISTS productos;
CREATE SCHEMA IF NOT EXISTS precios;
CREATE SCHEMA IF NOT EXISTS seguridad;
CREATE SCHEMA IF NOT EXISTS documentos;
CREATE SCHEMA IF NOT EXISTS contabilidad;

-- MÓDULO DE CONFIGURACIÓN Y ENTIDADES

CREATE TABLE configuracion.TiposCliente (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT
);

CREATE TABLE configuracion.CondicionesPago (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT
);

CREATE TABLE configuracion.TiposIdentificacion (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT
);

CREATE TABLE configuracion.Entidades (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    apellido VARCHAR(150),
    id_tipo_identificacion INT REFERENCES configuracion.TiposIdentificacion(id),
    numero_identificacion VARCHAR(50) UNIQUE,
    telefono_celular VARCHAR(20),
    direccion TEXT,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE configuracion.Clientes (
    id SERIAL PRIMARY KEY,
    id_entidad INT UNIQUE REFERENCES configuracion.Entidades(id),
    id_tipo INT REFERENCES configuracion.TiposCliente(id),
    id_condicion_pago INT REFERENCES configuracion.CondicionesPago(id)
);

CREATE TABLE configuracion.Proveedores (
    id SERIAL PRIMARY KEY,
    id_entidad INT UNIQUE REFERENCES configuracion.Entidades(id),
    id_condicion_pago INT REFERENCES configuracion.CondicionesPago(id)
);

CREATE TABLE configuracion.EntidadContactos (
    id SERIAL PRIMARY KEY,
    id_entidad INT REFERENCES configuracion.Entidades(id),
    nombre VARCHAR(150),
    email VARCHAR(100),
    telefono VARCHAR(50),
    cargo VARCHAR(100),
    es_facturacion BOOLEAN DEFAULT FALSE,
    es_envio BOOLEAN DEFAULT FALSE
);

-- MÓDULO SEGURIDAD

CREATE TABLE seguridad.Modulos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,   -- Ej: 'Ventas', 'Compras'
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE
);


CREATE TABLE seguridad.Roles (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT
);

CREATE TABLE seguridad.RolesPermisos (
    id SERIAL PRIMARY KEY,
    id_rol INT REFERENCES seguridad.Roles(id),
    id_modulo INT REFERENCES seguridad.Modulos(id),
    permiso VARCHAR(50)
);

CREATE TABLE seguridad.Usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    usuario VARCHAR(100) NOT NULL UNIQUE,
    contraseña VARCHAR(255) NOT NULL,
    id_rol INT REFERENCES seguridad.Roles(id),
    estado VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- MÓDULO PRODUCTOS E INVENTARIO

CREATE TABLE productos.Departamentos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT
);

CREATE TABLE productos.Categorias (
    id SERIAL PRIMARY KEY,
    id_departamento INT REFERENCES productos.Departamentos(id),
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT
);

CREATE TABLE productos.Marcas (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT
);

CREATE TABLE productos.UnidadesMedida (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    abreviacion VARCHAR(10),
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE productos.Productos (
    id SERIAL PRIMARY KEY,
    id_categoria INT REFERENCES productos.Categorias(id),
    id_marca INT REFERENCES productos.Marcas(id),
    id_unidad INT REFERENCES productos.UnidadesMedida(id),
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT
);

CREATE TABLE productos.Bodegas (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    ubicacion TEXT
);

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

CREATE TABLE productos.MovimientosInventario (
    id SERIAL PRIMARY KEY,
    id_producto INT REFERENCES productos.Productos(id),
    id_bodega INT REFERENCES productos.Bodegas(id),
    tipo_movimiento VARCHAR(20) NOT NULL,
    cantidad DECIMAL(10,2) NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    referencia VARCHAR(100),
    id_usuario INT REFERENCES seguridad.Usuarios(id),
    motivo TEXT
);

CREATE INDEX ix_movimientos_producto_fecha
    ON productos.MovimientosInventario (id_producto, fecha);

-- MÓDULO PRECIOS Y MONEDAS

CREATE TABLE precios.ListasPrecios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
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
    codigo VARCHAR(10) NOT NULL UNIQUE,
    nombre VARCHAR(50) NOT NULL,
    simbolo VARCHAR(5),
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE precios.TasasCambio (
    id SERIAL PRIMARY KEY,
    id_moneda INT REFERENCES precios.Monedas(id),
    cambio DECIMAL(10,4),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE
);

-- MÓDULO DE DOCUMENTOS Y SERIES

CREATE TABLE documentos.TiposDocumento (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT
);

CREATE TABLE documentos.SeriesDocumentos (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    descripcion TEXT,
    id_tipo_documento INT REFERENCES documentos.TiposDocumento(id),
    prefijo VARCHAR(20),
    ultimo_numero INT DEFAULT 0,
    reinicio_anual BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE documentos.MetodosPago (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE documentos.Ventas (
    id SERIAL PRIMARY KEY,
    serie_id INT REFERENCES documentos.SeriesDocumentos(id),
    numero_secuencia INT,
    numero_documento VARCHAR(100) NOT NULL UNIQUE,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(50),
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

CREATE TABLE documentos.DetalleVenta (
    id SERIAL PRIMARY KEY,
    id_venta INT REFERENCES documentos.Ventas(id),
    id_producto INT REFERENCES productos.Productos(id),
    cantidad DECIMAL(10,2) NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    descuento DECIMAL(10,2) DEFAULT 0,
    subtotal DECIMAL(12,2) NOT NULL
);

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

CREATE TABLE documentos.Pagos (
    id SERIAL PRIMARY KEY,
    fecha_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_entidad INT REFERENCES configuracion.Entidades(id),
    monto DECIMAL(18,2),
    id_moneda INT REFERENCES precios.Monedas(id),
    tipo_cambio DECIMAL(18,6),
    id_metodo INT REFERENCES documentos.MetodosPago(id),
    referencia VARCHAR(100),
    created_by INT REFERENCES seguridad.Usuarios(id)
);

CREATE INDEX ix_pagos_entidad_fecha
    ON documentos.Pagos (id_entidad, fecha_pago);

CREATE TABLE documentos.PagoAplicaciones (
    id SERIAL PRIMARY KEY,
    id_pago INT REFERENCES documentos.Pagos(id),
    origen_tipo VARCHAR(20),
    id_origen INT,
    monto_aplicado DECIMAL(18,2),
    id_moneda INT REFERENCES precios.Monedas(id),
    tipo_cambio DECIMAL(18,6),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- MÓDULO CONTABLE

CREATE TABLE contabilidad.CuentasContables (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(200) NOT NULL,
    tipo VARCHAR(50),
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE contabilidad.Asientos (
    id SERIAL PRIMARY KEY,
    numero VARCHAR(50) NOT NULL UNIQUE,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    descripcion TEXT,
    origen_tipo VARCHAR(50),
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
    nombre VARCHAR(100),
    porcentaje DECIMAL(8,4),
    aplica_en VARCHAR(20),
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE contabilidad.DetalleVentaImpuestos (
    id SERIAL PRIMARY KEY,
    id_detalle_venta INT REFERENCES documentos.DetalleVenta(id),
    id_impuesto INT REFERENCES contabilidad.Impuestos(id),
    monto DECIMAL(18,2)
);
