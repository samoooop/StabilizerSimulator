package structure;

public class Stabilizer extends Structure{
	private Structure base;
	private Structure platform;
	private Structure triLeg;
	public Stabilizer(){
		super();
		base = new Base();
		platform = new Platform();
		triLeg = new TriLeg();
		subStructure.add(new Base());
		subStructure.add(new Platform());
		subStructure.add(new TriLeg());
	}
}
