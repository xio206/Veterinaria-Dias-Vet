package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Panel principal del administrador.
 * Ofrece acceso a todos los módulos de gestión del sistema.
 */
public class PanelAdminUI extends JFrame {


    
    public PanelAdminUI() {
        
        setTitle("Panel de Administración - Días Vet");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);


        // -- Panel de botones principal --
        JPanel panelPrincipal = new JPanel(new GridLayout(3, 2, 20, 20));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Botones para cada módulo
        JButton btnClientesMascotas = new JButton("Gestionar Clientes y Mascotas");
        JButton btnCitas = new JButton("Agenda de Citas");
        JButton btnInventario = new JButton("Inventario Pet Shop");
        JButton btnHistorial = new JButton("Historial Clínico");
        JButton btnReportes = new JButton("Generar Reportes");
        JButton btnConfig = new JButton("Configuración");
        
        // Estilo de los botones (personalización)
        Font botonFont = new Font("Arial", Font.BOLD, 16);
        Color botonColor = new Color(70, 130, 180); // SteelBlue
        
        JButton[] botones = {btnClientesMascotas, btnCitas, btnInventario, btnHistorial, btnReportes, btnConfig};
        
        for(JButton btn : botones){
            btn.setFont(botonFont);
            btn.setBackground(botonColor);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            panelPrincipal.add(btn);
        }

        // Botón que abriran las ventanas correspondientes (ClienteMascotaUI, CitasUI, etc.)
        btnClientesMascotas.addActionListener(e -> {
            // Cuando creamos la ventana hija, le pasamos el controlador
            ClienteMascotaUI ventanaClientes = new ClienteMascotaUI();
            ventanaClientes.setVisible(true);
            });
            
        // 2. Botón para abrir la Agenda de Citas
        btnCitas.addActionListener(e -> {
            CitasUI ventanaCitas = new CitasUI();
            ventanaCitas.setVisible(true);
        });    
        
        // 3. Botón para abrir el Inventario
        btnInventario.addActionListener(e -> {
            InventarioUI ventanaInventario = new InventarioUI();
            ventanaInventario.setVisible(true);
        });
        
        // 4. Botón para abrir el Historial Clínico
        btnHistorial.addActionListener(e -> {
            HistorialUI ventanaHistorial = new HistorialUI();
            ventanaHistorial.setVisible(true);
        });
        
        // 5. Botones para módulos futuros (muestran un mensaje)
        ActionListener proximoListener = e -> JOptionPane.showMessageDialog(
            this, 
            "Este módulo está en desarrollo.", 
            "Próximamente", 
            JOptionPane.INFORMATION_MESSAGE
        );
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                // Al cerrar el panel de admin, creamos y mostramos una nueva ventana de login
                new LoginUI().setVisible(true);
            }
        });
        
        btnReportes.addActionListener(proximoListener);
        btnConfig.addActionListener(proximoListener);
        
        add(panelPrincipal);
    }
}