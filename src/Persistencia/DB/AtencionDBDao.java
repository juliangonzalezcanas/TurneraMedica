package Persistencia.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import Entidades.Atencion;
import Entidades.Consultorio;
import Entidades.Medico;
import Persistencia.ICrud;

public class AtencionDBDao extends BaseH2 implements ICrud<Atencion> {

    public AtencionDBDao() {
        super();
    }

    @Override
    public void grabar(Atencion a) throws SQLException {
        String sql = "INSERT INTO ATENCION (id_medico, id_consultorio, fecha_inicio, fecha_fin) VALUES (?, ?, ?, ?)";
        try {
            updateDeleteInsertSql(sql, a.getMedico().getId(), a.getConsultorio().getId(), a.getDesde(), a.getHasta());
            super.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Atencion leer(Integer id) throws SQLException {
        String sql = "SELECT * FROM ATENCION WHERE id=?";
        Atencion atencion = null;
        try {
            ResultSet rs = super.selectSql(sql, id);
            if (rs.next()) {
                atencion = new Atencion(rs.getInt(1), buscarMedico(rs.getInt(2)), buscarConsultorio(rs.getInt(3)), rs.getTimestamp(4).toLocalDateTime().toLocalDate(), rs.getTimestamp(5).toLocalDateTime().toLocalDate());
            }
            super.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return atencion;
    }

    private Consultorio buscarConsultorio(int id) {
        String sql = "SELECT * FROM CONSULTORIO WHERE id=?";
        ResultSet rs;
        Consultorio c = null;
        try {
            rs = super.selectSql(sql, id);
            if (rs.first()) {
                c = new Consultorio(rs.getInt("id"), rs.getString("nombre"), rs.getString("direccion"));
            }
            super.cerrarConexion();
            return c;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return c;
        
    }

    @Override
    public List<Atencion> leerTodos() throws SQLException {
        String sql = "SELECT * FROM ATENCION";
        List<Atencion> atenciones = new java.util.ArrayList<>();
        try {
            ResultSet rs = super.selectSql(sql);
            while (rs.next()) {
                Atencion a = new Atencion(rs.getInt(1), buscarMedico(rs.getInt(2)), buscarConsultorio(rs.getInt(3)), rs.getTimestamp(4).toLocalDateTime().toLocalDate(), rs.getTimestamp(5).toLocalDateTime().toLocalDate());
                atenciones.add(a);
            }
            super.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return atenciones;
    }

    @Override
    public void modificar(Atencion t) throws SQLException {
        String sql = "UPDATE ATENCION SET id_medico=?, id_consultorio=?, fecha_inicio=?, fecha_fin=? WHERE id=?";
        try {
            updateDeleteInsertSql(sql, t.getMedico().getId(), t.getConsultorio().getId(), t.getDesde(), t.getHasta(), t.getId());
            super.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void eliminar(Integer id) throws SQLException {
        String sql = "DELETE FROM ATENCION WHERE id=?";
        try {
            updateDeleteInsertSql(sql, id);
            super.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    private Medico buscarMedico(int id) throws SQLException {
        String sql = "select * from MEDICO where id = ?";
        ResultSet rs = super.selectSql(sql, id);
        if (rs.first()) {
            return new Medico(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5), rs.getString(6), rs.getFloat(7), rs.getString(8), rs.getString(9));
        }
		super.cerrarConexion();
        return null;
    }
}
