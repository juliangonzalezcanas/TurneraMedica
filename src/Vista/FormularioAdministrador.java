package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;

import Entidades.Consultorio;
import Entidades.Turno;
import Servicios.ConsultorioServicio;
import Servicios.TurnoServicio;
import Vista.Exceptions.FechaVaciaException;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class FormularioAdministrador extends JFrame {

    private JTextField nombre, direccion;
    private JComboBox<Consultorio> comboConsultorios;

    private JTextField fechaDesde, fechaHasta;
    private JTable tablaRecaudacion;

    private ConsultorioServicio consultorioServicio;
    private TurnoServicio turnoServicio;

    public FormularioAdministrador() {
        consultorioServicio = new ConsultorioServicio();
        turnoServicio = new TurnoServicio();

        setLayout(new BorderLayout(10, 10));

       
        JPanel panelCentral = new JPanel(new GridLayout(2, 1, 10, 10));
        panelCentral.add(crearPanelConsultorios());
        panelCentral.add(crearPanelRecaudacion());

        add(panelCentral, BorderLayout.CENTER);
    }

    private JPanel crearPanelConsultorios() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 8, 8));
        panel.setBorder(new TitledBorder("Gestión de Consultorios"));

        comboConsultorios = new JComboBox<>();
        nombre = new JTextField();
        direccion = new JTextField();

        cargarConsultorios();

        panel.add(new JLabel("Consultorios existentes:"));
        panel.add(comboConsultorios);

        panel.add(new JLabel("Nombre:"));
        panel.add(nombre);

        panel.add(new JLabel("Dirección:"));
        panel.add(direccion);

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(e -> {
            if(nombre.getText().isEmpty() || direccion.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe completar todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Consultorio nuevo = new Consultorio(nombre.getText(), direccion.getText());
                consultorioServicio.grabar(nuevo);
                cargarConsultorios();
            }
            
        });

        JButton btnModificar = new JButton("Modificar");
        btnModificar.addActionListener(e -> {
            Consultorio seleccionado = (Consultorio) comboConsultorios.getSelectedItem();
            if (seleccionado != null && !nombre.getText().isEmpty() && !direccion.getText().isEmpty()) {
                seleccionado.setNombre(nombre.getText());
                seleccionado.setDireccion(direccion.getText());
                consultorioServicio.modificar(seleccionado);
                cargarConsultorios();
            } else {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un consultorio y completar los campos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> {
            Consultorio seleccionado = (Consultorio) comboConsultorios.getSelectedItem();
            if (seleccionado != null) {
                consultorioServicio.eliminar(seleccionado.getId());
                cargarConsultorios();
            }
        });

        panel.add(btnAgregar);
        panel.add(btnModificar);
        panel.add(btnEliminar);

        return panel;
    }

    private JPanel crearPanelRecaudacion() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(new TitledBorder("Reporte de Recaudación por Médico"));

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        
        JPanel panelFechas = new JPanel(new GridLayout(2, 2, 5, 5));
        fechaDesde = new JTextField();
        fechaHasta = new JTextField();

        panelFechas.add(new JLabel("Desde (yyyy-MM-dd):"));
        panelFechas.add(fechaDesde);
        panelFechas.add(new JLabel("Hasta (yyyy-MM-dd):"));
        panelFechas.add(fechaHasta);

        JButton btnGenerar = new JButton("Generar Reporte");

        btnGenerar.addActionListener(e -> {
            try {

                if(fechaDesde.getText().isEmpty() || fechaHasta.getText().isEmpty()) {
                    throw new FechaVaciaException("Las fechas no pueden estar vacías");
                }

                LocalDate desde = LocalDate.parse(fechaDesde.getText());
                LocalDate hasta = LocalDate.parse(fechaHasta.getText());

                List<Turno> turnos = turnoServicio.calcularRecaudaciones(desde, hasta);

                Float total = 0f;

                DefaultTableModel model = new DefaultTableModel(new Object[]{"Médico", "Recaudación", "Fecha"}, 0);
                for (Turno t : turnos) {
                    model.addRow(new Object[]{t.getMedico().getNombre(), t.getPrecioConsulta(), t.getFecha()});
                    total += t.getPrecioConsulta();
                }
                tablaRecaudacion.setModel(model);
                JOptionPane.showMessageDialog(this, "Total Recaudado: " + total, "Recaudación", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panelSuperior.add(panelFechas);
        panelSuperior.add(Box.createVerticalStrut(5)); //espacio
        panelSuperior.add(btnGenerar);

        panel.add(panelSuperior, BorderLayout.NORTH);


        tablaRecaudacion = new JTable();
        JScrollPane scroll = new JScrollPane(tablaRecaudacion);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }



    private void cargarConsultorios() {
        comboConsultorios.removeAllItems();
        List<Consultorio> lista = consultorioServicio.leerTodos();
        for (Consultorio c : lista) comboConsultorios.addItem(c);
    }
}
