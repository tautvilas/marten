package marten.aoe.test;

import java.util.List;

import marten.aoe.data.units.Dwarf;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.engine.Engine;
import marten.aoe.engine.EngineListener;
import marten.aoe.engine.GlobalEvent;
import marten.aoe.engine.LocalEvent;
import marten.aoe.engine.Map;
import marten.aoe.engine.PathFinder;
import marten.aoe.engine.Tile;
import marten.aoe.engine.loader.MapLoader;

import org.junit.Test;

public class PathFinderTest {
    @Test
    public void SimpleTest() {
        PlayerDTO[] players = {new PlayerDTO(0, "player")};
        Engine engine = new Engine("Freelands", players);
        Map map = MapLoader.loadMap(engine, "Freelands");
        engine.addListener(new EngineListener() {
            @Override
            public void onGlobalEvent(GlobalEvent event) {
            }
            @Override
            public void onLocalEvent(LocalEvent event, TileDTO tileDTO) {
            }}, players[0]);
        new Dwarf(players[0], map.getTile(new PointDTO(13, 6)));
        PathFinder finder = new PathFinder(map, map.getTile(new PointDTO(13, 6)));
        List<Tile> path = finder.findPathTo(map.getTile(new PointDTO(13, 7)));
        for (Tile tile : path) {
            System.out.println(tile.getCoordinates());
        }
    }
}
