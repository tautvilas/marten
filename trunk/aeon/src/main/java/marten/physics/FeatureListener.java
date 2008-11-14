package marten.physics;

import marten.util.Point;
import marten.util.Rotation;
import marten.util.Vector;

public interface FeatureListener {
	void featureChange (Feature feature, String parameter, double value);
	void featureChange (Feature feature, String parameter, Vector value);
	void featureChange (Feature feature, String parameter, Point value);
	void featureChange (Feature feature, String parameter, Rotation value);
	void featureChange (Feature feature, String parameter, Object value);
}
