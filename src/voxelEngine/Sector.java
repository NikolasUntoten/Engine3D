package voxelEngine;

import java.util.ArrayList;
import java.util.List;

import baseEngine.Point3D;
import baseEngine.World;

public class Sector implements World {
	
	public static final double MINIMUM_SIZE = .1;
	
	public Sector[] subSectors;
	public List<Particle> particles;
	public double size;
	public Point3D start;
	private boolean homogenous;
	
	public Sector(Point3D start, double size) {
		this.start = start;
		this.size = size;
		homogenous = false;
		
		if (size <= MINIMUM_SIZE) {
			particles = new ArrayList<Particle>();
			
		} else {
			subSectors = new Sector[8];
		}
	}
	
	//adds a particle to this sector. If this sector is not a minimum sized sector, the particle is added to a subsector.
	public void addParticle(Particle p) {
		if (subSectors != null) {
			int i = findSector(p.location);
			if (subSectors[i] == null) {
				makeSector(i, p.location);
			}
			subSectors[i].addParticle(p);
		} else {
			particles.add(p);
		}
	}
	
	//helper method which locates the sector containing the given point
	private int findSector(Point3D contains) {
		int num = 0;
		int xVal = contains.x < 0 ? 1 : 0;
		int yVal = contains.y < 0 ? 2 : 0;
		int zVal = contains.z < 0 ? 4 : 0;
		
		num += xVal;
		num += yVal;
		num += zVal;
		
		return num;
	}
	
	//helper method that creates a sector which contains a given point
	private void makeSector(int index, Point3D contains) {
		int num = findSector(contains);
		
		Point3D p = new Point3D();
		p.x = p.x < 0 ? start.x : size/2;
		p.y = p.y < 0 ? start.y : size/2;
		p.z = p.z < 0 ? start.z : size/2;
		
		subSectors[num] = new Sector(p, size/2);
	}

	
	public boolean homogenous() {
		if (homogenous) return homogenous;
		if (subSectors != null) {
			for (Sector sub : subSectors) {
				if (sub == null) {
					homogenous = false;
					return false;
				}
				if (!sub.homogenous()) {
					homogenous = false;
					return false;
				}
			}
			homogenous = true;
			return true;
			
		} else {
			homogenous = particles.size() > 5;
			return homogenous;
		}
	}
}
