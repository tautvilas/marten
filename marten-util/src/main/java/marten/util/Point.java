package marten.util;

/**
 * A class to simulate three-dimensional points.
 * 
 * @author Petras Razanskas
 */
public final class Point {
    /** The origin point. */
    public static final Point ZERO = new Point();
    /** The <code>x</code> coordinate of this point. */
    public double x = 0.0;
    /** The <code>y</code> coordinate of this point. */
    public double y = 0.0;
    /** The <code>z</code> coordinate of this point. */
    public double z = 0.0;

    /** An empty constructor, which creates the origin point. */
    public Point() {
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * A copy constructor, which creates a point, identical to the <b>source</b>
     * point.
     * 
     * @param source
     *                the source point, copy of which is being made.
     */
    public Point(Point source) {
        this.x = source.x;
        this.y = source.y;
        this.z = source.z;
    }

    /**
     * A constructor, which creates a point that would be pointed to by the
     * <b>source</b> vector if it started from the origin.
     * 
     * @param source
     *                the vector, which points to the point being created from
     *                the origin.
     */
    public Point(Vector source) {
        this.x = source.x;
        this.y = source.y;
        this.z = source.z;
    }

    /**
     * A constructor, which creates a point that is somewhere along the axis of
     * the rotation quaternion centered at the origin.
     * 
     * @param source
     *                the quaternion along the axis of which the point is
     */
    public Point(Rotation source) {
        this.x = source.x;
        this.y = source.y;
        this.z = source.z;
    }

    /**
     * A constructor, which creates a point from the three given
     * <code>double</code> coordinates.
     * 
     * @param newX
     *                the <code>x</code> coordinate of the new point.
     * @param newY
     *                the <code>y</code> coordinate of the new point.
     * @param newZ
     *                the <code>z</code> coordinate of the new point.
     */
    public Point(double newX, double newY, double newZ) {
        this.x = newX;
        this.y = newY;
        this.z = newZ;
    }

    /**
     * Compares the two points for equality.
     * 
     * @param other
     *                the point to which this point is compared to.
     * @return <code>true</code> if the points are identical,
     *         <code>false</code> otherwise.
     */
    public synchronized boolean equals(Point other) {
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }

    /**
     * Compares the two points for approximate equality.
     * 
     * @param other
     *                the point to which this point is compared to.
     * @param epsilon
     *                the maximum difference between the respective coordinates
     *                for the points to be still considered equal
     * @return <code>true</code> if the points are within <b>epsilon</b>
     *         length units of each other, <code>false</code> otherwise.
     * @throws <code>RuntimeException</code> if the <b>epsilon</b> is
     *                 negative.
     */
    public synchronized boolean approximatelyEquals(Point other, double epsilon) {
        if (epsilon < 0.0)
            throw new RuntimeException(
                    "Attempted approximate comparison with epsilon less than zero.");
        return (Math.abs(this.x - other.x) <= epsilon)
                && (Math.abs(this.y - other.y) <= epsilon)
                && (Math.abs(this.z - other.z) <= epsilon);
    }

    /**
     * @param scaleFactor
     *                the <code>double</code> value, signifying how much
     *                further than this point the new point will be from the
     *                origin.
     * @return a point that is in the same direction away from the origin, but
     *         <b>scaleFactor</b> times further.
     */
    public synchronized Point scale(double scaleFactor) {
        return new Point(this.x * scaleFactor, this.y * scaleFactor, this.z
                * scaleFactor);
    }

    /**
     * @param other
     *                the vector according to which this point is moved.
     * @return a point that is a copy of this point, only moved away in the
     *         direction, given by the <b>other</b> vector.
     */
    public synchronized Point move(Vector other) {
        return new Point(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    /**
     * @param other
     *                the point to where the origin would be moved.
     * @return a point that is a copy of this point when the origin point is
     *         moved to the <b>other</b> point.
     */
    public synchronized Point move(Point other) {
        return new Point(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    /**
     * @param other
     *                the point to which the distance is measured.
     * @return the distance between this and <b>other</b> points. Use
     *         {@link #distanceSquared(Point)} if you need this value for
     *         comparison since it is calculated faster.
     */
    public synchronized double distance(Point other) {
        return new Vector(this, other).length();
    }

    /**
     * @param other
     *                the point to which the distance is measured.
     * @return the square of the distance between this and <b>other</b> points.
     */
    public synchronized double distanceSquared(Point other) {
        return new Vector(this, other).lengthSquared();
    }

    /**
     * @param other
     *                the rotation pattern, centered around the origin point.
     * @return an image of this point as if the coordinate system was rotated
     *         around the origin.
     */
    public synchronized Point rotate(Rotation other) {
        Rotation temp = new Rotation(this);
        temp = other.multiply(temp);
        temp = temp.multiply(other.inverse());
        return new Point(temp);
    }

    /**
     * @param other
     *                a point to which the mid-point is being calculated.
     * @return a point that is right in the middle between this and <b>other</b>
     *         points.
     */
    public synchronized Point midPoint(Point other) {
        return new Point((this.x + other.x) / 2.0, (this.y + other.y) / 2.0,
                (this.z + other.z) / 2.0);
    }

    /**@return a <code>String</code> representation of this point to facilitate the output.*/
    @Override
    public synchronized String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }
}
