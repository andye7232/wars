package Statistical;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.ml.clustering.*;

import edu.avystats.DAO.MeansVar;


public class KMeans {

	public static class PointWrapper implements Clusterable{
		private double[] points;
		private MeansVar meansvar;
		
		
		public PointWrapper(MeansVar mv){
			this.meansvar=mv;
			this.points=new double[]{mv.getMv1(),mv.getMv2()};
		}
		
		@Override
		public double[] getPoint() {
			// TODO Auto-generated method stub
			return points;
		}
		
		public  MeansVar getMeansVar(){
			return meansvar;
		}
	}
	
	public KMeans(){
		
	}
	
	
	
	public ArrayList<ArrayList<MeansVar>> calculate(List<MeansVar> vals){
		List<PointWrapper> clusterInput= new ArrayList<PointWrapper>(vals.size());
		ArrayList<ArrayList<MeansVar>> overlist=new ArrayList<ArrayList<MeansVar>>();
		ArrayList<MeansVar> outlist=new ArrayList<MeansVar>();
		
		for(MeansVar var: vals){
			clusterInput.add(new PointWrapper(var));
		}
		
		KMeansPlusPlusClusterer<PointWrapper> clusterer = new KMeansPlusPlusClusterer<PointWrapper>(10,1000);
		List<CentroidCluster<PointWrapper>> clusterResults = clusterer.cluster(clusterInput);
		// output the clusters
		for (int i=0; i<clusterResults.size(); i++) {
		    for (PointWrapper pointWrapper : clusterResults.get(i).getPoints()){
		        outlist.add(pointWrapper.getMeansVar());
			}
		    overlist.add(outlist);
		    outlist=new ArrayList<MeansVar>();
		}
		
		
		return overlist;
	}
}
