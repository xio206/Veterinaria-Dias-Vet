package controladores;

import basedatos.ConexionSQLite;
import modelos.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para gestionar las operaciones CRUD de los Productos del Pet Shop.
 */
public class ProductoController {

    /**
     * Registra un nuevo producto en el inventario.
     * @param producto El objeto Producto a registrar.
     * @return true si el registro fue exitoso.
     */
    public boolean registrarProducto(Producto producto) {
        String sql = "INSERT INTO productos(codigo, nombre, descripcion, precio, stock) VALUES(?,?,?,?,?)";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, producto.getCodigo());
            pstmt.setString(2, producto.getNombre());
            pstmt.setString(3, producto.getDescripcion());
            pstmt.setDouble(4, producto.getPrecio());
            pstmt.setInt(5, producto.getStock());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al registrar producto: " + e.getMessage());
            return false;
        }
    }
    
    // Agrega este método dentro de la clase 'ProductoController.java'

/**
 * Busca un producto específico por su código único.
 * @param codigo El código (SKU) del producto a buscar.
 * @return Un objeto Producto si se encuentra, de lo contrario devuelve null.
 */
    public Producto buscarProductoPorCodigo(String codigo) {
        String sql = "SELECT * FROM productos WHERE codigo = ?";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigo);

            ResultSet rs = pstmt.executeQuery();

            // Verificamos si se encontró un resultado
            if (rs.next()) {
                // Creamos un objeto Producto con los datos de la base de datos
                return new Producto(
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getInt("stock")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar producto por código: " + e.getMessage());
        }

        // Si no se encontró el producto o hubo un error, se devuelve null
        return null;
    }

    /**
     * Obtiene una lista de todos los productos del inventario.
     * @return Una lista de objetos Producto.
     */
    public List<Producto> obtenerTodosLosProductos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY nombre";
        try (Connection conn = ConexionSQLite.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                productos.add(new Producto(
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getInt("stock")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los productos: " + e.getMessage());
        }
        return productos;
    }
    
    /**
     * Actualiza la información de un producto, incluyendo el stock.
     * @param producto El producto con la información actualizada.
     * @return true si la actualización fue exitosa.
     */
    public boolean actualizarProducto(Producto producto) {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, stock = ? WHERE codigo = ?";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getDescripcion());
            pstmt.setDouble(3, producto.getPrecio());
            pstmt.setInt(4, producto.getStock());
            pstmt.setString(5, producto.getCodigo());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina un producto del inventario usando su código.
     * @param codigo El código del producto a eliminar.
     * @return true si la eliminación fue exitosa.
     */
    public boolean eliminarProducto(String codigo) {
        String sql = "DELETE FROM productos WHERE codigo = ?";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, codigo);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }
}