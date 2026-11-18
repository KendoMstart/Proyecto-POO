
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

    // Textura blanca para la barra de stamina
    private Texture barraTexture;

    // Personaje
    private Personaje jugador;

    // Escenarios disponibles
    private final Escenario[] escenarios = {
        new EscenarioPradera(),
        new EscenarioDesierto(),
        new EscenarioNoche()
    };

    private Escenario escenario;

    // Constructor para seleccionar un escenario
    public PantallaJuego(Main game, Escenario escenario) {
        this.game = game;
        this.escenario = escenario;

        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // Textura blanca para la barra de stamina
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

    // Constructor utilizado por el menú principal
    public PantallaJuego(Main game) {
        this(game, new EscenarioPradera());
    }

    // Cambiar entre los tres escenarios
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

        // Cambio de escenarios con teclas 1, 2 y 3
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            cambiarEscenario(0);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            cambiarEscenario(1);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            cambiarEscenario(2);
        }

        // Actualizar movimiento y stamina del personaje
        jugador.actualizar(
            delta,
            escenario.getAlturaPiso()
        );

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
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        escenario.dibujar(
            shapeRenderer,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );

        shapeRenderer.end();

        // Dibujar personaje
        batch.begin();
        batch.setColor(Color.WHITE);

        jugador.render(batch);

        // Barra de stamina
        float porcentajeStamina =
            jugador.getStamina() / jugador.getStaminaMaxima();

        float barraX = 20f;
        float barraY = Gdx.graphics.getHeight() - 70f;
        float barraAncho = 200f;
        float barraAlto = 18f;

        // Fondo oscuro
        batch.setColor(Color.DARK_GRAY);

        batch.draw(
            barraTexture,
            barraX,
            barraY,
            barraAncho,
            barraAlto
        );

        // Color según el nivel de stamina
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

        // Restaurar color
        batch.setColor(Color.WHITE);

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

        if (barraTexture != null) {
            barraTexture.dispose();
        }
    }
}
