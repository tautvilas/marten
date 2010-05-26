package marten.testing.tests;

import marten.age.core.AgeApp;
import marten.testing.tests.maptest.Map;

public class MapTest extends AgeApp {

    @Override
    public void configure() {
        this.setActiveScene(new Map());
    }

    @Override
    public void finalize() {
        // do nothing
    }
}
