package algorithm.routing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import elements.facility.CTaxiwayLink;
import elements.facility.CTaxiwayNode;
import elements.network.ALink;
import elements.network.ANode;

public class DijkstraAlgorithm implements IRoutingAlgorithm{

	private List<ANode> iNode;
	private List<ALink> iLink;
	
	private List<ALink> iLinkIgnore = new ArrayList<ALink>();
	private Set<ANode> iUsedNodes= new HashSet<ANode>();
	private Set<ANode> iQofNodes= new HashSet<ANode>(); // Queue
	private LinkedList<ANode> iOutput;
	
	private Map<ANode, ANode> iCostChangeByWhom = new HashMap<ANode, ANode>();
	private Map<ANode, Double> iDistance = new HashMap<ANode, Double>();

	ANode iNodeOrigin;
	ANode iNodeDestin;
	
//	
//	public Dijkstra(NetworkBuilder aNetwork){
//		this.fNode = aNetwork.getNode();
//		this.fLink = aNetwork.getLink();
//	}

	
	@SuppressWarnings("unchecked")
	public DijkstraAlgorithm(List<? extends ALink> aLink, List<? extends ANode> aNode) {
		iNode = (List<ANode>) aNode;
		iLink = (List<ALink>) aLink;
	}
	
	/*
	 * Testing Code
	*/
//		public static void main(String args[]){
//	
//			NetworkBuilder network = NetworkBuilder.createNetwork();
//			
//			Dijkstra dj = new Dijkstra(network);
//			System.out.println("Size of Link : " + network.getLink().size());
//			System.out.println("Size of Node : " + network.getNode().size());		
//	
//			List<Node> nodes = new ArrayList<Node>();
//			
//			double estimatedTimeTOT = 0;
//			for(int i = 0; i < network.getNode().size(); i++){
//				for(int j = 0; j< network.getNode().size(); j++){
//					double startTime = System.currentTimeMillis();
//					nodes.add(dj.fNode.get(i));
//					nodes.add(dj.fNode.get(j));	
//					//System.out.println(nodes);
//					dj.findShortedPath(nodes);
//					nodes.removeAll(nodes);
//					double estimatedTime = System.currentTimeMillis() - startTime;
//					estimatedTimeTOT += estimatedTime;
//					//System.out.println("took " + estimatedTime + "ms");	
//				}
//			}
//			
//			System.out.println("Average Compuation Time " + estimatedTimeTOT/(network.getNode().size()*network.getNode().size())  + "ms");
//	
//		}




	/**
	 * 
	 * Coded by S. J. Yun
	 */
	public LinkedList<? extends ANode> findShortedPath(List<? extends ANode> aNodeStartMiddleEnd){
		iOutput = new LinkedList<ANode>();
		
		double l_TotalDistance = 0;
		//System.out.println("=========================================================================");
		for(int loop_node = aNodeStartMiddleEnd.size()-1; loop_node>0; loop_node--){

			// Initialize settled Set
			iUsedNodes.clear();
			iQofNodes.clear();
			iDistance.clear();
			iCostChangeByWhom.clear();		
//			iOutput.clear();

			// Extract Nodes this phase
			iNodeOrigin	= iNode.get(iNode.indexOf(aNodeStartMiddleEnd.get(loop_node-1)));;
			iNodeDestin	= iNode.get(iNode.indexOf(aNodeStartMiddleEnd.get(loop_node)));

			// Initialize
			iQofNodes.add(iNodeOrigin);
			iDistance.put(iNodeOrigin, 0.0);
			iCostChangeByWhom.put(iNodeOrigin, null);
			//System.out.println(l_NodeOrigin.getName() + " > " + l_NodeDestin.getName() );

			// Running Algorithm
			while (iQofNodes.size()>0 && iUsedNodes.contains(iNodeDestin) == false){
				ANode node = findMinCostNode(iQofNodes);
				iUsedNodes.add(node);
				iQofNodes.remove(node);
				insertNodeIntoQ(node);	
			}

			/**
			// BackTracking Result
			 **/
			ANode l_backTrackingNode = iNodeDestin;
			while(iCostChangeByWhom.get(l_backTrackingNode) != null){		
				if(iOutput.indexOf(l_backTrackingNode)!=0){
					iOutput.addFirst(l_backTrackingNode);
				}				
				l_backTrackingNode = iCostChangeByWhom.get(l_backTrackingNode);
			}		
			
			iOutput.addFirst(l_backTrackingNode);
			try{
				l_TotalDistance += iDistance.get(iNodeDestin);
			}catch(Exception e){
				iOutput.clear();				
			}			
		}
		System.out.println(iOutput);
		
		
		
		
		
		return iOutput ;
	}

	
	
	
	
	private ANode findMinCostNode(Set<ANode> aQofNodes){
		ANode l_MinCostNode = null;
		for (ANode loopNode : aQofNodes){
			if(l_MinCostNode==null){l_MinCostNode = loopNode;};

			if (iDistance.get(l_MinCostNode) > iDistance.get(loopNode)){
				l_MinCostNode = loopNode;
			}

		}//for (Node loopNode : aUnUsedNodes){

		return l_MinCostNode;
	}

	private void insertNodeIntoQ(ANode aNode){
		for(ALink loopLink : iLink){
			// Skip Link
			if(iLinkIgnore.contains(loopLink)) continue;
				
			
			
			double l_AdjacentCost = 0.0;	
			ANode l_AdjacentNode = null;

			// Find adjacent node and get Cost (for asymmetric distance, it is divided)
			if(loopLink.getOrigin().equals(aNode)){
				l_AdjacentCost = loopLink.getDistance();	
				l_AdjacentNode = loopLink.getDestination(); 		
			}else if(loopLink.getDestination().equals(aNode)){
				l_AdjacentCost = loopLink.getDistance();
				l_AdjacentNode = loopLink.getOrigin();				
			}	

			
			
			// RunwayLink?
			if(loopLink.isRunnway()) {
				l_AdjacentCost = l_AdjacentCost * 1.5;
			}
			
			
			
			
		
			
			// Calculate Cost from Start Node and Insert into Queue and Distance Map	
			if (iOutput.indexOf(l_AdjacentNode) == 0 && aNode.equals(iNodeOrigin) && iOutput.indexOf(iNodeOrigin) == 1){
				continue;
			}
//			if (fOutput.indexOf(l_AdjacentNode) == 0 && aNode.getName().equalsIgnoreCase(fNodeOrigin.getName())){
//				continue;
//			}
//			if(l_AdjacentNode != null && l_AdjacentNode.getName().equalsIgnoreCase("RWY1_2_1")){
//				System.out.println(fOutput.indexOf(l_AdjacentNode));
//			}
			if(l_AdjacentNode != null && (iOutput.indexOf(l_AdjacentNode) != 1)){ 
				// Calculate Cost from Start Node
				double l_updatedCost = l_AdjacentCost + iDistance.get(aNode).doubleValue();
				// Insert into Queue when Calculated Cost is less than previous calculated cost
				if(iDistance.get(l_AdjacentNode) == null || (iDistance.get(l_AdjacentNode) != null && iDistance.get(l_AdjacentNode).doubleValue() > l_updatedCost)){
					// insert into Queue
					iQofNodes.add(l_AdjacentNode);  
					// insert into distance map
					iDistance.put(l_AdjacentNode, l_updatedCost);
					// insert into previous matrix
					iCostChangeByWhom.put(l_AdjacentNode, aNode);
				}

			}

		}
	}


	private double calculateHeading(ANode aOrigin, ANode aDestin){
		
		double[] pt1 = { aOrigin.getCoordination().getXCoordination(), aOrigin.getCoordination().getYCoordination()};
		double[] pt2 = { aDestin.getCoordination().getXCoordination(), aOrigin.getCoordination().getYCoordination()};
		
		pt2[0] = pt2[0] - pt1[0];
		pt2[1] = pt2[1] - pt1[1];
		
//		System.out.println(Arrays.toString(pt1));
//		System.out.println(Arrays.toString(pt2));
//		System.out.println();
		
		double l_initialDegree = 1.0*(int)((Math.atan(pt2[1]/pt2[0])*180/3.1415926535)*10000)/10000;
//		System.out.println(l_initialDegree);
		if (pt2[0] >= 0 && pt2[1] >= 0){l_initialDegree = 90  - l_initialDegree;} // 1
		if (pt2[0] <  0 && pt2[1] >= 0){l_initialDegree = 270 - l_initialDegree;} // 2
		if (pt2[0] <  0 && pt2[1] <  0){l_initialDegree = 270 - l_initialDegree;} // 3
		if (pt2[0] >= 0 && pt2[1] <  0){l_initialDegree = 90  - l_initialDegree;} // 4
		return l_initialDegree;
				
	}

	@Override
	public LinkedList<? extends ANode> findShortedPathIgnoreNodeList(List<? extends ANode> aNodeStartMiddleEnd,
			List<? extends ALink> aIgnoredLinkList) {
		
		iLinkIgnore.clear();
		
		for(ALink loopLinkInputIg : aIgnoredLinkList) {
			iLinkIgnore.add(loopLinkInputIg);
		}
		
		// Run Algorithm
		 LinkedList<? extends ANode> output =  findShortedPath(aNodeStartMiddleEnd);
		
		
		// Restore Ignore List
		iLinkIgnore.clear();
		
		return output;
	}

	//	public <T> LinkedList<T> reverseLinkedList (LinkedList<T> aLinkedList)
	//	{	
	//		LinkedList<T> output = new LinkedList<T>();
	//		for(T loopList : aLinkedList){
	//			output.addFirst(loopList);
	//		}		
	//		
	//		return output;
	//	}
	//	


}














