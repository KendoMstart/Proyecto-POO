package ProyectoPOO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PantallaGameOver implements Screen {

    private final Main juego; // Cámbialo a App si tu clase principal se llama App
    private final SpriteBatch batch;
    private final ShapeRenderer shapes;
    private final BitmapFont font;

    private int opcionSeleccionada = 0; // 0 = Reintentar, 1 = Salir

    public PantallaGameOver(Main juego) {
        this.juego = juego;
        this.batch = new SpriteBatch();
        this.shapes = new ShapeRenderer();
        this.font = new BitmapFont();
        this.font.getData().setScale(1.6f);
    }

    @Override
    public void render(float delta) {
        procesarEntrada();

        Gdx.gl.glClearColor(0.08f, 0.05f, 0.07f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float ancho = Gdx.graphics.getWidth();
        float alto = Gdx.graphics.getHeight();
        float btnAncho = 280f;
        float btnAlto = 55f;
        float btnX = (ancho - btnAncho) / 2f;
        float reintentarY = alto * 0.45f;
        float salirY = alto * 0.32f;

        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = alto - Gdx.input.getY();

            if (touchX >= btnX && touchX <= btnX + btnAncho) {
                if (touchY >= reintentarY && touchY <= reintentarY + btnAlto) {
                    reintentar();
                    return;
                }
                if (touchY >= salirY && touchY <= salirY + btnAlto) {
                    salir();
                    return;
                }
            }
        }

        shapes.begin(ShapeRenderer.ShapeType.Filled);

        shapes.setColor(opcionSeleccionada == 0 ? new Color(0.18f, 0.55f, 0.28f, 1f) : new Color(0.22f, 0.22f, 0.26f, 1f));
        shapes.rect(btnX, reintentarY, btnAncho, btnAlto);

        shapes.setColor(opcionSeleccionada == 1 ? new Color(0.65f, 0.18f, 0.22f, 1f) : new Color(0.22f, 0.22f, 0.26f, 1f));
        shapes.rect(btnX, salirY, btnAncho, btnAlto);

        shapes.end();

        batch.begin();
        font.setColor(Color.SCARLET);
        font.draw(batch, "GAME OVER", ancho * 0.5f - 85f, alto * 0.68f);

        font.setColor(Color.WHITE);
        font.draw(batch, "1. Reintentar", btnX + 20f, reintentarY + 35f);
        font.draw(batch, "2. Salir", btnX + 20f, salirY + 35f);

        font.getData().setScale(1.0f);
        font.setColor(Color.LIGHT_GRAY);
        font.draw(batch, "Teclas [1] / [2], flechas + ENTER, o clic", btnX, alto * 0.20f);
        font.getData().setScale(1.6f);
        batch.end();
    }

    private void procesarEntrada() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_1)) {
            reintentar();
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_2)) {
            salir();
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)
                || Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            opcionSeleccionada = (opcionSeleccionada == 0) ? 1 : 0;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (opcionSeleccionada == 0) {
                reintentar();
            } else {
                salir();
            }
        }
    }

    private void reintentar() {
        juego.setScreen(new PantallaJuego(juego));
        dispose();
    }

    private void salir() {
        Gdx.app.exit();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        shapes.dispose();
        font.dispose();
    }
}