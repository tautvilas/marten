package marten.aoe.gui.scene;

import java.rmi.Naming;
import java.util.ArrayList;

import marten.age.core.AppInfo;
import marten.age.event.AgeSceneSwitchEvent;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.layout.SimpleLayout;
import marten.age.io.Loadable;
import marten.age.io.LoadingState;
import marten.age.io.SimpleLoader;
import marten.aoe.Path;
import marten.aoe.gui.widget.AoeString;
import marten.aoe.server.face.EngineFace;
import marten.aoe.server.serializable.GameDetails;

import org.apache.log4j.Logger;

public class GameLoader extends TileLoader implements Loadable {
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
        try {
            this.engine = (EngineFace)Naming.lookup(this.details.engineUrl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        state.status = "Loading tile data 0%";
        log.info("Loading tile data...");
        this.loadLayers(new ArrayList<String>(), Path.TILE_DATA_PATH);
        this.loadMasks(Path.MASKS_DATA_PATH);
        state.status = "100%";
    }
}
