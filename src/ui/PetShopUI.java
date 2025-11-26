package ui;

import controladores.ProductoController;
import modelos.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PetShopUI extends JFrame {

    private ProductoController productoController;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;

    public PetShopUI() {
        this.productoController = new ProductoController();

        setTitle("Pet Shop - Productos Disponibles");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Para que solo se cierre esta ventana
        setLocationRelativeTo(null);

        // --- Panel Principal ---
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- Título ---
        JLabel lblTitulo = new JLabel("Catálogo de Productos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(lblTitulo, BorderLayout.NORTH);

        // --- Tabla para mostrar los productos ---
        modeloTabla = new DefaultTableModel(new String[]{"Producto", "Descripción", "Precio"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacemos la tabla de solo lectura
            }
        };
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setRowHeight(30); // Aumentamos la altura de la fila para mejor legibilidad
        tablaProductos.setFont(new Font("Arial", Font.PLAIN, 16));
        tablaProductos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));

        // Ajustar el ancho de las columnas
        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(200);
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(400);
        tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(100);

        panel.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);
        
        add(panel);
        
        cargarProductos();
    }

    private void cargarProductos() {
        modeloTabla.setRowCount(0); // Limpiar la tabla antes de cargar
        List<Producto> productos = productoController.obtenerTodosLosProductos();
        
        for (Producto producto : productos) {
            // Solo mostramos productos que tengan stock
            if (producto.getStock() > 0) {
                modeloTabla.addRow(new Object[]{
                    producto.getNombre(),
                    producto.getDescripcion(),
                    String.format("S/ %.2f", producto.getPrecio()) // Formateamos el precio
                });
            }
        }
    }
}