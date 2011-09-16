package marten.aoe.gui.scene.editor;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import marten.age.control.KeyboardController;
import marten.age.control.MouseController;
import marten.age.control.MouseListener;
import marten.age.core.AgeScene;
import marten.age.core.AppInfo;
import marten.age.graphics.BasicSceneGraphBranch;
import marten.age.graphics.SceneGraphBranch;
import marten.age.graphics.SceneGraphChild;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.flat.sprite.TextureSprite;
import marten.age.graphics.layout.SimpleLayout;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.FontCache;
import marten.age.widget.Action;
import marten.age.widget.Button;
import marten.age.widget.obsolete.FpsCounter;
import marten.aoe.dto.MapDTO;
import marten.aoe.dto.MapMetaDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.gui.AoeButtonFactory;
import marten.aoe.gui.TileImageFactory;
import marten.aoe.gui.TileLayer;
import marten.aoe.gui.widget.MapWidget;
import marten.aoe.gui.widget.Sidebar;

public class MapEditor extends AgeScene implements MouseListener {

    BitmapFont font = FontCache.getFont(new Font("Arial", Font.PLAIN, 16));
    private final int MAP_SCROLL_SPEED = 15;

    private Flatland flatland = new Flatland();
    private MouseController mouseController = new MouseController();
    private NewMapDialog newMapDialog;
    private HashMap<String, SceneGraphBranch<SceneGraphChild>> tabs = new HashMap<String, SceneGraphBranch<SceneGraphChild>>();
    private SimpleLayout layout = new SimpleLayout(AppInfo
            .getDisplayDimension());
    private String brush = "Grassland";
    private boolean mouseDown = false;
    private MapWidget map;
    private LoadMapDialog loadMapDialog;
    private LoadMapDialog saveMapDialog;

    public MapEditor() {
        Button newButton = AoeButtonFactory.getEditorButton("New");
        int buttonWidth = (int)newButton.getDimension().width;
        int buttonHeight = (int)newButton.getDimension().height;
        int windowHeight = AppInfo.getDisplayHeight();
        int windowWidth = AppInfo.getDisplayWidth();
        int padding = 5;
        this.addController(this.mouseController);
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
        this.loadMapDialog = new LoadMapDialog(this, new Action() {
            @Override
            public void perform() {
                MapDTO dto = MapDTO.loadFromMapFile(loadMapDialog.getMapName());
                MapEditor.this.map = new MapWidget(dto, AppInfo
                        .getDisplayDimension());
                map.setId("map");
                MapEditor.this.flatland.updateChild(map, 0);
                MapEditor.this.updateControllable(map.getId(), map);
                // MapDTO.writeToMapFile("Free", dto);
                loadMapDialog.hide();
            }
        });
        loadButton.setAction(new Action() {
            @Override
            public void perform() {
                MapEditor.this.loadMapDialog.show();
            }
        });
        this.registerControllable(loadButton);
        this.flatland.addChild(loadButton);
        this.layout.center(this.loadMapDialog);
        // save button
        Button saveButton = AoeButtonFactory.getEditorButton("Save");
        saveButton.setPosition(new Point((buttonWidth + padding) * 2,
                windowHeight - buttonHeight));
        this.flatland.addChild(saveButton);
        this.saveMapDialog = new LoadMapDialog(this, new Action() {
            @Override
            public void perform() {
                List<PointDTO> startPositions = new ArrayList<PointDTO>();
                Collection<TileDTO> tiles = MapEditor.this.map.getTiles();
                for (TileDTO tile : tiles) {
                    if (Arrays.asList(tile.getLayers()).contains("Hq")) {
                        startPositions.add(tile.getCoordinates());
                    }
                }
                Dimension size = map.getSize();
                MapMetaDTO meta = new MapMetaDTO(saveMapDialog.getMapName(),
                        (int)size.width, (int)size.height, startPositions);
                MapDTO dto = new MapDTO(tiles, meta);
                MapDTO.writeToMapFile(saveMapDialog.getMapName(), dto);
                saveMapDialog.hide();
            }
        });
        saveButton.setAction(new Action() {
            @Override
            public void perform() {
                MapEditor.this.saveMapDialog.show();
            }
        });
        this.registerControllable(saveButton);
        this.layout.center(this.saveMapDialog);
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
                map = new MapWidget(newMapDialog.getMapSize(), AppInfo
                        .getDisplayDimension());
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
        int i = 0;
        SceneGraphBranch<SceneGraphChild> layout = null;
        for (final TileLayer layer : layers) {
            if (!tab.equals(layer.getPriorities()[0])) {
                i = 0;
                layout = new BasicSceneGraphBranch<SceneGraphChild>();
                layout.hide();
                tab = layer.getPriorities()[0];
                final Button button = AoeButtonFactory.getEditorTab(tab);
                button.setPosition(buttonPos);
                this.flatland.addChild(button);
                this.flatland.addChild(layout);
                buttonPos = buttonPos.move(new Point(50, 0));
                this.tabs.put(tab, layout);
                button.setAction(new Action() {
                    @Override
                    public void perform() {
                        for (String key : MapEditor.this.tabs.keySet()) {
                            SceneGraphBranch<SceneGraphChild> branch = MapEditor.this.tabs
                                    .get(key);
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
            Button icon = new Button(new TextureSprite(TileImageFactory
                    .getTile(layer.getType()), new Dimension(32, 32)));
            iconPos = sidebar.getPosition().move(
                    new Point(50 + i % 3 * 50, 450 - i / 3 * 50));
            icon.setPosition(iconPos);
            layout.addChild(icon);
            icon.setAction(new Action() {
                @Override
                public void perform() {
                    MapEditor.this.brush = layer.getType();
                }
            });
            this.registerControllable(icon);
            i++;
        }
        this.tabs.get("a").show();
        Button eraser = new Button(new TextureSprite(TileImageFactory
                .getTile("Void"), new Dimension(32, 32)));
        iconPos = sidebar.getPosition().move(new Point(50, 550));
        eraser.setPosition(iconPos);
        this.layout.addChild(eraser);
        eraser.setAction(new Action() {
            @Override
            public void perform() {
                MapEditor.this.brush = "eraser";
            }
        });
        this.registerControllable(eraser);
        this.registerControllable(this);
    }

    private void draw(Point coords) {
        if (this.map == null)
            return;
        TileDTO tile = this.map.tileHit(coords);
        if (tile == null)
            return;
        if (this.brush == "eraser") {
            this.map.updateTile(new TileDTO("Void", tile.getCoordinates(), tile
                    .getUnit()));
        } else {
            this.map.updateTile(TileImageFactory.blendTile(tile, this.brush));
        }
    }

    @Override
    public void compute() {
        if (this.map == null)
            return;
        Point coords = this.mouseController.getMouseCoordinates();
        if (coords.x < 5) {
            map.ScrollLeft(this.MAP_SCROLL_SPEED);
        } else if (coords.x > AppInfo.getDisplayWidth() - 5
                && coords.x < AppInfo.getDisplayWidth()) {
            map.ScrollRight(this.MAP_SCROLL_SPEED);
        } else if (coords.y < 5) {
            map.ScrollDown(this.MAP_SCROLL_SPEED);
        } else if (coords.y > AppInfo.getDisplayHeight() - 5) {
            map.ScrollUp(this.MAP_SCROLL_SPEED);
        }
    }

    @Override
    public void render() {
        this.flatland.render();
    }

    @Override
    public void mouseDown(Point coords) {
        this.mouseDown = true;
        this.draw(coords);
    }

    @Override
    public void mouseMove(Point coords) {
        if (this.mouseDown) {
            this.draw(coords);
        }
    }

    @Override
    public void mouseUp(Point coords) {
        this.mouseDown = false;
    }

    @Override
    public void mouseWheelRoll(int delta, Point coords) {
    }

}
