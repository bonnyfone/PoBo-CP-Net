package pobo.gui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import org.apache.commons.collections15.Transformer;

import pobo.common.CPNETAdjList;
import pobo.cpnet.CPNET;
import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedState;

public class PoBoGraphPanel {
	
	/** Private members */
	private CPNET cpnet = null;
	private CPNETAdjList cpnetadj = null;
	private DefaultModalGraphMouse<Integer, Number> graphMouse = new DefaultModalGraphMouse<Integer, Number>();
	private DirectedSparseGraph<String, String> graph = new DirectedSparseGraph<String, String>();
	private VisualizationViewer<String, String> viewer;
	private GraphZoomScrollPane panel;
	private Layout<String, String> layout;
	
	/**
	 * Default Constructor
	 */
	public PoBoGraphPanel() 
	{ 
		initialize(); 
	}
	
	/**
	 * One parameter constructor
	 * @param cpnet
	 */
	public PoBoGraphPanel(CPNET cpnet) {
		initialize();
		setCPNET(cpnet);
	}
	
	/**
	 * Initialize the panel
	 * @return
	 */
	private void initialize() {
		this.graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
		
		layout = new DAGLayout<String, String>(graph);
		viewer = new VisualizationViewer<String, String>(layout);
		viewer.setGraphMouse(graphMouse);
		viewer.setBackground(Color.white);

        Transformer<String, String> stringer = new Transformer<String, String>() {
            public String transform(String e) {
                return "";//graph.getEndpoints(e).toString();
            }
        };
        
        Transformer<String, String> stringerV = new Transformer<String, String>() {
			@Override
			public String transform(String arg0) {
				return arg0;
			}
        };
        
        viewer.getRenderContext().setEdgeLabelTransformer(stringer);
        viewer.getRenderContext().setVertexLabelTransformer(stringerV);
        viewer.getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer<String>(viewer.getPickedEdgeState(), Color.black, Color.cyan));
        viewer.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<String, String>());
        viewer.getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<String>(viewer.getPickedVertexState(), Color.red, Color.yellow));
        viewer.setVertexToolTipTransformer(new ToStringLabeller<String>());
        
        viewer.setGraphMouse(graphMouse);
        viewer.addGraphMouseListener(new GraphMouseListener<String>() {

			@Override
			public void graphClicked(String arg0, MouseEvent arg1) {
			}

			@Override
			public void graphPressed(String arg0, MouseEvent arg1) {
				try {
					PickedState<String> v = viewer.getPickedVertexState();
					System.out.println(v.getPicked().toArray()[0].toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void graphReleased(String arg0, MouseEvent arg1) {}
		});
        
        panel = new GraphZoomScrollPane(viewer);
	}
	
	public void cleanAll(){
		clearGraph();
		layout.reset();
		viewer.revalidate();
		viewer.repaint();
	}

	/**
	 * Set the graph to the given CP-Net
	 * @param cn
	 */
	public void setCPNET(CPNET cn) {
		// Clear previous net
		clearGraph();
		
		this.cpnet = cn;
		
		// Add vertex
		for (int i = 0; i < cpnet.getVertexName().size(); i++)
			graph.addVertex((String)cpnet.getVertexName().get(i));
		
		// Add edges
		for (int i = 0; i < cpnet.getCpnet_data().getMatrix().length; i++)
			for (int j = 0; j < cpnet.getCpnet_data().getMatrix().length; j++)
				if ((Integer)cpnet.getCpnet_data().getMatrix()[i][j] == 1)
					graph.addEdge("edge"+i+j, (String)cpnet.getVertexName().get(i), (String)cpnet.getVertexName().get(j));
		
		// Refresh
		layout.reset();
		viewer.revalidate();
		viewer.repaint();
	}
	
	/**
	 * Set the graph to the given CP-Net Adjacent list
	 * @param cn
	 * @param best
	 */
	public void setCPNETAdjList(CPNETAdjList cn, String best) {
		// Clear previous net
		clearGraph();
		
		this.cpnetadj = cn;
		
		// Add vertex
		Iterator<String> it = cpnetadj.getList().keySet().iterator();
		while(it.hasNext()) {
			graph.addVertex(it.next());
		}
		
		// Add edges
		Iterator<String> it2 = cpnetadj.getList().keySet().iterator();
		
		while(it2.hasNext()) {
			String v = it2.next();
			for (int j = 0; j < cpnetadj.getList().get(v).size(); j++)
				graph.addEdge("edge"+v+j, v, cpnetadj.getList().get(v).get(j));
		}
		
		// Refresh
		layout.reset();
		((DAGLayout<String, String>)layout).setRoot(best);
		viewer.revalidate();
		viewer.repaint();
	}
	
	/**
	 * Remove all vertex and edges from the graph
	 * @return
	 */
	private void clearGraph() {
		while (graph.getVertexCount() > 0)
			graph.removeVertex(graph.getVertices().toArray()[0].toString());
	}

	public DefaultModalGraphMouse<Integer, Number> getGraphMouse() {
		return graphMouse;
	}

	public DirectedSparseGraph<String, String> getGraph() {
		return graph;
	}

	public VisualizationViewer<String, String> getViewer() {
		return viewer;
	}
	
	public GraphZoomScrollPane getPanel() {
		return panel;
	}
}
