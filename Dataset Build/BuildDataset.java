import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class BuildDataset {
	
	public static Map<String,CDs> CDs = new TreeMap<String,CDs>();
	public static Map<String,Cluster> clusters = new HashMap<String,Cluster>();

	public static void main(String[] args) throws IOException {
		
		String theCDs = "C:/Users/onest/Desktop/2018 House Model/CDs.csv";
		String theline,State,CDNum,CD_Name,MedianAge,Male,White,Black,Hispanic,ForeignBorn,Married,HSGrad,BachGrad,
				MedianIncome,Poverty,MedianEarningsHS,MedianEarningsBach,MedEarnDiff,Urbanicity,LFPR,Religiosity,
				Evangelical,Catholic,Veteran,Cluster;
		String holder[];
		int theLength,firstyear;
		
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
						for(int i = 2006; i <= 2010; i=i+2){
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
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("CDsDataset.csv")));
		
		for(Map.Entry<String,CDs> entry: CDs.entrySet()){
			bw.write(entry.getValue().toString());
		}

	}

}
