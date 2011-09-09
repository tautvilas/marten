package marten.aoe.gui.scene.editor;

import java.io.File;
import java.util.ArrayList;

import marten.age.core.AgeScene;
import marten.age.core.AppInfo;
import marten.age.event.AgeSceneSwitchEvent;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.layout.SimpleLayout;
import marten.age.io.Loadable;
import marten.age.io.LoadingState;
import marten.age.io.SimpleLoader;
import marten.aoe.Path;
import marten.aoe.gui.TileImageFactory;
import marten.aoe.gui.widget.AoeString;

import org.apache.log4j.Logger;

public class MapEditorLoader extends AgeScene implements Loadable {
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

    private void loadLayers(ArrayList<String> priorities, String path) {
        File tileFolder = new File(path);
        File[] files = tileFolder.listFiles();
        for (int i = 0; i < files.length; i++) {
            String filename = files[i].getName();
            if (filename.charAt(0) == '.') continue;
            String[] parts = filename.split("_");
            System.out.println(filename);
            String priority = parts[0];
            String newPath = path + "/" + filename;
            priorities.add(priority);
            if (files[i].isDirectory()) {
                this.loadLayers(priorities, newPath);
            } else {
                String[] priors = new String[priorities.size()];
                priorities.toArray(priors);
                TileImageFactory.addLayer(priors, parts[1].split("\\.")[0], newPath);
            }
            priorities.remove(priorities.size() - 1);
        }
    }

    @Override
    public synchronized void load(LoadingState state) {
        state.status = "Loading tile data 0%";
        log.info("Loading tile data...");
        this.loadLayers(new ArrayList<String>(), Path.NEW_TILE_DATA_PATH);
        state.status = "100%";
    }
}
