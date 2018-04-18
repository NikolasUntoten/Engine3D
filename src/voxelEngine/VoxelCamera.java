package voxelEngine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import baseEngine.Camera;
import baseEngine.Point3D;
import polygonEngine.PolygonWorld;

public class VoxelCamera extends Camera {

	// image overlay for HUD/text displays
	private BufferedImage overlay;

	public VoxelCamera() {
		super();
		world = new Sector(new Point3D(), 0.0);
	}

	@Override
	public void paintComponent(Graphics g) {
		drawVoxels(g, (Sector) world, position.copy());
	}

	// draws all voxels in current voxelmap;
	private void drawVoxels(Graphics g, Sector sec, Point3D location) {
		if (sec.homogenous()) {
			drawHomoSector(g, sec, location);

		} else if (sec.particles != null) {

			for (Particle p : sec.particles) {
				drawParticle(g, p, location);
			}
		} else {
			for (Sector subSec : sec.subSectors) {
				if (subSec != null) {
					drawVoxels(g, subSec, location);
				}
			}
		}
	}

	// draws a sector that is full of particles.
	private void drawHomoSector(Graphics g, Sector sec, Point3D location) {
		Point3D center = sec.start;
		center.x += sec.size / 2;
		center.y += sec.size / 2;
		center.z += sec.size / 2;
		Point3D relative = getRelativeCoordinates(center, location);
		Point drawPoint = getDrawingCoordinates(relative, location);

		double distance = location.distance(center);
		int size = (int) Math.round(20 / Sector.MINIMUM_SIZE * sec.size / distance);
		g.setColor(new Color(100, 100, 100));
		g.fillOval(drawPoint.x, drawPoint.y, size, size);
	}

	// draws a given particle, size based on distance.
	private void drawParticle(Graphics g, Particle p, Point3D location) {
		g.setColor(p.getColor());

		Point3D relative = getRelativeCoordinates(p.location, location);
		Point drawPoint = getDrawingCoordinates(relative, location);

		double distance = location.distance(p.location);
		int size = (int) Math.round(20 / distance);

		g.fillOval(drawPoint.x - size / 2, drawPoint.y - size / 2, size, size);
	}
}
