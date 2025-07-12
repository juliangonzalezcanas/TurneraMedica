package Vista;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.*;

public class MenuPaciente extends JFrame {

    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem miPaciente;
    private JMenuItem miTurno;

    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MenuPaciente(int idPaciente) {
        setTitle("Menú Paciente");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        menuBar = new JMenuBar();
        menu = new JMenu("Menú");
        miPaciente = new JMenuItem("Datos del Paciente");
        miTurno = new JMenuItem("Turnos");

        menu.add(miPaciente);
        menu.add(miTurno);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // FormularioPaciente con ID para prellenar datos si querés
        contentPanel.add(new FormularioPaciente(idPaciente), "PACIENTE");
        contentPanel.add(new FormularioTurno(true, idPaciente), "TURNO");

        miPaciente.addActionListener(e -> switchPanel("PACIENTE"));
        miTurno.addActionListener(e -> switchPanel("TURNO"));

        add(contentPanel, BorderLayout.CENTER);
        switchPanel("PACIENTE");
        setVisible(true);
    }

    private void switchPanel(String name) {
        cardLayout.show(contentPanel, name);
    }
}
