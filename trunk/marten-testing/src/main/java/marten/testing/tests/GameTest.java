package marten.testing.tests;

import marten.age.event.AgeEvent;
import marten.age.util.AgeApp;
import marten.age.util.AgeScene;

public class GameTest extends AgeApp implements AgeScene {

    @Override
    public void init() {
    }

    @Override
    public void handle(AgeEvent e) {
    }

    @Override
    public void compute() {
    }

    @Override
    public void render() {
    }

    @Override
    public void cleanup() {
    }

    /*** AgeApp methods ***/

    @Override
    public void configure() {
        this.setActiveScene(this);
    }

    @Override
    public void finalize() {
    }
}
