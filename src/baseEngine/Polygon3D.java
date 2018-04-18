package baseEngine;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Polygon3D {

	private Point3D[] points;

	private BufferedImage texture;
	private Color fillColor = Color.GRAY;

	public Polygon3D() {
		points = new Point3D[0];
	}
	
	public Polygon3D(Point3D...inputPoints) {
		points = inputPoints;
	}
	
	public Point3D getPoint(int index) {
		return points[index];
	}
	
	public Point3D[] getPoints() {
		return points;
	}

	public List<Point3D[]> getLines() {
		List<Point3D[]> lines = new ArrayList<Point3D[]>();

		for (int i = 0; i < points.length - 1; i++) {
			Point3D[] line = { points[i], points[i + 1] };
			lines.add(line);
		}

		if (points.length > 1) {
			Point3D[] line = { points[points.length - 1], points[0] };
			lines.add(line);
		}

		return lines;
	}
	
	public BufferedImage getTexture() {
		return texture;
	}
	
	public Color getFill() {
		return fillColor;
	}
}
