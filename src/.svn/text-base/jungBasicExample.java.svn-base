
import java.awt.Color;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.renderers.BasicRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;


 
public class jungBasicExample extends JPanel {
 
	private Graph m_graph;
	private Layout mVisualizer;
	private Renderer mRenderer;
	private VisualizationViewer mVizViewer;
	private	DefaultModalGraphMouse m_graphmouse;
	 
	public jungBasicExample(){
	
		m_graph = new DirectedSparseMultigraph();// new SparseGraph();//PluggableRendererDemo().getGraph();
		
		
		//for fun try ....  new CircleLayout(m_graph);  
		mVisualizer = new FRLayout(m_graph);
		
		mRenderer = new BasicRenderer();
				
		mVizViewer = new VisualizationViewer(mVisualizer); //dimensione??
		mVizViewer.setBackground(Color.WHITE);
		
		m_graphmouse = new DefaultModalGraphMouse();
	    mVizViewer.setGraphMouse(m_graphmouse);
		
		add(mVizViewer);
		
			somefunction();
	}
	
	public void somefunction(){
		for(int i=0;i<20;i++){
			m_graph.addVertex(new Integer(i));
		
		}
		for(int i=0;i<10;i++){
			m_graph.addEdge(i+5, i+6, EdgeType.DIRECTED);
		}
		
		
		//as an example...
		//lets loop through all verticies....
		Iterator walker = m_graph.getVertices().iterator();
		for(;walker.hasNext();){
			Object j = walker.next();
			System.out.println(j.getClass()+" "+j.toString());
		}
		//need to recalcualte where everyone is drawn...
		mVisualizer.reset();
		
		//show it off
		mVizViewer.revalidate();
		mVizViewer.repaint();
	}
	
	
	
	
	public static void main(String[] args) {
	//create a window to display...	
	JFrame jf = new JFrame("basic graph");
	//this is something we are adding from this class
	jf.getContentPane().add(new jungBasicExample());
	
	//set some size
	jf.setSize(700, 500);
	
	//do something when click on x
	jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//make sure everything fits...
	jf.pack();
	//make it show up...
	jf.setVisible(true);

	}
	
}
