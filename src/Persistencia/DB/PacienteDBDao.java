package Persistencia.DB;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import Entidades.Paciente;
import Persistencia.ICrud;

public class PacienteDBDao extends BaseH2 implements ICrud<Paciente> {

	public PacienteDBDao() {
		super();
	}

	@Override
	public void modificar(Paciente p) {
		String sql = "update PACIENTE set nombre=?, apellido=?, dni=?, mail=?, obra_social=? where id = ? ";
		try {
			updateDeleteInsertSql(sql,p.getNombre(), 
					p.getApellido(), 
					p.getDni(),
					p.getEmail(),
					p.getObraSocial(), 
					p.getId());
			super.cerrarConexion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void grabar(Paciente entity) {

		String sql = "INSERT INTO PACIENTE (nombre, apellido, dni, mail, obra_social, password) VALUES (?,?,?,?,?,?)";
		
		try {
			updateDeleteInsertSql(sql, entity.getNombre(), entity.getApellido(), entity.getDni(), entity.getEmail(), entity.getObraSocial(), entity.getPassword());
			super.cerrarConexion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Paciente> leerTodos() {
		String sql = "select * from PACIENTE";
		List<Paciente> p = new ArrayList<>();
		try {
			ResultSet rs = super.selectSql(sql);
			while (rs.next()) {
				p.add(new Paciente(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5), rs.getString(6), rs.getString(7)));
			}
			super.cerrarConexion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return p;
	}


	public Object[] login(String email) {
		String sql = "select id, password from PACIENTE where mail = ?";
		int id = -1;
		String password = null;
		try {
			ResultSet rs = super.selectSql(sql, email);
			if (rs.first()) {
				id = rs.getInt(1);
				password = rs.getString(2);
			}
			super.cerrarConexion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return  new Object[] {id, password};
	}

	@Override
	public Paciente leer(Integer id) throws SQLException {

		String sql = "select * from PACIENTE where id = ?";

		try {
			ResultSet rs = super.selectSql(sql, id);
			if (rs.first()) {
				Paciente p = new Paciente(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5), rs.getString(6), rs.getString(7));
				super.cerrarConexion();
				return p;
			}
			super.cerrarConexion();
			
		} catch (SQLException e) {
			throw new SQLException("Error al leer el paciente con ID: " + id, e);
		} 
		return null;
	}

	@Override
	public void eliminar(Integer id) throws SQLException {
		String sql = "delete from PACIENTE where id = ?";
		try {
			updateDeleteInsertSql(sql, id);
			super.cerrarConexion();
		} catch (SQLException e) {
			throw new SQLException("Error al eliminar el paciente con ID: " + id, e);
		}
		
	}

}
