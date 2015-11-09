package structure;

import org.joml.Vector3f;

public class Stabilizer extends Structure{
	public Base base;
	public Platform platform;
	public Stabilizer(){
		super();
		base = new Base();
		platform = new Platform(this);
		subStructure.add(base);
		subStructure.add(platform);
		platform.adjustTriLeg();
	}
}
