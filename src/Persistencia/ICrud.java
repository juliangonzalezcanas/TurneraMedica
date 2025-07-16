package Persistencia;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface ICrud<T> {
	
	public void grabar(T t) throws SQLException;
	public T leer(Integer id) throws SQLException;
	public List<T> leerTodos() throws SQLException;
	public void modificar(T t) throws SQLException;
	public void eliminar(Integer id) throws SQLException;
    
	
	

}
