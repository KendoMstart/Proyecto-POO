package ProyectoPOO;

import com.badlogic.gdx.graphics.Color;

public class PRS2 extends Personaje {

    public static final String SPRITE_SELECTOR = "prs2_selector.png";
    public static final String SPRITE_DERECHA = "prs2_der.png";
    public static final String SPRITE_IZQUIERDA = "prs2_izq.png";

    public PRS2(float x, float y, float ancho, float alto) {
        super(x, y, ancho, alto, SPRITE_SELECTOR, SPRITE_DERECHA, SPRITE_IZQUIERDA, Color.WHITE);
    }
}