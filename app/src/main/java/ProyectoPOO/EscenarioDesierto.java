package ProyectoPOO;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/** Escenario 2: desierto con dunas, sol grande y cactus. */
public class EscenarioDesierto extends Escenario {

    public EscenarioDesierto() {
        super("Desierto", "f8c471", "e0b872", 100f);
    }

    @Override
    protected void dibujarFondo(ShapeRenderer sr, float w, float h) {
        // Sol grande
        sr.setColor(Color.valueOf("e74c3c"));
        sr.circle(w * 0.75f, h * 0.75f, 90);
        sr.setColor(Color.valueOf("f5b041"));
        sr.circle(w * 0.75f, h * 0.75f, 70);

        // Dunas
        sr.setColor(Color.valueOf("d4a35a"));
        sr.circle(w * 0.18f, alturaPiso - 20, 210);
        sr.circle(w * 0.70f, alturaPiso - 30, 260);
        sr.setColor(Color.valueOf("c8923f"));
        sr.circle(w * 0.45f, alturaPiso - 10, 150);
    }

    @Override
    protected void dibujarDecoracion(ShapeRenderer sr, float w, float h) {
        sr.setColor(Color.valueOf("c8923f"));
        sr.rect(0, alturaPiso - 12, w, 12);

        cactus(sr, w * 0.25f, alturaPiso, 120);
        cactus(sr, w * 0.55f, alturaPiso, 90);
        cactus(sr, w * 0.90f, alturaPiso, 140);

        // Rocas
        sr.setColor(Color.valueOf("7e5f3a"));
        sr.circle(w * 0.40f, alturaPiso, 24);
        sr.circle(w * 0.40f + 26, alturaPiso, 16);
    }

    private void cactus(ShapeRenderer sr, float x, float y, float alto) {
        sr.setColor(Color.valueOf("1e8449"));
        sr.rect(x, y, 22, alto);                      // tronco
        sr.circle(x + 11, y + alto, 11);              // punta
        sr.rect(x - 22, y + alto * 0.45f, 22, 12);    // brazo izquierdo
        sr.rect(x - 22, y + alto * 0.45f, 12, alto * 0.30f);
        sr.rect(x + 22, y + alto * 0.30f, 22, 12);    // brazo derecho
        sr.rect(x + 32, y + alto * 0.30f, 12, alto * 0.35f);
    }
}
