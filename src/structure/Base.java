package structure;

public class Base extends Structure {
	private TriLeg triLeg;
	public Base(){
		super();
		triLeg = new TriLeg();
		subStructure.add(triLeg);
		createBaseTriangle();
	}
	private void createBaseTriangle(){
		myStructure = new Structure();
	}
	
}
