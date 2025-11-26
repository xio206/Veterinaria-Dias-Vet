package algoritmos.lineales;

/**
 * Implementación de una Lista Enlazada Simple genérica.
 * Una estructura de datos lineal donde los elementos no se almacenan en ubicaciones de memoria contiguas.
 * Cada elemento (Nodo) apunta al siguiente.
 * @param <T> El tipo de dato que almacenará la lista.
 */
public class ListaSimple<T> {

    /**
     * Clase interna que representa un nodo en la lista.
     * Contiene el dato y una referencia al siguiente nodo.
     */
    private class Nodo {
        T dato;
        Nodo siguiente;

        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    private Nodo cabeza; // Referencia al primer nodo de la lista
    private int tamano;

    public ListaSimple() {
        this.cabeza = null;
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
     * Si la lista está vacía, el nuevo elemento se convierte en la cabeza.
     * @param dato El dato a insertar.
     */
    public void insertar(T dato) {
        Nodo nuevoNodo = new Nodo(dato);
        if (estaVacia()) {
            cabeza = nuevoNodo;
        } else {
            Nodo actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevoNodo;
        }
        tamano++;
    }

    /**
     * Busca un dato específico en la lista.
     * @param dato El dato a buscar.
     * @return El nodo que contiene el dato si se encuentra, de lo contrario null.
     */
    public T buscar(T dato) {
        Nodo actual = cabeza;
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
     * @param dato El dato a eliminar.
     * @return true si el dato fue encontrado y eliminado, false en caso contrario.
     */
    public boolean eliminar(T dato) {
        if (estaVacia()) {
            return false;
        }

        // Caso 1: El nodo a eliminar es la cabeza
        if (cabeza.dato.equals(dato)) {
            cabeza = cabeza.siguiente;
            tamano--;
            return true;
        }

        // Caso 2: El nodo a eliminar está en otra posición
        Nodo anterior = cabeza;
        Nodo actual = cabeza.siguiente;
        while (actual != null) {
            if (actual.dato.equals(dato)) {
                anterior.siguiente = actual.siguiente; // Se salta el nodo actual
                tamano--;
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }

        return false; // El dato no se encontró en la lista
    }

    /**
     * Muestra el contenido de la lista en la consola.
     * Útil para fines de depuración y demostración.
     */
    public void mostrarLista() {
        if (estaVacia()) {
            System.out.println("La lista está vacía.");
            return;
        }
        Nodo actual = cabeza;
        System.out.print("Lista Simple: [ ");
        while (actual != null) {
            System.out.print(actual.dato.toString() + " -> ");
            actual = actual.siguiente;
        }
        System.out.println("NULL ]");
    }
}