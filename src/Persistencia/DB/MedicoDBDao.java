package Persistencia.DB;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Entidades.Medico;
import Entidades.Paciente;
import Persistencia.ICrud;

public class MedicoDBDao extends BaseH2 implements ICrud<Medico>{
    
    public MedicoDBDao() {
        super();
    }
    
    @Override
	public void modificar(Medico m) {
		String sql = "update MEDICO set nombre=?, apellido=?, dni=?, mail=?, obra_social=?, precio_consulta=? where id = ? ";
		try {
			updateDeleteInsertSql(sql,m.getNombre(), 
					m.getApellido(), 
					m.getDni(),
					m.getEmail(),
					m.getObraSocial(), 
                    m.getPrecioConsulta(),
					m.getId());
			super.cerrarConexion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void grabar(Medico entity) {

		String sql = "INSERT INTO MEDICO (nombre, apellido, dni, mail, obra_social, precio_cosulta, especialidad, password) VALUES (?,?,?,?,?,?,?,?)";
		try {
			updateDeleteInsertSql(sql, entity.getNombre(), entity.getApellido(), entity.getDni(), entity.getEmail(), entity.getObraSocial(), entity.getPrecioConsulta(), entity.getEspecialidad(), entity.getPassword());
			super.cerrarConexion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Medico> leerTodos() {
		String sql = "select * from MEDICO";
		List<Medico> m = new ArrayList<>();
		try {
			ResultSet rs = super.selectSql(sql);
			

			while (rs.next()) {
				m.add(new Medico(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5), rs.getString(6), rs.getFloat(7), rs.getString(8), rs.getString(9)));
			}
			super.cerrarConexion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return m;
	}

	public int login(String email, String password) {
		String sql = "select id from MEDICO where mail = ? and password = ?";
		int id = -1;
		try {
			ResultSet rs = super.selectSql(sql, email, password);
			if (rs.first()) {
				id = rs.getInt(1);
			}
			super.cerrarConexion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public Medico leer(Integer id) throws IOException, ClassNotFoundException {

		String sql = "select * from MEDICO where id = ?";

		try {
			ResultSet rs = super.selectSql(sql, id);
			if (rs.first()) {
				return new Medico(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5), rs.getString(6), rs.getFloat(7), rs.getString(8), rs.getString(9));
			}
			super.cerrarConexion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void eliminar(Integer id) throws IOException, ClassNotFoundException {
		String sql = "delete from MEDICO where id = ?";
		try {
			updateDeleteInsertSql(sql, id);
			super.cerrarConexion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
