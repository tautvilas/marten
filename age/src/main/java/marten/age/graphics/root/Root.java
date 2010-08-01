package marten.age.graphics.root;

import java.util.ArrayList;

import marten.age.graphics.BasicSceneGraphChild;
import marten.age.graphics.BasicSceneGraphParent;
import marten.age.graphics.SceneGraphChild;
import marten.age.graphics.SceneGraphNode;
import marten.age.graphics.SceneGraphParent;
import marten.age.graphics.geometry.GeneratedGeometry;
import marten.age.graphics.geometry.Geometry;
import marten.age.graphics.geometry.GeometryCache;
import marten.age.graphics.geometry.OptimizedGeometry;
import marten.age.graphics.geometry.SimpleModel;
import marten.age.graphics.model.ComplexModel;

import org.apache.log4j.Logger;

public abstract class Root extends BasicSceneGraphParent {
    private static org.apache.log4j.Logger log = Logger.getLogger(Root.class);

    public void compile() {
        compileSceneParent(this);
    }

    private void compileSceneParent(SceneGraphParent parent) {
        ArrayList<SceneGraphChild> parentBranches = parent.getBranches();
        for (SceneGraphNode child : parentBranches) {
            if (child instanceof BasicSceneGraphParent) {
                compileSceneParent((SceneGraphParent) child);
            } else if (child instanceof BasicSceneGraphChild) {
                compileSceneNode(child);
            }
        }
    }

    private void compileSceneNode(SceneGraphNode node) {
        if (node instanceof SimpleModel) {
            SimpleModel sm = (SimpleModel) node;
            Geometry g = sm.getGeometry();
            sm.setGeometry(optimizeGeometry(g));
        } else if (node instanceof ComplexModel) {
            ComplexModel cm = (ComplexModel) node;
            ArrayList<SimpleModel> parts = cm.getParts();
            for (SimpleModel part : parts) {
                part.setGeometry(optimizeGeometry(part.getGeometry()));
            }
        }
    }

    private Geometry optimizeGeometry(Geometry g) {
        Geometry g2 = GeometryCache.get(g);
        if (g2 != null) {
            return g2;
        }
        if (!(g instanceof GeneratedGeometry)) {
            log.debug("Optimizing geometry " + g);
            OptimizedGeometry og2 = new OptimizedGeometry(g);
            og2.generate();
            GeometryCache.add(og2);
            return og2;
        }
        return g;
    }
}
