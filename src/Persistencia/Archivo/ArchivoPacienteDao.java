package Persistencia.Archivo;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import Entidades.Paciente;
import Persistencia.ICrud;



public class ArchivoPacienteDao implements ICrud<Paciente> {

	private static final String nombreArchivo = "paciente.txt";
	
	@Override
	public void grabar(Paciente t) throws IOException {
		FileOutputStream fos = new FileOutputStream(String.valueOf(t.getId()).concat(".txt"));
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(t);
		oos.close();
		fos.close();
	}

	@Override
	public Paciente leer(Integer id) throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(String.valueOf(id));
		ObjectInputStream ois = new ObjectInputStream(fis);
		Paciente p = (Paciente) ois.readObject();
		ois.close();
		fis.close();
		return p;

		/*
		FileInputStream fis = new FileInputStream(nombreArchivo);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Paciente p = (Paciente) ois.readObject();
		ois.close();
		fis.close();
		return p;
		*/ 
	}

	@Override
	public List<Paciente> leerTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void modificar(Paciente t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eliminar(Integer id) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'eliminar'");
	}

	
}
