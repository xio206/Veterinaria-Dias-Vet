package ui;

import controladores.ClienteController;
import controladores.HistorialAccionesController;
import modelos.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import utilidades.Validador;

public class ClienteMascotaUI extends JFrame {

    // --- Controladores ---
    private final ClienteController clienteController;

    // --- Componentes de la UI ---
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    private JTextField txtDni;
    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTextField txtDireccion;
    private JButton btnGuardar;
    private JButton btnLimpiar;
    
    // Variable para guardar el DNI del cliente seleccionado para la actualización
    private String dniSeleccionado;

    public ClienteMascotaUI(HistorialAccionesController historialController) {
        this.clienteController = new ClienteController(); // Esta ventana gestiona su propio ClienteController

        setTitle("Gestión de Clientes y Mascotas");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        inicializarComponentes();
        cargarDatosEnTabla();
    }

    public ClienteMascotaUI() {
        this.clienteController = new ClienteController(); 

        setTitle("Gestión de Clientes y Mascotas");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Llamamos a los métodos de inicialización desde aquí
        inicializarComponentes();
        cargarDatosEnTabla();
    }

    private void inicializarComponentes() {
        // --- Panel Principal (Izquierda: Tabla, Derecha: Formulario) ---
        setLayout(new BorderLayout(10, 10));

        // --- 1. Tabla de Clientes ---
        modeloTabla = new DefaultTableModel(new String[]{"DNI", "Nombre", "Teléfono", "Dirección"}, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }};
        tablaClientes = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaClientes);
        add(scrollPane, BorderLayout.CENTER);

        // --- 2. Formulario de Edición ---
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtDni = new JTextField(20);
        txtNombre = new JTextField(20);
        txtTelefono = new JTextField(20);
        txtDireccion = new JTextField(20);
        
        gbc.gridx = 0; gbc.gridy = 0; panelFormulario.add(new JLabel("DNI:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panelFormulario.add(txtDni, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panelFormulario.add(new JLabel("Nombre Completo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panelFormulario.add(txtNombre, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panelFormulario.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panelFormulario.add(txtTelefono, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panelFormulario.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; panelFormulario.add(txtDireccion, gbc);

        // --- 3. Botones ---
        btnGuardar = new JButton("Guardar Cambios");
        btnLimpiar = new JButton("Limpiar Formulario");
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panelFormulario.add(panelBotones, gbc);
        
        add(panelFormulario, BorderLayout.EAST);
        
        // --- 4. Lógica de Eventos ---

        // Evento para cargar datos en el formulario al seleccionar una fila de la tabla
        tablaClientes.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && tablaClientes.getSelectedRow() != -1) {
                int filaSeleccionada = tablaClientes.getSelectedRow();
                dniSeleccionado = modeloTabla.getValueAt(filaSeleccionada, 0).toString();
                
                txtDni.setText(dniSeleccionado);
                txtDni.setEnabled(false); // No se debe poder editar el DNI
                txtNombre.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
                txtTelefono.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
                txtDireccion.setText(modeloTabla.getValueAt(filaSeleccionada, 3).toString());
            }
        });
        
        // AQUÍ ESTÁ LA ACCIÓN DE "ACTUALIZAR" USANDO EL SISTEMA UNDO/REDO
        btnGuardar.addActionListener(e -> {
            if (dniSeleccionado == null || dniSeleccionado.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un cliente de la tabla para actualizar.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // RECOLECTAR TODOS LOS DATOS DEL FORMULARIO
            String dni = txtDni.getText();
            String nuevoNombre = txtNombre.getText();
            String nuevoTelefono = txtTelefono.getText();
            String nuevaDireccion = txtDireccion.getText();

            // 2. Validar que los campos no estén vacíos (excepto la dirección que puede ser opcional)
            if (nuevoNombre.trim().isEmpty() || nuevoTelefono.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre y el teléfono no pueden estar vacíos.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar el formato del teléfono
            if (!Validador.validarTelefono(nuevoTelefono)) {
                JOptionPane.showMessageDialog(this, Validador.getMensajeErrorTelefono(), "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 3. CREAR UN OBJETO CLIENTE CON LOS DATOS ACTUALIZADOS
            Cliente clienteActualizado = new Cliente(dni, nuevoNombre, nuevoTelefono, nuevaDireccion);
            
            // 4. LLAMAR DIRECTAMENTE AL MÉTODO DE ACTUALIZACIÓN DEL CONTROLADOR
            if (clienteController.actualizarCliente(clienteActualizado)) {
                JOptionPane.showMessageDialog(this, "Cliente actualizado con éxito.");
                // Refrescamos la tabla para ver los cambios
                cargarDatosEnTabla();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        aplicarFiltrosValidacion();
    }

    private void cargarDatosEnTabla() {
        // Limpiar tabla antes de cargar
        modeloTabla.setRowCount(0); 
        
        List<Cliente> clientes = clienteController.obtenerTodosLosClientes();
        for (Cliente cliente : clientes) {
            modeloTabla.addRow(new Object[]{
                cliente.getDni(),
                cliente.getNombre(),
                cliente.getTelefono(),
                cliente.getDireccion()
            });
        }
    }
    
    private void limpiarFormulario() {
        txtDni.setText("");
        txtDni.setEnabled(true);
        txtNombre.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        dniSeleccionado = null;
        tablaClientes.clearSelection();
    }
    
    private void aplicarFiltrosValidacion() {
        // Filtro para DNI (máximo 8 dígitos)
        ((AbstractDocument) txtDni.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                    throws BadLocationException {
                String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
                if (newText.matches("\\d{0,8}")) {
                    super.replace(fb, offset, length, text, attrs);}}

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) 
                    throws BadLocationException {
                String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
                if (newText.matches("\\d{0,8}")) {
                    super.insertString(fb, offset, string, attr);}}
            });

        // Filtro para teléfono (máximo 9 dígitos)
        ((AbstractDocument) txtTelefono.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                    throws BadLocationException {
                String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
                if (newText.matches("\\d{0,9}")) {
                    super.replace(fb, offset, length, text, attrs);}}

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) 
                    throws BadLocationException {
                String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
                if (newText.matches("\\d{0,9}")) {
                    super.insertString(fb, offset, string, attr);}}
            });
        }
}