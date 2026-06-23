# Guía de Demostración — Sistema de Gestión de Compras y Pagos

Camino de ejecución para presentar el sistema completo. Seguí los pasos **en orden**:
cada módulo usa datos cargados en el anterior, y las ventanas cargan sus listas
desplegables al abrirse (si abriste una ventana antes de cargar un dato, cerrala y
volvé a abrirla para que refresque).

> **Arranque:** ejecutá `src/Main/App.java`. Se abre el **Menú Principal**.
> Dejá visible la **consola** de VS Code: varios botones imprimen el detalle ahí.

---

## Paso 1 — Rubros

Abrí **Rubros** y cargá uno:

| ID | Nombre | Descripción |
|----|--------|-------------|
| 1 | Insumos descartables | Insumos descartables y estériles |

➡️ Agregar. Aparece en la tabla.

*Restricción que se ve:* si intentás cargar otro con ID `1`, lo rechaza (ID único).

---

## Paso 2 — Proveedores

Abrí **Proveedores** y cargá uno:

| Campo | Valor |
|-------|-------|
| CUIT | 30-11111111-1 |
| Condición Impositiva | RESPONSABLE_INSCRIPTO |
| Razón Social | Distribuidora Médica SA |
| Nombre Comercial | DistriMed |
| Domicilio | Av. Siempre Viva 123 |
| Teléfono | 11-4000-0000 |
| Email | ventas@distrimed.com |
| Fecha Inicio | 2020-01-01 |
| **Límite de Deuda** | **100000** |

➡️ Agregar.
➡️ Con la fila seleccionada, clic en **Asignar Rubro** → elegí "Insumos descartables".

*Restricción que se ve:* el límite no puede ser negativo; el CUIT es único; y el
proveedor **necesita un rubro** para poder recibir documentos (lo asignás acá).

---

## Paso 3 — Ítems

Abrí **Ítems** (ahora el combo de Rubros ya muestra "Insumos descartables"). Cargá un producto:

| Campo | Valor |
|-------|-------|
| Código | P-001 |
| Nombre | Guantes de látex (caja x100) |
| Descripción | Caja x100 unidades |
| Unidad Medida | Caja |
| Precio Base | 3000 |
| Alícuota IVA | 0.21 |
| Rubro | Insumos descartables |
| Tipo | Producto |
| Stock | 500 |
| Vencimiento | 2027-12-31 |

➡️ Agregar.

---

## Paso 4 — Órdenes de Compra (regla del límite de crédito)

Abrí **Órdenes de Compra**. Vamos a crear **dos** para mostrar los dos resultados posibles.

### OC-1 (entra dentro del límite → "Emitida")
- Nº OC: `1`, Proveedor: DistriMed
- Detalle: Ítem `P-001`, Cantidad `10`, Precio Acordado `3000` → **Agregar Detalle** (subtotal 30000)
- **Crear Orden** → nace en "Borrador"
- Seleccionala en la tabla → **Confirmar Orden**

➡️ **Resultado: "Emitida"** ✅

### OC-2 (supera el límite → "Pendiente Aprobacion")
- Nº OC: `2`, Proveedor: DistriMed
- Detalle: Ítem `P-001`, Cantidad `10`, Precio Acordado `4000` → **Agregar Detalle** (subtotal 40000)
- **Crear Orden**
- Seleccionala → **Confirmar Orden**

➡️ **Resultado: "Pendiente Aprobacion"** ⚠️ (la deuda acumulada supera el límite autorizado)

*Esta es la regla de negocio crítica del TP:* el sistema valida automáticamente el
monto contra el Límite de Deuda del proveedor.

---

## Paso 5 — Documentos Comerciales

Abrí **Documentos Comerciales**.

### Factura (incrementa la deuda)
- Nº Documento: `F-0001-00000001`, Proveedor: DistriMed, Tipo: **Factura**
- Detalle: Ítem `P-001`, Cantidad `10`, Precio `3000`, Alícuota `0.21` → **Agregar Detalle**
- **Crear Documento**

➡️ Neto 30000 · IVA 6300 · **Total 36300** · Estado "Pendiente".

### Nota de Crédito (disminuye la deuda)
- Nº Documento: `NC-0001-00000001`, Proveedor: DistriMed, Tipo: **NotaCredito**
- Doc. Afectado: elegí `F-0001-00000001` (se habilita el combo al elegir NotaCredito)
- Detalle: Ítem `P-001`, Cantidad `2`, Precio `3000`, Alícuota `0.21` → **Agregar Detalle**
- **Crear Documento**

➡️ **Total 7260** con impacto **negativo** en cuenta corriente.

*Restricción que se ve:* las NC/ND exigen un documento afectado; el proveedor debe
tener rubro (coherencia de conceptos); la Factura suma deuda y la NC la resta.

---

## Paso 6 — Órdenes de Pago (retenciones y cancelación)

Abrí **Órdenes de Pago**.
- Nº OP: `1`, Proveedor: DistriMed

**Pestaña Documentos:** elegí `F-0001-00000001` → **Agregar Documento** (bruto 36300).

**Pestaña Retenciones:** cargá dos:
| Tipo | Porcentaje | Base | → Retenido |
|------|-----------|------|-----------|
| Ganancias | 2 | 30000 | 600 |
| IIBB | 3 | 30000 | 900 |

**Pestaña Medios de Pago:** Tipo `Transferencia`, Monto `34800`, Nº/Ref `TRF-001`, Banco/Cuenta `Banco Nación` → **Agregar Medio**.

➡️ **Crear OP** → muestra: Bruto $36300 · Retenciones $1500 · **Neto a pagar $34800**.
➡️ Seleccionala en la tabla → **Emitir OP**.

➡️ La Factura `F-0001-00000001` pasa a estado **"Cancelado"**.

---

## Paso 7 — Consultas y Reportes

Abrí **Consultas y Reportes** y mostrá cada uno:

1. **Cuenta Corriente de un Proveedor** → DistriMed → saldo **$29040** (36300 − 7260).
2. **Documentos Pendientes de Pago** → ya **no** aparece la Factura (fue cancelada); sí la NC.
3. **Órdenes de Compra por Estado** → "Emitida" muestra OC-1; "Pendiente Aprobacion" muestra OC-2.
4. **Pagos por Proveedor** → DistriMed → la OP-1 con su neto.
5. **Libro IVA Compras** → período `2026-01-01` a `2026-12-31` → Factura + NC, **Total IVA $7560**.
6. **Total Retenido por Impuesto** → "Ganancias" → $600 · "IIBB" → $900.

---

## Resumen de lo que demuestra esta corrida

| Concepto del TP | Dónde se ve |
|---|---|
| Herencia Producto/Servicio | Paso 3 |
| Relación Proveedor–Rubro (N:M) | Paso 2 |
| Regla de límite de crédito | Paso 4 (OC-1 vs OC-2) |
| Herencia y polimorfismo en Documentos | Paso 5 (Factura/NC e impacto en cuenta corriente) |
| Cálculo de retenciones | Paso 6 |
| Polimorfismo de Medios de Pago | Paso 6 |
| Cancelación de documentos al emitir OP | Paso 6 |
| Consultas obligatorias (cuenta corriente, Libro IVA, etc.) | Paso 7 |
