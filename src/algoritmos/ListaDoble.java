package algoritmos;

/**
 * Implementación de una Lista Doblemente Enlazada genérica.
 * Cada nodo tiene una referencia tanto al nodo siguiente como al anterior,
 * lo que permite el recorrido bidireccional.
 * @param <T> El tipo de dato que almacenará la lista.
 */
public class ListaDoble<T> {

    /**
     * Clase interna que representa un nodo en la lista doble.
     * Contiene el dato y referencias al nodo anterior y siguiente.
     */
    private class NodoDoble {
        T dato;
        NodoDoble siguiente;
        NodoDoble anterior;

        NodoDoble(T dato) {
            this.dato = dato;
            this.siguiente = null;
            this.anterior = null;
        }
    }

    private NodoDoble cabeza; // Referencia al primer nodo
    private NodoDoble cola;   // Referencia al último nodo
    private int tamano;

    public ListaDoble() {
        this.cabeza = null;
        this.cola = null;
        this.tamano = 0;
    }
    
    /**
     * Verifica si la lista está vacía.
     * @return true si la lista no tiene nodos, false en caso contrario.
     */
    public boolean estaVacia() {
        return cabeza == null;
    }

    /**
     * Devuelve el número de elementos en la lista.
     * @return El tamaño de la lista.
     */
    public int getTamano() {
        return tamano;
    }

    /**
     * Inserta un nuevo elemento al final de la lista.
     * @param dato El dato a insertar.
     */
    public void insertar(T dato) {
        NodoDoble nuevoNodo = new NodoDoble(dato);
        if (estaVacia()) {
            cabeza = nuevoNodo;
            cola = nuevoNodo;
        } else {
            cola.siguiente = nuevoNodo;
            nuevoNodo.anterior = cola;
            cola = nuevoNodo; // El nuevo nodo es ahora la cola
        }
        tamano++;
    }

    /**
     * Busca un dato específico en la lista.
     * @param dato El dato a buscar.
     * @return El dato si se encuentra, de lo contrario null.
     */
    public T buscar(T dato) {
        NodoDoble actual = cabeza;
        while (actual != null) {
            if (actual.dato.equals(dato)) {
                return actual.dato; // Dato encontrado
            }
            actual = actual.siguiente;
        }
        return null; // Dato no encontrado
    }

    /**
     * Elimina la primera ocurrencia de un dato específico en la lista.
     * La principal ventaja de la lista doble se ve aquí, al no necesitar un puntero "anterior".
     * @param dato El dato a eliminar.
     * @return true si el dato fue encontrado y eliminado, false en caso contrario.
     */
    public boolean eliminar(T dato) {
        NodoDoble actual = cabeza;

        // Bucle para encontrar el nodo a eliminar
        while (actual != null && !actual.dato.equals(dato)) {
            actual = actual.siguiente;
        }

        if (actual == null) {
            return false; // No se encontró el dato
        }

        // Caso 1: El nodo a eliminar es la cabeza
        if (actual == cabeza) {
            cabeza = actual.siguiente;
        } else {
            actual.anterior.siguiente = actual.siguiente;
        }

        // Caso 2: El nodo a eliminar es la cola
        if (actual == cola) {
            cola = actual.anterior;
        } else {
            actual.siguiente.anterior = actual.anterior;
        }
        
        // Si la lista queda vacía
        if (cabeza != null) {
            cabeza.anterior = null;
        }
        if (cola != null) {
            cola.siguiente = null;
        }

        tamano--;
        return true;
    }
    
    /**
     * Muestra el contenido de la lista en la consola desde la cabeza hasta la cola.
     */
    public void mostrarAdelante() {
        if (estaVacia()) {
            System.out.println("La lista doble está vacía.");
            return;
        }
        NodoDoble actual = cabeza;
        System.out.print("Lista Doble (Adelante): [ NULL <- ");
        while (actual != null) {
            System.out.print(actual.dato.toString() + " <-> ");
            actual = actual.siguiente;
        }
        System.out.println("NULL ]");
    }

    /**
     * Muestra el contenido de la lista en la consola desde la cola hasta la cabeza.
     */
    public void mostrarAtras() {
        if (estaVacia()) {
            System.out.println("La lista doble está vacía.");
            return;
        }
        NodoDoble actual = cola;
        System.out.print("Lista Doble (Atrás): [ NULL <-> ");
        while (actual != null) {
            System.out.print(actual.dato.toString() + " -> ");
            actual = actual.anterior;
        }
        System.out.println("NULL ]");
    }
}