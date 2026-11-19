package ProyectoPOO;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BotasVelocidad extends Item {

    private Texture textura;


    public BotasVelocidad(float x, float y) {
         super(x, y, 35f, 35f);


        // Textura amarilla temporal para representar las botas
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.YELLOW);
        pixmap.fill();

        textura = new Texture(pixmap);
        pixmap.dispose();
    }

    // Dibujar las botas si no han sido recogidas
    public void render(SpriteBatch batch) {
        if (!recogido) {
            batch.setColor(Color.WHITE);
            batch.draw(textura, x, y, ancho, alto);
        }
    }

    // Activar las botas al recogerlas
    @Override
    public void recoger(Personaje jugador) {
        if (!recogido) {
            jugador.activarBotasVelocidad();
            recogido = true;
        }
    }

    public boolean estanRecogidas() {
        return recogido;
    }

    public void dispose() {
        if (textura != null) {
            textura.dispose();
        }
    }
}
