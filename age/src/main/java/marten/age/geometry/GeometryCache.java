package marten.age.geometry;

import java.util.HashSet;

import org.apache.log4j.Logger;

public class GeometryCache {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(GeometryCache.class);
 // TODO(zv): use hash map
    private static HashSet<Geometry> geometryCache = new HashSet<Geometry>();

    public static Geometry get(Geometry geometry) {
        for (Geometry g : geometryCache) {
            if (g.equals(geometry)) {
                log.debug("Found geometry in the cache returning " + g
                        + " for " + geometry);
                return g;
            }
        }
        return null;
    }

    public static void add(Geometry geometry) {
        if (geometryCache.contains(geometry)) {
            log.error("You try to add geometry that is allready in the cache");
        } else {
            geometryCache.add(geometry);
            log.debug("Added geometry " + geometry + " to the cache. Size = "
                    + geometryCache.size());
        }
    }
}
