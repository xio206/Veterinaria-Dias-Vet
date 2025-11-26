package controladores.comandos;

/**
 * Interfaz para el Patrón de Diseño Command.
 * Define las operaciones para ejecutar una acción y para deshacerla.
 */
public interface Command {
    /**
     * Ejecuta la acción.
     * @return true si la acción fue exitosa, false en caso contrario.
     */
    boolean ejecutar();

    /**
     * Deshace la acción previamente ejecutada.
     * @return true si la acción de deshacer fue exitosa, false en caso contrario.
     */
    boolean deshacer();
}