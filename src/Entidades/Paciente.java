package Entidades;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Paciente extends Usuario{

    public Paciente(Integer id, String nombre, String apellido, Integer dni, String email, String obraSocial, String password) {
        super(id, nombre, apellido, dni, email, obraSocial, password);
        
    }

    public ArrayList<Turno> getTurnosPendientes() {
        ArrayList<Turno> turnosPendientes = new ArrayList<>();
        for (Turno t : super.getTurnos()) {
            if (t.getFecha().isAfter(LocalDateTime.now())) {
                turnosPendientes.add(t);
            }
        }
        return turnosPendientes;
    }
    
}
