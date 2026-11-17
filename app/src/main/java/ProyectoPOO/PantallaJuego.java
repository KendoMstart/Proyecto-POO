package ProyectoPOO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
    private ShapeRenderer shapeRenderer;
    private Label titleLabel;
    private Personaje jugador;

    private final Escenario[] escenarios = {
        new EscenarioPradera(), new EscenarioDesierto(), new EscenarioNoche()
    };
    private Escenario escenario;

    // El selector de escenarios (menú) puede usar este constructor
    public PantallaJuego(Main game, Escenario escenario) {
        this.game = game;
        this.escenario = escenario;
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        jugador = new Personaje(150, escenario.getAlturaPiso(), 48, 64, "personaje.png");

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        titleLabel = new Label("", labelStyle);
        actualizarTexto();
        table.top().padTop(30);
        table.add(titleLabel);
    }

    // Constructor que ya usaba Menus: no hay que cambiar nada allá
    public PantallaJuego(Main game) {
        this(game, new EscenarioPradera());
    }

    private void cambiarEscenario(int indice) {
        escenario = escenarios[indice];
        actualizarTexto();
    }

    private void actualizarTexto() {
        titleLabel.setText("Escenario: " + escenario.getNombre()
                + "  |  A, D = mover, W = saltar  |  1, 2, 3 = cambiar escenario");
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) cambiarEscenario(0);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) cambiarEscenario(1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) cambiarEscenario(2);

        jugador.actualizar(delta, escenario.getAlturaPiso());

        Color cielo = escenario.getColorCielo();
        Gdx.gl.glClearColor(cielo.r, cielo.g, cielo.b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Escenario (formas dibujadas por código, sin imágenes)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        escenario.dibujar(shapeRenderer, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        // Personaje encima del escenario
        batch.begin();
        jugador.render(batch);
        batch.end();

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
        if (jugador != null) jugador.dispose();
    }
}
