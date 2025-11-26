package algoritmos;

import java.util.List;
import modelos.Cita;

/**
 * Implementación del algoritmo de Búsqueda Secuencial (o Lineal).
 * Revisa cada elemento de una lista hasta encontrar el objetivo o llegar al final.
 */
public class BusquedaSecuencial {

    /**
     * Busca una cita por el DNI del cliente en una lista.
     * @param listaCitas La lista donde se buscará.
     * @param dniCliente El DNI del cliente a encontrar.
     * @return La Cita si se encuentra, de lo contrario null.
     */
    public Cita buscar(List<Cita> listaCitas, String dniCliente) {
        for (Cita cita : listaCitas) {
            if (cita.getDniCliente().equals(dniCliente)) {
                return cita; // Elemento encontrado
            }
        }
        return null; // Elemento no encontrado
    }
}