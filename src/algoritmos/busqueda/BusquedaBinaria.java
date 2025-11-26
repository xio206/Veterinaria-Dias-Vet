package algoritmos.busqueda;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import modelos.Producto;

/**
 * Implementación del algoritmo de Búsqueda Binaria.
 * Requiere que la lista esté previamente ordenada. Es mucho más eficiente que la búsqueda secuencial.
 */
public class BusquedaBinaria {

    /**
     * Busca un producto por su código en una lista previamente ordenada.
     * @param listaProductos La lista ordenada de productos.
     * @param codigo El código del producto a buscar.
     * @return El Producto si se encuentra, de lo contrario null.
     */
    public Producto buscar(List<Producto> listaProductos, String codigo) {
        // Asegurarse de que la lista está ordenada por código
        Collections.sort(listaProductos, Comparator.comparing(Producto::getCodigo));

        int bajo = 0;
        int alto = listaProductos.size() - 1;

        while (bajo <= alto) {
            int medio = bajo + (alto - bajo) / 2;
            Producto productoMedio = listaProductos.get(medio);
            int comparacion = productoMedio.getCodigo().compareTo(codigo);

            if (comparacion == 0) {
                return productoMedio; // Elemento encontrado
            }

            if (comparacion < 0) {
                bajo = medio + 1; // Buscar en la mitad derecha
            } else {
                alto = medio - 1; // Buscar en la mitad izquierda
            }
        }
        return null; // Elemento no encontrado
    }
}
