package ProyectoPOO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SelectorPersonaje implements Screen {

    private final Main game;
    private final Escenario escenario;
    private final SpriteBatch batch;
    private final ShapeRenderer shapes;
    private final BitmapFont font;

    private final String[] nombres = {
        "PRS 1",
        "PRS 2",
        "PRS 3"
    };

    private final String[] spritesSelector = {
        PRS1.SPRITE_SELECTOR,
        PRS2.SPRITE_SELECTOR,
        PRS3.SPRITE_SELECTOR
    };

    private final Color[] coloresRespaldo = {
        Color.valueOf("e74c3c"),
        Color.valueOf("3498db"),
        Color.valueOf("2ecc71")
    };

    private final Texture[] texturasPreview = new Texture[3];
    private int seleccionado = 0;

    public SelectorPersonaje(Main game, Escenario escenario) {
        this.game = game;
        this.escenario = (escenario != null) ? escenario : new EscenarioPradera();
        this.batch = new SpriteBatch();
        this.shapes = new ShapeRenderer();
        this.font = new BitmapFont();
        this.font.getData().setScale(1.4f);

        for (int i = 0; i < 3; i++) {
            texturasPreview[i] = Personaje.cargarTexturaSegura(spritesSelector[i], coloresRespaldo[i]);
        }
    }

    public SelectorPersonaje(Main game) {
        this(game, new EscenarioPradera());
    }

    @Override
    public void render(float delta) {
        procesarEntrada();

        Gdx.gl.glClearColor(0.11f, 0.13f, 0.19f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float ancho = Gdx.graphics.getWidth();
        float alto = Gdx.graphics.getHeight();
        float cardAncho = 220f;
        float cardAlto = 240f;
        float espacio = 30f;
        float totalAncho = cardAncho * 3f + espacio * 2f;
        float inicioX = (ancho - totalAncho) / 2f;
        float cardY = (alto - cardAlto) / 2f;

        if (Gdx.input.justTouched()) {
            float tx = Gdx.input.getX();
            float ty = alto - Gdx.input.getY();

            for (int i = 0; i < 3; i++) {
                float cx = inicioX + i * (cardAncho + espacio);
                if (tx >= cx && tx <= cx + cardAncho && ty >= cardY && ty <= cardY + cardAlto) {
                    seleccionado = i;
                    confirmarSeleccion();
                    return;
                }
            }
        }

        shapes.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < 3; i++) {
            float cx = inicioX + i * (cardAncho + espacio);
            shapes.setColor(i == seleccionado ? new Color(0.24f, 0.46f, 0.78f, 1f) : new Color(0.20f, 0.22f, 0.28f, 1f));
            shapes.rect(cx, cardY, cardAncho, cardAlto);
        }
        shapes.end();

        batch.begin();
        font.setColor(Color.WHITE);
        font.draw(batch, "ELIGE TU PERSONAJE", inicioX, alto - 50f);

        for (int i = 0; i < 3; i++) {
            float cx = inicioX + i * (cardAncho + espacio);
            float spriteAncho = 64f;
            float spriteAlto = 84f;
            float spriteX = cx + (cardAncho - spriteAncho) / 2f;
            float spriteY = cardY + 95f;

            batch.setColor(Color.WHITE);
            batch.draw(texturasPreview[i], spriteX, spriteY, spriteAncho, spriteAlto);

            font.draw(batch, (i + 1) + ". " + nombres[i], cx + 45f, cardY + 55f);
        }

        font.getData().setScale(1.0f);
        font.setColor(Color.LIGHT_GRAY);
        font.draw(batch, "Teclas [1] [2] [3] o flechas + ENTER, o clic", inicioX, 50f);
        font.getData().setScale(1.4f);
        batch.end();
    }

    private void procesarEntrada() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_1)) {
            seleccionado = 0;
            confirmarSeleccion();
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_2)) {
            seleccionado = 1;
            confirmarSeleccion();
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_3)) {
            seleccionado = 2;
            confirmarSeleccion();
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            seleccionado = (seleccionado + 2) % 3;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            seleccionado = (seleccionado + 1) % 3;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            confirmarSeleccion();
        }
    }

    private void confirmarSeleccion() {
        Personaje elegido = crearPersonaje(seleccionado, 150f, escenario.getAlturaPiso(), 48f, 64f);
        game.setScreen(new PantallaJuego(game, escenario, elegido));
        dispose();
    }

    private Personaje crearPersonaje(int indice, float x, float y, float ancho, float alto) {
        switch (indice) {
            case 1:
                return new PRS2(x, y, ancho, alto);
            case 2:
                return new PRS3(x, y, ancho, alto);
            case 0:
            default:
                return new PRS1(x, y, ancho, alto);
        }
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
        for (int i = 0; i < texturasPreview.length; i++) {
            if (texturasPreview[i] != null) {
                texturasPreview[i].dispose();
            }
        }
    }
}