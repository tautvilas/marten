package marten.aoe.gui.scene.editor;

import java.util.ArrayList;

import marten.age.core.AppInfo;
import marten.age.event.AgeSceneSwitchEvent;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.layout.SimpleLayout;
import marten.age.io.Loadable;
import marten.age.io.LoadingState;
import marten.age.io.SimpleLoader;
import marten.aoe.Path;
import marten.aoe.gui.scene.TileLoader;
import marten.aoe.gui.widget.AoeString;

import org.apache.log4j.Logger;

public class MapEditorLoader extends TileLoader implements Loadable {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(MapEditorLoader.class);

    private Flatland flatland;
    private SimpleLoader loader = new SimpleLoader(this);
    private AoeString statusString;
    String status = "Loading map data";

    public MapEditorLoader() {
    }

    @Override
    public void init() {
        this.flatland = new Flatland();
        SimpleLayout layout = new SimpleLayout(AppInfo.getDisplayDimension());
        this.statusString = new AoeString("Loading: 0%");
        layout.center(this.statusString);
        this.flatland.addChild(layout);
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
            this.fireEvent(new AgeSceneSwitchEvent(new MapEditor()));
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
        this.loadLayers(new ArrayList<String>(), Path.NEW_TILE_DATA_PATH);
        state.status = "100%";
    }
}
