package ProyectoPOO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Personaje {

    protected Texture texturaSelector;
    protected Texture texturaDerecha;
    protected Texture texturaIzquierda;

    protected float x, y;
    protected float ancho, alto;
    protected Color colorTint = Color.WHITE;
    protected boolean mirandoDerecha = true;

    // Variables de física y movimiento
    protected float velocidadX = 0;
    protected float velocidadY = 0;
    protected float velocidadCaminar = 350f;
    protected float fuerzaSalto = 550f;
    protected float gravedad = -1200f;
    protected boolean enSuelo = false;

    // Sistema de stamina
    protected float stamina = 100f;
    protected float staminaMaxima = 100f;
    protected float consumoStamina = 25f;
    protected float recuperacionStamina = 15f;

    // Velocidad al correr
    protected float velocidadCorrer = 550f;

    // Potenciador: botas de velocidad
    protected float velocidadBotas = 750f;
    protected float duracionBotas = 5f;
    protected float tiempoBotasRestante = 0f;

    // Potenciador: pluma de doble salto
    protected float duracionDobleSalto = 10f;
    protected float tiempoDobleSaltoRestante = 0f;
    protected boolean segundoSaltoDisponible = false;

    // Escudo protector
    protected boolean escudoActivo = false;

    // Sistema de vida (3 vidas)
    protected int vidas = 3;
    protected int vidasMaximas = 3;
    protected float tiempoInvulnerable = 0f;
    protected float duracionInvulnerabilidad = 1.0f;

    public Personaje(
        float x,
        float y,
        float ancho,
        float alto,
        String archivoSelector,
        String archivoDerecha,
        String archivoIzquierda
    ) {
        this(x, y, ancho, alto, archivoSelector, archivoDerecha, archivoIzquierda, Color.WHITE);
    }

    public Personaje(
        float x,
        float y,
        float ancho,
        float alto,
        String archivoSelector,
        String archivoDerecha,
        String archivoIzquierda,
        Color colorTint
    ) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        if (colorTint != null) {
            this.colorTint = colorTint;
        }

        this.texturaSelector = cargarTexturaSegura(archivoSelector, this.colorTint);
        this.texturaDerecha = cargarTexturaSegura(archivoDerecha, this.colorTint);
        this.texturaIzquierda = cargarTexturaSegura(archivoIzquierda, this.colorTint);
    }

    public static Texture cargarTexturaSegura(String nombreArchivo, Color colorRespaldo) {
        try {
            if (nombreArchivo != null && Gdx.files.internal(nombreArchivo).exists()) {
                return new Texture(Gdx.files.internal(nombreArchivo));
            }
            if (Gdx.files.internal("personaje.png").exists()) {
                return new Texture(Gdx.files.internal("personaje.png"));
            }
        } catch (Exception ignored) {
        }

        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(colorRespaldo != null ? colorRespaldo : Color.WHITE);
        pm.fill();
        Texture tex = new Texture(pm);
        pm.dispose();
        return tex;
    }

    public void actualizar(float delta, float alturaPiso) {
        // Actualizar temporizadores de potenciadores e invulnerabilidad
        if (tiempoBotasRestante > 0) {
            tiempoBotasRestante -= delta;
            if (tiempoBotasRestante < 0) {
                tiempoBotasRestante = 0;
            }
        }

        if (tiempoDobleSaltoRestante > 0) {
            tiempoDobleSaltoRestante -= delta;
            if (tiempoDobleSaltoRestante <= 0) {
                tiempoDobleSaltoRestante = 0;
                segundoSaltoDisponible = false;
            }
        }

        if (tiempoInvulnerable > 0) {
            tiempoInvulnerable -= delta;
            if (tiempoInvulnerable < 0) {
                tiempoInvulnerable = 0;
            }
        }

        // Movimiento horizontal (A / D)
        boolean moverIzquierda = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean moverDerecha = Gdx.input.isKeyPressed(Input.Keys.D);
        boolean intentandoCorrer = (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)
                || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT))
                && (moverIzquierda || moverDerecha);

        // Determinar si el personaje puede correr
        boolean corriendo = intentandoCorrer && stamina > 0;

        // Administrar la stamina independientemente de las botas
        if (corriendo) {
            stamina -= consumoStamina * delta;
        } else {
            stamina += recuperacionStamina * delta;
        }

        // Mantener la stamina entre 0 y su valor máximo
        stamina = Math.max(0, Math.min(stamina, staminaMaxima));

        // Elegir la velocidad según los poderes activos
        float velocidadActual;

        if (corriendo && tiempoBotasRestante > 0) {
            velocidadActual = velocidadBotas;
        } else if (corriendo) {
            velocidadActual = velocidadCorrer;
        } else {
            velocidadActual = velocidadCaminar;
        }

        velocidadX = 0;
        if (moverIzquierda && !moverDerecha) {
            velocidadX = -velocidadActual;
            mirandoDerecha = false;
        } else if (moverDerecha && !moverIzquierda) {
            velocidadX = velocidadActual;
            mirandoDerecha = true;
        } else if (moverIzquierda) {
            velocidadX = -velocidadActual;
            mirandoDerecha = false;
        } else if (moverDerecha) {
            velocidadX = velocidadActual;
            mirandoDerecha = true;
        }

        // Salto y doble salto (W)
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            if (enSuelo) {
                velocidadY = fuerzaSalto;
                enSuelo = false;
                if (tiempoDobleSaltoRestante > 0) {
                    segundoSaltoDisponible = true;
                }
            } else if (tiempoDobleSaltoRestante > 0 && segundoSaltoDisponible) {
                velocidadY = fuerzaSalto;
                segundoSaltoDisponible = false;
            }
        }

        // Aplicar gravedad y posición
        velocidadY += gravedad * delta;
        x += velocidadX * delta;
        y += velocidadY * delta;

        // Limitar bordes horizontales de pantalla
        float anchoPantalla = Gdx.graphics.getWidth();
        if (x < 0) {
            x = 0;
        } else if (x + ancho > anchoPantalla) {
            x = anchoPantalla - ancho;
        }

        // Colisión con el piso
        if (y <= alturaPiso) {
            y = alturaPiso;
            velocidadY = 0;
            enSuelo = true;
            if (tiempoDobleSaltoRestante > 0) {
                segundoSaltoDisponible = true;
            }
        } else {
            enSuelo = false;
        }
    }

    public void render(SpriteBatch batch) {
        Texture spriteActual = mirandoDerecha ? texturaDerecha : texturaIzquierda;
        float alpha = (tiempoInvulnerable > 0) ? 0.45f : 1f;
        batch.setColor(colorTint.r, colorTint.g, colorTint.b, alpha);
        batch.draw(spriteActual, x, y, ancho, alto);
        batch.setColor(Color.WHITE);
    }

    public Texture getTexturaSelector() {
        return texturaSelector;
    }

    // Daño y escudo
    public void recibirDanio() {
        if (tiempoInvulnerable > 0) {
            return;
        }
        if (bloquearGolpe()) {
            tiempoInvulnerable = duracionInvulnerabilidad;
            return;
        }
        if (vidas > 0) {
            vidas--;
            tiempoInvulnerable = duracionInvulnerabilidad;
        }
    }

    public boolean bloquearGolpe() {
        if (escudoActivo) {
            escudoActivo = false;
            return true;
        }
        return false;
    }

    public boolean estaVivo() {
        return vidas > 0;
    }

    // Potenciadores
    public void agregarStamina(float cantidad) {
        stamina += cantidad;
        if (stamina > staminaMaxima) {
            stamina = staminaMaxima;
        }
    }

    public void recuperarStamina(float energiaRecuperada) {
        agregarStamina(energiaRecuperada);
    }

    public void activarBotasVelocidad() {
        tiempoBotasRestante = duracionBotas;
    }

    public void activarDobleSalto() {
        tiempoDobleSaltoRestante = duracionDobleSalto;
        segundoSaltoDisponible = true;
    }

    public void activarEscudo() {
        escudoActivo = true;
    }

    public boolean tieneBotasActivas() {
        return tiempoBotasRestante > 0;
    }

    public float getTiempoBotasRestante() {
        return tiempoBotasRestante;
    }

    public boolean tieneDobleSaltoActivo() {
        return tiempoDobleSaltoRestante > 0;
    }

    public float getTiempoDobleSaltoRestante() {
        return tiempoDobleSaltoRestante;
    }

    public boolean tieneEscudoActivo() {
        return escudoActivo;
    }

    // Getters básicos
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

    public float getStamina() {
        return stamina;
    }

    public float getStaminaMaxima() {
        return staminaMaxima;
    }

    public int getVidas() {
        return vidas;
    }

    public int getVidasMaximas() {
        return vidasMaximas;
    }

    // Recuperar una vida sin superar el máximo permitido
    public void recuperarVida() {
        if (vidas < vidasMaximas) {
            vidas++;
        }
    }

    public Color getColorTint() {
        return colorTint;
    }

    public void dispose() {
        if (texturaSelector != null) {
            texturaSelector.dispose();
        }
        if (texturaDerecha != null && texturaDerecha != texturaSelector) {
            texturaDerecha.dispose();
        }
        if (texturaIzquierda != null && texturaIzquierda != texturaSelector && texturaIzquierda != texturaDerecha) {
            texturaIzquierda.dispose();
        }
    }
}