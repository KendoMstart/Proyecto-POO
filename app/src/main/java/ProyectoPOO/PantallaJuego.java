package ProyectoPOO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PantallaJuego implements Screen {

    private final Main game;
    private Stage stage;
    private SpriteBatch batch;

    // Sistema de escenarios
    private ShapeRenderer shapeRenderer;
    private Label titleLabel;

    // Barra de stamina y corazones de vida
    private Texture barraTexture;
    private Texture corazonTexture;

    // Personaje e ítems
    private Personaje jugador;
    private PocionStamina pocion;
    private BotasVelocidad botas;
    private PlumaDobleSalto pluma;
    private EscudoProtector escudo;

    // Escenarios disponibles
    private final Escenario[] escenarios = {
        new EscenarioPradera(),
        new EscenarioDesierto(),
        new EscenarioNoche()
    };

    private Escenario escenario;

    // Constructor principal con personaje elegido
    public PantallaJuego(Main game, Escenario escenario, Personaje jugadorElegido) {
        this.game = game;
        this.escenario = escenario;

        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // Crear textura blanca para las barras
        Pixmap barraPixmap = new Pixmap(
            1, 1, Pixmap.Format.RGBA8888
        );

        barraPixmap.setColor(Color.WHITE);
        barraPixmap.fill();

        barraTexture = new Texture(barraPixmap);
        barraPixmap.dispose();

        // Cargar textura del corazón pixelado (Cora.png)
        try {
            if (Gdx.files.internal("Cora.png").exists()) {
                corazonTexture = new Texture(Gdx.files.internal("Cora.png"));
            } else {
                corazonTexture = barraTexture;
            }
        } catch (Exception e) {
            corazonTexture = barraTexture;
        }

        // Asignar personaje elegido (por defecto PRS1)
     if (jugadorElegido != null) {
            this.jugador = jugadorElegido;
        } else {
            this.jugador = new PRS1(
                150f,
                escenario.getAlturaPiso(),
                48f,
                64f
            );
        }

        // Crear poción sobre el piso del escenario
        pocion = new PocionStamina(
            500,
            escenario.getAlturaPiso()
        );

        // Crear botas de velocidad sobre el suelo
        botas = new BotasVelocidad(
            700,
            escenario.getAlturaPiso()
        );

        // Crear la pluma de doble salto
        pluma = new PlumaDobleSalto(
            900,
            escenario.getAlturaPiso()
        );

        // Crear escudo protector sobre el suelo
        escudo = new EscudoProtector(
            1100,
            escenario.getAlturaPiso()
        );

        // Interfaz de texto
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label.LabelStyle labelStyle = new Label.LabelStyle(
            new BitmapFont(),
            Color.WHITE
        );

        titleLabel = new Label("", labelStyle);
        actualizarTexto();

        table.top().padTop(30);
        table.add(titleLabel);
    }

    public PantallaJuego(Main game, Escenario escenario) {
        this(game, escenario, null);
    }

    public PantallaJuego(Main game) {
        this(game, new EscenarioPradera(), null);
    }

    // Cambiar escenario
    private void cambiarEscenario(int indice) {
        escenario = escenarios[indice];
        actualizarTexto();
    }

    private void actualizarTexto() {
        titleLabel.setText(
            "Escenario: " + escenario.getNombre()
            + " | A, D = mover, W = saltar"
            + " | Shift = correr"
            + " | 1, 2, 3 = cambiar escenario"
            + " | H = probar daño"
        );
    }

    @Override
    public void render(float delta) {

        // PRUEBA TEMPORAL: simular un golpe con H
        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            int vidasAntes = jugador.getVidas();
            jugador.recibirDanio();
            if (jugador.getVidas() == vidasAntes) {
                System.out.println("¡El escudo bloqueó el golpe o estás invulnerable!");
            } else {
                System.out.println("Golpe recibido. Vidas restantes: " + jugador.getVidas());
            }
        }

        // Cambiar escenarios con 1, 2 y 3
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            cambiarEscenario(0);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            cambiarEscenario(1);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            cambiarEscenario(2);
        }

        // Actualizar movimiento y stamina
        jugador.actualizar(
            delta,
            escenario.getAlturaPiso()
        );

        // Comprobar si perdió las 3 vidas -> ir a Game Over
        if (!jugador.estaVivo()) {
            game.setScreen(new PantallaGameOver(game));
            dispose();
            return;
        }

        // Detectar colisión con la poción
        if (!pocion.estaRecogida()) {
            boolean colision =
                jugador.getX() < pocion.getX() + pocion.getAncho() &&
                jugador.getX() + jugador.getAncho() > pocion.getX() &&
                jugador.getY() < pocion.getY() + pocion.getAlto() &&
                jugador.getY() + jugador.getAlto() > pocion.getY();

            if (colision) {
                pocion.recoger(jugador);
                System.out.println("¡Poción recogida! +50 de stamina");
            }
        }

        // Detectar colisión con las botas de velocidad
        if (!botas.estanRecogidas()) {
            boolean colisionBotas =
                jugador.getX() < botas.getX() + botas.getAncho() &&
                jugador.getX() + jugador.getAncho() > botas.getX() &&
                jugador.getY() < botas.getY() + botas.getAlto() &&
                jugador.getY() + jugador.getAlto() > botas.getY();

            if (colisionBotas) {
                botas.recoger(jugador);
                System.out.println("¡Botas recogidas! Velocidad extra durante 5 segundos");
            }
        }

        // Detectar colisión con la pluma de doble salto
        if (!pluma.estaRecogida()) {
            boolean colisionPluma =
                jugador.getX() < pluma.getX() + pluma.getAncho() &&
                jugador.getX() + jugador.getAncho() > pluma.getX() &&
                jugador.getY() < pluma.getY() + pluma.getAlto() &&
                jugador.getY() + jugador.getAlto() > pluma.getY();

            if (colisionPluma) {
                pluma.recoger(jugador);
                System.out.println("¡Pluma recogida! Doble salto durante 10 segundos");
            }
        }

        // Detectar colisión con el escudo protector
        if (!escudo.estaRecogido()) {
            boolean colisionEscudo =
                jugador.getX() < escudo.getX() + escudo.getAncho() &&
                jugador.getX() + jugador.getAncho() > escudo.getX() &&
                jugador.getY() < escudo.getY() + escudo.getAlto() &&
                jugador.getY() + jugador.getAlto() > escudo.getY();

            if (colisionEscudo) {
                escudo.recoger(jugador);
                System.out.println("¡Escudo recogido! Protección activada");
            }
        }

        // Color del cielo según el escenario
        Color cielo = escenario.getColorCielo();
        Gdx.gl.glClearColor(cielo.r, cielo.g, cielo.b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Dibujar escenario
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        escenario.dibujar(shapeRenderer, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        // Dibujar personaje, ítems y barras de estado
        batch.begin();
        batch.setColor(Color.WHITE);

        jugador.render(batch);
        pocion.render(batch);
        botas.render(batch);
        pluma.render(batch);
        escudo.render(batch);

        // Dibujar corazones de vida arriba a la izquierda
        float corazonSize = 28f;
        float separacionCorazones = 8f;
        float corazonX = 20f;
        float corazonY = Gdx.graphics.getHeight() - 48f;

        for (int i = 0; i < jugador.getVidasMaximas(); i++) {
            if (i < jugador.getVidas()) {
                batch.setColor(Color.WHITE);
            } else {
                batch.setColor(0.35f, 0.35f, 0.35f, 0.5f);
            }
            batch.draw(
                corazonTexture,
                corazonX + i * (corazonSize + separacionCorazones),
                corazonY,
                corazonSize,
                corazonSize
            );
        }
        batch.setColor(Color.WHITE);

        // Calcular porcentaje de stamina
        float porcentajeStamina = jugador.getStamina() / jugador.getStaminaMaxima();
        float barraX = 20f;
        float barraY = Gdx.graphics.getHeight() - 82f;
        float barraAncho = 200f;
        float barraAlto = 18f;

        // Fondo de la barra
        batch.setColor(Color.DARK_GRAY);
        batch.draw(barraTexture, barraX, barraY, barraAncho, barraAlto);

        // Colores de stamina
        if (porcentajeStamina >= 0.50f) {
            batch.setColor(Color.GREEN);
        } else if (porcentajeStamina >= 0.25f) {
            batch.setColor(Color.YELLOW);
        } else {
            batch.setColor(Color.RED);
        }

        batch.draw(barraTexture, barraX, barraY, barraAncho * porcentajeStamina, barraAlto);
        batch.setColor(Color.WHITE);

        // Mostrar barra mientras las botas estén activas
        if (jugador.tieneBotasActivas()) {
            float porcentajeBotas = jugador.getTiempoBotasRestante() / 5f;
            float botasBarraX = 20f;
            float botasBarraY = Gdx.graphics.getHeight() - 107f;

            batch.setColor(Color.DARK_GRAY);
            batch.draw(barraTexture, botasBarraX, botasBarraY, 200f, 12f);

            batch.setColor(Color.YELLOW);
            batch.draw(barraTexture, botasBarraX, botasBarraY, 200f * porcentajeBotas, 12f);
            batch.setColor(Color.WHITE);
        }

        // Barra de duración de la pluma de doble salto
        if (jugador.tieneDobleSaltoActivo()) {
            float porcentajePluma = jugador.getTiempoDobleSaltoRestante() / 10f;
            float plumaBarraX = 20f;
            float plumaBarraY = Gdx.graphics.getHeight() - (jugador.tieneBotasActivas() ? 127f : 107f);

            batch.setColor(Color.DARK_GRAY);
            batch.draw(barraTexture, plumaBarraX, plumaBarraY, 200f, 12f);

            batch.setColor(Color.MAGENTA);
            batch.draw(barraTexture, plumaBarraX, plumaBarraY, 200f * porcentajePluma, 12f);
            batch.setColor(Color.WHITE);
        }

        // Indicador visual del escudo protector
        if (jugador.tieneEscudoActivo()) {
            float escudoBarraX = 20f;
            float escudoBarraY = Gdx.graphics.getHeight() - 107f;

            if (jugador.tieneBotasActivas()) {
                escudoBarraY -= 20f;
            }
            if (jugador.tieneDobleSaltoActivo()) {
                escudoBarraY -= 20f;
            }

            batch.setColor(Color.BLUE);
            batch.draw(barraTexture, escudoBarraX, escudoBarraY, 200f, 12f);
            batch.setColor(Color.WHITE);
        }

        batch.end();

        // Dibujar interfaz
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        shapeRenderer.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        shapeRenderer.updateMatrices();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        shapeRenderer.dispose();

        if (jugador != null) {
            jugador.dispose();
        }
        if (pocion != null) {
            pocion.dispose();
        }
        if (barraTexture != null) {
            barraTexture.dispose();
        }
        if (corazonTexture != null && corazonTexture != barraTexture) {
            corazonTexture.dispose();
        }
        if (escudo != null) {
            escudo.dispose();
        }
    }
}