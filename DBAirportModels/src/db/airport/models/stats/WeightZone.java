package db.airport.models.stats;

/**
 * @author Morgan
 */
public class WeightZone {

	private double meanWeight;
	private double totalWeight;
	private String zone;
	private String gender;

	/**
	 *
	 * @param meanWeight
	 * @param zone
	 */
	public WeightZone(double meanWeight, String zone) {
		this.meanWeight = meanWeight;
		this.zone = zone;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public double getMeanWeight() {
		return meanWeight;
	}

	public void setMeanWeight(double meanWeight) {
		this.meanWeight = meanWeight;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(double totalWeight) {
		this.totalWeight = totalWeight;
	}

}
