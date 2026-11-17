package ProyectoPOO;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.Random;

/** Escenario 3: bosque de noche con luna, estrellas y montañas. */
public class EscenarioNoche extends Escenario {
    private static final int CANTIDAD_ESTRELLAS = 70;
    private final float[] estrellasX = new float[CANTIDAD_ESTRELLAS]; // 0..1
    private final float[] estrellasY = new float[CANTIDAD_ESTRELLAS]; // 0..1

    public EscenarioNoche() {
        super("Noche", "0b1026", "1e3d2f", 100f);
        Random r = new Random(42); // semilla fija: las estrellas no cambian de lugar
        for (int i = 0; i < CANTIDAD_ESTRELLAS; i++) {
            estrellasX[i] = r.nextFloat();
            estrellasY[i] = 0.35f + r.nextFloat() * 0.65f;
        }
    }

    @Override
    protected void dibujarFondo(ShapeRenderer sr, float w, float h) {
        // Estrellas
        sr.setColor(Color.WHITE);
        for (int i = 0; i < CANTIDAD_ESTRELLAS; i++) {
            sr.rect(estrellasX[i] * w, estrellasY[i] * h, 3, 3);
        }

        // Luna (círculo + círculo del color del cielo = creciente)
        sr.setColor(Color.valueOf("f4f1de"));
        sr.circle(w * 0.80f, h * 0.80f, 50);
        sr.setColor(colorCielo);
        sr.circle(w * 0.80f + 20, h * 0.80f + 10, 42);

        // Montañas
        sr.setColor(Color.valueOf("1b2540"));
        sr.triangle(w * 0.00f, alturaPiso, w * 0.20f, h * 0.45f, w * 0.42f, alturaPiso);
        sr.triangle(w * 0.30f, alturaPiso, w * 0.55f, h * 0.55f, w * 0.80f, alturaPiso);
        sr.triangle(w * 0.65f, alturaPiso, w * 0.85f, h * 0.40f, w * 1.05f, alturaPiso);
    }

    @Override
    protected void dibujarDecoracion(ShapeRenderer sr, float w, float h) {
        sr.setColor(Color.valueOf("16301f"));
        sr.rect(0, alturaPiso - 12, w, 12);

        arbol(sr, w * 0.12f, alturaPiso, 1.0f);
        arbol(sr, w * 0.33f, alturaPiso, 1.4f);
        arbol(sr, w * 0.58f, alturaPiso, 1.1f);
        arbol(sr, w * 0.88f, alturaPiso, 1.5f);
    }

    private void arbol(ShapeRenderer sr, float x, float y, float e) {
        sr.setColor(Color.valueOf("4a3320"));
        sr.rect(x - 8 * e, y, 16 * e, 50 * e);                 // tronco
        sr.setColor(Color.valueOf("0f5132"));
        sr.triangle(x - 50 * e, y + 40 * e, x, y + 130 * e, x + 50 * e, y + 40 * e);
        sr.triangle(x - 40 * e, y + 90 * e, x, y + 170 * e, x + 40 * e, y + 90 * e);
    }
}
