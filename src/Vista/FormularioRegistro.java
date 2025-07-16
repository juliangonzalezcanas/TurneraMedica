package Vista;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import Servicios.PacienteServicio;
import Servicios.Exceptions.GrabandoMedicoException;
import Servicios.Exceptions.GrabandoPacienteException;
import Vista.Exceptions.ApellidoVacioException;
import Vista.Exceptions.DniVacioException;
import Vista.Exceptions.EmailVacioException;
import Vista.Exceptions.EspecialidadVaciaException;
import Vista.Exceptions.IdVacioException;
import Vista.Exceptions.NombreVacioException;
import Vista.Exceptions.ObraSocialVaciaException;
import Vista.Exceptions.PasswdVacioException;
import Vista.Exceptions.PrecioConsultaVacioException;
import Servicios.MedicoServicio;


import Entidades.Medico;
import Entidades.Paciente;

public class FormularioRegistro extends JFrame {

    private JTextField nombre, apellido, dni, mail, obraSocial, especialidad, precioConsulta, password;
    private JToggleButton toggleTipo;
    private JLabel labelEspecialidad, labelPrecio;



    public FormularioRegistro() {
        setTitle("Registro");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(10, 2));

        toggleTipo = new JToggleButton("Paciente / Médico");
        nombre = new JTextField();
        apellido = new JTextField();
        dni = new JTextField();
        mail = new JTextField();
        obraSocial = new JTextField();
        password = new JTextField();
        especialidad = new JTextField(); // solo médico
        precioConsulta = new JTextField(); // solo médico
        labelEspecialidad = new JLabel("Especialidad:");
        labelPrecio = new JLabel("Precio Consulta:");


        add(new JLabel("Nombre:")); add(nombre);
        add(new JLabel("Apellido:")); add(apellido);
        add(new JLabel("DNI:")); add(dni);
        add(new JLabel("Email:")); add(mail);
        add(new JLabel("Obra Social:")); add(obraSocial);
        add(new JLabel("Contraseña:")); add(password);
        add(labelEspecialidad); add(especialidad);
        add(labelPrecio); add(precioConsulta);

        add(new JLabel("Tipo:"));
        add(toggleTipo);
        actualizarVisibilidadCampos();
        toggleTipo.addActionListener(e -> actualizarVisibilidadCampos());


        JButton registrar = new JButton("Registrar");
        registrar.addActionListener(e -> registrarUsuario());
        add(new JLabel());
        add(registrar);

        setVisible(true);
    }

    private void actualizarVisibilidadCampos() {
        boolean esMedico = toggleTipo.isSelected();

        labelEspecialidad.setVisible(esMedico);
        especialidad.setVisible(esMedico);
        labelPrecio.setVisible(esMedico);
        precioConsulta.setVisible(esMedico);

        this.revalidate();
        this.repaint();
    }


    private void registrarUsuario() {
        try {

            if (toggleTipo.isSelected()) {
                validarCampos();
                MedicoServicio medicoServicio = new MedicoServicio();
                medicoServicio.grabar(new Medico(nombre.getText(), apellido.getText(), Integer.valueOf(dni.getText()), mail.getText(), obraSocial.getText(),
                    Float.parseFloat(precioConsulta.getText()), especialidad.getText(), password.getText()
                ));
                JOptionPane.showMessageDialog(this, "Médico registrado correctamente.");
            } else {
                validarCampos();
                PacienteServicio pacienteServicio = new PacienteServicio();
                pacienteServicio.grabar(new Paciente(nombre.getText(), apellido.getText(), Integer.valueOf(dni.getText()), mail.getText(), obraSocial.getText(),
                    password.getText())
                );
                JOptionPane.showMessageDialog(this, "Paciente registrado correctamente.");
            }

            this.dispose();

        } catch (NombreVacioException | ApellidoVacioException |
                 DniVacioException | EmailVacioException |
                 ObraSocialVaciaException | IdVacioException |
                 PasswdVacioException | EspecialidadVaciaException |
                 PrecioConsultaVacioException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }catch (GrabandoMedicoException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (GrabandoPacienteException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void validarCampos() throws NombreVacioException, ApellidoVacioException,
            DniVacioException, EmailVacioException, ObraSocialVaciaException, IdVacioException, PasswdVacioException, EspecialidadVaciaException, PrecioConsultaVacioException {

        if (nombre.getText().isEmpty()) throw new NombreVacioException();
        if (apellido.getText().isEmpty()) throw new ApellidoVacioException();
        if (dni.getText().isEmpty()) throw new DniVacioException();
        if (mail.getText().isEmpty()) throw new EmailVacioException();
        if (obraSocial.getText().isEmpty()) throw new ObraSocialVaciaException();
        if (password.getText().isEmpty()) throw new PasswdVacioException();
        if (toggleTipo.isSelected()) { // Médico
            if (especialidad.getText().isEmpty()) throw new EspecialidadVaciaException();
            if (precioConsulta.getText().isEmpty()) throw new PrecioConsultaVacioException();
        } 
    }
}
