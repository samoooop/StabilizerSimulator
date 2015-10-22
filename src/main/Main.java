package main;

import org.joml.Matrix4f;

import simulator.Renderer;

public class Main {
	public static void main(String[] args){
		Matrix4f a = new Matrix4f();
		a.translation(-1, 0, 0);
		a.rotateY((float)Math.PI);
		System.out.println(a);
		Renderer sim = new Renderer();
		sim.run();
		
		
		
	}
}
