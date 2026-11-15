package ProyectoPOO;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class App {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("Integration");
        

        Graphics.DisplayMode displayMode = Lwjgl3ApplicationConfiguration.getDisplayMode(
            Lwjgl3ApplicationConfiguration.getPrimaryMonitor()
        );

        config.setWindowedMode(displayMode.width, displayMode.height);

        new Lwjgl3Application(new Main(), config);
    }
}