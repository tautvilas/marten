package marten.aoe.server.serializable;

import java.io.Serializable;

public enum ServerNotification implements Serializable {
    GAME_STARTED,
    PLAYER_LIST_UPDATED,
    CREATOR_QUIT,
    NEW_MESSAGE
}
