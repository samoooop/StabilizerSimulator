package structure;

import java.awt.Color;

import org.joml.Matrix4f;

public class AdjustParameter {
	public Color color;
	public Matrix4f transformation;

	public AdjustParameter(Color color, Matrix4f transformation) {
		this.color = color;
		this.transformation = new Matrix4f(transformation);
	}
}