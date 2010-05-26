package marten.testing.tests;

import marten.age.core.AgeApp;
import marten.testing.tests.networktest.NetworkScene;

public class NetworkTest extends AgeApp {

    @Override
    public void configure() {
        this.setActiveScene(new NetworkScene());
    }

    @Override
    public void finalize() {
    }

}
