package controladores;

import basedatos.ConexionSQLite;
import modelos.Cita;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para la gestión de Citas.
 * Incluye la lógica de negocio para validar horarios.
 */
public class CitaController {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Valida si una fecha y hora están dentro del horario de atención.
     * @param fechaHora La fecha y hora a validar.
     * @return true si el horario es válido, false en caso contrario.
     */
    public boolean validarHorarioDisponible(LocalDateTime fechaHora) {
        DayOfWeek dia = fechaHora.getDayOfWeek();
        int hora = fechaHora.getHour();

        if (dia == DayOfWeek.SUNDAY) {
            // Domingo: 7:00 am a 9:00 am (hora 7 y 8)
            return hora >= 7 && hora < 9;
        } else {
            // Lunes a Sábado
            // Turno mañana: 7:00 am a 12:00 pm (horas 7 a 11)
            boolean turnoManana = hora >= 7 && hora < 12;
            // Turno tarde: 3:00 pm a 7:00 pm (horas 15 a 18)
            boolean turnoTarde = hora >= 15 && hora < 19;
            return turnoManana || turnoTarde;
        }
    }

    /**
     * Registra una nueva cita en la base de datos previa validación.
     * @param cita El objeto Cita a registrar.
     * @return true si el registro fue exitoso.
     */
    public boolean registrarCita(Cita cita) {
        LocalDateTime fechaHoraCita = LocalDateTime.parse(cita.getFechaHora(), FORMATTER);
        if (!validarHorarioDisponible(fechaHoraCita)) {
            System.err.println("Error: El horario seleccionado no está disponible.");
            return false;
        }

        String sql = "INSERT INTO citas(fecha_hora, motivo, dni_cliente, id_mascota) VALUES(?,?,?,?)";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cita.getFechaHora());
            pstmt.setString(2, cita.getMotivo());
            pstmt.setString(3, cita.getDniCliente());
            pstmt.setInt(4, cita.getIdMascota());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al registrar la cita: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todas las citas ordenadas por fecha.
     * @return Una lista de objetos Cita.
     */
    public List<Cita> obtenerTodasLasCitas() {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT * FROM citas ORDER BY fecha_hora ASC";
        try (Connection conn = ConexionSQLite.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                citas.add(new Cita(
                    rs.getInt("id"),
                    rs.getString("fecha_hora"),
                    rs.getString("motivo"),
                    rs.getString("dni_cliente"),
                    rs.getInt("id_mascota")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las citas: " + e.getMessage());
        }
        return citas;
    }

    /**
     * Elimina una cita de la base de datos por su ID.
     * @param idCita El ID de la cita a eliminar.
     * @return true si la eliminación fue exitosa.
     */
    public boolean eliminarCita(int idCita) {
        String sql = "DELETE FROM citas WHERE id = ?";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCita);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar la cita: " + e.getMessage());
            return false;
        }
    }
}