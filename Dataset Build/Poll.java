import java.util.Date;

public class Poll {
	
	String pollster;
	Double pollster_rating,gop,dem,days,weight;
	Date date;

	//constructor
	public Poll(String pollster, Double pollster_rating, Double gop, Double dem, Date date, long days) {
		this.pollster = pollster;
		this.pollster_rating = pollster_rating;
		this.gop = gop;
		this.dem = dem;
		this.date = date;
		this.days = (double) days;
	}
	
	//getters
	public double getGOP() {
		return gop;
	}
	
	public double getDem() {
		return dem;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public double getDays() {
		return days;
	}
	
	//calculate weight method
	public double calcWeight(double max_poll, double min_poll, double max_days, double min_days) {
		double pollster = (2*(pollster_rating-min_poll))/(max_poll - min_poll)-1;
		double dayrate = ((2*(days-min_days))/(max_days-min_days)-1)*-1;
		
		weight = .6*Math.asin(pollster)+.4*Math.asin(dayrate);
		//System.out.println(pollster+","+dayrate+","+weight);
		return weight;
	}
}
