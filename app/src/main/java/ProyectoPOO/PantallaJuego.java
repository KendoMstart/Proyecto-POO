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
    private Texture barraTexture;
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

        // Textura blanca para dibujar la barra de stamina
        Pixmap barraPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        barraPixmap.setColor(Color.WHITE);
        barraPixmap.fill();

        barraTexture = new Texture(barraPixmap);
        barraPixmap.dispose();

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

        // Barra visual de stamina
        float porcentajeStamina =
                jugador.getStamina() / jugador.getStaminaMaxima();

        float barraX = 20f;
        float barraY = Gdx.graphics.getHeight() - 70f;
        float barraAncho = 200f;
        float barraAlto = 18f;

        // Fondo oscuro de la barra
        batch.setColor(Color.DARK_GRAY);
        batch.draw(barraTexture, barraX, barraY,
                   barraAncho, barraAlto);

        // Cambiar el color según la stamina disponible
        if (porcentajeStamina >= 0.50f) {
            batch.setColor(Color.GREEN);
        } else if (porcentajeStamina >= 0.25f) {
            batch.setColor(Color.YELLOW);
        } else {
            batch.setColor(Color.RED);
        }

        // Dibujar la energía disponible
        batch.draw(barraTexture, barraX, barraY,
                   barraAncho * porcentajeStamina, barraAlto);

        // Restaurar el color para no afectar otros dibujos
        batch.setColor(Color.WHITE);


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
        if (barraTexture != null) barraTexture.dispose();
    }
}