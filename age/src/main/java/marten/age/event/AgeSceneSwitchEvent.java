package marten.age.event;

import marten.age.core.AgeScene;

public class AgeSceneSwitchEvent implements AgeEvent {
    public AgeScene newScene;

    public AgeSceneSwitchEvent(AgeScene newScene) {
        this.newScene = newScene;
    }
}
