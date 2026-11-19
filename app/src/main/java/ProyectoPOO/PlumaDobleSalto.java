package ProyectoPOO;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlumaDobleSalto extends Item {

    private Texture textura;

    public PlumaDobleSalto(float x, float y) {
        super(x, y, 30f, 40f);

        // Textura morada temporal para representar la pluma
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.MAGENTA);
        pixmap.fill();

        textura = new Texture(pixmap);
        pixmap.dispose();
    }

    // Dibujar la pluma si todavía no fue recogida
    public void render(SpriteBatch batch) {
        if (!recogido) {
            batch.setColor(Color.WHITE);
            batch.draw(textura, x, y, ancho, alto);
        }
    }

    // Activar el doble salto al recoger la pluma
    @Override
    public void recoger(Personaje jugador) {
        if (!recogido) {
            jugador.activarDobleSalto();
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
