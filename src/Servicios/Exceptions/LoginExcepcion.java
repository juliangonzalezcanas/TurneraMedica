package Servicios.Exceptions;

public class LoginExcepcion extends Exception {
    public LoginExcepcion() {
        super("Error al iniciar sesión: Email o contraseña incorrectos");
    }   
}
