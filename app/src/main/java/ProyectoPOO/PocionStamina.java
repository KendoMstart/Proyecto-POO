package ProyectoPOO;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PocionStamina extends Item {

    private final float energiaRecuperada = 50f;
    private Texture textura;

    public PocionStamina(float x, float y) {
        super(x, y, 30f, 40f);

        // Textura azul temporal para representar la poción
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.CYAN);
        pixmap.fill();

        textura = new Texture(pixmap);
        pixmap.dispose();
    }

    // Dibuja la poción mientras no haya sido recogida
    public void render(SpriteBatch batch) {
        if (!recogido) {
            batch.draw(textura, x, y, ancho, alto);
        }
    }

    // Recupera energía y marca la poción como recogida
    @Override
    public void recoger(Personaje jugador) {
        if (!recogido) {
            jugador.recuperarStamina(energiaRecuperada);
            recogido = true;
        }
    }

    public boolean estaRecogida() {
        return recogido;
    }

    public void dispose() {
        if (textura != null) {
            textura.dispose();
        }
    }
}
