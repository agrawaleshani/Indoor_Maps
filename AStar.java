import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;


/**
 * A Star - Heuristic Search Algorithm for finding the shortest path
 * Worst Case => O(|E|)
 * @author hardik alakh eshani  geetika
 */
public class AStar {

    private PriorityQueue<Node> openList;
    private ArrayList<Node> closedList;
    HashMap<Node, Integer> gVals;
    HashMap<Node, Integer> fVals;
    private int initialCapacity = 100;
    private int distanceBetweenNodes = 1;

    public AStar() {
        gVals = new HashMap<Node, Integer>();
        fVals = new HashMap<Node, Integer>();
        openList = new PriorityQueue<Node>(initialCapacity, new fCompare());
        closedList = new ArrayList<Node>();
    }

    public static void main(String[] args) throws Exception,IOException {

        /*
         * X = Walls
         * N1 => Start
         * N8 => Goal
         *
        N0 - N3 - N5 - N8
        |         |
        N1   X    N6    X
        |         |
        N2 - N4 - N7 - N9
         */




        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\hp\\Documents\\MATLAB\\final.txt"));
        BufferedReader fr=new BufferedReader(new InputStreamReader(System.in));
        BufferedReader fr2=new BufferedReader(new FileReader("C:\\Users\\hp\\Documents\\MATLAB\\Names.txt"));
        int j=0;

        String dimension[]=br.readLine().split(" ");
        int row=Integer.parseInt(dimension[0]);
        int col=Integer.parseInt(dimension[1]);
         Node arr[][]=new Node[row][col];//image matrix
        LinkedList<Node>  nodes=new LinkedList<>();//contains all the nodes +the doors
        HashMap<String,Node> hm=new HashMap<>();//contains the door names
        String v=br.readLine();
        while(v!=null){
            String line[]=v.trim().split("\\s+");
          for(int i=0;i<line.length;i++)
          {
              if(line[i].equals(""))
              {
                  continue;
              }
              int x=Integer.parseInt(line[i]);
              if(x==0)
              {
                  continue;
              }

              if(x==127)
              {   //repersents a door
                  Node p=new Node(i,j);
                  arr[j][i]=p;
                  String read=fr2.readLine();
                  if(read!=null){
                      p.setData(read);
                  }else
                  {
                      throw new Exception("THE NAMES ARE LESS");
                  }
                 nodes.add(p);//i repersent the value in the x direction
                  //and j repersent the value in the y direction
                 hm.put(p.getData(),p);
              }else if(1<=x &&x<=2)
              {//repersents a node
                  Node p=new Node(i,j);
                  arr[j][i]=p;
                  nodes.add(p);
              }
          }
           v=br.readLine();
            j++;
        }





        //neighbours
        for(Node x:nodes) {

            int xx = x.getX();
            int yy = x.getY();

            if(isValid(row,col,xx-1,yy))
            {  //left
                if(arr[yy][xx-1]!=null)
                {
                    x.addNeighbor(arr[yy][xx-1]);
            }
            }

            if(isValid(row,col,xx+1,yy))
            {  //right
                Node p=arr[yy][xx+1];
                if(p!=null)
                {
                    x.addNeighbor(p);
                }
            }
            if(isValid(row,col,xx,yy-1))
            {  //top
                Node p=arr[yy-1][xx];
                if(p!=null)
                {
                    x.addNeighbor(p);
                }
            }
            if(isValid(row,col,xx,yy+1))
            {  //bottom
                Node p=arr[yy+1][xx];
                if(p!=null)
                {
                    x.addNeighbor(p);
                }
            }
        }
        System.out.println();


       

        System.out.println("enter start and end");
        String Start=fr.readLine();
        String end=fr.readLine();

    new AStar().traverse(hm.get(Start),hm.get(end));
    br.close();
    fr.close();
    fr2.close();
    }

    private static boolean isValid(int row,int col,int i,int j)
    {
        if(0<=i&&i<col&&0<=j&&j<row)
        {
            return true;
        }
        return false;
    }
    public void traverse(Node start, Node end) throws FileNotFoundException{
        openList.clear();
        closedList.clear();

        gVals.put(start, 0);
        openList.add(start);

        while (!openList.isEmpty()) {
            Node current = openList.element();
            if (current.equals(end)) {
                System.out.println("Goal Reached");
                printPath(current);
                return;
            }
            closedList.add(openList.poll());
            ArrayList<Node> neighbors = current.getNeighbors();

            for (Node neighbor : neighbors) {
                int gScore = gVals.get(current) + distanceBetweenNodes;
                int fScore = gScore + h(neighbor, current);

                if (closedList.contains(neighbor)) {

                    if (gVals.get(neighbor) == null) {
                        gVals.put(neighbor, gScore);
                    }
                    if (fVals.get(neighbor) == null) {
                        fVals.put(neighbor, fScore);
                    }

                    if (fScore >= fVals.get(neighbor)) {
                        continue;
                    }
                }
                if (!openList.contains(neighbor) || fScore < fVals.get(neighbor)) {
                    neighbor.setParent(current);
                    gVals.put(neighbor, gScore);
                    fVals.put(neighbor, fScore);
                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);

                    }
                }
            }
        }
        System.out.println("FAIL");
    }

    private int h(Node node, Node goal) {
        int x = node.getX() - goal.getX();
        int y = node.getY() - goal.getY();
        return x * x + y * y;
    }

    private void printPath(Node node) throws FileNotFoundException {
         PrintWriter pw=new PrintWriter("C:\\Users\\hp\\Documents\\MATLAB\\result.txt");
        System.out.println(node);
        pw.write(node.toString()+"\n");
        ArrayList<Integer> x=new ArrayList<>();
        x.add(node.getX());
        ArrayList<Integer> y=new ArrayList<>();
        y.add(node.getY());
        while (node.getParent() != null) {
            node = node.getParent();
            System.out.println(node);
            pw.write(node.toString()+"\n");
            x.add(node.getX());
            y.add(node.getY());
        }
        plot(x,y);
        pw.close();
    }
    public static void plot(ArrayList<Integer> x, ArrayList<Integer> y)
    {
        int[] xd =new int[x.size()];
        int[] yd =new int[y.size()];
        for(int i=0;i<x.size();i++)
        {
            xd[i]=x.get(i);
            yd[i]=y.get(i);
        }
      Tutorial t=new Tutorial(xd,yd);
    }
    

    class fCompare implements Comparator<Node> {

        public int compare(Node o1, Node o2) {
            if (fVals.get(o1) < fVals.get(o2)) {
                return -1;
            } else if (fVals.get(o1) > fVals.get(o2)) {
                return 1;
            } else {
                return 1;
            }
        }
    }
}

class Node {

    private Node parent;
    private ArrayList<Node> neighbors;
    private int x;
    private int y;
    private String  data;

    public Node() {
        neighbors = new ArrayList<Node>();
        data = "";
    }

    public Node(int x, int y) {
        this();
        this.x = x;
        this.y = y;
    }

    public Node(Node parent) {
        this();
        this.parent = parent;
    }

    public Node(Node parent, int x, int y) {
        this();
        this.parent = parent;
        this.x = x;
        this.y = y;
    }

    public ArrayList<Node> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(ArrayList<Node> neighbors) {
        this.neighbors = neighbors;
    }

    public void addNeighbor(Node node) {
        this.neighbors.add(node);
    }

    public void addNeighbors(ArrayList<Node> nodes) {
        this.neighbors.addAll(nodes);
    }



    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean equals(Node n) {
        return this.x == n.x && this.y == n.y;
    }

    @Override
    public String toString() {

        if(!data.equals(""))
        {
          return data;
        }else
        {
            return "Node=" +getY()+" "+getX();
        }
    }
}
 class Tutorial extends JFrame {
    int  x[];
    int  y[];
    public Tutorial() {
        setTitle("tut");
        setSize(5010, 673);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

     public Tutorial(int [] x, int [] y){
            this();
         this.x = x;
         this.y = y;
     }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.BLACK);
        Stroke stroke = new BasicStroke(2f);
        g2d.setStroke(stroke);
//        g.drawLine(0,0,1000,1000);
        for(int i=0;i<x.length-5;i+=5)
        {
            g2d.drawLine(x[i],y[i],x[i+1],y[i+1]);
        }
    }

    public static void main(String[] args) {
//        int x[]={100,200,300,400,500};
//        int y[]={100,200,300,400,500};
//        Tutorial t=new Tutorial(x,y);



    }
}
