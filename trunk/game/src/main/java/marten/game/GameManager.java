package marten.game;

import marten.game.map.Map;

/** The main class of the game engine, which governs all the interactions of the game*/
public class GameManager {
    /** All data related to the map of the game*/
    private Map map = null;
    
    /** Provides a handle to the internal representation of the map
     * @return a reference to the game map */
    public Map getMap() {
        return this.map;
    }
    
    /** Forces a creation of a new map of given dimensions */
    public void newMap(int height, int width) {
        this.map = new Map(height, width);
    }
}
