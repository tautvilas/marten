package marten.util;

/**A class used to pass cosine function into the body of another function.
 * @author Petras Razanskas*/
public final class Cosine implements FunctionR2R {
	/**Calculates the cosine of <b>t</b> (in radians)
	 * @param t The angle in radians
	 * @return The cosine of the angle <b>t</b>*/
	public double calculate(double t) {
		return Math.cos(t);
	}
}
