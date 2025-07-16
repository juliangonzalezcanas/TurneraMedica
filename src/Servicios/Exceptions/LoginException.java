package Servicios.Exceptions;

public class LoginException extends Exception {
    public LoginException() {
        super("Error al iniciar sesión: Email o contraseña incorrectos");
    }
    public LoginException(String message) {
        super(message);
    }
}
