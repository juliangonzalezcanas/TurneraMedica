package Servicios;

import java.sql.SQLException;
import java.util.List;

import Entidades.Consultorio;
import Persistencia.ICrud;
import Persistencia.DB.ConsultorioDBDao;

public class ConsultorioServicio {
    ICrud<Consultorio> persistencia;

    public ConsultorioServicio() {
        persistencia = new ConsultorioDBDao();
    }

    public void grabar(Consultorio c) {
        try {
            persistencia.grabar(c);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modificar(Consultorio c) {
        try {
            persistencia.modificar(c);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Consultorio leer(Integer id) {
        try {
            return persistencia.leer(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;    
    }   

    public List<Consultorio> leerTodos() {
        try {
            return persistencia.leerTodos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void eliminar(Integer id) {
        try {
            persistencia.eliminar(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ICrud<Consultorio> getPersistencia() {
        return persistencia;
    }

    public void setPersistencia(ICrud<Consultorio> persistencia) {
        this.persistencia = persistencia;
    }
}
