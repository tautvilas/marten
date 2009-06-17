package marten.game;

public enum ParserState {
    /**The state where the action was carried out normally*/
    SUCCESS,
    /**The state where the action failed, but it is possible to ignore the error*/
    WARNING,
    /**The state where the action failed and further work is thus impossible*/
    FAILURE
}
