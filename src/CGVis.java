import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.Point;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import java.awt.SystemColor;

public class CGVis {

	private static JFrame frame;
	private static MyPanel panel;
	private static File inputfile;
	private static JTextPane txtpnId;
	private static ArrayList<Point> points;
	private static ArrayList<Pair<Point,Point>> arcs;
	private static ArrayList<Pair<Point,Point>> addedArcs;
	
	private static int iter = 0;
	private static int maxIter = 0;
	private static boolean showAdded = true;
	private static String strategy = "";
	private static double t = 0.0;
	private static int satisfied = 0;
	private static int violated = 0;
	private static int added = 0;
	
	
	/**
	 * Create the application.
	 */
	public CGVis() {
		initialize();
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		initLists();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CGVis window = new CGVis();
					CGVis.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		if (args.length > 0) {
            inputfile = new File(args[0]);
		}
	}

	private static void initLists() {
		iter = 0;
		maxIter = 0;
		added = 0;
		showAdded = true;
		satisfied = 0;
		violated = 0;
		points = new ArrayList<Point>();
		arcs = new ArrayList<Pair<Point, Point>>();
		addedArcs = new ArrayList<Pair<Point, Point>>();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(0, 0, 900, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
//		JPanel controlPanel = new JPanel();
//		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
//		controlPanel.setMinimumSize(new Dimension(200, 800));
		
//		frame.getContentPane().add(controlPanel, BorderLayout.EAST);
//		
//		JLabel lblSelectId = new JLabel("Enter ID:");
//		controlPanel.add(lblSelectId);
//		
//		textField = new JTextField();
//		textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, textField.getPreferredSize().height));
//		controlPanel.add(textField);
//		
//		
//		JLabel lblChooseId = new JLabel("Choose ID:");
//		controlPanel.add(lblChooseId);
//		
//		textField_1 = new JTextField();
//		textField_1.setMaximumSize(new Dimension(Integer.MAX_VALUE, textField_1.getPreferredSize().height));
//		controlPanel.add(textField_1);
//		controlPanel.add(Box.createRigidArea(new Dimension(0, 50)));
		final JFileChooser fc = new JFileChooser(new File("."));
		JButton btnGo = new JButton("Load file");
		btnGo.setPreferredSize(new Dimension(100, btnGo.getPreferredSize().height + 40));
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					initLists();
			        int returnVal = fc.showOpenDialog(frame);

			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			            inputfile = fc.getSelectedFile();
			            loadFile(showAdded);
			        } 
			        updateInfo();
			        
			}
		});
		
//		btnGo.setPreferredSize(new Dimension(200, btnGo.getPreferredSize().height + 40));
//		btnGo.setMaximumSize(new Dimension(200, btnGo.getPreferredSize().height + 40));
//		btnGo.setMinimumSize(new Dimension(200, btnGo.getPreferredSize().height + 40));
//		controlPanel.add(btnGo);
//		controlPanel.add(Box.createRigidArea(new Dimension(0, 50)));
		JButton prevButton = new JButton("Prev");
//		prevButton.setMaximumSize(new Dimension(100, 50));
		prevButton.setPreferredSize(new Dimension(100, prevButton.getPreferredSize().height + 40));
		prevButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				prevIter();
			}
		});
		
		JButton nextButton = new JButton("Next");
//		nextButton.setMaximumSize(new Dimension(100, 50));
		nextButton.setPreferredSize(new Dimension(100, nextButton.getPreferredSize().height +40));
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				nextIter();
			}
		});

//		JSplitPane buttonPane = new JSplitPane();
//		buttonPane.setResizeWeight(.5d);
//		buttonPane.setLeftComponent(prevButton);
//		buttonPane.setRightComponent(nextButton);
//		controlPanel.add(buttonPane);
//		controlPanel.add(Box.createRigidArea(new Dimension(0,200)));
//		controlPanel.add(Box.createRigidArea(new Dimension(0, 150)));

//		controlPanel.add(Box.createRigidArea(new Dimension(0, 50)));
//		frame.getContentPane().add(txtpnId, BorderLayout.EAST);
		
		panel = new MyPanel();
		panel.setBackground(SystemColor.window);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
		JPanel toolBar = new JPanel();
		toolBar.setPreferredSize(new Dimension((int)toolBar.getPreferredSize().getWidth(), 100));
		frame.add(toolBar, BorderLayout.SOUTH);
		
		toolBar.add(btnGo);
		JLabel label = new JLabel();
		label.setPreferredSize(new Dimension(100, 50));
		toolBar.add(label);
		toolBar.add(prevButton);

		
		toolBar.add(nextButton);
		JLabel label2 = new JLabel();
		label2.setPreferredSize(new Dimension(50, 50));
		toolBar.add(label2);
		txtpnId = new JTextPane();
		txtpnId.setText("File: " + (inputfile == null ? "No file loaded \n -----------" : inputfile.getName() + "\n-----------"));
		txtpnId.setEditable(false);
		txtpnId.setPreferredSize(new Dimension(400, 90));
		toolBar.add(txtpnId);
        if (inputfile != null ) {
        	loadFile(showAdded);
        }
			}
	
	private static void loadFile(boolean showAdded) {
		frame.remove(panel);
		panel = new MyPanel();
		frame.getContentPane().add(panel);
        readPointsXML();
        readArcsXML();
        panel.setDsts(points);
        panel.setArcs(arcs);
        if (!showAdded) {		
        	addedArcs.clear();
        }
    	panel.setAddedArcs(addedArcs);
        frame.revalidate();
        frame.repaint();
	}
	
	private static void updateInfo() {
        txtpnId.setText("File: " + inputfile.getName() + "\nStrategy = " + strategy + "         T= " + t + " \n Iteration = " + iter + " \n Violated: " + violated + " \t Satisfied: " + satisfied + "\t Added: " + added);
	}
	
	private static void readArcsXML() {
	     try {
	    	  DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	          DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	          Document doc = dBuilder.parse(inputfile);
	          doc.getDocumentElement().normalize();
	         NodeList nList = doc.getElementsByTagName("iteration");
	         maxIter = nList.getLength();
	         arcs = new ArrayList<>();
	         for (int temp = 0; temp < nList.getLength(); temp++) {
	            Node nNode = nList.item(temp);
	            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	            	Element eElement = (Element) nNode;
	            	if (Integer.parseInt(eElement.getAttribute("i")) == iter) { 
	            		added = Integer.parseInt(eElement.getAttribute("ac"));
	            		satisfied = Integer.parseInt(eElement.getAttribute("sc"));
	            		violated = Integer.parseInt(eElement.getAttribute("vc"));
	            		NodeList nListViolated = eElement.getElementsByTagName("violated");
	            		for (int i = 0; i < nListViolated.getLength(); i ++) {
	            			Node node = nListViolated.item(i);
	            			Element el = (Element) node;
	            			Point p1 = points.get(Integer.parseInt(el.getAttribute("s")));
	            			Point p2 = points.get(Integer.parseInt(el.getAttribute("t")));
	            			double val = Double.parseDouble(el.getAttribute("val"));
	            			Pair<Point, Point> pairToAdd = new Pair<Point, Point>(p1, p2, val); 
	            			arcs.add(pairToAdd);
	            			if (Boolean.parseBoolean((el.getAttribute("added")))) {
	            				addedArcs.add(pairToAdd);
	            			}
	            		}
	            	}
	            }
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	}



	// when next button is pressed
	private static void nextIter() {
		if (iter <= maxIter) {
			if (showAdded) {
				iter++;
			}
			showAdded = !showAdded;
			loadFile(showAdded);
		}
        updateInfo();
	}
	
	// when prev button is pressed
	private static void prevIter() {
		if (iter > 0) {
			if (!showAdded) {
				iter--;
			}
			else {
				addedArcs.clear();
			}
				showAdded = !showAdded;
			loadFile(showAdded);
		}
        updateInfo();
	}
	
	private static void readPointsXML() {
	     try {
	    	  DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	          DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	          Document doc = dBuilder.parse(inputfile);
	          doc.getDocumentElement().normalize();
	          NodeList strategyNodeList = doc.getElementsByTagName("run");;
	          Node strategyNode = strategyNodeList.item(0);
	          strategy = ((Element) strategyNode).getAttribute("strategy");
	          t = Double.parseDouble(((Element) doc.getElementsByTagName("run").item(0)).getAttribute("tolerance"));
	          
	         NodeList nList = doc.getElementsByTagName("dest");
	         points = new ArrayList<>();
	         for (int temp = 0; temp < nList.getLength(); temp++) {
	            Node nNode = nList.item(temp);
	            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	            	Element eElement = (Element) nNode;
	            	points.add(new Point(Integer.parseInt(eElement.getAttribute("x")), Integer.parseInt(eElement.getAttribute("y"))));
	            }
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	}

}


