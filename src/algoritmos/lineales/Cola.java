package algoritmos.lineales;

/**
 * Implementación de una Cola (Queue) genérica usando una Lista Doblemente Enlazada.
 * FIFO: First In, First Out (Primero en entrar, primero en salir)
 * 
 * Casos de uso en el proyecto:
 * - Cola de citas del día (agenda de atención)
 * - Cola de impresión de reportes
 * - Sistema de notificaciones
 * 
 * @param <T> Tipo de dato a almacenar
 */
public class Cola<T> {
    
    private class Nodo {
        T dato;
        Nodo siguiente;
        
        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }
    
    private Nodo frente;  // Donde se atiende (dequeue)
    private Nodo fin;     // Donde se agrega (enqueue)
    private int tamano;
    
    public Cola() {
        this.frente = null;
        this.fin = null;
        this.tamano = 0;
    }
    
    /**
     * Verifica si la cola está vacía.
     * Complejidad: O(1)
     */
    public boolean estaVacia() {
        return frente == null;
    }
    
    /**
     * Agrega un elemento al final de la cola.
     * Complejidad: O(1)
     * @param dato Elemento a agregar
     */
    public void encolar(T dato) {
        Nodo nuevoNodo = new Nodo(dato);
        if (estaVacia()) {
            frente = nuevoNodo;
            fin = nuevoNodo;
        } else {
            fin.siguiente = nuevoNodo;
            fin = nuevoNodo;
        }
        tamano++;
    }
    
    /**
     * Elimina y retorna el elemento al frente de la cola.
     * Complejidad: O(1)
     * @return El elemento al frente, o null si está vacía
     */
    public T desencolar() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola está vacía");
        }
        T dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) {
            fin = null; // La cola quedó vacía
        }
        tamano--;
        return dato;
    }
    
    /**
     * Retorna el elemento al frente sin eliminarlo.
     * Complejidad: O(1)
     */
    public T verFrente() {
        if (estaVacia()) {
            return null;
        }
        return frente.dato;
    }
    
    public int getTamano() {
        return tamano;
    }
    
    /**
     * Muestra el contenido de la cola de frente a fin.
     */
    public void mostrar() {
        if (estaVacia()) {
            System.out.println("Cola vacía");
            return;
        }
        Nodo actual = frente;
        System.out.print("Cola [FRENTE -> ");
        while (actual != null) {
            System.out.print(actual.dato + " -> ");
            actual = actual.siguiente;
        }
        System.out.println("FIN]");
    }
    
    public void limpiar() {
        frente = null;
        fin = null;
        tamano = 0;
    }
}