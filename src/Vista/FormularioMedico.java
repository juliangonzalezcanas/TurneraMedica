package Vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import Entidades.Medico;
import Entidades.Turno;
import Servicios.MedicoServicio;
import Servicios.TurnoServicio;
import Servicios.Exceptions.GrabandoPacienteException;
import Vista.Exceptions.ApellidoVacioException;
import Vista.Exceptions.DniVacioException;
import Vista.Exceptions.EmailVacioException;
import Vista.Exceptions.EspecialidadVaciaException;
import Vista.Exceptions.IdVacioException;
import Vista.Exceptions.NombreVacioException;
import Vista.Exceptions.ObraSocialVaciaException;
import Vista.Exceptions.PrecioConsultaVacioException;

public class FormularioMedico extends JPanel {
    private JTextField nombre;
	private JTextField apellido;
	private JTextField dni;
	private JTextField email;
	private JTextField obra_social;
	private JTextField id;
    private JTextField precio_consulta;
	private JTextField especialidad;

	private JTable tablaMedicos;

	private JTextField idM;

	private JTextField idE;

	private JTextField idT;
	private JTextField fecha1;
	private JTextField fecha2;

	private JTextField fechaB;
	private JTextField idTurnoBusqueda;
	
	private MedicoServicio medicoServicio;
	private TurnoServicio turnoServicio;



	public FormularioMedico() {

		
		setLayout(new GridLayout(8,2));
        add(new JLabel("Gestión de Medicos", SwingConstants.CENTER), BorderLayout.NORTH);
		add(new JLabel(" ", SwingConstants.CENTER), BorderLayout.NORTH);

		medicoServicio = new MedicoServicio();
		turnoServicio = new TurnoServicio();
		
        nombre = new JTextField();
		apellido = new JTextField();
		dni = new JTextField();
		email = new JTextField();
        precio_consulta = new JTextField();
		obra_social = new JTextField();
		especialidad = new JTextField();
		id = new JTextField();

		tablaMedicos = new JTable();
		
		idM = new JTextField();

		idE = new JTextField();

		idT = new JTextField();

		fecha1 = new JTextField();
		fecha2 = new JTextField();

		fechaB = new JTextField();
		idTurnoBusqueda = new JTextField();

		
		

		//mostrarEntidades();
		createPanelAgregar();
		crearBotonAceptar();
		
		crearBotonLlenarCampos();
		crearBotonModificar();

		createPaneEliminar();
		crearBotonEliminar();

		createPaneGanancia();
		createPaneBuscarTurno();

		crearTabla();
		
	}
	
	private void createPanelAgregar() {
		JPanel panel = new JPanel();
		
		JLabel titulo = new JLabel("Agregar Medico");
		JLabel nombreLbl = new JLabel("Nombre ");
		JLabel apellidoLbl = new JLabel("Apellido ");
		JLabel dniLbl = new JLabel("Dni ");
		JLabel emailLbl = new JLabel("Email ");
		JLabel obra_socialLbl = new JLabel("Obra social ");
		JLabel idLbl = new JLabel("Id ");
        JLabel precio_consultaLbl = new JLabel("Precio consulta ");
		JLabel especialidadLbl = new JLabel("Especialidad ");
		
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
        panel.add(precio_consultaLbl);
        panel.add(precio_consulta);
		panel.add(obra_socialLbl);
		panel.add(obra_social);
		panel.add(especialidadLbl);
		panel.add(especialidad);


		this.add(panel);
	}



	private void createPaneEliminar(){

		JPanel panel = new JPanel();

		JLabel titulo = new JLabel("Eliminar Medico");
		
		JLabel idLbl = new JLabel("Id ");
		
		panel.setLayout(new FlowLayout());
		panel.add(titulo);
		panel.add(idLbl);
		panel.add(idE);

		this.add(panel);
	}

	private void createPaneGanancia(){
		JPanel panel = new JPanel();
		JLabel idLbl = new JLabel("Id ");
		JLabel fecha1Lbl = new JLabel("Fecha desde ");
		JLabel fecha2Lbl = new JLabel("hasta ");
		JButton boton = new JButton("Calcular Ganancia");

		panel.add(idLbl);
		panel.add(idT);

		panel.add(fecha1Lbl);
		panel.add(fecha1);
		panel.add(fecha2Lbl);
		panel.add(fecha2);

		boton.addActionListener( e -> {

			List<Turno> turnosMedico = turnoServicio.buscarPorMedico(Integer.valueOf(idT.getText()));
			Float ganancia = medicoServicio.calcularGanancia(LocalDateTime.of(LocalDate.parse(fecha1.getText()), LocalTime.of(0, 0)), LocalDateTime.of(LocalDate.parse(fecha2.getText()), LocalTime.of(0, 0)), turnosMedico);
			panel.add(new JLabel("El medico/a: " + turnosMedico.getFirst().getMedico().getNombre() + " gano: " + ganancia + "$" + " en el periodo seleccionado"));
			panel.revalidate();
			panel.repaint();
		});

		
		panel.add(boton);

		this.add(panel);

	}

	private void createPaneBuscarTurno(){
		JPanel panel = new JPanel();
		JLabel idLbl = new JLabel("Id ");
		JLabel fechaLbl = new JLabel("Fecha: ");
		JButton boton = new JButton("Buscar Turnos");

		panel.add(idLbl);
		panel.add(idTurnoBusqueda);

		panel.add(fechaLbl);
		panel.add(fechaB);

		boton.addActionListener( e -> {
			List<Turno> turnosMedico = turnoServicio.buscarPorMedico(Integer.valueOf(idTurnoBusqueda.getText()));
			List<Turno> turnosLimpia = medicoServicio.buscarTurnosPorFecha(turnosMedico, LocalDate.parse(fechaB.getText()));
			for(Turno t: turnosLimpia) {
				panel.add(new JLabel("Turno: " + t.getId() + " para el paciente: " + t.getPaciente().getNombre() + " en la fecha seleccionada"));
			}
			panel.revalidate();
			panel.repaint();
		});
		
		
		panel.add(boton);

		this.add(panel);

	}

	private void crearBotonEliminar(){

		JPanel panel = new JPanel();
		JButton boton = new JButton("Eliminar");
		
		boton.addActionListener( e -> {
			try {
				medicoServicio.eliminar(Integer.valueOf(idE.getText()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
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
				Medico m = new Medico( Integer.valueOf(id.getText()), nombre.getText(), apellido.getText(), 
					Integer.valueOf(dni.getText()), email.getText(), obra_social.getText(), Float.valueOf(precio_consulta.getText()), especialidad.getText(), "");
				medicoServicio.modificar(m);
				JOptionPane.showMessageDialog(this, "Medico modificado", 
					"Campo vacio", JOptionPane.YES_OPTION);
			}  catch (EspecialidadVaciaException e1) {
				e1.printStackTrace();
			} catch (PrecioConsultaVacioException e1) {
				JOptionPane.showMessageDialog(this, "El precio de la consulta no puede estar vacío", 
					"Campo vacio", JOptionPane.INFORMATION_MESSAGE);
				e1.printStackTrace();
			} catch (NombreVacioException e1) {
				JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío", 
					"Campo vacio", JOptionPane.INFORMATION_MESSAGE);
				e1.printStackTrace();
			} catch (ApellidoVacioException e1) {
				JOptionPane.showMessageDialog(this, "El apellido no puede estar vacío", 
					"Campo vacio", JOptionPane.INFORMATION_MESSAGE);
				e1.printStackTrace();
			} catch (DniVacioException e1) {
				JOptionPane.showMessageDialog(this, "El dni no puede estar vacío", 
					"Campo vacio", JOptionPane.INFORMATION_MESSAGE);
				e1.printStackTrace();
			} catch (EmailVacioException e1) {
				JOptionPane.showMessageDialog(this, "El email no puede estar vacío", 
					"Campo vacio", JOptionPane.INFORMATION_MESSAGE);
				e1.printStackTrace();
			} catch (ObraSocialVaciaException e1) {
				JOptionPane.showMessageDialog(this, "La obra social no puede estar vacía", 
					"Campo vacio", JOptionPane.INFORMATION_MESSAGE);
				e1.printStackTrace();
			} catch (IdVacioException e1) {
				JOptionPane.showMessageDialog(this, "El id no puede estar vacío", 
					"Campo vacio", JOptionPane.INFORMATION_MESSAGE);
				e1.printStackTrace();
			}
		});

		panel.add(boton);
		this.add(panel);
	}

	private void crearBotonLlenarCampos() {
		JPanel panel = new JPanel();
		JButton boton = new JButton("Llenar campos");

		panel.add(new JLabel("Interte su id"));
		panel.add(idM);
		boton.addActionListener(e ->{
			try {
        		Medico m = medicoServicio.leerPorId(Integer.parseInt(idM.getText())); 
				if(m != null){
					id.setText(m.getId().toString());
					nombre.setText(m.getNombre());
					apellido.setText(m.getApellido());
					dni.setText(m.getDni().toString());
					email.setText(m.getEmail());
					obra_social.setText(m.getObraSocial());
					precio_consulta.setText(m.getPrecioConsulta().toString());
					especialidad.setText(m.getEspecialidad());

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

	private void crearTabla(){
		JPanel panel = new JPanel();
		List<Medico> listaMedicos = medicoServicio.leerTodos();

		String[] columnas = {"ID", "Nombre", "Apellido", "DNI", "Email", "Obra Social", "Precio Consulta", "Especialidad"};
		DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
		modelo.addRow(columnas);
		for (Medico m : listaMedicos) {
			Object[] fila = {
				m.getId(),
				m.getNombre(),
				m.getApellido(),
				m.getDni(),
				m.getEmail(),
				m.getObraSocial(),
				m.getPrecioConsulta(),
				m.getEspecialidad()
			};
			modelo.addRow(fila);
		}
		tablaMedicos.setModel(modelo);
		panel.add(tablaMedicos);
		this.add(panel);

	}
	
	private void crearBotonAceptar() {

		JPanel panel = new JPanel();
		JButton boton = new JButton("Aceptar");

		boton.addActionListener( e -> {
			try {
				validarCampos();
				Medico m = new Medico(Integer.valueOf(id.getText()), nombre.getText(), apellido.getText(), 
					Integer.valueOf(dni.getText()), email.getText(), obra_social.getText(), Float.valueOf(precio_consulta.getText()), especialidad.getText(), "");
				medicoServicio.grabar(m);
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
				
			} catch (IOException e1) {
				
			} catch (GrabandoPacienteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (PrecioConsultaVacioException e1) {
				JOptionPane.showMessageDialog(this, "El precio de la consulta no puede estar vacío", 
					"Campo vacio", JOptionPane.INFORMATION_MESSAGE);
				
			} catch (IdVacioException e1) {
				JOptionPane.showMessageDialog(this, "El id no puede estar vacío", 
					"Campo vacio", JOptionPane.INFORMATION_MESSAGE);
				
			} catch (EspecialidadVaciaException e1) {
				JOptionPane.showMessageDialog(this, "La especialidad no puede estar vacía", 
					"Campo vacio", JOptionPane.INFORMATION_MESSAGE);
			} 
		});
		panel.add(boton);
		this.add(panel);
	}


	
	private Boolean validarCampos() throws NombreVacioException, ApellidoVacioException, DniVacioException, EmailVacioException, ObraSocialVaciaException, PrecioConsultaVacioException, IdVacioException, EspecialidadVaciaException {

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
		if (precio_consulta.getText().isEmpty())
			throw new PrecioConsultaVacioException();
		if (especialidad.getText().isEmpty())
			throw new EspecialidadVaciaException();
		return true;
	}

}
