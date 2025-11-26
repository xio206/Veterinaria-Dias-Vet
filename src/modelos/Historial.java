package modelos;

public class Historial {
    private int id;
    private int idMascota;
    private String vacunasAplicadas;
    private String vacunasPendientes;
    private String ultimaDesparasitacion;
    private String ultimaVisita;

    public Historial(int id, int idMascota, String vacunasAplicadas, String vacunasPendientes, String ultimaDesparasitacion, String ultimaVisita) {
        this.id = id;
        this.idMascota = idMascota;
        this.vacunasAplicadas = vacunasAplicadas;
        this.vacunasPendientes = vacunasPendientes;
        this.ultimaDesparasitacion = ultimaDesparasitacion;
        this.ultimaVisita = ultimaVisita;
    }

    // --- Getters y Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdMascota() { return idMascota; }
    public void setIdMascota(int idMascota) { this.idMascota = idMascota; }
    public String getVacunasAplicadas() { return vacunasAplicadas; }
    public void setVacunasAplicadas(String vacunasAplicadas) { this.vacunasAplicadas = vacunasAplicadas; }
    public String getVacunasPendientes() { return vacunasPendientes; }
    public void setVacunasPendientes(String vacunasPendientes) { this.vacunasPendientes = vacunasPendientes; }
    public String getUltimaDesparasitacion() { return ultimaDesparasitacion; }
    public void setUltimaDesparasitacion(String ultimaDesparasitacion) { this.ultimaDesparasitacion = ultimaDesparasitacion; }
    public String getUltimaVisita() { return ultimaVisita; }
    public void setUltimaVisita(String ultimaVisita) { this.ultimaVisita = ultimaVisita; }
}