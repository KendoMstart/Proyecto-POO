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

    // Sistema de stamina
    private float stamina = 100f;
    private final float STAMINA_MAXIMA = 100f;
    private final float CONSUMO_STAMINA = 25f;
    private final float RECUPERACION_STAMINA = 15f;

    // Velocidad al correr
    private final float VELOCIDAD_CORRER = 550f;

    // Potenciador: botas de velocidad
    private final float VELOCIDAD_BOTAS = 750f;
    private final float DURACION_BOTAS = 5f;
    private float tiempoBotasRestante = 0f;

    // Potenciador: pluma de doble salto
    private final float DURACION_DOBLE_SALTO = 10f;
    private float tiempoDobleSaltoRestante = 0f;
    private boolean segundoSaltoDisponible = false;

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
        
        // Descontar el tiempo restante de las botas
        if (tiempoBotasRestante > 0) {
            tiempoBotasRestante -= delta;

            if (tiempoBotasRestante < 0) {
                tiempoBotasRestante = 0;
            }
        }

        // Descontar el tiempo restante del doble salto
        if (tiempoDobleSaltoRestante > 0) {
            tiempoDobleSaltoRestante -= delta;

            // Desactivar el poder cuando termine
            if (tiempoDobleSaltoRestante <= 0) {
                tiempoDobleSaltoRestante = 0;
                segundoSaltoDisponible = false;
            }
        }

        // Control de movimiento horizontal con A y D
        velocidadX = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.A) ||
            Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velocidadX = -VELOCIDAD_CAMINAR;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) ||
            Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocidadX = VELOCIDAD_CAMINAR;
        }

        // Sistema de stamina para correr con Shift
        boolean moviendose = velocidadX != 0;

        boolean shiftPresionado =
                Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ||
                Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);

        if (shiftPresionado && moviendose && stamina > 0) {
    
            // Si las botas están activas, correr más rápido
            if (tiempoBotasRestante > 0) {
                velocidadX = Math.signum(velocidadX) * VELOCIDAD_BOTAS;
            } else {
                velocidadX = Math.signum(velocidadX) * VELOCIDAD_CORRER;
            }

            // Consumir stamina
            stamina -= CONSUMO_STAMINA * delta;
        } else {
            // Recuperar stamina
            stamina += RECUPERACION_STAMINA * delta;
        }

        // Mantener stamina entre 0 y 100
        stamina = Math.max(0, Math.min(stamina, STAMINA_MAXIMA));

        // 2. Control del salto y doble salto
        boolean presionoSalto =
                Gdx.input.isKeyJustPressed(Input.Keys.W) ||
                Gdx.input.isKeyJustPressed(Input.Keys.UP);

        if (presionoSalto) {

            if (enSuelo) {
                // Primer salto normal
                velocidadY = FUERZA_SALTO;
                enSuelo = false;

                // Preparar el segundo salto si la pluma está activa
                segundoSaltoDisponible =
                        tiempoDobleSaltoRestante > 0;

            } else if (tiempoDobleSaltoRestante > 0 &&
                       segundoSaltoDisponible) {

                // Segundo salto mientras está en el aire
                velocidadY = FUERZA_SALTO;
                segundoSaltoDisponible = false;
            }
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

    // Devuelve la stamina actual del personaje
    public float getStamina() {
        return stamina;
    }

    // Devuelve la cantidad máxima de stamina
    public float getStaminaMaxima() {
        return STAMINA_MAXIMA;
    }

    // Recupera energía al recoger x poción
    public void recuperarStamina(float cantidad) {
        if (cantidad > 0) {
            stamina = Math.min(stamina + cantidad, STAMINA_MAXIMA);
        }
    }

    // Obtener posición y tamaño del personaje
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getAncho() {
        return ancho;
    }

    public float getAlto() {
        return alto;
    }

    // Activa las botas de velocidad durante 5 segundos
    public void activarBotasVelocidad() {
        tiempoBotasRestante = DURACION_BOTAS;
    }

    // Indica si el potenciador sigue activo
    public boolean tieneBotasActivas() {
        return tiempoBotasRestante > 0;
    }

    // Devuelve los segundos restantes del efecto
    public float getTiempoBotasRestante() {
        return tiempoBotasRestante;
    }

    // Activar el poder de doble salto durante 10 segundos
    public void activarDobleSalto() {
        tiempoDobleSaltoRestante = DURACION_DOBLE_SALTO;
        segundoSaltoDisponible = false;
    }

    // Consultar si el poder sigue activo
    public boolean tieneDobleSaltoActivo() {
        return tiempoDobleSaltoRestante > 0;
    }

    // Consultar el tiempo restante del poder
    public float getTiempoDobleSaltoRestante() {
        return tiempoDobleSaltoRestante;
    }

}