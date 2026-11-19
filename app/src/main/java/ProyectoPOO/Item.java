package ProyectoPOO;

public abstract class Item {

    // Características comunes de los objetos
    protected float x, y;
    protected float ancho, alto;
    protected boolean recogido = false;

    // Constructor de la clase padre
    public Item(float x, float y, float ancho, float alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
    }

    // Cada objeto tendrá su propio efecto
    public abstract void recoger(Personaje jugador);

    // Métodos comunes
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

    public boolean estaRecogido() {
        return recogido;
    }
}
