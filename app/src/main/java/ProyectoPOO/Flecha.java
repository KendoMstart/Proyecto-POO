package ProyectoPOO;

public class Flecha {

    private float x;
    private float y;
    private float velocidadX;
    private float velocidadY;

    private final float ancho = 30f;
    private final float alto = 8f;

    public Flecha(float x, float y, boolean haciaDerecha, float velocidadVertical) {
        this.x = x;
        this.y = y;
        this.velocidadX = haciaDerecha ? 650f : -650f;
        this.velocidadY = velocidadVertical;
    }

    public void actualizar(float delta) {
        x += velocidadX * delta;
        y += velocidadY * delta;
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