package Persistencia.Archivo;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.List;

import Entidades.Paciente;
import Persistencia.ICrud;



public class ArchivoPacienteDao implements ICrud<Paciente> {

	private static final String nombreArchivo = "paciente.txt";

	@Override
	public void grabar(Paciente t) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'grabar'");
	}

	@Override
	public Paciente leer(Integer id) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'leer'");
	}

	@Override
	public List<Paciente> leerTodos() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'leerTodos'");
	}

	@Override
	public void modificar(Paciente t) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'modificar'");
	}

	@Override
	public void eliminar(Integer id) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'eliminar'");
	}
	
	

	
}
