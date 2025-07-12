package Servicios;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import Entidades.Medico;
import Entidades.Turno;
import Persistencia.ICrud;
import Persistencia.DB.MedicoDBDao;
import Servicios.Exceptions.GrabandoPacienteException;
import Servicios.Exceptions.LoginExcepcion;

public class MedicoServicio {
    ICrud<Medico> persistencia;

    public MedicoServicio() {
        persistencia = new MedicoDBDao();
    }

    public void setPersistencia(ICrud<Medico> persistencia) {
        this.persistencia = persistencia;
    }

    public void grabar(Medico m) throws IOException, GrabandoPacienteException {
        try{
            String hashedPasswd = BCrypt.hashpw(m.getPassword(), BCrypt.gensalt());
            m.setPassword(hashedPasswd);
            persistencia.grabar(m);
            
        } catch (IOException e) {
            throw new GrabandoPacienteException(); 
        }
    }
    public int login(){
        
        return 0;
    }
    
    public int login(String email, String password) throws LoginExcepcion {

        Object[] info = ((MedicoDBDao) persistencia).login(email);
        int id = (int) info[0];
        if (!BCrypt.checkpw(password, (String) info[1])) {
            throw new LoginExcepcion();
        }
        
        return id;
    }

    public void modificar(Medico m) {
        String hashedPasswd = BCrypt.hashpw(m.getPassword(), BCrypt.gensalt());
        m.setPassword(hashedPasswd);
        persistencia.modificar(m);
    }

    public List<Medico> leerTodos(){
        return persistencia.leerTodos();
    }

    public Medico leerPorId(Integer id) throws ClassNotFoundException, IOException {
        return persistencia.leer(id);
    }

    public void eliminar(Integer id) throws IOException, ClassNotFoundException {
        persistencia.eliminar(id);
    }

    public List<Turno> buscarTurnosPorFecha(List<Turno> turnos, LocalDate f) {
        List<Turno> turnosFiltrados = new ArrayList<>();
        for (Turno turno : turnos) {
            
            if (turno.getFecha().toLocalDate().equals(f)) {
                turnosFiltrados.add(turno);
            }
        }
        return turnosFiltrados;
    }

    public Float calcularGanancia(LocalDateTime fecha1, LocalDateTime fecha2, List<Turno> turnos) {
        Float ganancia = 0f;
        for (Turno t : turnos) {
            if (t.getFecha().isAfter(fecha1) && t.getFecha().isBefore(fecha2)) {
                ganancia += t.getPrecioConsulta();
            }
        }
        return ganancia;

    }

 
}
