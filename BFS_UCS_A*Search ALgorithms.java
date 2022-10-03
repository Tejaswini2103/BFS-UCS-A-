import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Homework8 {
	
	class Node implements Comparator<Node>{
		String coordinate;
		int pathCost;
		
		Node(){}
		
		Node(String coordinate,int pathCost) {
			this.coordinate = coordinate;
			this.pathCost = pathCost;
		}
		public String getCoordinate() {
			return coordinate;
		}
		public int getPathCost() {
			return pathCost;
		}
		@Override 
		public int compare(Node node1, Node node2)
	    {
	 
	        if (node1.pathCost < node2.pathCost)
	            return -1;
	 
	        if (node1.pathCost > node2.pathCost)
	            return 1;

	        return 0;
	    }
		
	}
	

	static Map<Integer,String> mapping = new HashMap<>();
	static Map<String,Boolean> visitedForBfs = new HashMap<>();
	static Map<String,List<String>> adjForBfs = new LinkedHashMap<>();
	static Map<String,String> parent = new HashMap<>();
	static Map<String,List<Node>> adjForUCS = new LinkedHashMap<>();
	static Map<String,List<Node>> adjForAStar = new LinkedHashMap<>();
	static Map<String,Boolean> visitedForUCS = new HashMap<>();
	static Map<String,Boolean> visitedForAStar = new HashMap<>();
	static Map<String,Integer> heuristic = new HashMap<>();
	static Map<String,String> parentChildMapping = new HashMap<>();
	static Map<String,String> parentChildMappingForAStar = new HashMap<>();
	static Map<String,Integer> CostMappingForUCS = new HashMap<>();
	static Map<String,Integer> CostMappingForAStar = new HashMap<>();
	static PriorityQueue<Node> pqueue;
	static Map<String,Integer> dist = new HashMap<>();
	static Map<String,Integer> gValue = new HashMap<>();
	static Map<String,Integer> fValue = new HashMap<>();
    static Set<String> covered = new HashSet<>(adjForUCS.size());
	static String algorithm;
 	static String bound;
 	static String startPoint;
 	static String exitPoint;
 	static String numberOfGridPoints;
 	static Node startNode;
	
	public static void actionToCodeMapping() {
		
		mapping.put(1,"X+");
		mapping.put(2,"X-");
		mapping.put(3,"Y+");
		mapping.put(4,"Y-");
		mapping.put(5,"Z+");
		mapping.put(6,"Z-");
		mapping.put(7,"X+Y+");
		mapping.put(8,"X+Y-");
		mapping.put(9,"X-Y+");
		mapping.put(10,"X-Y-");
		mapping.put(11,"X+Z+");
		mapping.put(12,"X+Z-");
		mapping.put(13,"X-Z+");
		mapping.put(14,"X-Z-");
		mapping.put(15,"Y+Z+");
		mapping.put(16,"Y+Z-");
		mapping.put(17,"Y-Z+");
		mapping.put(18,"Y-Z-");
		
	}
	
	public static void parseInputFile() throws FileNotFoundException, IOException {
		
		 try (BufferedReader br = new BufferedReader(new FileReader("src/input.txt"))) {
	            
			 	String line;
			 	algorithm = br.readLine();
			 	bound = br.readLine();
			 	startPoint = br.readLine();
			 	exitPoint = br.readLine();
			 	numberOfGridPoints = br.readLine();
	 			 
			 	
			 	if(algorithm.equals("BFS")) {
			 	
			 	 while ((line = br.readLine()) != null) {
			 		
			 		 List<String> listOfAdjNodes = new ArrayList<>();
			 		 String s = line;
			 		 String pointAndActions[] = s.split("\\s+",4);
			 		 
			 		 String point = pointAndActions[0]+" "+pointAndActions[1]+" "+pointAndActions[2];
			 		 
			 		 if(!visitedForBfs.containsKey(point)) {
			 			visitedForBfs.put(point, false);
			 		 }
			 		 
			 		 String pointCoordinates[] = point.split(" ");
			 		 int pointX = Integer.parseInt(pointCoordinates[0]);
			 		 int pointY = Integer.parseInt(pointCoordinates[1]);
			 		 int pointZ = Integer.parseInt(pointCoordinates[2]);
			 		 
			 		 String adjCoordinates = pointAndActions[3];
			 		 
			 		 String adjPoints[] = adjCoordinates.split(" ");
			 		 
			 		 String adjPointStr=null;
			 		 for(String adjPoint : adjPoints) {
			 			 
			 			 int code = Integer.parseInt(adjPoint);
			 			 
			 			 String action = mapping.get(code);
			 			 
			 			 if(action.equals("X+")) {
			 				
			 				adjPointStr = (pointX+1)+" "+pointY+" "+pointZ;
			 				 
			 			 }
			 			 else if(action.equals("X-")) {
			 				
			 				adjPointStr = pointX-1+" "+pointY+" "+pointZ;
			 			 }
			 			 else if(action.equals("Y+")) {
			 				
			 				adjPointStr = pointX+" "+(pointY+1)+" "+pointZ;
			 			}
			 			 else if(action.equals("Y-")) {
			 				adjPointStr = pointX+" "+(pointY-1)+" "+pointZ;
			 			}
			 			 else if(action.equals("Z+")) {
			 				adjPointStr = pointX+" "+pointY+" "+(pointZ+1);
			 			}
			 			 else if(action.equals("Z-")) {
			 				adjPointStr = pointX+" "+pointY+" "+(pointZ-1);
			 			}
			 			 else if(action.equals("X+Y+")) {
			 				adjPointStr = (pointX+1)+" "+(pointY+1)+" "+pointZ;
				 		}
			 			 else if(action.equals("X+Y-")) {
			 				adjPointStr = (pointX+1)+" "+(pointY-1)+" "+pointZ;
				 		}
			 			 else if(action.equals("X-Y+")) {
			 				adjPointStr = (pointX-1)+" "+(pointY+1)+" "+pointZ;
				 		}
			 			 else if(action.equals("X-Y-")) {
			 				adjPointStr = (pointX-1)+" "+(pointY-1)+" "+pointZ;
				 		}
			 			else if(action.equals("X+Z+")) {
			 				adjPointStr = (pointX+1)+" "+pointY+" "+(pointZ+1);
					 	}
				 		else if(action.equals("X+Z-")) {
				 			adjPointStr = (pointX+1)+" "+pointY+" "+(pointZ-1);
					 	}
				 		 else if(action.equals("X-Z+")) {
				 			adjPointStr = (pointX-1)+" "+pointY+" "+(pointZ+1);
				 		}
			 			 else if(action.equals("X-Z-")) {
			 				adjPointStr = (pointX-1)+" "+pointY+" "+(pointZ-1);
				 		}
			 			 else if(action.equals("Y+Z+")) {
			 				adjPointStr = pointX+" "+(pointY+1)+" "+(pointZ+1);
				 		}
			 			 else if(action.equals("Y+Z-")) {
			 				adjPointStr = pointX+" "+(pointY+1)+" "+(pointZ-1);
				 		}
			 			 else if(action.equals("Y-Z+")) {
			 				adjPointStr = pointX+" "+(pointY-1)+" "+(pointZ+1);
				 		}
			 			 else if(action.equals("Y-Z-")) {
			 				adjPointStr = pointX+" "+(pointY-1)+" "+(pointZ-1);
				 		}
			 			 
			 			 
			 			listOfAdjNodes.add(adjPointStr);
			 			adjForBfs.put(point,listOfAdjNodes);
			 				
			 		    		 
			 		 }
			 	}
			 	 
			 	if(!adjForBfs.containsKey(startPoint) || !adjForBfs.containsKey(exitPoint)) {
			 		PrintWriter out = new PrintWriter("src/output.txt", "UTF-8");
			 		out.print("FAIL");
			 		out.close();
			 	}
			 		
			 	else { 
			 		bfs();
			 		findOptimalPathAndCost();
			 	}
			 	} 
			 	
			 	
			 	else if(algorithm.equals("UCS")) {
			 		
			 		Homework8 hw = new Homework8();
			 		while ((line = br.readLine()) != null) {
			 			
			 			 List<Node> listOfAdjNodes = new ArrayList<>();
			 			 String s = line;
				 		 String pointAndActions[] = s.split("\\s+",4);
				 		 String point = pointAndActions[0]+" "+pointAndActions[1]+" "+pointAndActions[2];
				 		 String pointCoordinates[] = point.split(" ");
				 		 
				 		 if(!visitedForUCS.containsKey(point)) {
				 			 	visitedForUCS.put(point, false);
					 	 }
				 		 
				 		 int pointX = Integer.parseInt(pointCoordinates[0]);
				 		 int pointY = Integer.parseInt(pointCoordinates[1]);
				 		 int pointZ = Integer.parseInt(pointCoordinates[2]);
				 		 
				 		 String adjCoordinates = pointAndActions[3];
				 		 
				 		 String adjPoints[] = adjCoordinates.split(" ");
				 		 
				 		 String adjPointStr=null;
				 		 int adjPointCost = 0;
				 		 
				 		 for(String adjPoint : adjPoints) {
				 			int code = Integer.parseInt(adjPoint);
				 			 
				 			 String action = mapping.get(code);
				 			 
				 			 if(action.equals("X+")) {
				 				
				 				adjPointCost = 10;
				 				adjPointStr = (pointX+1)+" "+pointY+" "+pointZ;
				 				 
				 			 }
				 			 else if(action.equals("X-")) {
				 				
				 				adjPointCost = 10; 
				 				adjPointStr = pointX-1+" "+pointY+" "+pointZ;
				 			 }
				 			 else if(action.equals("Y+")) {
				 				
				 				adjPointCost = 10;
				 				adjPointStr = pointX+" "+(pointY+1)+" "+pointZ;
				 			}
				 			 else if(action.equals("Y-")) {
				 				
				 				adjPointCost = 10; 
				 				adjPointStr = pointX+" "+(pointY-1)+" "+pointZ;
				 			}
				 			 else if(action.equals("Z+")) {
				 				
				 				adjPointCost = 10; 
				 				adjPointStr = pointX+" "+pointY+" "+(pointZ+1);
				 			}
				 			 else if(action.equals("Z-")) {
				 				
				 				adjPointCost = 10; 
				 				adjPointStr = pointX+" "+pointY+" "+(pointZ-1);
				 			}
				 			 else if(action.equals("X+Y+")) {
				 				 
				 				adjPointCost = 14;
				 				adjPointStr = (pointX+1)+" "+(pointY+1)+" "+pointZ;
					 		}
				 			 else if(action.equals("X+Y-")) {
				 				
				 				adjPointCost = 14; 
				 				adjPointStr = (pointX+1)+" "+(pointY-1)+" "+pointZ;
					 		}
				 			 else if(action.equals("X-Y+")) {
				 				 
				 				adjPointCost = 14; 
				 				adjPointStr = (pointX-1)+" "+(pointY+1)+" "+pointZ;
					 		}
				 			 else if(action.equals("X-Y-")) {
				 				 
				 				adjPointCost = 14; 
				 				adjPointStr = (pointX-1)+" "+(pointY-1)+" "+pointZ;
					 		}
				 			else if(action.equals("X+Z+")) {
				 				
				 				adjPointCost = 14;
				 				adjPointStr = (pointX+1)+" "+pointY+" "+(pointZ+1);
						 	}
					 		else if(action.equals("X+Z-")) {
					 			
					 			adjPointCost = 14;
					 			adjPointStr = (pointX+1)+" "+pointY+" "+(pointZ-1);
						 	}
					 		 else if(action.equals("X-Z+")) {
					 			 
					 			adjPointCost = 14; 
					 			adjPointStr = (pointX-1)+" "+pointY+" "+(pointZ+1);
					 		}
				 			 else if(action.equals("X-Z-")) {
				 				 
				 				adjPointCost = 14; 
				 				adjPointStr = (pointX-1)+" "+pointY+" "+(pointZ-1);
					 		}
				 			 else if(action.equals("Y+Z+")) {
				 				 
				 				adjPointCost = 14; 
				 				adjPointStr = pointX+" "+(pointY+1)+" "+(pointZ+1);
					 		}
				 			 else if(action.equals("Y+Z-")) {
				 				 
				 				adjPointCost = 14; 
				 				adjPointStr = pointX+" "+(pointY+1)+" "+(pointZ-1);
					 		}
				 			 else if(action.equals("Y-Z+")) {
				 				 
				 				adjPointCost = 14; 
				 				adjPointStr = pointX+" "+(pointY-1)+" "+(pointZ+1);
					 		}
				 			 else if(action.equals("Y-Z-")) {
				 				 
				 				adjPointCost = 14; 
				 				adjPointStr = pointX+" "+(pointY-1)+" "+(pointZ-1);
					 		}
				 			
				 			 
				 			Node e = hw.new Node(adjPointStr,adjPointCost);
				 			listOfAdjNodes.add(e);
				 			adjForUCS.put(point,listOfAdjNodes);

				 		 }
			 			
			 		}
			 	
			 		/*
			 		System.out.println(adjForUCS);
			 		for(Map.Entry m: adjForUCS.entrySet()) {
			 			System.out.print(m.getKey()+"---");
			 			
			 			List list = (List) m.getValue();
			 			
			 			for(int i=0;i<list.size();i++) {
			 				Node e = (Homework8.Node) list.get(i);
			 				System.out.print(e.getCoordinate()+ " "+ e.getPathCost()+" ");
			 			}
			 			
			 			System.out.println();
			 		}
			 		
			 		*/
			 		if(!adjForUCS.containsKey(startPoint) || !adjForUCS.containsKey(exitPoint)) {
				 		PrintWriter out = new PrintWriter("src/output.txt", "UTF-8");
				 		out.print("FAIL");
				 		out.close();
				 	}
			 		else {
			 		ucs(adjForUCS,startPoint);
			 		}
			 	}
			 	
			 	else if(algorithm.equals("A*")) {
			 		
			 		Homework8 hw = new Homework8();
			 		while ((line = br.readLine()) != null) {
			 			
			 			 List<Node> listOfAdjNodes = new ArrayList<>();
			 			 String s = line;
				 		 String pointAndActions[] = s.split("\\s+",4);
				 		 String point = pointAndActions[0]+" "+pointAndActions[1]+" "+pointAndActions[2];
				 		 String pointCoordinates[] = point.split(" ");
				 		 
				 		 if(!visitedForAStar.containsKey(point)) {
				 			visitedForAStar.put(point, false);
					 	 }
				 		 
				 		 int pointX = Integer.parseInt(pointCoordinates[0]);
				 		 int pointY = Integer.parseInt(pointCoordinates[1]);
				 		 int pointZ = Integer.parseInt(pointCoordinates[2]);
				 		 
				 		 String adjCoordinates = pointAndActions[3];
				 		 
				 		 String adjPoints[] = adjCoordinates.split(" ");
				 		 
				 		 String adjPointStr=null;
				 		 int adjPointCost = 0;
				 		 
				 		 for(String adjPoint : adjPoints) {
				 			int code = Integer.parseInt(adjPoint);
				 			 
				 			 String action = mapping.get(code);
				 			 
				 			 if(action.equals("X+")) {
				 				
				 				adjPointCost = 10;
				 				adjPointStr = (pointX+1)+" "+pointY+" "+pointZ;
				 				 
				 			 }
				 			 else if(action.equals("X-")) {
				 				
				 				adjPointCost = 10; 
				 				adjPointStr = pointX-1+" "+pointY+" "+pointZ;
				 			 }
				 			 else if(action.equals("Y+")) {
				 				
				 				adjPointCost = 10;
				 				adjPointStr = pointX+" "+(pointY+1)+" "+pointZ;
				 			}
				 			 else if(action.equals("Y-")) {
				 				
				 				adjPointCost = 10; 
				 				adjPointStr = pointX+" "+(pointY-1)+" "+pointZ;
				 			}
				 			 else if(action.equals("Z+")) {
				 				
				 				adjPointCost = 10; 
				 				adjPointStr = pointX+" "+pointY+" "+(pointZ+1);
				 			}
				 			 else if(action.equals("Z-")) {
				 				
				 				adjPointCost = 10; 
				 				adjPointStr = pointX+" "+pointY+" "+(pointZ-1);
				 			}
				 			 else if(action.equals("X+Y+")) {
				 				 
				 				adjPointCost = 14;
				 				adjPointStr = (pointX+1)+" "+(pointY+1)+" "+pointZ;
					 		}
				 			 else if(action.equals("X+Y-")) {
				 				
				 				adjPointCost = 14; 
				 				adjPointStr = (pointX+1)+" "+(pointY-1)+" "+pointZ;
					 		}
				 			 else if(action.equals("X-Y+")) {
				 				 
				 				adjPointCost = 14; 
				 				adjPointStr = (pointX-1)+" "+(pointY+1)+" "+pointZ;
					 		}
				 			 else if(action.equals("X-Y-")) {
				 				 
				 				adjPointCost = 14; 
				 				adjPointStr = (pointX-1)+" "+(pointY-1)+" "+pointZ;
					 		}
				 			else if(action.equals("X+Z+")) {
				 				
				 				adjPointCost = 14;
				 				adjPointStr = (pointX+1)+" "+pointY+" "+(pointZ+1);
						 	}
					 		else if(action.equals("X+Z-")) {
					 			
					 			adjPointCost = 14;
					 			adjPointStr = (pointX+1)+" "+pointY+" "+(pointZ-1);
						 	}
					 		 else if(action.equals("X-Z+")) {
					 			 
					 			adjPointCost = 14; 
					 			adjPointStr = (pointX-1)+" "+pointY+" "+(pointZ+1);
					 		}
				 			 else if(action.equals("X-Z-")) {
				 				 
				 				adjPointCost = 14; 
				 				adjPointStr = (pointX-1)+" "+pointY+" "+(pointZ-1);
					 		}
				 			 else if(action.equals("Y+Z+")) {
				 				 
				 				adjPointCost = 14; 
				 				adjPointStr = pointX+" "+(pointY+1)+" "+(pointZ+1);
					 		}
				 			 else if(action.equals("Y+Z-")) {
				 				 
				 				adjPointCost = 14; 
				 				adjPointStr = pointX+" "+(pointY+1)+" "+(pointZ-1);
					 		}
				 			 else if(action.equals("Y-Z+")) {
				 				 
				 				adjPointCost = 14; 
				 				adjPointStr = pointX+" "+(pointY-1)+" "+(pointZ+1);
					 		}
				 			 else if(action.equals("Y-Z-")) {
				 				 
				 				adjPointCost = 14; 
				 				adjPointStr = pointX+" "+(pointY-1)+" "+(pointZ-1);
					 		}
				 			
				 			 
				 			Node e = hw.new Node(adjPointStr,adjPointCost);
				 			listOfAdjNodes.add(e);
				 			adjForAStar.put(point,listOfAdjNodes);

				 		 }
			 			
			 		}
			 	
			 		/*
			 		System.out.println(adjForUCS);
			 		for(Map.Entry m: adjForUCS.entrySet()) {
			 			System.out.print(m.getKey()+"---");
			 			
			 			List list = (List) m.getValue();
			 			
			 			for(int i=0;i<list.size();i++) {
			 				Node e = (Homework8.Node) list.get(i);
			 				System.out.print(e.getCoordinate()+ " "+ e.getPathCost()+" ");
			 			}
			 			
			 			System.out.println();
			 		}
			 		
			 		*/
			 		if(!adjForAStar.containsKey(startPoint) || !adjForAStar.containsKey(exitPoint)) {
				 		PrintWriter out = new PrintWriter("src/output.txt", "UTF-8");
				 		out.print("FAIL");
				 		out.close();
				 	}
			 		else {
			 		computeHeuristic();
			 		}
			 	}
			 	
	        }
	}
	
	public static void computeHeuristic() throws FileNotFoundException, UnsupportedEncodingException {
		
		
		String exitcoordinates[] = exitPoint.split(" ");
		int gx = Integer.parseInt(exitcoordinates[0]);
		int gy = Integer.parseInt(exitcoordinates[1]);
		int gz = Integer.parseInt(exitcoordinates[2]);
		
		for(Map.Entry m : visitedForAStar.entrySet()) {

				String currentCoordinate = (String) m.getKey();
				String coordinates[] = currentCoordinate.split(" ");
				int x = Integer.parseInt(coordinates[0]);
				int y = Integer.parseInt(coordinates[1]);
				int z = Integer.parseInt(coordinates[2]);
				
				int hx = (int) Math.pow(gx-x, 2);
				int hy = (int) Math.pow(gy-y, 2);
				int hz = (int) Math.pow(gz-z, 2);
				
				int euclideanDistance = (int) Math.sqrt(hx+hy+hz);
				
				heuristic.put(currentCoordinate, euclideanDistance);
				
				
				
		}
		astar(adjForAStar,startPoint);
		
		
	}
	
	public static void astar(Map<String,List<Node>> adjForAStar, String start) throws FileNotFoundException, UnsupportedEncodingException {
		pqueue = new PriorityQueue<>(adjForAStar.size(),new Homework8().new Node());
		
		for(Map.Entry m : adjForAStar.entrySet()) {
			gValue.put((String)m.getKey(), Integer.MAX_VALUE);
			fValue.put((String)m.getKey(), Integer.MAX_VALUE);
			
		}
		
		pqueue.add(new Homework8().new Node(start, 0));
		gValue.put(start, 0);
		fValue.put(start, heuristic.get(startPoint));
		visitedForAStar.put(start, true);
		parentChildMappingForAStar.put(start,null);
		
		covered.clear();
		
		while (covered.size() != adjForAStar.size()) {

            if (pqueue.isEmpty())
                return;

            String point = pqueue.remove().coordinate;
 
            if (covered.contains(point))
                continue;

            if(covered.contains(exitPoint))
            	break;  
            
            covered.add(point);
            
            AdjacentNeighboursForAStar(point);
            
        }
		
		System.out.println(fValue);
		System.out.println(CostMappingForAStar);
		System.out.println(parentChildMappingForAStar);
		findOptimalPathAndCostforAStar();
		
	}
			
	public static void AdjacentNeighboursForAStar(String point) {
		int edgeDistance = -1;
        int computedGValue = -1;
        int computedFValue = -1;
 
        List<Node> list = adjForAStar.get(point);
        
        for (int i = 0; i < list.size(); i++) {
            Node adjNode = list.get(i);
 
            if (!covered.contains(adjNode.coordinate)) {
                edgeDistance = adjNode.pathCost;
                computedGValue = gValue.get(point) + edgeDistance;
                

                computedFValue = computedGValue + heuristic.get(adjNode.coordinate);
                
 
                if (computedFValue < fValue.get(adjNode.coordinate)) {
                	gValue.put(adjNode.coordinate,computedGValue);
                	fValue.put(adjNode.coordinate,computedFValue);
                	
                }
                	
                	pqueue.add(new Homework8().new Node(adjNode.coordinate, fValue.get(adjNode.coordinate)));
            }
            
            if(visitedForAStar.get(adjNode.coordinate)==false) {
            	parentChildMappingForAStar.put(adjNode.coordinate,point);
            	CostMappingForAStar.put(adjNode.coordinate, adjNode.pathCost);
            	visitedForAStar.put(adjNode.coordinate, true);
            }
            
        }
       
	}
	
	
	public static void findOptimalPathAndCostforAStar() throws FileNotFoundException, UnsupportedEncodingException {
		
		PrintWriter out = new PrintWriter("src/output.txt", "UTF-8");
		int count=1;
		List<String> result = new ArrayList<>();
		out.println(gValue.get(exitPoint));
		System.out.println(gValue.get(exitPoint));
		String node = exitPoint;
		System.out.println(node);
		result.add(node+" "+CostMappingForAStar.get(node));
		while(parentChildMappingForAStar.get(node)!=null) {
			System.out.println(parentChildMappingForAStar.get(node)+" "+CostMappingForAStar.get(parentChildMappingForAStar.get(node)));
			result.add(parentChildMappingForAStar.get(node)+" "+CostMappingForAStar.get(parentChildMappingForAStar.get(node)));
			node = parentChildMappingForAStar.get(node);
			count++;
		}
		System.out.println(count);
		out.println(count);
		out.println(startPoint+" "+0);
		int i;
		for(i=result.size()-2;i>0;i--) {
			out.println(result.get(i));
		}
		out.print(result.get(i));
		out.close();
		
	}
	
	
	
	public static void ucs(Map<String,List<Node>> adjForUCS, String start) throws FileNotFoundException, UnsupportedEncodingException {
		
		pqueue = new PriorityQueue<>(adjForUCS.size(),new Homework8().new Node());
		
		for(Map.Entry m : adjForUCS.entrySet()) {
			dist.put((String)m.getKey(), Integer.MAX_VALUE);
		}
		
		pqueue.add(new Homework8().new Node(start, 0));
		dist.put(start, 0);
		visitedForUCS.put(start, true);
		parentChildMapping.put(start+" "+0,null);
		
		while (covered.size() != adjForUCS.size()) {

            if (pqueue.isEmpty())
                return;

            String point = pqueue.remove().coordinate;
 
            if (covered.contains(point))
                continue;

            if(covered.contains(exitPoint))
            	break;  
            
            covered.add(point);
            
            AdjacentNeighbours(point);
            
        }
		
		System.out.println(dist);
		System.out.println(parentChildMapping);
		System.out.println(CostMappingForUCS);
		findOptimalPathAndCostforUCS();
		
    }
	
	public static void AdjacentNeighbours(String point)
    {
        int edgeDistance = -1;
        int computedDistance = -1;
 
        List<Node> list = adjForUCS.get(point);
        
        for (int i = 0; i < list.size(); i++) {
            Node adjNode = list.get(i);
 
            if (!covered.contains(adjNode.coordinate)) {
                edgeDistance = adjNode.pathCost;
                computedDistance = dist.get(point) + edgeDistance;
 
                if (computedDistance < dist.get(adjNode.coordinate));
                	dist.put(adjNode.coordinate,computedDistance);
 
                	pqueue.add(new Homework8().new Node(adjNode.coordinate, dist.get(adjNode.coordinate)));
            }
            
            if(visitedForUCS.get(adjNode.coordinate)==false) {
            	parentChildMapping.put(adjNode.coordinate,point);
            	CostMappingForUCS.put(adjNode.coordinate, adjNode.pathCost);
            	visitedForUCS.put(adjNode.coordinate, true);
            }
            
        }
        
    }
	
	
	
	
	public static void findOptimalPathAndCostforUCS() throws FileNotFoundException, UnsupportedEncodingException {
		
		PrintWriter out = new PrintWriter("src/output.txt", "UTF-8");
		int count=1;
		List<String> result = new ArrayList<>();
		out.println(dist.get(exitPoint));
		System.out.println(dist.get(exitPoint));
		String node = exitPoint;
		System.out.println(node);
		result.add(node+" "+CostMappingForUCS.get(node));
		while(parentChildMapping.get(node)!=null) {
			System.out.println(parentChildMapping.get(node)+" "+CostMappingForUCS.get(parentChildMapping.get(node)));
			result.add(parentChildMapping.get(node)+" "+CostMappingForUCS.get(parentChildMapping.get(node)));
			node = parentChildMapping.get(node);
			count++;
		}
		System.out.println(count);
		out.println(count);
		out.println(startPoint+" "+0);
		int i;
		for(i=result.size()-2;i>0;i--) {
			out.println(result.get(i));
		}
		out.print(result.get(i));
		out.close();
	}


	
	public static void bfs() {
		
		Queue<String> queue = new LinkedList<>();
		queue.add(startPoint);
		visitedForBfs.put(startPoint,true);
		parent.put(startPoint, null);
		while(!queue.isEmpty()) {
			String point = queue.poll();
		//	System.out.println(point);
			Iterator<String> iterator = adjForBfs.get(point).iterator();
			
			while(iterator.hasNext()) {
				String p = iterator.next();
				if(visitedForBfs.get(p)==false) {
				queue.add(p);
				visitedForBfs.put(p, true);
				parent.put(p, point);
				
				if(p.equals(exitPoint))
					return;
				
				}
			}
		}
	}
	
	public static void findOptimalPathAndCost() throws FileNotFoundException, UnsupportedEncodingException {
		
		
		List<String> result = new ArrayList<>();
		PrintWriter out = new PrintWriter("src/output.txt", "UTF-8");
		int optimalCost = 0;
		String node = exitPoint;
		result.add(node);
		while(parent.get(node)!=null) {
			result.add(parent.get(node));
			node = parent.get(node);
			optimalCost++;
		}
		out.println(optimalCost);
		out.println(optimalCost+1);
		out.println(result.get(result.size()-1)+" "+0);
		int i;
		for(i = result.size()-2;i>0;i--) {
			out.println(result.get(i)+" "+1);
		}
		out.print(result.get(i)+" "+1);
		out.close();
		
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		actionToCodeMapping();
		parseInputFile();
	}

}
