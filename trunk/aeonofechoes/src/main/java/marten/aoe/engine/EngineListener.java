package marten.aoe.engine;

import marten.aoe.dto.TileDTO;

/** This interface must be implemented by any class which wants to get event notifications from Engine class.
 * @author Petras Ražanskas
 * @see marten.aoe.engine.Engine*/
public interface EngineListener {
    /** This method is invoked whenever an event affecting only one tile happens.
     * @param event - the type of event that happened.
     * @param tileDTO - the necessary data of the changed tile.*/
    void onLocalEvent(LocalEvent event, TileDTO tileDTO);
    /** This method is invoked whenever an event affecting the whole map happens.
     * @param event - the type of event that happened.*/
    void onGlobalEvent(GlobalEvent event);
}
