package Persistencia.DB;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import Entidades.Medico;
import Entidades.Paciente;
import Entidades.Turno;
import Persistencia.ICrud;

public class TurnoDBDao extends BaseH2 implements ICrud<Turno>{

    public TurnoDBDao() {
		super();
	}

	@Override
	public void modificar(Turno t) {
		String sql = "update TURNO set fecha=?, valor_final=?, mdeico_id=?, paciente_id=? where id = ? ";
		try {
			updateDeleteInsertSql(sql,t.getFecha(), 
					t.getPrecioConsulta(), 
					t.getMedico().getId(),
					t.getPaciente().getId()
                );
			super.cerrarConexion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void grabar(Turno entity) {

		String sql = "INSERT INTO TURNO (fecha, valor_final, medico_id, paciente_id) VALUES (?,?,?,?)";
		try {
			updateDeleteInsertSql(sql, entity.getFecha(), entity.getPrecioConsulta(), entity.getMedico().getId(), entity.getPaciente().getId());
			super.cerrarConexion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Turno> leerTodos() {
		String sql = "select * from TURNO";
		List<Turno> t = new ArrayList<>();
		try {
			ResultSet rs = super.selectSql(sql);
			while (rs.next()) {
				t.add(new Turno(rs.getInt(1), rs.getTimestamp(2).toLocalDateTime(), buscarMedico(rs.getInt(4)), buscarPaciente(rs.getInt(5))));
			}
			super.cerrarConexion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return t;
	}


	@Override
	public Turno leer(Integer id) throws IOException, ClassNotFoundException {

		String sql = "select * from TURNO where id = ?";

		try {
			ResultSet rs = super.selectSql(sql, id);
			if (rs.first()) {
				return new Turno(rs.getInt(1), LocalDateTime.ofInstant(rs.getDate(2).toInstant(), ZoneId.systemDefault()), buscarMedico(rs.getInt(3)), buscarPaciente(rs.getInt(4)));
			}
			super.cerrarConexion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void eliminar(Integer id) throws IOException, ClassNotFoundException {
		String sql = "delete from TURNO where id = ?";
		try {
			updateDeleteInsertSql(sql, id);
			super.cerrarConexion();
		} catch (SQLException e) {
			throw new IOException();
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

    private Paciente buscarPaciente(int id) throws SQLException {
        String sql = "select * from PACIENTE where id = ?";
        ResultSet rs = super.selectSql(sql, id);
        if (rs.first()) {
			Paciente p = new Paciente(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5), rs.getString(6), rs.getString(7));
			super.cerrarConexion();
            return p;
        }
		super.cerrarConexion();
        return null;
    }

	public boolean existeTurno(LocalDateTime fechaHora, int medicoId) {
		String sql = "select * from TURNO where fecha = ? and medico_id = ?";
		Boolean existe = false;
		try {
			ResultSet rs = super.selectSql(sql, fechaHora, medicoId);
			if (rs.first()) {
				existe = true;
			}
			super.cerrarConexion();
			return existe;
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		return false;
	}
    
}
