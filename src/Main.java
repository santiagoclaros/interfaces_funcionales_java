import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

// ============================================================================
// CLASE PRINCIPAL DE EJECUCIÓN Y DEMOSTRACIÓN
// ============================================================================
public class Main {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println(" GUÍA 1: INTERFACES FUNCIONALES EN JAVA");
        System.out.println("=================================================");

        // Lab 1: Generador de slugs
        System.out.println("\n--- [Guía 1 - Lab 1] Generador de Slugs ---");
        String tituloArticulo = "  ¡Guía Práctica de Java 2024: Programación Funcional!  ";
        String slug = Lab1Slugs.generarSlug(tituloArticulo);
        System.out.println("Texto original: \"" + tituloArticulo + "\"");
        System.out.println("Slug generado : " + slug);

        // Lab 2: Motor de validación
        System.out.println("\n--- [Guía 1 - Lab 2] Motor de Validación ---");
        Regla<String> noVacio = Regla.exigir(s -> s != null && !s.isBlank(), "El campo no puede estar vacío");
        Regla<String> longitudMinima = Regla.exigir(s -> s != null && s.length() >= 5, "Debe tener al menos 5 caracteres");
        Regla<String> reglaUsuario = noVacio.y(longitudMinima);

        System.out.println("Validando 'Ana': " + reglaUsuario.validar("Ana"));
        System.out.println("Validando 'Santiago': " + reglaUsuario.validar("Santiago"));

        // Lab 3: Calculadora con estrategias
        System.out.println("\n--- [Guía 1 - Lab 3] Calculadora con Estrategias ---");
        String[] expresiones = { "10 + 5", "20 - 7", "4 * 3", "15 / 3", "2 ^ 4", "10 max 25" };
        for (String exp : expresiones) {
            System.out.println(exp + " = " + Lab3Calculadora.evaluar(exp));
        }

        // Lab 4: Motor de promociones
        System.out.println("\n--- [Guía 1 - Lab 4] Motor de Promociones ---");
        Producto laptop = new Producto("Laptop Gamer", "Electrónica", 3500.0, 5);
        Producto libro = new Producto("Java Moderno", "Libros", 120.0, 10);

        Promocion promoElectronica = new Promocion(
                "15% Descuento en Electrónica",
                p -> "Electrónica".equalsIgnoreCase(p.categoria()) && p.disponible(),
                precio -> precio * 0.85
        );

        System.out.println(laptop.nombre() + " ($" + laptop.precio() + ") -> Precio con promo: $" + promoElectronica.precioCon(laptop));
        System.out.println(libro.nombre() + " ($" + libro.precio() + ") -> Precio con promo: $" + promoElectronica.precioCon(libro));

        // Retos Guía 1
        System.out.println("\n--- [Guía 1 - Retos] ---");
        TriFunction<Double, Double, Double, Double> calcularVolumen = (largo, ancho, alto) -> largo * ancho * alto;
        var curryVolumen = calcularVolumen.curry();
        double volumen = curryVolumen.apply(2.0).apply(3.0).apply(4.0);
        System.out.println("Volumen currificado (2 x 3 x 4): " + volumen);

        Function<Integer, Integer> calculoPesado = UtilidadesFuncionales.memorizarSegura(n -> {
            System.out.println(" [Calculando factorial/doble de " + n + "...]");
            return n * 2;
        });
        System.out.println("Primera llamada f(5): " + calculoPesado.apply(5));
        System.out.println("Segunda llamada f(5) (desde caché): " + calculoPesado.apply(5));

        Supplier<String> servicioInestable = new Supplier<>() {
            int intentos = 0;
            @Override
            public String get() {
                intentos++;
                if (intentos < 3) throw new RuntimeException("Fallo temporal");
                return "Conexión exitosa en intento #" + intentos;
            }
        };
        Supplier<String> conReintento = UtilidadesFuncionales.reintentarDe(servicioInestable, 4);
        System.out.println("Resultado reintento: " + conReintento.get());

        System.out.println("\n=================================================");
        System.out.println(" GUÍA 2: INTERFACES Y ENUMS EN JAVA");
        System.out.println("=================================================");

        // Lab 1: Playlist ordenada
        System.out.println("\n--- [Guía 2 - Lab 1] Mi Playlist Ordenada ---");
        List<Cancion> playlist = new ArrayList<>(List.of(
                new Cancion("Bohemian Rhapsody", "Queen", 354),
                new Cancion("Imagine", "John Lennon", 183),
                new Cancion("Billie Jean", "Michael Jackson", 294)
        ));
        Collections.sort(playlist);
        playlist.forEach(System.out::println);

        // Lab 2: Pasarela de pagos
        System.out.println("\n--- [Guía 2 - Lab 2] Pasarela de Pagos ---");
        TarjetaCredito tarjeta = new TarjetaCredito(500.0);
        System.out.println("Cupo inicial: $" + tarjeta.getCupoDisponible());
        System.out.println("Intentando pagar $200 (Comisión 3% = $6.0, Total: $206.0)");
        boolean pago1 = tarjeta.pagar(200);
        System.out.println("¿Pago exitoso?: " + pago1 + " | Cupo restante: $" + tarjeta.getCupoDisponible());

        // Lab 3: Semáforo inteligente
        System.out.println("\n--- [Guía 2 - Lab 3] Semáforo Inteligente ---");
        Semaforo actual = Semaforo.VERDE;
        for (int i = 0; i < 3; i++) {
            System.out.println("Estado: " + actual + " (" + actual.getSegundos() + "s) -> Acción: " + actual.getAccion());
            actual = actual.siguiente();
        }

        // Lab 4: Cafetería El Algoritmo
        System.out.println("\n--- [Guía 2 - Lab 4] Cafetería 'El Algoritmo' ---");
        Item pedido = new Item(Bebida.CAPUCHINO, Tamano.GRANDE, 2);
        double subtotal = pedido.subtotal();
        System.out.println("Pedido: 2 Capuchinos Grandes. Subtotal: $" + subtotal);

        PromocionCafeteria promo10 = PromocionCafeteria.porcentaje(10);
        PromocionCafeteria promo5 = PromocionCafeteria.porcentaje(5);
        PromocionCafeteria combo = promo10.y(promo5);

        double desc = combo.descuento(subtotal);
        System.out.println("Descuento combinado (10% + 5%): $" + desc);
        System.out.println("Total a pagar: $" + (subtotal - desc));

        // Retos Guía 2
        System.out.println("\n--- [Guía 2 - Retos] ---");
        Figura circulo = new Circulo(5.0);
        Figura rectangulo = new Rectangulo(4.0, 6.0);
        Figura triangulo = new Triangulo(3.0, 4.0);
        Figura cuadrado = new Cuadrado(4.0);

        System.out.println("Perímetro Círculo (r=5): " + String.format("%.2f", circulo.perimetro()));
        System.out.println("Perímetro Rectángulo (4x6): " + rectangulo.perimetro());
        System.out.println("Perímetro Triángulo equilátero (base=3): " + triangulo.perimetro());
        System.out.println("Perímetro Cuadrado (lado=4): " + cuadrado.perimetro());

        System.out.println("\nCalculadora con Enum:");
        System.out.println("10 SUMA 5 = " + OperadorEnum.SUMA.aplicar(10, 5));
        System.out.println("2 POTENCIA 8 = " + OperadorEnum.POTENCIA.aplicar(2, 8));

        System.out.println("\nPermisos con EnumSet:");
        System.out.println("Invitado puede leer: " + Rol.INVITADO.puede(Permiso.LEER));
        System.out.println("Invitado puede borrar: " + Rol.INVITADO.puede(Permiso.BORRAR));
        System.out.println("Admin puede borrar: " + Rol.ADMIN.puede(Permiso.BORRAR));

        System.out.println("\nBaraja Española generada (primeras 5 cartas):");
        List<Carta> baraja = CreadorBaraja.generar();
        baraja.stream().limit(5).forEach(c -> System.out.println(" - " + c));
    }
}

// ============================================================================
// GUÍA 1: INTERFACES FUNCIONALES EN JAVA
// ============================================================================

// Laboratorio 1: Generador de slugs
class Lab1Slugs {
    static final UnaryOperator<String> RECORTAR = String::strip;
    static final UnaryOperator<String> MINUSCULAS = String::toLowerCase;
    static final UnaryOperator<String> SIN_TILDES =
            s -> Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    static final UnaryOperator<String> SOLO_VALIDOS = s -> s.replaceAll("[^a-z0-9\\s-]", "");
    static final UnaryOperator<String> GUIONES = s -> s.strip().replaceAll("[\\s-]+", "-");

    static Function<String, String> tuberia(List<? extends Function<String, String>> pasos) {
        Function<String, String> resultado = Function.identity();
        for (Function<String, String> paso : pasos) {
            resultado = resultado.andThen(paso);
        }
        return resultado;
    }

    @SafeVarargs
    static Function<String, String> tuberia(Function<String, String>... pasos) {
        return tuberia(Arrays.asList(pasos));
    }

    static String generarSlug(String texto) {
        return tuberia(RECORTAR, MINUSCULAS, SIN_TILDES, SOLO_VALIDOS, GUIONES).apply(texto);
    }
}

// Laboratorio 2: Motor de validación
@FunctionalInterface
interface Regla<T> {
    List<String> validar(T valor);

    default Regla<T> y(Regla<? super T> otra) {
        return valor -> {
            List<String> errores = new ArrayList<>(validar(valor));
            errores.addAll(otra.validar(valor));
            return errores;
        };
    }

    static <T> Regla<T> exigir(Predicate<? super T> condicion, String mensaje) {
        return valor -> condicion.test(valor) ? List.of() : List.of(mensaje);
    }

    static <T, C> Regla<T> campo(Function<? super T, ? extends C> extractor, Regla<? super C> regla) {
        return valor -> regla.validar(extractor.apply(valor));
    }
}

// Laboratorio 3: Calculadora con estrategias
class Lab3Calculadora {
    static final Map<String, DoubleBinaryOperator> OPERACIONES = new LinkedHashMap<>();
    static {
        OPERACIONES.put("+", Double::sum);
        OPERACIONES.put("-", (a, b) -> a - b);
        OPERACIONES.put("*", (a, b) -> a * b);
        OPERACIONES.put("/", (a, b) -> {
            if (b == 0) throw new ArithmeticException("división por cero");
            return a / b;
        });
        OPERACIONES.put("^", Math::pow);
        OPERACIONES.put("max", Math::max);
    }

    static double evaluar(String expresion) {
        String[] partes = expresion.strip().split("\\s+");
        if (partes.length < 3) {
            throw new IllegalArgumentException("Expresión inválida. Formato esperado: '<número> <operador> <número>'");
        }
        DoubleBinaryOperator operacion = OPERACIONES.get(partes[1]);
        if (operacion == null) throw new IllegalArgumentException("operador desconocido: " + partes[1]);
        return operacion.applyAsDouble(Double.parseDouble(partes[0]), Double.parseDouble(partes[2]));
    }
}

// Laboratorio 4: Motor de promociones
record Producto(String nombre, String categoria, double precio, int stock) {
    public boolean disponible() { return stock > 0; }
}

record Promocion(String nombre, Predicate<Producto> aplicaA, DoubleUnaryOperator ajuste) {
    double precioCon(Producto p) {
        return aplicaA.test(p) ? ajuste.applyAsDouble(p.precio()) : p.precio();
    }
}

// Retos para profundizar (Guía 1)
@FunctionalInterface
interface TriFunction<A, B, C, R> {
    R aplicar(A a, B b, C c);

    // Reto 1: Currificación
    default Function<A, Function<B, Function<C, R>>> curry() {
        return a -> b -> c -> aplicar(a, b, c);
    }
}

class UtilidadesFuncionales {
    // Reto 3: Memorizar segura
    static <T, R> Function<T, R> memorizarSegura(Function<T, R> funcion) {
        Map<T, R> cache = new ConcurrentHashMap<>();
        return entrada -> cache.computeIfAbsent(entrada, funcion);
    }

    // Reto 4: Reintentar
    static <T> Supplier<T> reintentarDe(Supplier<T> sup, int intentos) {
        if (intentos < 1) {
            throw new IllegalArgumentException("El número de intentos debe ser al menos 1");
        }
        return () -> {
            for (int i = 0; i < intentos - 1; i++) {
                try {
                    return sup.get();
                } catch (Exception ignored) {
                }
            }
            return sup.get();
        };
    }
}

// ============================================================================
// GUÍA 2: INTERFACES Y ENUMS EN JAVA
// ============================================================================

// Laboratorio 1: Mi playlist ordenada
class Cancion implements Comparable<Cancion> {
    private final String titulo;
    private final String artista;
    private final int duracionSeg;

    Cancion(String titulo, String artista, int duracionSeg) {
        this.titulo = titulo;
        this.artista = artista;
        this.duracionSeg = duracionSeg;
    }

    public String getTitulo() { return titulo; }
    public String getArtista() { return artista; }
    public int getDuracionSeg() { return duracionSeg; }
    public String duracion() { return String.format("%d:%02d", duracionSeg / 60, duracionSeg % 60); }

    @Override
    public int compareTo(Cancion otra) { return titulo.compareTo(otra.titulo); }

    @Override
    public String toString() {
        return String.format("'%s' de %s (%s)", titulo, artista, duracion());
    }
}

// Laboratorio 2: Pasarela de pagos
interface MetodoPago {
    String nombre();
    boolean pagar(double monto);
    default double comision(double monto) { return 0; }
    default double totalACobrar(double monto) { return monto + comision(monto); }
}

class TarjetaCredito implements MetodoPago {
    private double cupoDisponible;

    TarjetaCredito(double cupo) { this.cupoDisponible = cupo; }

    @Override public String nombre() { return "Tarjeta de crédito"; }
    @Override public double comision(double monto) { return monto * 0.03; }

    @Override
    public boolean pagar(double monto) {
        double total = totalACobrar(monto);
        if (total > cupoDisponible) return false;
        cupoDisponible -= total;
        return true;
    }

    public double getCupoDisponible() { return cupoDisponible; }
}

// Laboratorio 3: Semáforo inteligente
enum Semaforo {
    VERDE(30, "Avance"), AMARILLO(5, "Precaución"), ROJO(35, "Pare");

    private final int segundos;
    private final String accion;

    Semaforo(int segundos, String accion) { this.segundos = segundos; this.accion = accion; }
    public int getSegundos() { return segundos; }
    public String getAccion() { return accion; }

    public Semaforo siguiente() {
        return switch (this) {
            case VERDE -> AMARILLO;
            case AMARILLO -> ROJO;
            case ROJO -> VERDE;
        };
    }
}

// Laboratorio 4: Cafetería "El Algoritmo"
enum Bebida {
    TINTO(2_500), CAPUCHINO(6_000), CHOCOLATE(5_000), AROMATICA(3_000);
    private final double precioBase;
    Bebida(double precioBase) { this.precioBase = precioBase; }
    public double getPrecioBase() { return precioBase; }
}

enum Tamano {
    PEQUENO(0), MEDIANO(1_000), GRANDE(2_000);
    private final double recargo;
    Tamano(double recargo) { this.recargo = recargo; }
    public double getRecargo() { return recargo; }
}

@FunctionalInterface
interface PromocionCafeteria {
    double descuento(double subtotal);
    static PromocionCafeteria ninguna() { return subtotal -> 0; }
    static PromocionCafeteria porcentaje(double pct) { return subtotal -> subtotal * (pct / 100); }

    // Reto 4 (Guía 2): Promociones combinables
    default PromocionCafeteria y(PromocionCafeteria otra) {
        return subtotal -> Math.max(0, this.descuento(subtotal) + otra.descuento(subtotal));
    }
}

record Item(Bebida bebida, Tamano tamano, int cantidad) {
    public double subtotal() {
        return (bebida.getPrecioBase() + tamano.getRecargo()) * cantidad;
    }
}

// Retos para ir más allá (Guía 2)
// Reto 1: Figuras con perímetro y área
sealed interface Figura permits Circulo, Rectangulo, Triangulo, Cuadrado {
    default double perimetro() {
        return switch (this) {
            case Circulo c -> 2 * Math.PI * c.radio();
            case Rectangulo r -> 2 * (r.base() + r.altura());
            case Triangulo t -> t.base() * 3;
            case Cuadrado c -> c.lado() * 4;
        };
    }

    default double area() {
        return switch (this) {
            case Circulo c -> Math.PI * c.radio() * c.radio();
            case Rectangulo r -> r.base() * r.altura();
            case Triangulo t -> (t.base() * t.altura()) / 2.0;
            case Cuadrado c -> c.lado() * c.lado();
        };
    }
}

record Circulo(double radio) implements Figura {}
record Rectangulo(double base, double altura) implements Figura {}
record Triangulo(double base, double altura) implements Figura {}
record Cuadrado(double lado) implements Figura {}

// Reto 2: Baraja Española
enum Palo { OROS, COPAS, ESPADAS, BASTOS }
enum Valor { UNO, DOS, TRES, CUATRO, CINCO, SEIS, SIETE, OCHO, NUEVE, DIEZ, ONCE, DOCE }
record Carta(Palo p, Valor v) {
    @Override
    public String toString() {
        return v + " de " + p;
    }
}

class CreadorBaraja {
    static List<Carta> generar() {
        List<Carta> baraja = new ArrayList<>();
        for (Palo p : Palo.values()) {
            for (Valor v : Valor.values()) baraja.add(new Carta(p, v));
        }
        Collections.shuffle(baraja);
        return baraja;
    }
}

// Reto 3: Calculadora con lambdas en el Enum
enum OperadorEnum {
    SUMA((a, b) -> a + b),
    RESTA((a, b) -> a - b),
    MULTIPLICACION((a, b) -> a * b),
    DIVISION((a, b) -> {
        if (b == 0) throw new ArithmeticException("división por cero");
        return a / b;
    }),
    POTENCIA(Math::pow),
    MODULO((a, b) -> a % b);

    private final DoubleBinaryOperator op;
    OperadorEnum(DoubleBinaryOperator op) { this.op = op; }
    public double aplicar(double a, double b) { return op.applyAsDouble(a, b); }
}

// Reto 5: Permisos con EnumSet
enum Permiso { LEER, ESCRIBIR, BORRAR, ADMINISTRAR }
enum Rol {
    INVITADO(EnumSet.of(Permiso.LEER)),
    ADMIN(EnumSet.allOf(Permiso.class));

    private final EnumSet<Permiso> permisos;
    Rol(EnumSet<Permiso> permisos) { this.permisos = permisos; }
    public boolean puede(Permiso p) { return permisos.contains(p); }
}