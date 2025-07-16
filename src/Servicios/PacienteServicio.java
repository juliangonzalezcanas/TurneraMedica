package Servicios;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import Entidades.Paciente;
import Entidades.Turno;
import Persistencia.ICrud;
import Persistencia.DB.MedicoDBDao;
import Persistencia.DB.PacienteDBDao;
import Servicios.Exceptions.EliminandoPacienteException;
import Servicios.Exceptions.GrabandoPacienteException;
import Servicios.Exceptions.LoginException;
import Servicios.Exceptions.ModificandoPacienteException;

public class PacienteServicio {
    ICrud<Paciente> persistencia;

    public PacienteServicio() {
        persistencia = new PacienteDBDao();
    }

    public void setPersistencia(ICrud<Paciente> persistencia) {
        this.persistencia = persistencia;
    }

    public void grabar(Paciente p) throws GrabandoPacienteException {
        try{
            String hashedPasswd = BCrypt.hashpw(p.getPassword(), BCrypt.gensalt());
            p.setPassword(hashedPasswd);
            persistencia.grabar(p);
        } catch (SQLException e) {
            throw new GrabandoPacienteException(); 
        }
    }

    public void modificar(Paciente p) throws ModificandoPacienteException {
        try {
            String hashedPasswd = BCrypt.hashpw(p.getPassword(), BCrypt.gensalt());
            p.setPassword(hashedPasswd);
            persistencia.modificar(p);
        } catch (Exception e) {
            throw new ModificandoPacienteException();
        }
        
    }

    public int login(String email, String password) throws LoginException {
        Object[] info;
        Integer id = null;
        try {
            info = ((PacienteDBDao) persistencia).login(email);
            BCrypt.checkpw(password, (String) info[1]);
            id = (int) info[0];
        } catch (SQLException | NullPointerException e) {
            throw new LoginException();
        }
        
        return id;
    }

    public List<Paciente> leerTodos(){
        try {
            return persistencia.leerTodos();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void eliminar(Integer id) throws EliminandoPacienteException {
        try {
            persistencia.eliminar(id);
        } catch (Exception e) {
            throw new EliminandoPacienteException();
        }
        
        
    }

    public Paciente leer(Integer id) throws IOException, ClassNotFoundException {
        try {
            return persistencia.leer(id);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
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
