package Servicios;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import Entidades.Medico;
import Entidades.Turno;
import Persistencia.ICrud;
import Persistencia.DB.MedicoDBDao;
import Servicios.Exceptions.GrabandoMedicoException;
import Servicios.Exceptions.GrabandoPacienteException;
import Servicios.Exceptions.LoginException;

public class MedicoServicio {
    ICrud<Medico> persistencia;

    public MedicoServicio() {
        persistencia = new MedicoDBDao();
    }

    public void setPersistencia(ICrud<Medico> persistencia) {
        this.persistencia = persistencia;
    }

    public void grabar(Medico m) throws GrabandoMedicoException {
        try{
            String hashedPasswd = BCrypt.hashpw(m.getPassword(), BCrypt.gensalt());
            m.setPassword(hashedPasswd);
            persistencia.grabar(m);
            
        } catch (SQLException e) {
            throw new GrabandoMedicoException();
        }
    }
    public int login(){
        
        return 0;
    }
    
    public int login(String email, String password) throws LoginException {
        Object[] info;
        Integer id = null;
        try {
            info = ((MedicoDBDao) persistencia).login(email);
            BCrypt.checkpw(password, (String) info[1]);
            id = (int) info[0];
        } catch (SQLException | NullPointerException e) {
            throw new LoginException();
        }
        
        return id;
    }

    public void modificar(Medico m) throws SQLException {
        String hashedPasswd = BCrypt.hashpw(m.getPassword(), BCrypt.gensalt());
        m.setPassword(hashedPasswd);
        try {
            persistencia.modificar(m);
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    public List<Medico> leerTodos(){
        try {
            return persistencia.leerTodos();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public Medico leerPorId(Integer id) throws SQLException  {
        try {
            return persistencia.leer(id);
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    public void eliminar(Integer id) throws SQLException {
        try {
            persistencia.eliminar(id);
        } catch (SQLException e) {
            throw new SQLException();
        }
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
