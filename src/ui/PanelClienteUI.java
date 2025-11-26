package ui;

import modelos.Cliente; // Importamos el modelo Cliente
import javax.swing.*;
import java.awt.*;

public class PanelClienteUI extends JFrame {

    private Cliente clienteLogueado; // Guardará la información del cliente que inició sesión

    public PanelClienteUI(Cliente cliente) {
        this.clienteLogueado = cliente;

        setTitle("Portal del Cliente - " + clienteLogueado.getNombre());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- Panel Principal con Diseño Moderno ---
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // --- Cabecera de Bienvenida ---
        JLabel lblBienvenida = new JLabel("¡Hola, " + clienteLogueado.getNombre() + "!", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 28));
        panelPrincipal.add(lblBienvenida, BorderLayout.NORTH);
        
        // --- Panel de Botones de Acción ---
        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 25, 25)); // 2x2 grid con espaciado
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JButton btnVerMascotas = new JButton("<html><center>Ver mis<br>Mascotas</center></html>");
        JButton btnAgendarCita = new JButton("<html><center>Agendar<br>una Cita</center></html>");
        JButton btnPetShop = new JButton("<html><center>Pet<br>Shop</center></html>");
        JButton btnMiPerfil = new JButton("<html><center>Mi<br>Perfil</center></html>");

        // Aplicamos un estilo profesional a los botones
        Font botonFont = new Font("Arial", Font.BOLD, 18);
        JButton[] botones = {btnVerMascotas, btnAgendarCita, btnPetShop, btnMiPerfil};
        for (JButton btn : botones) {
            btn.setFont(botonFont);
            btn.setPreferredSize(new Dimension(180, 120));
            btn.setFocusPainted(false);
            // Podrías añadirles iconos aquí
            panelBotones.add(btn);
        }
        
        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
        add(panelPrincipal);

        // --- Lógica de los Botones ---
        btnVerMascotas.addActionListener(e -> {
            // Abrirá la nueva ventana para ver las mascotas del cliente
            MascotasClienteUI mascotasUI = new MascotasClienteUI(clienteLogueado);
            mascotasUI.setVisible(true);
        }); 
        
        btnPetShop.addActionListener(e -> {
            // Creamos y mostramos la nueva ventana de Pet Shop
            PetShopUI petShop = new PetShopUI();
            petShop.setVisible(true);
        });
        
        btnMiPerfil.addActionListener(e -> {
            // Creamos y mostramos la nueva ventana de perfil
            PerfilClienteUI perfilUI = new PerfilClienteUI(clienteLogueado);
            perfilUI.setVisible(true);
        });
       
        btnAgendarCita.addActionListener(e -> {
            // Reutilizamos la ventana de Citas, pero podríamos hacer una versión simplificada
            CitasUI citasUI = new CitasUI();
            citasUI.setVisible(true);
        });
        
        // Este código se activa cuando el usuario hace clic en la "X"
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                // Al cerrar el panel del cliente, volvemos a mostrar la ventana de login
                new LoginUI().setVisible(true);
            }
        });
        
    }
}
