package structure;

public class Stabilizer extends Structure{
	private Base base;
	private Platform platform;
	public Stabilizer(){
		super();
		base = new Base();
//		platform = new Platform();
		subStructure.add(base);
//		subStructure.add(platform);
	}
}
