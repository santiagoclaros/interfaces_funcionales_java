# Interfaces Funcionales y Enums en Java

Proyecto práctico que implementa y resuelve las Guías de Laboratorio 1 y 2 de programación funcional y estructuras modernas en Java.

---

## Contenido del Proyecto

### 📌 Guía 1: Interfaces Funcionales
* **Lab 1: Generador de Slugs (`Lab1Slugs`):**
  Pipeline funcional con composición de funciones (`Function.andThen`, `UnaryOperator`) para transformar textos a formato slug URL seguro.
* **Lab 2: Motor de Validación (`Regla<T>`):**
  Interfaz funcional con métodos por defecto y estáticos (`validar`, `y`, `exigir`, `campo`) para componer reglas de validación encadenadas.
* **Lab 3: Calculadora con Estrategias (`Lab3Calculadora`):**
  Patrón estrategia implementado mediante `Map<String, DoubleBinaryOperator>` para evaluar expresiones aritméticas (`+`, `-`, `*`, `/`, `^`, `max`).
* **Lab 4: Motor de Promociones (`Producto`, `Promocion`):**
  Uso de `record`, `Predicate<Producto>` y `DoubleUnaryOperator` para aplicar promociones condicionales a catálogo de productos.
* **Retos Guía 1:**
  * Currificación de funciones con `TriFunction<A, B, C, R>`.
  * Memorización concurrente y segura con `ConcurrentHashMap` (`memorizarSegura`).
  * Patrón de reintentos resiliente (`reintentarDe`) con `Supplier<T>`.

### 📌 Guía 2: Interfaces y Enums
* **Lab 1: Playlist Ordenada (`Cancion`):**
  Implementación de `Comparable<Cancion>` y ordenamiento de colecciones.
* **Lab 2: Pasarela de Pagos (`MetodoPago`, `TarjetaCredito`):**
  Diseño orientado a contratos con métodos `default` para comisiones y totales a cobrar.
* **Lab 3: Semáforo Inteligente (`Semaforo`):**
  Enum con estado interno y máquina de estados mediante `switch expressions`.
* **Lab 4: Cafetería 'El Algoritmo' (`Bebida`, `Tamano`, `PromocionCafeteria`, `Item`):**
  Enums con precios base y recargos, records y composición de promociones.
* **Retos Guía 2:**
  * Interfaces selladas (`sealed interface Figura permits Circulo, Rectangulo, Triangulo, Cuadrado`) con *pattern matching* para cálculo de áreas y perímetros.
  * Baraja Española generada dinámicamente con Enums (`Palo`, `Valor`).
  * Calculadora con lambdas y operadores binarios encapsulados en un Enum (`OperadorEnum`).
  * Control de acceso y roles basado en conjuntos eficientes de permisos (`EnumSet<Permiso>`).

---

## 🚀 Requisitos y Ejecución

* **Java JDK:** Versión 17+ (probado en OpenJDK 27).

### Compilar y Ejecutar

```bash
# Compilar
javac src/Main.java -d out

# Ejecutar demostración completa
java -cp out Main
```
