package structure;

public class Stabilizer extends Structure{
	public Base base;
	public Platform platform;
	public Stabilizer(){
		super();
		base = new Base();
		platform = new Platform();
		subStructure.add(base);
		subStructure.add(platform);
	}
}
