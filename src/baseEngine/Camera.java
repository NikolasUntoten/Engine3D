package baseEngine;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import polygonEngine.PolygonWorld;

public class Camera extends JPanel {

	// width/height of world shown on screen by camera
	private final static double FOV = 0.5;

	// current angle of camera on XZ plane
	protected double angleXZ;

	// current angle of camera of YZ plane
	protected double angleYZ;

	// current absolute position of camera
	protected Point3D position;

	// current world being viewed by camera
	protected World world;

	// image overlay for HUD/text displays
	protected BufferedImage overlay;

	public Camera() {
		super();
		angleXZ = 0;
		angleYZ = 0;
		position = new Point3D(0, 0, 0);
	}
	
	public void changeAngleXZ(double deltaAngle) {
		angleXZ += deltaAngle;
	}
	
	public void setAngleXZ(double newAngle) {
		angleXZ = newAngle;
	}
	
	public double getAngleXZ() {
		return angleXZ;
	}
	
	public void changeAngleYZ(double deltaAngle) {
		angleYZ += deltaAngle;
	}
	
	public void setAngleYZ(double newAngle) {
		angleYZ = newAngle;
	}
	
	public double getAngleYZ() {
		return angleYZ;
	}
	
	public void changePosition(double deltaX, double deltaY, double deltaZ) {
		position.x += deltaX;
		position.y += deltaY;
		position.z += deltaZ;
	}
	
	public void setPosition(Point3D newPosition) {
		position = newPosition;
	}
	
	public void setOverlay(BufferedImage image) {
		overlay = image;
	}
	
	public void setWorld(World newWorld) {
		world = newWorld;
	}
	
	public World getWorld() {
		return world;
	}

	// draws a line on the screen, representing the two given points in 3d space
	protected void renderLine(Graphics g, Point3D start, Point3D end, Point3D location) {
		Point3D startTemp = getRelativeCoordinates(start, location);
		Point3D endTemp = getRelativeCoordinates(end, location);

		Point startDraw = getDrawingCoordinates(startTemp, location);
		Point endDraw = getDrawingCoordinates(endTemp, location);

		g.drawLine(startDraw.x, startDraw.y, endDraw.x, endDraw.y);
	}

	// returns the coordinates of a given absolute point as the relative
	// coordinates based on camera location and angle
	protected Point3D getRelativeCoordinates(Point3D absolutePoint, Point3D location) {

		double dx = absolutePoint.x - location.x;
		double dy = absolutePoint.y - location.y;
		double dz = absolutePoint.z - location.z;

		double cosXZ = Math.cos(angleXZ);
		double sinXZ = Math.sin(angleXZ);
		double cosYZ = Math.cos(angleYZ);
		double sinYZ = Math.sin(angleYZ);

		double relX = dx * cosXZ + dz * sinXZ;
		double tempZ = dz * cosXZ - dx * sinXZ;
		double relY = dy * cosYZ - tempZ * sinYZ;
		double relZ = dy * sinYZ + tempZ * cosYZ;

		Point3D relPoint = new Point3D(relX, relY, relZ);
		return relPoint;
	}

	// returns the coordinates of a point in 2 dimensional space that represents
	// the 3 dimensional location
	// translation based on theory of smaller theta(See notes, unable to
	// document here)
	protected Point getDrawingCoordinates(Point3D point, Point3D location) {
		double thetaXZ = Math.atan2(point.x, point.z);
		double thetaYZ = Math.atan2(point.y, point.z);
		
		//double thetaXZ = point.x / point.z;
		//double thetaYZ = point.y / point.z;

		int w = getWidth() / 2;
		int h = getHeight() / 2;

		double drawX = w + (thetaXZ * w / FOV);
		double drawY = h + (thetaYZ * h / FOV);

		Point drawPoint = new Point((int) drawX, (int) drawY);
		return drawPoint;
	}
}
