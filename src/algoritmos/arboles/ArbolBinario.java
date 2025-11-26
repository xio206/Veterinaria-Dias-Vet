package algoritmos.arboles;

import algoritmos.lineales.Cola;

/**
 * Implementación de un Árbol Binario de Búsqueda (BST) genérico.
 * Cada nodo puede tener máximo 2 hijos (izquierdo y derecho).
 * Los elementos menores van a la izquierda, los mayores a la derecha.
 * 
 * Casos de uso en el proyecto Días Vet:
 * - Organización jerárquica de categorías de productos
 * - Búsqueda eficiente de clientes por DNI
 * - Índice ordenado de mascotas por nombre
 * 
 * @param <T> Tipo de dato que debe ser Comparable
 * @author XIOMARA
 */
public class ArbolBinario<T extends Comparable<T>> {
    
    /**
     * Clase interna que representa un nodo del árbol.
     */
    protected class Nodo {
        T dato;
        Nodo izquierdo;
        Nodo derecho;
        
        Nodo(T dato) {
            this.dato = dato;
            this.izquierdo = null;
            this.derecho = null;
        }
    }
    
    protected Nodo raiz;
    protected int tamano;
    
    /**
     * Constructor que inicializa un árbol vacío.
     */
    public ArbolBinario() {
        this.raiz = null;
        this.tamano = 0;
    }
    
    /**
     * Verifica si el árbol está vacío.
     * Complejidad: O(1)
     * @return true si el árbol no tiene nodos
     */
    public boolean estaVacio() {
        return raiz == null;
    }
    
    /**
     * Retorna el número de elementos en el árbol.
     * Complejidad: O(1)
     * @return cantidad de nodos
     */
    public int getTamano() {
        return tamano;
    }
    
    /**
     * Inserta un nuevo elemento en el árbol manteniendo el orden BST.
     * Complejidad: O(log n) promedio, O(n) peor caso
     * @param dato El elemento a insertar
     */
    public void insertar(T dato) {
        raiz = insertarRecursivo(raiz, dato);
        tamano++;
    }
    
    private Nodo insertarRecursivo(Nodo nodo, T dato) {
        // Caso base: encontramos la posición vacía
        if (nodo == null) {
            return new Nodo(dato);
        }
        
        // Comparamos para decidir hacia dónde ir
        int comparacion = dato.compareTo(nodo.dato);
        
        if (comparacion < 0) {
            // El dato es menor, va a la izquierda
            nodo.izquierdo = insertarRecursivo(nodo.izquierdo, dato);
        } else if (comparacion > 0) {
            // El dato es mayor, va a la derecha
            nodo.derecho = insertarRecursivo(nodo.derecho, dato);
        }
        // Si son iguales, no insertamos duplicados
        
        return nodo;
    }
    
    /**
     * Busca un elemento en el árbol.
     * Complejidad: O(log n) promedio, O(n) peor caso
     * @param dato El elemento a buscar
     * @return El elemento si se encuentra, null si no existe
     */
    public T buscar(T dato) {
        Nodo resultado = buscarRecursivo(raiz, dato);
        return resultado != null ? resultado.dato : null;
    }
    
    private Nodo buscarRecursivo(Nodo nodo, T dato) {
        // Caso base: no encontrado o encontrado
        if (nodo == null || nodo.dato.compareTo(dato) == 0) {
            return nodo;
        }
        
        // Buscar en el subárbol correspondiente
        if (dato.compareTo(nodo.dato) < 0) {
            return buscarRecursivo(nodo.izquierdo, dato);
        } else {
            return buscarRecursivo(nodo.derecho, dato);
        }
    }
    
    /**
     * Verifica si un elemento existe en el árbol.
     * @param dato El elemento a verificar
     * @return true si el elemento existe
     */
    public boolean contiene(T dato) {
        return buscar(dato) != null;
    }
    
    /**
     * Elimina un elemento del árbol.
     * Complejidad: O(log n) promedio, O(n) peor caso
     * @param dato El elemento a eliminar
     * @return true si se eliminó exitosamente
     */
    public boolean eliminar(T dato) {
        if (!contiene(dato)) {
            return false;
        }
        raiz = eliminarRecursivo(raiz, dato);
        tamano--;
        return true;
    }
    
    private Nodo eliminarRecursivo(Nodo nodo, T dato) {
        if (nodo == null) {
            return null;
        }
        
        int comparacion = dato.compareTo(nodo.dato);
        
        if (comparacion < 0) {
            nodo.izquierdo = eliminarRecursivo(nodo.izquierdo, dato);
        } else if (comparacion > 0) {
            nodo.derecho = eliminarRecursivo(nodo.derecho, dato);
        } else {
            // Encontramos el nodo a eliminar
            
            // Caso 1: Nodo sin hijos (hoja)
            if (nodo.izquierdo == null && nodo.derecho == null) {
                return null;
            }
            
            // Caso 2: Nodo con un solo hijo
            if (nodo.izquierdo == null) {
                return nodo.derecho;
            }
            if (nodo.derecho == null) {
                return nodo.izquierdo;
            }
            
            // Caso 3: Nodo con dos hijos
            // Encontramos el sucesor inorden (mínimo del subárbol derecho)
            T sucesor = encontrarMinimo(nodo.derecho);
            nodo.dato = sucesor;
            nodo.derecho = eliminarRecursivo(nodo.derecho, sucesor);
        }
        
        return nodo;
    }
    
    /**
     * Encuentra el valor mínimo en el árbol.
     * @return El elemento mínimo, o null si el árbol está vacío
     */
    public T encontrarMinimo() {
        if (estaVacio()) {
            return null;
        }
        return encontrarMinimo(raiz);
    }
    
    private T encontrarMinimo(Nodo nodo) {
        // El mínimo siempre está en el extremo izquierdo
        while (nodo.izquierdo != null) {
            nodo = nodo.izquierdo;
        }
        return nodo.dato;
    }
    
    /**
     * Encuentra el valor máximo en el árbol.
     * @return El elemento máximo, o null si el árbol está vacío
     */
    public T encontrarMaximo() {
        if (estaVacio()) {
            return null;
        }
        Nodo actual = raiz;
        while (actual.derecho != null) {
            actual = actual.derecho;
        }
        return actual.dato;
    }
    
    /**
     * Calcula la altura del árbol.
     * La altura es la longitud del camino más largo desde la raíz hasta una hoja.
     * Complejidad: O(n)
     * @return La altura del árbol (-1 si está vacío)
     */
    public int altura() {
        return alturaRecursiva(raiz);
    }
    
    private int alturaRecursiva(Nodo nodo) {
        if (nodo == null) {
            return -1; // Árbol vacío tiene altura -1
        }
        int alturaIzq = alturaRecursiva(nodo.izquierdo);
        int alturaDer = alturaRecursiva(nodo.derecho);
        return 1 + Math.max(alturaIzq, alturaDer);
    }
    
    /**
     * Cuenta el número de hojas (nodos sin hijos).
     * @return Número de nodos hoja
     */
    public int contarHojas() {
        return contarHojasRecursivo(raiz);
    }
    
    private int contarHojasRecursivo(Nodo nodo) {
        if (nodo == null) {
            return 0;
        }
        if (nodo.izquierdo == null && nodo.derecho == null) {
            return 1;
        }
        return contarHojasRecursivo(nodo.izquierdo) + contarHojasRecursivo(nodo.derecho);
    }
    
    // =====================================================
    // RECORRIDOS DEL ÁRBOL
    // =====================================================
    
    /**
     * Recorrido In-Orden (Izquierda - Raíz - Derecha).
     * Para un BST, este recorrido muestra los elementos en orden ascendente.
     * Útil para: Obtener elementos ordenados.
     */
    public void recorridoInOrden() {
        System.out.print("In-Orden: ");
        recorridoInOrdenRecursivo(raiz);
        System.out.println();
    }
    
    private void recorridoInOrdenRecursivo(Nodo nodo) {
        if (nodo != null) {
            recorridoInOrdenRecursivo(nodo.izquierdo);
            System.out.print(nodo.dato + " ");
            recorridoInOrdenRecursivo(nodo.derecho);
        }
    }
    
    /**
     * Recorrido Pre-Orden (Raíz - Izquierda - Derecha).
     * Útil para: Copiar el árbol, generar expresiones prefijas.
     */
    public void recorridoPreOrden() {
        System.out.print("Pre-Orden: ");
        recorridoPreOrdenRecursivo(raiz);
        System.out.println();
    }
    
    private void recorridoPreOrdenRecursivo(Nodo nodo) {
        if (nodo != null) {
            System.out.print(nodo.dato + " ");
            recorridoPreOrdenRecursivo(nodo.izquierdo);
            recorridoPreOrdenRecursivo(nodo.derecho);
        }
    }
    
    /**
     * Recorrido Post-Orden (Izquierda - Derecha - Raíz).
     * Útil para: Eliminar el árbol, evaluar expresiones postfijas.
     */
    public void recorridoPostOrden() {
        System.out.print("Post-Orden: ");
        recorridoPostOrdenRecursivo(raiz);
        System.out.println();
    }
    
    private void recorridoPostOrdenRecursivo(Nodo nodo) {
        if (nodo != null) {
            recorridoPostOrdenRecursivo(nodo.izquierdo);
            recorridoPostOrdenRecursivo(nodo.derecho);
            System.out.print(nodo.dato + " ");
        }
    }
    
    /**
     * Recorrido por Niveles (Breadth-First Search - BFS).
     * Usa una Cola para procesar nivel por nivel.
     * Útil para: Imprimir el árbol por niveles, encontrar el camino más corto.
     */
    public void recorridoPorNiveles() {
        if (raiz == null) {
            System.out.println("Árbol vacío");
            return;
        }
        
        // Usamos nuestra propia implementación de Cola
        Cola<Nodo> cola = new Cola<>();
        cola.encolar(raiz);
        
        System.out.print("Por Niveles: ");
        while (!cola.estaVacia()) {
            Nodo actual = cola.desencolar();
            System.out.print(actual.dato + " ");
            
            if (actual.izquierdo != null) {
                cola.encolar(actual.izquierdo);
            }
            if (actual.derecho != null) {
                cola.encolar(actual.derecho);
            }
        }
        System.out.println();
    }
    
    /**
     * Muestra una representación visual del árbol en consola.
     * Útil para depuración y demostración.
     */
    public void mostrarArbol() {
        if (estaVacio()) {
            System.out.println("Árbol vacío");
            return;
        }
        System.out.println("=== Árbol Binario de Búsqueda ===");
        mostrarArbolRecursivo(raiz, "", true);
    }
    
    private void mostrarArbolRecursivo(Nodo nodo, String prefijo, boolean esUltimo) {
        if (nodo != null) {
            System.out.println(prefijo + (esUltimo ? "└── " : "├── ") + nodo.dato);
            
            // Primero mostramos el hijo derecho (arriba visualmente)
            if (nodo.derecho != null || nodo.izquierdo != null) {
                mostrarArbolRecursivo(nodo.derecho, prefijo + (esUltimo ? "    " : "│   "), false);
                mostrarArbolRecursivo(nodo.izquierdo, prefijo + (esUltimo ? "    " : "│   "), true);
            }
        }
    }
    
    /**
     * Verifica si el árbol es un BST válido.
     * @return true si cumple la propiedad BST
     */
    public boolean esBSTValido() {
        return verificarBST(raiz, null, null);
    }
    
    private boolean verificarBST(Nodo nodo, T minimo, T maximo) {
        if (nodo == null) {
            return true;
        }
        
        // Verificar límites
        if (minimo != null && nodo.dato.compareTo(minimo) <= 0) {
            return false;
        }
        if (maximo != null && nodo.dato.compareTo(maximo) >= 0) {
            return false;
        }
        
        // Verificar recursivamente los subárboles
        return verificarBST(nodo.izquierdo, minimo, nodo.dato) &&
               verificarBST(nodo.derecho, nodo.dato, maximo);
    }
    
    /**
     * Limpia completamente el árbol.
     */
    public void limpiar() {
        raiz = null;
        tamano = 0;
    }
}