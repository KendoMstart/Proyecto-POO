package ProyectoPOO;

import java.util.ArrayList;
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
    private int personajeSeleccionado;
    private HabilidadEspecial habilidadEspecial;
    
    // Área del golpe especial del Guerrero
    private boolean golpeEspecialActivo = false;
    private float tiempoGolpeEspecial = 0f;

    // Tiempo mínimo entre golpes 
    private float cooldownGolpe = 0f;
    private final float duracionCooldownGolpe = 0.5f;

    // Cooldown independiente para habilidades especiales
    private float cooldownAtaqueEspecial = 0f;
    private boolean ataqueEspecialDisponible = true;
    private final float duracionCooldownAtaqueEspecial = 2f;

    private final float duracionGolpeEspecial = 0.2f;
    private final float alcanceGolpeEspecial = 80f;
    private final float alturaGolpeEspecial = 50f;
   
    // Cooldown exclusivo del escudo del Guerrero
    private float cooldownEscudo = 0f;
    private final float duracionCooldownEscudo = 10f;

    // Efecto visual del teletransporte del Mago
    private boolean efectoTeletransporteActivo = false;
    private float tiempoEfectoTeletransporte = 0f;
    private final float duracionEfectoTeletransporte = 0.25f;

    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<ProyectilMagico> proyectilesMagicos = new ArrayList<>();

    // Explosión mágica especial del Mago
    private boolean explosionMagicaActiva = false;
    private float tiempoExplosionMagica = 0f;
    private final float duracionExplosionMagica = 0.35f;
    private final float radioExplosionMagica = 100f;

    private ArrayList<Flecha> flechas = new ArrayList<>();
    private ArrayList<ProyectilMagico> ondasEspada = new ArrayList<>();
    private PocionStamina pocion;
    private BotasVelocidad botas;
    private PlumaDobleSalto pluma;
    private CorazonVida corazonVida;
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

        // Identificar al personaje y asignarle su habilidad especial
        if (jugador instanceof PRS2) {
            personajeSeleccionado = 2;
            habilidadEspecial = new HabilidadMago();

        } else if (jugador instanceof PRS3) {
            personajeSeleccionado = 3;
            habilidadEspecial = new HabilidadArquero();

        } else {
            personajeSeleccionado = 1;
            habilidadEspecial = new HabilidadGuerrero();
        }

        // Crear poción sobre el piso del escenario
        pocion = new PocionStamina(
            500,
            escenario.getAlturaPiso()
        );

        items.add(pocion);

        // Crear botas de velocidad sobre el suelo
        botas = new BotasVelocidad(
            700,
            escenario.getAlturaPiso()
        );

        items.add(botas);

        // Crear la pluma de doble salto
        pluma = new PlumaDobleSalto(
            900,
            escenario.getAlturaPiso()
        );

        items.add(pluma);

        // Crear escudo protector sobre el suelo
        escudo = new EscudoProtector(
            1100,
            escenario.getAlturaPiso()
        );

        items.add(escudo);

        // Crear corazón de curación sobre el suelo
        corazonVida = new CorazonVida(
            350f,
            escenario.getAlturaPiso()
        );

        items.add(corazonVida);

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

    private boolean hayColision(Item item) {
        return jugador.getX() < item.getX() + item.getAncho()
            && jugador.getX() + jugador.getAncho() > item.getX()
            && jugador.getY() < item.getY() + item.getAlto()
            && jugador.getY() + jugador.getAlto() > item.getY();
    }

    @Override
    public void render(float delta) {

        // Ataque básico del Arquero con clic izquierdo
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                && personajeSeleccionado == 3) {

            if (cooldownGolpe <= 0f) {

                boolean derecha = jugador.estaMirandoDerecha();

                float flechaX = derecha
                        ? jugador.getX() + jugador.getAncho()
                        : jugador.getX() - 30f;

                float flechaY = jugador.getY()
                        + jugador.getAlto() / 2f;

                // Crear una sola flecha recta
                flechas.add(new Flecha(flechaX, flechaY, derecha, 0f));

                // Esperar 0.5 segundos entre ataques básicos
                cooldownGolpe = duracionCooldownGolpe;

                System.out.println("¡El Arquero dispara una flecha!");
            }
        }

        // Disparo triple especial del Arquero con clic derecho
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)
                && personajeSeleccionado == 3) {

            if (cooldownAtaqueEspecial <= 0f) {
                cooldownAtaqueEspecial = duracionCooldownAtaqueEspecial;

                boolean derecha = jugador.estaMirandoDerecha();

                float flechaX = derecha
                        ? jugador.getX() + jugador.getAncho()
                        : jugador.getX() - 30f;

                float flechaY = jugador.getY() + jugador.getAlto() / 2f;

                // Flecha hacia arriba
                flechas.add(new Flecha(flechaX, flechaY, derecha, 150f));

                // Flecha recta
                flechas.add(new Flecha(flechaX, flechaY, derecha, 0f));

                // Flecha hacia abajo
                flechas.add(new Flecha(flechaX, flechaY, derecha, -150f));

                System.out.println("¡El Arquero realiza un disparo triple!");

            } else {
                System.out.println("¡No tienes suficiente energía!");
            }
        }

        // Habilidad especial del Mago con clic derecho
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)
                && personajeSeleccionado == 2) {

            if (cooldownAtaqueEspecial <= 0f) {

                // Activar la explosión mágica
                explosionMagicaActiva = true;
                tiempoExplosionMagica = duracionExplosionMagica;

                // Iniciar cooldown de 2 segundos
                cooldownAtaqueEspecial = duracionCooldownAtaqueEspecial;

                System.out.println("¡El Mago activa su explosión mágica!");
            }
        }

       // Ataque básico del Mago con clic izquierdo
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                && personajeSeleccionado == 2) {

            if (cooldownGolpe <= 0f) {

                boolean derecha = jugador.estaMirandoDerecha();

                float proyectilX = derecha
                        ? jugador.getX() + jugador.getAncho()
                        : jugador.getX() - 25f;

                float proyectilY = jugador.getY() + jugador.getAlto() / 2f;

                proyectilesMagicos.add(
                    new ProyectilMagico(proyectilX, proyectilY, derecha)
                );

                // Esperar 0.5 segundos entre disparos
                cooldownGolpe = duracionCooldownGolpe;

                System.out.println("¡El Mago lanza una bola mágica!");
            }
        }

        // Prueba de controles del mouse
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            System.out.println("Clic izquierdo: ataque básico");
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            System.out.println("Clic derecho: habilidad especial");
        }
        
        // Ataque especial del Guerrero con clic derecho
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)
                && personajeSeleccionado == 1) {

            if (cooldownAtaqueEspecial <= 0f) {

                // Iniciar cooldown de 2 segundos
                cooldownAtaqueEspecial = duracionCooldownAtaqueEspecial;

                // Dirección del ataque
                boolean derecha = jugador.estaMirandoDerecha();

                float ondaX = derecha
                        ? jugador.getX() + jugador.getAncho()
                        : jugador.getX() - 25f;

                float ondaY = jugador.getY() + jugador.getAlto() / 2f;

                // Crear la onda de espada
                ondasEspada.add(
                    new ProyectilMagico(ondaX, ondaY, derecha)
                );

                System.out.println("¡El Guerrero lanza una onda de espada!");
            }
        }

        // Ataque básico del Guerrero con clic izquierdo
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                && personajeSeleccionado == 1) {

            if (cooldownGolpe <= 0f) {

                // Activar el espadazo
                golpeEspecialActivo = true;
                tiempoGolpeEspecial = duracionGolpeEspecial;

                // Evitar golpes demasiado rápidos
                cooldownGolpe = duracionCooldownGolpe;

                System.out.println("¡El Guerrero realiza un espadazo!");
            }
        }

        // Activar escudo del Guerrero con Q
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)
                && personajeSeleccionado == 1) {

            if (cooldownEscudo <= 0f) {

                jugador.activarEscudo();

                // Iniciar espera de 10 segundos
                cooldownEscudo = duracionCooldownEscudo;

                System.out.println("¡Escudo del Guerrero activado!");

            } else {
                System.out.println(
                    "Escudo en recarga. Faltan "
                    + (int) Math.ceil(cooldownEscudo)
                    + " segundos."
                );
            }
        }
        
        // Defensa del Mago y del Arquero con Q
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)
                && (personajeSeleccionado == 2 || personajeSeleccionado == 3)) {

            if (cooldownEscudo <= 0f) {

                if (personajeSeleccionado == 2) {
                    // Mago: teletransporte instantáneo
                    DashEvasivo teletransporte = new DashEvasivo();

                    float destinoX = teletransporte.calcularDestino(
                        jugador.getX(),
                        jugador.estaMirandoDerecha()
                    );

                    jugador.desplazarHorizontalmente(destinoX);

                    // Activar el efecto visual del teletransporte
                    efectoTeletransporteActivo = true;
                    tiempoEfectoTeletransporte = duracionEfectoTeletransporte;

                    System.out.println("¡Teletransporte mágico!");
                } else {
                    // Arquero: dash fluido
                    jugador.iniciarDash();

                    System.out.println("¡Dash evasivo!");
                }

                cooldownEscudo = duracionCooldownEscudo;
            }
        }

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
       
        // Actualizar el cooldown de las habilidades especiales
        if (cooldownAtaqueEspecial > 0f) {
            cooldownAtaqueEspecial -= delta;

            if (cooldownAtaqueEspecial < 0f) {
                cooldownAtaqueEspecial = 0f;
            }
            ataqueEspecialDisponible = cooldownAtaqueEspecial <= 0f;
        }

        // Actualizar el cooldown del golpe especial
        if (cooldownGolpe > 0f) {
            cooldownGolpe -= delta;

            if (cooldownGolpe < 0f) {
                cooldownGolpe = 0f;
            }
        }

        // Actualizar la duración de la explosión mágica
        if (explosionMagicaActiva) {
            tiempoExplosionMagica -= delta;

            if (tiempoExplosionMagica <= 0f) {
                tiempoExplosionMagica = 0f;
                explosionMagicaActiva = false;
            }
        }

        // Actualizar la duración del golpe especial
        if (golpeEspecialActivo) {
            tiempoGolpeEspecial -= delta;

            if (tiempoGolpeEspecial <= 0f) {
                tiempoGolpeEspecial = 0f;
                golpeEspecialActivo = false;
            }
        }

        // Actualizar el tiempo de espera del escudo
        if (cooldownEscudo > 0f) {
            cooldownEscudo -= delta;

            if (cooldownEscudo < 0f) {
                cooldownEscudo = 0f;
            }
        }

        // Actualizar duración del efecto de teletransporte
        if (efectoTeletransporteActivo) {
            tiempoEfectoTeletransporte -= delta;

            if (tiempoEfectoTeletransporte <= 0f) {
                tiempoEfectoTeletransporte = 0f;
                efectoTeletransporteActivo = false;
            }
        }

        // Mover las flechas del Arquero
        for (int i = flechas.size() - 1; i >= 0; i--) {
            Flecha flecha = flechas.get(i);

            flecha.actualizar(delta);

            // Eliminar flechas cuando salgan de la pantalla
            if (flecha.getX() > Gdx.graphics.getWidth()
                    || flecha.getX() + flecha.getAncho() < 0
                    || flecha.getY() > Gdx.graphics.getHeight()
                    || flecha.getY() + flecha.getAlto() < 0) {

                flechas.remove(i);
            }
        }

        // Mover las ondas de espada del Guerrero
        for (int i = ondasEspada.size() - 1; i >= 0; i--) {
            ProyectilMagico onda = ondasEspada.get(i);

            onda.actualizar(delta);

            // Eliminar la onda cuando salga de la pantalla
            if (onda.getX() > Gdx.graphics.getWidth()
                    || onda.getX() + onda.getAncho() < 0) {

                ondasEspada.remove(i);
            }
        }

        // Mover los proyectiles mágicos del Mago
        for (int i = proyectilesMagicos.size() - 1; i >= 0; i--) {
            ProyectilMagico proyectil = proyectilesMagicos.get(i);

            proyectil.actualizar(delta);

            // Eliminar proyectiles que salen de la pantalla
            if (proyectil.getX() > Gdx.graphics.getWidth()
                    || proyectil.getX() + proyectil.getAncho() < 0) {
                proyectilesMagicos.remove(i);
            }
        }

        // Actualizar movimiento y stamina
        jugador.actualizar(
            delta,
            escenario.getAlturaPiso()
        );

        // Comprobar si perdió las 3 vidas -> ir a Game Over
        if (!jugador.estaVivo()) {
            game.setScreen(new PantallaGameOver(game, personajeSeleccionado));
            dispose();
            return;
        }

        // Detectar colisiones de todos los power-ups
        for (Item item : items) {
            if (!item.estaRecogido() && hayColision(item)) {
                item.recoger(jugador);
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

        // Mostrar temporalmente el área del golpe especial
        if (golpeEspecialActivo) {

            float golpeX;

            if (jugador.estaMirandoDerecha()) {
                golpeX = jugador.getX() + jugador.getAncho();
            } else {
                golpeX = jugador.getX() - alcanceGolpeEspecial;
            }

            float golpeY = jugador.getY()
                + (jugador.getAlto() - alturaGolpeEspecial) / 2f;

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.RED);

            shapeRenderer.rect(
                golpeX,
                golpeY,
                alcanceGolpeEspecial,
                alturaGolpeEspecial
            );

            shapeRenderer.end();
        }

        // Dibujar las flechas del Arquero
        if (!flechas.isEmpty()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.ORANGE);

            for (Flecha flecha : flechas) {
                shapeRenderer.rect(
                    flecha.getX(),
                    flecha.getY(),
                    flecha.getAncho(),
                    flecha.getAlto()
                );
            }

            shapeRenderer.end();
        }

        // Dibujar la explosión mágica del Mago
        if (explosionMagicaActiva) {
            float centroX = jugador.getX() + jugador.getAncho() / 2f;
            float centroY = jugador.getY() + jugador.getAlto() / 2f;

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.MAGENTA);

            shapeRenderer.circle(
                centroX,
                centroY,
                radioExplosionMagica
            );

            shapeRenderer.end();
        }

        // Dibujar las ondas de espada del Guerrero
        if (!ondasEspada.isEmpty()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.YELLOW);

            for (ProyectilMagico onda : ondasEspada) {

                // Onda vertical del tamaño del personaje
                shapeRenderer.rect(
                    onda.getX(),
                    onda.getY() - 20f,
                    35f,
                    jugador.getAlto()
                );
            }

            shapeRenderer.end();
        }

        // Dibujar los proyectiles mágicos del Mago
        if (!proyectilesMagicos.isEmpty() || efectoTeletransporteActivo) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.CYAN);

            for (ProyectilMagico proyectil : proyectilesMagicos) {
                shapeRenderer.rect(
                    proyectil.getX(),
                    proyectil.getY(),
                    proyectil.getAncho(),
                    proyectil.getAlto()
                );
            }
            
            // Destello mágico del teletransporte del Mago
            if (efectoTeletransporteActivo && personajeSeleccionado == 2) {

                float centroX = jugador.getX() + jugador.getAncho() / 2f;
                float centroY = jugador.getY() + jugador.getAlto() / 2f;

                float progreso = tiempoEfectoTeletransporte
                        / duracionEfectoTeletransporte;

                float radio = 85f * progreso;

                // Dibujar dos anillos mágicos
                shapeRenderer.end();
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

                // Anillo violeta exterior con grosor
                shapeRenderer.setColor(Color.PURPLE);

                for (int i = 0; i < 4; i++) {
                    shapeRenderer.circle(centroX, centroY, radio - i, 64);
                }

                // Anillo celeste interior con grosor
                shapeRenderer.setColor(Color.CYAN);

                for (int i = 0; i < 4; i++) {
                    shapeRenderer.circle(centroX, centroY, radio * 0.65f - i, 64);
                }

                // Partículas mágicas alrededor del teletransporte
                shapeRenderer.setColor(Color.YELLOW);

                for (int i = 0; i < 8; i++) {
                    float angulo = i * 45f;

                    float particulaX = centroX
                            + (float) Math.cos(Math.toRadians(angulo)) * radio;

                    float particulaY = centroY
                            + (float) Math.sin(Math.toRadians(angulo)) * radio;

                    shapeRenderer.circle(particulaX, particulaY, 4f, 16);
                }

                // Restaurar el modo de dibujo para los demás efectos
                shapeRenderer.end();
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            }

            shapeRenderer.end();
        }

        // Dibujar personaje, ítems y barras de estado
        batch.begin();
        batch.setColor(Color.WHITE);

        jugador.render(batch);

        for (Item item : items) {
            item.render(batch);
        }       

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

       // Indicador de recarga del ataque especial
        float progresoEspecial = 1f - Math.max(
            0f, cooldownAtaqueEspecial
        ) / duracionCooldownAtaqueEspecial;

        // Fondo oscuro del indicador
        batch.setColor(Color.DARK_GRAY);
        batch.draw(barraTexture, 20f, 20f, 40f, 40f);

        // Relleno que crece mientras se recarga
        if (ataqueEspecialDisponible) {
            batch.setColor(Color.GREEN);
        } else {
            batch.setColor(Color.ORANGE);
        }

        batch.draw(
            barraTexture,
            20f, 20f,
            40f, 40f * progresoEspecial
        );

        batch.setColor(Color.WHITE);

      // HUD de power-ups activos: cuadritos 
        float slotX = 20f;
        float slotY = Gdx.graphics.getHeight() - 155f;
        float slotSize = 38f;
        float separacion = 8f;

        // BOTAS: cuadrito amarillo con duración
        if (jugador.tieneBotasActivas()) {
            float progreso = Math.max(0f,
                Math.min(1f, jugador.getTiempoBotasRestante() / 5f));

            batch.setColor(Color.DARK_GRAY);
            batch.draw(barraTexture, slotX, slotY, slotSize, slotSize);

            batch.setColor(Color.YELLOW);
            batch.draw(barraTexture, slotX + 4f, slotY + 8f, 30f, 26f);

            batch.setColor(Color.ORANGE);
            batch.draw(barraTexture, slotX, slotY, slotSize * progreso, 5f);

            slotX += slotSize + separacion;
        }

        // PLUMA: cuadrito morado con duración
        if (jugador.tieneDobleSaltoActivo()) {
            float progreso = Math.max(0f,
                Math.min(1f, jugador.getTiempoDobleSaltoRestante() / 10f));

            batch.setColor(Color.DARK_GRAY);
            batch.draw(barraTexture, slotX, slotY, slotSize, slotSize);

            batch.setColor(Color.MAGENTA);
            batch.draw(barraTexture, slotX + 4f, slotY + 8f, 30f, 26f);

            batch.setColor(Color.PINK);
            batch.draw(barraTexture, slotX, slotY, slotSize * progreso, 5f);

            slotX += slotSize + separacion;
        }

        // ESCUDO: cuadrito azul mientras esté activo
        if (jugador.tieneEscudoActivo()) {
            batch.setColor(Color.DARK_GRAY);
            batch.draw(barraTexture, slotX, slotY, slotSize, slotSize);

            batch.setColor(Color.BLUE);
            batch.draw(barraTexture, slotX + 4f, slotY + 8f, 30f, 26f);
        }

        // Restaurar el color original
        batch.setColor(Color.WHITE);

        // Indicador de recarga de la habilidad defensiva (Q)
        float progresoDefensa = 1f - Math.max(
            0f, cooldownEscudo
        ) / duracionCooldownEscudo;

        // Fondo oscuro
        batch.setColor(Color.DARK_GRAY);
        batch.draw(barraTexture, 70f, 20f, 40f, 40f);

        // Relleno de recarga
        if (cooldownEscudo <= 0f) {
            batch.setColor(Color.GREEN);
        } else {
            batch.setColor(Color.BLUE);
        }

        batch.draw(
            barraTexture,
            70f, 20f,
            40f, 40f * progresoDefensa
        );

        batch.setColor(Color.WHITE);

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
        for (Item item : items) {
            item.dispose();
        }
        if (barraTexture != null) {
            barraTexture.dispose();
        }
        if (corazonTexture != null && corazonTexture != barraTexture) {
            corazonTexture.dispose();
        }
        
    }
}