package controladores;

import algoritmos.lineales.ListaDoble; // Usamos nuestra implementación!
import controladores.comandos.Command;
import controladores.comandos.Command;

/**
 * Controlador que gestiona el historial de acciones para las funciones
 * de Deshacer (Undo) y Rehacer (Redo).
 * Utiliza una Lista Doblemente Enlazada para almacenar los comandos ejecutados.
 */
public class HistorialAccionesController {

    // Clase interna NodoDoble para gestionar el puntero actual
    private class Nodo<T> {
        T comando;
        Nodo<T> anterior;
        Nodo<T> siguiente;

        Nodo(T comando) {
            this.comando = comando;
        }
    }
    
    private Nodo<Command> punteroActual;

    public HistorialAccionesController() {
        // El historial comienza con un nodo "nulo" o "base"
        punteroActual = new Nodo<>(null); 
    }

    /**
     * Ejecuta un nuevo comando y lo añade al historial.
     * Cualquier acción en la pila de "rehacer" es eliminada.
     * @param comando El comando a ejecutar.
     */
    public void ejecutarComando(Command comando) {
        if (comando.ejecutar()) {
            Nodo<Command> nuevoNodo = new Nodo<>(comando);
            punteroActual.siguiente = nuevoNodo;
            nuevoNodo.anterior = punteroActual;
            punteroActual = nuevoNodo;
            System.out.println("Comando ejecutado y añadido al historial.");
        } else {
            System.err.println("El comando no se pudo ejecutar.");
        }
    }

    /**
     * Deshace la acción actual.
     * Mueve el puntero hacia atrás en el historial.
     */
    public void deshacer() {
        if (!puedeDeshacer()) {
            System.out.println("Nada que deshacer.");
            return;
        }
        
        if (punteroActual.comando.deshacer()) {
            punteroActual = punteroActual.anterior;
            System.out.println("Acción deshecha.");
        } else {
            System.err.println("No se pudo deshacer la acción.");
        }
    }

    /**
     * Rehace la última acción deshecha.
     * Mueve el puntero hacia adelante en el historial.
     */
    public void rehacer() {
        if (!puedeRehacer()) {
            System.out.println("Nada que rehacer.");
            return;
        }

        // Movemos el puntero primero
        punteroActual = punteroActual.siguiente;
        if (punteroActual.comando.ejecutar()) {
             System.out.println("Acción rehecha.");
        } else {
             System.err.println("No se pudo rehacer la acción.");
             // Si falla, volvemos para mantener la consistencia
             punteroActual = punteroActual.anterior;
        }
    }

    /**
     * Verifica si es posible deshacer una acción.
     * @return true si hay comandos en la pila de deshacer.
     */
    public boolean puedeDeshacer() {
        return punteroActual.comando != null;
    }

    /**
     * Verifica si es posible rehacer una acción.
     * @return true si hay comandos en la pila de rehacer.
     */
    public boolean puedeRehacer() {
        return punteroActual.siguiente != null;
    }
}