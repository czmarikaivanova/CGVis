import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import javax.swing.JPanel;

public class MyPanel extends JPanel {

	private final int dstSize = 10;
	private final int SCALE = 9;
	ArrayList<Point> dstsCoords;
	ArrayList<Pair<Point,Point>> arcs;
	ArrayList<Pair<Point,Point>> addedArcs;
	ArrayList<Pair<Point,Point>> newlySatArcs;
	
	public void setDsts(ArrayList<Point> dstsCoords) {
		this.dstsCoords = dstsCoords;
	}

	public void setArcs(ArrayList<Pair<Point, Point>> arcs) {
		this.arcs = arcs;
	}

	public void setAddedArcs(ArrayList<Pair<Point, Point>> arcs) {
		this.addedArcs = arcs;
	}
	
	public void setNewlySatArcs(ArrayList<Pair<Point, Point>> arcs) {
		this.newlySatArcs = arcs;
	}
	
	public void paint(Graphics graphics) {

		Graphics2D g = (Graphics2D) graphics;

		g.setColor(Color.WHITE);
		g.setStroke(new BasicStroke(3));
	    g.fillRect(0, 0, 100*SCALE, 100*SCALE);

	    if (newlySatArcs != null) {
	    	for (Pair<Point, Point> p: newlySatArcs) {
	    		if (!addedArcs.contains(p)) {
				g.setColor(Color.YELLOW);
				 g.setStroke(new BasicStroke(5));
				drawArc(g, p);
	    		}
			}
	    }
	    if (arcs != null) {
			for (Pair<Point, Point> p: arcs) {
				g.setColor(Color.GRAY);
				g.setStroke(new BasicStroke(2));
				drawArc(g, p);
			}
	    }
	    if (addedArcs != null) {
			for (Pair<Point, Point> p: addedArcs) {
				g.setColor(Color.RED);
				g.setStroke(new BasicStroke(2));
				drawArc(g, p);
			}
	    }
	    g.setColor(Color.BLACK);
	    if (dstsCoords != null) {
			for (Point p: dstsCoords) {
				int px = (int) p.getX() * SCALE - 5;
				int py = (int) p.getY() * SCALE - 5;
//				g.fillRect(px, py, dstSize, dstSize);
				g.fillOval(px, py, dstSize, dstSize);
				g.setFont(new Font("TimesRoman", Font.BOLD, 15)); 
			      g.drawString(Integer.toString(dstsCoords.indexOf(p)), px + 8, py);
			}
	    }
	  }
	
	private void drawArc(Graphics2D g, Pair<Point, Point> p) {
		int sx = (int) ((Point) p.getFirst()).getX() * SCALE;
		int sy = (int) ((Point) p.getFirst()).getY() * SCALE;
		int tx = (int) ((Point) p.getSecond()).getX() * SCALE;
		int ty = (int) ((Point) p.getSecond()).getY() * SCALE;
		g.drawLine(sx, sy, tx, ty);
		g.setColor(Color.blue);
		g.setFont(new Font("TimesRoman", Font.BOLD, 12)); 
        g.drawString(Double.toString(round(p.getVal(), 2)), (sx + tx)/2-3, (sy + ty)/2-3);
	}
	
	private double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}	

}