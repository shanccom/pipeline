# Procesador Distribuido de Datos Académicos (Pipeline)

Proyecto universitario simple y claro para **Sistemas Distribuidos**. Cada etapa del pipeline corre como proceso independiente y se comunica por **sockets TCP**.

## Objetivo
Procesar registros académicos en etapas independientes:

Gateway → Lector → Validador → Transformador → Reporte

## Estructura del proyecto
```
procesador-distribuido/
│
├── common/
│   ├── AcademicRecord.java
│   ├── JsonUtils.java
│   ├── SocketUtils.java
│   └── Constants.java
│
├── gateway/
│   ├── Gateway.java
│   └── GatewayConfig.java
│
├── lector-service/
│   ├── LectorService.java
│   └── CsvReader.java
│
├── validador-service/
│   ├── ValidadorService.java
│   └── RecordValidator.java
│
├── transformador-service/
│   ├── TransformadorService.java
│   └── RecordTransformer.java
│
├── reporte-service/
│   ├── ReporteService.java
│   └── ReportGenerator.java
│
├── data/
│   └── academic_records.csv
│
├── pom.xml
└── README.md
```

## Cómo ejecutar (paso a paso)

1. **Compilar todo el proyecto** (desde la raíz):
   ```bash
   mvn -q -DskipTests package
   ```

2. **Ejecutar servicios (terminales separadas)**
   ```bash
   java -cp target/classes ReporteService
   java -cp target/classes TransformadorService
   java -cp target/classes ValidadorService
   java -cp target/classes LectorService
   java -cp target/classes Gateway
   ```

## Validaciones
- Promedio entre **0 y 20**
- Campos no vacíos
- Formato correcto (números válidos)
- Errores se registran, pero **no detienen** el pipeline

## Resultados del Reporte
El reporte muestra en consola:
- Total aprobados
- Total desaprobados
- Total errores

## Puertos
| Servicio | Puerto |
|---|---|
| Gateway | 6000 |
| Lector | 6001 |
| Validador | 6002 |
| Transformador | 6003 |
| Reporte | 6004 |

## Logs esperados
```
[Gateway] Pipeline iniciado
[Lector] Registro leído
[Validador] Registro válido
[Transformador] Estado asignado
[Reporte] Registro agregado
```

## Notas educativas
- Cada etapa es independiente y se conecta por TCP.
- El código está comentado y organizado para exposición oral.
