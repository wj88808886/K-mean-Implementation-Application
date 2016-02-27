import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Kmean {

	private ArrayList<Point> traindata;
	private int k;
	private Cluster[] kclusters;
	private int labels;
	private String filename;

	public Kmean(int k, String path, String filename, int labels) {
		this.k = k;
		this.labels = labels;
		readData(path);
		kclusters = new Cluster[k];
		this.filename = filename;
	}

	private void readData(String path) {
		Scanner sc;
		try {
			sc = new Scanner(new File(path));
			traindata = new ArrayList<Point>();
			sc.nextLine();
			while (sc.hasNext()) {
				String temp = sc.nextLine();
				String[] point = temp.split(",");
				ArrayList<Double> pos = new ArrayList<Double>();
				for (int i = 1; i < point.length - 1; i++) {
					pos.add(Double.parseDouble(point[i]));
				}
/*				Point p = (path.contains("wine")) ?  new Point(Integer.parseInt(point[0]), pos,
						point[point.length - 1].equals("Low")? 1 : 2) : new Point(Integer.parseInt(point[0].substring(1, point[0].length() - 1)), pos,
						Integer.parseInt(point[point.length - 1]));*/
				Point p = (path.contains("wine")) ?  new Point(Integer.parseInt(point[0]), pos,
						Integer.parseInt(point[point.length - 1]) - 2) : new Point(Integer.parseInt(point[0].substring(1, point[0].length() - 1)), pos,
						Integer.parseInt(point[point.length - 1]));
/*				Point p = (path.contains("p2")) ?  new Point(Integer.parseInt(point[0]), pos,
						Integer.parseInt(point[point.length - 1])) : new Point(Integer.parseInt(point[0].substring(1, point[0].length() - 1)), pos,
						Integer.parseInt(point[point.length - 1]));*/
				traindata.add(p);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void normalize() {
		double[] max = new double[traindata.get(0).dim()];
		double[] min = new double[traindata.get(0).dim()];
		Arrays.fill(min, Integer.MAX_VALUE);
		Arrays.fill(max, Integer.MIN_VALUE);

		for (int i = 0; i < traindata.size(); i++) {
			for (int j = 0; j < traindata.get(0).dim(); j++) {

				max[j] = Math.max(traindata.get(i).x().get(j), max[j]);
				min[j] = Math.min(traindata.get(i).x().get(j), min[j]);
			}
		}
		for (int i = 0; i < traindata.size(); i++) {
			for (int j = 0; j < traindata.get(0).dim(); j++) {
				traindata.get(i).x().set(j, (traindata.get(i).x().get(j) - min[j]) / (max[j] - min[j]));
			}
		}
	}

	private void truedata() {
		Cluster[] Tcluster = new Cluster[labels];
		for (int i = 0; i < labels; i++) {
			Tcluster[i] = new Cluster(traindata.get(0));
		}
		for (int j = 0; j < traindata.size(); j++) {
			Tcluster[traindata.get(j).y() - 1].append(traindata.get(j));
		}
		for (int i = 0; i < labels; i++) {
			Tcluster[i].update();
		}
		Statistics st = new Statistics(traindata);
		st.SSE(Tcluster);
		st.SSB(Tcluster);
		st.Silhouette(Tcluster);
	}

	private double distance(Point p1, Point p2) {
		double d = 0;
		for (int i = 0; i < p1.x().size(); i++) {
			d += Math.pow((p1.x().get(i) - p2.x().get(i)), 2);
		}
		return Math.sqrt(d);
	}

	private void kmean() {
		ArrayList<Point> data = new ArrayList<Point>(traindata);
		boolean flag = true;

		for (int i = 0; i < k; i++) {
			int index = (int) (Math.random() * (data.size() - i));
			Point temp = data.get(index);
			kclusters[i] = new Cluster(temp);
			data.set(index, data.get(data.size() - i - 1));
			data.set(data.size() - i - 1, temp);
		}

		do {
			flag = true;
			for (int i = 0; i < k; i++) {
				kclusters[i].removeAll();
			}
			for (int i = 0; i < traindata.size(); i++) {
				Point temp = traindata.get(i);
				double min = Double.MAX_VALUE;
				int index = -1;
				for (int j = 0; j < k; j++) {
					double current = distance(temp, kclusters[j].mean());
					if (min > current) {
						min = current;
						index = j;
					}
				}
				temp.setk(index);
			}

			for (int i = 0; i < traindata.size(); i++) {
				kclusters[traindata.get(i).k()].append(traindata.get(i));
			}
			for (int i = 0; i < k; i++) {
				kclusters[i].update();
				flag &= kclusters[i].change();
			}

		} while (!flag);

		for (int i = 0; i < k; i++) {
			// System.out.println("mean k"+(i+1)+" = "
			// +kclusters[i].mean().x());
		}
	}
	
	public void klabel(){
		for (int i = 0; i < kclusters.length; i++) {
			int[] votes = new int[labels];
			for (int j = 0; j < kclusters[i].size(); j++) {
				votes[kclusters[i].get(j).y()-1] ++;
			}
			int maxi = 0;
			int maxvote = votes[0];
			for (int j = 1; j < votes.length; j++) {
				if (votes[j] > maxvote){
					maxvote = votes[j];
					maxi = j;
				}
			}
			maxi++;
			kclusters[i].setLabel(maxi);
			for (int j = 0; j < kclusters[i].size(); j++) {
				kclusters[i].get(j).setPLable(maxi);
			}
		}
/*		for (int i = 0; i < kclusters.length; i++) {
			System.out.println("Cluster " + (i+1) + " = " + "label " + kclusters[i].label());
		}*/
	}

	public void confusion(){
		int[][] confusion = new int[labels+1][labels+1];
		for (int i = 1; i <= labels; i++){
			for (int j = 1; j <= labels; j++){
				for(int k = 0; k < traindata.size(); k++){
					if (traindata.get(k).y() == i && traindata.get(k).Plabel() == j) confusion[i][j] ++;
				}
				String s = (j != labels) ? confusion[i][j] + "," : confusion[i][j]+ "";
				System.out.print(s);
			}
			System.out.println();
		}
	}
	
/*	public void output() {
		try {
			PrintWriter w = new PrintWriter(filename, "UTF-8");
			w.println("id,Cluster,x,y");
			for (int i = 0; i < traindata.size(); i++) {
				w.println(traindata.get(i).id() + "," + (traindata.get(i).k() + 1)+"," +traindata.get(i).x().get(0)+"," +traindata.get(i).x().get(1));
			}
			w.close();;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

	public void output() {
		try {
			PrintWriter w = new PrintWriter(filename, "UTF-8");
			w.println("id,Cluster");
			for (int i = 0; i < traindata.size(); i++) {
				w.println(traindata.get(i).id() + "," + (traindata.get(i).k() + 1));
			}
			w.close();;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		normalize();
		System.out.println("True clustering");
		truedata();
		kmean();
		Statistics st = new Statistics(traindata);
		System.out.println("K mean clustering");
		st.SSE(kclusters);
		st.SSB(kclusters);
		st.Silhouette(kclusters);
		klabel();
		confusion();
		output();
	}

	public static void main(String[] args) {
		Kmean kmean = new Kmean(2, "A.easy.csv", "output1.csv", 2);
		kmean.run();
		Kmean kmean2 = new Kmean(4, "A.hard.csv", "output2.csv", 4);
		kmean2.run();
		Kmean kmean3 = new Kmean(3, "A.easy.csv", "output1-1.csv", 2);
		kmean3.run();
		Kmean kmean4 = new Kmean(3, "A.hard.csv", "output2-1.csv", 4);
		kmean4.run();
/*		Kmean kmean3 = new Kmean(2, "wine.csv","output3.csv",2);
		kmean3.run();
		Kmean kmean4 = new Kmean(4, "wine.csv","output4.csv",2);
		kmean4.run();
		Kmean kmean5 = new Kmean(6, "wine.csv","output5.csv",2);
		kmean5.run();
		Kmean kmean6 = new Kmean(8, "wine.csv","output6.csv",2);
		kmean6.run();
		Kmean kmean7 = new Kmean(10, "wine.csv","output7.csv",2);
		kmean7.run();*/
		Kmean kmean7 = new Kmean(10, "wine.csv","output3.csv",6);
		kmean7.run();
/*		Kmean kmean7 = new Kmean(2, "p22.csv","output2-1.csv",2);
		kmean7.run();*/
//		Kmean kmean7 = new Kmean(3, "p22.csv","output2-3.csv",3);
//		kmean7.run();
/*		Kmean kmean7 = new Kmean(6, "p25.arff","output2-5.csv",6);
		kmean7.run();*/
	}

}
