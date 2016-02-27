import java.util.ArrayList;

public class Point {
	private int id;
	private ArrayList<Double> x;
	private int k;
	private int y;
	private int Plabel;
	
	public Point(int id, ArrayList<Double> x, int y){
		this.id = id;
		this.x = new ArrayList<Double>(x);
		this.y = y;
	}
	
	public Point(ArrayList<Double> x){
		this.x = new ArrayList<Double>(x);
	}
	
	public void setk(int k){
		this.k = k;
	}
	
	public int k(){
		return k;
	}
	
	public int dim(){
		return x.size();
	}
	
	public ArrayList<Double> x(){
		return x;
	}
	
	public int id(){
		return id;
	}
	
	public int y(){
		return y;
	}
	
	public void setPLable(int l){
		this.Plabel = l;
	}
	
	public int Plabel(){
		return Plabel;
	}
	
	public boolean successPredict(){
		return Plabel == y;
	}
}
