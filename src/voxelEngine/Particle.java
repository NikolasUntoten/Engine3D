package voxelEngine;

import java.awt.Color;

import baseEngine.Point3D;

public class Particle {
	
	public static final int BASIC_PARTICLE = 1384;
	
	
	public Point3D location;
	private int pNum;
	
	public Particle(Point3D initLoc, int materialNumber) {
		location = initLoc;
		pNum = materialNumber;
	}
	
	public Color getColor() {
		switch (pNum) {
		
		case BASIC_PARTICLE: return Color.GRAY;
		
		default: return Color.BLACK;
		}
	}
}
