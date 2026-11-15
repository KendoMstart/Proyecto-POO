package ProyectoPOO;

import com.badlogic.gdx.Game;

public class Main extends Game {

    @Override
    public void create() {
        setScreen(new Menus(this));
    }

    @Override
    public void render() {
        super.render();
    }
}