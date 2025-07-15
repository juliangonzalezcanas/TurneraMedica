package Vista;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Entidades.Paciente;
import Entidades.Turno;
import Servicios.PacienteServicio;
import Servicios.TurnoServicio;
import Servicios.Exceptions.EliminandoPacienteException;
import Servicios.Exceptions.ModificandoPacienteException;
import Vista.Exceptions.*;

public class FormularioPaciente extends JPanel {

    private JTextField nombre, apellido, dni, email, obra_social, passwd;
    private JTable tablaPacientes;
    private JTextField fechaTurno;

    private PacienteServicio pacienteServicio;
    private TurnoServicio turnoServicio;

    private int idPaciente;

    public FormularioPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
        this.setLayout(new BorderLayout());

        pacienteServicio = new PacienteServicio();
        turnoServicio = new TurnoServicio();

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Datos del Paciente"));

        nombre = new JTextField(); apellido = new JTextField();
        dni = new JTextField(); email = new JTextField();
        obra_social = new JTextField(); passwd = new JTextField();

        formPanel.add(new JLabel("Nombre:")); formPanel.add(nombre);
        formPanel.add(new JLabel("Apellido:")); formPanel.add(apellido);
        formPanel.add(new JLabel("DNI:")); formPanel.add(dni);
        formPanel.add(new JLabel("Email:")); formPanel.add(email);
        formPanel.add(new JLabel("Obra Social:")); formPanel.add(obra_social);
        formPanel.add(new JLabel("Contraseña:")); formPanel.add(passwd);

        JButton btnLlenarCampos = new JButton("Llenar campos");
        btnLlenarCampos.addActionListener(e -> llenarCampos());
        formPanel.add(btnLlenarCampos);

        JButton btnModificar = new JButton("Modificar");
        btnModificar.addActionListener(e -> modificarPaciente());
        formPanel.add(btnModificar);

        this.add(formPanel, BorderLayout.NORTH);

        // Botones secundarios
        JPanel accionesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnEliminar = new JButton("Eliminar Paciente");
        btnEliminar.addActionListener(e -> eliminarPaciente());
        accionesPanel.add(btnEliminar);

        JToggleButton btnPersistencia = new JToggleButton("BDD/Archivo");
        btnPersistencia.addActionListener(e -> togglePersistencia(btnPersistencia.isSelected()));
        accionesPanel.add(btnPersistencia);

        this.add(accionesPanel, BorderLayout.CENTER);

        // Buscar turnos
        JPanel turnoPanel = new JPanel();
        turnoPanel.setBorder(BorderFactory.createTitledBorder("Turnos"));
        fechaTurno = new JTextField(10);
        JButton btnBuscarTurnos = new JButton("Buscar Turnos");
        btnBuscarTurnos.addActionListener(e -> buscarTurnos());
        turnoPanel.add(new JLabel("Fecha (yyyy-MM-dd):"));
        turnoPanel.add(fechaTurno);
        turnoPanel.add(btnBuscarTurnos);

        this.add(turnoPanel, BorderLayout.SOUTH);

        // Tabla de pacientes
        tablaPacientes = new JTable();
        JScrollPane scroll = new JScrollPane(tablaPacientes);
        scroll.setBorder(BorderFactory.createTitledBorder("Todos los Pacientes"));
        this.add(scroll, BorderLayout.EAST);

        actualizarTabla();
    }

    private void llenarCampos() {
        try {
            Paciente paciente = pacienteServicio.leer(idPaciente);
            if (paciente != null) {
                nombre.setText(paciente.getNombre());
                apellido.setText(paciente.getApellido());
                dni.setText(paciente.getDni().toString());
                email.setText(paciente.getEmail());
                obra_social.setText(paciente.getObraSocial());
            } else {
                mostrarError("Paciente no encontrado");
            }
        } catch (IOException | ClassNotFoundException ex) {
            mostrarError("Error al buscar paciente: " + ex.getMessage());
        }
    }

    private void modificarPaciente() {
        try {
            validarCampos();
            Paciente p = new Paciente(idPaciente, nombre.getText(), apellido.getText(),
                    Integer.parseInt(dni.getText()), email.getText(), obra_social.getText(), passwd.getText());
            pacienteServicio.modificar(p);
            mostrarInfo("Paciente modificado correctamente");
            actualizarTabla();
        } catch (Exception e) {
            mostrarError("Error al modificar el paciente: " + e.getMessage());
        }
    }

    private void eliminarPaciente() {
        try {
            pacienteServicio.eliminar(idPaciente);
            mostrarInfo("Paciente eliminado correctamente");
            actualizarTabla();
        } catch (EliminandoPacienteException e) {
            mostrarError("Error al eliminar paciente");
        }
    }

    private void buscarTurnos() {
        try {
            List<Turno> turnos = turnoServicio.buscarPorPaciente(idPaciente);
            StringBuilder sb = new StringBuilder("\nTurnos:\n");
            for (Turno t : turnos) {
                sb.append("ID: ").append(t.getId())
                        .append(", Fecha: ").append(t.getFecha())
                        .append(", Médico: ").append(t.getMedico().getNombre())
                        .append("\n");
            }
            mostrarInfo(sb.toString());
        } catch (Exception ex) {
            mostrarError("Error al buscar turnos: " + ex.getMessage());
        }
    }

    private void actualizarTabla() {
        List<Paciente> lista = pacienteServicio.leerTodos();
        String[] columnas = {"ID", "Nombre", "Apellido", "DNI", "Email", "Obra Social"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        for (Paciente p : lista) {
            modelo.addRow(new Object[]{
                    p.getId(), p.getNombre(), p.getApellido(),
                    p.getDni(), p.getEmail(), p.getObraSocial()
            });
        }
        tablaPacientes.setModel(modelo);
    }

    private void togglePersistencia(boolean bdd) {
        if (bdd) {
            pacienteServicio.setPersistencia(new Persistencia.DB.PacienteDBDao());
            mostrarInfo("Persistencia cambiada a Base de Datos");
        } else {
            pacienteServicio.setPersistencia(new Persistencia.Archivo.ArchivoPacienteDao());
            mostrarInfo("Persistencia cambiada a Archivo");
        }
    }

    private void validarCampos() throws NombreVacioException, ApellidoVacioException,
            DniVacioException, EmailVacioException, ObraSocialVaciaException, IdVacioException {

        if (nombre.getText().isEmpty()) throw new NombreVacioException();
        if (apellido.getText().isEmpty()) throw new ApellidoVacioException();
        if (dni.getText().isEmpty()) throw new DniVacioException();
        if (email.getText().isEmpty()) throw new EmailVacioException();
        if (obra_social.getText().isEmpty()) throw new ObraSocialVaciaException();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}
