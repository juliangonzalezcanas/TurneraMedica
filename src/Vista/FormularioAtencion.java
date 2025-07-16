package Vista;

import Entidades.Atencion;
import Entidades.Consultorio;
import Servicios.AtencionServicio;
import Servicios.ConsultorioServicio;
import Servicios.MedicoServicio;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;


public class FormularioAtencion  extends JPanel{
    
private JComboBox<Consultorio> comboConsultorios;
    private JTextField fechaDesde;
    private JTextField fechaHasta;
    private AtencionServicio atencionServicio;
    private ConsultorioServicio consultorioServicio;
     private MedicoServicio medicoServicio;
    private int idMedico;

    public FormularioAtencion(int idMedico) {
        this.idMedico = idMedico;
        this.atencionServicio = new AtencionServicio();
        this.consultorioServicio = new ConsultorioServicio();
        this.medicoServicio = new MedicoServicio();

        setLayout(new BorderLayout(10, 10));
        setBorder(new TitledBorder("Registrar Atención en Consultorio"));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        comboConsultorios = new JComboBox<>();
        cargarConsultorios();

        fechaDesde = new JTextField();
        fechaHasta = new JTextField();

        formPanel.add(new JLabel("Consultorio:"));
        formPanel.add(comboConsultorios);
        formPanel.add(new JLabel("Desde (yyyy-MM-dd):"));
        formPanel.add(fechaDesde);
        formPanel.add(new JLabel("Hasta (yyyy-MM-dd):"));
        formPanel.add(fechaHasta);

        JButton btnRegistrar = new JButton("Registrar Atención");
        btnRegistrar.addActionListener(e -> registrarAtencion());

        formPanel.add(new JLabel());
        formPanel.add(btnRegistrar);

        add(formPanel, BorderLayout.CENTER);
    }

    private void cargarConsultorios() {
        List<Consultorio> consultorios = consultorioServicio.leerTodos();
        for (Consultorio c : consultorios) {
            comboConsultorios.addItem(c);
        }
    }

    private void registrarAtencion() {
        try {
            Consultorio consultorio = (Consultorio) comboConsultorios.getSelectedItem();
            LocalDate desde = LocalDate.parse(fechaDesde.getText());
            LocalDate hasta = LocalDate.parse(fechaHasta.getText());

            List<Atencion> atenciones = atencionServicio.leerTodos();

            boolean conflicto = atenciones.stream().anyMatch(a ->
                a.getConsultorio().getId() == consultorio.getId() &&
                !(hasta.isBefore(a.getDesde()) || desde.isAfter(a.getHasta()))
            );

            if (conflicto) {
                JOptionPane.showMessageDialog(this, "Ese consultorio ya está asignado a otro médico en ese rango de fechas.", "Conflicto", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Atencion nueva = new Atencion(medicoServicio.leerPorId(idMedico), consultorioServicio.leer(consultorio.getId()), desde, hasta);
            atencionServicio.grabar(nueva);

            JOptionPane.showMessageDialog(this, "Atención registrada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            fechaDesde.setText("");
            fechaHasta.setText("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar atención: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}

