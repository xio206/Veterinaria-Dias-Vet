package controladores;

import basedatos.ConexionSQLite;
import modelos.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utilidades.Validador;

/**
 * Controlador para gestionar las operaciones CRUD de los Clientes.
 * Se encarga de la lógica de negocio y la interacción con la base de datos
 * para la entidad Cliente.
 */
public class ClienteController {

    /**
     * Registra un nuevo cliente en la base de datos.
     * @param cliente El objeto Cliente con los datos a guardar.
     * @return true si el registro fue exitoso, false en caso contrario.
     */
    public boolean registrarCliente(Cliente cliente) {
        
        // Validar DNI antes de registrar
        if (!Validador.validarDNI(cliente.getDni())) {
            System.err.println("Error al registrar cliente: " + Validador.getMensajeErrorDNI());
            return false;
        }

        // Validar teléfono antes de registrar
        if (!Validador.validarTelefono(cliente.getTelefono())) {
            System.err.println("Error al registrar cliente: " + Validador.getMensajeErrorTelefono());
            return false;
        }
        
        String sql = "INSERT INTO clientes(dni, nombre, telefono, direccion) VALUES(?,?,?,?)";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cliente.getDni());
            pstmt.setString(2, cliente.getNombre());
            pstmt.setString(3, cliente.getTelefono());
            pstmt.setString(4, cliente.getDireccion());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al registrar cliente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene una lista con todos los clientes registrados.
     * @return Una lista de objetos Cliente.
     */
    public List<Cliente> obtenerTodosLosClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY nombre";
        try (Connection conn = ConexionSQLite.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("telefono"),
                    rs.getString("direccion")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los clientes: " + e.getMessage());
        }
        return clientes;
    }

    /**
     * Actualiza los datos de un cliente existente.
     * @param cliente El cliente con los datos modificados.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizarCliente(Cliente cliente) {
        
        // Validar DNI antes de actualizar
        if (!Validador.validarDNI(cliente.getDni())) {
            System.err.println("Error al actualizar cliente: " + Validador.getMensajeErrorDNI());
            return false;
        }
        // Validar teléfono antes de actualizar
        if (!Validador.validarTelefono(cliente.getTelefono())) {
            System.err.println("Error al actualizar cliente: " + Validador.getMensajeErrorTelefono());
            return false;
        }
        
        String sql = "UPDATE clientes SET nombre = ?, telefono = ?, direccion = ? WHERE dni = ?";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getTelefono());
            pstmt.setString(3, cliente.getDireccion());
            pstmt.setString(4, cliente.getDni());
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    public void eliminarCliente(String dni) {
        String sql = "DELETE FROM clientes WHERE dni = ?";
        // Usar try-with-resources asegura que la conexión y el statement se cierren solos.
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dni);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente durante rollback: " + e.getMessage());
        }
    }
    
    /**
    * Busca un cliente por su DNI.
    * @param dni El DNI del cliente a buscar.
    * @return Un objeto Cliente si se encuentra, de lo contrario devuelve null.
    */
    public Cliente buscarClientePorDni(String dni) {
       String sql = "SELECT * FROM clientes WHERE dni = ?";

       try (Connection conn = ConexionSQLite.conectar();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

           pstmt.setString(1, dni);
           ResultSet rs = pstmt.executeQuery();

           if (rs.next()) {
               // Si se encuentra, creamos y devolvemos el objeto Cliente
               return new Cliente(
                   rs.getString("dni"),
                   rs.getString("nombre"),
                   rs.getString("telefono"),
                   rs.getString("direccion")
               );
           }
       } catch (SQLException e) {
           System.err.println("Error al buscar cliente por DNI: " + e.getMessage());
       }

       // Si no se encuentra o hay un error, devuelve null
       return null;
   }
    
}