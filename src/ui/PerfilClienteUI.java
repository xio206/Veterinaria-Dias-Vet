package ui;

import controladores.ClienteController;
import controladores.MascotaController;
import modelos.Cliente;
import modelos.Mascota;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PerfilClienteUI extends JFrame {

    private final Cliente clienteLogueado;
    private final ClienteController clienteController;
    private final MascotaController mascotaController;
    private JTextField txtNombre, txtTelefono, txtDireccion;
    private JComboBox<Mascota> comboMascotas;
    private JTextField txtNombreMascota, txtRazaMascota, txtEdadMascota, txtPesoMascota;
    private JComboBox<String> comboEspecieMascota;
    private JButton btnAccionMascota;
    
    public PerfilClienteUI(Cliente cliente) {
        this.clienteLogueado = cliente;
        this.clienteController = new ClienteController();
        this.mascotaController = new MascotaController();

        setTitle("Mi Perfil - " + cliente.getNombre());
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Mis Datos Personales", crearPanelDatosPersonales());
        tabbedPane.addTab("Gestionar Mascotas", crearPanelMascotas());
        add(tabbedPane);
    }
    
    private JPanel crearPanelDatosPersonales() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre = new JTextField(clienteLogueado.getNombre(), 20);
        txtTelefono = new JTextField(clienteLogueado.getTelefono(), 20);
        txtDireccion = new JTextField(clienteLogueado.getDireccion(), 20);
        JButton btnActualizarCliente = new JButton("Actualizar Mis Datos");
        
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("DNI:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(new JLabel(clienteLogueado.getDni()), gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Nombre Completo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(txtNombre, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(txtTelefono, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(txtDireccion, gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.anchor = GridBagConstraints.EAST; panel.add(btnActualizarCliente, gbc);

        btnActualizarCliente.addActionListener(e -> {
            clienteLogueado.setNombre(txtNombre.getText());
            clienteLogueado.setTelefono(txtTelefono.getText());
            clienteLogueado.setDireccion(txtDireccion.getText());
            if (clienteController.actualizarCliente(clienteLogueado)) {
                JOptionPane.showMessageDialog(this, "Datos actualizados con éxito.");
                setTitle("Mi Perfil - " + clienteLogueado.getNombre());
            }
        });
        return panel;
    }
    
    private JPanel crearPanelMascotas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        comboMascotas = new JComboBox<>();
        cargarMascotasEnCombo();
        panelSeleccion.add(new JLabel("Seleccionar Mascota:"));
        panelSeleccion.add(comboMascotas);
        panel.add(panelSeleccion, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNombreMascota = new JTextField(20);
        comboEspecieMascota = new JComboBox<>(new String[]{"Canino (Perro)", "Felino (Gato)", "Otro"});
        txtRazaMascota = new JTextField(20);
        txtEdadMascota = new JTextField(20);
        txtPesoMascota = new JTextField(20);
        btnAccionMascota = new JButton("Actualizar Mascota");
        
        gbc.gridx=0; gbc.gridy=0; panelFormulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx=1; gbc.gridy=0; panelFormulario.add(txtNombreMascota, gbc);
        gbc.gridx=0; gbc.gridy=1; panelFormulario.add(new JLabel("Especie:"), gbc);
        gbc.gridx=1; gbc.gridy=1; panelFormulario.add(comboEspecieMascota, gbc);
        gbc.gridx=0; gbc.gridy=2; panelFormulario.add(new JLabel("Raza:"), gbc);
        gbc.gridx=1; gbc.gridy=2; panelFormulario.add(txtRazaMascota, gbc);
        gbc.gridx=0; gbc.gridy=3; panelFormulario.add(new JLabel("Edad (años):"), gbc);
        gbc.gridx=1; gbc.gridy=3; panelFormulario.add(txtEdadMascota, gbc);
        gbc.gridx=0; gbc.gridy=4; panelFormulario.add(new JLabel("Peso (Kg):"), gbc);
        gbc.gridx=1; gbc.gridy=4; panelFormulario.add(txtPesoMascota, gbc);
        gbc.gridx=1; gbc.gridy=5; gbc.anchor = GridBagConstraints.EAST; panelFormulario.add(btnAccionMascota, gbc);
        panel.add(panelFormulario, BorderLayout.CENTER);
        
        comboMascotas.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                Object item = comboMascotas.getSelectedItem();
                if (item instanceof Mascota) {
                    Mascota mascota = (Mascota) item;
                    if (mascota.getId() == -1) { // Opción "Agregar Nueva"
                        limpiarFormularioMascota();
                        btnAccionMascota.setText("Registrar Mascota");
                    } else {
                        cargarDatosDeMascotaEnFormulario(mascota);
                        btnAccionMascota.setText("Actualizar Mascota");
                    }
                }
            }
        });

        btnAccionMascota.addActionListener(e -> {
            if (btnAccionMascota.getText().equals("Registrar Mascota")) {
                registrarNuevaMascota();
            } else {
                actualizarMascotaExistente();
            }
        });
        return panel;
    }
    
    private void cargarMascotasEnCombo() {
        comboMascotas.removeAllItems();
        List<Mascota> mascotas = mascotaController.buscarMascotasPorCliente(clienteLogueado.getDni());
        for (Mascota mascota : mascotas) {
            comboMascotas.addItem(mascota);
        }
        Mascota opcionAgregar = new Mascota(-1, "-- Agregar Mascota Nueva --", "", "", 0, 0.0, "");
        comboMascotas.addItem(opcionAgregar);
        
        comboMascotas.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Mascota) {
                    setText(((Mascota) value).getNombre());
                }
                return this;
            }
        });
    }
    
    private void limpiarFormularioMascota() {
        txtNombreMascota.setText("");
        comboEspecieMascota.setSelectedIndex(0);
        txtRazaMascota.setText("");
        txtEdadMascota.setText("");
        txtPesoMascota.setText("");
    }
    
    private void cargarDatosDeMascotaEnFormulario(Mascota mascota) {
        txtNombreMascota.setText(mascota.getNombre());
        comboEspecieMascota.setSelectedItem(mascota.getEspecie());
        txtRazaMascota.setText(mascota.getRaza());
        txtEdadMascota.setText(String.valueOf(mascota.getEdad()));
        txtPesoMascota.setText(String.valueOf(mascota.getPeso()));
    }

    private void registrarNuevaMascota() {
        try {
            String nombre = txtNombreMascota.getText();
            if (nombre.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre de la mascota es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Mascota nuevaMascota = new Mascota(0, nombre, 
                (String) comboEspecieMascota.getSelectedItem(), 
                txtRazaMascota.getText(), 
                Integer.parseInt(txtEdadMascota.getText()), 
                Double.parseDouble(txtPesoMascota.getText()), 
                clienteLogueado.getDni());
            
            if (mascotaController.registrarMascota(nuevaMascota, null, null)) {
                JOptionPane.showMessageDialog(this, "Mascota registrada con éxito.");
                cargarMascotasEnCombo();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar la mascota.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La edad y el peso deben ser números.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarMascotaExistente() {
        Mascota mascotaSeleccionada = (Mascota) comboMascotas.getSelectedItem();
        if (mascotaSeleccionada == null || mascotaSeleccionada.getId() == -1) return;
        try {
            mascotaSeleccionada.setNombre(txtNombreMascota.getText());
            mascotaSeleccionada.setEspecie((String) comboEspecieMascota.getSelectedItem());
            mascotaSeleccionada.setRaza(txtRazaMascota.getText());
            mascotaSeleccionada.setEdad(Integer.parseInt(txtEdadMascota.getText()));
            mascotaSeleccionada.setPeso(Double.parseDouble(txtPesoMascota.getText()));
            
            if (mascotaController.actualizarMascota(mascotaSeleccionada)) {
                JOptionPane.showMessageDialog(this, "Datos de la mascota actualizados.");
                cargarMascotasEnCombo();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La edad y el peso deben ser números.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}