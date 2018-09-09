import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class BuildDataset {
	
	public static Map<String,CDs> CDs = new TreeMap<String,CDs>();
	public static Map<String,Cluster> clusters = new HashMap<String,Cluster>();

	public static void main(String[] args) throws IOException {
		
		String theCDs = "C:/Users/onest/Desktop/2018 House Model/CDs.csv";
		//CDs values
		String theline,State,CDNum,CD_Name,MedianAge,Male,White,Black,Hispanic,ForeignBorn,Married,HSGrad,BachGrad,
				MedianIncome,Poverty,MedianEarningsHS,MedianEarningsBach,MedEarnDiff,Urbanicity,LFPR,Religiosity,
				Evangelical,Catholic,Veteran,Cluster;
		//fec results variables
		String fec_file,state,cd,fecid,incumbency = null,can_name = null,party = null,gen_prct = null,gen_runoff = null,gen_comb = null;
		String cdlookup = null;
		double result = 0;
		int incum = 0;
		boolean insert = false;
		
		String holder[];
		int firstyear;
		
		//create try...catch for bufferedreader
		try{
			BufferedReader br = new BufferedReader(new FileReader(theCDs));
			
			//skip the first line
			theline = br.readLine();
			theline = br.readLine();
			
			//while not null... insert data
			while(theline != null){
				
				holder = theline.split(",");
				
				State = holder[0].trim().replace("\"", "");
				CDNum = holder[1];
				CD_Name = holder[2].trim().replace("\"", "");
				MedianAge = holder[5];
				Male = holder[6];
				White = holder[7];
				Black = holder[8];
				Hispanic = holder[9];
				ForeignBorn = holder[10];
				Married = holder[12];
				HSGrad = holder[14];
				BachGrad = holder[18];
				MedianIncome = holder[19];
				Poverty = holder[20];
				MedianEarningsHS = holder[21];
				MedianEarningsBach = holder[22];
				MedEarnDiff = holder[23];
				Urbanicity = holder[24];
				LFPR = holder[25];
				Religiosity = holder[26];
				Evangelical = holder[27];
				Catholic = holder[28];
				Veteran = holder[30];
				Cluster = holder[31];
				
				//old CDs (2006-2010) or redistricted ones
				if(CD_Name.length() > 4){
					if(CD_Name.charAt(CD_Name.length()-1) =='o'){
						for(int i = 2008; i <= 2010; i=i+2){
							String name = CD_Name + "_" + i;
							CDs.put(name, new CDs(name,State,CDNum,CD_Name,i,MedianAge,Male,White,Black,
									Hispanic,ForeignBorn,Married,HSGrad,BachGrad,MedianIncome,Poverty,
									MedianEarningsHS,MedianEarningsBach,MedEarnDiff,Urbanicity,LFPR,
									Religiosity,Evangelical,Catholic,Veteran,Cluster));
						}
					} else if(State.equals("PA")) {
						for(int i = 2012; i <=2016; i=i+2){
							String name = CD_Name + "_" + i;
							CDs.put(name, new CDs(name,State,CDNum,CD_Name,i,MedianAge,Male,White,Black,
									Hispanic,ForeignBorn,Married,HSGrad,BachGrad,MedianIncome,Poverty,
									MedianEarningsHS,MedianEarningsBach,MedEarnDiff,Urbanicity,LFPR,
									Religiosity,Evangelical,Catholic,Veteran,Cluster));
						}
					} else if(State.equals("FL") | State.equals("NC")){
						for(int i = 2012; i <=2014; i=i+2){
							String name = CD_Name + "_" + i;
							CDs.put(name, new CDs(name,State,CDNum,CD_Name,i,MedianAge,Male,White,Black,
									Hispanic,ForeignBorn,Married,HSGrad,BachGrad,MedianIncome,Poverty,
									MedianEarningsHS,MedianEarningsBach,MedEarnDiff,Urbanicity,LFPR,
									Religiosity,Evangelical,Catholic,Veteran,Cluster));
						}
					} else {
						System.out.println("Error: " + theline);
					}
				} else {
					//initialize the start year...starts later for redistricted CDs
					firstyear = 2012;
					if(State.equals("PA")){
						firstyear = 2018;
					} else if(State.equals("NC") | State.equals("FL")){
						firstyear = 2016;
					}
					
					for(int i = firstyear; i <=2018; i=i+2){
						String name = CD_Name + "_" + i;
						CDs.put(name, new CDs(name,State,CDNum,CD_Name,i,MedianAge,Male,White,Black,
								Hispanic,ForeignBorn,Married,HSGrad,BachGrad,MedianIncome,Poverty,
								MedianEarningsHS,MedianEarningsBach,MedEarnDiff,Urbanicity,LFPR,
								Religiosity,Evangelical,Catholic,Veteran,Cluster));
					}
				}
				
				theline = br.readLine();
			}
			
			br.close();
		} catch(FileNotFoundException e){
			System.out.println("File not found: " + args[0]);
		} catch(IOException e) {
			e.printStackTrace();
		} 
		
		for(int i = 2014; i <= 2014; i = i+2) {
			//set fec file name to read in
			fec_file = "C:/Users/onest/Desktop/2018 House Model/election data files/house_results_" + i + ".csv";
			
			//read in election results for 2008
			try {
				BufferedReader br = new BufferedReader(new FileReader(fec_file));
				
				//skip the first line
				theline = br.readLine();
				theline = br.readLine();
				
				//while not null... insert data
				while(theline != null){
					//set blank to start
					state = "";
					cd = "";
					fecid = "";
					incumbency = "";
					can_name = "";
					party = "";
					gen_prct = "";
					gen_runoff = "";
					gen_comb = "";
					result = 0;
					insert = false;
					
					holder = theline.split(",");
					if(holder.length >= 3) {
						if(i <= 2010) {
							state = holder[2].trim().replace("\"", "");
						} else {
							state = holder[1].trim().replace("\"", "");
						}
					}
					
					if(holder.length >= 4) {
						cd = holder[3].trim().replace("\"", "");
					}
					
					if(holder.length >= 5) {
						fecid = holder[4].trim().replace("\"", "");
					}
					
					if(holder.length >= 6) {
						incumbency = holder[5].trim().replace("\"", "");
					}
					
					
					if(holder.length >= 9) {
						can_name = holder[8].trim().replace("\"", "");	
					}
					
					
					if(holder.length >= 11) {
						party = holder[10].trim().replace("\"", "");
					}
					
					
					if(holder.length >= 17) {
						gen_prct = holder[16].trim().replace("\"", "");
					}
					
					
					if(holder.length >= 19) {
						if(i != 2010) {
							gen_runoff = holder[18].trim().replace("\"", "");
						} else {
							gen_comb = holder[18].trim().replace("\"", "");
						}
						
					}
					
					if(holder.length >= 21) {
						gen_comb = holder[20].trim().replace("\"", "");
					}
					
					//only take Republican or Democrat results...avoids double counting for LA,CT,NY,SC
					if((party.equals("R") | party.equals("REP") | party.equals("D") | party.equals("DEM")) & !fecid.equals("n/a") &
							!(cd.equals("H") | cd.equals("S") | cd.length() > 2 | cd.equals(""))) {
						//set incumbency
						if(!incumbency.equals("")) {
							incum = 1;
						} else {
							incum = 0;
						}
						
						//get resulting percents
						if(gen_runoff.length()>1) {
							result = Double.parseDouble(gen_runoff.substring(0, gen_runoff.length()-1));
							insert = true;
						} else if(gen_comb.length()>1) {
							result = Double.parseDouble(gen_comb.substring(0, gen_comb.length()-1));
							insert = true;
						} else {
							//if not blank
							if(gen_prct.length()>1) {
								//if unopposed... 100%
								if(gen_prct.equals("n/a")) {
									result = 100;
								} else {
									result = Double.parseDouble(gen_prct.substring(0, gen_prct.length()-1));
								}
								insert = true;
							}
						}
						
						//if candidate was in the general and should be inserted
						if(insert == true) {
							//construct lookup
							if(cd.equals("0")) {
								cd = "1";
							}
							if(cd.length() < 2) {
								cd = "0" + cd;
							}
							
							if(i <= 2010) {
								cdlookup = state + cd + "o_" + i;
							} else if(state.equals("PA") | state.equals("NC") | state.equals("FL")) {
								if(state.equals("PA")) {
									if(i < 2018) {
										cdlookup = state + cd + "r_" + i;
									} else {
										cdlookup = state + cd + "_" + i;
									}
								} else if (state.equals("NC") | state.equals("FL")) {
									if(i < 2016) {
										cdlookup = state + cd + "r_" + i;
									} else {
										cdlookup = state + cd + "_" + i;
									}
								}
							} else {
								cdlookup = state + cd + "_" + i;
							}
							
							//insert the values
							if(CDs.containsKey(cdlookup)) {
								CDs.get(cdlookup).insertResults(fecid, incum, can_name, party, result);
								System.out.println(Arrays.toString(holder));
								System.out.println(fecid + "," + incum + "," + can_name + "," + party + "," + result);
							}
						}
						
					}
					
					theline = br.readLine();
				}	
				
				br.close();
			} catch(FileNotFoundException e){
				System.out.println("File not found: " + args[0]);
			} catch(IOException e) {
				e.printStackTrace();
			} 
		}
		
		
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("CDsDataset.csv")));
		
		int i = 0;
		for(Map.Entry<String,CDs> entry: CDs.entrySet()){
			bw.write(entry.getValue().toString());
			//System.out.println(i + ": " + entry.getValue().toString());
			i++;
		}
		
		bw.close();
		System.out.println(i);
	}

}
