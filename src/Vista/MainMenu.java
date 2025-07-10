package Vista;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class MainMenu extends JFrame {

    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem miPaciente;
    private JMenuItem miMedico;
    private JMenuItem miTurno;

    private JPanel contentPanel;
    private CardLayout cardLayout;


    public MainMenu() {

        setVisible(true);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear menú
        menuBar = new JMenuBar();
        menu = new JMenu("Entidades");
        miPaciente = new JMenuItem("Paciente");
        miMedico = new JMenuItem("Medico");
        miTurno = new JMenuItem("Turno");
        
    

        // Construir menú

        menu.add(miPaciente);
        menu.add(miMedico);
        menu.add(miTurno);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Agregar los paneles para cada entidad

        miPaciente.addActionListener(e -> switchPanel("PACIENTE"));
        miMedico.addActionListener(e -> switchPanel("MEDICO"));
        miTurno.addActionListener(e -> switchPanel("TURNO"));

        contentPanel.add(new FormularioPaciente(), "PACIENTE");
        contentPanel.add(new FormularioMedico(), "MEDICO");
        contentPanel.add(new FormularioTurno(), "TURNO");


        add(contentPanel, BorderLayout.CENTER);

        // Mostrar panel inicial
        switchPanel("PACIENTE");



    }

    private void switchPanel(String name) {

        cardLayout.show(contentPanel, name);
    }
    
    
}
