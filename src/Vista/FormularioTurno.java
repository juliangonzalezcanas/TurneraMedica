package Vista;

import java.awt.GridLayout;
import java.sql.SQLException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
import Entidades.Turno;
import Servicios.MedicoServicio;
import Servicios.PacienteServicio;
import Servicios.TurnoServicio;
import Vista.Exceptions.MedicoVacioException;

public class FormularioTurno extends JPanel{

    private JTextField fecha;

	private JTextField id;

    private JTextField fechaM;
	private JTextField medicoM;
	private JTextField idM;

	private JTextField idE;

    private JComboBox<Medico> comboMedico;

    private JComboBox<String> comboHorarios;
    

	
	private TurnoServicio turnoServicio;
    private MedicoServicio medicoServicio;
    private PacienteServicio pacienteServicio;

    public FormularioTurno() {

        setLayout(new GridLayout(5,2));
        add(new JLabel("Gestión de Turnos", SwingConstants.CENTER));
		crearBotonSwitch();

		fecha = new JTextField();

		id = new JTextField();
		

		fechaM = new JTextField();
		medicoM = new JTextField();
		idM = new JTextField();

		idE = new JTextField();

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

        comboMedico = new JComboBox<>();
        cargarMedicos();

        comboHorarios = new JComboBox<>();
		
		JLabel idLbl = new JLabel("Id ");
		
		panel.setLayout(new GridLayout(13, 2));
		panel.add(titulo);
		panel.add(idLbl);
		panel.add(id);
        panel.add(fechaLbl);
        panel.add(fecha);
        panel.add(medicoLbl);
        panel.add(comboMedico);
        panel.add(turnoLbl);
        panel.add(comboHorarios);
		
        crearBotonHorarios();
        


		this.add(panel);
    }

    private void cargarMedicos() {
        List<Medico> medicos = medicoServicio.leerTodos();
        for (Medico medico : medicos) {
            comboMedico.addItem(medico);
        }
    }
    

    private void cargarHorarios() throws MedicoVacioException, SQLException{
        comboHorarios.removeAllItems();
        Medico medico = (Medico) comboMedico.getSelectedItem();
        if (medico == null) {
            throw new MedicoVacioException();
        } else {
            try{
                LocalDate f = LocalDate.parse(fecha.getText()); // formato yyyy-MM-dd
                List<LocalDateTime> horarios = turnoServicio.generarHorariosDisponibles(f, medico.getId());

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
                Medico m = (Medico) comboMedico.getSelectedItem();

                Turno t = new Turno(Integer.parseInt(id.getText()), horarioSeleccionado, m, pacienteServicio.leer(1));
                turnoServicio.grabar(t);

                JOptionPane.showMessageDialog(this, "Turno agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al agregar el turno: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panel.add(boton);
        this.add(panel);
    }



    
}
