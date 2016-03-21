import java.awt.Point;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;



public class Test implements AIModule
{
   
   private class Node
   {
	   final Point x;
	   double y;
	   double g;
	   Node parent;
	   
	   private Node(final Point x1, final double y1, final double g1, final Node parent1)
	   {
		   this.x = x1;
		   this.y = y1;
		   this.g = g1;
		   this.parent = parent1;
	   }
	   
	   private final Node set(final double y1, final double g1, final Node parent1)
	   {
		   this.y = y1;
		   this.g = g1;
		   this.parent = parent1;
		   return this;
	   }
	   
   }
	
 	private double getHeuristic(final TerrainMap map, final Point p1, final Point pt2)
	{
    int x, y;
    double d;
    double dx = Math.abs(p1.x-pt2.x);
    double dy = Math.abs(p1.y-pt2.y);
    d = Math.max(dx, dy);
    double h1 = map.getTile(p1);
    double h2 = map.getTile(pt2);
    double h = h2 - h1;
    
    
 
      
   
   
    
    if (h >= 0 && d >= h )
      return  (d-h+h*Math.E);
    else if (h >=0 && d < h)
      return d *Math.exp(Math.floor(h/d));
    
    h = -h;
     if ( d -h > 0)
        return (d-h + h*Math.exp(-1));
    
     else return d*Math.exp(Math.floor(-h/d));
 

    //if (d > 1 )
    //return (d-1)/2 * Math.pow(h2/h1, 1/(d-1));
  
    //return 0;
  
 		
	}


	
	public List<Point> createPath(final TerrainMap map)
	{
		final Point end = map.getEndPoint();
		final ArrayList<Point> path = new ArrayList<Point>();
		Point CurrentPoint = new Point(map.getStartPoint());
		
		Node[][] m = new Node[map.getWidth()][map.getHeight()]; //initialize
		
		for (int i=0;i<map.getWidth();i++)
			for(int j=0;j<map.getHeight();j++)
				m[i][j] = new Node(new Point(i,j), Double.MAX_VALUE, Double.MAX_VALUE, null);
		
		////////////////////////// TEST PART
		
		System.out.println("distance: " + CurrentPoint.distance(map.getEndPoint()));
		System.out.println("Start Height: " + map.getTile(CurrentPoint) + " End Height: " + map.getTile(map.getEndPoint()));
		
		//////////////////////////
		
		//final Hashtable<Point, Double> cost = new Hashtable<Point, Double>(); trash code				
		
        final PriorityQueue<Node> frontier = new PriorityQueue<Node>
        (
        		new Comparator<Node>(){
                    public int compare (Node n1, Node n2){
                    	return n1.y > n2.y ? 1:-1;
                    }
        		}       		
        ); //frontier
        
        Node cu = m[CurrentPoint.x][CurrentPoint.y].set(getHeuristic(map, CurrentPoint,end), 0.0, null);
        frontier.add(cu);
        //cost.put(map.getStartPoint(), 0.0);
		double newCost; //temp variable
		
		while (!((cu=frontier.remove()) == null || cu.x.x == end.x && cu.x.y ==end.y))
			for (Point n : map.getNeighbors(cu.x))//for each neighbor =
				if ((newCost = (cu.g + map.getCost(cu.x, n))) < m[n.x][n.y].g || m[n.x][n.y].g == Double.MAX_VALUE ) // cost.containskey					
					frontier.add(m[n.x][n.y].set(newCost+getHeuristic(map, n, end), newCost, cu));

		path.add(cu.x);
		while ((cu = cu.parent) != null) 
			path.add(cu.x);
		
		Collections.reverse(path);//reverse from end to start

		return path;
	}
	

	
	
}