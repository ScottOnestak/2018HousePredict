import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Polls {
	
	String name;
	public Map<String,Poll> thepolls = new HashMap<String,Poll>();
	double max_poll,min_poll,max_days,min_days,gop,dem,gap;
	
	//constructor
	public Polls(String name) {
		this.name = name;
		max_poll = Double.MIN_VALUE;
		min_poll = Double.MAX_VALUE;
		max_days = Double.MIN_VALUE;
		min_days = Double.MAX_VALUE;
	}
	
	//insert method
	public void insert(String pollster, Double pollster_rating, Double gop, Double dem, Date date, long days) {
		
		if(!thepolls.containsKey(pollster)) {
			thepolls.put(pollster, new Poll(pollster,pollster_rating,gop,dem,date,days));
		} else {
			if(thepolls.get(pollster).getDays() > days) {
				thepolls.put(pollster, new Poll(pollster,pollster_rating,gop,dem,date,days));
			}
		}
		
		
		if(pollster_rating > max_poll) {
			max_poll = pollster_rating;
		}
		
		if(pollster_rating < min_poll) {
			min_poll = pollster_rating;
		}
		
		if((double) days > max_days) {
			max_days = (double) days;
		}
		
		if((double) days < min_days) {
			min_days = (double) days;
		}
	}
	
	//calculate weighted poll
	public void getPoll() {
		double sumofweights = 0;
		double sumofweightedgop = 0;
		double sumofweighteddem = 0;
		double weight = 0;
		
		//System.out.println(name + ": " + max_poll + "," + min_poll + "," + max_days + "," + min_days + "\n");
		
		for(Map.Entry<String, Poll> entry: thepolls.entrySet()) {
			weight = entry.getValue().calcWeight(max_poll, min_poll, max_days, min_days);
			
			sumofweights += weight;
			sumofweightedgop += weight * entry.getValue().getGOP();
			sumofweighteddem += weight * entry.getValue().getDem();
		}
		
		gop = sumofweightedgop/sumofweights;
		dem = sumofweighteddem/sumofweights;
		gap = gop - dem;
	}

	//getters
	public double getGOP() {
		return gop;
	}
	
	public double getDem() {
		return dem;
	}
	
	public double getGap() {
		return gap;
	}
}
