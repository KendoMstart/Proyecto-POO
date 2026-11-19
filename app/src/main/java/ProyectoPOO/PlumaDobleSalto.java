package ProyectoPOO;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlumaDobleSalto {

    private final float x, y;
    private final float ancho = 30f;
    private final float alto = 40f;

    private Texture textura;
    private boolean recogida = false;

    public PlumaDobleSalto(float x, float y) {
        this.x = x;
        this.y = y;

        // Textura morada temporal para representar la pluma
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.MAGENTA);
        pixmap.fill();

        textura = new Texture(pixmap);
        pixmap.dispose();
    }

    // Dibujar la pluma si todavía no fue recogida
    public void render(SpriteBatch batch) {
        if (!recogida) {
            batch.setColor(Color.WHITE);
            batch.draw(textura, x, y, ancho, alto);
        }
    }

    // Activar el doble salto al recoger la pluma
    public void recoger(Personaje jugador) {
        if (!recogida) {
            jugador.activarDobleSalto();
            recogida = true;
        }
    }

    public boolean estaRecogida() {
        return recogida;
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

    public void dispose() {
        if (textura != null) {
            textura.dispose();
        }
    }
}
