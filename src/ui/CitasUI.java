package ui;

import controladores.CitaController;
import modelos.Cita;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

public class CitasUI extends JFrame {

    private final CitaController citaController;
    private LocalDate fechaActual;
    private JLabel lblTituloFecha;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel panelVistas = new JPanel(cardLayout);
    private JPanel panelSemana, panelMes, panelAnio;

    private enum VistaActual { SEMANA, MES, ANIO }
    private VistaActual vistaActual = VistaActual.SEMANA;

    public CitasUI() {
        this.citaController = new CitaController();
        this.fechaActual = LocalDate.now();

        setTitle("Agenda de Citas - Días Vet");
        setSize(1300, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
        actualizarVista();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- PANEL SUPERIOR ---
        JPanel panelSuperior = new JPanel(new BorderLayout(20, 0));
        JPanel panelNavegacion = new JPanel();
        JButton btnAnterior = new JButton("<");
        JButton btnSiguiente = new JButton(">");
        panelNavegacion.add(btnAnterior);
        panelNavegacion.add(btnSiguiente);

        lblTituloFecha = new JLabel("", SwingConstants.CENTER);
        lblTituloFecha.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel panelSelectorVista = new JPanel();
        JButton btnVistaAnio = new JButton("Año");
        JButton btnVistaMes = new JButton("Mes");
        JButton btnVistaSemana = new JButton("Semana");
        panelSelectorVista.add(btnVistaAnio);
        panelSelectorVista.add(btnVistaMes);
        panelSelectorVista.add(btnVistaSemana);

        panelSuperior.add(panelNavegacion, BorderLayout.WEST);
        panelSuperior.add(lblTituloFecha, BorderLayout.CENTER);
        panelSuperior.add(panelSelectorVista, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);

        // --- PANEL CENTRAL CON VISTAS ---
        panelSemana = crearPanelSemana();
        panelMes = crearPanelMes();
        panelAnio = crearPanelAnio();
        panelVistas.add(panelSemana, "Semana");
        panelVistas.add(panelMes, "Mes");
        panelVistas.add(panelAnio, "Año");
        add(panelVistas, BorderLayout.CENTER);

        // --- PANEL DERECHO DEL FORMULARIO ---
        add(crearPanelFormulario(), BorderLayout.EAST);

        // --- LÓGICA DE NAVEGACIÓN ---
        btnAnterior.addActionListener(e -> navegar(-1));
        btnSiguiente.addActionListener(e -> navegar(1));
        btnVistaAnio.addActionListener(e -> cambiarVista(VistaActual.ANIO));
        btnVistaMes.addActionListener(e -> cambiarVista(VistaActual.MES));
        btnVistaSemana.addActionListener(e -> cambiarVista(VistaActual.SEMANA));
    }
    
    private void navegar(int direccion) {
        switch (vistaActual) {
            case SEMANA: fechaActual = fechaActual.plusWeeks(direccion); break;
            case MES: fechaActual = fechaActual.plusMonths(direccion); break;
            case ANIO: fechaActual = fechaActual.plusYears(direccion); break;
        }
        actualizarVista();
    }

    private void cambiarVista(VistaActual nuevaVista) {
        vistaActual = nuevaVista;
        fechaActual = LocalDate.now();
        actualizarVista();
    }

    private void actualizarVista() {
        List<Cita> citas = citaController.obtenerTodasLasCitas();
        switch (vistaActual) {
            case SEMANA:
                cardLayout.show(panelVistas, "Semana");
                actualizarVistaSemana(citas);
                break;
            case MES:
                cardLayout.show(panelVistas, "Mes");
                actualizarVistaMes(citas);
                break;
            case ANIO:
                cardLayout.show(panelVistas, "Año");
                actualizarVistaAnio(citas);
                break;
        }
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Agendar / Modificar Cita"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtFecha = new JTextField(15);
        txtFecha.setToolTipText("Usar formato YYYY-MM-DD");
        JComboBox<String> comboHora = new JComboBox<>();
        for (int i = 7; i <= 18; i++) {
            comboHora.addItem(String.format("%02d", i));
        }
        JComboBox<String> comboMinutos = new JComboBox<>(new String[]{"00", "30"});
        JTextField txtMotivo = new JTextField(15);
        JTextField txtDniCliente = new JTextField(15);
        JTextField txtIdMascota = new JTextField(15);
        JButton btnAgendar = new JButton("Agendar Cita");
        JButton btnEliminar = new JButton("Eliminar Cita (requiere ID)");
        JTextField txtIdCitaEliminar = new JTextField(5);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Fecha (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(txtFecha, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Hora:"), gbc);
        JPanel panelHora = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelHora.add(comboHora);
        panelHora.add(new JLabel(":"));
        panelHora.add(comboMinutos);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(panelHora, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("DNI del Cliente:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(txtDniCliente, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("ID de la Mascota:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(txtIdMascota, gbc);
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Motivo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; panel.add(txtMotivo, gbc);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(btnAgendar, gbc);
        gbc.gridy = 6; gbc.insets = new Insets(20, 5, 5, 5);
        panel.add(new JSeparator(), gbc);
        gbc.gridy = 7; gbc.gridwidth = 1; gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(new JLabel("ID de Cita a Eliminar:"), gbc);
        gbc.gridx = 1; gbc.gridy = 7;
        panel.add(txtIdCitaEliminar, gbc);
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnEliminar, gbc);

        btnAgendar.addActionListener(e -> {
            try {
                String fechaStr = txtFecha.getText();
                String horaStr = comboHora.getSelectedItem().toString();
                String minutoStr = comboMinutos.getSelectedItem().toString();
                String fechaCompletaStr = fechaStr + " " + horaStr + ":" + minutoStr;
                Cita nuevaCita = new Cita(0, fechaCompletaStr, txtMotivo.getText(), txtDniCliente.getText(), Integer.parseInt(txtIdMascota.getText()));
                if (citaController.registrarCita(nuevaCita)) {
                    JOptionPane.showMessageDialog(this, "Cita agendada con éxito.");
                    actualizarVista();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo agendar la cita.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en los datos ingresados.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEliminar.addActionListener(e -> {
            try {
                int idCita = Integer.parseInt(txtIdCitaEliminar.getText());
                int confirmacion = JOptionPane.showConfirmDialog(this, "¿Eliminar la cita con ID " + idCita + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirmacion == JOptionPane.YES_OPTION) {
                    if (citaController.eliminarCita(idCita)) {
                        JOptionPane.showMessageDialog(this, "Cita eliminada.");
                        actualizarVista();
                    } else {
                        JOptionPane.showMessageDialog(this, "No se pudo eliminar la cita.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID de cita inválido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel crearPanelSemana() { return new JPanel(new GridBagLayout()); }
    private JPanel crearPanelMes() { return new JPanel(new GridLayout(0, 7, 5, 5)); }
    private JPanel crearPanelAnio() { return new JPanel(new GridLayout(3, 4, 10, 10)); }

    private void actualizarVistaSemana(List<Cita> citas) {
        panelSemana.removeAll();
        LocalDate inicioSemana = fechaActual.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate finSemana = inicioSemana.plusDays(6);
        lblTituloFecha.setText("Semana del " + inicioSemana.format(DateTimeFormatter.ofPattern("dd 'de' MMMM", new Locale("es", "ES"))) + " al " + finSemana.format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"))));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0;
        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        Map<String, JTextArea> panelesHorarioSemana = new HashMap<>();

        for (int i = 0; i < dias.length; i++) {
            gbc.gridx = i; gbc.gridy = 0; gbc.weighty = 0.05;
            JLabel lblDia = new JLabel(dias[i], SwingConstants.CENTER);
            lblDia.setFont(new Font("Arial", Font.BOLD, 16));
            lblDia.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
            panelSemana.add(lblDia, gbc);
        }

        gbc.weighty = 1.0;
        for (int i = 0; i < dias.length; i++) {
            gbc.gridx = i; gbc.gridy = 1;
            JPanel panelDia = new JPanel();
            panelDia.setLayout(new BoxLayout(panelDia, BoxLayout.Y_AXIS));
            panelDia.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.GRAY));
            if (i < 6) {
                panelDia.add(new JScrollPane(createScheduleArea(panelesHorarioSemana, dias[i] + "_manana", "7am-12pm")));
                panelDia.add(new JScrollPane(createScheduleArea(panelesHorarioSemana, dias[i] + "_tarde", "3pm-7pm")));
            } else {
                panelDia.add(new JScrollPane(createScheduleArea(panelesHorarioSemana, dias[i] + "_domingo", "7am-9am")));
            }
            panelSemana.add(panelDia, gbc);
        }

        DateTimeFormatter formatterCita = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Cita cita : citas) {
            try {
                LocalDateTime fechaCita = LocalDateTime.parse(cita.getFechaHora(), formatterCita);
                if (!fechaCita.toLocalDate().isBefore(inicioSemana) && !fechaCita.toLocalDate().isAfter(finSemana)) {
                    DayOfWeek dia = fechaCita.getDayOfWeek(); int hora = fechaCita.getHour(); String key = "";
                    switch (dia) {
                        case MONDAY: key="Lunes"; break; case TUESDAY: key="Martes"; break; case WEDNESDAY: key="Miércoles"; break;
                        case THURSDAY: key="Jueves"; break; case FRIDAY: key="Viernes"; break; case SATURDAY: key="Sábado"; break;
                        case SUNDAY: key="Domingo"; break;
                    }
                    if (dia == DayOfWeek.SUNDAY) { if (hora >= 7 && hora < 9) key += "_domingo"; }
                    else { if (hora >= 7 && hora < 12) key += "_manana"; else if (hora >= 15 && hora < 19) key += "_tarde"; }
                    
                    JTextArea area = panelesHorarioSemana.get(key);
                    if (area != null) {
                        area.append(String.format("• ID:%d %s\n  Mascota:%d (%s)\n\n", cita.getId(), fechaCita.format(DateTimeFormatter.ofPattern("hh:mm a")), cita.getIdMascota(), cita.getMotivo()));
                    }
                }
            } catch (Exception e) {}
        }

        panelSemana.revalidate(); panelSemana.repaint();
    }

    private JTextArea createScheduleArea(Map<String, JTextArea> mapa, String key, String title) {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setLineWrap(true); textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createTitledBorder(title));
        textArea.setMargin(new Insets(5, 5, 5, 5));
        mapa.put(key, textArea);
        return textArea;
    }

    private void actualizarVistaMes(List<Cita> citas) {
        panelMes.removeAll();
        String[] dias = {"LUN", "MAR", "MIÉ", "JUE", "VIE", "SÁB", "DOM"};
        for (String dia : dias) {
            JLabel header = new JLabel(dia, SwingConstants.CENTER);
            header.setFont(new Font("Arial", Font.BOLD, 14));
            panelMes.add(header);
        }
        lblTituloFecha.setText(fechaActual.format(DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("es", "ES"))).toUpperCase());
        YearMonth anioMes = YearMonth.from(fechaActual);
        LocalDate primerDiaDelMes = fechaActual.withDayOfMonth(1);
        int diaDeLaSemana = primerDiaDelMes.getDayOfWeek().getValue();
        for (int i = 1; i < diaDeLaSemana; i++) { panelMes.add(new JLabel("")); }
        for (int i = 1; i <= anioMes.lengthOfMonth(); i++) {
            JPanel panelDia = new JPanel(new BorderLayout());
            panelDia.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            panelDia.add(new JLabel(" " + i), BorderLayout.NORTH);
            JTextArea areaCitas = new JTextArea(); areaCitas.setEditable(false);
            areaCitas.setFont(new Font("Monospaced", Font.PLAIN, 10));
            LocalDate diaActual = anioMes.atDay(i);
            for(Cita cita : citas) {
                if(LocalDateTime.parse(cita.getFechaHora(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).toLocalDate().equals(diaActual)) {
                    areaCitas.append("• " + cita.getMotivo() + "\n");
                }
            }
            panelDia.add(new JScrollPane(areaCitas), BorderLayout.CENTER);
            panelMes.add(panelDia);
        }
        panelMes.revalidate(); panelMes.repaint();
    }

    private void actualizarVistaAnio(List<Cita> citas) {
        panelAnio.removeAll();
        lblTituloFecha.setText(String.valueOf(fechaActual.getYear()));
        for (int i = 1; i <= 12; i++) {
            YearMonth anioMes = YearMonth.of(fechaActual.getYear(), i);
            JPanel panelMiniMes = new JPanel(new BorderLayout());
            String nombreMes = anioMes.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
            panelMiniMes.setBorder(BorderFactory.createTitledBorder(nombreMes.toUpperCase()));
            long contadorCitas = citas.stream().filter(c -> {
                try {
                    return YearMonth.from(LocalDateTime.parse(c.getFechaHora(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).equals(anioMes);
                } catch (Exception e) { return false; }
            }).count();
            JLabel lblContador = new JLabel(contadorCitas + " citas", SwingConstants.CENTER);
            lblContador.setFont(new Font("Arial", Font.BOLD, 24));
            panelMiniMes.add(lblContador, BorderLayout.CENTER);
            panelAnio.add(panelMiniMes);
        }
        panelAnio.revalidate(); panelAnio.repaint();
    }
}