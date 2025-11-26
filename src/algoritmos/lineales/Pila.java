package algoritmos.lineales;

/**
 * Implementación de una Pila (Stack) genérica usando una Lista Enlazada.
 * LIFO: Last In, First Out (Último en entrar, primero en salir)
 * 
 * Casos de uso en el proyecto:
 * - Historial de navegación (botón "Atrás" en ventanas)
 * - Sistema Undo/Redo (mejora del actual HistorialAccionesController)
 * - Validación de paréntesis en fórmulas de precios
 * 
 * @param <T> Tipo de dato a almacenar
 */
public class Pila<T> {
    
    private class Nodo {
        T dato;
        Nodo siguiente;
        
        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }
    
    private Nodo tope;
    private int tamano;
    
    public Pila() {
        this.tope = null;
        this.tamano = 0;
    }
    
    /**
     * Verifica si la pila está vacía.
     * Complejidad: O(1)
     */
    public boolean estaVacia() {
        return tope == null;
    }
    
    /**
     * Inserta un elemento en el tope de la pila.
     * Complejidad: O(1)
     * @param dato Elemento a insertar
     */
    public void apilar(T dato) {
        Nodo nuevoNodo = new Nodo(dato);
        nuevoNodo.siguiente = tope;
        tope = nuevoNodo;
        tamano++;
    }
    
    /**
     * Elimina y retorna el elemento en el tope.
     * Complejidad: O(1)
     * @return El elemento en el tope, o null si está vacía
     */
    public T desapilar() {
        if (estaVacia()) {
            throw new IllegalStateException("La pila está vacía");
        }
        T dato = tope.dato;
        tope = tope.siguiente;
        tamano--;
        return dato;
    }
    
    /**
     * Retorna el elemento en el tope sin eliminarlo.
     * Complejidad: O(1)
     * @return El elemento en el tope, o null si está vacía
     */
    public T verTope() {
        if (estaVacia()) {
            return null;
        }
        return tope.dato;
    }
    
    /**
     * Retorna el número de elementos en la pila.
     * Complejidad: O(1)
     */
    public int getTamano() {
        return tamano;
    }
    
    /**
     * Muestra el contenido de la pila desde el tope hacia abajo.
     * Útil para depuración.
     */
    public void mostrar() {
        if (estaVacia()) {
            System.out.println("Pila vacía");
            return;
        }
        Nodo actual = tope;
        System.out.print("Pila [TOPE -> ");
        while (actual != null) {
            System.out.print(actual.dato + " ");
            actual = actual.siguiente;
        }
        System.out.println("]");
    }
    
    /**
     * Vacía completamente la pila.
     * Complejidad: O(1)
     */
    public void limpiar() {
        tope = null;
        tamano = 0;
    }
}
