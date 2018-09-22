import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class BuildDataset {
	
	public static Map<String,CDs> CDs = new TreeMap<String,CDs>();
	public static Map<String,Cluster> clusters = new HashMap<String,Cluster>();
	public static LinkedList<String> fecids2008 = new LinkedList<String>();
	public static LinkedList<String> fecids2010 = new LinkedList<String>();
	public static LinkedList<String> fecids2012 = new LinkedList<String>();
	public static LinkedList<String> fecids2014 = new LinkedList<String>();
	public static LinkedList<String> fecids2016 = new LinkedList<String>();
	public static LinkedList<String> fecids2018 = new LinkedList<String>();

	public static void main(String[] args) throws IOException {
		
		String theCDs = "C:/Users/onest/Desktop/2018 House Model/CDs.csv";
		//CDs values
		String theline,State,CDNum,CD_Name,MedianAge,Male,White,Black,Hispanic,ForeignBorn,Married,HSGrad,BachGrad,
				MedianIncome,Poverty,MedianEarningsHS,MedianEarningsBach,MedEarnDiff,Urbanicity,LFPR,Religiosity,
				Evangelical,Catholic,Veteran,Cluster;
		//fec results variables
		String fec_file,state,cd,fecid,incumbency = null,can_name1,can_name2,can_name=null,party = null,gen_prct = null,gen_runoff = null,gen_comb = null,gen_count=null;
		String cdlookup = null;
		double result = 0;
		int incum = 0,type = 0;
		boolean insert = false;
		String TotRec,TotDisb,COHCOP,COHBOP,DebtOBC,IndItemCont,IndUnitemCont,IndCont,OthCommContr,PartyCommContr,TotCont,
			TransFOAC,TotLoan,OfftoOpExpend,OthReceipts,OpExpend,TransTOAC,TotLoanRepay,TotContrRef,OthDisb,NetContr,NetOpExp;
		double Total_Receipts,Total_Disbursment,COH_Ending,COH_Beginning,Debt_Owed_By_Committee,Individual_Itemized_Contribution,
			Individual_Unitemized_Contribution,Individual_Contribution,Other_Committee_Contribution,Party_Committee_Contribution,
			Total_Contribution,Transfer_From_Other_Authorized_Committee,Total_Loan,Offset_To_Operating_Expenditure,Other_Receipts,
			Operating_Expenditure,Transfer_To_Other_Authorized_Committee,Total_Loan_Repayment,Total_Contribution_Refund,
			Other_Disbursments,Net_Contribution,Net_Operating_Expenditure;
			
		
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
		
		//read in election results and fec id numbers for 2008-2016 elections
		for(int i = 2008; i <= 2016; i = i+2) {
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
					can_name1 = "";
					can_name2 = "";
					can_name = "";
					party = "";
					gen_prct = "";
					gen_count = "";
					gen_runoff = "";
					gen_comb = "";
					result = 0;
					insert = false;
					type = 0;
					
					holder = theline.split(",");
					if(holder.length >= 3) {
						if(i <= 2010) {
							state = holder[2].trim().replace("\"", "").replace(" ", "");
						} else {
							state = holder[1].trim().replace("\"", "").replace(" ", "");
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
					
					
					if(holder.length >= 8) {
						can_name1 = holder[6].trim().replace("\"", "");	
						can_name2 = holder[7].trim().replace("\"", "");
						can_name = can_name1 + " " + can_name2;
					}
					
					
					if(holder.length >= 11) {
						party = holder[10].trim().replace("\"", "").replace(" ", "");
					}
					
					if(holder.length >= 16) {
						gen_count = holder[15].trim().replace("\"", "").replaceAll(" ", "");
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
					if((party.equals("R") | party.equals("REP") | party.equals("D") | party.equals("DEM") | party.equals("DFL") | party.equals("DNL"))
							& !fecid.equals("n/a") 
							& !(cd.equals("H") | cd.equals("S") | cd.length() > 2 | cd.equals(""))) {
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
							type = 3;
						} else if(gen_comb.length()>1) {
							result = Double.parseDouble(gen_comb.substring(0, gen_comb.length()-1));
							insert = true;
							type = 2;
						} else {
							//if not blank
							if(gen_prct.length()>1 | gen_count.length() > 1) {
								//if unopposed... 100%
								if(gen_prct.equals("n/a") | gen_count.equals("Unopposed")) {
									result = 100;
								} else {
									//System.out.println(state + " " + can_name+ " " + gen_prct);
									result = Double.parseDouble(gen_prct.substring(0, gen_prct.length()-1));
								}
								insert = true;
								type = 1;
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
								CDs.get(cdlookup).insertResults(fecid, incum, can_name, party, result,type);
								if(i==2008) {
									if(!fecids2008.contains(fecid)) {
										fecids2008.add(fecid);
									}
								} else if(i==2010) {
									if(!fecids2010.contains(fecid)) {
										fecids2010.add(fecid);
									}
								} else if(i==2012) {
									if(!fecids2012.contains(fecid)) {
										fecids2012.add(fecid);
									}
								} else if(i==2014) {
									if(!fecids2014.contains(fecid)) {
										fecids2014.add(fecid);
									}
								} else if(i==2016) {
									if(!fecids2016.contains(fecid)) {
										fecids2016.add(fecid);
									}
								} else {
									System.out.println("Problem: " + i);
								}
								
								//System.out.println(Arrays.toString(holder));
								
							}
						}
						
					}
					
					theline = br.readLine();
				}	
				
				br.close();
			} catch(FileNotFoundException e){
				System.out.println("File not found: " + i);
			} catch(IOException e) {
				e.printStackTrace();
			} 
		}
		
		//read in fec id for 2018 candidates
		try {
			
			BufferedReader br = new BufferedReader(new FileReader("C:/Users/onest/Desktop/2018 House Model/election data files/FECNum2018.csv"));
			
			//skip header
			theline = br.readLine();
			theline = br.readLine();
			
			while(theline != null) {
				
				state = "";
				cd = "";
				can_name = "";
				party = "";
				incumbency = "";
				fecid = "";
				
				holder = theline.split(",");
				
				state = holder[1].trim().replace("\"", "");
				cd = holder[2].trim().replace("\"", "");
				can_name = holder[3].trim().replace("\"", "");
				party = holder[4].trim().replace("\"", "");
				incumbency = holder[5].trim().replace("\"", "");
				fecid = holder[6].trim().replace("\"", "");
							
				//construct lookup
				if(cd.equals("0")) {
					cd = "1";
				}
				if(cd.length() < 2) {
					cd = "0" + cd;
				}
				
				//set incumbency
				if(!incumbency.equals("")) {
					incum = 1;
				} else {
					incum = 0;
				}
				
				//if no fec info, set blank  (George McDermott - MD04)
				if(fecid == "NA") {
					fecid = "";
				}
				
				cdlookup = state + cd + "_2018";
				
				//insert
				if(CDs.containsKey(cdlookup)) {
					CDs.get(cdlookup).insert2018(fecid, incum, can_name, party);
					if(!fecids2018.contains(fecid)) {
						fecids2018.add(fecid);
					}
				}
				
				theline = br.readLine();
			}
			
			br.close();
		} catch(FileNotFoundException e){
			System.out.println("File not found: FECNum2018.csv");
		} catch(IOException e) {
			e.printStackTrace();
		} 
		
		//read in and store FEC data for 2008-2018
		for(int i = 2008; i <= 2018; i = i+2) {
			String thefile = "C:/Users/onest/Desktop/2018 House Model/election data files/candidate_summary_" + i + ".csv";
			
			//try...catch statement
			try {
			
				BufferedReader br = new BufferedReader(new FileReader(thefile));
				
				//skip first line
				theline = br.readLine();
				theline = br.readLine();
				boolean exists = false;
				
				while(theline != null) {
					fecid = "";
					State = "";
					cd = "";
					party = "";
					TotRec = "";
					TotDisb = "";
					COHCOP = "";
					COHBOP = "";
					DebtOBC = "";
					IndItemCont = "";
					IndUnitemCont = "";
					IndCont = "";
					OthCommContr = "";
					PartyCommContr = "";
					TotCont = "";
					TransFOAC = "";
					TotLoan = "";
					OfftoOpExpend = "";
					OthReceipts = "";
					OpExpend = "";
					TransTOAC = "";
					TotLoanRepay = "";
					TotContrRef = "";
					OthDisb = "";
					NetContr = "";
					NetOpExp = "";
					exists = false;
					
					holder = theline.split(",");
					
					fecid = holder[2];
					
					if(i==2008) {
						if(fecids2008.contains(fecid)) {
							exists = true;
						}
					} else if(i==2010) {
						if(fecids2010.contains(fecid)) {
							exists = true;
						}
					} else if(i==2012) {
						if(fecids2012.contains(fecid)) {
							exists = true;
						}
					} else if(i==2014) {
						if(fecids2014.contains(fecid)) {
							exists = true;
						}
					} else if(i==2016) {
						if(fecids2016.contains(fecid)) {
							exists = true;
						}
					} else if(i==2018) {
						if(fecids2018.contains(fecid)) {
							exists = true;
						}
					} else {}
					
					if(exists==true) {
						State = holder[4];
						cd = holder[5];
						party = holder[6];
						TotRec = holder[8];
						TotDisb = holder[9];
						COHCOP = holder[10];
						COHBOP = holder[47];
						DebtOBC = holder[11];
						IndItemCont = holder[18];
						IndUnitemCont = holder[19];
						IndCont = holder[20];
						OthCommContr = holder[21];
						PartyCommContr = holder[22];
						TotCont = holder[24];
						TransFOAC = holder[25];
						TotLoan = holder[28];
						OfftoOpExpend = holder[29];
						OthReceipts = holder[32];
						OpExpend = holder[33];
						TransTOAC = holder[36];
						TotLoanRepay = holder[39];
						TotContrRef = holder[43];
						OthDisb = holder[44];
						NetContr = holder[45];
						NetOpExp = holder[46];
						
						Total_Receipts = Double.parseDouble(TotRec);
						Total_Disbursment = Double.parseDouble(TotDisb);
						COH_Ending = Double.parseDouble(COHCOP);
						COH_Beginning = Double.parseDouble(COHBOP);
						Debt_Owed_By_Committee = Double.parseDouble(DebtOBC);
						Individual_Itemized_Contribution = Double.parseDouble(IndItemCont);
						Individual_Unitemized_Contribution = Double.parseDouble(IndUnitemCont);
						Individual_Contribution = Double.parseDouble(IndCont);
						Other_Committee_Contribution = Double.parseDouble(OthCommContr);
						Party_Committee_Contribution = Double.parseDouble(PartyCommContr);
						Total_Contribution = Double.parseDouble(TotCont);
						Transfer_From_Other_Authorized_Committee = Double.parseDouble(TransFOAC);
						Total_Loan = Double.parseDouble(TotLoan);
						Offset_To_Operating_Expenditure = Double.parseDouble(OfftoOpExpend);
						Other_Receipts = Double.parseDouble(OthReceipts);
						Operating_Expenditure = Double.parseDouble(OpExpend);
						Transfer_To_Other_Authorized_Committee = Double.parseDouble(TransTOAC);
						Total_Loan_Repayment = Double.parseDouble(TotLoanRepay);
						Total_Contribution_Refund = Double.parseDouble(TotContrRef);
						Other_Disbursments = Double.parseDouble(OthDisb);
						Net_Contribution = Double.parseDouble(NetContr);
						Net_Operating_Expenditure = Double.parseDouble(NetOpExp);
						
						//construct lookup
						if(cd.equals("0")) {
							cd = "1";
						}
						if(cd.length() < 2) {
							cd = "0" + cd;
						}
						
						//construct hashmap name
						if(i <= 2010) {
							cdlookup = State + cd + "o_" + i;
						} else if(State.equals("PA") | State.equals("NC") | State.equals("FL")) {
							if(State.equals("PA")) {
								if(i < 2018) {
									cdlookup = State + cd + "r_" + i;
								} else {
									cdlookup = State + cd + "_" + i;
								}
							} else if (State.equals("NC") | State.equals("FL")) {
								if(i < 2016) {
									cdlookup = State + cd + "r_" + i;
								} else {
									cdlookup = State + cd + "_" + i;
								}
							}
						} else {
							cdlookup = State + cd + "_" + i;
						}
					}
				}
				
			} catch(FileNotFoundException e){
				System.out.println("File not found: FECNum2018.csv");
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
