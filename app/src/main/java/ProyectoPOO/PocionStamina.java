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

    // Dibujar la poción mientras no haya sido recogida
    public void render(SpriteBatch batch) {
        if (!recogido) {
            batch.draw(textura, x, y, ancho, alto);
        }
    }

    // Recuperar stamina al recoger la poción
    @Override
    public void recoger(Personaje jugador) {
        if (!recogido) {
            jugador.recuperarStamina(energiaRecuperada);
            recogido = true;
        }
    }

    // Mantener este método para PantallaJuego.java
    public boolean estaRecogida() {
        return recogido;
    }

    // Liberar la textura al cerrar el juego
    public void dispose() {
        if (textura != null) {
            textura.dispose();
        }
    }

}
