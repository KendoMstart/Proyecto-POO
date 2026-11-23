package ProyectoPOO;

import com.badlogic.gdx.graphics.Color;

public class PRS3 extends Personaje {

    public static final String SPRITE_SELECTOR = "prs3_selector.png";
    public static final String SPRITE_DERECHA = "prs3_der.png";
    public static final String SPRITE_IZQUIERDA = "prs3_izq.png";

    public PRS3(float x, float y, float ancho, float alto) {
        super(x, y, ancho, alto, SPRITE_SELECTOR, SPRITE_DERECHA, SPRITE_IZQUIERDA, Color.WHITE);
    }
}