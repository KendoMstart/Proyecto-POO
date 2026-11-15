package ProyectoPOO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PantallaJuego implements Screen {
    private final Main game;
    private Stage stage;
    private SpriteBatch batch;
    private Texture pisoTexture;
    private Personaje jugador;
    private final float alturaPiso = 100f; // Altura en píxeles del piso verde

    public PantallaJuego(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();

        // Textura del piso verde
        Pixmap pisoPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pisoPixmap.setColor(Color.valueOf("2ecc71"));
        pisoPixmap.fill();
        pisoTexture = new Texture(pisoPixmap);
        pisoPixmap.dispose();

        // Creamos al personaje justo sobre la altura del piso (100)
        jugador = new Personaje(150, alturaPiso, 48, 64, "personaje.png");

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        Label titleLabel = new Label("¡Usa A, D para moverte y W para saltar!", labelStyle);
        table.top().padTop(30);
        table.add(titleLabel);
    }

    @Override
    public void render(float delta) {
        // Actualizamos la física del personaje enviándole el delta y la altura del piso
        jugador.actualizar(delta, alturaPiso);

        // Fondo Celeste (Cielo)
        Gdx.gl.glClearColor(0.53f, 0.81f, 0.92f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Renderizado
        batch.begin();
        // Dibujar piso verde
        batch.draw(pisoTexture, 0, 0, Gdx.graphics.getWidth(), alturaPiso);
        
        // Dibujar personaje
        jugador.render(batch);
        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    
    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        if (pisoTexture != null) pisoTexture.dispose();
        if (jugador != null) jugador.dispose();
    }
}