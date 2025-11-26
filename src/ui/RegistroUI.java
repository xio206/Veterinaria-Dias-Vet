package ui;

import controladores.ClienteController;
import controladores.MascotaController;
import controladores.UsuarioController;
import modelos.Cliente;
import modelos.Mascota;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.*;
import java.awt.*;
import modelos.Usuario;

public class RegistroUI extends JFrame {

    // --- Controladores ---
    private ClienteController clienteController;
    private MascotaController mascotaController;
    private UsuarioController usuarioController;
    
    // --- Variables de Instancia (Componentes de la UI) ---
    private CardLayout cardLayout;
    private JPanel panelPrincipal;
    
    // Panel 1: Cuenta
    private JTextField txtNickname;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JButton btnSiguienteCuenta;
    
    // Panel 2: Datos Personales
    private JTextField txtNombres, txtApellidos, txtCorreo, txtTelefono, txtDni, txtFechaNac, txtDireccion;
    private JButton btnAtrasDatos, btnSiguienteDatos;
    
    // Panel 3: Mascota
    private JTextField txtNombreMascota, txtNacimientoMascota, txtRaza, txtCaracteristicas;
    private JComboBox<String> comboEspecie;
    private JComboBox<String> comboSexo;
    private JLabel lblPreviewFotoMascota, lblArchivoVacunas;
    private JButton btnAtrasMascota, btnFinalizar;
    
    // Rutas de los archivos subidos
    private String rutaFotoPerfil; // Para la foto de la persona
    private String rutaFotoMascota;
    private String rutaRegistroVacunas;

   

    // Clase puede ser usada desde cualquier otro lugar
    public RegistroUI() {
        // Inicialización de controladores
        this.clienteController = new ClienteController();
        this.mascotaController = new MascotaController();
        this.usuarioController = new UsuarioController();

        // UI
        setTitle("Registro de Nuevo Cliente - Días Vet");
        setSize(600, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Configuración del CardLayout (Diseño)
        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);

        // --- Panel 1: Creación de Cuenta ---
        JPanel panelCuenta = crearPanelCuenta();
        JPanel panelDatosPersonales = crearPanelDatosPersonales();
        JPanel panelMascota = crearPanelMascota();

        // Añadir los paneles al CardLayout
        panelPrincipal.add(panelCuenta, "CUENTA");
        panelPrincipal.add(panelDatosPersonales, "DATOS_PERSONALES");
        panelPrincipal.add(panelMascota, "MASCOTA");
        add(panelPrincipal);
        configurarNavegacion();
    }

// Clase puede ser usada desde cualquier otro lugar
    private JPanel crearPanelCuenta() {
        //UI
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos de la Cuenta"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15); // Aumentamos el espaciado
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Fila 0: Nickname ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nombre de Usuario:"), gbc);
        txtNickname = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(txtNickname, gbc);

        // --- Fila 1: Contraseña ---
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Contraseña:"), gbc);
        txtPassword = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(txtPassword, gbc);

        // --- Fila 2: Confirmar Contraseña ---
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Confirmar Contraseña:"), gbc);
        txtConfirmPassword = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(txtConfirmPassword, gbc);

        btnSiguienteCuenta = new JButton("Siguiente >");
        btnSiguienteCuenta.setFont(new Font("Arial", Font.BOLD, 12));

        gbc.gridx = 1; // Colocamos el botón en la columna de la derecha
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE; // Para que el botón no se estire
        gbc.anchor = GridBagConstraints.EAST; // Alineamos el botón a la derecha
        gbc.insets = new Insets(20, 15, 10, 15); // Más espacio superior para el botón
        
        panel.add(btnSiguienteCuenta, gbc);

        // Añadimos un componente invisible para empujar todo hacia arriba
        gbc.gridy = 4;
        gbc.weighty = 1.0;
        panel.add(new JLabel(""), gbc);

        return panel;
    }

    // Crea el panel para el Paso 2: Datos personales.
    private JPanel crearPanelDatosPersonales() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Paso 2: Datos Personales"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nombres
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Nombres:"), gbc);
        txtNombres = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; panel.add(txtNombres, gbc);

        // Apellidos
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; panel.add(new JLabel("Apellidos:"), gbc);
        txtApellidos = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2; panel.add(txtApellidos, gbc);

        // Correo
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; panel.add(new JLabel("Correo:"), gbc);
        txtCorreo = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2; panel.add(txtCorreo, gbc);

        // DNI
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; panel.add(new JLabel("DNI:"), gbc);
        txtDni = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2; panel.add(txtDni, gbc);

        // Fecha de nacimiento
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; panel.add(new JLabel("Fecha de Nacimiento YYYY-MM-DD:"), gbc);
        txtFechaNac = new JTextField(20);
        txtFechaNac.setToolTipText("Usar formato YYYY-MM-DD");
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2; panel.add(txtFechaNac, gbc);

        // Teléfono
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1; panel.add(new JLabel("Teléfono:"), gbc);
        txtTelefono = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 2; panel.add(txtTelefono, gbc);

        // Dirección
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1; panel.add(new JLabel("Dirección:"), gbc);
        txtDireccion = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 6; gbc.gridwidth = 2; panel.add(txtDireccion, gbc);

        // Foto de perfil
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Foto de Perfil:"), gbc);

        JButton btnSubirFotoPerfil = new JButton("Seleccionar...");
        gbc.gridx = 1; gbc.gridy = 7; gbc.anchor = GridBagConstraints.WEST; gbc.gridwidth = 1;

        panel.add(btnSubirFotoPerfil, gbc);

        JLabel lblPreviewFotoPerfil = new JLabel("Vista Previa", SwingConstants.CENTER);
        lblPreviewFotoPerfil.setPreferredSize(new Dimension(100, 100));
        lblPreviewFotoPerfil.setBorder(BorderFactory.createEtchedBorder());
        gbc.gridx = 2; gbc.gridy = 7; gbc.gridheight = 2; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblPreviewFotoPerfil, gbc);

        btnSubirFotoPerfil.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "jpeg", "png");
            chooser.setFileFilter(filter);

            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File archivo = chooser.getSelectedFile();
                try {
                    new File("uploads/profiles").mkdirs();
                    String nuevoNombre = System.currentTimeMillis() + "_" + archivo.getName();
                    File destino = new File("uploads/profiles/" + nuevoNombre);
                    Files.copy(archivo.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    this.rutaFotoPerfil = destino.getPath();

                    ImageIcon icon = new ImageIcon(destino.getAbsolutePath());
                    Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    lblPreviewFotoPerfil.setIcon(new ImageIcon(image));
                    lblPreviewFotoPerfil.setText("");

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error al guardar la foto de perfil.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Botones de navegación
        btnAtrasDatos = new JButton("< Atrás");
        btnSiguienteDatos = new JButton("Siguiente >");
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnAtrasDatos);
        panelBotones.add(btnSiguienteDatos);

        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTHEAST; gbc.insets = new Insets(15, 10, 5, 10);

        panel.add(panelBotones, gbc);

        return panel;
    }



    // Crea el panel para el Paso 3: Datos de la mascota y subida de archivos.

    private JPanel crearPanelMascota() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Paso 3: Datos de la Mascota"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- INICIALIZAMOS los campos de la clase ---
        txtNombreMascota = new JTextField(20);
        txtNacimientoMascota = new JTextField();
        txtNacimientoMascota.setToolTipText("Usar formato YYYY-MM-DD");
        // Especie
        String[] especies = {"Canino (Perro)", "Felino (Gato)", "Otro"};
        comboEspecie = new JComboBox<>(especies);
        //Raza
        txtRaza = new JTextField();
        // Sexo
        comboSexo = new JComboBox<>(new String[]{"Macho", "Hembra"});
        // Caracteristicas
        txtCaracteristicas = new JTextField();
        // Subir foto de mascota
        JButton btnSubirFotoMascota = new JButton("Seleccionar...");
        lblPreviewFotoMascota = new JLabel("Vista Previa", SwingConstants.CENTER);
        // Subir foto de registro de vacunas fisico
        JButton btnSubirVacunas = new JButton("Seleccionar...");
        lblArchivoVacunas = new JLabel("Ningún archivo seleccionado.");

        // --- Layout ---
        // Nombre
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; panel.add(txtNombreMascota, gbc);

        // Nacimiento
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; panel.add(new JLabel("Nacimiento (aprox) YYYY-MM-DD:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2; panel.add(txtNacimientoMascota, gbc);

        // Especie
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; panel.add(new JLabel("Especie:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2; panel.add(comboEspecie, gbc);

        // Raza
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; panel.add(new JLabel("Raza:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2; panel.add(txtRaza, gbc);

        // Sexo
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; panel.add(new JLabel("Sexo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2; panel.add(comboSexo, gbc);

        // Características
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1; panel.add(new JLabel("Color o características:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 2; panel.add(txtCaracteristicas, gbc);

        // Subida de archivos
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST; panel.add(new JLabel("Foto de la Mascota:"), gbc);
        gbc.gridx = 1; gbc.gridy = 6; gbc.anchor = GridBagConstraints.WEST; panel.add(btnSubirFotoMascota, gbc);

        lblPreviewFotoMascota.setPreferredSize(new Dimension(100, 100));
        lblPreviewFotoMascota.setBorder(BorderFactory.createEtchedBorder());
        gbc.gridx = 2; gbc.gridy = 6; gbc.gridheight = 2; panel.add(lblPreviewFotoMascota, gbc);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridheight = 1; gbc.anchor = GridBagConstraints.EAST; panel.add(new JLabel("Registro de Vacunación:"), gbc);
        gbc.gridx = 1; gbc.gridy = 7; gbc.anchor = GridBagConstraints.WEST; panel.add(btnSubirVacunas, gbc);
        gbc.gridx = 1; gbc.gridy = 8; panel.add(lblArchivoVacunas, gbc);

        

        // --- Botones de Navegación ---
        btnAtrasMascota = new JButton("< Atrás");
        btnFinalizar = new JButton("Finalizar Registro");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnAtrasMascota);
        panelBotones.add(btnFinalizar);

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.insets = new Insets(20, 10, 5, 10);

        panel.add(panelBotones, gbc);

        // --- LÓGICA DE ACTION LISTENERS ---
        btnSubirFotoMascota.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Imágenes (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif");
            chooser.setFileFilter(filter);

            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File archivo = chooser.getSelectedFile();
                try {
                    new File("uploads/pets").mkdirs();
                    String nuevoNombre = System.currentTimeMillis() + "_" + archivo.getName();
                    File destino = new File("uploads/pets/" + nuevoNombre);
                    Files.copy(archivo.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    this.rutaFotoMascota = destino.getPath();
                    ImageIcon icon = new ImageIcon(destino.getAbsolutePath());
                    Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

                    lblPreviewFotoMascota.setIcon(new ImageIcon(image));
                    lblPreviewFotoMascota.setText("");

                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error al guardar la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnSubirVacunas.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Documentos (PDF, JPG, PNG)", "pdf", "jpg", "jpeg", "png");
            chooser.setFileFilter(filter);

            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File archivo = chooser.getSelectedFile();
                try {
                    new File("uploads/records").mkdirs();
                    String nuevoNombre = System.currentTimeMillis() + "_" + archivo.getName();
                    File destino = new File("uploads/records/" + nuevoNombre);
                    Files.copy(archivo.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    this.rutaRegistroVacunas = destino.getPath();
                    lblArchivoVacunas.setText(archivo.getName());

                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error al guardar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return panel;
    }
    
    //Configura la lógica de los botones para navegar entre los paneles del CardLayout.
    private void configurarNavegacion() {
        btnSiguienteCuenta.addActionListener(e -> cardLayout.show(panelPrincipal, "DATOS_PERSONALES"));
        btnAtrasDatos.addActionListener(e -> cardLayout.show(panelPrincipal, "CUENTA"));
        btnSiguienteDatos.addActionListener(e -> cardLayout.show(panelPrincipal, "MASCOTA"));
        btnAtrasMascota.addActionListener(e -> cardLayout.show(panelPrincipal, "DATOS_PERSONALES"));
        btnFinalizar.addActionListener (e -> {

            // --- 1. Validación de Datos ---
            // Validar contraseñas
            if (txtPassword.getPassword().length == 0 || txtConfirmPassword.getPassword().length == 0) {
                JOptionPane.showMessageDialog(this, "Debe ingresar y confirmar la contraseña.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                cardLayout.show(panelPrincipal, "CUENTA");
                return;
            }

            // Validar campos obligatorios (ejemplo simple)
            if (txtNickname.getText().trim().isEmpty() ||
                txtDni.getText().trim().isEmpty() ||
                txtNombres.getText().trim().isEmpty() ||
                txtApellidos.getText().trim().isEmpty() ||
                txtCorreo.getText().trim().isEmpty() ||
                txtTelefono.getText().trim().isEmpty() ||
                txtNombreMascota.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // --- 2. Recolección de Datos ---

            // Datos Cliente
            String nombres = txtNombres.getText();
            String apellidos = txtApellidos.getText();
            String correo = txtCorreo.getText();
            String dni = txtDni.getText();
            String telefono = txtTelefono.getText();
            String direccion = txtDireccion.getText();

            // Datos Cuenta
            String nickname = txtNickname.getText();
            String password = new String(txtPassword.getPassword());

            // Datos Mascota
            String nombreMascota = txtNombreMascota.getText();
            String especie = (String) comboEspecie.getSelectedItem();
            String raza = txtRaza.getText();

            // (Aquí convertirías la fecha de nacimiento a edad si es necesario)
            int edadMascota = 1; 
            double pesoMascota = 0.0; // Puedes añadir un campo para esto

            // --- 3. Creación de Objetos ---
            Cliente nuevoCliente = new Cliente(dni, nombres + " " + apellidos, telefono, direccion);
            Mascota nuevaMascota = new Mascota(0, nombreMascota, especie, raza, edadMascota, pesoMascota, dni);

            // --- 4. Interacción con Controladores ---
            boolean exito = false;
            // Paso A: Intentar registrar al cliente
            if (clienteController.registrarCliente(nuevoCliente)) {
                // Éxito A: Cliente registrado. Ahora intentamos con el usuario.
                // Paso B: Intentar registrar al usuario
                Usuario nuevoUsuario = new Usuario(nickname, password, dni, "cliente", this.rutaFotoPerfil);
                if (usuarioController.registrarUsuario(nuevoUsuario)) {
                    // Éxito B: Usuario registrado. Ahora intentamos con la mascota.
                    // Paso C: Intentar registrar la mascota
                    // (Asegúrate de que tu método registrarMascota acepte los 3 parámetros)
                    if (mascotaController.registrarMascota(nuevaMascota, rutaFotoMascota, rutaRegistroVacunas)) {
                        // Éxito C: Mascota registrada. ¡Todo el proceso fue exitoso!
                        exito = true;
                    } else {
                        // Fallo C: No se pudo registrar la mascota.
                        // DESHACEMOS los pasos A y B.
                        System.err.println("Fallo al registrar mascota. Deshaciendo registro de usuario y cliente.");
                        usuarioController.eliminarUsuario(nickname);
                        clienteController.eliminarCliente(dni);
                    }
                } else {
                    // Fallo B: No se pudo registrar el usuario.
                    // DESHACEMOS el paso A.
                    System.err.println("Fallo al registrar usuario. Deshaciendo registro de cliente.");
                    clienteController.eliminarCliente(dni);
                }
            } else {
                // Fallo A: No se pudo registrar al cliente. El proceso se detiene.
                System.err.println("Fallo inicial al registrar el cliente.");
            }

            // --- 5. Feedback Final ---

            if (exito) {
                JOptionPane.showMessageDialog(this, "¡Registro completado exitosamente! Ahora puede iniciar sesión.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); // Cierra la ventana de registro
            } else {
                JOptionPane.showMessageDialog(this, "Ocurrió un error durante el registro. Es posible que el DNI o Nickname ya existan.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
            }
        });        
    }
}