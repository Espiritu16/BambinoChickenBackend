-- =====================================================
-- BAMBINO MySQL 8+ full replication script
-- Source: Oracle schema BAMBINO (tables, indexes, constraints, triggers, seed data)
-- Generated on: 2026-04-16
-- =====================================================

SET NAMES utf8mb4;
SET time_zone = '+00:00';
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS bambino
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE bambino;

-- Drop in dependency-safe order
DROP TABLE IF EXISTS detalle_pedido;
DROP TABLE IF EXISTS detalle_venta;
DROP TABLE IF EXISTS movimiento_inventario;
DROP TABLE IF EXISTS pedido;
DROP TABLE IF EXISTS venta;
DROP TABLE IF EXISTS caja;
DROP TABLE IF EXISTS insumo;
DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS usuario;
DROP TABLE IF EXISTS cliente;
DROP TABLE IF EXISTS proveedor;
DROP TABLE IF EXISTS categoria;
DROP TABLE IF EXISTS local;
DROP TABLE IF EXISTS rol;

CREATE TABLE rol (
  id_rol BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL,
  descripcion VARCHAR(150) NULL,
  creado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  actualizado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  creado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  actualizado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  PRIMARY KEY (id_rol),
  UNIQUE KEY uq_rol_nombre (nombre)
) ENGINE=InnoDB;

CREATE TABLE local (
  id_local BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(120) NOT NULL,
  direccion VARCHAR(200) NULL,
  estado TINYINT NOT NULL DEFAULT 1,
  creado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  actualizado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  creado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  actualizado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  PRIMARY KEY (id_local),
  UNIQUE KEY uq_local_nombre (nombre),
  CONSTRAINT chk_local_estado CHECK (estado IN (0, 1))
) ENGINE=InnoDB;

CREATE TABLE categoria (
  id_categoria BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  descripcion VARCHAR(150) NULL,
  creado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  actualizado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  creado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  actualizado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  PRIMARY KEY (id_categoria),
  UNIQUE KEY uq_categoria_nombre (nombre)
) ENGINE=InnoDB;

CREATE TABLE proveedor (
  id_proveedor BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(120) NOT NULL,
  telefono VARCHAR(20) NULL,
  correo VARCHAR(120) NULL,
  direccion VARCHAR(200) NULL,
  estado TINYINT NOT NULL DEFAULT 1,
  creado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  actualizado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  creado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  actualizado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  PRIMARY KEY (id_proveedor),
  CONSTRAINT chk_proveedor_estado CHECK (estado IN (0, 1))
) ENGINE=InnoDB;

CREATE TABLE cliente (
  id_cliente BIGINT NOT NULL AUTO_INCREMENT,
  nombres VARCHAR(120) NOT NULL,
  dni_ruc VARCHAR(20) NULL,
  telefono VARCHAR(20) NULL,
  creado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  actualizado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  creado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  actualizado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  PRIMARY KEY (id_cliente),
  UNIQUE KEY uq_cliente_dni_ruc (dni_ruc)
) ENGINE=InnoDB;

CREATE TABLE usuario (
  id_usuario BIGINT NOT NULL AUTO_INCREMENT,
  nombres VARCHAR(100) NOT NULL,
  apellidos VARCHAR(100) NULL,
  correo VARCHAR(120) NOT NULL,
  contrasena VARCHAR(255) NOT NULL,
  estado TINYINT NOT NULL DEFAULT 1,
  id_rol BIGINT NOT NULL,
  creado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  actualizado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  creado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  actualizado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  PRIMARY KEY (id_usuario),
  UNIQUE KEY uq_usuario_correo (correo),
  KEY idx_usuario_rol (id_rol),
  CONSTRAINT chk_usuario_estado CHECK (estado IN (0, 1)),
  CONSTRAINT fk_usuario_rol FOREIGN KEY (id_rol) REFERENCES rol (id_rol)
) ENGINE=InnoDB;

CREATE TABLE producto (
  id_producto BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(120) NOT NULL,
  descripcion VARCHAR(200) NULL,
  precio DECIMAL(10,2) NOT NULL,
  stock INT NOT NULL DEFAULT 0,
  stock_minimo INT NOT NULL DEFAULT 0,
  tiempo_preparacion_min INT NOT NULL DEFAULT 0,
  estado TINYINT NOT NULL DEFAULT 1,
  id_categoria BIGINT NOT NULL,
  creado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  actualizado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  creado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  actualizado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  PRIMARY KEY (id_producto),
  KEY idx_producto_categoria (id_categoria),
  CONSTRAINT chk_producto_precio CHECK (precio >= 0),
  CONSTRAINT chk_producto_stock CHECK (stock >= 0),
  CONSTRAINT chk_producto_stock_min CHECK (stock_minimo >= 0),
  CONSTRAINT chk_producto_tiempo CHECK (tiempo_preparacion_min >= 0),
  CONSTRAINT chk_producto_estado CHECK (estado IN (0, 1)),
  CONSTRAINT fk_producto_categoria FOREIGN KEY (id_categoria) REFERENCES categoria (id_categoria)
) ENGINE=InnoDB;

CREATE TABLE insumo (
  id_insumo BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(120) NOT NULL,
  unidad_medida VARCHAR(30) NULL,
  stock_actual INT NOT NULL DEFAULT 0,
  stock_minimo INT NOT NULL DEFAULT 0,
  estado TINYINT NOT NULL DEFAULT 1,
  id_proveedor BIGINT NULL,
  creado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  actualizado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  creado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  actualizado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  PRIMARY KEY (id_insumo),
  KEY idx_insumo_proveedor (id_proveedor),
  CONSTRAINT chk_insumo_stock_actual CHECK (stock_actual >= 0),
  CONSTRAINT chk_insumo_stock_minimo CHECK (stock_minimo >= 0),
  CONSTRAINT chk_insumo_estado CHECK (estado IN (0, 1)),
  CONSTRAINT fk_insumo_proveedor FOREIGN KEY (id_proveedor) REFERENCES proveedor (id_proveedor)
) ENGINE=InnoDB;

CREATE TABLE caja (
  id_caja BIGINT NOT NULL AUTO_INCREMENT,
  fecha_apertura DATETIME(6) NOT NULL,
  fecha_cierre DATETIME(6) NULL,
  monto_inicial DECIMAL(10,2) NOT NULL,
  monto_final DECIMAL(10,2) NULL,
  tipo_cierre VARCHAR(20) NULL,
  estado VARCHAR(20) NOT NULL DEFAULT 'ABIERTA',
  id_usuario BIGINT NOT NULL,
  id_local BIGINT NOT NULL,
  creado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  actualizado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  creado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  actualizado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  PRIMARY KEY (id_caja),
  KEY idx_caja_usuario (id_usuario),
  KEY idx_caja_local (id_local),
  CONSTRAINT chk_caja_monto_inicial CHECK (monto_inicial >= 0),
  CONSTRAINT chk_caja_monto_final CHECK (monto_final IS NULL OR monto_final >= 0),
  CONSTRAINT chk_caja_monto_cierre CHECK (monto_final IS NULL OR monto_final >= monto_inicial),
  CONSTRAINT chk_caja_estado CHECK (estado IN ('ABIERTA', 'CERRADA')),
  CONSTRAINT chk_caja_tipo_cierre CHECK (tipo_cierre IS NULL OR tipo_cierre IN ('CIEGO', 'CLASICO')),
  CONSTRAINT chk_caja_fecha_cierre CHECK (fecha_cierre IS NULL OR fecha_cierre >= fecha_apertura),
  CONSTRAINT fk_caja_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario),
  CONSTRAINT fk_caja_local FOREIGN KEY (id_local) REFERENCES local (id_local)
) ENGINE=InnoDB;

CREATE TABLE venta (
  id_venta BIGINT NOT NULL AUTO_INCREMENT,
  fecha_venta DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  tipo_comprobante VARCHAR(20) NOT NULL,
  serie_comprobante VARCHAR(10) NULL,
  numero_comprobante VARCHAR(20) NULL,
  metodo_pago VARCHAR(30) NOT NULL,
  subtotal DECIMAL(10,2) NOT NULL,
  porcentaje_igv DECIMAL(5,2) NOT NULL DEFAULT 18,
  igv DECIMAL(10,2) NOT NULL,
  total DECIMAL(10,2) NOT NULL,
  estado VARCHAR(20) NOT NULL DEFAULT 'REGISTRADA',
  fecha_anulacion DATETIME(6) NULL,
  motivo_anulacion VARCHAR(250) NULL,
  id_usuario BIGINT NOT NULL,
  id_cliente BIGINT NULL,
  id_caja BIGINT NULL,
  id_local BIGINT NOT NULL,
  creado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  actualizado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  creado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  actualizado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  PRIMARY KEY (id_venta),
  UNIQUE KEY uq_venta_comprobante (serie_comprobante, numero_comprobante),
  KEY idx_venta_usuario (id_usuario),
  KEY idx_venta_cliente (id_cliente),
  KEY idx_venta_caja (id_caja),
  KEY idx_venta_local (id_local),
  CONSTRAINT chk_venta_comprobante CHECK (tipo_comprobante IN ('BOLETA', 'FACTURA')),
  CONSTRAINT chk_venta_metodo_pago CHECK (metodo_pago IN ('EFECTIVO', 'YAPE', 'PLIN', 'TARJETA')),
  CONSTRAINT chk_venta_subtotal CHECK (subtotal >= 0),
  CONSTRAINT chk_venta_porc_igv CHECK (porcentaje_igv >= 0 AND porcentaje_igv <= 100),
  CONSTRAINT chk_venta_igv CHECK (igv >= 0),
  CONSTRAINT chk_venta_total CHECK (total >= 0),
  CONSTRAINT chk_venta_total_calc CHECK (total = subtotal + igv),
  CONSTRAINT chk_venta_estado CHECK (estado IN ('REGISTRADA', 'ANULADA')),
  CONSTRAINT chk_venta_comp_serie_numero CHECK (
    (serie_comprobante IS NULL AND numero_comprobante IS NULL)
    OR
    (serie_comprobante IS NOT NULL AND numero_comprobante IS NOT NULL)
  ),
  CONSTRAINT fk_venta_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario),
  CONSTRAINT fk_venta_cliente FOREIGN KEY (id_cliente) REFERENCES cliente (id_cliente),
  CONSTRAINT fk_venta_caja FOREIGN KEY (id_caja) REFERENCES caja (id_caja),
  CONSTRAINT fk_venta_local FOREIGN KEY (id_local) REFERENCES local (id_local)
) ENGINE=InnoDB;

CREATE TABLE pedido (
  id_pedido BIGINT NOT NULL AUTO_INCREMENT,
  fecha_pedido DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  nro_orden BIGINT NOT NULL,
  nro_comanda VARCHAR(30) NULL,
  estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
  observacion VARCHAR(200) NULL,
  fecha_listo DATETIME(6) NULL,
  fecha_entrega DATETIME(6) NULL,
  id_usuario BIGINT NOT NULL,
  id_venta BIGINT NULL,
  creado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  actualizado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  creado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  actualizado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  PRIMARY KEY (id_pedido),
  UNIQUE KEY uq_pedido_nro_orden (nro_orden),
  UNIQUE KEY uq_pedido_nro_comanda (nro_comanda),
  KEY idx_pedido_usuario (id_usuario),
  KEY idx_pedido_venta (id_venta),
  CONSTRAINT chk_pedido_estado CHECK (estado IN ('PENDIENTE', 'EN_PREPARACION', 'LISTO', 'ENTREGADO', 'ANULADO')),
  CONSTRAINT chk_pedido_fecha_listo CHECK (fecha_listo IS NULL OR fecha_listo >= fecha_pedido),
  CONSTRAINT chk_pedido_fecha_entrega CHECK (fecha_entrega IS NULL OR fecha_entrega >= fecha_pedido),
  CONSTRAINT chk_pedido_fechas CHECK (fecha_entrega IS NULL OR fecha_listo IS NULL OR fecha_entrega >= fecha_listo),
  CONSTRAINT fk_pedido_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario),
  CONSTRAINT fk_pedido_venta FOREIGN KEY (id_venta) REFERENCES venta (id_venta)
) ENGINE=InnoDB;

CREATE TABLE detalle_venta (
  id_detalle_venta BIGINT NOT NULL AUTO_INCREMENT,
  id_venta BIGINT NOT NULL,
  id_producto BIGINT NOT NULL,
  cantidad INT NOT NULL,
  precio_unitario DECIMAL(10,2) NOT NULL,
  importe DECIMAL(10,2) NOT NULL,
  creado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  actualizado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  creado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  actualizado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  PRIMARY KEY (id_detalle_venta),
  KEY idx_detalle_venta_venta (id_venta),
  KEY idx_detalle_venta_producto (id_producto),
  CONSTRAINT chk_detalle_venta_cantidad CHECK (cantidad > 0),
  CONSTRAINT chk_detalle_venta_precio CHECK (precio_unitario >= 0),
  CONSTRAINT chk_detalle_venta_importe CHECK (importe >= 0),
  CONSTRAINT chk_detalle_venta_importe_calc CHECK (importe = cantidad * precio_unitario),
  CONSTRAINT fk_detalle_venta_venta FOREIGN KEY (id_venta) REFERENCES venta (id_venta),
  CONSTRAINT fk_detalle_venta_producto FOREIGN KEY (id_producto) REFERENCES producto (id_producto)
) ENGINE=InnoDB;

CREATE TABLE detalle_pedido (
  id_detalle_pedido BIGINT NOT NULL AUTO_INCREMENT,
  id_pedido BIGINT NOT NULL,
  id_producto BIGINT NOT NULL,
  cantidad INT NOT NULL,
  observacion VARCHAR(200) NULL,
  creado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  actualizado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  creado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  actualizado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  PRIMARY KEY (id_detalle_pedido),
  KEY idx_detalle_pedido_pedido (id_pedido),
  KEY idx_detalle_pedido_producto (id_producto),
  CONSTRAINT chk_detalle_pedido_cantidad CHECK (cantidad > 0),
  CONSTRAINT fk_detalle_pedido_pedido FOREIGN KEY (id_pedido) REFERENCES pedido (id_pedido),
  CONSTRAINT fk_detalle_pedido_producto FOREIGN KEY (id_producto) REFERENCES producto (id_producto)
) ENGINE=InnoDB;

CREATE TABLE movimiento_inventario (
  id_movimiento BIGINT NOT NULL AUTO_INCREMENT,
  tipo_movimiento VARCHAR(20) NOT NULL,
  fecha_movimiento DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  cantidad INT NOT NULL,
  stock_resultante INT NOT NULL,
  referencia VARCHAR(60) NULL,
  descripcion VARCHAR(200) NULL,
  id_insumo BIGINT NULL,
  id_producto BIGINT NULL,
  id_usuario BIGINT NOT NULL,
  creado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  actualizado_en DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  creado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  actualizado_por VARCHAR(120) NOT NULL DEFAULT 'SYSTEM',
  PRIMARY KEY (id_movimiento),
  KEY idx_movimiento_insumo (id_insumo),
  KEY idx_movimiento_producto (id_producto),
  KEY idx_movimiento_usuario (id_usuario),
  CONSTRAINT chk_movimiento_tipo CHECK (tipo_movimiento IN ('ENTRADA', 'SALIDA', 'AJUSTE', 'MERMA')),
  CONSTRAINT chk_movimiento_cantidad CHECK (cantidad > 0),
  CONSTRAINT chk_movimiento_stock_resultante CHECK (stock_resultante >= 0),
  CONSTRAINT chk_movimiento_xor CHECK (
    (id_insumo IS NOT NULL AND id_producto IS NULL)
    OR
    (id_insumo IS NULL AND id_producto IS NOT NULL)
  ),
  CONSTRAINT fk_movimiento_insumo FOREIGN KEY (id_insumo) REFERENCES insumo (id_insumo),
  CONSTRAINT fk_movimiento_producto FOREIGN KEY (id_producto) REFERENCES producto (id_producto),
  CONSTRAINT fk_movimiento_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB;

-- Seed data from Oracle instance
INSERT INTO rol (id_rol, nombre, descripcion, creado_en, actualizado_en, creado_por, actualizado_por) VALUES
(1, 'ADMINISTRADOR', 'Control total del sistema', '2026-04-09 06:03:35.113638', '2026-04-09 06:03:35.817390', 'SYSTEM', 'SYSTEM'),
(2, 'CAJERO', 'Registra ventas y caja', '2026-04-09 06:03:35.113638', '2026-04-09 06:03:35.818103', 'SYSTEM', 'SYSTEM'),
(3, 'MOZO', 'Gestiona pedidos', '2026-04-09 06:03:35.113638', '2026-04-09 06:03:35.818122', 'SYSTEM', 'SYSTEM');

INSERT INTO local (id_local, nombre, direccion, estado, creado_en, actualizado_en, creado_por, actualizado_por) VALUES
(1, 'LOCAL PRINCIPAL', 'Lima', 1, '2026-04-09 06:03:35.263497', '2026-04-09 06:03:35.822694', 'SYSTEM', 'SYSTEM');

INSERT INTO categoria (id_categoria, nombre, descripcion, creado_en, actualizado_en, creado_por, actualizado_por) VALUES
(1, 'POLLOS', 'Productos principales', '2026-04-09 06:03:35.308614', '2026-04-09 06:03:35.830493', 'SYSTEM', 'SYSTEM'),
(2, 'BEBIDAS', 'Bebidas del local', '2026-04-09 06:03:35.308614', '2026-04-09 06:03:35.830533', 'SYSTEM', 'SYSTEM'),
(3, 'ACOMPAÑAMIENTOS', 'Guarniciones', '2026-04-09 06:03:35.308614', '2026-04-09 06:03:35.830547', 'SYSTEM', 'SYSTEM');

INSERT INTO proveedor (id_proveedor, nombre, telefono, correo, direccion, estado, creado_en, actualizado_en, creado_por, actualizado_por) VALUES
(1, 'Proveedor General', '999888777', 'proveedor@bambino.com', 'Lima', 1, '2026-04-09 06:03:35.545848', '2026-04-09 06:03:35.857336', 'SYSTEM', 'SYSTEM'),
(2, 'Proveedor General', '999888777', 'proveedor@bambino.com', 'Lima', 1, '2026-04-09 06:03:35.545848', '2026-04-09 06:03:35.857373', 'SYSTEM', 'SYSTEM');

INSERT INTO usuario (id_usuario, nombres, apellidos, correo, contrasena, estado, id_rol, creado_en, actualizado_en, creado_por, actualizado_por) VALUES
(1, 'Kevin', 'Espiritu', 'admin@bambino.com', '$2a$10$abcdefghijklmnopqrstuv/0123456789ABCDEfghijKLMNO', 1, 1, '2026-04-09 06:03:35.289063', '2026-04-09 06:03:35.826511', 'SYSTEM', 'SYSTEM'),
(2, 'Mauricio', 'Palma', 'cajero@bambino.com', '$2a$10$abcdefghijklmnopqrstuv/0123456789ABCDEfghijKLMNO', 1, 2, '2026-04-09 06:03:35.289063', '2026-04-09 06:03:35.826575', 'SYSTEM', 'SYSTEM'),
(3, 'Jaren', 'Sullcapuma', 'mozo@bambino.com', '$2a$10$abcdefghijklmnopqrstuv/0123456789ABCDEfghijKLMNO', 1, 3, '2026-04-09 06:03:35.289063', '2026-04-09 06:03:35.826588', 'SYSTEM', 'SYSTEM');

INSERT INTO producto (id_producto, nombre, descripcion, precio, stock, stock_minimo, tiempo_preparacion_min, estado, id_categoria, creado_en, actualizado_en, creado_por, actualizado_por) VALUES
(1, 'Pollo a la brasa', 'Pollo entero con papas', 65.00, 20, 5, 35, 1, 1, '2026-04-09 06:03:35.336732', '2026-04-09 06:03:35.835812', 'SYSTEM', 'SYSTEM'),
(2, 'Gaseosa 1.5L', 'Bebida gaseosa', 8.00, 30, 5, 0, 1, 2, '2026-04-09 06:03:35.336732', '2026-04-09 06:03:35.835842', 'SYSTEM', 'SYSTEM'),
(3, 'Porción de papas', 'Acompañamiento', 12.00, 40, 10, 12, 1, 3, '2026-04-09 06:03:35.336732', '2026-04-09 06:03:35.835853', 'SYSTEM', 'SYSTEM'),
(4, 'Pollo a la brasa', 'Pollo entero con papas', 65.00, 20, 5, 35, 1, 1, '2026-04-09 06:03:35.336732', '2026-04-09 06:03:35.835860', 'SYSTEM', 'SYSTEM'),
(5, 'Gaseosa 1.5L', 'Bebida gaseosa', 8.00, 30, 5, 0, 1, 2, '2026-04-09 06:03:35.336732', '2026-04-09 06:03:35.835866', 'SYSTEM', 'SYSTEM'),
(6, 'Porción de papas', 'Acompañamiento', 12.00, 40, 10, 12, 1, 3, '2026-04-09 06:03:35.336732', '2026-04-09 06:03:35.835873', 'SYSTEM', 'SYSTEM');

INSERT INTO insumo (id_insumo, nombre, unidad_medida, stock_actual, stock_minimo, estado, id_proveedor, creado_en, actualizado_en, creado_por, actualizado_por) VALUES
(1, 'Pollo entero', 'UNIDAD', 50, 10, 1, 1, '2026-04-09 06:03:35.561627', '2026-04-09 06:03:35.859853', 'SYSTEM', 'SYSTEM'),
(2, 'Papa', 'KILOGRAMO', 100, 20, 1, 1, '2026-04-09 06:03:35.561627', '2026-04-09 06:03:35.859870', 'SYSTEM', 'SYSTEM'),
(3, 'Pollo entero', 'UNIDAD', 50, 10, 1, 1, '2026-04-09 06:03:35.561627', '2026-04-09 06:03:35.860062', 'SYSTEM', 'SYSTEM'),
(4, 'Papa', 'KILOGRAMO', 100, 20, 1, 1, '2026-04-09 06:03:35.561627', '2026-04-09 06:03:35.860071', 'SYSTEM', 'SYSTEM');

-- Audit triggers equivalent to Oracle (INSERT + UPDATE per table)
DELIMITER $$

CREATE TRIGGER trg_rol_auditoria_bi
BEFORE INSERT ON rol
FOR EACH ROW
BEGIN
  IF NEW.creado_en IS NULL THEN SET NEW.creado_en = CURRENT_TIMESTAMP(6); END IF;
  IF NEW.creado_por IS NULL OR NEW.creado_por = '' THEN SET NEW.creado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$
CREATE TRIGGER trg_rol_auditoria_bu
BEFORE UPDATE ON rol
FOR EACH ROW
BEGIN
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$

CREATE TRIGGER trg_local_auditoria_bi
BEFORE INSERT ON local
FOR EACH ROW
BEGIN
  IF NEW.creado_en IS NULL THEN SET NEW.creado_en = CURRENT_TIMESTAMP(6); END IF;
  IF NEW.creado_por IS NULL OR NEW.creado_por = '' THEN SET NEW.creado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$
CREATE TRIGGER trg_local_auditoria_bu
BEFORE UPDATE ON local
FOR EACH ROW
BEGIN
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$

CREATE TRIGGER trg_categoria_auditoria_bi
BEFORE INSERT ON categoria
FOR EACH ROW
BEGIN
  IF NEW.creado_en IS NULL THEN SET NEW.creado_en = CURRENT_TIMESTAMP(6); END IF;
  IF NEW.creado_por IS NULL OR NEW.creado_por = '' THEN SET NEW.creado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$
CREATE TRIGGER trg_categoria_auditoria_bu
BEFORE UPDATE ON categoria
FOR EACH ROW
BEGIN
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$

CREATE TRIGGER trg_cliente_auditoria_bi
BEFORE INSERT ON cliente
FOR EACH ROW
BEGIN
  IF NEW.creado_en IS NULL THEN SET NEW.creado_en = CURRENT_TIMESTAMP(6); END IF;
  IF NEW.creado_por IS NULL OR NEW.creado_por = '' THEN SET NEW.creado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$
CREATE TRIGGER trg_cliente_auditoria_bu
BEFORE UPDATE ON cliente
FOR EACH ROW
BEGIN
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$

CREATE TRIGGER trg_usuario_auditoria_bi
BEFORE INSERT ON usuario
FOR EACH ROW
BEGIN
  IF NEW.creado_en IS NULL THEN SET NEW.creado_en = CURRENT_TIMESTAMP(6); END IF;
  IF NEW.creado_por IS NULL OR NEW.creado_por = '' THEN SET NEW.creado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$
CREATE TRIGGER trg_usuario_auditoria_bu
BEFORE UPDATE ON usuario
FOR EACH ROW
BEGIN
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$

CREATE TRIGGER trg_producto_auditoria_bi
BEFORE INSERT ON producto
FOR EACH ROW
BEGIN
  IF NEW.creado_en IS NULL THEN SET NEW.creado_en = CURRENT_TIMESTAMP(6); END IF;
  IF NEW.creado_por IS NULL OR NEW.creado_por = '' THEN SET NEW.creado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$
CREATE TRIGGER trg_producto_auditoria_bu
BEFORE UPDATE ON producto
FOR EACH ROW
BEGIN
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$

CREATE TRIGGER trg_proveedor_auditoria_bi
BEFORE INSERT ON proveedor
FOR EACH ROW
BEGIN
  IF NEW.creado_en IS NULL THEN SET NEW.creado_en = CURRENT_TIMESTAMP(6); END IF;
  IF NEW.creado_por IS NULL OR NEW.creado_por = '' THEN SET NEW.creado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$
CREATE TRIGGER trg_proveedor_auditoria_bu
BEFORE UPDATE ON proveedor
FOR EACH ROW
BEGIN
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$

CREATE TRIGGER trg_insumo_auditoria_bi
BEFORE INSERT ON insumo
FOR EACH ROW
BEGIN
  IF NEW.creado_en IS NULL THEN SET NEW.creado_en = CURRENT_TIMESTAMP(6); END IF;
  IF NEW.creado_por IS NULL OR NEW.creado_por = '' THEN SET NEW.creado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$
CREATE TRIGGER trg_insumo_auditoria_bu
BEFORE UPDATE ON insumo
FOR EACH ROW
BEGIN
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$

CREATE TRIGGER trg_caja_auditoria_bi
BEFORE INSERT ON caja
FOR EACH ROW
BEGIN
  IF NEW.creado_en IS NULL THEN SET NEW.creado_en = CURRENT_TIMESTAMP(6); END IF;
  IF NEW.creado_por IS NULL OR NEW.creado_por = '' THEN SET NEW.creado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$
CREATE TRIGGER trg_caja_auditoria_bu
BEFORE UPDATE ON caja
FOR EACH ROW
BEGIN
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$

CREATE TRIGGER trg_venta_auditoria_bi
BEFORE INSERT ON venta
FOR EACH ROW
BEGIN
  IF NEW.creado_en IS NULL THEN SET NEW.creado_en = CURRENT_TIMESTAMP(6); END IF;
  IF NEW.creado_por IS NULL OR NEW.creado_por = '' THEN SET NEW.creado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$
CREATE TRIGGER trg_venta_auditoria_bu
BEFORE UPDATE ON venta
FOR EACH ROW
BEGIN
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$

CREATE TRIGGER trg_pedido_auditoria_bi
BEFORE INSERT ON pedido
FOR EACH ROW
BEGIN
  IF NEW.creado_en IS NULL THEN SET NEW.creado_en = CURRENT_TIMESTAMP(6); END IF;
  IF NEW.creado_por IS NULL OR NEW.creado_por = '' THEN SET NEW.creado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$
CREATE TRIGGER trg_pedido_auditoria_bu
BEFORE UPDATE ON pedido
FOR EACH ROW
BEGIN
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$

CREATE TRIGGER trg_detalle_venta_auditoria_bi
BEFORE INSERT ON detalle_venta
FOR EACH ROW
BEGIN
  IF NEW.creado_en IS NULL THEN SET NEW.creado_en = CURRENT_TIMESTAMP(6); END IF;
  IF NEW.creado_por IS NULL OR NEW.creado_por = '' THEN SET NEW.creado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$
CREATE TRIGGER trg_detalle_venta_auditoria_bu
BEFORE UPDATE ON detalle_venta
FOR EACH ROW
BEGIN
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$

CREATE TRIGGER trg_detalle_pedido_auditoria_bi
BEFORE INSERT ON detalle_pedido
FOR EACH ROW
BEGIN
  IF NEW.creado_en IS NULL THEN SET NEW.creado_en = CURRENT_TIMESTAMP(6); END IF;
  IF NEW.creado_por IS NULL OR NEW.creado_por = '' THEN SET NEW.creado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$
CREATE TRIGGER trg_detalle_pedido_auditoria_bu
BEFORE UPDATE ON detalle_pedido
FOR EACH ROW
BEGIN
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$

CREATE TRIGGER trg_movimiento_inventario_auditoria_bi
BEFORE INSERT ON movimiento_inventario
FOR EACH ROW
BEGIN
  IF NEW.creado_en IS NULL THEN SET NEW.creado_en = CURRENT_TIMESTAMP(6); END IF;
  IF NEW.creado_por IS NULL OR NEW.creado_por = '' THEN SET NEW.creado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$
CREATE TRIGGER trg_movimiento_inventario_auditoria_bu
BEFORE UPDATE ON movimiento_inventario
FOR EACH ROW
BEGIN
  SET NEW.actualizado_en = CURRENT_TIMESTAMP(6);
  IF NEW.actualizado_por IS NULL OR NEW.actualizado_por = '' THEN SET NEW.actualizado_por = SUBSTRING_INDEX(CURRENT_USER(), '@', 1); END IF;
END$$

DELIMITER ;

SET FOREIGN_KEY_CHECKS = 1;

-- End
