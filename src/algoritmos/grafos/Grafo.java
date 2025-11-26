package algoritmos.grafos;

import algoritmos.lineales.Cola;
import algoritmos.lineales.Pila;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementación de un Grafo usando Lista de Adyacencia.
 * Soporta grafos dirigidos y no dirigidos, con o sin pesos.
 * 
 * Casos de uso en el proyecto Días Vet:
 * - Red de relaciones entre clientes (referidos)
 * - Mapa de ubicaciones para delivery del Pet Shop
 * - Dependencias entre tratamientos veterinarios
 * 
 * @param <T> Tipo de dato para los vértices (debe implementar equals y hashCode)
 * @author XIOMARA
 */
public class Grafo<T> {
    
    /**
     * Clase interna que representa una arista con peso.
     */
    private class Arista {
        T destino;
        double peso;
        
        Arista(T destino, double peso) {
            this.destino = destino;
            this.peso = peso;
        }
        
        Arista(T destino) {
            this(destino, 1.0); // Peso por defecto
        }
    }
    
    // Lista de adyacencia: cada vértice tiene una lista de aristas
    private Map<T, List<Arista>> listaAdyacencia;
    private boolean esDirigido;
    private int numeroAristas;
    
    /**
     * Constructor para crear un grafo.
     * @param esDirigido true para grafo dirigido, false para no dirigido
     */
    public Grafo(boolean esDirigido) {
        this.listaAdyacencia = new HashMap<>();
        this.esDirigido = esDirigido;
        this.numeroAristas = 0;
    }
    
    /**
     * Constructor por defecto: crea un grafo no dirigido.
     */
    public Grafo() {
        this(false);
    }
    
    // =====================================================
    // OPERACIONES BÁSICAS
    // =====================================================
    
    /**
     * Agrega un nuevo vértice al grafo.
     * Complejidad: O(1)
     * @param vertice El vértice a agregar
     * @return true si se agregó (no existía previamente)
     */
    public boolean agregarVertice(T vertice) {
        if (listaAdyacencia.containsKey(vertice)) {
            return false; // Ya existe
        }
        listaAdyacencia.put(vertice, new ArrayList<>());
        return true;
    }
    
    /**
     * Agrega una arista entre dos vértices (sin peso).
     * Complejidad: O(1)
     * @param origen Vértice de origen
     * @param destino Vértice de destino
     * @return true si se agregó exitosamente
     */
    public boolean agregarArista(T origen, T destino) {
        return agregarArista(origen, destino, 1.0);
    }
    
    /**
     * Agrega una arista con peso entre dos vértices.
     * Si los vértices no existen, los crea automáticamente.
     * Complejidad: O(1)
     * @param origen Vértice de origen
     * @param destino Vértice de destino
     * @param peso El peso de la arista
     * @return true si se agregó exitosamente
     */
    public boolean agregarArista(T origen, T destino, double peso) {
        // Asegurar que ambos vértices existen
        agregarVertice(origen);
        agregarVertice(destino);
        
        // Agregar la arista
        listaAdyacencia.get(origen).add(new Arista(destino, peso));
        
        // Si no es dirigido, agregar la arista inversa
        if (!esDirigido) {
            listaAdyacencia.get(destino).add(new Arista(origen, peso));
        }
        
        numeroAristas++;
        return true;
    }
    
    /**
     * Elimina un vértice y todas sus aristas asociadas.
     * Complejidad: O(V + E)
     * @param vertice El vértice a eliminar
     * @return true si se eliminó exitosamente
     */
    public boolean eliminarVertice(T vertice) {
        if (!listaAdyacencia.containsKey(vertice)) {
            return false;
        }
        
        // Contar aristas que se eliminarán
        int aristasEliminadas = listaAdyacencia.get(vertice).size();
        
        // Eliminar el vértice
        listaAdyacencia.remove(vertice);
        
        // Eliminar todas las aristas que apuntan a este vértice
        for (List<Arista> aristas : listaAdyacencia.values()) {
            aristas.removeIf(arista -> arista.destino.equals(vertice));
        }
        
        numeroAristas -= aristasEliminadas;
        return true;
    }
    
    /**
     * Elimina una arista entre dos vértices.
     * @param origen Vértice de origen
     * @param destino Vértice de destino
     * @return true si se eliminó exitosamente
     */
    public boolean eliminarArista(T origen, T destino) {
        if (!listaAdyacencia.containsKey(origen)) {
            return false;
        }
        
        boolean eliminado = listaAdyacencia.get(origen)
            .removeIf(arista -> arista.destino.equals(destino));
        
        if (eliminado && !esDirigido) {
            listaAdyacencia.get(destino)
                .removeIf(arista -> arista.destino.equals(origen));
        }
        
        if (eliminado) {
            numeroAristas--;
        }
        
        return eliminado;
    }
    
    /**
     * Verifica si existe una arista entre dos vértices.
     * Complejidad: O(grado del vértice origen)
     * @param origen Vértice de origen
     * @param destino Vértice de destino
     * @return true si existe la arista
     */
    public boolean existeArista(T origen, T destino) {
        if (!listaAdyacencia.containsKey(origen)) {
            return false;
        }
        
        for (Arista arista : listaAdyacencia.get(origen)) {
            if (arista.destino.equals(destino)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Obtiene el peso de una arista.
     * @param origen Vértice de origen
     * @param destino Vértice de destino
     * @return El peso de la arista, o -1 si no existe
     */
    public double obtenerPeso(T origen, T destino) {
        if (!listaAdyacencia.containsKey(origen)) {
            return -1;
        }
        
        for (Arista arista : listaAdyacencia.get(origen)) {
            if (arista.destino.equals(destino)) {
                return arista.peso;
            }
        }
        return -1;
    }
    
    /**
     * Obtiene los vecinos de un vértice.
     * @param vertice El vértice
     * @return Lista de vértices adyacentes
     */
    public List<T> obtenerVecinos(T vertice) {
        List<T> vecinos = new ArrayList<>();
        if (listaAdyacencia.containsKey(vertice)) {
            for (Arista arista : listaAdyacencia.get(vertice)) {
                vecinos.add(arista.destino);
            }
        }
        return vecinos;
    }
    
    /**
     * Obtiene el grado de un vértice (número de aristas conectadas).
     * @param vertice El vértice
     * @return El grado del vértice
     */
    public int obtenerGrado(T vertice) {
        if (!listaAdyacencia.containsKey(vertice)) {
            return 0;
        }
        return listaAdyacencia.get(vertice).size();
    }
    
    // =====================================================
    // INFORMACIÓN DEL GRAFO
    // =====================================================
    
    /**
     * @return Número de vértices en el grafo
     */
    public int getNumeroVertices() {
        return listaAdyacencia.size();
    }
    
    /**
     * @return Número de aristas en el grafo
     */
    public int getNumeroAristas() {
        return numeroAristas;
    }
    
    /**
     * @return true si el grafo está vacío
     */
    public boolean estaVacio() {
        return listaAdyacencia.isEmpty();
    }
    
    /**
     * @return true si el grafo es dirigido
     */
    public boolean esDirigido() {
        return esDirigido;
    }
    
    /**
     * @return Conjunto de todos los vértices
     */
    public Set<T> obtenerVertices() {
        return new HashSet<>(listaAdyacencia.keySet());
    }
    
    // =====================================================
    // RECORRIDOS
    // =====================================================
    
    /**
     * Recorrido en Anchura (Breadth-First Search - BFS).
     * Explora todos los vecinos de un nivel antes de pasar al siguiente.
     * Usa una Cola para mantener el orden.
     * Complejidad: O(V + E)
     * @param inicio Vértice inicial
     * @return Lista con el orden de visita
     */
    public List<T> recorridoBFS(T inicio) {
        List<T> resultado = new ArrayList<>();
        
        if (!listaAdyacencia.containsKey(inicio)) {
            return resultado;
        }
        
        Set<T> visitados = new HashSet<>();
        Cola<T> cola = new Cola<>();
        
        cola.encolar(inicio);
        visitados.add(inicio);
        
        while (!cola.estaVacia()) {
            T actual = cola.desencolar();
            resultado.add(actual);
            
            for (Arista arista : listaAdyacencia.get(actual)) {
                if (!visitados.contains(arista.destino)) {
                    visitados.add(arista.destino);
                    cola.encolar(arista.destino);
                }
            }
        }
        
        return resultado;
    }
    
    /**
     * Recorrido en Profundidad (Depth-First Search - DFS).
     * Explora lo más profundo posible antes de retroceder.
     * Usa una Pila (o recursión) para mantener el orden.
     * Complejidad: O(V + E)
     * @param inicio Vértice inicial
     * @return Lista con el orden de visita
     */
    public List<T> recorridoDFS(T inicio) {
        List<T> resultado = new ArrayList<>();
        
        if (!listaAdyacencia.containsKey(inicio)) {
            return resultado;
        }
        
        Set<T> visitados = new HashSet<>();
        Pila<T> pila = new Pila<>();
        
        pila.apilar(inicio);
        
        while (!pila.estaVacia()) {
            T actual = pila.desapilar();
            
            if (!visitados.contains(actual)) {
                visitados.add(actual);
                resultado.add(actual);
                
                // Agregar vecinos no visitados a la pila
                for (Arista arista : listaAdyacencia.get(actual)) {
                    if (!visitados.contains(arista.destino)) {
                        pila.apilar(arista.destino);
                    }
                }
            }
        }
        
        return resultado;
    }
    
    /**
     * Versión recursiva del DFS.
     * @param inicio Vértice inicial
     * @return Lista con el orden de visita
     */
    public List<T> recorridoDFSRecursivo(T inicio) {
        List<T> resultado = new ArrayList<>();
        Set<T> visitados = new HashSet<>();
        dfsRecursivoHelper(inicio, visitados, resultado);
        return resultado;
    }
    
    private void dfsRecursivoHelper(T vertice, Set<T> visitados, List<T> resultado) {
        if (vertice == null || visitados.contains(vertice)) {
            return;
        }
        
        visitados.add(vertice);
        resultado.add(vertice);
        
        if (listaAdyacencia.containsKey(vertice)) {
            for (Arista arista : listaAdyacencia.get(vertice)) {
                dfsRecursivoHelper(arista.destino, visitados, resultado);
            }
        }
    }
    
    // =====================================================
    // ALGORITMOS ADICIONALES
    // =====================================================
    
    /**
     * Encuentra el camino más corto entre dos vértices (sin considerar pesos).
     * Usa BFS que garantiza el camino más corto en grafos sin peso.
     * @param origen Vértice de inicio
     * @param destino Vértice de destino
     * @return Lista con el camino, o lista vacía si no existe
     */
    public List<T> encontrarCamino(T origen, T destino) {
        List<T> camino = new ArrayList<>();
        
        if (!listaAdyacencia.containsKey(origen) || !listaAdyacencia.containsKey(destino)) {
            return camino;
        }
        
        if (origen.equals(destino)) {
            camino.add(origen);
            return camino;
        }
        
        // BFS con seguimiento de padres
        Map<T, T> padres = new HashMap<>();
        Set<T> visitados = new HashSet<>();
        Cola<T> cola = new Cola<>();
        
        cola.encolar(origen);
        visitados.add(origen);
        padres.put(origen, null);
        
        boolean encontrado = false;
        
        while (!cola.estaVacia() && !encontrado) {
            T actual = cola.desencolar();
            
            for (Arista arista : listaAdyacencia.get(actual)) {
                if (!visitados.contains(arista.destino)) {
                    visitados.add(arista.destino);
                    padres.put(arista.destino, actual);
                    
                    if (arista.destino.equals(destino)) {
                        encontrado = true;
                        break;
                    }
                    
                    cola.encolar(arista.destino);
                }
            }
        }
        
        // Reconstruir el camino
        if (encontrado) {
            T actual = destino;
            while (actual != null) {
                camino.add(0, actual); // Insertar al inicio
                actual = padres.get(actual);
            }
        }
        
        return camino;
    }
    
    /**
     * Verifica si el grafo es conexo (todos los vértices son alcanzables).
     * @return true si el grafo es conexo
     */
    public boolean esConexo() {
        if (estaVacio()) {
            return true;
        }
        
        // Tomar cualquier vértice como inicio
        T inicio = listaAdyacencia.keySet().iterator().next();
        List<T> alcanzables = recorridoBFS(inicio);
        
        return alcanzables.size() == getNumeroVertices();
    }
    
    /**
     * Detecta si existe un ciclo en el grafo.
     * @return true si existe al menos un ciclo
     */
    public boolean tieneCiclo() {
        Set<T> visitados = new HashSet<>();
        Set<T> enPila = new HashSet<>(); // Para detectar back edges
        
        for (T vertice : listaAdyacencia.keySet()) {
            if (detectarCicloHelper(vertice, visitados, enPila, null)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean detectarCicloHelper(T vertice, Set<T> visitados, Set<T> enPila, T padre) {
        if (enPila.contains(vertice)) {
            return true; // Encontramos un ciclo
        }
        if (visitados.contains(vertice)) {
            return false; // Ya procesado
        }
        
        visitados.add(vertice);
        enPila.add(vertice);
        
        for (Arista arista : listaAdyacencia.get(vertice)) {
            // Para grafos no dirigidos, ignoramos la arista hacia el padre
            if (!esDirigido && arista.destino.equals(padre)) {
                continue;
            }
            
            if (detectarCicloHelper(arista.destino, visitados, enPila, vertice)) {
                return true;
            }
        }
        
        enPila.remove(vertice);
        return false;
    }
    
    // =====================================================
    // VISUALIZACIÓN
    // =====================================================
    
    /**
     * Muestra la representación del grafo en consola.
     */
    public void mostrar() {
        System.out.println("=== Grafo " + (esDirigido ? "Dirigido" : "No Dirigido") + " ===");
        System.out.println("Vértices: " + getNumeroVertices() + ", Aristas: " + getNumeroAristas());
        System.out.println("Lista de Adyacencia:");
        
        for (Map.Entry<T, List<Arista>> entry : listaAdyacencia.entrySet()) {
            System.out.print("  " + entry.getKey() + " -> ");
            List<Arista> aristas = entry.getValue();
            
            if (aristas.isEmpty()) {
                System.out.println("(sin conexiones)");
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < aristas.size(); i++) {
                    Arista a = aristas.get(i);
                    sb.append(a.destino);
                    if (a.peso != 1.0) {
                        sb.append("(").append(a.peso).append(")");
                    }
                    if (i < aristas.size() - 1) {
                        sb.append(", ");
                    }
                }
                System.out.println(sb.toString());
            }
        }
    }
    
    /**
     * Limpia completamente el grafo.
     */
    public void limpiar() {
        listaAdyacencia.clear();
        numeroAristas = 0;
    }
}