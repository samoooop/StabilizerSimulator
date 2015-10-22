package structure;

public class LowerLeg extends Structure{
	protected UpperLeg upperLeg;
	public LowerLeg(){
		super();
		upperLeg = new UpperLeg();
		subStructure.add(upperLeg);
	}
}
