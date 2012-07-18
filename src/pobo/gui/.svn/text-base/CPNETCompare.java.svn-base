package pobo.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import pobo.common.Util.Relation;
import pobo.cpnet.CPNET;

public class CPNETCompare extends JFrame {

	private static final long serialVersionUID = 8705503463923716892L;
	
	private JButton compare;
	private JEditorPane result;
	private CPNET currentNet;
	private ArrayList<String> vertex;
	
	private JComboBox combo1[];
	private JComboBox combo2[];
	
	private JPanel s1;
	private JPanel s2;
	
	
	
	/** constructor */
	public CPNETCompare(CPNET current){
		currentNet = current;
		initializeGUI();
		createSolutionsFields();
	}

	private void compare(){
		if(!currentNet.checkPartialOrderReady())currentNet.getPartialOrder();
		//build solutions
		String s1="";
		String s2="";
		
		String ss1="";
		String ss2="";
		for(int i=0;i<combo1.length;i++){
			if(((String)combo1[i].getSelectedItem()).startsWith("!"))s1+="0";
			else s1+="1";
			ss1+=(String)combo1[i].getSelectedItem()+" ";
			
			if(((String)combo2[i].getSelectedItem()).startsWith("!"))s2+="0";
			else s2+="1";
			ss2+=(String)combo2[i].getSelectedItem()+" ";
		}
		String s ="<center>Solution 1: "+ss1+"<br>"+"Solution 2: "+ss2+"<br><b>";
		Relation ris = currentNet.compareSolutions(s1, s2);
		if(ris == Relation.EQUAL)
			s+="Solution 1 is <font color='blue'>"+currentNet.compareSolutions(s1, s2)+"</font> to Solution 2</center>";
		else if(ris == Relation.INCOMPARABLE)
			s+="Solution 1 is <font color='red'>"+currentNet.compareSolutions(s1, s2)+"</font> with Solution 2</center>";
		else if(ris == Relation.BETTER)
			s+="Solution 1 is <font color='green'>"+currentNet.compareSolutions(s1, s2)+"</font> then Solution 2</b></center>";
		else
			s+="Solution 1 is <font color='red'>"+currentNet.compareSolutions(s1, s2)+"</font> then Solution 2</b></center>";
		
		result.setText(s);
		
	}
	
	/**create gui stuff for solutions setup*/
	private void createSolutionsFields() {
		vertex=currentNet.getVertexName();
		combo1 = new JComboBox[vertex.size()];
		combo2 = new JComboBox[vertex.size()];
		
		for(int i=0;i<vertex.size();i++){
			String[] vals = { vertex.get(i),"!"+vertex.get(i)};
			
			combo1[i] = new JComboBox(vals);
			combo2[i] = new JComboBox(vals);
			
			s1.add(combo1[i]);
			s2.add(combo2[i]);
		}
	}


	private void initializeGUI() {
		setTitle("Compare two solutions");
		setSize(400, 400);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null); //align in center
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		
		JPanel p1 = new JPanel();
		GridLayout g = new GridLayout(1,2);
		p1.setLayout(g);
		
		s1 = new JPanel();
		GridLayout gs1 = new GridLayout(50,1);
		s1.setLayout(gs1);
		s2 = new JPanel();
		GridLayout gs2 = new GridLayout(50,1);
		s2.setLayout(gs2);
		
		JScrollPane scr1 = new JScrollPane(s1);
		JScrollPane scr2 = new JScrollPane(s2);
		
		scr1.setBorder(BorderFactory.createTitledBorder("Solution 1"));
		scr2.setBorder(BorderFactory.createTitledBorder("Solution 2"));
		scr1.setVerticalScrollBar( new JScrollBar());
		scr1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scr2.setVerticalScrollBar( new JScrollBar());
		scr2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		p1.add(scr1);
		p1.add(scr2);
		
		compare = new JButton("Compare");
		result = new JEditorPane("text/html","<center>(result will appear here)</center>");
		result.setPreferredSize(new Dimension(300,80));
		result.setEditable(false);
		//result.setHorizontalAlignment(JTextField.CENTER);
		
		
		JPanel p3 = new JPanel();
		//GridLayout g3 = new GridLayout(2,1);
		p3.setLayout(new BorderLayout());
		p3.add(compare,BorderLayout.NORTH);
		p3.add(result,BorderLayout.CENTER);
		add(p1,BorderLayout.CENTER);
		add(p3, BorderLayout.SOUTH);
		
		compare.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				compare();
			}
		});
	}

}
