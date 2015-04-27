package Geometry;

import java.awt.Color;

import Other.ScreenPointFinder;
import main.Main;

public class Polygon3D {
	
	Point3D[] myPoints;
	Color myColor;
	public double distanceFromCamera;
	
	public Polygon3D(Point3D[] toAdd, Color toColor) {
		myColor = toColor;
		myPoints = toAdd;
		updateDistance();
	}
	
	public void updateDistance() {
		int maxID = 0;
		Line[] current = getLinesToPoint(Main.cameraLocation);
		for(int i = 1; i < current.length; i++) {
			if(current[i].length < current[maxID].length) {
				maxID = i;
			}
		}
		distanceFromCamera = current[maxID].length;
	}
	
	public Line[] getLinesToPoint(Point3D toPathTo) {
		Line[] lines = new Line[myPoints.length];
		for(int i = 0; i < myPoints.length; i++) {
			lines[i] = new Line(toPathTo.xPos, toPathTo.yPos, toPathTo.zPos, myPoints[i].xPos, myPoints[i].yPos, myPoints[i].zPos);
		}
		return lines;
	}
	
	public ColoredPolygon rasterizeToScreen() {
		int[] xs = new int[myPoints.length];
		int[] ys = new int[myPoints.length];
		Line[] lines = getLinesToPoint(Main.cameraLocation);
		for(int i = 0; i < lines.length; i++) {
			xs[i] = Main.find.getWidthPixel(lines[i].getHorAngle());
			ys[i] = Main.find.getHeightPixel(lines[i].getVertAngle());
		}
		return new ColoredPolygon(myColor, xs, ys);
	}
	
}