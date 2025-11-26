package ui;

// import com.toedter.calendar.JDateChooser; // <-- CAMBIO: Eliminado el import

import controladores.ClienteController;

import controladores.HistorialController;

import controladores.MascotaController;

import modelos.Cliente;

import modelos.Historial;

import modelos.Mascota;



import javax.swing.*;

import java.awt.*;

import java.text.SimpleDateFormat;

// import java.util.Date; // Ya no es necesario para la UI

import java.util.List;



public class HistorialUI extends JFrame {



    private ClienteController clienteController;

    private MascotaController mascotaController;

    private HistorialController historialController;



    private JComboBox<Cliente> comboClientes;

    private JComboBox<Mascota> comboMascotas;

    private JTextArea areaVacunasAplicadas, areaVacunasPendientes;

    private JTextField txtDesparasitacion, txtUltimaVisita; // <-- CAMBIO: Volvemos a usar JTextField

    private JButton btnGuardar;

    private JPanel panelCentral;

    

    private Historial historialActual;

    // El SimpleDateFormat ya no es necesario para la UI, pero puede ser útil para validaciones

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");



    public HistorialUI() {

        this.clienteController = new ClienteController();

        this.mascotaController = new MascotaController();

        this.historialController = new HistorialController();

        

        setTitle("Historial Clínico - Días Vet");

        setSize(800, 600);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        

        inicializarComponentes();

        cargarClientes();

    }

    

    private void inicializarComponentes() {

        setLayout(new BorderLayout(10, 10));



        // --- Panel de Selección (Arriba) ---

        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        panelSeleccion.setBorder(BorderFactory.createTitledBorder("Seleccionar Mascota"));

        panelSeleccion.add(new JLabel("Cliente:"));

        comboClientes = new JComboBox<>();

        comboClientes.setPreferredSize(new Dimension(200, 25));

        panelSeleccion.add(comboClientes);

        panelSeleccion.add(Box.createHorizontalStrut(20));

        panelSeleccion.add(new JLabel("Mascota:"));

        comboMascotas = new JComboBox<>();

        comboMascotas.setPreferredSize(new Dimension(200, 25));

        panelSeleccion.add(comboMascotas);

        add(panelSeleccion, BorderLayout.NORTH);



        // --- Panel Central ---

        panelCentral = new JPanel();

        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        

        // --- Sub-Panel para Vacunas ---

        JPanel panelVacunas = new JPanel(new GridLayout(1, 2, 10, 0));

        panelVacunas.setBorder(BorderFactory.createTitledBorder("Registro de Vacunación"));

        JPanel panelVacunasAplicadas = new JPanel(new BorderLayout());

        panelVacunasAplicadas.add(new JLabel("Vacunas Aplicadas:"), BorderLayout.NORTH);

        areaVacunasAplicadas = new JTextArea(8, 20);

        panelVacunasAplicadas.add(new JScrollPane(areaVacunasAplicadas), BorderLayout.CENTER);

        JPanel panelVacunasPendientes = new JPanel(new BorderLayout());

        panelVacunasPendientes.add(new JLabel("Vacunas Pendientes:"), BorderLayout.NORTH);

        areaVacunasPendientes = new JTextArea(8, 20);

        panelVacunasPendientes.add(new JScrollPane(areaVacunasPendientes), BorderLayout.CENTER);

        panelVacunas.add(panelVacunasAplicadas);

        panelVacunas.add(panelVacunasPendientes);

        panelCentral.add(panelVacunas);

        panelCentral.add(Box.createVerticalStrut(15));



        // --- Sub-Panel para Fechas Clave ---

        JPanel panelFechas = new JPanel(new GridBagLayout());

        panelFechas.setBorder(BorderFactory.createTitledBorder("Fechas Clave"));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.weightx = 1.0;



        // <-- CAMBIO: Creamos los JTextField para las fechas

        txtDesparasitacion = new JTextField();

        txtDesparasitacion.setToolTipText("Ingrese la fecha en formato YYYY-MM-DD");

        txtUltimaVisita = new JTextField();

        txtUltimaVisita.setToolTipText("Ingrese la fecha en formato YYYY-MM-DD");



        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; panelFechas.add(new JLabel("Última Desparasitación:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1; panelFechas.add(txtDesparasitacion, gbc); // <-- CAMBIO: Añadimos el JTextField

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; panelFechas.add(new JLabel("Última Visita General:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1; panelFechas.add(txtUltimaVisita, gbc); // <-- CAMBIO: Añadimos el JTextField



        panelCentral.add(panelFechas);

        

        panelCentral.add(Box.createVerticalGlue());



        // --- Panel del Botón Guardar ---

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnGuardar = new JButton("Guardar Cambios");

        btnGuardar.setFont(new Font("Arial", Font.BOLD, 14));

        panelBoton.add(btnGuardar);

        panelCentral.add(panelBoton);



        add(panelCentral, BorderLayout.CENTER);

        

        setFieldsEnabled(false);



        // --- Action Listeners ---

        comboClientes.addActionListener(e -> cargarMascotasDeCliente());

        comboMascotas.addActionListener(e -> cargarHistorialDeMascota());

        btnGuardar.addActionListener(e -> guardarHistorial());

    }



    private void cargarClientes() {

        // ... (Este método no cambia)

        comboClientes.setRenderer(new DefaultListCellRenderer() {

            @Override

            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof Cliente) setText(((Cliente) value).getNombre());

                else setText("Seleccione un cliente...");

                return this;

            }

        });

        

        List<Cliente> clientes = clienteController.obtenerTodosLosClientes();

        comboClientes.removeAllItems();

        comboClientes.addItem(null);

        for (Cliente cliente : clientes) {

            comboClientes.addItem(cliente);

        }

    }

    

    private void cargarMascotasDeCliente() {

        // ... (Este método no cambia)

        Cliente clienteSeleccionado = (Cliente) comboClientes.getSelectedItem();

        if (clienteSeleccionado == null) {

            comboMascotas.removeAllItems();

            return;

        }

        

        comboMascotas.setRenderer(new DefaultListCellRenderer() {

             @Override

            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof Mascota) setText(((Mascota) value).getNombre());

                else setText("Seleccione una mascota...");

                return this;

            }

        });



        List<Mascota> mascotas = mascotaController.buscarMascotasPorCliente(clienteSeleccionado.getDni());

        comboMascotas.removeAllItems();

        comboMascotas.addItem(null);

        for (Mascota mascota : mascotas) {

            comboMascotas.addItem(mascota);

        }

    }



    private void cargarHistorialDeMascota() {

        Mascota mascotaSeleccionada = (Mascota) comboMascotas.getSelectedItem();

        if (mascotaSeleccionada == null) {

            limpiarCampos();

            return;

        }



        historialActual = historialController.obtenerHistorialPorMascota(mascotaSeleccionada.getId());

        if (historialActual != null) {

            areaVacunasAplicadas.setText(historialActual.getVacunasAplicadas());

            areaVacunasPendientes.setText(historialActual.getVacunasPendientes());

            

            // <-- CAMBIO: Cargamos el texto directamente

            txtDesparasitacion.setText(historialActual.getUltimaDesparasitacion());

            txtUltimaVisita.setText(historialActual.getUltimaVisita());

            

            setFieldsEnabled(true);

        } else {

            limpiarCampos();

            JOptionPane.showMessageDialog(this, "No se encontró historial para esta mascota.", "Aviso", JOptionPane.WARNING_MESSAGE);

        }

    }



    private void guardarHistorial() {

        if (historialActual == null) {

            JOptionPane.showMessageDialog(this, "No hay un historial cargado para guardar.", "Error", JOptionPane.ERROR_MESSAGE);

            return;

        }

        

        historialActual.setVacunasAplicadas(areaVacunasAplicadas.getText());

        historialActual.setVacunasPendientes(areaVacunasPendientes.getText());

        

        // <-- CAMBIO: Obtenemos el texto directamente de los campos

        historialActual.setUltimaDesparasitacion(txtDesparasitacion.getText().trim());

        historialActual.setUltimaVisita(txtUltimaVisita.getText().trim());



        if (historialController.actualizarHistorial(historialActual)) {

            JOptionPane.showMessageDialog(this, "Historial actualizado con éxito.");

        } else {

            JOptionPane.showMessageDialog(this, "No se pudo actualizar el historial.", "Error", JOptionPane.ERROR_MESSAGE);

        }

    }

    

    private void limpiarCampos() {

        areaVacunasAplicadas.setText("");

        areaVacunasPendientes.setText("");

        txtDesparasitacion.setText(""); // <-- CAMBIO

        txtUltimaVisita.setText("");    // <-- CAMBIO

        setFieldsEnabled(false);

        historialActual = null;

    }



    private void setFieldsEnabled(boolean enabled) {

        areaVacunasAplicadas.setEnabled(enabled);

        areaVacunasPendientes.setEnabled(enabled);

        txtDesparasitacion.setEnabled(enabled); // <-- CAMBIO

        txtUltimaVisita.setEnabled(enabled);    // <-- CAMBIO

        btnGuardar.setEnabled(enabled);
    }
}