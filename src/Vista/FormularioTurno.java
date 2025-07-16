package Vista;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import Entidades.Atencion;
import Entidades.Medico;
import Entidades.Paciente;
import Entidades.Turno;
import Servicios.AtencionServicio;
import Servicios.MedicoServicio;
import Servicios.PacienteServicio;
import Servicios.TurnoServicio;
import Vista.Exceptions.MedicoVacioException;

public class FormularioTurno extends JPanel {
    private JTextField fecha;
    private JComboBox<Object> comboEntidad;
    private JComboBox<String> comboHorarios;
    private JComboBox<Turno> comboTurnos;

    private int idUsuario;
    private boolean esPaciente;

    private TurnoServicio turnoServicio;
    private MedicoServicio medicoServicio;
    private PacienteServicio pacienteServicio;
    private AtencionServicio atencionServicio;


    public FormularioTurno(boolean esPaciente, int idUsuario) {
        this.esPaciente = esPaciente;
        this.idUsuario = idUsuario;

        setLayout(new BorderLayout(10, 10));

        turnoServicio = new TurnoServicio();
        medicoServicio = new MedicoServicio();
        pacienteServicio = new PacienteServicio();
        atencionServicio = new AtencionServicio();

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        crearBotonSwitch(topPanel);
        add(topPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(new TitledBorder("Agregar Turno"));

        fecha = new JTextField();
        comboEntidad = new JComboBox<>();
        comboHorarios = new JComboBox<>();

        formPanel.add(new JLabel(esPaciente ? "Médico:" : "Paciente:"));
        formPanel.add(comboEntidad);
        formPanel.add(new JLabel("Fecha (yyyy-MM-dd):"));
        formPanel.add(fecha);

        JButton buscarHorarios = new JButton("Buscar Horarios");
        buscarHorarios.addActionListener(e -> {
            try {
                cargarHorarios();
            } catch (MedicoVacioException ex) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un médico", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar los horarios: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(new JLabel());
        formPanel.add(buscarHorarios);

        formPanel.add(new JLabel("Horarios Disponibles:"));
        formPanel.add(comboHorarios);

        add(formPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        crearBotonAceptar(buttonPanel);
        crearPanelEliminarTurno();
        add(buttonPanel, BorderLayout.CENTER);

        cargarEntidades();
    }

    private void crearPanelEliminarTurno() {
        JPanel panel = new JPanel();
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Eliminar Turno"));
        panel.setLayout(new GridLayout(2, 2));

        comboTurnos = new JComboBox<>();
        cargarTurnosUsuario();

        JButton btnEliminar = new JButton("Eliminar Turno");

        btnEliminar.addActionListener(e -> {
            Turno turnoSeleccionado = (Turno) comboTurnos.getSelectedItem();
            if (turnoSeleccionado != null) {
                try {
                    turnoServicio.eliminar(turnoSeleccionado.getId());
                    JOptionPane.showMessageDialog(this, "Turno eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    comboTurnos.removeItem(turnoSeleccionado); 
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar turno: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(new JLabel("Seleccionar turno:"));
        panel.add(comboTurnos);
        panel.add(btnEliminar);

        this.add(panel, BorderLayout.SOUTH);
    }


    private void cargarEntidades() {
        comboEntidad.removeAllItems();
        if (esPaciente) {
            List<Medico> medicos = medicoServicio.leerTodos();
            for (Medico m : medicos){
                if(!(m.getId() == -1)){
                    comboEntidad.addItem(m);
                }
            }
        } else {
            List<Paciente> pacientes = pacienteServicio.leerTodos();
            for (Paciente p : pacientes) comboEntidad.addItem(p);
        }
    }

    private void cargarHorarios() throws MedicoVacioException, SQLException {
        comboHorarios.removeAllItems();
        Medico medico = null;
        try {
            medico = esPaciente ? (Medico) comboEntidad.getSelectedItem() : medicoServicio.leerPorId(idUsuario);
        } catch (Error e){
            //revisar
        }

        if (medico == null) throw new MedicoVacioException();

        try {
            LocalDate f = LocalDate.parse(fecha.getText());
            List<LocalDateTime> horarios = null;
            Atencion atencion = null;
            if (f.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "La fecha no puede ser anterior a hoy.");
            } else {
                List<Atencion> aMedico = atencionServicio.leerPorMedico(medico.getId());
                for(Atencion a : aMedico) {
                    if ((a.getDesde().isBefore(f) || a.getDesde().isEqual(f)) && (a.getHasta().isAfter(f) || a.getHasta().isEqual(f))) {
                        horarios = turnoServicio.generarHorariosDisponibles(f, medico.getId());
                        atencion = a;
                    }

                }
            }

            if (horarios == null || horarios.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay horarios disponibles.");
            } else {
                for (LocalDateTime horario : horarios) {
                    String horarioFormateado = String.format("%tF %tR", horario, horario);
                    comboHorarios.addItem(horarioFormateado + " - " + atencion.getConsultorio());
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los horarios. Revise el formato de la fecha (yyyy-MM-dd).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearBotonSwitch(JPanel panel) {
        JToggleButton boton = new JToggleButton("BDD/Archivo");
        boton.addActionListener(e -> {
            if (boton.isSelected()) {
                turnoServicio.setPersistencia(new Persistencia.DB.TurnoDBDao());
                JOptionPane.showMessageDialog(this, "Persistencia cambiada a Base de Datos", "Cambio de persistencia", JOptionPane.INFORMATION_MESSAGE);
            } else {
                turnoServicio.setPersistencia(new Persistencia.Archivo.ArchivoTurnoDao());
                JOptionPane.showMessageDialog(this, "Persistencia cambiada a Archivo", "Cambio de persistencia", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        panel.add(boton);
    }

    private void crearBotonAceptar(JPanel panel) {
        JButton boton = new JButton("Agregar Turno");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        boton.addActionListener(e -> {
            try {
                String seleccionado = (String) comboHorarios.getSelectedItem();
                LocalDateTime horarioSeleccionado = LocalDateTime.parse(seleccionado.split(" ")[0] + " " + seleccionado.split(" ")[1], formatter);
                Turno t;
                if (esPaciente) {
                    Medico m = (Medico) comboEntidad.getSelectedItem();
                    Paciente p = pacienteServicio.leer(idUsuario);
                    t = new Turno(horarioSeleccionado, m, p, atencionServicio.buscarConsultorioPorMedico(m.getId(), horarioSeleccionado.toLocalDate()));
                    cargarTurnosUsuario();
                } else {
                    Paciente p = (Paciente) comboEntidad.getSelectedItem();
                    Medico m = medicoServicio.leerPorId(idUsuario);
                    t = new Turno(horarioSeleccionado, m, p, atencionServicio.buscarConsultorioPorMedico(idUsuario, horarioSeleccionado.toLocalDate()));
                    cargarTurnosUsuario();
                }
                turnoServicio.grabar(t);
                JOptionPane.showMessageDialog(this, "Turno agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al agregar el turno: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(boton);
    }

    private void cargarTurnosUsuario() {
        List<Turno> turnos;
        if (esPaciente) {
            turnos = turnoServicio.buscarPorPaciente(idUsuario);
        } else {
            turnos = turnoServicio.buscarPorMedico(idUsuario);
        }

        for (Turno t : turnos) {
            comboTurnos.addItem(t);
        }
    }

}