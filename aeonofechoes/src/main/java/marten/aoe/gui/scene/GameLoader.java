package marten.aoe.gui.scene;

import java.io.File;
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
        String status = loader.getStatus();
        if (!this.status.equals(status)) {
            this.status = status;
            this.statusString.setContent(status);
        }
        if (this.loader.loadingFinished()) {
            this.fireEvent(new AgeSceneSwitchEvent(new Game(engine,
                    this.details)));
        }
    }

    @Override
    public void render() {
        this.flatland.render();
    }

    @Override
    public synchronized void load(LoadingState state) {
        state.status = "Loading map data 0%";
        try {
            this.engine = (EngineFace)Naming.lookup(this.details.engineUrl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("Loading map data for '" + this.details.mapName + "'...");
        File mapFolder = new File(Path.TILE_DATA_PATH);
        String[] filenames = mapFolder.list();
        for (int i = 0; i < filenames.length; i++) {
            state.status = "Loading map data " + Integer.toString((i * 100) / filenames.length) + "%";
            if (filenames[i].charAt(0) == '.')
                continue;
            ImageCache.loadImage(Path.TILE_DATA_PATH + filenames[i],
                    Path.TILE_DATA_PATH + filenames[i].split("\\.")[0]);
        }
        state.status = "100%";
    }
}
