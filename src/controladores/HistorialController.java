package controladores;

import basedatos.ConexionSQLite;
import modelos.Historial;
import java.sql.*;
import modelos.Mascota;

public class HistorialController {

    /**
     * Crea un registro de historial vacío para una nueva mascota.
     * Se asegura de que cada mascota tenga un historial desde su registro.
     * @param idMascota El ID de la mascota recién creada.
     */

    public void crearHistorialInicial(int idMascota) {

        String sql = "INSERT INTO historiales (id_mascota, vacunas_aplicadas, vacunas_pendientes, ultima_desparasitacion, ultima_visita) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionSQLite.conectar();

             PreparedStatement pstmt = conn.prepareStatement(sql)) {



            pstmt.setInt(1, idMascota);

            pstmt.setString(2, "Ninguna"); // Valor por defecto

            pstmt.setString(3, "Ninguna"); // Valor por defecto

            pstmt.setString(4, "No registrada"); // Valor por defecto

            pstmt.setString(5, "No registrada"); // Valor por defecto

            pstmt.executeUpdate();



        } catch (SQLException e) {

            System.err.println("Error al crear historial inicial: " + e.getMessage());

        }

    }



    /**

     * Busca y obtiene el historial clínico de una mascota específica.

     * @param idMascota El ID de la mascota cuyo historial se desea obtener.

     * @return Un objeto Historial con los datos, o null si no se encuentra.

     */

    public Historial obtenerHistorialPorMascota(int idMascota) {

        String sql = "SELECT * FROM historiales WHERE id_mascota = ?";

        try (Connection conn = ConexionSQLite.conectar();

             PreparedStatement pstmt = conn.prepareStatement(sql)) {



            pstmt.setInt(1, idMascota);

            ResultSet rs = pstmt.executeQuery();



            if (rs.next()) {

                return new Historial(

                        rs.getInt("id"),

                        rs.getInt("id_mascota"),

                        rs.getString("vacunas_aplicadas"),

                        rs.getString("vacunas_pendientes"),

                        rs.getString("ultima_desparasitacion"),

                        rs.getString("ultima_visita")

                );

            }

        } catch (SQLException e) {

            System.err.println("Error al obtener el historial: " + e.getMessage());

        }

        return null;

    }



    /**

     * Actualiza la información de un historial clínico existente.

     * @param historial El objeto Historial con los datos modificados.

     * @return true si la actualización fue exitosa, false en caso contrario.

     */

    public boolean actualizarHistorial(Historial historial) {

        String sql = "UPDATE historiales SET vacunas_aplicadas = ?, vacunas_pendientes = ?, ultima_desparasitacion = ?, ultima_visita = ? WHERE id_mascota = ?";

        try (Connection conn = ConexionSQLite.conectar();

             PreparedStatement pstmt = conn.prepareStatement(sql)) {



            pstmt.setString(1, historial.getVacunasAplicadas());

            pstmt.setString(2, historial.getVacunasPendientes());

            pstmt.setString(3, historial.getUltimaDesparasitacion());

            pstmt.setString(4, historial.getUltimaVisita());

            pstmt.setInt(5, historial.getIdMascota());



            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println("Error al actualizar el historial: " + e.getMessage());

            return false;

        }

    }

    

    // Dentro de la clase MascotaController...



    /**

     * Registra una nueva mascota y crea su historial clínico inicial.

     * @param mascota El objeto Mascota con los datos a guardar.

     * @return true si el registro fue exitoso, false en caso contrario.

     */

    public boolean registrarMascota(Mascota mascota) {

        String sql = "INSERT INTO mascotas(nombre, especie, raza, edad, peso, dni_cliente) VALUES(?,?,?,?,?,?)";

        Connection conn = null;

        try {

            conn = ConexionSQLite.conectar();

            if (conn == null) return false;



            // Desactivamos el auto-commit para manejar la transacción manualmente

            conn.setAutoCommit(false); 



            // 1. Insertar la mascota

            try (PreparedStatement pstmtMascota = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstmtMascota.setString(1, mascota.getNombre());

                pstmtMascota.setString(2, mascota.getEspecie());

                pstmtMascota.setString(3, mascota.getRaza());

                pstmtMascota.setInt(4, mascota.getEdad());

                pstmtMascota.setDouble(5, mascota.getPeso());

                pstmtMascota.setString(6, mascota.getDniCliente());



                int filasAfectadas = pstmtMascota.executeUpdate();

                if (filasAfectadas == 0) {

                    conn.rollback(); // Si no se insertó la mascota, revertimos todo

                    return false;

                }



                // 2. Obtener el ID de la mascota recién insertada

                try (ResultSet generatedKeys = pstmtMascota.getGeneratedKeys()) {

                    if (generatedKeys.next()) {

                        int idNuevaMascota = generatedKeys.getInt(1);

                        // 3. Crear su historial clínico inicial

                        HistorialController historialController = new HistorialController();

                        historialController.crearHistorialInicial(idNuevaMascota);

                    } else {

                        conn.rollback(); // Si no se pudo obtener el ID, revertimos

                        throw new SQLException("Fallo al obtener el ID de la mascota, no se pudo crear el historial.");

                    }
                }
            }



            conn.commit(); // Si todo fue bien, confirmamos la transacción

            return true;



        } catch (SQLException e) {

            System.err.println("Error en la transacción de registro de mascota: " + e.getMessage());

            try {

                if (conn != null) conn.rollback(); // En caso de cualquier error, revertimos

            } catch (SQLException ex) {

                System.err.println("Error al hacer rollback: " + ex.getMessage());

            }

            return false;

        } finally {

            try {

                if (conn != null) conn.setAutoCommit(true); // Restauramos el modo auto-commit

                if (conn != null) conn.close();

            } catch (SQLException e) {

                System.err.println("Error al cerrar conexión: " + e.getMessage());

            }
        }
    }
}