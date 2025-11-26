package controladores;



import basedatos.ConexionSQLite;

import modelos.Mascota;

import java.sql.*;

import java.util.ArrayList;

import java.util.List;



/**

 * Controlador para gestionar las operaciones CRUD de las Mascotas.

 */

public class MascotaController {



    /**

     * Registra una nueva mascota y crea su historial clínico inicial DENTRO de una

     * única transacción para garantizar la integridad de los datos.

     * @param mascota El objeto Mascota con los datos a guardar.

     * @return true si la mascota y su historial fueron creados exitosamente.

     */

    public boolean registrarMascota(Mascota mascota, String rutaFotoMascota, String rutaRegistroVacunas) 

    {

    // SQLs modificados para incluir las nuevas columnas

    String sqlMascota = "INSERT INTO mascotas(nombre, especie, raza, edad, peso, dni_cliente, foto_mascota_ruta) VALUES(?,?,?,?,?,?,?)";

    String sqlHistorial = "INSERT INTO historiales (id_mascota, vacunas_aplicadas, vacunas_pendientes, ultima_desparasitacion, ultima_visita, registro_vacunas_ruta) VALUES (?, ?, ?, ?, ?, ?)";



        Connection conn = null;

        PreparedStatement pstmtMascota = null;

        PreparedStatement pstmtHistorial = null;

        ResultSet generatedKeys = null;



        try {

            conn = ConexionSQLite.conectar();

            if (conn == null) return false;



            // Iniciar transacción

            conn.setAutoCommit(false);



            // --- 1. Insertar la Mascota ---

            pstmtMascota = conn.prepareStatement(sqlMascota, Statement.RETURN_GENERATED_KEYS);

            pstmtMascota.setString(1, mascota.getNombre());

            pstmtMascota.setString(2, mascota.getEspecie());

            pstmtMascota.setString(3, mascota.getRaza());

            pstmtMascota.setInt(4, mascota.getEdad());

            pstmtMascota.setDouble(5, mascota.getPeso());

            pstmtMascota.setString(6, mascota.getDniCliente());

            pstmtMascota.executeUpdate();



            // --- 2. Obtener el ID de la Mascota recién creada ---

            generatedKeys = pstmtMascota.getGeneratedKeys();

            int idNuevaMascota;

            if (generatedKeys.next()) {

                idNuevaMascota = generatedKeys.getInt(1);

            } else {

                throw new SQLException("No se pudo obtener el ID de la mascota creada.");

            }



            // --- 3. Insertar el Historial Clínico usando el nuevo ID ---

            pstmtHistorial = conn.prepareStatement(sqlHistorial);

            pstmtHistorial.setInt(1, idNuevaMascota);

            pstmtHistorial.setString(2, "Ninguna");

            pstmtHistorial.setString(3, "Ninguna");

            pstmtHistorial.setString(4, "No registrada");

            pstmtHistorial.setString(5, "No registrada");

            pstmtHistorial.executeUpdate();



            // Si todo salió bien, confirmar la transacción

            conn.commit();

            return true;



        } catch (SQLException e) {

            System.err.println("Error en la transacción de registro de mascota, se hará rollback: " + e.getMessage());

            try {

                if (conn != null) {

                    conn.rollback(); // Revertir todos los cambios si algo falló

                }

            } catch (SQLException ex) {

                System.err.println("Error al intentar hacer rollback: " + ex.getMessage());

            }

            return false;

        } finally {

            // --- 4. Cerrar todos los recursos ---

            try {

                if (generatedKeys != null) generatedKeys.close();

                if (pstmtMascota != null) pstmtMascota.close();

                if (pstmtHistorial != null) pstmtHistorial.close();

                if (conn != null) {

                    conn.setAutoCommit(true); // Devolver la conexión a su estado normal

                    conn.close();

                }

            } catch (SQLException e) {

                System.err.println("Error al cerrar recursos: " + e.getMessage());

            }

        }

    }

    

    /**

    * Registra una nueva mascota en la base de datos (versión simplificada).

    * @param mascota El objeto Mascota con todos los datos a registrar.

    * @return true si el registro fue exitoso.

    */

   public boolean registrarMascota(Mascota mascota) {

       String sql = "INSERT INTO mascotas(nombre, especie, raza, edad, peso, dni_cliente) VALUES(?,?,?,?,?,?)";

       try (Connection conn = ConexionSQLite.conectar();

            PreparedStatement pstmt = conn.prepareStatement(sql)) {



           pstmt.setString(1, mascota.getNombre());

           pstmt.setString(2, mascota.getEspecie());

           pstmt.setString(3, mascota.getRaza());

           pstmt.setInt(4, mascota.getEdad());

           pstmt.setDouble(5, mascota.getPeso());

           pstmt.setString(6, mascota.getDniCliente());



           return pstmt.executeUpdate() > 0;

       } catch (SQLException e) {

           e.printStackTrace();

           return false;

       }

   }



    /**

     * Obtiene una lista de todas las mascotas asociadas a un cliente por su DNI.

     * @param dniCliente El DNI del cliente.

     * @return Una lista de objetos Mascota.

     */

    public List<Mascota> buscarMascotasPorCliente(String dniCliente) {

        List<Mascota> mascotas = new ArrayList<>();

        String sql = "SELECT * FROM mascotas WHERE dni_cliente = ?";

        try (Connection conn = ConexionSQLite.conectar();

             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            

            pstmt.setString(1, dniCliente);

            ResultSet rs = pstmt.executeQuery();



            while (rs.next()) {

                Mascota mascota = new Mascota(

                    rs.getInt("id"),

                    rs.getString("nombre"),

                    rs.getString("especie"),

                    rs.getString("raza"),

                    rs.getInt("edad"),

                    rs.getDouble("peso"),

                    rs.getString("dni_cliente")

                );

                mascotas.add(mascota);

            }

        } catch (SQLException e) {

            System.err.println("Error al buscar mascotas por cliente: " + e.getMessage());

        }

        return mascotas;

    }

    

    /**

     * Actualiza los datos de una mascota.

     * @param mascota El objeto mascota con los datos actualizados.

     * @return true si la operación fue exitosa.

     */

    public boolean actualizarMascota(Mascota mascota) {

        String sql = "UPDATE mascotas SET nombre = ?, especie = ?, raza = ?, edad = ?, peso = ? WHERE id = ?";

        try (Connection conn = ConexionSQLite.conectar();

             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            

            pstmt.setString(1, mascota.getNombre());

            pstmt.setString(2, mascota.getEspecie());

            pstmt.setString(3, mascota.getRaza());

            pstmt.setInt(4, mascota.getEdad());

            pstmt.setDouble(5, mascota.getPeso());

            pstmt.setInt(6, mascota.getId());



            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println("Error al actualizar la mascota: " + e.getMessage());

            return false;

        }

    }



    /**

     * Elimina una mascota por su ID.

     * @param idMascota El ID único de la mascota.

     * @return true si la mascota fue eliminada.

     */

    public boolean eliminarMascota(int idMascota) {

        String sql = "DELETE FROM mascotas WHERE id = ?";

        try (Connection conn = ConexionSQLite.conectar();

             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            

            pstmt.setInt(1, idMascota);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println("Error al eliminar la mascota: " + e.getMessage());

            return false;

        }

    }

}