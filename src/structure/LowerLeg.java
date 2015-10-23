package structure;

import org.joml.Vector3f;

public class LowerLeg extends Structure {
	protected UpperLeg upperLeg;

	public LowerLeg() {
		super();
		this.start = new Vector3f();
		this.end = new Vector3f(0.0f, 1.0f, 0.0f);
		upperLeg = new UpperLeg();
		subStructure.add(upperLeg);
	}
}
