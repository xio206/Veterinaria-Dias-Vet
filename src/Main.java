import ui.LoginUI;
import javax.swing.SwingUtilities;
import basedatos.ConexionSQLite; // Importar

/**
 * Clase principal que inicia la aplicación del sistema de gestión para la veterinaria "Días Vet".
 * @author TuNombreCompleto
 * @version 1.0
 * @since 2025-10-03
 */
public class Main {
    public static void main(String[] args) {
        // Primero, nos aseguramos de que la base de datos y las tablas existan.
        ConexionSQLite.inicializarBaseDatos();
        // Se utiliza SwingUtilities.invokeLater para asegurar que la creación de la GUI
        // se realice en el hilo de despacho de eventos (EDT), práctica recomendada en Swing.
        SwingUtilities.invokeLater(() -> {
            System.out.println("Proyecto Veterinaria Días Vet iniciado...");
            LoginUI loginFrame = new LoginUI();
            loginFrame.setVisible(true);
        });
    }
}