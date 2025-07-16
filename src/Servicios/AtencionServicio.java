package Servicios;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import Entidades.Atencion;
import Entidades.Consultorio;
import Persistencia.ICrud;
import Persistencia.DB.AtencionDBDao;
public class AtencionServicio {

    ICrud<Atencion> persistencia;

    public AtencionServicio() {
        persistencia = new AtencionDBDao();
    }

    public void grabar(Atencion a) {
        try {
            persistencia.grabar(a);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modificar(Atencion a) {
        try {
            persistencia.modificar(a);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Atencion leer(Integer id) {
        try {
            return persistencia.leer(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Atencion> leerTodos() {
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

    public List<Atencion> leerPorMedico(int idMedico) {
        try {
            return ((AtencionDBDao) persistencia).leerPorMedico(idMedico);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Consultorio buscarConsultorioPorMedico(int idMedico, LocalDate fecha) {
        try {
            List<Atencion> atenciones = leerPorMedico(idMedico);
            for (Atencion atencion : atenciones) {
                if ((fecha.isAfter(atencion.getDesde()) || fecha.isEqual(atencion.getDesde())) && (fecha.isBefore(atencion.getHasta()) || fecha.isEqual(atencion.getHasta()))) {
                    return atencion.getConsultorio();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
        
    
}
