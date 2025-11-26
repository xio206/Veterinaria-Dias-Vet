package algoritmos.recursividad;

/**
 * Clase que contiene ejemplos de algoritmos recursivos clásicos.
 * Estos métodos demuestran el concepto de recursividad para propósitos académicos.
 */
public class Recursividad {

    /**
     * Calcula el factorial de un número n de forma recursiva.
     * El factorial de n (n!) es el producto de todos los enteros positivos menores o iguales a n.
     * Caso base: factorial(0) = 1.
     * @param n Número entero no negativo.
     * @return El valor del factorial de n.
     */
    public static long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("El número debe ser no negativo.");
        }
        if (n == 0) {
            return 1; // Caso base
        }
        return n * factorial(n - 1); // Llamada recursiva
    }

    /**
     * Calcula el n-ésimo término de la serie de Fibonacci de forma recursiva.
     * La serie comienza con 0 y 1, y cada término subsiguiente es la suma de los dos anteriores.
     * Casos base: fibonacci(0) = 0, fibonacci(1) = 1.
     * @param n Índice del término a calcular (n >= 0).
     * @return El n-ésimo término de la serie de Fibonacci.
     */
    public static long fibonacci(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("El índice debe ser no negativo.");
        }
        if (n <= 1) {
            return n; // Casos base
        }
        return fibonacci(n - 1) + fibonacci(n - 2); // Doble llamada recursiva
    }
}
