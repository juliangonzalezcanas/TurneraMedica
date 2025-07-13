package Vista;

import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import Entidades.Medico;
import Entidades.Paciente;
import Entidades.Turno;
import Servicios.MedicoServicio;
import Servicios.PacienteServicio;
import Servicios.TurnoServicio;
import Vista.Exceptions.MedicoVacioException;

public class FormularioTurno extends JPanel{

    private JTextField fecha;



    private JTextField fechaM;
	private JTextField medicoM;


    private JComboBox comboEntidad;

    private JComboBox<String> comboHorarios;

    private int idUsuario;
    private boolean esPaciente;

    

	
	private TurnoServicio turnoServicio;
    private MedicoServicio medicoServicio;
    private PacienteServicio pacienteServicio;

    public FormularioTurno(boolean esPaciente, int idUsuario) {

        setLayout(new GridLayout(5,2));
        add(new JLabel("Gestión de Turnos", SwingConstants.CENTER));
		crearBotonSwitch();

        this.esPaciente = esPaciente;
        this.idUsuario = idUsuario;

		fecha = new JTextField();
		

		fechaM = new JTextField();
		medicoM = new JTextField();

		turnoServicio = new TurnoServicio();
        medicoServicio = new MedicoServicio();
        pacienteServicio = new PacienteServicio();

        createPaneAgregar();
        crearBotonAceptar();
    }

    private void createPaneAgregar(){
        JPanel panel = new JPanel();
		
		JLabel titulo = new JLabel("Agregar Turno", SwingConstants.CENTER);
        JLabel fechaLbl = new JLabel("Fecha ");
        JLabel medicoLbl = new JLabel("Medico ");
        JLabel turnoLbl = new JLabel("Horarios Disponibles:");

        comboEntidad = new JComboBox<>();
        cargarEntidades();

        comboHorarios = new JComboBox<>();
		

		
		panel.setLayout(new GridLayout(13, 2));
		panel.add(titulo);
        panel.add(fechaLbl);
        panel.add(fecha);
        panel.add(medicoLbl);
        panel.add(comboEntidad);
        panel.add(turnoLbl);
        panel.add(comboHorarios);
		
        crearBotonHorarios();
        


		this.add(panel);
    }

    private void cargarEntidades() {
        if (esPaciente) {
            List<Medico> medicos = medicoServicio.leerTodos();
            for (Medico m : medicos) comboEntidad.addItem(m);
        } else {
            List<Paciente> pacientes = pacienteServicio.leerTodos();
            for (Paciente p : pacientes) comboEntidad.addItem(p);
        }
    }
    

    private void cargarHorarios() throws MedicoVacioException, SQLException{
        comboHorarios.removeAllItems();
        Medico medico = (Medico) comboEntidad.getSelectedItem();
        List<LocalDateTime> horarios = null;
        if (medico == null) {
            throw new MedicoVacioException();
        } else {
            try{
                LocalDate f = LocalDate.parse(fecha.getText()); // formato yyyy-MM-dd
                if(esPaciente){
                    horarios = turnoServicio.generarHorariosDisponibles(f, medico.getId());
                } else {
                    horarios = turnoServicio.generarHorariosDisponibles(f, idUsuario);
                }

                if (horarios.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No hay horarios disponibles.");
                } else {
                    for (LocalDateTime horario : horarios) {
                        String horarioFormateado = String.format("%tF %tR", horario, horario); // yyyy-MM-dd HH:mm
                        comboHorarios.addItem(horarioFormateado);
                    }

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al cargar los horarios. Revise el formato de la fecha (yyyy-MM-dd).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void crearBotonHorarios(){

        JPanel panel = new JPanel();
        JButton buscarHorarios = new JButton("Buscar Horarios");

        buscarHorarios.addActionListener(e -> {
            try{
                cargarHorarios();
            } catch (MedicoVacioException ex) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un médico", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar los horarios: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(buscarHorarios);
        this.add(panel);
    }

    private void crearBotonSwitch(){
		
		JPanel panel = new JPanel();
		JToggleButton boton = new JToggleButton("BDD/Archivo");
			
		boton.addActionListener( e -> {
			
			if(boton.isSelected()) {
				turnoServicio.setPersistencia(new Persistencia.DB.TurnoDBDao());
				JOptionPane.showMessageDialog(this, "Persistencia cambiada a Base de Datos", 
					"Cambio de persistencia", JOptionPane.INFORMATION_MESSAGE);
			} else {
				turnoServicio.setPersistencia(new Persistencia.Archivo.ArchivoTurnoDao());
				JOptionPane.showMessageDialog(this, "Persistencia cambiada a Archivo", 
					"Cambio de persistencia", JOptionPane.INFORMATION_MESSAGE);
			}
			
		});

		panel.add(boton);
		this.add(panel);

	}

    public void crearBotonAceptar() {

        JPanel panel = new JPanel();
        JButton boton = new JButton("Agregar Turno");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        boton.addActionListener(e -> {
            try {
                String seleccionado = (String) comboHorarios.getSelectedItem();
                LocalDateTime horarioSeleccionado = LocalDateTime.parse(seleccionado, formatter);
                Turno t = null;
                if (esPaciente) {
                    Medico m = (Medico) comboEntidad.getSelectedItem();
                    Paciente p = pacienteServicio.leer(idUsuario);
                    t = new Turno(horarioSeleccionado, m, p);
                    turnoServicio.grabar(t);
                } else {
                    Paciente p = (Paciente) comboEntidad.getSelectedItem();
                    Medico m = medicoServicio.leerPorId(idUsuario);
                    t = new Turno(horarioSeleccionado, m, p);
                    turnoServicio.grabar(t);
                }


                JOptionPane.showMessageDialog(this, "Turno agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al agregar el turno: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panel.add(boton);
        this.add(panel);
    }



    
}
