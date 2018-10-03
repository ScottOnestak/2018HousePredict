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
		double pollster_rate = (pollster_rating-min_poll)/(max_poll - min_poll);
		double dayrate = (max_days-days)/(max_days-min_days);
		
		weight = .6*Math.asin(pollster_rate)+.4*Math.asin(dayrate);
		//System.out.println(pollster+","+pollster_rate+","+dayrate+","+weight);
		return weight;
	}
}
