package ProyectoPOO;

public class DashEvasivo {

    private final float distancia = 120f;

    public float calcularDestino(float x, boolean haciaDerecha) {
        if (haciaDerecha) {
            return x + distancia;
        } else {
            return x - distancia;
        }
    }
}