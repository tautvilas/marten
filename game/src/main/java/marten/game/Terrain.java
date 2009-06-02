package marten.game;

/** Defines the different generic types of terrain, recognized by the game.
 * @author carnifex*/
public enum Terrain {
    /** Denotes terrain with minimum impedance to moving across it. */
    PLAIN,
    /** Denotes terrain, where the movement of ground troops and vehicles is slowed down. */
    DIFFICULT,
    /** Denotes terrain full off obstructions, which may be difficult to navigate for larger units, but provide cover for smaller ones. */
    OBSTRUCTED,
    /** Denotes terrain, which is higher than surroundings and thus provides defensive advantage to occupying forces. */
    ELEVATED,
    /** Denotes terrain, which is significantly higher than its surroundings, providing major defensive advantage, but very difficult to traverse. */
    MOUNTAINOUS,
    /** Denotes terrain, which was specifically altered to provide maximum defense for its occupants. */
    FORTIFIED,
    /** Denotes terrain, where ground troops are severely impeded, however, marine units are at their maximum effectiveness. */
    LIQUID,
    /** Denotes deep chasms and the void of the space, where there is literally nothing. Only air units may traverse this terrain. */
    VOID
}
