package Servicios;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import Entidades.Paciente;
import Entidades.Turno;
import Persistencia.ICrud;
import Persistencia.DB.PacienteDBDao;
import Persistencia.DB.TurnoDBDao;
import Servicios.Exceptions.GrabandoPacienteException;

public class TurnoServicio {
    ICrud<Turno> persistencia;

    public TurnoServicio() {
        persistencia = new TurnoDBDao();
    }

    public void setPersistencia(ICrud<Turno> persistencia) {
        this.persistencia = persistencia;
    }

    public void grabar(Turno t) throws IOException, GrabandoPacienteException {
        try{
            persistencia.grabar(t);
        } catch (IOException e) {
            throw new GrabandoPacienteException(); 
        }
    }

    public void modificar(Turno t) {
        persistencia.modificar(t);
    }

    public List<Turno> leerTodos(){
        return persistencia.leerTodos();
    }

    public void eliminar(Integer id) throws IOException, ClassNotFoundException {
        persistencia.eliminar(id);
    }

    public List<Turno> buscarPorMedico(int medicoId) {
        List<Turno> turnos = new ArrayList<>();
        for (Turno turno : persistencia.leerTodos()) {
            if (turno.getMedico().getId() == medicoId) {
                turnos.add(turno);
            }
        }
        return turnos;
    }

    public List<Turno> buscarPorPaciente(int pacienteId) {
        List<Turno> turnos = new ArrayList<>();
        for (Turno turno : persistencia.leerTodos()) {
            if (turno.getPaciente().getId() == pacienteId) {
                turnos.add(turno);
            }
        }
        return turnos;
    }

    public List<LocalDateTime> generarHorariosDisponibles(LocalDate fecha, int medicoId) {
        List<LocalDateTime> disponibles = new ArrayList<>();

        LocalTime horaInicio = LocalTime.of(8, 0);
        LocalTime horaFin = LocalTime.of(16, 0);

        for (LocalTime hora = horaInicio; hora.isBefore(horaFin); hora = hora.plusMinutes(30)) {
            LocalDateTime fechaHora = LocalDateTime.of(fecha, hora);

            if (!estaOcupado(fechaHora, medicoId)) {
                disponibles.add(fechaHora);
            }
        }

        return disponibles;
    }

    public boolean estaOcupado(LocalDateTime fechaHora, int medicoId) {
        return ((TurnoDBDao) persistencia).existeTurno(fechaHora, medicoId);
    }


}
