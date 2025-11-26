package controladores;

import basedatos.ConexionSQLite;
import modelos.Usuario; // <-- Importación necesaria
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class UsuarioController {

    /**
     * -- MÉTODO CORREGIDO --
     * Registra un nuevo usuario en la base de datos a partir de un objeto Usuario.
     * @param usuario El objeto Usuario con todos los datos.
     * @return true si el registro fue exitoso.
     */
    public boolean registrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios(nickname, password_hash, dni_cliente, rol, foto_perfil_ruta) VALUES(?,?,?,?,?)";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getNickname());
            pstmt.setString(2, hashPassword(usuario.getPassword()));
            pstmt.setString(3, usuario.getDniCliente());
            pstmt.setString(4, usuario.getRol());
            pstmt.setString(5, usuario.getFotoPerfilRuta());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * -- MÉTODO CORREGIDO --
     * Verifica las credenciales de un usuario y devuelve su rol.
     * @param nickname El nickname ingresado.
     * @param password La contraseña en texto plano ingresada.
     * @return El rol ("admin", "cliente") si las credenciales son correctas, o null si falla.
     */
    public String verificarLoginYObtenerRol(String nickname, String password) {
        String sql = "SELECT password_hash, rol FROM usuarios WHERE nickname = ?";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nickname);
            ResultSet rs = pstmt.executeQuery();

            // Solo necesitamos un bloque 'if', no dos.
            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                String enteredHash = hashPassword(password);

                if (storedHash.equals(enteredHash)) {
                    return rs.getString("rol"); // ¡Éxito! Devolvemos el rol.
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Login fallido
    }

    /**
     * Elimina un usuario para deshacer un registro si un paso posterior falla.
     * @param nickname El nickname del usuario a eliminar.
     */
    public void eliminarUsuario(String nickname) {
        String sql = "DELETE FROM usuarios WHERE nickname = ?";
        // Usar try-with-resources asegura que la conexión y el statement se cierren solos.
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nickname);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario durante rollback: " + e.getMessage());
        }
    }
    
    public String obtenerDniPorNickname(String nickname) {
       String sql = "SELECT dni_cliente FROM usuarios WHERE nickname = ?";
       try (Connection conn = ConexionSQLite.conectar();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
           pstmt.setString(1, nickname);
           ResultSet rs = pstmt.executeQuery();
           if (rs.next()) {
               return rs.getString("dni_cliente");
           }
       } catch (SQLException e) {
           System.err.println("Error al obtener DNI por nickname: " + e.getMessage());
       }
       return null;
   }

    /**
     * Cifra una contraseña usando el algoritmo SHA-256.
     * @param password La contraseña en texto plano.
     * @return La contraseña cifrada en formato Base64.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception ex) {
            throw new RuntimeException("Error al cifrar la contraseña", ex);
        }
    }
}