package marten.util;

public final class Point {
	public static final Point ZERO = new Point();
	public double x = 0.0, y = 0.0, z = 0.0;
	public Point () {}
	public Point (Point source) {
		this.x = source.x;
		this.y = source.y;
		this.z = source.z;
	}
	public Point (double newX, double newY, double newZ) {
		this.x = newX;
		this.y = newY;
		this.z = newZ;
	}
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
	public void setZ(double z) {
		this.z = z;
	}
	public synchronized boolean equals (Point other) {
		return this.x == other.x && this.y == other.y && this.z == other.z;
	}
	public synchronized boolean approximatelyEquals (Point other, double epsilon) {
		if (epsilon < 0.0)
			throw new RuntimeException ("Attempted approximate comparison with epsilon less than zero.");
		return (this.x > other.x * (1 - epsilon)) && (this.x < other.x * (1 + epsilon)) && (this.y > other.y * (1 - epsilon)) && (this.y < other.y * (1 + epsilon)) && (this.z > other.z * (1 - epsilon)) && (this.z < other.z * (1 + epsilon)); 			
	}
	public synchronized Point scale (double scaleFactor) {
		Point temp = new Point (this);
		temp.x *= scaleFactor;
		temp.y *= scaleFactor;
		temp.z *= scaleFactor;
		return temp;
	}
	public synchronized Point _scale (double scaleFactor) {
		this.x *= scaleFactor;
		this.y *= scaleFactor;
		this.z *= scaleFactor;
		return new Point (this);
	}
	public synchronized Point move (Vector other) {
		Point temp = new Point (this);
		temp.x += other.x;
		temp.y += other.y;
		temp.z += other.z;
		return temp;
	}
	public synchronized Point move (Point other) {
		Point temp = new Point (this);
		temp.x += other.x;
		temp.y += other.y;
		temp.z += other.z;
		return temp;
	}
	public synchronized Point _move (Vector other) {
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;
		return new Point (this);
	}
	public synchronized Point _move (Point other) {
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;
		return new Point (this);
	}
	public synchronized double distance (Point other) {
		return new Vector(this, other).length();
	}
	public synchronized double distanceSquared (Point other) {
		return new Vector(this, other).lengthSquared();
	}
	public synchronized Point rotate (Rotation other) {
		Point temp = new Point ();
		temp.x = other.w * other.w * this.x + other.x * other.x * this.x + other.y * other.y * this.x - other.z * other.z * this.x + 2 * other.x * other.z * this.z - 2 * other.w * other.z * this.y;
		temp.y = other.w * other.w * this.y - other.x * other.x * this.y + other.y * other.y * this.y + other.z * other.z * this.y + 2 * other.x * other.y * this.x - 2 * other.w * other.x * this.z;
		temp.z = other.w * other.w * this.z + other.x * other.x * this.z - other.y * other.y * this.z + other.z * other.z * this.z + 2 * other.y * other.z * this.y - 2 * other.w * other.y * this.x;
		return temp;
	}
	public synchronized Point _rotate (Rotation other) {
		Point temp = new Point ();
		temp.x = other.w * other.w * this.x + other.x * other.x * this.x + other.y * other.y * this.x - other.z * other.z * this.x + 2 * other.x * other.z * this.z - 2 * other.w * other.z * this.y;
		temp.y = other.w * other.w * this.y - other.x * other.x * this.y + other.y * other.y * this.y + other.z * other.z * this.y + 2 * other.x * other.y * this.x - 2 * other.w * other.x * this.z;
		temp.z = other.w * other.w * this.z + other.x * other.x * this.z - other.y * other.y * this.z + other.z * other.z * this.z + 2 * other.y * other.z * this.y - 2 * other.w * other.y * this.x;
		this.x = temp.x;
		this.y = temp.y;
		this.z = temp.z;
		return temp;
	}
	public synchronized Point midPoint (Point other) {
		return new Point ((this.x + other.x)/2.0, (this.y + other.y)/2.0, (this.z + other.z)/2.0);
	}
	@Override public synchronized String toString () {
		return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}
}
