package Vista.Exceptions;

public class IdVacioException extends Exception{
    public IdVacioException() {
        super("El ID no puede estar vacío");
    }
}
