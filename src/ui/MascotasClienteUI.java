package ui;

import controladores.MascotaController;
import modelos.Cliente;
import modelos.Mascota;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MascotasClienteUI extends JFrame {

    private Cliente clienteLogueado;
    private MascotaController mascotaController;
    private JTable tablaMascotas;
    private DefaultTableModel modeloTabla;

    public MascotasClienteUI(Cliente cliente) {
        this.clienteLogueado = cliente;
        this.mascotaController = new MascotaController();

        setTitle("Mis Mascotas - " + cliente.getNombre());
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Se cierra solo esta ventana
        setLocationRelativeTo(null);

        // --- Panel Principal ---
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Tabla para mostrar las mascotas ---
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Especie", "Raza", "Edad"}, 0);
        tablaMascotas = new JTable(modeloTabla);
        tablaMascotas.setRowHeight(25);
        tablaMascotas.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaMascotas.getColumnModel().getColumn(0).setMaxWidth(50);
        panel.add(new JScrollPane(tablaMascotas), BorderLayout.CENTER);
        
        // --- Panel de Botones (Ver Historial, Añadir Mascota) ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVerHistorial = new JButton("Ver Historial Clínico");
        panelBotones.add(btnVerHistorial);
        panel.add(panelBotones, BorderLayout.SOUTH);

        add(panel);
        
        cargarMascotas();

        // --- Lógica de Botones ---
        btnVerHistorial.addActionListener(e -> {
            int filaSeleccionada = tablaMascotas.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione una mascota de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Aquí abriríamos la ventana de HistorialUI, pasándole el ID de la mascota
            // (La implementación actual de HistorialUI habría que adaptarla un poco)
            JOptionPane.showMessageDialog(this, "Funcionalidad 'Ver Historial' en desarrollo.", "Próximamente", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void cargarMascotas() {
        modeloTabla.setRowCount(0); // Limpiar tabla
        List<Mascota> mascotas = mascotaController.buscarMascotasPorCliente(clienteLogueado.getDni());
        for (Mascota mascota : mascotas) {
            modeloTabla.addRow(new Object[]{
                mascota.getId(),
                mascota.getNombre(),
                mascota.getEspecie(),
                mascota.getRaza(),
                mascota.getEdad() + " años"
            });
        }
    }
}