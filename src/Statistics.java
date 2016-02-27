import java.util.ArrayList;
import java.util.List;

public class Statistics {

	private List<Point> traindata;

	public Statistics(List<Point> traindata) {
		this.traindata = new ArrayList<Point>(traindata);
	}

	private double distance(Point p1, Point p2) {
		double d = 0;
		for (int i = 0; i < p1.x().size(); i++) {
			d += Math.pow((p1.x().get(i) - p2.x().get(i)), 2);
		}
		return Math.sqrt(d);
	}

	public Point center(List<Point> traindata) {
		ArrayList<Double> a = new ArrayList<Double>();
		for (int j = 0; j < traindata.get(0).x().size(); j++) {
			a.add(0.0);
		}
		for (int i = 0; i < traindata.size(); i++) {
			for (int j = 0; j < a.size(); j++) {
				a.set(j, a.get(j) + traindata.get(i).x().get(j));
			}
		}
		for (int j = 0; j < a.size(); j++) {
			a.set(j, a.get(j) / traindata.size());
		}
		return new Point(a);
	}

	public double SSE(Cluster[] kclusters) {
		double sse = 0;
		for (int i = 0; i < kclusters.length; i++) {
			double sum = 0;
			for (int j = 0; j < kclusters[i].size(); j++) {
				sum += Math.pow(distance(kclusters[i].mean(), kclusters[i].get(j)), 2);
			}
			System.out.println("Cluster " + (i + 1) + " SSE," + sum);
			sse += sum;
		}
		System.out.println("Total k cluster SSE," + sse);
		return sse;
	}

	public double SSB(Cluster[] kclusters) {
		double ssb = 0;
		for (int i = 0; i < kclusters.length; i++) {
			double sum = 0;
			sum += Math.pow(distance(kclusters[i].mean(), center(traindata)), 2) * kclusters[i].size();
			ssb += sum;
		}
		System.out.println("Total SSB," + ssb);
		return ssb;
	}

	public double Silhouette(Cluster[] kclusters) {
		double silhouette = 0;
		for (int i = 0; i < kclusters.length; i++) {
			double silhouettek = 0;
			for (int m = 0; m < kclusters[i].size(); m++) {
				double s = 0;
				double a = 0;
				double[] blist = new double[kclusters.length];
				double b = Integer.MAX_VALUE;
				for (int j = 0; j < kclusters.length; j++) {
					if (i == j) {
						for (int l = 0; l < kclusters[j].size(); l++) {
							a += distance(kclusters[i].get(m), kclusters[j].get(l));
						}
					} else {
						for (int l = 0; l < kclusters[j].size(); l++) {
							blist[j] += distance(kclusters[i].get(m), kclusters[j].get(l));
						}
					}
				}
				a /= (kclusters[i].size() - 1);
				for (int j = 0; j < blist.length; j++) {
					if (j != i) {
						blist[j] /= kclusters[j].size();
						b = Math.min(b, blist[j]);
					}
				}				
				s = (b - a) / Math.max(a, b);
				silhouettek += s;
			}
			silhouettek /= kclusters[i].size();
			System.out.println("Silhouette in " + (i+1) + " cluster," + silhouettek);
			silhouette += silhouettek;
		}
		silhouette /= kclusters.length;
		System.out.println("Total Silhouette," + silhouette);
		return silhouette;
	}
}
