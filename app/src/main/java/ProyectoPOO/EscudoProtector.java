package ProyectoPOO;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EscudoProtector extends Item {

    private Texture textura;

    public EscudoProtector(float x, float y) {
        super(x, y, 35f, 40f);

        // Textura azul temporal para representar el escudo
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLUE);
        pixmap.fill();

        textura = new Texture(pixmap);
        pixmap.dispose();
    }

    // Dibujar el escudo mientras no haya sido recogido
    public void render(SpriteBatch batch) {
        if (!recogido) {
            batch.setColor(Color.WHITE);
            batch.draw(textura, x, y, ancho, alto);
        }
    }

    // Activar la protección al recoger el escudo
    @Override
    public void recoger(Personaje jugador) {
        if (!recogido) {
            jugador.activarEscudo();
            recogido = true;
        }
    }

    public boolean estaRecogido() {
        return recogido;
    }
    
    public void dispose() {
        if (textura != null) {
            textura.dispose();
        }
    }
}
