package marten.aoe.engine;

/** The enumerator of all possible events pertaining to the whole map.
 * @author Petras Ražanskas*/
public enum GlobalEvent {
    PLAYER_REFRESH,
    /** This event is invoked whenever the map is loaded for the first time.*/
    MAP_INITIATED,
    /** This event is invoked whenever the whole map is freshly reloaded.*/
    MAP_CHANGE,
    /** This event is invoked whenever the map is terminated.
     * A listener should not expect to get any more events from its monitor.*/
    MAP_TERMINATED,
    /** This event is invoked whenever the acting player announces the end of his turn.*/
    TURN_END,
    /** This event precedes any burst of information from the server.*/
    STREAM_START,
    /** This event comes after all information about changes was transmitted.*/
    STREAM_END
}
