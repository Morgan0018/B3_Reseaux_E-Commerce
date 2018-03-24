package db.airport.models.stats;

/**
 * @author Morgan
 */
public class WeightDistance {

	private double meanWeight;
	private int distance;
	private int age;
	private int nbAcc;

	/**
	 * Constructor.
	 * 
	 * @param meanWeight
	 * @param distance
	 */
	public WeightDistance(double meanWeight, int distance) {
		this.meanWeight = meanWeight;
		this.distance = distance;
	}

	public double getMeanWeight() {
		return meanWeight;
	}

	public void setMeanWeight(double meanWeight) {
		this.meanWeight = meanWeight;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getNbAcc() {
		return nbAcc;
	}

	public void setNbAcc(int nbAcc) {
		this.nbAcc = nbAcc;
	}

}
