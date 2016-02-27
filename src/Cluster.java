import java.util.ArrayList;
import java.util.Collections;


public class Cluster {

	private ArrayList<Point> points = new ArrayList<Point>();
	private Point mean;
	private boolean change;
	private int label;
	
	public Cluster(Point mean){
		this.mean = mean;
	}
	
	public void append(Point d){
		points.add(d);
	}
	
	public void removeAll(){
		points.clear();
	}
	
	public ArrayList<Point> points(){
		return points;
	}
	
	public void setmean(Point mean){
		this.mean = mean;
	}
	
	public void setLabel(int label){
		this.label = label;
	}
	
	public int label(){
		return label;
	}
	
	public Point mean(){
		return mean;
	}
	
	public int size(){
		return points.size();
	}
		
	public boolean change(){
		return change;
	}
	
	public Point get(int i){
		return points.get(i);
	}
	
	public void update(){
		ArrayList<Double> a = new ArrayList<Double>();
		for (int j = 0; j < mean.x().size(); j++){
			a.add(0.0);
		}
		for (int i = 0; i < points.size(); i++) {
			for (int j = 0; j < a.size(); j++){
				a.set(j, a.get(j)+points.get(i).x().get(j));
			}
		}
		for (int j = 0; j < a.size(); j++){
			a.set(j, a.get(j)/points.size());
		}
		double diff = 0;
		for (int j = 0; j < a.size(); j++){
			diff += Math.abs(mean.x().get(j) - a.get(j)); 
		}
		this.change = (diff <= 0.000000001)? true : false;
		this.mean = new Point(a);
	}
}
