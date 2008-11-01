package marten.cage;

import marten.util.Rotation;

public class Collider {
    public static boolean collides(Collidable c1, Collidable c2) {
	return false;
    }
    
    public static Rotation getCollisionAngle(Collidable c1, Collidable c2) {
	return new Rotation();
    }
}
