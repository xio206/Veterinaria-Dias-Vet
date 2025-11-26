package modelos;

/**
 * Modelo que representa a un Cliente de la veterinaria.
 */
public class Cliente {
    private String dni;
    private String nombre;
    private String telefono;
    private String direccion;

    // Constructor, Getters y Setters
    public Cliente(String dni, String nombre, String telefono, String direccion) {
        this.dni = dni;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}