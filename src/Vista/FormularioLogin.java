package Vista;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import java.awt.GridLayout;

import Servicios.MedicoServicio;
import Servicios.PacienteServicio;
import Servicios.Exceptions.LoginExcepcion;

public class FormularioLogin extends JFrame {

    private JTextField email;
    private JTextField passwd;
    private MedicoServicio medicoServicio;
    private PacienteServicio pacienteServicio;


    public FormularioLogin() {

        setLayout(new GridLayout(3, 2));
        setVisible(true);
        

        medicoServicio = new MedicoServicio();
        pacienteServicio = new PacienteServicio();

        createPaneLogin();
        crearBotonLog();
        
    }

    private void createPaneLogin() {

        JPanel panel = new JPanel();

        JLabel labelEmail = new JLabel("Email:");
        email = new JTextField();
        JLabel labelPasswd = new JLabel("Contraseña:");
        passwd = new JTextField();

        panel.add(labelEmail);
        panel.add(email);
        panel.add(labelPasswd);
        panel.add(passwd);

        this.add(panel);
        
    }

    private void crearBotonLog() {
        
        JPanel panel = new JPanel();
		JButton boton = new JButton("Iniciar Sesión");
        JToggleButton botonToggle = new JToggleButton("Paciente/Medico");

        boton.addActionListener( e -> {
			try {
				if(botonToggle.isSelected()) {
                    int idPaciente = pacienteServicio.login(email.getText(), passwd.getText());
                    MenuPaciente menu = new MenuPaciente(idPaciente);
                    menu.setVisible(true);
                    this.dispose();
                   
                } else {
                    int idMedico = medicoServicio.login(email.getText(), passwd.getText());
                    MenuMedico menu = new MenuMedico(idMedico);
                    menu.setVisible(true);
                    this.dispose();
                    
                }
			} catch (LoginExcepcion e1) {
                JOptionPane.showMessageDialog(this, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
		});

        JButton botonRegistro = new JButton("Registrarse");

        botonRegistro.addActionListener(e -> {
            new FormularioRegistro(); // abre ventana nueva
        });

        panel.add(botonRegistro);


        this.add(panel);
        panel.add(boton);
        panel.add(botonToggle);
    }

    
}
