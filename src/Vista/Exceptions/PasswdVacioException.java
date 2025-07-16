package Vista.Exceptions;

public class PasswdVacioException extends Exception {
    public PasswdVacioException() {
        super("La contraseña no puede estar vacía.");
    }
    
}
