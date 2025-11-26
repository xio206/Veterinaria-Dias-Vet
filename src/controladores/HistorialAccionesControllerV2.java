package controladores;

import algoritmos.lineales.Pila;
import controladores.comandos.Command;

/**
 * Versión mejorada del HistorialAccionesController usando Pila.
 * Más eficiente y con mejor separación de responsabilidades.
 */
public class HistorialAccionesControllerV2 {
    
    private Pila<Command> pilaDeshacer;
    private Pila<Command> pilaRehacer;
    private static final int MAX_HISTORIAL = 50;
    
    public HistorialAccionesControllerV2() {
        this.pilaDeshacer = new Pila<>();
        this.pilaRehacer = new Pila<>();
    }
    
    /**
     * Ejecuta un comando y lo agrega al historial.
     * Limpia la pila de rehacer (estándar en editores).
     */
    public void ejecutarComando(Command comando) {
        if (comando.ejecutar()) {
            // Limitar tamaño del historial
            if (pilaDeshacer.getTamano() >= MAX_HISTORIAL) {
                // Implementar lógica para remover el más antiguo
            }
            
            pilaDeshacer.apilar(comando);
            pilaRehacer.limpiar(); // Nueva acción invalida el "Rehacer"
            System.out.println("Comando ejecutado. Historial: " + pilaDeshacer.getTamano());
        } else {
            System.err.println("Fallo al ejecutar comando");
        }
    }
    
    /**
     * Deshace la última acción.
     */
    public void deshacer() {
        if (!puedeDeshacer()) {
            System.out.println("Nada que deshacer");
            return;
        }
        
        Command comando = pilaDeshacer.desapilar();
        if (comando.deshacer()) {
            pilaRehacer.apilar(comando);
            System.out.println("Acción deshecha");
        } else {
            // Si falla, volver a poner en la pila
            pilaDeshacer.apilar(comando);
            System.err.println("No se pudo deshacer la acción");
        }
    }
    
    /**
     * Rehace la última acción deshecha.
     */
    public void rehacer() {
        if (!puedeRehacer()) {
            System.out.println("Nada que rehacer");
            return;
        }
        
        Command comando = pilaRehacer.desapilar();
        if (comando.ejecutar()) {
            pilaDeshacer.apilar(comando);
            System.out.println("Acción rehecha");
        } else {
            pilaRehacer.apilar(comando);
            System.err.println("No se pudo rehacer la acción");
        }
    }
    
    public boolean puedeDeshacer() {
        return !pilaDeshacer.estaVacia();
    }
    
    public boolean puedeRehacer() {
        return !pilaRehacer.estaVacia();
    }
    
    /**
     * Retorna información sobre el estado del historial.
     */
    public String getEstadoHistorial() {
        return String.format("Deshacer: %d acciones | Rehacer: %d acciones",
                           pilaDeshacer.getTamano(),
                           pilaRehacer.getTamano());
    }
}