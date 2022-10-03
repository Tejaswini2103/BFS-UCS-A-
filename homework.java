import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class homework {
	
	static Map<Integer,String> mapping = new HashMap<>();
	static Map<Node,Boolean> visited = new HashMap<>();
	static Map<Node,List<Node>> adj = new LinkedHashMap<>();
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
			 	String[] startPointArr = startPoint.split(" ");
	 			int startPointX = Integer.parseInt(startPointArr[0]);
	 			int startPointY = Integer.parseInt(startPointArr[1]);
	 			int startPointZ = Integer.parseInt(startPointArr[2]);
	 			 
			 	 while ((line = br.readLine()) != null) {
			 		
			 		 List<Node> listOfAdjNodes = new ArrayList<>();
			 		 String s = line;
			 		 String str[] = s.split(" ");
			 		 int x = Integer.parseInt(str[0]);
			 		 int y = Integer.parseInt(str[1]);
			 		 int z = Integer.parseInt(str[2]);
			 		 Node node = new Node(x,y,z);
			 		 visited.put(node, false);
			 		 for(int i=3;i<str.length;i++) {
			 			 int code = Integer.parseInt(str[i]);
			 			 String action = mapping.get(code);
			 			 Node adjNode=null;
			 			 if(action.equals("X+")) {
			 				adjNode = new Node(x+1,y,z);
			 			 }
			 			 else if(action.equals("X-")) {
			 				adjNode = new Node(x-1,y,z);
			 			 }
			 			 else if(action.equals("Y+")) {
			 				adjNode = new Node(x,y+1,z);
			 			}
			 			 else if(action.equals("Y-")) {
			 				adjNode = new Node(x,y-1,z);
			 			}
			 			 else if(action.equals("Z+")) {
			 				adjNode = new Node(x,y,z+1);
			 			}
			 			 else if(action.equals("Z-")) {
			 				adjNode = new Node(x,y,z-1);
			 			}
			 			 else if(action.equals("X+Y+")) {
			 				adjNode = new Node(x+1,y+1,z);
				 		}
			 			 else if(action.equals("X+Y-")) {
			 				adjNode = new Node(x+1,y-1,z);
				 		}
			 			 else if(action.equals("X-Y+")) {
			 				adjNode = new Node(x-1,y+1,z);
				 		}
			 			 else if(action.equals("X-Y-")) {
			 				adjNode = new Node(x+1,y-1,z);
				 		}
			 			else if(action.equals("X+Z+")) {
				 			adjNode = new Node(x+1,y,z+1);
					 	}
				 		else if(action.equals("X+Z-")) {
				 			adjNode = new Node(x+1,y,z-1);
					 	}
				 		 else if(action.equals("X-Z+")) {
				 			adjNode = new Node(x-1,y,z+1);
				 		}
			 			 else if(action.equals("X-Z-")) {
			 				adjNode = new Node(x-1,y,z-1);
				 		}
			 			 else if(action.equals("Y+Z+")) {
			 				adjNode = new Node(x,y+1,z+1);
				 		}
			 			 else if(action.equals("Y+Z-")) {
			 				adjNode = new Node(x,y+1,z-1);
				 		}
			 			 else if(action.equals("Y-Z+")) {
			 				adjNode = new Node(x,y-1,z+1);
				 		}
			 			 else if(action.equals("Y-Z-")) {
			 				adjNode = new Node(x,y-1,z-1);
				 		}
			 			 
			 			 if(node.getX()==startPointX && node.getY()==startPointY && node.getZ()==startPointZ) {
			 				startNode = node;
			 			 }
			 			 
			 			   
			 			    
			 			     
			 				listOfAdjNodes.add(adjNode);
			 				adj.put(node,listOfAdjNodes);
			 				
			 				 
			 				
			 				
			 		 }
			 	}
			 	 
			 	 
			 	for(Map.Entry m : adj.entrySet()) {
			 		Node node = (Node) m.getKey();
			 		List<Node> list = (List<Node>) m.getValue();
			 		
			 		System.out.println(node.getX()+" "+node.getY()+" "+node.getZ());
			 		
			 		System.out.println("Adjacency list is");
			 		
			 		for(int i=0;i<list.size();i++) {
			 			Node n = list.get(i);
			 			System.out.println(n.getX()+" "+n.getY()+" "+n.getZ());
			 			
			 		}
			 		
			 		System.out.println("************");
			 		
			 	}
			 	
			 	System.out.println("*******visited********");
			 	
			 	for(Map.Entry m : visited.entrySet()) {
			 		Node node = (Node) m.getKey();
			 		System.out.println(node.getX()+" "+node.getY()+" "+node.getZ()+" "+m.getValue());
			 		
			 	}
			 	
			 	System.out.println("*******visited********");
			 	
			 	
	        }
	}
	
	public static void bfs() {
		
		Queue<Node> queue = new LinkedList<>();
		queue.add(startNode);
		while(!queue.isEmpty()) {
			Node popped = queue.poll();
			System.out.println(popped.getX()+" "+popped.getY()+" "+popped.getZ());
			
			boolean res = visited.get(popped);
			if(res==false)
				visited.put(popped,true);
			
			Iterator<Node> iterator = adj.get(popped).iterator();
			while(iterator.hasNext()) {
				
				boolean ress = visited.get(iterator.next());
				if(ress==false) {
					visited.put(popped,true);
					queue.add(iterator.next());
				}
			  }
			}
			
		}
		

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		String toSplit = "0 0 10 11 16"; 
		String[] split = toSplit.split("\\s+",4);
		for(String s : split) {
			System.out.println(s);
		}
	/*	actionToCodeMapping();
		parseInputFile();
		bfs();
		*/
		
		int i = (int)1.23;
		

	}
}
