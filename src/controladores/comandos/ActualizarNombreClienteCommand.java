package controladores.comandos;

import controladores.ClienteController;
import modelos.Cliente;

/**
 * Comando concreto para actualizar el nombre de un cliente.
 * Encapsula la lógica para ejecutar y deshacer esta acción específica.
 */
public class ActualizarNombreClienteCommand implements Command {

    private ClienteController controller;
    private String dni;
    private String nombreNuevo;
    private String nombreAntiguo;

    public ActualizarNombreClienteCommand(ClienteController controller, String dni, String nombreNuevo) {
        this.controller = controller;
        this.dni = dni;
        this.nombreNuevo = nombreNuevo;
    }

    @Override
    public boolean ejecutar() {
        // Antes de ejecutar, obtenemos el estado anterior para poder deshacerlo.
        Cliente cliente = controller.buscarClientePorDni(dni);
        if (cliente == null) return false;
        
        this.nombreAntiguo = cliente.getNombre();
        cliente.setNombre(nombreNuevo);
        
        return controller.actualizarCliente(cliente);
    }

    @Override
    public boolean deshacer() {
        // Para deshacer, simplemente restauramos el estado antiguo.
        Cliente cliente = controller.buscarClientePorDni(dni);
        if (cliente == null) return false;

        cliente.setNombre(nombreAntiguo);
        return controller.actualizarCliente(cliente);
    }
}