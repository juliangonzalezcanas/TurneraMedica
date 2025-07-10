import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import Entidades.Medico;
import Entidades.Usuario;

public class Sistema {

    private ArrayList<Usuario> usuarios;

    public Sistema() {
        this.usuarios = new ArrayList<>();
    }

    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public void eliminarUsuario(Usuario usuario) {
        usuarios.remove(usuario);
    }

    public HashMap<Medico, Float> calcularGananciaMedicos(LocalDateTime d1, LocalDateTime d2) {
        HashMap<Medico, Float> ganancias = new HashMap<>();
        for (Usuario u : usuarios) {
            if (u instanceof Medico) {
                Medico medico = (Medico) u;
                Float total = medico.calcularTotalConsultas(d1, d2);
                ganancias.put(medico, total);
            }
        }
        return ganancias;
    }
}
