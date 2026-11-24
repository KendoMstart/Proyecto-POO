package ProyectoPOO;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CorazonVida extends Item {

    private Texture textura;

    public CorazonVida(float x, float y) {
        super(x, y, 30f, 30f);

        // Crear una textura roja para el objeto
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fill();

        textura = new Texture(pixmap);
        pixmap.dispose();
    }

    // Dibujar el objeto mientras esté disponible
    public void render(SpriteBatch batch) {
        if (!recogido) {
            batch.setColor(Color.WHITE);
            batch.draw(textura, x, y, ancho, alto);
        }
    }

    // Recuperar una vida al recoger el objeto
    @Override
    public void recoger(Personaje jugador) {
        if (!recogido && jugador.getVidas() < 3) {
            jugador.recuperarVida();
            recogido = true;
        }
    }

    // Liberar recursos gráficos
    public void dispose() {
        if (textura != null) {
            textura.dispose();
        }
    }
}
