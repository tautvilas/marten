package marten.aoe.engine;

/** The enumerator of all possible events pertaining to the whole map.
 * @author Petras Ražanskas*/
public enum GlobalEvent {
    /** This event is invoked whenever the whole map is freshly reloaded.*/
    MAP_CHANGE,
    /** This event is invoked whenever the acting player announces the end of his turn.*/
    TURN_END
}
