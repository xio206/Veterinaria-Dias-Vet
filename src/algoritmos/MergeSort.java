package algoritmos;

import java.util.ArrayList;
import java.util.List;
import modelos.Producto; // Ejemplo de uso

/**
 * Implementación del algoritmo de ordenamiento MergeSort.
 * También basado en la técnica "divide y vencerás", garantiza un rendimiento O(n log n).
 */
public class MergeSort {

    /**
     * Ordena una lista de productos por nombre.
     * @param lista La lista de productos a ordenar.
     */
    public void ordenar(List<Producto> lista) {
        if (lista.size() <= 1) {
            return; // Ya está ordenada
        }

        int medio = lista.size() / 2;
        List<Producto> izquierda = new ArrayList<>(lista.subList(0, medio));
        List<Producto> derecha = new ArrayList<>(lista.subList(medio, lista.size()));

        ordenar(izquierda);
        ordenar(derecha);

        fusionar(lista, izquierda, derecha);
    }

    private void fusionar(List<Producto> original, List<Producto> izquierda, List<Producto> derecha) {
        int i = 0, j = 0, k = 0;
        while (i < izquierda.size() && j < derecha.size()) {
            if (izquierda.get(i).getNombre().compareTo(derecha.get(j).getNombre()) <= 0) {
                original.set(k++, izquierda.get(i++));
            } else {
                original.set(k++, derecha.get(j++));
            }
        }

        while (i < izquierda.size()) {
            original.set(k++, izquierda.get(i++));
        }

        while (j < derecha.size()) {
            original.set(k++, derecha.get(j++));
        }
    }
}