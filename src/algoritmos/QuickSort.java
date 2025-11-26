package algoritmos;

import java.util.List;
import modelos.Cliente; // Ejemplo de uso con un modelo

/**
 * Implementación del algoritmo de ordenamiento QuickSort.
 * Es un algoritmo eficiente de "divide y vencerás".
 */
public class QuickSort {

    /**
     * Ordena una lista de clientes por su DNI utilizando QuickSort.
     * @param lista La lista de clientes a ordenar.
     * @param bajo El índice inicial de la sublista.
     * @param alto El índice final de la sublista.
     */
    public void ordenar(List<Cliente> lista, int bajo, int alto) {
        if (bajo < alto) {
            int pi = particion(lista, bajo, alto);
            ordenar(lista, bajo, pi - 1);  // Ordena recursivamente la sublista izquierda
            ordenar(lista, pi + 1, alto);   // Ordena recursivamente la sublista derecha
        }
    }

    private int particion(List<Cliente> lista, int bajo, int alto) {
        Cliente pivote = lista.get(alto);
        int i = (bajo - 1); // Índice del elemento más pequeño

        for (int j = bajo; j < alto; j++) {
            // Si el elemento actual es menor o igual al pivote
            if (lista.get(j).getDni().compareTo(pivote.getDni()) <= 0) {
                i++;
                // Intercambiar arr[i] y arr[j]
                Cliente temp = lista.get(i);
                lista.set(i, lista.get(j));
                lista.set(j, temp);
            }
        }

        // Intercambiar arr[i+1] y el pivote (arr[alto])
        Cliente temp = lista.get(i + 1);
        lista.set(i + 1, lista.get(alto));
        lista.set(alto, temp);

        return i + 1;
    }
}
