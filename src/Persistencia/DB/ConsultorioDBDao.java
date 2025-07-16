package Persistencia.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Entidades.Consultorio;
import Persistencia.ICrud;

public class ConsultorioDBDao extends BaseH2 implements ICrud<Consultorio> {

    public ConsultorioDBDao() {
        super();
    }

    @Override
    public void modificar(Consultorio c) {
        String sql = "UPDATE CONSULTORIO SET nombre=?, direccion=? WHERE id=?";
        try {
            updateDeleteInsertSql(sql, c.getNombre(), c.getDireccion(), c.getId());
            super.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();    
    
        }
    }

    @Override
    public void grabar(Consultorio t) throws SQLException {
        String sql = "INSERT INTO CONSULTORIO (nombre, direccion) VALUES (?, ?)";
        try {
            updateDeleteInsertSql(sql, t.getNombre(), t.getDireccion());
            super.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Consultorio leer(Integer id) throws SQLException {
        String sql = "SELECT * FROM CONSULTORIO WHERE id=?";
        Consultorio consultorio = null;
        try {
            ResultSet rs = super.selectSql(sql, id);
            if (rs.next()) {
                consultorio = new Consultorio(rs.getInt(1), rs.getString(2), rs.getString(3));
            }
            super.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultorio;
    }

    @Override
    public List<Consultorio> leerTodos() throws SQLException {
        String sql = "SELECT * FROM CONSULTORIO";
        List<Consultorio> consultorios = new ArrayList<>();
        try {
            ResultSet rs = super.selectSql(sql);
            while (rs.next()) {
                consultorios.add(new Consultorio(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
            super.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultorios;
    }

    @Override
    public void eliminar(Integer id) throws SQLException {
        String sql = "DELETE FROM CONSULTORIO WHERE id=?";
        try {
            updateDeleteInsertSql(sql, id);
            super.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }    

}
