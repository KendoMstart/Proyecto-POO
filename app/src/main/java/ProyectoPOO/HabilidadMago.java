package ProyectoPOO;

public class HabilidadMago implements HabilidadEspecial {

    @Override
    public void activar(Personaje jugador) {
        System.out.println("¡El Mago lanza su hechizo especial!");
    }
}