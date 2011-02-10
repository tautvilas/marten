package marten.aoe.gui.scene;

import java.io.IOException;
import java.rmi.Naming;

import marten.age.core.AgeScene;
import marten.age.core.AppInfo;
import marten.age.event.AgeSceneSwitchEvent;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.image.ImageCache;
import marten.age.graphics.layout.SimpleLayout;
import marten.age.io.Loadable;
import marten.age.io.LoadingState;
import marten.age.io.SimpleLoader;
import marten.aoe.Path;
import marten.aoe.dto.MapDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.gui.widget.AoeString;
import marten.aoe.server.face.EngineFace;
import marten.aoe.server.serializable.GameDetails;

import org.apache.log4j.Logger;

public class GameLoader extends AgeScene implements Loadable {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(GameLoader.class);

    private Flatland flatland;
    private SimpleLayout layout;
    private SimpleLoader loader = new SimpleLoader(this);
    private AoeString statusString;
    private GameDetails details;
    private EngineFace engine;
    String status = "Loading map data";

    public GameLoader(GameDetails details) {
        this.details = details;
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
            this.fireEvent(new AgeSceneSwitchEvent(new Game(engine,
                    this.details)));
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
        try {
            this.engine = (EngineFace) Naming.lookup(this.details.engineUrl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("Loading map data for '" + this.details.mapName + "'...");
        MapDTO mapData;
        try {
            mapData = engine.getMap();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (TileDTO[] tileLine : mapData.getTileMap()) {
            for (TileDTO tile : tileLine) {
                state.status = "Loading map images ";
                // System.out.println(tile.getName());
                ImageCache.loadImage("data/gui/tiles/"
                        + tile.getName().toLowerCase() + ".png",
                        Path.TILE_DATA_PATH + tile.getName());
            }
        }
        state.status = "100%";
    }
}