package modelos;

/**
 * Modelo que representa a un Usuario del sistema con sus credenciales de acceso.
 */
public class Usuario {

    private String nickname;
    private String password; // El modelo maneja la contraseña sin cifrar
    private String dniCliente;
    private String rol;
    private String fotoPerfilRuta;

    /**
     * Constructor para crear una nueva instancia de Usuario.
     */
    public Usuario(String nickname, String password, String dniCliente, String rol, String fotoPerfilRuta) {
        this.nickname = nickname;
        this.password = password;
        this.dniCliente = dniCliente;
        this.rol = rol;
        this.fotoPerfilRuta = fotoPerfilRuta;
    }

    // --- Getters y Setters ---

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDniCliente() {
        return dniCliente;
    }

    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getFotoPerfilRuta() {
        return fotoPerfilRuta;
    }

    public void setFotoPerfilRuta(String fotoPerfilRuta) {
        this.fotoPerfilRuta = fotoPerfilRuta;
    }
}
