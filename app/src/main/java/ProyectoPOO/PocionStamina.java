package ProyectoPOO;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PocionStamina {

    private float x, y;
    private final float ancho = 30f;
    private final float alto = 40f;
    private final float energiaRecuperada = 50f;

    private Texture textura;
    private boolean recogida = false;

    public PocionStamina(float x, float y) {
        this.x = x;
        this.y = y;

        // Textura azul temporal para representar la poción
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.CYAN);
        pixmap.fill();

        textura = new Texture(pixmap);
        pixmap.dispose();
    }

    // Dibuja la poción mientras no haya sido recogida
    public void render(SpriteBatch batch) {
        if (!recogida) {
            batch.draw(textura, x, y, ancho, alto);
        }
    }

    // Recupera energía y marca la poción como recogida
    public void recoger(Personaje jugador) {
        if (!recogida) {
            jugador.recuperarStamina(energiaRecuperada);
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
