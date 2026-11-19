package ProyectoPOO;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BotasVelocidad {

    private final float x, y;
    private final float ancho = 35f;
    private final float alto = 35f;

    private Texture textura;
    private boolean recogidas = false;

    public BotasVelocidad(float x, float y) {
        this.x = x;
        this.y = y;

        // Textura amarilla temporal para representar las botas
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.YELLOW);
        pixmap.fill();

        textura = new Texture(pixmap);
        pixmap.dispose();
    }

    // Dibujar las botas mientras no hayan sido recogidas
    public void render(SpriteBatch batch) {
        if (!recogidas) {
            batch.setColor(Color.WHITE);
            batch.draw(textura, x, y, ancho, alto);
        }
    }

    // Activar el potenciador al recoger las botas
    public void recoger(Personaje jugador) {
        if (!recogidas) {
            jugador.activarBotasVelocidad();
            recogidas = true;
        }
    }

    public boolean estanRecogidas() {
        return recogidas;
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
