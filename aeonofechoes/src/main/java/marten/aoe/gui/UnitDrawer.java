package marten.aoe.gui;

import java.util.HashMap;

import marten.age.graphics.SceneGraphBranch;
import marten.age.graphics.SceneGraphChild;
import marten.age.graphics.primitives.Point;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.gui.widget.UnitWidget;

public class UnitDrawer {
    private HashMap<PointDTO, UnitWidget> units = new HashMap<PointDTO, UnitWidget>();

    private SceneGraphBranch<SceneGraphChild> context;

    public UnitDrawer(SceneGraphBranch<SceneGraphChild> context) {
        this.context = context;
    }

    public void updateTile(TileDTO tile, Point tileDisplayCoordinates) {
        if (this.units.containsKey(tile.getCoordinates())) {
            UnitWidget unit = this.units.get(tile.getCoordinates());
            this.context.removeChild(unit);
            this.units.remove(tile.getCoordinates());
        }
        if (tile.getUnit() != null) {
            UnitWidget unit = new UnitWidget(tile.getUnit());
            unit.setPosition(new Point(tileDisplayCoordinates.x + 64 / 2
                    - unit.getDimension().width / 2, tileDisplayCoordinates.y
                    + 64 / 2 - unit.getDimension().height / 2));
            this.context.addChild(unit);
            units.put(tile.getCoordinates(), unit);
        }
    }
}
