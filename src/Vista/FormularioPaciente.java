package Vista;


import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import Entidades.Paciente;
import Entidades.Turno;
import Servicios.PacienteServicio;
import Servicios.TurnoServicio;
import Servicios.Exceptions.EliminandoPacienteException;
import Servicios.Exceptions.GrabandoPacienteException;
import Servicios.Exceptions.ModificandoPacienteException;
import Vista.Exceptions.ApellidoVacioException;
import Vista.Exceptions.DniVacioException;
import Vista.Exceptions.EmailVacioException;
import Vista.Exceptions.IdVacioException;
import Vista.Exceptions.NombreVacioException;
import Vista.Exceptions.ObraSocialVaciaException;

public class FormularioPaciente extends JPanel {


	private JTextField nombre;
	private JTextField apellido;
	private JTextField dni;
	private JTextField email;
	private JTextField obra_social;
	private JTextField id;

	private JTextField idM;

	private JTextField idE;

	private JTextField idT;

	private JTable tablaPacientes;
	
	private PacienteServicio pacienteServicio;
	private TurnoServicio turnoServicio;
	



	public FormularioPaciente() {

		
		setLayout(new GridLayout(6,2));
        add(new JLabel("Gestión de Pacientes", SwingConstants.CENTER));
		crearBotonSwitch();
		
		

		nombre = new JTextField();
		apellido = new JTextField();
		dni = new JTextField();
		email = new JTextField();
		obra_social = new JTextField();
		id = new JTextField();
		idM = new JTextField();

		idE = new JTextField();

		idT = new JTextField();

		tablaPacientes = new JTable();

		pacienteServicio = new PacienteServicio();
		turnoServicio = new TurnoServicio();
		
		
		

		createPaneAgregar();
		crearBotonAceptar();

		crearBotonLlenarCampos();
		crearBotonModificar();


		createPanelEliminar();
		crearBotonEliminar();

		paneBuscarTurnos();
		add(new JLabel(" ", SwingConstants.CENTER));

		crearTabla();
		
		
	}
	
	private void createPaneAgregar() {
		JPanel panel = new JPanel();
		
		JLabel titulo = new JLabel("Paciente");
		JLabel nombreLbl = new JLabel("Nombre ");
		JLabel apellidoLbl = new JLabel("Apellido ");
		JLabel dniLbl = new JLabel("Dni ");
		JLabel emailLbl = new JLabel("Email ");
		JLabel obra_socialLbl = new JLabel("Obra social ");
		JLabel idLbl = new JLabel("Id ");
		
		
		panel.setLayout(new FlowLayout());
		panel.add(titulo);
		panel.add(idLbl);
		panel.add(id);
		panel.add(nombreLbl);
		panel.add(nombre);
		panel.add(apellidoLbl);
		panel.add(apellido);
		panel.add(dniLbl);
		panel.add(dni);
		panel.add(emailLbl);
		panel.add(email);
		panel.add(obra_socialLbl);
		panel.add(obra_social);
		



		this.add(panel);
	}

	private void crearBotonLlenarCampos() {
		JPanel panel = new JPanel();
		JButton boton = new JButton("Llenar campos");

		panel.add(new JLabel("Interte su id"));
		panel.add(idM);
		boton.addActionListener(e ->{
			try {
        		Paciente paciente = pacienteServicio.leer(Integer.parseInt(idM.getText())); 
				if(paciente != null){
					id.setText(paciente.getId().toString());
					nombre.setText(paciente.getNombre());
					apellido.setText(paciente.getApellido());
					dni.setText(paciente.getDni().toString());
					email.setText(paciente.getEmail());
					obra_social.setText(paciente.getObraSocial());

					panel.revalidate();
					panel.repaint();
				} else {
					JOptionPane.showMessageDialog(this, "Paciente no encontrado", 
						"Error", JOptionPane.ERROR_MESSAGE);
				}

			} catch (IOException | ClassNotFoundException ex) {
				JOptionPane.showMessageDialog(this, "Error al buscar paciente: " + ex.getMessage(), 
					"Error", JOptionPane.ERROR_MESSAGE);
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Id inválido", 
					"Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		panel.add(boton);
		this.add(panel);
	}

	private void createPanelEliminar(){

		JPanel panel = new JPanel();

		JLabel titulo = new JLabel("Eliminar Paciente");
		
		JLabel idLbl = new JLabel("Id ");
		
		panel.setLayout(new GridLayout(3, 2));
		panel.add(titulo);
		panel.add(idLbl);
		panel.add(idE);

		this.add(panel);
	}

	private void crearTabla(){
		JPanel panel = new JPanel();
		List<Paciente> listaPacientes = pacienteServicio.leerTodos();

		String[] columnas = {"ID", "Nombre", "Apellido", "DNI", "Email", "Obra Social"};
		DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
		modelo.addRow(columnas);
		for (Paciente p : listaPacientes) {
			Object[] fila = {
				p.getId(),
				p.getNombre(),
				p.getApellido(),
				p.getDni(),
				p.getEmail(),
				p.getObraSocial()
			};
			modelo.addRow(fila);
		}
		tablaPacientes.setModel(modelo);
		panel.add(tablaPacientes);
		this.add(panel);

	}

	private void crearBotonEliminar(){

		JPanel panel = new JPanel();
		JButton boton = new JButton("Eliminar");
		
		boton.addActionListener( e -> {
			try {
				if(idE.getText().isEmpty()) {
					throw new IdVacioException();
				} else {
					pacienteServicio.eliminar(Integer.valueOf(idE.getText()));

					JOptionPane.showMessageDialog(this, "Paciente eliminado correctamente", 
						"Éxito", JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (IdVacioException e1) {
				JOptionPane.showMessageDialog(this, "El id no puede estar vacío", 
					"Campo vacio", JOptionPane.INFORMATION_MESSAGE);
			} catch (EliminandoPacienteException e1) {
				JOptionPane.showMessageDialog(this, "Error al eliminar el paciente", 
					"Error", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			} 
		});

		panel.add(boton);
		this.add(panel);
	}

	private void crearBotonModificar(){

		JPanel panel = new JPanel();
		JButton boton = new JButton("Modificar");
		
		boton.addActionListener( e -> {
			try {
				validarCampos();
				Paciente p = new Paciente( Integer.valueOf(id.getText()), nombre.getText(), apellido.getText(), 
					Integer.valueOf(dni.getText()), email.getText(), obra_social.getText());
				pacienteServicio.modificar(p);

				JOptionPane.showMessageDialog(this, "Paciente modificado correctamente", 
					"Éxito", JOptionPane.INFORMATION_MESSAGE);
				

			} catch (NombreVacioException e1) {
				JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío", 
					"Campo vacio", JOptionPane.INFORMATION_MESSAGE);
			} catch (ApellidoVacioException e1) {
				JOptionPane.showMessageDialog(this, "El apellido no puede estar vacío", 
					"Campo vacio", JOptionPane.INFORMATION_MESSAGE);
				e1.printStackTrace();
			} catch (DniVacioException e1) {
				JOptionPane.showMessageDialog(this, "El dni no puede estar vacío", 
					"Campo vacio", JOptionPane.INFORMATION_MESSAGE);
			} catch (EmailVacioException e1) {
				JOptionPane.showMessageDialog(this, "El email no puede estar vacío", 
					"Campo vacio", JOptionPane.INFORMATION_MESSAGE);
			} catch (ObraSocialVaciaException e1) {
				JOptionPane.showMessageDialog(this, "La obra social no puede estar vacía", 
					"Campo vacio", JOptionPane.INFORMATION_MESSAGE);
			} catch (IdVacioException e1) {
				JOptionPane.showMessageDialog(this, "El id no puede estar vacío", 
					"Campo vacio", JOptionPane.INFORMATION_MESSAGE);
			} catch (ModificandoPacienteException e1) {
				JOptionPane.showMessageDialog(this, "Error al modificar el paciente", 
					"Error", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			} 
		});

		panel.add(boton);
		this.add(panel);
	}
	
	private void crearBotonAceptar() {

		JPanel panel = new JPanel();
		JButton boton = new JButton("Aceptar");

		boton.addActionListener( e -> {
			try {
				validarCampos();
				Paciente p = new Paciente( Integer.valueOf(id.getText()), nombre.getText(), apellido.getText(), 
					Integer.valueOf(dni.getText()), email.getText(), obra_social.getText());
				pacienteServicio.grabar(p);

				JOptionPane.showMessageDialog(this, "Paciente agregado correctamente", 
					"Éxito", JOptionPane.INFORMATION_MESSAGE);
				
				

			} catch (NombreVacioException e1) {
				JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío", 
					"Campo vacio", JOptionPane.INFORMATION_MESSAGE);
			} catch (ApellidoVacioException e1) {
				JOptionPane.showMessageDialog(this, "El apellido no puede estar vacío", 
					"Campo vacio", JOptionPane.INFORMATION_MESSAGE);
			} catch (DniVacioException e1) {
				JOptionPane.showMessageDialog(this, "El dni no puede estar vacío", 
					"Campo vacio", JOptionPane.INFORMATION_MESSAGE);
			} catch (EmailVacioException e1) {
				JOptionPane.showMessageDialog(this, "El email no puede estar vacío", 
					"Campo vacio", JOptionPane.INFORMATION_MESSAGE);
			} catch (ObraSocialVaciaException e1) {
				JOptionPane.showMessageDialog(this, "La obra social no puede estar vacía", 
					"Campo vacio", JOptionPane.INFORMATION_MESSAGE);
			} catch (GrabandoPacienteException e1) {
				JOptionPane.showMessageDialog(this, "El dni ingresado ya existe tiene cuenta", 
					"Campo vacio", JOptionPane.INFORMATION_MESSAGE);
			} catch (IdVacioException e1) {
				JOptionPane.showMessageDialog(this, "El id no puede estar vacío", 
					"Campo vacio", JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		panel.add(boton);
		this.add(panel);
	}

	private void crearBotonSwitch(){

		JPanel panel = new JPanel();
		JToggleButton boton = new JToggleButton("BDD/Archivo");
			
		boton.addActionListener( e -> {
			
			if(boton.isSelected()) {
				pacienteServicio.setPersistencia(new Persistencia.DB.PacienteDBDao());
				JOptionPane.showMessageDialog(this, "Persistencia cambiada a Base de Datos", 
					"Cambio de persistencia", JOptionPane.INFORMATION_MESSAGE);
			} else {
				pacienteServicio.setPersistencia(new Persistencia.Archivo.ArchivoPacienteDao());
				JOptionPane.showMessageDialog(this, "Persistencia cambiada a Archivo", 
					"Cambio de persistencia", JOptionPane.INFORMATION_MESSAGE);
			}
			
		});

		panel.add(boton);
		this.add(panel);

	}

	private void paneBuscarTurnos(){
		JPanel panel = new JPanel();
		JToggleButton boton = new JToggleButton("Buscar Turnos");
		JLabel idLbl = new JLabel("Id Paciente ");

		panel.add(idLbl);
		panel.add(idT);


		boton.addActionListener(e -> {
			try {
				List<Turno> turnosPaciente = turnoServicio.buscarPorPaciente(Integer.valueOf(idT.getText()));
				List<Turno> turnosPorFecha = pacienteServicio.buscarTurnosPorFecha(turnosPaciente, java.time.LocalDate.now());
				for(Turno t: turnosPorFecha){
					panel.add(new JLabel("Turno: " + t.getId() + ", Medico: " + t.getMedico() + " - Fecha: " + t.getFecha().toLocalDate() + " - Hora: " + t.getFecha().toLocalTime()));
				}
				panel.revalidate();
				panel.repaint();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error al buscar turnos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		panel.add(boton);
		this.add(panel);
	}


	
	private Boolean validarCampos() throws NombreVacioException, ApellidoVacioException, DniVacioException, EmailVacioException, ObraSocialVaciaException, IdVacioException {

		if (nombre.getText().isEmpty())
			throw new NombreVacioException();
		if (apellido.getText().isEmpty())
			throw new ApellidoVacioException();
		if (dni.getText().isEmpty())
			throw new DniVacioException();
		if (email.getText().isEmpty())
			throw new EmailVacioException();
		if (obra_social.getText().isEmpty())
			throw new ObraSocialVaciaException();
		if (id.getText().isEmpty())
			throw new IdVacioException();
		return true;
	}


}



