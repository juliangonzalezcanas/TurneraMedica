package Persistencia;
import java.io.IOException;
import java.util.List;

public interface ICrud<T> {
	
	public void grabar(T t) throws IOException;
	public T leer(Integer id) throws IOException, ClassNotFoundException;
	public List<T> leerTodos();
	public void modificar(T t);
	public void eliminar(Integer id) throws IOException, ClassNotFoundException;
    
	
	

}
