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

    // Barra de stamina
    private Texture barraTexture;

    // Personaje y poción
    private Personaje jugador;
    private PocionStamina pocion;
    private BotasVelocidad botas;

    // Escenarios disponibles
    private final Escenario[] escenarios = {
        new EscenarioPradera(),
        new EscenarioDesierto(),
        new EscenarioNoche()
    };

    private Escenario escenario;

    // Constructor principal
    public PantallaJuego(Main game, Escenario escenario) {
        this.game = game;
        this.escenario = escenario;

        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // Crear textura blanca para la barra
        Pixmap barraPixmap = new Pixmap(
            1, 1, Pixmap.Format.RGBA8888
        );

        barraPixmap.setColor(Color.WHITE);
        barraPixmap.fill();

        barraTexture = new Texture(barraPixmap);
        barraPixmap.dispose();

        // Crear personaje sobre el piso del escenario
        jugador = new Personaje(
            150,
            escenario.getAlturaPiso(),
            48,
            64,
            "personaje.png"
        );

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

    // Constructor usado por el menú
    public PantallaJuego(Main game) {
        this(game, new EscenarioPradera());
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
        );
    }

    @Override
    public void render(float delta) {

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

        // Detectar colisión con la poción
        if (!pocion.estaRecogida()) {

            float jugadorX = jugador.getX();
            float jugadorY = jugador.getY();
            float jugadorAncho = jugador.getAncho();
            float jugadorAlto = jugador.getAlto();

            boolean colision =
                jugadorX < pocion.getX() + pocion.getAncho() &&
                jugadorX + jugadorAncho > pocion.getX() &&
                jugadorY < pocion.getY() + pocion.getAlto() &&
                jugadorY + jugadorAlto > pocion.getY();

            if (colision) {
                pocion.recoger(jugador);

                System.out.println(
                    "¡Poción recogida! +50 de stamina"
                );
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


        // Color del cielo según el escenario
        Color cielo = escenario.getColorCielo();

        Gdx.gl.glClearColor(
            cielo.r,
            cielo.g,
            cielo.b,
            1f
        );

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Dibujar escenario
        shapeRenderer.begin(
            ShapeRenderer.ShapeType.Filled
        );

        escenario.dibujar(
            shapeRenderer,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );

        shapeRenderer.end();

        // Dibujar personaje y poción
        batch.begin();
        batch.setColor(Color.WHITE);

        jugador.render(batch);
        pocion.render(batch);
        botas.render(batch);

        // Calcular porcentaje de stamina
        float porcentajeStamina =
            jugador.getStamina() / jugador.getStaminaMaxima();

        float barraX = 20f;
        float barraY = Gdx.graphics.getHeight() - 70f;
        float barraAncho = 200f;
        float barraAlto = 18f;

        // Fondo de la barra
        batch.setColor(Color.DARK_GRAY);

        batch.draw(
            barraTexture,
            barraX,
            barraY,
            barraAncho,
            barraAlto
        );

        // Colores de stamina
        if (porcentajeStamina >= 0.50f) {
            batch.setColor(Color.GREEN);
        } else if (porcentajeStamina >= 0.25f) {
            batch.setColor(Color.YELLOW);
        } else {
            batch.setColor(Color.RED);
        }

        // Dibujar energía disponible
        batch.draw(
            barraTexture,
            barraX,
            barraY,
            barraAncho * porcentajeStamina,
            barraAlto
        );

        // Restaurar el color
        batch.setColor(Color.WHITE);

        // Mostrar barra mientras las botas estén activas
        if (jugador.tieneBotasActivas()) {

            float porcentajeBotas =
                jugador.getTiempoBotasRestante() / 5f;

            float botasBarraX = 20f;
            float botasBarraY = Gdx.graphics.getHeight() - 95f;
            float botasBarraAncho = 200f;
            float botasBarraAlto = 12f;

            // Fondo oscuro
            batch.setColor(Color.DARK_GRAY);
            batch.draw(
                barraTexture,
                botasBarraX,
                botasBarraY,
                botasBarraAncho,
                botasBarraAlto
            );

            // Duración restante en amarillo
            batch.setColor(Color.YELLOW);
            batch.draw(
                barraTexture,
                botasBarraX,
                botasBarraY,
                botasBarraAncho * porcentajeBotas,
                botasBarraAlto
            );

            batch.setColor(Color.WHITE);
        }

        batch.end();

        // Dibujar interfaz
        stage.act(
            Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)
        );

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

        stage.getViewport().update(width, height, true);

        shapeRenderer.getProjectionMatrix().setToOrtho2D(
            0, 0, width, height
        );

        shapeRenderer.updateMatrices();

        batch.getProjectionMatrix().setToOrtho2D(
            0, 0, width, height
        );
    }

    @Override
    public void show() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

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
    }
}
