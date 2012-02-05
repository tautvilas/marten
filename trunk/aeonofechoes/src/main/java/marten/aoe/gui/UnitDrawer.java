package marten.aoe.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import marten.age.graphics.SceneGraphBranch;
import marten.age.graphics.SceneGraphChild;
import marten.age.graphics.primitives.Point;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.dto.UnitDTO;
import marten.aoe.gui.widget.UnitWidget;

import org.apache.log4j.Logger;

public class UnitDrawer {
    private HashMap<Integer, UnitData> units = new HashMap<Integer, UnitDrawer.UnitData>();

    private SceneGraphBranch<SceneGraphChild> context;

    private static org.apache.log4j.Logger log = Logger
            .getLogger(UnitDrawer.class);

    public UnitDrawer(SceneGraphBranch<SceneGraphChild> context) {
        this.context = context;
    }

    public void updateTile(TileDTO oldTile, TileDTO tile, Point tileDisplayCoordinates) {
        UnitDTO unitDTO = tile.getUnit();
        UnitDTO oldUnit = null;
        if (oldTile != null) {
            oldUnit = oldTile.getUnit();
        }
        PointDTO coords = tile.getCoordinates();
        if (unitDTO != null) {
            int id = unitDTO.getId();
            if (!this.units.containsKey(id)) {
                UnitWidget unit = new UnitWidget(tile.getUnit());
                unit.setPosition(new Point(tileDisplayCoordinates.x + 64 / 2
                        - unit.getDimension().width / 2, tileDisplayCoordinates.y
                        + 64 / 2 - unit.getDimension().height / 2));
                this.context.addChild(unit);
                this.units.put(tile.getUnit().getId(), new UnitData(unit, coords));
                log.info("Unit " + id + " entered " + coords);
            } else {
                UnitData unit = this.units.get(id);
                unit.widget.setPosition(new Point(tileDisplayCoordinates.x + 64 / 2
                        - unit.widget.getDimension().width / 2, tileDisplayCoordinates.y
                        + 64 / 2 - unit.widget.getDimension().height / 2));
                unit.widget.update(unitDTO);
                unit.path.add(coords);
                log.info("Unit " + id + " moved to " + coords);
            }
        } else if (oldUnit != null){
            UnitData unit = this.units.get(oldUnit.getId());
            if (unit.path.get(unit.path.size() - 1).equals(coords)) {
                this.context.removeChild(unit.widget);
                this.units.remove(oldUnit.getId());
            }
        }
    }

    private class UnitData {
        public UnitWidget widget;
        public List<PointDTO> path = new ArrayList<PointDTO>();
        public UnitData(UnitWidget widget, PointDTO coords) {
            path.add(coords);
            this.widget = widget;
        }
    }
}
