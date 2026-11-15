package ProyectoPOO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Menus implements Screen {
    private final Main game;
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Texture fallbackTexture; // Textura de respaldo por si falla la imagen
    private OrthographicCamera camera;

    public Menus(Main game) {
        this.game = game;
        
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        batch = new SpriteBatch();
        
        // Creamos un color azul/morado de respaldo por si el archivo no carga
        Pixmap fallbackPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        fallbackPixmap.setColor(Color.valueOf("1a1a2e"));
        fallbackPixmap.fill();
        fallbackTexture = new Texture(fallbackPixmap);
        fallbackPixmap.dispose();

        // Intento de carga de la imagen con validación estricta
        try {
            if (Gdx.files.internal("fondo.png").exists()) {
                backgroundTexture = new Texture(Gdx.files.internal("fondo.png"));
                System.out.println("¡IMAGEN 'fondo.png' CARGADA EXITOSAMENTE!");
            } else {
                System.out.println("ERROR: El archivo 'fondo.png' NO existe en el directorio de assets accesible por LibGDX.");
            }
        } catch (Exception e) {
            System.out.println("EXCEPCIÓN al cargar 'fondo.png': " + e.getMessage());
        }

        createBasicSkin();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton btnPlay = new TextButton("Jugar", skin);
        TextButton btnSettings = new TextButton("Configuraciones", skin);
        TextButton btnExit = new TextButton("Salir", skin);

        table.add(btnPlay).width(350).height(70).pad(15);
        table.row();
        table.add(btnSettings).width(350).height(70).pad(15);
        table.row();
        table.add(btnExit).width(350).height(70).pad(15);

        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Iniciando partida...");
                game.setScreen(new PantallaJuego(game));
            }
        });

        btnSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Abriendo configuraciones...");
            }
        });

        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

private void createBasicSkin() {
        skin = new Skin();
        skin.add("default", new BitmapFont());

        // 1. Estado Normal: Estilo madera oscura / retro con borde sutil
        Pixmap pixmapUp = new Pixmap(120, 40, Pixmap.Format.RGBA8888);
        pixmapUp.setColor(Color.valueOf("2b1d11")); // Marrón oscuro profundo
        pixmapUp.fill();
        pixmapUp.setColor(Color.valueOf("5c3f25")); // Borde más claro simulando relieve
        pixmapUp.drawRectangle(0, 0, 120, 40);
        skin.add("background-up", new Texture(pixmapUp));

        // 2. Estado Hover (al pasar el mouse): Tono ámbar/dorado brillante (combina con el sol del fondo)
        Pixmap pixmapOver = new Pixmap(120, 40, Pixmap.Format.RGBA8888);
        pixmapOver.setColor(Color.valueOf("b86b20")); 
        pixmapOver.fill();
        pixmapOver.setColor(Color.valueOf("f39c12")); // Borde brillante
        pixmapOver.drawRectangle(0, 0, 120, 40);
        skin.add("background-over", new Texture(pixmapOver));

        // 3. Estado Presionado (al hacer clic): Un tono más hundido
        Pixmap pixmapDown = new Pixmap(120, 40, Pixmap.Format.RGBA8888);
        pixmapDown.setColor(Color.valueOf("18100a"));
        pixmapDown.fill();
        skin.add("background-down", new Texture(pixmapDown));

        // Construir el estilo del botón
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = skin.getFont("default");
        textButtonStyle.fontColor = Color.valueOf("f5f5f5"); // Blanco suave
        textButtonStyle.up = skin.newDrawable("background-up");
        textButtonStyle.over = skin.newDrawable("background-over");
        textButtonStyle.down = skin.newDrawable("background-down");
        skin.add("default", textButtonStyle);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        // Si la imagen cargó bien, la dibuja; si no, dibuja el fondo de respaldo para que no se quede negro
        if (backgroundTexture != null) {
            batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        } else {
            batch.draw(fallbackTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        camera.setToOrtho(false, width, height);
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        batch.dispose();
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
        if (fallbackTexture != null) {
            fallbackTexture.dispose();
        }
    }
    
}