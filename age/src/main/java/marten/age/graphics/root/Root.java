package marten.age.graphics.root;

import java.util.ArrayList;

import marten.age.graphics.BasicSceneGraphChild;
import marten.age.graphics.BasicSceneGraphBranch;
import marten.age.graphics.SceneGraphChild;
import marten.age.graphics.SceneGraphNode;
import marten.age.graphics.SceneGraphBranch;
import marten.age.graphics.geometry.GeneratedGeometry;
import marten.age.graphics.geometry.Geometry;
import marten.age.graphics.geometry.GeometryCache;
import marten.age.graphics.geometry.OptimizedGeometry;
import marten.age.graphics.model.ComplexModel;
import marten.age.graphics.model.SimpleModel;

import org.apache.log4j.Logger;

public abstract class Root extends BasicSceneGraphBranch {
    private static org.apache.log4j.Logger log = Logger.getLogger(Root.class);

    public void compile() {
        compileSceneParent(this);
    }

    private void compileSceneParent(SceneGraphBranch parent) {
        ArrayList<SceneGraphChild> parentBranches = parent.getBranches();
        for (SceneGraphNode child : parentBranches) {
            if (child instanceof BasicSceneGraphBranch) {
                compileSceneParent((SceneGraphBranch) child);
            } else if (child instanceof BasicSceneGraphChild) {
                compileSceneNode(child);
            }
        }
    }

    private void compileSceneNode(SceneGraphNode node) {
        if (node instanceof SimpleModel) {
            SimpleModel sm = (SimpleModel) node;
            optimizeGeometry(sm);
        } else if (node instanceof ComplexModel) {
            ComplexModel cm = (ComplexModel) node;
            ArrayList<SimpleModel> parts = cm.getParts();
            for (SimpleModel part : parts) {
                optimizeGeometry(part);
            }
        }
    }

    private void optimizeGeometry(SimpleModel sm) {
        ArrayList<Geometry> geometries = sm.getGeometries();
        ArrayList<Geometry> optimizedGeometries = new ArrayList<Geometry>();
        for (Geometry g : geometries) {
            optimizedGeometries.add(optimizeGeometry(g));
        }
        sm.setGeometries(optimizedGeometries);
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
