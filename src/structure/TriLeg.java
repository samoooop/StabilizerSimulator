package structure;

public class TriLeg extends Structure{
	private Leg[] leg = new Leg[3];
	public TriLeg(){
		super();
		leg[0] = new Leg(); // Leg number 1
		leg[1] = new Leg(); // Leg number 2
		leg[2] = new Leg(); // Leg number 3
	}
}
