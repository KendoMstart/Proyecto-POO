package ProyectoPOO;

import com.badlogic.gdx.graphics.Color;

public class PRS1 extends Personaje {

    public static final String SPRITE_SELECTOR = "prs1_selector.png";
    public static final String SPRITE_DERECHA = "prs1_der.png";
    public static final String SPRITE_IZQUIERDA = "prs1_izq.png";

    public PRS1(float x, float y, float ancho, float alto) {
        super(x, y, ancho, alto, SPRITE_SELECTOR, SPRITE_DERECHA, SPRITE_IZQUIERDA, Color.WHITE);
    }
}