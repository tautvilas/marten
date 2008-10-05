package marten.util;

public final class Rotation {
	public double x = 0.0, y = 0.0, z = 0.0, w = 1.0;
	public static final Rotation ZERO = new Rotation();
	public Rotation () {}
	public Rotation (Rotation source) {
		this.x = source.x;
		this.y = source.y;
		this.z = source.z;
		this.w = source.w;
	}
	public Rotation (Vector axis, double angle) {
		this.set(axis, angle);
	}
	public Rotation (double newX, double newY, double newZ, double newW) {
		this.x = newX;
		this.y = newY;
		this.z = newZ;
		this.w = newW;
	}
	public synchronized void set(Vector axis, double angle) {
		this.w = Math.cos(angle / 2.0);
		Vector normal = axis.normalize();
		double sin = Math.sin(angle / 2.0);
		this.x = normal.x * sin;
		this.y = normal.y * sin;
		this.z = normal.z * sin;
	}
	public synchronized double getAngle () {
		return 2.0 * Math.acos(this.w);
	}
	public synchronized Vector getAxis () {
		Vector temp = new Vector();
		temp.x = this.x;
		temp.y = this.y;
		temp.z = this.z;
		return temp.normalize();
	}
	public synchronized boolean equals (Rotation other) {
		return (this.x == other.x) && (this.y == other.y) && (this.z == other.z) && (this.w == other.w);
	}
	public synchronized Rotation scale (double scaleFactor) {
		Rotation temp = new Rotation ();
		temp.set(this.getAxis(), this.getAngle() * scaleFactor);
		return temp;
	}
	public synchronized Rotation _scale (double scaleFactor) {
		this.set(this.getAxis(), this.getAngle() * scaleFactor);
		return new Rotation(this);
	}
	public synchronized Rotation multiply (Rotation other) {
		Rotation temp = new Rotation();
		temp.w = this.w * other.w - this.x * other.x - this.y * other.y - this.z * other.z;
		temp.x = this.x * other.w + this.w * other.x + this.y * other.z - this.z * other.y;
		temp.y = this.y * other.w + this.w * other.y + this.z * other.x - this.x * other.z;
		temp.z = this.z * other.w + this.w * other.z + this.x * other.y - this.y * other.x;
		return temp;
	}
	public synchronized Rotation _multiply (Rotation other) {
		Rotation temp = new Rotation();
		temp.w = this.w * other.w - this.x * other.x - this.y * other.y - this.z * other.z;
		temp.x = this.x * other.w + this.w * other.x + this.y * other.z - this.z * other.y;
		temp.y = this.y * other.w + this.w * other.y + this.z * other.x - this.x * other.z;
		temp.z = this.z * other.w + this.w * other.z + this.x * other.y - this.y * other.x;
		this.x = temp.x;
		this.y = temp.y;
		this.z = temp.z;
		this.w = temp.w;
		return temp;
	}
	public synchronized Rotation multiplyLimited (Rotation other) {
		Rotation temp = this.multiply(other);
		if (temp.getAngle() > Math.PI / 2)
			temp = new Rotation (temp.getAxis(), Math.PI / 2);
		return temp;
	}
	public synchronized Rotation _multiplyLimited (Rotation other) {
		Rotation temp = this.multiply(other);
		if (temp.getAngle() > Math.PI / 2)
			temp = new Rotation (temp.getAxis(), Math.PI / 2);
		this.x = temp.x;
		this.y = temp.y;
		this.z = temp.z;
		this.w = temp.w;		
		return temp;
	}
	public synchronized Rotation inverse () {
		Rotation temp = new Rotation();
		temp.x = -this.x;
		temp.y = -this.y;
		temp.z = -this.z;
		temp.w = this.w;
		return temp;
	}
	public synchronized Rotation _inverse () {
		this.x *= -1.0;
		this.y *= -1.0;
		this.z *= -1.0;
		return new Rotation(this);
	}
	public void normalize () {
		double length = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
		if (length == 0.0)
			throw new RuntimeException ("Attempted to normalize rotation with all four fields set to zero.");
		this.x /= length;
		this.y /= length;
		this.z /= length;
		this.w /= length;
	}
	public synchronized String toString () {
		return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + ")";
	}
}
