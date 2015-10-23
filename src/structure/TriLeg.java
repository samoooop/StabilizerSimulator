package structure;

import java.awt.Color;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class TriLeg extends Structure{
	public Leg[] leg = new Leg[6];
	public TriLeg(Structure chamfer){
		super();
		Matrix4f legRotation = new Matrix4f().rotateY((float) Math.toRadians(-120));
		for(int i=0;i<3;i++){//Three corner of legset
			leg[i*2] = new Leg();
			leg[i*2+1] = new Leg();
			if(i==0){
				leg[0].location = chamfer.start;
				leg[1].location = chamfer.end;
			}
			else{
				Vector4f evenLegLocation = new Vector4f(leg[(i-1)*2].location,0);
				Vector4f oddLegLocation = new Vector4f(leg[(i-1)*2+1].location,0);
				evenLegLocation.mul(legRotation);
				oddLegLocation.mul(legRotation);
				leg[i*2].location.add(evenLegLocation.x,evenLegLocation.y,evenLegLocation.z);
				leg[i*2+1].location.add(oddLegLocation.x,oddLegLocation.y,oddLegLocation.z);
	
			}
			leg[i*2].color = Color.BLUE;
			leg[i*2+1].color = Color.RED;
			subStructure.add(leg[i*2]);
			subStructure.add(leg[i*2+1]);
		}
	}
}


