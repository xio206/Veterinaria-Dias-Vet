package modelos;

/**
 * Modelo que representa una Cita agendada en la veterinaria.
 * Vincula a un cliente y una mascota para una fecha y motivo específicos.
 */
public class Cita {
    
    private int id; // ID autoincremental de la cita en la BD
    private String fechaHora; // Formato "YYYY-MM-DD HH:MM:SS" para compatibilidad con SQLite
    private String motivo;
    private String dniCliente;
    private int idMascota;

    /**
     * Constructor para crear una nueva instancia de Cita.
     * @param id El identificador único de la cita.
     * @param fechaHora La fecha y hora programadas para la cita.
     * @param motivo La razón de la visita (consulta, vacunación, etc.).
     * @param dniCliente El DNI del cliente que agenda la cita.
     * @param idMascota El ID de la mascota para la cual es la cita.
     */
    public Cita(int id, String fechaHora, String motivo, String dniCliente, int idMascota) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.dniCliente = dniCliente;
        this.idMascota = idMascota;
    }

    // --- Getters y Setters ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getDniCliente() { return dniCliente; }
    public void setDniCliente(String dniCliente) { this.dniCliente = dniCliente; }

    public int getIdMascota() { return idMascota; }
    public void setIdMascota(int idMascota) { this.idMascota = idMascota; }
}