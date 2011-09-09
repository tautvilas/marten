package marten.aoe.gui.scene.editor;

import java.awt.Font;
import java.util.HashMap;
import java.util.List;

import marten.age.control.KeyboardController;
import marten.age.control.MouseController;
import marten.age.core.AgeScene;
import marten.age.core.AppInfo;
import marten.age.graphics.BasicSceneGraphBranch;
import marten.age.graphics.SceneGraphBranch;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.geometry.primitives.Rectangle;
import marten.age.graphics.layout.SimpleLayout;
import marten.age.graphics.model.SimpleModel;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.FontCache;
import marten.age.widget.Action;
import marten.age.widget.Button;
import marten.age.widget.obsolete.FpsCounter;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.gui.TileImageFactory;
import marten.aoe.gui.TileLayer;
import marten.aoe.gui.widget.AoeButtonFactory;
import marten.aoe.gui.widget.MapWidget;
import marten.aoe.gui.widget.Sidebar;
import marten.aoe.gui.widget.TerrainCache;

public class MapEditor extends AgeScene {

    BitmapFont font = FontCache.getFont(new Font("Arial", Font.PLAIN, 16));

    private Flatland flatland = new Flatland();
    private NewMapDialog newMapDialog;
    @SuppressWarnings("unused")
    private int mapSize = 0;
    private HashMap<String, SceneGraphBranch<SimpleModel>> tabs = new HashMap<String, SceneGraphBranch<SimpleModel>>();
    private SimpleLayout layout = new SimpleLayout(AppInfo
            .getDisplayDimension());

    public MapEditor() {
        Button newButton = AoeButtonFactory.getEditorButton("New");
        int buttonWidth = (int)newButton.getDimension().width;
        int buttonHeight = (int)newButton.getDimension().height;
        int windowHeight = AppInfo.getDisplayHeight();
        int windowWidth = AppInfo.getDisplayWidth();
        int padding = 5;
        this.addController(new MouseController());
        this.addController(new KeyboardController());
        // new button
        newButton.setPosition(new Point(0, windowHeight - buttonHeight));
        this.flatland.addChild(newButton);
        newButton.setAction(new Action() {
            @Override
            public void perform() {
                newMapDialog.setup();
                MapEditor.this.layout.center(newMapDialog);
            }
        });
        this.registerControllable(newButton);
        // load button
        Button loadButton = AoeButtonFactory.getEditorButton("Load");
        loadButton.setPosition(new Point(buttonWidth + padding, windowHeight
                - buttonHeight));
        this.flatland.addChild(loadButton);
        // save button
        Button saveButton = AoeButtonFactory.getEditorButton("Save");
        saveButton.setPosition(new Point((buttonWidth + padding) * 2,
                windowHeight - buttonHeight));
        this.flatland.addChild(saveButton);
        // sidebar
        Sidebar sidebar = new Sidebar(new Dimension(256, windowHeight));
        sidebar.setPosition(new Point(windowWidth - 256, 0));
        flatland.addChild(sidebar);
        // other stuff
        this.flatland.addChild(new FpsCounter());
        this.flatland.addChild(layout);
        newMapDialog = new NewMapDialog(this, new Action() {
            @Override
            public void perform() {
                MapWidget map = new MapWidget(newMapDialog.getMapSize(),
                        AppInfo.getDisplayDimension());
                map.setId("map");
                MapEditor.this.flatland.updateChild(map, 0);
                MapEditor.this.updateControllable(map.getId(), map);
            }
        });
        // sidebar buttons
        List<TileLayer> layers = TileImageFactory.getSortedLayerTypes();
        String tab = "";
        Point buttonPos = sidebar.getPosition().move(new Point(50, 500));
        Point iconPos = null;
        SceneGraphBranch<SimpleModel> layout = null;
        for (TileLayer layer : layers) {
            if (!tab.equals(layer.getPriorities()[0])) {
                layout = new BasicSceneGraphBranch<SimpleModel>();
                layout.hide();
                tab = layer.getPriorities()[0];
                iconPos = sidebar.getPosition().move(new Point(50, 450));
                final Button button = AoeButtonFactory.getEditorTab(tab);
                button.setPosition(buttonPos);
                this.flatland.addChild(button);
                this.flatland.addChild(layout);
                buttonPos = buttonPos.move(new Point(50, 0));
                this.tabs.put(tab, layout);
                button.setAction(new Action() {
                    @Override
                    public void perform() {
                        for (String key: MapEditor.this.tabs.keySet()) {
                            SceneGraphBranch<SimpleModel> branch =MapEditor.this.tabs.get(key);
                            if (key.equals(button.getId())) {
                                branch.show();
                            } else {
                                branch.hide();
                            }
                        }
                    }
                });
                this.registerControllable(button);
            }
            SimpleModel layerModel = TerrainCache.addType(new TileDTO(layer
                    .getType(), new PointDTO(0, 0), null, true));
            layerModel.addGeometry(new Rectangle(new Dimension(32, 32),
                    iconPos));
            iconPos = iconPos.move(new Point(0, -50));
            layout.addChild(layerModel);
        }
    }

    @Override
    public void compute() {
    }

    @Override
    public void render() {
        this.flatland.render();
    }

}
