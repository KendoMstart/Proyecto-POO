package ProyectoPOO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Personaje {
    private Texture textura;
    private float x, y;
    private float ancho, alto;
    
    // Variables de física y movimiento
    private float velocidadX = 0;
    private float velocidadY = 0;
    private final float VELOCIDAD_CAMINAR = 350f; // Velocidad horizontal
    private final float FUERZA_SALTO = 550f;      // Impulso hacia arriba al saltar
    private final float GRAVEDAD = -1200f;        // Fuerza de atracción hacia abajo
    private boolean enSuelo = false;

    public Personaje(float x, float y, float ancho, float alto, String nombreArchivo) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;

        try {
            if (Gdx.files.internal(nombreArchivo).exists()) {
                textura = new Texture(Gdx.files.internal(nombreArchivo));
            } else {
                Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
                pixmap.setColor(Color.valueOf("e74c3c"));
                pixmap.fill();
                textura = new Texture(pixmap);
                pixmap.dispose();
            }
        } catch (Exception e) {
            System.out.println("Error al cargar la textura del personaje: " + e.getMessage());
        }
    }

    public void actualizar(float delta, float alturaPiso) {
        // 1. Control de movimiento horizontal con A y D
        velocidadX = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velocidadX = -VELOCIDAD_CAMINAR;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocidadX = VELOCIDAD_CAMINAR;
        }

        // 2. Control de salto con W o FLECHA ARRIBA
        if ((Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) && enSuelo) {
            velocidadY = FUERZA_SALTO;
            enSuelo = false;
        }

        // 3. Aplicar gravedad si no está en el suelo
        if (!enSuelo) {
            velocidadY += GRAVEDAD * delta;
        }

        // 4. Actualizar posiciones
        x += velocidadX * delta;
        y += velocidadY * delta;

        // 5. Colisión con el piso (la altura del piso verde es de 100 píxeles)
        if (y <= alturaPiso) {
            y = alturaPiso;
            velocidadY = 0;
            enSuelo = true;
        }

        // 6. Evitar que salga de los límites laterales de la ventana
        if (x < 0) x = 0;
        if (x > Gdx.graphics.getWidth() - ancho) {
            x = Gdx.graphics.getWidth() - ancho;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(textura, x, y, ancho, alto);
    }

    public void dispose() {
        if (textura != null) {
            textura.dispose();
        }
    }
}