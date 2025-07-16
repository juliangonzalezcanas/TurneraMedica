package Vista;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.*;

public class MenuMedico extends JFrame {

    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem miMedico;
    private JMenuItem miTurno;
    private JMenuItem miAtencion;

    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MenuMedico(int idMedico) {
        setTitle("Menú Médico");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        menuBar = new JMenuBar();
        menu = new JMenu("Menú");
        miMedico = new JMenuItem("Datos del Médico");
        miTurno = new JMenuItem("Turnos");
        miAtencion = new JMenuItem("Atencion");

        menu.add(miMedico);
        menu.add(miTurno);
        menu.add(miAtencion);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(new FormularioMedico(idMedico), "MEDICO");
        contentPanel.add(new FormularioTurno(false, idMedico), "TURNO");
        contentPanel.add(new FormularioAtencion(idMedico), "ATENCION");

        miMedico.addActionListener(e -> switchPanel("MEDICO"));
        miTurno.addActionListener(e -> switchPanel("TURNO"));
        miAtencion.addActionListener(e -> switchPanel("ATENCION"));

        add(contentPanel, BorderLayout.CENTER);
        switchPanel("MEDICO");
        setVisible(true);
    }

    private void switchPanel(String name) {
        cardLayout.show(contentPanel, name);
    }
}
