package basedatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gestiona la conexión con la base de datos SQLite.
 * También se encarga de la creación inicial de las tablas del sistema.
 */

public class ConexionSQLite {

    private static final String URL = "jdbc:sqlite:veterinaria_dias_vet.db";

    /**
     * Establece una conexión con la base de datos.
     * Implementa el patrón Singleton para mantener una única instancia de conexión.
     * @return La conexión a la base de datos.
     */

    public static Connection conectar() {

        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.err.println("Error en la conexión a SQLite: " + e.getMessage());
            return null;
        }
    }

    /**
     * Crea las tablas necesarias para el sistema si no existen.
     * @param connection La conexión a la base de datos.
     */

    public static void inicializarBaseDatos() {

        // SQL para crear tablas si no existen
        String sqlCliente = "CREATE TABLE IF NOT EXISTS clientes (dni TEXT PRIMARY KEY, nombre TEXT NOT NULL, telefono TEXT, direccion TEXT);";
        String sqlMascota = "CREATE TABLE IF NOT EXISTS mascotas (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL, especie TEXT, raza TEXT, edad INTEGER, peso REAL, dni_cliente TEXT, FOREIGN KEY (dni_cliente) REFERENCES clientes(dni));";
        String sqlProducto = "CREATE TABLE IF NOT EXISTS productos (codigo TEXT PRIMARY KEY, nombre TEXT NOT NULL, descripcion TEXT, precio REAL, stock INTEGER);";
        String sqlCita = "CREATE TABLE IF NOT EXISTS citas (id INTEGER PRIMARY KEY AUTOINCREMENT, fecha_hora TEXT NOT NULL, motivo TEXT, dni_cliente TEXT, id_mascota INTEGER, FOREIGN KEY (dni_cliente) REFERENCES clientes(dni), FOREIGN KEY (id_mascota) REFERENCES mascotas(id));";
        String sqlHistorial = "CREATE TABLE IF NOT EXISTS historiales (id INTEGER PRIMARY KEY AUTOINCREMENT, id_mascota INTEGER NOT NULL, vacunas_aplicadas TEXT, vacunas_pendientes TEXT, ultima_desparasitacion TEXT, ultima_visita TEXT, FOREIGN KEY(id_mascota) REFERENCES mascotas(id));";

        // 1. Nueva tabla para los datos de login
        String sqlUsuario = "CREATE TABLE IF NOT EXISTS usuarios (" +
                    "    nickname TEXT PRIMARY KEY," +
                    "    password_hash TEXT NOT NULL," +
                    "    dni_cliente TEXT NOT NULL," +
                    "    rol TEXT NOT NULL DEFAULT 'cliente'," + // <- linea para designar un rol al usuario
                    "    foto_perfil_ruta TEXT," +
                    "    FOREIGN KEY (dni_cliente) REFERENCES clientes(dni)" +
                    ");";

        // 2. Añadir columnas para las rutas de archivos a las tablas existentes
        String sqlAlterMascotas = "ALTER TABLE mascotas ADD COLUMN foto_mascota_ruta TEXT;";
        String sqlAlterHistorial = "ALTER TABLE historiales ADD COLUMN registro_vacunas_ruta TEXT;";

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement()) {

            if (conn != null) {
                stmt.execute(sqlCliente);
                stmt.execute(sqlMascota);
                stmt.execute(sqlProducto);
                stmt.execute(sqlCita);
                stmt.execute(sqlHistorial);
                stmt.execute(sqlUsuario);
                System.out.println("Base de datos inicializada y tablas verificadas.");

            try { stmt.execute(sqlAlterMascotas); } catch (SQLException e) { /* Ignorar si ya existe */ }
            try { stmt.execute(sqlAlterHistorial); } catch (SQLException e) { /* Ignorar si ya existe */ }
            }
        }
            catch (SQLException e) {
                System.err.println("Error al inicializar la base de datos: " + e.getMessage());
        }
    }
    
}