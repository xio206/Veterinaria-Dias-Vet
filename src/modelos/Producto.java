package modelos;

/**
 * Modelo que representa un Producto del inventario del Pet Shop.
 */
public class Producto {
    
    private String codigo; // Código de barras o SKU del producto
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;

    /**
     * Constructor para crear una nueva instancia de Producto.
     * @param codigo El código único del producto.
     * @param nombre El nombre comercial del producto.
     * @param descripcion Breve descripción de sus características.
     * @param precio El precio de venta al público.
     * @param stock La cantidad de unidades disponibles en inventario.
     */
    public Producto(String codigo, String nombre, String descripcion, double precio, int stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }

    // --- Getters y Setters ---

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}