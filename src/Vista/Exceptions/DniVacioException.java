package Vista.Exceptions;

public class DniVacioException extends Exception {
    public DniVacioException() {
        super("El DNI no puede estar vacío");
    }

}
