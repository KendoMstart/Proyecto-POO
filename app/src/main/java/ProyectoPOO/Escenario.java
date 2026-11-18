package ProyectoPOO;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Clase base de todos los escenarios (herencia + polimorfismo).
 * Cada escenario define su cielo, su piso y su decoración.
 */
public abstract class Escenario {
    protected final String nombre;
    protected final Color colorCielo;
    protected final Color colorPiso;
    protected final float alturaPiso;

    protected Escenario(String nombre, String cielo, String piso, float alturaPiso) {
        this.nombre = nombre;
        this.colorCielo = Color.valueOf(cielo);
        this.colorPiso = Color.valueOf(piso);
        this.alturaPiso = alturaPiso;
    }

    public String getNombre() { return nombre; }
    public Color getColorCielo() { return colorCielo; }
    public float getAlturaPiso() { return alturaPiso; }

    /** Se llama con el ShapeRenderer ya iniciado en modo Filled. */
    public final void dibujar(ShapeRenderer sr, float ancho, float alto) {
        dibujarFondo(sr, ancho, alto);          // sol, colinas, montañas...
        sr.setColor(colorPiso);
        sr.rect(0, 0, ancho, alturaPiso);       // piso
        dibujarDecoracion(sr, ancho, alto);     // arbustos, cactus, árboles...
    }

    protected abstract void dibujarFondo(ShapeRenderer sr, float ancho, float alto);
    protected abstract void dibujarDecoracion(ShapeRenderer sr, float ancho, float alto);

    /** Nube hecha con círculos, reutilizable por los escenarios. */
    protected void nube(ShapeRenderer sr, float x, float y, float escala) {
        sr.circle(x, y, 28 * escala);
        sr.circle(x + 30 * escala, y + 12 * escala, 34 * escala);
        sr.circle(x + 62 * escala, y, 28 * escala);
        sr.rect(x, y - 28 * escala, 62 * escala, 28 * escala);
    }
}
