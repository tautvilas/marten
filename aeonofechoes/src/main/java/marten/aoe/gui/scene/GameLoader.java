package marten.aoe.gui.scene;

import java.io.IOException;
import java.util.Set;

import marten.age.core.AgeScene;
import marten.age.core.AppInfo;
import marten.age.event.AgeSceneSwitchEvent;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.flat.SimpleLayout;
import marten.age.graphics.image.ImageCache;
import marten.age.io.Loadable;
import marten.age.io.LoadingState;
import marten.age.io.SimpleLoader;
import marten.aoe.Path;
import marten.aoe.engine.Engine;
import marten.aoe.gui.GameParams;
import marten.aoe.gui.widget.AoeString;
import marten.aoe.gui.widget.MapWidget;
import marten.aoe.loader.Loader;

import org.apache.log4j.Logger;

public class GameLoader extends AgeScene implements Loadable {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(GameLoader.class);

    private Flatland flatland;
    private SimpleLayout layout;
    private SimpleLoader loader = new SimpleLoader(this);
    private AoeString statusString;
    private GameParams params;
    private Engine engine;
    String status = "Loading map data";

    public GameLoader(GameParams params) {
        this.params = params;
    }

    @Override
    public void init() {
        this.flatland = new Flatland();
        this.layout = new SimpleLayout(AppInfo.getDisplayDimension());
        this.statusString = new AoeString("Loading: 0%");
        this.layout.center(this.statusString);
        this.flatland.addChild(this.layout);
        loader.load();
    }

    @Override
    public void compute() {
        if (this.loader.loadingFinished()) {
            MapWidget map = new MapWidget(engine, params.getMapName());
            this.fireEvent(new AgeSceneSwitchEvent(new Game(map, params)));
        }
        String status = loader.getStatus();
        if (!this.status.equals(status)) {
            this.status = status;
            this.statusString.setContent(status);
        }
    }

    @Override
    public void render() {
        this.flatland.render();
    }

    @Override
    public synchronized void load(LoadingState state) {
        state.status = "Loading map data";
        this.engine = new Engine();
        log.info("Loading map data for '" + this.params.getMapName() + "'...");
        try {
            Loader.load(engine, Path.MAP_PATH + params.getMapName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Set<String> definedTerrain = engine.terrain.definedTerrain();
        for (String terrainType : definedTerrain) {
            state.status = "Loading map images ";
            ImageCache.loadImage("data/gui/tiles/" + terrainType + ".png");
        }
        state.status = "100%";
    }
}
