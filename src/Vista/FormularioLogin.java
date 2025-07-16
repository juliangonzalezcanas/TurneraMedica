package Vista;

import javax.swing.*;
import java.awt.*;

import Servicios.MedicoServicio;
import Servicios.PacienteServicio;
import Servicios.Exceptions.LoginException;

public class FormularioLogin extends JFrame {

    private JTextField email;
    private JPasswordField passwd;
    private JLabel rolLabel;
    private JToggleButton botonToggle;

    private MedicoServicio medicoServicio;
    private PacienteServicio pacienteServicio;

    public FormularioLogin() {
        setTitle("Inicio de Sesión");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana

        medicoServicio = new MedicoServicio();
        pacienteServicio = new PacienteServicio();

        // Layout principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Título
        JLabel titulo = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titulo);
        mainPanel.add(Box.createVerticalStrut(10));

        // Panel formulario con GridLayout para alinear los campos
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        formPanel.add(new JLabel("Email:"));
        email = new JTextField(15);
        formPanel.add(email);

        formPanel.add(new JLabel("Contraseña:"));
        passwd = new JPasswordField(15);
        formPanel.add(passwd);


        JPanel rolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        rolPanel.add(new JLabel("Rol:"));
        rolLabel = new JLabel("Paciente");
        botonToggle = new JToggleButton("Cambiar");
        botonToggle.addActionListener(e -> rolLabel.setText(botonToggle.isSelected() ? "Médico" : "Paciente"));
        rolPanel.add(rolLabel);
        rolPanel.add(botonToggle);

        formPanel.add(rolPanel);
        mainPanel.add(formPanel);

        
        JPanel botonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton botonLogin = new JButton("Iniciar Sesión");
        JButton botonRegistro = new JButton("Registrarse");

        botonLogin.addActionListener(e -> iniciarSesion());
        botonRegistro.addActionListener(e -> new FormularioRegistro());

        botonPanel.add(botonLogin);
        botonPanel.add(botonRegistro);

        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(botonPanel);

        add(mainPanel);
        setVisible(true);
    }


    private void iniciarSesion() {
        try {
            String correo = email.getText();
            String clave = new String(passwd.getPassword());

            if (correo.isEmpty() || clave.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                throw new LoginException("Campos incompletos");
            }

            if (botonToggle.isSelected()) { 
                int idMedico = medicoServicio.login(correo, clave);
                if (idMedico == -1) {
                    FormularioAdministrador admin = new FormularioAdministrador();
                    admin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    admin.setSize(600, 500); 
                    admin.setVisible(true);
                } else {
                    MenuMedico menu = new MenuMedico(idMedico);
                    menu.setVisible(true);
                }
            } else { 
                int idPaciente = pacienteServicio.login(correo, clave);
                MenuPaciente menu = new MenuPaciente(idPaciente);
                menu.setVisible(true);
            }
            this.dispose(); 
        } catch (LoginException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
