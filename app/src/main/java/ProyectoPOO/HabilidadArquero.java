package ProyectoPOO;

public class HabilidadArquero implements HabilidadEspecial {

    @Override
    public void activar(Personaje jugador) {
        System.out.println("¡El Arquero dispara tres flechas especiales!");
    }
}
