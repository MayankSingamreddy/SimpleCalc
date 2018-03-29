
/*The identifier class is used to store values into variables. It finds
*    the associated value based on the name of a variable, and pass that
*    value into the evaluateExpression
*    method, so no edits were necessary to the stack alg.
*/
public class Identifier {

	private String name;
	private double value;

	public Identifier (String n, double v) {
		name = n;
		value = v;
	}

	public String getName() { return name; }

	public double getValue() { return value; }
}