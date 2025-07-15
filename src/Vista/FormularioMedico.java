// Versión mejorada de tu formulario de médicos con mejor estética y organización visual

package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import Entidades.Medico;
import Entidades.Turno;
import Servicios.MedicoServicio;
import Servicios.TurnoServicio;
import Vista.Exceptions.*;

public class FormularioMedico extends JPanel {
    private JTextField nombre, apellido, dni, email, obraSocial, precioConsulta, especialidad, passwd;
    private JTextField fechaInicio, fechaFin, fechaBusqueda;
    private JTable tablaMedicos;
    private int idMedico;

    private MedicoServicio medicoServicio = new MedicoServicio();
    private TurnoServicio turnoServicio = new TurnoServicio();

    public FormularioMedico(int idMedico) {
        this.idMedico = idMedico;

        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(crearPanelBotones(), BorderLayout.CENTER);
        add(crearPanelTabla(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(5, 4, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Médico"));

        nombre = new JTextField(); apellido = new JTextField(); dni = new JTextField();
        email = new JTextField(); obraSocial = new JTextField(); precioConsulta = new JTextField();
        especialidad = new JTextField(); passwd = new JTextField();

        panel.add(new JLabel("Nombre:")); panel.add(nombre);
        panel.add(new JLabel("Apellido:")); panel.add(apellido);
        panel.add(new JLabel("DNI:")); panel.add(dni);
        panel.add(new JLabel("Email:")); panel.add(email);
        panel.add(new JLabel("Obra Social:")); panel.add(obraSocial);
        panel.add(new JLabel("Precio Consulta:")); panel.add(precioConsulta);
        panel.add(new JLabel("Especialidad:")); panel.add(especialidad);
        panel.add(new JLabel("Contraseña:")); panel.add(passwd);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel filaBotones = new JPanel(new FlowLayout());
        JButton btnLlenar = new JButton("Llenar campos");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");

        filaBotones.add(btnLlenar);
        filaBotones.add(btnModificar);
        filaBotones.add(btnEliminar);

        btnLlenar.addActionListener(e -> llenarCampos());
        btnModificar.addActionListener(e -> modificarMedico());
        btnEliminar.addActionListener(e -> eliminarMedico());

        panel.add(filaBotones);

        // Panel Ganancia
        JPanel gananciaPanel = new JPanel(new FlowLayout());
        fechaInicio = new JTextField(8);
        fechaFin = new JTextField(8);
        JButton btnGanancia = new JButton("Calcular Ganancia");
        gananciaPanel.add(new JLabel("Desde:")); gananciaPanel.add(fechaInicio);
        gananciaPanel.add(new JLabel("Hasta:")); gananciaPanel.add(fechaFin);
        gananciaPanel.add(btnGanancia);
        btnGanancia.addActionListener(e -> calcularGanancia());
        panel.add(gananciaPanel);

        // Panel Turno
        JPanel turnoPanel = new JPanel(new FlowLayout());
        fechaBusqueda = new JTextField(8);
        JButton btnBuscarTurno = new JButton("Buscar Turnos");
        turnoPanel.add(new JLabel("Fecha:")); turnoPanel.add(fechaBusqueda);
        turnoPanel.add(btnBuscarTurno);
        btnBuscarTurno.addActionListener(e -> buscarTurnosPorFecha());
        panel.add(turnoPanel);

        return panel;
    }

    private JScrollPane crearPanelTabla() {
        tablaMedicos = new JTable();
        cargarTabla();
        return new JScrollPane(tablaMedicos);
    }

    private void cargarTabla() {
        List<Medico> medicos = medicoServicio.leerTodos();
        String[] columnas = {"ID", "Nombre", "Apellido", "DNI", "Email", "Obra Social", "Precio", "Especialidad"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        for (Medico m : medicos) {
            modelo.addRow(new Object[]{m.getId(), m.getNombre(), m.getApellido(), m.getDni(), m.getEmail(), m.getObraSocial(), m.getPrecioConsulta(), m.getEspecialidad()});
        }
        tablaMedicos.setModel(modelo);
    }

    private void llenarCampos() {
        try {
            Medico m = medicoServicio.leerPorId(idMedico);
            if (m != null) {
                nombre.setText(m.getNombre()); 
				apellido.setText(m.getApellido());
				dni.setText(m.getDni().toString());
                email.setText(m.getEmail()); 
				obraSocial.setText(m.getObraSocial()); 
				precioConsulta.setText(m.getPrecioConsulta().toString());
                especialidad.setText(m.getEspecialidad());
            }
        } catch (Exception e) {
            mostrarError("Error al cargar datos: " + e.getMessage());
        }
    }

    private void modificarMedico() {
        try {
            validarCampos();
            Medico m = new Medico(idMedico, nombre.getText(), apellido.getText(), Integer.parseInt(dni.getText()),
                    email.getText(), obraSocial.getText(), Float.parseFloat(precioConsulta.getText()),
                    especialidad.getText(), passwd.getText());
            medicoServicio.modificar(m);
            mostrarInfo("Médico modificado con éxito.");
            cargarTabla();
        } catch (Exception e) {
            mostrarError("Error al modificar: " + e.getMessage());
        }
    }

    private void eliminarMedico() {
        try {
            medicoServicio.eliminar(idMedico);
            mostrarInfo("Médico eliminado.");
            cargarTabla();
        } catch (Exception e) {
            mostrarError("Error al eliminar: " + e.getMessage());
        }
    }

    private void calcularGanancia() {
        try {
            List<Turno> turnos = turnoServicio.buscarPorMedico(idMedico);
            Float ganancia = medicoServicio.calcularGanancia(
                    LocalDateTime.of(LocalDate.parse(fechaInicio.getText()), LocalTime.MIN),
                    LocalDateTime.of(LocalDate.parse(fechaFin.getText()), LocalTime.MIN),
                    turnos);
            mostrarInfo("Ganancia: $" + ganancia);
        } catch (Exception e) {
            mostrarError("Error en cálculo: " + e.getMessage());
        }
    }

    private void buscarTurnosPorFecha() {
        try {
            List<Turno> turnos = medicoServicio.buscarTurnosPorFecha(
                    turnoServicio.buscarPorMedico(idMedico),
                    LocalDate.parse(fechaBusqueda.getText()));
            for (Turno t : turnos) {
                mostrarInfo("Turno: " + t.getId() + " - Paciente: " + t.getPaciente().getNombre());
            }
        } catch (Exception e) {
            mostrarError("Error buscando turnos: " + e.getMessage());
        }
    }

    private void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void validarCampos() throws Exception {
        if (nombre.getText().isEmpty()) throw new NombreVacioException();
        if (apellido.getText().isEmpty()) throw new ApellidoVacioException();
        if (dni.getText().isEmpty()) throw new DniVacioException();
        if (email.getText().isEmpty()) throw new EmailVacioException();
        if (obraSocial.getText().isEmpty()) throw new ObraSocialVaciaException();
        if (precioConsulta.getText().isEmpty()) throw new PrecioConsultaVacioException();
        if (especialidad.getText().isEmpty()) throw new EspecialidadVaciaException();
    }
}
