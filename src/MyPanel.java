import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;

public class MyPanel extends JPanel {

	private final int dstSize = 10;
	ArrayList<Point> dstsCoords;
	ArrayList<Pair<Point,Point>> arcs;
	ArrayList<Pair<Point,Point>> addedArcs;
	
	public void setDsts(ArrayList<Point> dstsCoords) {
		this.dstsCoords = dstsCoords;
	}

	public void setArcs(ArrayList<Pair<Point, Point>> arcs) {
		this.arcs = arcs;
	}

	public void setAddedArcs(ArrayList<Pair<Point, Point>> arcs) {
		this.addedArcs = arcs;
	}
	
	public void paint(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
	    g.setColor(Color.WHITE);
		g.setStroke(new BasicStroke(3));
	    g.fillRect(0, 0, 900, 900);
	    g.setColor(Color.BLACK);
	    if (dstsCoords != null) {
			for (Point p: dstsCoords) {
				int px = (int) p.getX() * 9 - 5;
				int py = (int) p.getY() * 9 - 5;
//				g.fillRect(px, py, dstSize, dstSize);
				g.fillOval(px, py, dstSize, dstSize);
			      g.drawString(Integer.toString(dstsCoords.indexOf(p)), px + 8, py);
			}
	    }
	    if (arcs != null) {
	    	g.setColor(Color.BLACK);
			for (Pair<Point, Point> p: arcs) {
				int sx = (int) ((Point) p.getFirst()).getX() * 9;
				int sy = (int) ((Point) p.getFirst()).getY() * 9;
				int tx = (int) ((Point) p.getSecond()).getX() * 9;
				int ty = (int) ((Point) p.getSecond()).getY() * 9;
				g.drawLine(sx, sy, tx, ty);
			}
	    }
	    if (addedArcs != null) {
	    	g.setColor(Color.RED);
			for (Pair<Point, Point> p: addedArcs) {
				int sx = (int) ((Point) p.getFirst()).getX() * 9;
				int sy = (int) ((Point) p.getFirst()).getY() * 9;
				int tx = (int) ((Point) p.getSecond()).getX() * 9;
				int ty = (int) ((Point) p.getSecond()).getY() * 9;
				g.drawLine(sx, sy, tx, ty);
			}
	    }
	  }


}