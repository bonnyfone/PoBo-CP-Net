package pobo.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import pobo.common.CPNETAdjList;
import pobo.common.XMLInterface;
import pobo.cpnet.CPNET;
import pobo.cpnet.Solution;
import java.util.Iterator;

/**
 * Main gui-class for PoBo-CPNET
 */
public class PoBoMain extends JFrame implements ActionListener, ClipboardOwner{

	private static final long serialVersionUID = -2311996755670613376L;

	/* Menu entries */

	//CREATE menu
	private String menuNew     = "New CP-NET (acyclic)";
	private String menuSave    = "Save CP-NET...";
	private String menuLoad    = "Load CP-NET...";
	private String menuExit    = "Exit";

	//SOLVE menu
	private String menuOptimal = "Get optimal solution";
	private String menuCompare = "Compare solutions";
	private String menuPartial = "Get partial order of solutions";

	
	/** list of cpnets */
	private static ArrayList<CPNET> myCPNET;

	/** current used cpnet */
	private static CPNET currentCPNET;

	/** log area */
	private static JTextArea log ;
	
	/** static model for cpnets */
	private static DefaultListModel model;
	
	/** cpnet panel */
	private PoBoGraphPanel graphPanel;
	private JPanel netPanel;
	
	//some gui stuffs
	private JButton removeCpnet;
	private JList netList;
	private JTextArea textArea;
	
	/**
	 * Add new cpnet to available net
	 * @param newnet a CPNET
	 */
	public static void addNewCPNET(CPNET newnet){
		myCPNET.add(newnet);
	}

	/**
	 * Return available CPNET
	 * @return
	 */
	public static ArrayList<CPNET> getCPNET(){
		return myCPNET;
	}


	/**
	 * Main Constructor
	 */
	public PoBoMain(){
		super("PoBo CPNET");
		myCPNET = new ArrayList<CPNET>();
		initializeGui();
		createMenu();
		LOG("PoBo-CPNET started.");
	}

	/** show cpnet-choosing box*/
	private boolean chooseCPNET(){
		if(myCPNET.size()==0){
			JOptionPane.showMessageDialog(this,"No CPNET available. You have to create or load a CPNET first....", "No CPNET", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		String[] choose = new String[myCPNET.size()];
		for(int i=0;i<myCPNET.size();i++){
			choose[i] = myCPNET.get(i).getName();
		}

		String choice=(String) JOptionPane.showInputDialog(this, "Choose CPNET to use", "Choose CPNET", JOptionPane.PLAIN_MESSAGE,null, choose, choose[0]);

		if(choice==null)return false;
		if(choice.equals(""))return false;

		for(int i=0;i<myCPNET.size();i++){
			if(choice.equals(myCPNET.get(i).getName()))
				currentCPNET = myCPNET.get(i);
		}

		return true;
	}

	/** update list display*/
	public static void updateList(){
		model.clear();
		for(int i=0;i<myCPNET.size();i++){
			model.addElement(myCPNET.get(i).getName());
		}
	}

	/**
	 * Place a String on the clipboard, and make this class the
	 * owner of the Clipboard's contents.
	 */
	public void setClipboardContents( String aString ){
		StringSelection stringSelection = new StringSelection( aString );
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents( stringSelection, this );
	}


	/** Basic gui initialization */
	private void initializeGui(){
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		graphPanel = new PoBoGraphPanel();
		
		setSize(700, 500);
		setLocationRelativeTo(null); //align in center
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		removeCpnet = new JButton("Remove CPNET");
		JPanel pListNet = new JPanel();
		netPanel = graphPanel.getPanel();
		netPanel.setBorder(BorderFactory.createTitledBorder("CPNET preview"));
		pListNet.setBorder(BorderFactory.createTitledBorder("CPNET list"));
		log = new JTextArea();
		
		textArea = new JTextArea(5,20);
		//textArea.setPreferredSize(new Dimension(200, textArea.getHeight()));
		textArea.setBorder(BorderFactory.createTitledBorder("Info"));
		textArea.setBackground(Color.decode("#EEEEEE"));
		textArea.setEditable(false);
		
		JScrollPane scrollTxt = new JScrollPane(textArea);
		scrollTxt.setVerticalScrollBar(new JScrollBar());
		scrollTxt.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		netList = new JList();
		netList.setPreferredSize(new Dimension(200,netList.getHeight()));
		netList.setBackground(Color.decode("#EEEEEE"));
		netList.setForeground(Color.blue);
		log.setPreferredSize(new Dimension(log.getWidth(), 100));
		log.setBorder(BorderFactory.createTitledBorder(null, "Logs",TitledBorder.LEFT, TitledBorder.TOP, null, Color.white));
		//log.setBorder(BorderFactory.createTitledBorder("Logs"));
		log.setBackground(Color.DARK_GRAY);
		log.setForeground(Color.white);

		model = new DefaultListModel();
		netList.setModel(model);
		pListNet.setLayout(new BorderLayout());
		pListNet.add(netList,BorderLayout.CENTER);
		pListNet.add(removeCpnet,BorderLayout.SOUTH);
		
		JScrollPane scroll = new JScrollPane(log);
		scroll.setVerticalScrollBar(new JScrollBar());
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		add(pListNet, BorderLayout.WEST);
		add(netPanel, BorderLayout.CENTER);
		add(scrollTxt, BorderLayout.EAST);
		add(scroll, BorderLayout.SOUTH);
		
		createListeners();
		//ContentPanel c = new ContentPanel();
		//add(c);
	} 

	/** create command listners */
	private void createListeners() {
		removeCpnet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int sel = netList.getSelectedIndex();
				if(sel==-1)return;
				LOG("Removed CPNET '"+myCPNET.get(sel).getName()+"'");
				myCPNET.remove(sel);
				updateList();
				graphPanel.cleanAll();
				textArea.setText("");
			}
		});
		
		netList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int sel = netList.getSelectedIndex();
				if (sel == -1) return;
				graphPanel.setCPNET(myCPNET.get(sel));
				updateTextArea(myCPNET.get(sel));
				
			}
			
		});
		
		netList.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				int sel = netList.getSelectedIndex();
				if (sel == -1) return;
				graphPanel.setCPNET(myCPNET.get(sel));
				updateTextArea(myCPNET.get(sel));
			}
		});
		
	}
	
	/** update the textArea content */
	private void updateTextArea(CPNET cn) {
		textArea.removeAll();
		if (cn == null) return;
		textArea.setText("LEGEND:\n");
		for (int i = 0; i < cn.getVertexName().size(); i++) {
			textArea.append(cn.getVertexName().get(i) + " = " + ((char)(65 + i)) + "\n");
		}
		textArea.append("\n");
		for (int i = 0; i < cn.getVertex().length; i++) {
			textArea.append(cn.getVertexName().get(i) + " preferences:\n");
			for (int j = 0; j < cn.getVertex()[i].getNumParents(); j++) {
				textArea.append("" + (char)(cn.getVertexName().indexOf(cn.getVertex()[i].getParents().get(j)) + 65));	
			}
			textArea.append(" : " + (char)(i + 65) + "\n");
			Iterator<String> it = cn.getVertex()[i].getTable().keySet().iterator();
			while (it.hasNext()) {
				String s = it.next();
				int p = cn.getVertex()[i].getTable().get(s);
				textArea.append(s + " : " + p + " > " + (1-p) + "\n");				
			}
			textArea.append("\n");
			
		}
		textArea.setCaretPosition(0);
		
	}

	/** Creates menu */
	private void createMenu(){
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;

		//Create the menu bar.
		menuBar = new JMenuBar();

		//Build the first menu.
		menu = new JMenu(" CREATE ");
		menuBar.add(menu);
		//a group of JMenuItems
		menuItem = new JMenuItem(menuNew);
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuItem = new JMenuItem(menuSave);
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuItem = new JMenuItem(menuLoad);
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menu.addSeparator();
		menuItem = new JMenuItem(menuExit);
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menu = new JMenu(" SOLVE ");
		menuBar.add(menu);
		menuItem = new JMenuItem(menuOptimal);
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuItem = new JMenuItem(menuCompare);
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuItem = new JMenuItem(menuPartial);
		menuItem.addActionListener(this);
		menu.add(menuItem);


		menu = new JMenu(" ? ");
		menuBar.add(menu);
		menuItem = new JMenuItem("Info");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuItem = new JMenuItem("About");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		setJMenuBar(menuBar);

	}

	/** Menu listener*/
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		String c = e.getActionCommand().toString();

		if(c.equals(menuExit)){
			dispose();
		}
		else if(c.equals(menuNew)){
			CPNETWizard newNet = new CPNETWizard();
			newNet.setVisible(true);

		}
		else if(c.equals(menuSave)){
			saveCPNET();

		}
		else if(c.equals(menuLoad)){
			loadCPNET();
		}
		else if(c.equals(menuOptimal)){
			if(!chooseCPNET())return;
			String ris=Solution.buildComprensiveSolution(currentCPNET.getVertexName(),currentCPNET.getOptimalSolution().getSolutionAsChars());
			setClipboardContents(ris);
			JOptionPane.showMessageDialog(this, "Best solution for the CPNET '"+currentCPNET.getName()+"' is:\n\n"+ris+"\n\n(result copied to clipboard)", "Optimal Solution", JOptionPane.INFORMATION_MESSAGE);
			LOG("Best solution found for the CPNET '"+currentCPNET.getName()+"': "+ris);
		}
		else if(c.equals(menuCompare)){
			if(!chooseCPNET())return;
			new CPNETCompare(currentCPNET).setVisible(true);

		}
		else if(c.equals(menuPartial)){
			long start = System.currentTimeMillis();
			currentCPNET = myCPNET.get(netList.getSelectedIndex());
			CPNETAdjList partialOrder = currentCPNET.getPartialOrder();
			System.out.println("Finish of getPartialOrder");
			partialOrder.graphPruning();
			System.out.println("Finish of graphPruning");
			long end = System.currentTimeMillis();
			graphPanel.setCPNETAdjList(partialOrder, currentCPNET.getOptimalSolution().getSolutionAsString());
			System.out.println("Finish of setAdjList");
			System.out.println("Total comp time: "+(end-start));
			LOG("Calculated and printed Partial Order Graph");
		}
	}

	/** internal method for loading cpnet */
	private void loadCPNET() {
		final JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);
		fc.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return "CPNET XML files";
			}
			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}
				
				String name = f.getName();
				if(name.endsWith(".XML") || name.endsWith(".xml"))return true;
				else return false;
				
			}
		});
		int returnVal = fc.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION ){
			File[] file = fc.getSelectedFiles();
			
			for(File f : file){
				CPNET newNet =XMLInterface.readCPNET(f.getPath()); 
				myCPNET.add(newNet);
				LOG("Loaded CPNET '"+newNet.getName()+"'");
				updateList();
			}
			
		}
	}

	/** internal method for saving cpnet*/
	private void saveCPNET(){
		if(!chooseCPNET())return;
		final JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return "CPNET XML files";
			}
			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}
				
				String name = f.getName();
				if(name.endsWith(".XML") || name.endsWith(".xml"))return true;
				else return false;
				
			}
		});
		File f = new File(currentCPNET.getName()+".xml");
		fc.setSelectedFile(f);
		
		int returnVal = fc.showSaveDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION ){
			File file = fc.getSelectedFile();
			XMLInterface.writeCPNET(currentCPNET,file.getPath());
			LOG("'"+currentCPNET.getName()+"' CPNET saved in "+ file.getPath());
		}
	}


	/**
	 * Insert a new Log
	 * @param message the message to display
	 */
	public static void LOG(String message){
		if(log==null)return;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss");
		String time = sdf.format(cal.getTime());

		log.setText("["+time+"] "+message+"\n"+log.getText());
	}

	//Launch gui
	public static void main(String args[]){
		PoBoMain gui = new PoBoMain();
		gui.setVisible(true);
	}

	/*----------------------------------------------------------------------------------------------------*/

	/** Useless class for gui stuff */
	class ContentPanel extends JPanel {
		private static final long serialVersionUID = 739084894283561290L;
		Image bgimage = null;

		ContentPanel() {
			MediaTracker mt = new MediaTracker(this);
			bgimage = Toolkit.getDefaultToolkit().getImage("lol.png");
			mt.addImage(bgimage, 0);
			try {
				mt.waitForAll();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
//			int imwidth = bgimage.getWidth(null);
//			int imheight = bgimage.getHeight(null);
			g.drawImage(bgimage, 1, 1, null);
		}
	}


	@Override
	public void lostOwnership(Clipboard arg0, Transferable arg1) {
		// TODO Auto-generated method stub
	}
}
