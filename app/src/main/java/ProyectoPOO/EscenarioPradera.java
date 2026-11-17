package ProyectoPOO;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/** Escenario 1: pradera con colinas, nubes y tuberías (estilo Mario). */
public class EscenarioPradera extends Escenario {

    public EscenarioPradera() {
        super("Pradera", "87ceeb", "2ecc71", 100f);
    }

    @Override
    protected void dibujarFondo(ShapeRenderer sr, float w, float h) {
        // Sol
        sr.setColor(Color.valueOf("f9e04b"));
        sr.circle(w * 0.88f, h * 0.86f, 55);

        // Nubes
        sr.setColor(Color.WHITE);
        nube(sr, w * 0.15f, h * 0.80f, 1.2f);
        nube(sr, w * 0.50f, h * 0.88f, 1.0f);
        nube(sr, w * 0.72f, h * 0.70f, 1.4f);

        // Colinas (la mitad inferior queda tapada por el piso)
        sr.setColor(Color.valueOf("58d68d"));
        sr.circle(w * 0.25f, alturaPiso, 190);
        sr.circle(w * 0.78f, alturaPiso, 240);
        sr.setColor(Color.valueOf("45b974"));
        sr.circle(w * 0.52f, alturaPiso, 130);
    }

    @Override
    protected void dibujarDecoracion(ShapeRenderer sr, float w, float h) {
        // Franja de pasto oscuro sobre el piso
        sr.setColor(Color.valueOf("27ae60"));
        sr.rect(0, alturaPiso - 14, w, 14);

        // Arbustos
        sr.setColor(Color.valueOf("1e8449"));
        arbusto(sr, w * 0.20f, alturaPiso);
        arbusto(sr, w * 0.62f, alturaPiso);

        // Tuberías
        tuberia(sr, w * 0.40f, alturaPiso, 90);
        tuberia(sr, w * 0.85f, alturaPiso, 130);
    }

    private void arbusto(ShapeRenderer sr, float x, float y) {
        sr.circle(x, y, 22);
        sr.circle(x + 26, y + 6, 28);
        sr.circle(x + 54, y, 22);
    }

    private void tuberia(ShapeRenderer sr, float x, float y, float alto) {
        sr.setColor(Color.valueOf("229954"));
        sr.rect(x, y, 64, alto);
        sr.setColor(Color.valueOf("1e8449"));
        sr.rect(x - 6, y + alto - 24, 76, 24);
    }
}
