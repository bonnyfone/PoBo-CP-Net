import pobo.common.CPNETAdjMatrix;
import pobo.cpnet.CPNET;


public class Test {
	public static void main(String[] args) {
		CPNET cpnet = new CPNET(3);
		
		cpnet.addVertex("A");
		cpnet.addVertex("B");
		cpnet.addVertex("C");
		
		cpnet.addEdge("A", "B");
		cpnet.addEdge("B", "C");
		
		cpnet.generate();
		
		cpnet.setPreference("A", "0", 1);
		cpnet.setPreference("B", "1", 1);
		cpnet.setPreference("B", "0", 0);
		cpnet.setPreference("C", "1", 1);
		cpnet.setPreference("C", "0", 0); 
		
		//CPNET cpnet = XMLInterface.readCPNET("test.xml");
		
		System.out.println(((CPNETAdjMatrix)cpnet.getCpnet_data()).toString());
		for (int i = 0;i < cpnet.getVertex().length; i++) {
			System.out.println(cpnet.getVertex()[i].toString());
		}
		System.out.println("Best Solution: " + cpnet.getOptimalSolution().getSolutionAsString());
		cpnet.getPartialOrder();
		
		String s1 = "100";
		String s2 = "100";
		
		System.out.println(s1 +" "+ cpnet.compareSolutions(s1, s2).toString() +" "+ s2);
		
		System.out.println(cpnet.getPartialOrder());
		
		//XMLInterface.writeCPNET(cpnet, "test.xml");
	}
}
