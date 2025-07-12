package Servicios;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import Entidades.Paciente;
import Entidades.Turno;
import Persistencia.ICrud;
import Persistencia.DB.PacienteDBDao;
import Servicios.Exceptions.EliminandoPacienteException;
import Servicios.Exceptions.GrabandoPacienteException;
import Servicios.Exceptions.LoginExcepcion;
import Servicios.Exceptions.ModificandoPacienteException;

public class PacienteServicio {
    ICrud<Paciente> persistencia;

    public PacienteServicio() {
        persistencia = new PacienteDBDao();
    }

    public void setPersistencia(ICrud<Paciente> persistencia) {
        this.persistencia = persistencia;
    }

    public void grabar(Paciente p) throws IOException, GrabandoPacienteException {
        try{
            String hashedPasswd = BCrypt.hashpw(p.getPassword(), BCrypt.gensalt());
            p.setPassword(hashedPasswd);
            persistencia.grabar(p);
        } catch (IOException e) {
            throw new GrabandoPacienteException(); 
        }
    }

    public void modificar(Paciente p) throws ModificandoPacienteException {
        try {
            persistencia.modificar(p);
        } catch (Exception e) {
            throw new ModificandoPacienteException();
        }
        
    }

    public int login(String email, String password) throws LoginExcepcion {
        Object[] info = ((PacienteDBDao) persistencia).login(email);
        int id = (int) info[0];
        if (!BCrypt.checkpw(password, (String) info[1])) {
            throw new LoginExcepcion();
        }
        
        return id;
    }

    public List<Paciente> leerTodos(){
        return persistencia.leerTodos();
    }

    public void eliminar(Integer id) throws EliminandoPacienteException {
        try {
            persistencia.eliminar(id);
        } catch (Exception e) {
            throw new EliminandoPacienteException();
        }
        
        
    }

    public Paciente leer(Integer id) throws IOException, ClassNotFoundException {
        return persistencia.leer(id);
    }

    public List<Turno> buscarTurnosPorFecha(List<Turno> turnos, LocalDate f) {
        List<Turno> turnosFiltrados = new ArrayList<>();
        for (Turno turno : turnos) {
            if (turno.getFecha().toLocalDate().isAfter(f)) {
                turnosFiltrados.add(turno);
            }
        }
        return turnosFiltrados;
    }
}
