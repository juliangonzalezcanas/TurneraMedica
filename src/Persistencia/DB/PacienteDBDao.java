package Persistencia.DB;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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


	@Override
	public Paciente leer(Integer id) throws IOException, ClassNotFoundException {

		String sql = "select * from PACIENTE where id = ?";

		try {
			ResultSet rs = super.selectSql(sql, id);
			if (rs.first()) {
				Paciente p = new Paciente(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5), rs.getString(6), rs.getString(7));
				super.cerrarConexion();
				return p;
			}
			
		} catch (SQLException e) {
			throw new IOException();
		}
		return null;
	}

	@Override
	public void eliminar(Integer id) throws IOException, ClassNotFoundException {
		String sql = "delete from PACIENTE where id = ?";
		try {
			updateDeleteInsertSql(sql, id);
			super.cerrarConexion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
