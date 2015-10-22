package structure;

public class Leg extends Structure{
	private Structure upperLeg;
	private Structure lowerLeg;
	public Leg(){
		super();
		subStructure.add(lowerLeg);
		lowerLeg.subStructure.add(upperLeg);
	}
}
