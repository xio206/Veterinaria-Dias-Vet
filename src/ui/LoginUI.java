package ui;

import controladores.ClienteController;
import controladores.UsuarioController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import modelos.Cliente;

/**
 * Ventana de Login para el acceso al sistema.
 * Permite la autenticación de usuarios (administrador, cliente).
 */
public class LoginUI extends JFrame {

    public LoginUI() {
        setTitle("Días Vet - Inicio de Sesión");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla
        setResizable(false);

        // -- Panel Principal --
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // -- Componentes --
        JLabel lblUsuario = new JLabel("Usuario:");
        JTextField txtUsuario = new JTextField(20);
        JLabel lblClave = new JLabel("Contraseña:");
        JPasswordField txtClave = new JPasswordField(20);
        JButton btnIngresar = new JButton("Ingresar");
        JButton btnRegistrarse = new JButton("Registrarse");

        // -- Diseño (Layout) --
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(lblUsuario, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(txtUsuario, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblClave, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(txtClave, gbc);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.add(btnIngresar);
        panelBotones.add(btnRegistrarse);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(panelBotones, gbc);

        // -- Lógica de Eventos --
        // --- ActionListener para INGRESAR ---
        btnIngresar.addActionListener((ActionEvent e) -> {
            String usuario = txtUsuario.getText();
            String clave = new String(txtClave.getPassword());

            // 1. Validación de campos vacíos
            if (usuario.trim().isEmpty() || clave.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar usuario y contraseña.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            UsuarioController usuarioController = new UsuarioController();

            // 2. Verificar credenciales y obtener el rol
            // Se pasan las variables 'usuario' y 'clave' al método
            String rol = usuarioController.verificarLoginYObtenerRol(usuario, clave);

            if (rol != null) { // Si el login es exitoso...
                JOptionPane.showMessageDialog(this, "¡Bienvenido!", "Acceso Concedido", JOptionPane.INFORMATION_MESSAGE);

                if (rol.equals("admin")) {
                    // --- CASO ADMINISTRADOR ---
                    PanelAdminUI panelAdmin = new PanelAdminUI();
                    panelAdmin.setVisible(true);
                    this.dispose(); 
                } else {
                    // --- CASO CLIENTE ---
                    // Usamos la variable 'usuario' para buscar el DNI
                    String dniCliente = usuarioController.obtenerDniPorNickname(usuario); 

                    if (dniCliente != null) {
                        Cliente clienteLogueado = new ClienteController().buscarClientePorDni(dniCliente);

                        if (clienteLogueado != null) {
                            PanelClienteUI panelCliente = new PanelClienteUI(clienteLogueado);
                            panelCliente.setVisible(true);
                            this.dispose();
                        } else {
                            JOptionPane.showMessageDialog(this, "Error crítico: No se encontraron los datos del cliente.", "Error de Datos", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Error crítico: No se pudo encontrar el DNI para este usuario.", "Error de Datos", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                // Si el login falla
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            }
        });

                // --- ActionListener para REGISTRARSE ---
                btnRegistrarse.addActionListener(e -> {
                    RegistroUI ventanaRegistro = new RegistroUI();
                    ventanaRegistro.setVisible(true);
                    //this.dispose();
                });

                add(panel);
            }
        }

