import java.util.*;

public class simplePolygon{
    
    private Random rand = new Random();
    
    //A priority queue to store the self-intersections:
    private PriorityQueue<Intersection> interQueue;
    
    //The polygon as an array of points:
    public Point[] polygon;

    //The Point data structure that stores coordinates as well as the index in the polygon array:
    class Point{
        public double x;
        public double y;
        //The index of the point in the array that represents the polygon:
        public int index;
        
        public Point(double xvalue, double yvalue, int indexValue){
            x = xvalue;
            y = yvalue;
            index = indexValue;
        }
    }
    
    //The Intersection data structure that stores the fours points that make the two edges that produce the intersection. The intersection can be compared using the delta attribute, which is the change in boundary length of the polygon if the intersection is resolved.
    class Intersection implements Comparable<Intersection>{
        public Point p1;
        public Point p2;
        public Point p3;
        public Point p4;
        
        public double delta;
        
        public Intersection(Point point1, Point point2, Point point3, Point point4){
            p1 = point1;
            p2 = point2;
            p3 = point3;
            p4 = point4;
            
            //Calculate the change in boundary length of the polygon if the intersection is resolved:
            delta = dist(p4, p2) + dist(p3, p1) - dist(p1, p2) - dist(p3, p4);
        }
        
        //Intersections are compared by their delta:
        public int compareTo(Intersection other)
        {
            return ((Double)delta).compareTo((Double)other.delta);
        }
    }

    //Calculate the distance between two points:
    public double dist(Point p1, Point p2){
        return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
    }
    
    //Print the points that make the polygon:
    public void printPoly(){
        for (Point p: polygon){
            System.out.print(p.x + ",");
            System.out.print(p.y + "\n");
        }
    }
    
    //Reverse a section of the array that represent the polygon in order to make sure that the points are in the correct order:
    public void reverse(int start, int end){
        while(end > start){
            Point temp = polygon[start];
            polygon[start] = polygon[end];
            polygon[end] = temp;
            polygon[start].index = start;
            polygon[end].index = end;
            start++;
            end--;
        }
    }
    
    //Check whether the two edges formed by p1, p2, p3, p4 intersect. If so, return the resulting intersection; if not, return null:
    public Intersection checkInter(Point p1, Point p2, Point p3, Point p4){
        
        double x1 = p1.x;
        double y1 = p1.y;
        double x2 = p2.x;
        double y2 = p2.y;
        double x3 = p3.x;
        double y3 = p3.y;
        double x4 = p4.x;
        double y4 = p4.y;
        
        //If any vertices points match:
        if((x1 == x3 && y1 == y3)||(x1 == x4 && y1 == y4)||(x2 == x3 && y2 == y3)||(x2 == x4 && y2 == y4)) return null;
        
        
        double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        
        //If the two edges are parallel:
        if (denom == 0.0) return null;
        
        double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3))/denom;
        double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3))/denom;
        
        //If the two edges intersect:
        if (ua >= 0.0f && ua <= 1.0f && ub >= 0.0f && ub <= 1.0f) {
            return new Intersection(p1, p2, p3, p4);
        }
        return null;
    }
    
    //The constructor that produces a simple polygon with specified size:
    public simplePolygon(int size){
        
        //Initialize the polygon as an array of Points:
        polygon = new Point[size];
        
        //Initialize the polygon with random points:
        for (int i = 0; i < size; i++){
            polygon[i] = new Point(rand.nextDouble(), rand.nextDouble(), i);
        }
        
        //Initialize the priority queue that holds the self-intersections:
        interQueue = new PriorityQueue<Intersection>();
        
        while(true){
            
            //Find all self-intersections and put them into the priority queue:
            for (int i = 0; i < size - 1; i++){
                for (int j = i + 2; j < size; j++){
                    Point p1 = polygon[i];
                    Point p2 = polygon[i+1];
                    Point p3 = polygon[j];
                    Point p4 = polygon[0];
                    if (j != size - 1){
                        p4 = polygon[j+1];
                    }
                    
                    Intersection inter = checkInter(p1, p2, p3, p4);
                    if (inter != null){
                        interQueue.add(inter);
                    }
                }
            }
            
            //Break loop and finish if there is no more self-intersections:
            if (interQueue.size() == 0) break;
            
            //A hashset that tracks the edges that are already removed:
            Set<Integer> removed = new HashSet<Integer>();
            
            while(interQueue.size() != 0){
                
                //Get the intersection that, if removed, will shorten the boundary of the polygon by the greatest amount:
                Intersection inter = interQueue.poll();
                Point p1 = inter.p1;
                Point p2 = inter.p2;
                Point p3 = inter.p3;
                Point p4 = inter.p4;
                
                //Make sure that the order of the points (the orientation of the edge) is correct:
                if(p2.index < p1.index && p2.index != 0){
                    Point temp = p1;
                    p1 = p2;
                    p2 = temp;
                }
                
                if (p4.index < p3.index && p4.index != 0){
                    Point temp = p3;
                    p3 = p4;
                    p4 = temp;
                }
                
                //If the indices of one edge differ by more than 1, the edge is no more valid:
                if (Math.abs(p1.index-p2.index)!=1 || Math.abs(p4.index-p3.index)!=1){
                    if (p1.index*p2.index*p3.index*p4.index!=0){
                        continue;
                    }
                }
                
                //If any of the edges is already removed, the intersection is not valid:
                if (removed.contains(p1.index) || removed.contains(p3.index)){
                    continue;
                }
                
                //Add the edges to the removed set:
                removed.add(p1.index);
                removed.add(p3.index);
                
                //Find the range of the array to reverse:
                int start;
                int end;
                if (p3.index > p1.index){
                    start = p2.index;
                    end = p3.index;
                }else{
                    start = p4.index;
                    end = p1.index;
                }
                
                //Reverse the array:
                reverse(start,end);
            }
        }
    }

    public static void main(String[] args){
        //Read in the size of the polygon:
        int size = Integer.parseInt(args[0]);
        //Create the polygon:
        simplePolygon sp = new simplePolygon(size);
        //Print out the polygon:
        sp.printPoly();
    }
}
