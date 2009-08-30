package marten.testing.tests;

import marten.age.core.AgeApp;
import marten.testing.tests.loadingtest.FirstScene;

public class LoadingTest extends AgeApp {

    @Override
    public void configure() {
        this.setActiveScene(new FirstScene());
    }

    @Override
    public void finalize() {
        // do nothing
    }
}
