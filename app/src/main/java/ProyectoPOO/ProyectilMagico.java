package ProyectoPOO;

public class ProyectilMagico {

    private float x;
    private float y;
    private float velocidadX;

    private final float ancho = 25f;
    private final float alto = 25f;

    public ProyectilMagico(float x, float y, boolean haciaDerecha) {
        this.x = x;
        this.y = y;
        this.velocidadX = haciaDerecha ? 500f : -500f;
    }

    public void actualizar(float delta) {
        x += velocidadX * delta;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getAncho() {
        return ancho;
    }

    public float getAlto() {
        return alto;
    }
}