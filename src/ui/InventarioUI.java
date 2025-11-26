package ui;

import controladores.ProductoController;
import controladores.HistorialAccionesController;
import modelos.Producto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Ventana para la gestión del inventario del Pet Shop.
 * Permitirá registrar, actualizar y eliminar productos.
 */
public class InventarioUI extends JFrame {
    
    private final ProductoController productoController;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JTextField txtCodigo, txtNombre, txtDescripcion, txtPrecio, txtStock;
    private JButton btnRegistrar, btnGuardar, btnEliminar, btnLimpiar;
    private String codigoSeleccionado;
    
    public InventarioUI() {
        this.productoController = new ProductoController();
        
        setTitle("Inventario Pet Shop - Días Vet");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        inicializarComponentes();
        cargarProductosEnTabla();
    }
        
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        // Tabla de Productos
        modeloTabla = new DefaultTableModel(new String[]{"Código", "Nombre", "Precio", "Stock"}, 0);
        tablaProductos = new JTable(modeloTabla);
        add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        // --- Formulario de Producto (Panel Derecho) ---
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            BorderFactory.createTitledBorder("Datos del Producto")
        ));

        // GridBagConstraints para controlar la posición y el tamaño de los componentes
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaciado externo para cada componente
        gbc.fill = GridBagConstraints.HORIZONTAL; // Hace que los componentes se expandan horizontalmente

        // --- Campos del Formulario ---
        txtCodigo = new JTextField(15);
        txtNombre = new JTextField(15);
        txtDescripcion = new JTextField(15);
        txtPrecio = new JTextField(15);
        txtStock = new JTextField(15);
        
        // --- Fila 0: Código ---

        gbc.gridx = 0; gbc.gridy = 0; panelFormulario.add(new JLabel("Código (SKU):"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panelFormulario.add(txtCodigo, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panelFormulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panelFormulario.add(txtNombre, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panelFormulario.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panelFormulario.add(txtDescripcion, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panelFormulario.add(new JLabel("Precio (S/):"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; panelFormulario.add(txtPrecio, gbc);
        gbc.gridx = 0; gbc.gridy = 4; panelFormulario.add(new JLabel("Stock:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; panelFormulario.add(txtStock, gbc);

        // --- Fila 5: Panel de Botones ---
        // Creamos un panel separado para los botones para que se agrupen bien
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnRegistrar = new JButton("Registrar");
        btnGuardar = new JButton("Guardar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER; gbc.insets = new Insets(15, 5, 5, 5);
        panelFormulario.add(panelBotones, gbc);

        // Añadimos un "peso" vertical a una celda vacía para empujar todo hacia arriba
        gbc.gridy = 6;
        gbc.weighty = 1.0;
        panelFormulario.add(new JLabel(""), gbc);

        add(panelFormulario, BorderLayout.EAST);

        // --- Lógica de Eventos (ACTION LISTENERS) ---
        // Evento para cargar datos en el formulario al hacer clic en la tabla
        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaProductos.getSelectedRow() != -1) {
                int fila = tablaProductos.getSelectedRow();
                codigoSeleccionado = modeloTabla.getValueAt(fila, 0).toString();
                
                // Buscamos el producto completo para obtener todos sus datos (incluida la descripción)
                Producto p = productoController.buscarProductoPorCodigo(codigoSeleccionado);
                if (p != null) {
                    txtCodigo.setText(p.getCodigo());
                    txtCodigo.setEnabled(false); // El código no se debe editar
                    txtNombre.setText(p.getNombre());
                    txtDescripcion.setText(p.getDescripcion());
                    txtPrecio.setText(String.valueOf(p.getPrecio()));
                    txtStock.setText(String.valueOf(p.getStock()));
                }
            }
        });

        // --- Lógica del botón Registrar ---
        btnRegistrar.addActionListener(e -> {
            try {
                String codigo = txtCodigo.getText();
                String nombre = txtNombre.getText();
                String desc = txtDescripcion.getText();
                double precio = Double.parseDouble(txtPrecio.getText());
                int stock = Integer.parseInt(txtStock.getText());

                // Se reemplazó el comentario con la lógica real
                Producto nuevoProducto = new Producto(codigo, nombre, desc, precio, stock);

                if (productoController.registrarProducto(nuevoProducto)) {
                    JOptionPane.showMessageDialog(this, "Producto registrado con éxito.");
                    cargarProductosEnTabla();
                    limpiarFormulario();
                } else {
                    JOptionPane.showMessageDialog(this, "Error: El código del producto ya podría existir.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un número válido para Precio y Stock.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- Lógica del botón Guardar Cambios ---
        btnGuardar.addActionListener(e -> {
            if (codigoSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla para actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                String nombre = txtNombre.getText();
                String desc = txtDescripcion.getText();
                double precio = Double.parseDouble(txtPrecio.getText());
                int stock = Integer.parseInt(txtStock.getText());
                
                Producto productoActualizado = new Producto(codigoSeleccionado, nombre, desc, precio, stock);

                if (productoController.actualizarProducto(productoActualizado)) {
                    JOptionPane.showMessageDialog(this, "Producto actualizado con éxito.");
                    cargarProductosEnTabla();
                    limpiarFormulario();
                } else {
                     JOptionPane.showMessageDialog(this, "No se pudo actualizar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                 JOptionPane.showMessageDialog(this, "Por favor, ingrese un número válido para Precio y Stock.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- Lógica del botón Eliminar ---
        btnEliminar.addActionListener(e -> {
            if (codigoSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este producto?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                if(productoController.eliminarProducto(codigoSeleccionado)) {
                    JOptionPane.showMessageDialog(this, "Producto eliminado con éxito.");
                    cargarProductosEnTabla();
                    limpiarFormulario();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnLimpiar.addActionListener(e -> limpiarFormulario());
    }

    private void cargarProductosEnTabla() {
        modeloTabla.setRowCount(0); // Limpia la tabla
        List<Producto> productos = productoController.obtenerTodosLosProductos();
        for (Producto p : productos) {
            modeloTabla.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getPrecio(), p.getStock()});
        }
    }
    
    private void limpiarFormulario() {
        txtCodigo.setText("");
        txtCodigo.setEnabled(true);
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        codigoSeleccionado = null;
        tablaProductos.clearSelection();
    }
}