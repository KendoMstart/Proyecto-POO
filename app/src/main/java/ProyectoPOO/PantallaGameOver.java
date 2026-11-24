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

    // Recordar qué personaje eligió el jugador
    private final int personajeSeleccionado;

    private final SpriteBatch batch;
    private final ShapeRenderer shapes;
    private final BitmapFont font;

    private int opcionSeleccionada = 0; // 0 = Reintentar, 1 = Cambiar personaje, 2 = Salir


    public PantallaGameOver(Main juego, int personajeSeleccionado) {
        this.juego = juego;
        this.personajeSeleccionado = personajeSeleccionado;
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
        float reintentarY = alto * 0.50f;
        float cambiarY = reintentarY - btnAlto - 18f;
        float salirY = cambiarY - btnAlto - 18f;



        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = alto - Gdx.input.getY();

            if (touchX >= btnX && touchX <= btnX + btnAncho) {

                // Botón 1: Reintentar
                if (touchY >= reintentarY &&
                    touchY <= reintentarY + btnAlto) {
                    reintentar();
                    return;
                }

                // Botón 2: Cambiar personaje
                if (touchY >= cambiarY &&
                    touchY <= cambiarY + btnAlto) {
                    cambiarPersonaje();
                    return;
                }

                // Botón 3: Salir
                if (touchY >= salirY &&
                    touchY <= salirY + btnAlto) {
                    salir();
                    return;
                }
            }
        }

        shapes.begin(ShapeRenderer.ShapeType.Filled);

        // Botón 1: Reintentar (verde)
        shapes.setColor(opcionSeleccionada == 0
            ? new Color(0.18f, 0.55f, 0.28f, 1f)
            : new Color(0.22f, 0.22f, 0.26f, 1f));
        shapes.rect(btnX, reintentarY, btnAncho, btnAlto);

        // Botón 2: Cambiar personaje (azul)
        shapes.setColor(opcionSeleccionada == 1
            ? new Color(0.18f, 0.40f, 0.75f, 1f)
            : new Color(0.22f, 0.22f, 0.26f, 1f));
        shapes.rect(btnX, cambiarY, btnAncho, btnAlto);

        // Botón 3: Salir (rojo)
        shapes.setColor(opcionSeleccionada == 2
            ? new Color(0.65f, 0.18f, 0.22f, 1f)
            : new Color(0.22f, 0.22f, 0.26f, 1f));
        shapes.rect(btnX, salirY, btnAncho, btnAlto);

        shapes.end();

        batch.begin();
        font.setColor(Color.SCARLET);
        font.draw(batch, "GAME OVER", ancho * 0.5f - 85f, alto * 0.68f);


        font.setColor(Color.WHITE);
        font.draw(batch, "1. Reintentar", btnX + 20f, reintentarY + 35f);
        font.draw(batch, "2. Cambiar personaje", btnX + 20f, cambiarY + 35f);
        font.draw(batch, "3. Salir", btnX + 20f, salirY + 35f);


        font.getData().setScale(1.0f);
        font.setColor(Color.LIGHT_GRAY);
        font.draw(batch, "Teclas [1] / [2] / [3], flechas + ENTER", btnX, alto * 0.20f);
        font.getData().setScale(1.6f);
        batch.end();
    }

    private void procesarEntrada() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_1)) {
            reintentar();
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) ||
            Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_2)) {
            cambiarPersonaje();
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) ||
            Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_3)) {
            salir();
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)
                || Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            opcionSeleccionada = (opcionSeleccionada + 1) % 3;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
            Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {

            if (opcionSeleccionada == 0) {
                reintentar();
            } else if (opcionSeleccionada == 1) {
                cambiarPersonaje();
            } else {
                salir();
            }
        }

    }

    private void reintentar() {
        float x = 150f;
        float y = 100f;
        float ancho = 48f;
        float alto = 64f;

        Personaje personaje;

        if (personajeSeleccionado == 2) {
            personaje = new PRS2(x, y, ancho, alto);
        } else if (personajeSeleccionado == 3) {
            personaje = new PRS3(x, y, ancho, alto);
        } else {
            personaje = new PRS1(x, y, ancho, alto);
        }

        Escenario escenario = new EscenarioPradera();

        juego.setScreen(new PantallaJuego(juego, escenario, personaje));
        dispose();

    }

    // Volver al selector para elegir otro personaje
    private void cambiarPersonaje() {
        juego.setScreen(new SelectorPersonaje(juego));
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