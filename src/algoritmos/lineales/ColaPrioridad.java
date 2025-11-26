package algoritmos.lineales;

import java.util.Comparator;
// Incluir el diagrama de una Cola
/**
 * Cola con Prioridad (Priority Queue) usando un Heap Mínimo (Min Heap).
 * Los elementos con menor valor de prioridad se atienden primero.  */
public class ColaPrioridad<T> {
    
    private class ElementoPrioridad {
        T dato;
        int prioridad; // Menor valor = mayor prioridad
        
        ElementoPrioridad(T dato, int prioridad) {
            this.dato = dato;
            this.prioridad = prioridad;
        }
    }
    
    // Usamos la implementación de PriorityQueue de Java que se basa en un Heap
    private java.util.PriorityQueue<ElementoPrioridad> heap;
    
    public ColaPrioridad() {
        // Configuramos el comparador para ordenar por el valor entero de la prioridad
        this.heap = new java.util.PriorityQueue<>(
            Comparator.comparingInt(e -> e.prioridad)
        );
    }
    
    /**
     * Agrega un elemento con su prioridad. Complejidad: O(log n)
     */
    public void encolar(T dato, int prioridad) {
        heap.offer(new ElementoPrioridad(dato, prioridad));
    }
    
    /**
     * Remueve y retorna el elemento con mayor prioridad. Complejidad: O(log n)
     */
    public T desencolar() {
        if (heap.isEmpty()) {
            throw new IllegalStateException("Cola de prioridad vacía");
        }
        return heap.poll().dato;
    }
    
    public T verFrente() {
        if (heap.isEmpty()) return null;
        return heap.peek().dato;
    }
    
    public boolean estaVacia() {
        return heap.isEmpty();
    }
    
    public int getTamano() {
        return heap.size();
    }
}