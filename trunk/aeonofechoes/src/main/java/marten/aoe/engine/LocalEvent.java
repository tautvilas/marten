package marten.aoe.engine;

/** The enumerator of all possible events pertaining to a single location on the map.
 * @author Petras Ražanskas*/
public enum LocalEvent {
    /** This event is invoked whenever a unit on the map suffers a reduction of hit points.*/
    UNIT_HURT,
    /** This event is invoked whenever a unit loses its last hit point and is removed from the map.*/
    UNIT_DEAD,
    /** This event is invoked whenever a unit enters the location or is created there.*/
    UNIT_ENTRY,
    /** This event is invoked whenever a unit exits the location or is destroyed there.*/
    UNIT_EXIT,
    /** This event is invoked whenever the location is changed into another.*/
    TILE_CHANGE,
    /** This event is invoked whenever it is detected that previously unexplored tile is now visible.*/
    TILE_EXPLORED,
    /** This event is invoked whenever it is detected that a tile has become visible.*/
    TILE_VISIBLE,
    /** This event is invoked whenever it is detected that a tile has become invisible.*/
    TILE_INVISIBLE,
    /** This event is invoked whenever an object which was previously invisible due to cloaking is revealed.*/
    OBJECT_DETECTED,
    /** This event is invoked whenever an object which was previously visible cloaks and thus becomes invisible.*/
    OBJECT_CLOAKED
}
