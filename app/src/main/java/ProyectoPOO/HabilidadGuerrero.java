package ProyectoPOO;

public class HabilidadGuerrero implements HabilidadEspecial {

    @Override
    public void activar(Personaje jugador) {
        jugador.activarEscudo();
        System.out.println("¡El Guerrero activa su defensa especial!");
    }
}