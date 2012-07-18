package pobo.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pobo.common.ConjuctionTable;
import pobo.cpnet.CPNET;

public class CPNETWizard extends JFrame {

	private static final long serialVersionUID = 868861275513408244L;

	private JList vertex;
	private JList vertex2;
	private JList edges;
	private ArrayList<DefaultListModel> modelEdges;
	private DefaultListModel modelVertex;
 
	private JButton finish;
	private JButton proceed;
	private JButton addVertex;
	private JButton addEdge;
	private JButton removeSelectedVertex;
	private JButton removeSelectedEdge;
	private JPanel p1;
	private HashMap<String, JComboBox >references[] ;
	
	private ActionListener update;
	private Color colorNO = Color.getHSBColor(0,62,99);
	private Color colorOK = Color.GREEN;//Color.getHSBColor(120,49,99);
	

	JPanel panelPref;
	private CPNET net;

	public CPNETWizard(){
		super("Create acyclic CP-NET");

		//build gui
		initialize();

		//create gui listeners 
		createInitialListeners();
		createSecondaryListeners();
	}


	/**
	 * This method creates the CPNET from gui data
	 */
	@SuppressWarnings("unchecked")
	private void elaborateCPNET(){
		net = new CPNET(modelVertex.size());

		//create vertex first...
		for(int i=0;i<modelVertex.size();i++){
			net.addVertex((String) modelVertex.get(i));
		}

		//then fill edges..
		for(int i=0;i<modelVertex.size();i++){
			String from = ((String) modelVertex.get(i));
			for(int j=0;j<modelEdges.get(i).size();j++){
				net.addEdge(from, (String)modelEdges.get(i).get(j));
			}
		}		

		net.generate();

		references = new HashMap[modelVertex.size()]; 
		
		ConjuctionTable[] table = net.getVertex();
		for(int i=0; i<net.getVertexName().size();i++){

			System.out.println("Conjunction table for "+net.getVertexName().get(i)+": ");
			System.out.println(table[i]);
		}



	}

	private void createInitialListeners() {

		//Proceed button
		proceed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(modelVertex.getSize()==0)return;
				//disable precedent gui
				vertex.clearSelection();
				edges.clearSelection();
				addVertex.setEnabled(false);
				addEdge.setEnabled(false);
				removeSelectedEdge.setEnabled(false);
				removeSelectedVertex.setEnabled(false);
				proceed.setEnabled(false);
				vertex.setEnabled(false);
				edges.setEnabled(false);

				//elaborate CPNET structure
				elaborateCPNET();
				
				createUpdateActionListener();

				//activate second part of the gui
				vertex2.setEnabled(true);
				vertex2.setModel(modelVertex);
			}
		});

		//removeEdge listener
		removeSelectedEdge.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int sel = edges.getSelectedIndex();
				if(sel==-1)return;
				modelEdges.get(vertex.getSelectedIndex()).remove(sel);
			}
		});


		//removeVertex listener
		removeSelectedVertex.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				int sel =vertex.getSelectedIndex();
				if(sel==-1)return;
				//System.out.println("sel: "+sel);
				modelVertex.remove(sel);
				modelEdges.remove(sel);

				sel=sel-1;
				if(sel<0)sel=0;
				vertex.setSelectedIndex(sel);

			}
		});

		//Vertex listener
		vertex.addListSelectionListener(new  ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if( e.getSource() == vertex	&& !e.getValueIsAdjusting() ){
					//System.out.println(vertex.getSelectedIndex());
					if(vertex.getSelectedIndex()==-1)return;
					edges.setModel(modelEdges.get(vertex.getSelectedIndex()));
					edges.setBorder(BorderFactory.createTitledBorder("Edges from "+modelVertex.get(vertex.getSelectedIndex())));
				}
			}
		});


		//button addEdge
		addEdge.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(vertex.getSelectedIndex()==-1)return;
				String currentVertex = (String) modelVertex.get(vertex.getSelectedIndex());

				Object[] possibilities = new Object[modelVertex.size()-1];
				int j=0;
				for(int i=0;i<modelVertex.getSize();i++){
					if(!modelVertex.get(i).equals(currentVertex)){
						possibilities[j]=modelVertex.get(i);
						j++;
					}
				}

				String str = (String)JOptionPane.showInputDialog( 
						null, 
						"Create an edge with..", 
						"Create edge", 
						JOptionPane.PLAIN_MESSAGE, 
						null, 
						possibilities, 
				"");  

				//String str = JOptionPane.showInputDialog(null, "Type the vertex name to make an edge", "vertex_name", JOptionPane.PLAIN_MESSAGE);
				if(str==null)return;
				if(str.equals(""))return;

				DefaultListModel current = (DefaultListModel) edges.getModel();

				if(!current.contains(str)){
					//Make CYCLE DETECTION? actually only direct check
					if(modelEdges.get(modelVertex.indexOf(str)).contains(currentVertex)){
						JOptionPane.showMessageDialog(null, "This would create a cycle!",
								"Cycle detected",JOptionPane.ERROR_MESSAGE);
					}else 	 current.addElement(str);


					//modelEdges.get(modelVertex.indexOf(str)).addElement(currentVertex);
					//TODO replicare in maniera duale?? solo nelle cicliche 
				}
				else JOptionPane.showMessageDialog(null, "You have already entered an edge for this vertex.",
						"Edge already exist",JOptionPane.ERROR_MESSAGE);

			}
		});


		//button addVertex
		addVertex.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str = JOptionPane.showInputDialog(null, "Type new vertex name", "vertex", JOptionPane.PLAIN_MESSAGE);
				if(str==null)return;
				str=str.trim();
				if(str.equals(""))return;

				if(!modelVertex.contains(str)){
					modelVertex.addElement(str);
					modelEdges.add(new DefaultListModel());
				}
				else JOptionPane.showMessageDialog(null, "You have already entered a vertex named '"+str+"', please choose another name.",
						"Vertex already exist",JOptionPane.ERROR_MESSAGE);

			}
		});

	}
	
	/** Listener for every comboBox used in preferences setting*/
	private void createUpdateActionListener() {
		update = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int sel = vertex2.getSelectedIndex();
				if(sel==-1)return;
				
				for(Entry<String,JComboBox> entry : references[sel].entrySet()){
					String parents = entry.getKey();
					Integer bestval = reInterpretate((String) ((JComboBox)(entry.getValue())).getSelectedItem());
					net.setPreference(modelVertex.get(sel)+"", parents, bestval);
					if(entry.getValue().getSelectedItem().equals(""))entry.getValue().setBackground(colorNO);
					else entry.getValue().setBackground(colorOK);
				}
				
				System.out.println(net.getVertex()[sel]);
				
				if(preferencesAreSet())finish.setEnabled(true);
				else finish.setEnabled(false);
				
			}
		};
		
	}


	private boolean preferencesAreSet() {
		ConjuctionTable[] table = net.getVertex();
		
		for(int i=0;i<table.length;i++){
			for(Entry<String,Integer> e : table[i].getTable().entrySet()){
				if(e.getValue()==-1)return false;
			}
		}

		return true;
	}


	/** Listeners for the second part of the gui */ 
	private void createSecondaryListeners(){
		
		//finish listener
		finish.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				String savename = JOptionPane.showInputDialog("Specify a name for the CPNET", "net_name");
				if(savename==null)return;
				if(savename.equals(""))return;
				
				net.setName(savename);
				
				PoBoMain.addNewCPNET(net);
				PoBoMain.LOG("Created new CPNET '"+savename+"'");
				PoBoMain.updateList();
				
				dispose();
				try {
					finalize();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		//Vertex listener
		vertex2.addListSelectionListener(new  ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if( e.getSource() == vertex2	&& !e.getValueIsAdjusting() ){
					if(vertex2.getSelectedIndex()==-1)return;
					
					showPreferences(vertex2.getSelectedIndex());
				}
			}
		});
	}

	/** Create layout for edit preferences for selected vertex */
	private void showPreferences(int selectedIndex) {
		//Clean panel
		String current = (String) modelVertex.get(selectedIndex);
		panelPref.removeAll();
		addPrefHeader();
		
		ConjuctionTable entry = net.getVertex()[selectedIndex];
		HashMap<String, Integer> map = entry.getTable();
		System.out.println(map);
		
		references[selectedIndex] = new HashMap<String, JComboBox>();
		
		for(Entry<String, Integer> row : map.entrySet()){
			String conf = entry.getTextualConfiguration(row.getKey());
			
			addPrefRow(selectedIndex, row.getKey(),conf, current, interpretate(current, row.getValue()));
			
		}
		
		//redraw all
		panelPref.revalidate();
		panelPref.repaint();
	}
	
	/** method used to handle dynamic gui */
	private String interpretate(String var, Integer value){
		if(value==1)return var;
		else if(value==0)return "!"+var;
		else return "";
	}
	
	/** method used to handle dynamic gui */
	private Integer reInterpretate(String val){
		if(val.startsWith("!"))return 0;
		else if(val.equals(""))return -1;
		else return 1;
	}
	
	/** method used to handle dynamic gui */
	private void addPrefHeader(){
		JPanel newRow = new JPanel();
		GridLayout g = new GridLayout(1,2);
		newRow.setLayout(g);
		JLabel label = new JLabel("Parents conf.");
		JLabel label2 = new JLabel("Best value");
		newRow.add(label);
		newRow.add(label2);
		
		//newRow.setPreferredSize(new Dimension(0, 200));
		panelPref.add(newRow);
	}

	/** method used to handle dynamic gui */
	private void addPrefRow(int selIndex,String key,String config, String value, String current){
		
		JPanel newRow = new JPanel();
		GridLayout g = new GridLayout(1,2);
		newRow.setLayout(g);
		JLabel label = new JLabel(config);
		label.setFont(new Font("Serif", Font.ITALIC,15));

		String[] objs = {"",value,"!"+value};
		JComboBox combo = new JComboBox(objs);
		combo.setSelectedItem(current);
		
		newRow.add(label);
		newRow.add(combo);
		combo.addActionListener(update);
		
		if(combo.getSelectedItem().equals(""))combo.setBackground(colorNO);
		else combo.setBackground(colorOK);
		
		references[selIndex].put(key,combo);
		
		//newRow.setPreferredSize(new Dimension(0, 200));
		panelPref.add(newRow);
	}

	/**
	 * Initialize gui
	 */
	private void initialize() {
		setSize(500,600);
		setLocationRelativeTo(null); //align in center
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);

		proceed = new JButton("Proceed with preferences...");
		addVertex = new JButton("Add vertex");
		addEdge = new JButton("Add edge");
		removeSelectedVertex = new JButton("Remove vertex");
		removeSelectedEdge = new JButton("Remove edge");
		finish = new JButton("FINISH");
		finish.setEnabled(false);
		JPanel pGlobal = new JPanel();
		GridLayout gridGlobal = new GridLayout(2,1);
		pGlobal.setLayout(gridGlobal);

		p1 = new JPanel();


		modelVertex = new DefaultListModel();
		vertex = new JList(modelVertex);
		vertex.setBorder(BorderFactory.createTitledBorder("Vertex"));
		modelEdges = new ArrayList<DefaultListModel>();
		edges = new JList();
		edges.setBorder(BorderFactory.createTitledBorder("Edges"));

		p1.setBorder(BorderFactory.createTitledBorder("Populate CP-NET"));

		JPanel pList = new JPanel();
		GridLayout g1 = new GridLayout(1,2);
		pList.setLayout(g1);
		pList.add(vertex);
		pList.add(edges);

		JPanel pMiddle = new JPanel();
		GridLayout g3 = new GridLayout(2,1);
		pMiddle.setLayout(g3);
		JPanel pButton = new JPanel();
		GridLayout g2 = new GridLayout(2,2);
		pButton.setLayout(g2);
		pButton.add(addVertex);
		pButton.add(addEdge);
		pButton.add(removeSelectedVertex);
		pButton.add(removeSelectedEdge);
		pMiddle.add(pButton);
		pMiddle.add(proceed);

		p1.setLayout(new BorderLayout());
		p1.add(pList,BorderLayout.CENTER);
		p1.add(pMiddle,BorderLayout.SOUTH);


		//Second gui block
		JPanel p2 = new JPanel();
		panelPref = new JPanel();
		panelPref.setBorder(BorderFactory.createTitledBorder("Preferences"));
		//panelPref.setLayout(new BoxLayout(panelPref, BoxLayout.Y_AXIS));
		//panelPref.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
		GridLayout l = new GridLayout(50, 1);
		
		panelPref.setLayout(l);
		
		JScrollPane scrollPane = new JScrollPane( panelPref );
		scrollPane.setVerticalScrollBar( new JScrollBar());
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
//		addPrefRow("asasd", "C");
//		addPrefRow("asahdfsd", "D");
//		addPrefRow("asasddd", "E");

		vertex2 = new JList();
		vertex2.setEnabled(false);
		vertex2.setBorder(BorderFactory.createTitledBorder("Created vertex"));
		GridLayout g4 = new GridLayout(1,2);
		JPanel p3 = new JPanel();
		p3.setLayout(g4);

		p3.add(vertex2);
		p3.add(scrollPane);
		
		p2.setLayout(new BorderLayout());
		p2.add(p3, BorderLayout.CENTER);
		p2.add(finish,BorderLayout.SOUTH);

		//pack all
		pGlobal.add(p1);
		pGlobal.add(p2);

		add(pGlobal);
		//pack();
	}
}
