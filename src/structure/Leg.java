package structure;

public class Leg extends Structure{
	private UpperLeg upperLeg;
	private LowerLeg lowerLeg;
	public Leg(){
		super();
		lowerLeg = new LowerLeg();
		subStructure.add(lowerLeg);
		upperLeg = lowerLeg.upperLeg;
	}
}
