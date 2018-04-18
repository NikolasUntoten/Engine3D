package baseEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Written By Nikolas Gaub, 8/28/2017
 * 
 * Object3D stores data about a set of points which form a polygonal object.
 */

public class Object3D {
	
	public static final int DIRECTION_XY = 0;
	public static final int DIRECTION_XZ = 1;
	public static final int DIRECTION_YZ = 2;
	
	
	private List<Polygon3D> sides;
	
	public Object3D() {
		sides = new ArrayList<Polygon3D>();
	}
	
	public void addPolygon(Polygon3D poly) {
		sides.add(poly);
	}
	
	public void addPolygon(Point3D[] points) {
		sides.add(new Polygon3D(points));
	}
	
	public List<Point3D[]> getAllLines() {
		List<Point3D[]> lines = new ArrayList<Point3D[]>();
		for (Polygon3D p : sides) {
			lines.addAll(p.getLines());
		}
		return lines;
	}
	
	public List<Polygon3D> getPolygons() {
		return sides;
	}
	
	public void translate(double deltaX, double deltaY, double deltaZ) {
		//TODO
	}
	
	public void rotate(int degrees, int plane) {
		double theta = degrees * Math.PI / 180.0;
		rotate(theta, plane);
	}
	
	//TODO
	public void rotate(double theta, int plane) {
		switch(plane) {
		case DIRECTION_XY:
			break;
		case DIRECTION_XZ:
			break;
		case DIRECTION_YZ:
			break;
			default: throw new IllegalArgumentException();
		}
	}
}
