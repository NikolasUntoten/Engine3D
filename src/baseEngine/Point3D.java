package baseEngine;

/*
 * Written By Nikolas Gaub, 8/28/2017
 * 
 * Point3D stores the coordinates of a point in 3 dimensions, and allows for the translation of said point.
 */

public class Point3D {
	public double x;
	public double y;
	public double z;
	
	public Point3D() {
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Point3D(double initX, double initY, double initZ) {
		x = initX;
		y = initY;
		z = initZ;
	}
	
	public double distance(Point3D other) {
		double dx = other.x - x;
		double dy = other.y - y;
		double dz = other.z - z;
		return Math.sqrt(dx*dx + dy*dy + dz*dz);
	}
	
	public boolean equals(Object other) {
		if (other instanceof Point3D) {
			Point3D otherPoint = (Point3D) other;
			return x == otherPoint.x && y == otherPoint.y && z == otherPoint.z;
		} else {
			return false;
		}
	}
	
	public Point3D copy() {
		return new Point3D(x, y, z);
	}
	
	public String toString() {
		return "(" + x + ", " + y + ". " + z + ")";
	}
}
