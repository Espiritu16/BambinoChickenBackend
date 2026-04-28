# Backend por Modulos - Estado Hito 2

## 1. Alcance actual
En el backend se trabaja por modulos funcionales. Los modulos con estructura base (`controller`, `service`, `dto`, `repository`) quedan organizados asi:

- `auth`
- `admin/usuarios`
- `caja`
- `ventas`
- `pedidos`
- `inventario`
- `proveedores`
- `reportes`

Los modulos `caja`, `ventas`, `pedidos`, `inventario`, `proveedores` y `reportes` ya tienen contratos REST definidos y DTOs de entrada/salida listos para conectar implementacion completa en Hito 3.

## 2. Contratos REST del backend

### 2.1 Auth
- `POST /api/v1/auth/login`
- `POST /api/v1/auth/refresh`

### 2.2 Admin (Usuarios y Roles)
- `GET /api/v1/admin/usuarios?q=...`
- `GET /api/v1/admin/roles`
- `POST /api/v1/admin/usuarios`
- `PUT /api/v1/admin/usuarios/{idUsuario}`
- `PATCH /api/v1/admin/usuarios/{idUsuario}/inactivar`

### 2.3 Caja
- `GET /api/v1/caja`
- `GET /api/v1/caja/abierta?idLocal=...`
- `POST /api/v1/caja/aperturas`
- `POST /api/v1/caja/{idCaja}/cierres`

### 2.4 Ventas
- `GET /api/v1/ventas`
- `POST /api/v1/ventas`
- `POST /api/v1/ventas/{idVenta}/anulacion`
- `GET /api/v1/ventas/resumen-diario?idLocal=...`

### 2.5 Pedidos
- `GET /api/v1/pedidos`
- `POST /api/v1/pedidos`
- `PATCH /api/v1/pedidos/{idPedido}/estado`
- `POST /api/v1/pedidos/{idPedido}/anulacion`

### 2.6 Inventario
- `GET /api/v1/inventario/productos`
- `GET /api/v1/inventario/movimientos`
- `POST /api/v1/inventario/movimientos`

### 2.7 Proveedores
- `GET /api/v1/proveedores?q=...`
- `POST /api/v1/proveedores`
- `PUT /api/v1/proveedores/{idProveedor}`
- `PATCH /api/v1/proveedores/{idProveedor}/inactivar`

### 2.8 Reportes
- `POST /api/v1/reportes/ventas`
- `GET /api/v1/reportes/kpis?idLocal=...`

## 3. DTOs principales por modulo

### 3.1 Caja
- Entrada: `AbrirCajaRequest`, `CerrarCajaRequest`
- Salida: `CajaResponse`

### 3.2 Ventas
- Entrada: `RegistrarVentaRequest`, `AnularVentaRequest`
- Salida: `VentaResponse`, `VentaResumenDiarioResponse`

### 3.3 Pedidos
- Entrada: `PedidoUpsertRequest`, `ActualizarEstadoPedidoRequest`, `AnularPedidoRequest`
- Salida: `PedidoResponse`

### 3.4 Inventario
- Entrada: `MovimientoInventarioRequest`
- Salida: `InventarioProductoResponse`, `MovimientoInventarioResponse`

### 3.5 Proveedores
- Entrada: `ProveedorUpsertRequest`
- Salida: `ProveedorResponse`

### 3.6 Reportes
- Entrada: `ReporteVentasRequest`
- Salida: `ReporteVentasItemResponse`, `ReporteKpiResponse`

## 4. Tablas de base de datos por modulo

### 4.1 Auth
- `USUARIO`
- `ROL`
- `LOCAL`
- `AUTH_SESSION` (sesiones)

### 4.2 Admin
- `USUARIO`
- `ROL`
- `LOCAL`

### 4.3 Caja
- `CAJA`
- `USUARIO`
- `LOCAL`

### 4.4 Ventas
- `VENTA`
- `DETALLE_VENTA`
- `PEDIDO`
- `USUARIO`
- `CLIENTE`
- `CAJA`

### 4.5 Pedidos
- `PEDIDO`
- `DETALLE_PEDIDO`
- `CLIENTE`
- `USUARIO`
- `LOCAL`

### 4.6 Inventario
- `INSUMO`
- `MOVIMIENTO_INVENTARIO`
- `USUARIO`
- `LOCAL`

### 4.7 Proveedores
- `PROVEEDOR`
- `INSUMO` (relacion de abastecimiento)

### 4.8 Reportes
- `VENTA`
- `DETALLE_VENTA`
- `PEDIDO`
- `CAJA`
- `MOVIMIENTO_INVENTARIO`
- `PRODUCTO`
- `LOCAL`

## 5. Reglas de negocio por modulo

### 5.1 Caja
- Solo una caja abierta por local en un mismo momento.
- Para cerrar caja se registra monto de cierre y observacion.
- No se genera cierre si la caja no esta abierta.

### 5.2 Ventas
- Una venta valida queda asociada a caja abierta y usuario activo.
- La anulacion registra motivo y auditoria.
- El resumen diario consolida ventas por local y fecha.

### 5.3 Pedidos
- El pedido inicia en estado operativo y se actualiza por flujo (`PENDIENTE`, `EN_PREPARACION`, `LISTO`, `ENTREGADO`, `ANULADO`).
- No se permite pasar a estados inconsistentes con el flujo.
- La anulacion exige motivo.

### 5.4 Inventario
- Cada movimiento de inventario tiene tipo (`ENTRADA`, `SALIDA`, `AJUSTE`) y usuario responsable.
- No se acepta salida con stock insuficiente.
- Todo movimiento deja traza para auditoria.

### 5.5 Proveedores
- Se controla unicidad por RUC.
- La inactivacion conserva historial y evita borrado fisico.
- Los datos de contacto se mantienen como obligatorios para compra/reposicion.

### 5.6 Reportes
- Los reportes se generan por rango de fechas y opcionalmente por local.
- Los KPIs reutilizan informacion consolidada de ventas, pedidos e inventario.
- Se preserva coherencia entre modulo operativo y modulo analitico.

## 6. Endpoints que se implementaran en el transcurso del proyecto
Durante Hito 3 se completara la logica de repositorios y reglas transaccionales para los contratos ya definidos en:

- `caja`
- `ventas`
- `pedidos`
- `inventario`
- `proveedores`
- `reportes`

La estructura base ya permite integrar procedimientos SQL/PLSQL, validaciones finales y pruebas por modulo sin romper los contratos expuestos al frontend.
