import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class BuildDataset {
	
	public static Map<String,CDs> CDs = new TreeMap<String,CDs>();
	public static Map<String,Cluster> clusters = new HashMap<String,Cluster>();
	public static Map<String,Double> PVIs = new HashMap<String,Double>();
	public static Map<String,Polls> genericballot = new HashMap<String,Polls>();
	public static Map<Date,Polls> genericballot18 = new TreeMap<Date,Polls>();
	public static Map<String,Polls> presapproval = new HashMap<String,Polls>();
	public static Map<Date,Polls> Trumpapproval = new TreeMap<Date,Polls>();
	public static LinkedList<String> fecids2008 = new LinkedList<String>();
	public static LinkedList<String> fecids2010 = new LinkedList<String>();
	public static LinkedList<String> fecids2012 = new LinkedList<String>();
	public static LinkedList<String> fecids2014 = new LinkedList<String>();
	public static LinkedList<String> fecids2016 = new LinkedList<String>();
	public static LinkedList<String> fecids2018 = new LinkedList<String>();

	public static void main(String[] args) throws IOException, ParseException {
		
		String theCDs = "C:/Users/onest/Desktop/2018 House Model/CDs.csv";
		//CDs values
		String theline=null,State,CDNum,CD_Name,MedianAge,Male,White,Black,Hispanic,ForeignBorn,Married,HSGrad,BachGrad,
				MedianIncome,Poverty,MedianEarningsHS,MedianEarningsBach,MedEarnDiff,Urbanicity,LFPR,Religiosity,
				Evangelical,Catholic,Veteran,Cluster,pollster,year;
		//fec results variables
		String fec_file,state,cd,fecid,incumbency = null,can_name1,can_name2,can_name=null,party = null,gen_prct = null,gen_runoff = null,gen_comb = null,gen_count=null;
		String cdlookup = null;
		double result = 0,pvi;
		int incum = 0,type = 0;
		boolean insert = false;
		String TotRec,TotDisb,COHCOP,COHBOP,DebtOBC,IndItemCont,IndUnitemCont,IndCont,OthCommContr,PartyCommContr,TotCont,
			TransFOAC,TotLoan,OfftoOpExpend,OthReceipts,OpExpend,TransTOAC,TotLoanRepay,TotContrRef,OthDisb,NetContr,NetOpExp;
		double Total_Receipts,Total_Disbursement,COH_Ending,COH_Beginning,Debt_Owed_By_Committee,Individual_Itemized_Contribution,
			Individual_Unitemized_Contribution,Individual_Contribution,Other_Committee_Contribution,Party_Committee_Contribution,
			Total_Contribution,Transfer_From_Other_Authorized_Committee,Total_Loan,Offset_To_Operating_Expenditure,Other_Receipts,
			Operating_Expenditure,Transfer_To_Other_Authorized_Committee,Total_Loan_Repayment,Total_Contribution_Refund,
			Other_Disbursments,Net_Contribution,Net_Operating_Expenditure,pollster_grade,gop,dem,approve,disapprove;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date thedate;
		
		Date Election2008 = (Date) formatter.parse("11/4/2008"); 
		Date Election2010 = (Date) formatter.parse("11/2/2010");
		Date Election2012 = (Date) formatter.parse("11/6/2012");
		Date Election2014 = (Date) formatter.parse("11/4/2014");
		Date Election2016 = (Date) formatter.parse("11/8/2016");
		Date current = (Date) formatter.parse("10/2/2018");
		
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
						//System.out.println(i + "   " + theline);
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
						
						if(TotRec.length()>0) {
							Total_Receipts = Double.parseDouble(TotRec);
						} else {
							Total_Receipts = 0;
						}
						
						if(TotDisb.length()>0) {
							Total_Disbursement = Double.parseDouble(TotDisb);
						} else {
							Total_Disbursement = 0;
						}
						
						if(COHCOP.length()>0) {
							COH_Ending = Double.parseDouble(COHCOP);
						} else {
							COH_Ending = 0;
						}
						
						if(COHBOP.length()>0) {
							COH_Beginning = Double.parseDouble(COHBOP);
						} else {
							COH_Beginning = 0;
						}
						
						if(DebtOBC.length()>0) {
							Debt_Owed_By_Committee = Double.parseDouble(DebtOBC);
						} else {
							Debt_Owed_By_Committee = 0;
						}
						
						if(IndItemCont.length()>0) {
							Individual_Itemized_Contribution = Double.parseDouble(IndItemCont);
						} else {
							Individual_Itemized_Contribution = 0;
						}
						
						if(IndUnitemCont.length()>0) {
							Individual_Unitemized_Contribution = Double.parseDouble(IndUnitemCont);
						} else {
							Individual_Unitemized_Contribution = 0;
						}
						
						if(IndCont.length()>0) {
							Individual_Contribution = Double.parseDouble(IndCont);
						} else {
							Individual_Contribution = 0;
						}
						
						if(OthCommContr.length()>0) {
							Other_Committee_Contribution = Double.parseDouble(OthCommContr);
						} else {
							Other_Committee_Contribution = 0;
						}
						
						if(PartyCommContr.length()>0) {
							Party_Committee_Contribution = Double.parseDouble(PartyCommContr);
						} else {
							Party_Committee_Contribution = 0;
						}
						
						if(TotCont.length()>0) {
							Total_Contribution = Double.parseDouble(TotCont);
						} else {
							Total_Contribution = 0;
						}
						
						if(TransFOAC.length()>0) {
							Transfer_From_Other_Authorized_Committee = Double.parseDouble(TransFOAC);
						} else {
							Transfer_From_Other_Authorized_Committee = 0;
						}
						
						if(TotLoan.length()>0) {
							Total_Loan = Double.parseDouble(TotLoan);
						} else {
							Total_Loan = 0;
						}
						
						if(OfftoOpExpend.length()>0) {
							Offset_To_Operating_Expenditure = Double.parseDouble(OfftoOpExpend);
						} else {
							Offset_To_Operating_Expenditure = 0;
						}
						
						if(OthReceipts.length()>0) {
							Other_Receipts = Double.parseDouble(OthReceipts);
						} else {
							Other_Receipts = 0;
						}
						
						if(OpExpend.length()>0) {
							Operating_Expenditure = Double.parseDouble(OpExpend);
						} else {
							Operating_Expenditure = 0;
						}
						
						if(TransTOAC.length()>0) {
							Transfer_To_Other_Authorized_Committee = Double.parseDouble(TransTOAC);
						} else {
							Transfer_To_Other_Authorized_Committee = 0;
						}
						
						if(TotLoanRepay.length()>0) {
							Total_Loan_Repayment = Double.parseDouble(TotLoanRepay);
						} else {
							Total_Loan_Repayment = 0;
						}
						
						if(TotContrRef.length()>0) {
							Total_Contribution_Refund = Double.parseDouble(TotContrRef);
						} else {
							Total_Contribution_Refund = 0;
						}
						
						if(OthDisb.length()>0) {
							Other_Disbursments = Double.parseDouble(OthDisb);
						} else {
							Other_Disbursments = 0;
						}
						
						if(NetContr.length()>0) {
							Net_Contribution = Double.parseDouble(NetContr);
						} else {
							Net_Contribution = 0;
						}
						
						if(NetOpExp.length()>0) {
							Net_Operating_Expenditure = Double.parseDouble(NetOpExp);
						} else {
							Net_Operating_Expenditure = 0;
						}
						
						
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
						
						//insert data
						if(CDs.containsKey(cdlookup)) {
							//System.out.println("Inserting:" + cdlookup + " " + fecid + " " + party);
							CDs.get(cdlookup).insertFEC(fecid,party,Total_Receipts,Total_Disbursement,COH_Ending,COH_Beginning,
									Debt_Owed_By_Committee,Individual_Itemized_Contribution,Individual_Unitemized_Contribution,
									Individual_Contribution,Other_Committee_Contribution,Party_Committee_Contribution,
									Total_Contribution,Transfer_From_Other_Authorized_Committee,Total_Loan,
									Offset_To_Operating_Expenditure,Other_Receipts,Operating_Expenditure,
									Transfer_To_Other_Authorized_Committee,Total_Loan_Repayment,Total_Contribution_Refund,
									Other_Disbursments,Net_Contribution,Net_Operating_Expenditure);
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
		}
		
		//read in PVIs
		try {
			
			BufferedReader br = new BufferedReader(new FileReader("C:/Users/onest/Desktop/2018 House Model/election data files/PVIs.csv"));
			
			//skip header
			theline = br.readLine();
			theline = br.readLine();
			
			while(theline != null) {
				
				holder = theline.split(",");
				
				cdlookup = holder[2];
				pvi = Double.parseDouble(holder[3]);
				
				if(CDs.containsKey(cdlookup)) {
					CDs.get(cdlookup).insertPVI(pvi);
					PVIs.put(cdlookup, pvi);
				}
				
				theline = br.readLine();
			}
			
			br.close();
		} catch(FileNotFoundException e){
			System.out.println("File not found: FECNum2018.csv");
		} catch(IOException e) {
			e.printStackTrace();
		} 
		
		//create generic ballot 2018 instances
		long days;
		Date startdate = formatter.parse("5/1/2018");
		days = TimeUnit.DAYS.convert(Math.abs(current.getTime()-startdate.getTime()), TimeUnit.MILLISECONDS);
		Date iterate = startdate;
		
		for(int i = 0; i <= days; i++) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(startdate);
			cal.add(Calendar.DATE, i); 
			iterate = cal.getTime();
			
			genericballot18.put(iterate, new Polls(iterate.toString()));
		}
		
		
		//read in generic ballot data
		try {
			
			BufferedReader br = new BufferedReader(new FileReader("C:/Users/onest/Desktop/2018 House Model/election data files/genericballotcsv.csv"));
			
			theline = br.readLine();
			theline = br.readLine();
			
			while(theline != null) {
				holder = theline.split(",");
				
				pollster = holder[0];
				thedate = (Date) formatter.parse(holder[1]);
				pollster_grade = Double.parseDouble(holder[9]);
				gop = Double.parseDouble(holder[10]);
				dem = Double.parseDouble(holder[11]);
				
				if(thedate.getTime()-Election2008.getTime()<=0) {
					days = TimeUnit.DAYS.convert(Math.abs(thedate.getTime()-Election2008.getTime()), TimeUnit.MILLISECONDS);
					year = "2008";
				} else if(thedate.getTime()-Election2010.getTime()<=0) {
					days = TimeUnit.DAYS.convert(Math.abs(thedate.getTime()-Election2010.getTime()), TimeUnit.MILLISECONDS);
					year = "2010";
				} else if(thedate.getTime()-Election2012.getTime()<=0) {
					days = TimeUnit.DAYS.convert(Math.abs(thedate.getTime()-Election2012.getTime()), TimeUnit.MILLISECONDS);
					year = "2012";
				} else if(thedate.getTime()-Election2014.getTime()<=0) {
					days = TimeUnit.DAYS.convert(Math.abs(thedate.getTime()-Election2014.getTime()), TimeUnit.MILLISECONDS);
					year = "2014";
				} else if(thedate.getTime()-Election2016.getTime()<=0) {
					days = TimeUnit.DAYS.convert(Math.abs(thedate.getTime()-Election2016.getTime()), TimeUnit.MILLISECONDS);
					year = "2016";
				} else {
					days = TimeUnit.DAYS.convert(Math.abs(thedate.getTime()-current.getTime()), TimeUnit.MILLISECONDS);
					year = "2018";
					
					for(Map.Entry<Date, Polls> entry: genericballot18.entrySet()) {
						if(TimeUnit.DAYS.convert(entry.getKey().getTime()-thedate.getTime(), TimeUnit.MILLISECONDS) > 0 &&
								TimeUnit.DAYS.convert(entry.getKey().getTime()-thedate.getTime(), TimeUnit.MILLISECONDS) <= 28 ) {
							//System.out.println("Inserting: " + thedate + " for " + entry.getKey());
							entry.getValue().insert(pollster, pollster_grade, gop, dem, thedate,TimeUnit.DAYS.convert(entry.getKey().getTime()-thedate.getTime(), TimeUnit.MILLISECONDS));
						}
					}
				}
				
				//if within last 3 weeks, insert
				if(days<=28) {
					if(!genericballot.containsKey(year)) {
						genericballot.put(year, new Polls(year));
					} 
					genericballot.get(year).insert(pollster, pollster_grade, gop, dem, thedate, days);
					//System.out.println(year + "," + pollster + "," + pollster_grade + "," + gop + "," + dem + "," + thedate + "," + days + "\n");
				}
				
				theline = br.readLine();
			}
			
			for(Map.Entry<String, Polls> entry: genericballot.entrySet()) {
				entry.getValue().getPoll();
				System.out.println(entry.getKey() + "," + entry.getValue().getGOP() + "," + entry.getValue().getDem() + "," + entry.getValue().getGap());
			}			
			
			br.close();
		}catch(FileNotFoundException e){
			System.out.println("File not found: genericballotcsv.csv");
		} catch(IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println(theline);
			e.printStackTrace();
		} 
		
		//create Trump approval instances
		startdate = formatter.parse("2/1/2017");
		days = TimeUnit.DAYS.convert(Math.abs(current.getTime()-startdate.getTime()), TimeUnit.MILLISECONDS);
		iterate = startdate;
		
		for(int i = 0; i <= days; i++) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(startdate);
			cal.add(Calendar.DATE, i); 
			iterate = cal.getTime();
			
			Trumpapproval.put(iterate, new Polls(iterate.toString()));
		}
		
		
		//read in generic ballot data
		try {
			
			BufferedReader br = new BufferedReader(new FileReader("C:/Users/onest/Desktop/2018 House Model/election data files/presapprovalcsv.csv"));
			
			theline = br.readLine();
			theline = br.readLine();
			
			while(theline != null) {
				holder = theline.split(",");
				
				pollster = holder[0];
				thedate = (Date) formatter.parse(holder[1]);
				pollster_grade = Double.parseDouble(holder[8]);
				approve = Double.parseDouble(holder[9]);
				disapprove = Double.parseDouble(holder[10]);
				
				if(thedate.getTime()-Election2008.getTime()<=0) {
					days = TimeUnit.DAYS.convert(Math.abs(thedate.getTime()-Election2008.getTime()), TimeUnit.MILLISECONDS);
					year = "2008";
				} else if(thedate.getTime()-Election2010.getTime()<=0) {
					days = TimeUnit.DAYS.convert(Math.abs(thedate.getTime()-Election2010.getTime()), TimeUnit.MILLISECONDS);
					year = "2010";
				} else if(thedate.getTime()-Election2012.getTime()<=0) {
					days = TimeUnit.DAYS.convert(Math.abs(thedate.getTime()-Election2012.getTime()), TimeUnit.MILLISECONDS);
					year = "2012";
				} else if(thedate.getTime()-Election2014.getTime()<=0) {
					days = TimeUnit.DAYS.convert(Math.abs(thedate.getTime()-Election2014.getTime()), TimeUnit.MILLISECONDS);
					year = "2014";
				} else if(thedate.getTime()-Election2016.getTime()<=0) {
					days = TimeUnit.DAYS.convert(Math.abs(thedate.getTime()-Election2016.getTime()), TimeUnit.MILLISECONDS);
					year = "2016";
				} else {
					days = TimeUnit.DAYS.convert(Math.abs(thedate.getTime()-current.getTime()), TimeUnit.MILLISECONDS);
					year = "2018";
					
					for(Map.Entry<Date, Polls> entry: Trumpapproval.entrySet()) {
						if(TimeUnit.DAYS.convert(entry.getKey().getTime()-thedate.getTime(), TimeUnit.MILLISECONDS) > 0 &&
								TimeUnit.DAYS.convert(entry.getKey().getTime()-thedate.getTime(), TimeUnit.MILLISECONDS) <= 28 ) {
							//System.out.println("Inserting: " + thedate + " for " + entry.getKey());
							entry.getValue().insert(pollster, pollster_grade, approve, disapprove, thedate,TimeUnit.DAYS.convert(entry.getKey().getTime()-thedate.getTime(), TimeUnit.MILLISECONDS));
						}
					}
				}
				
				//if within last 3 weeks, insert
				if(days<=28) {
					if(!presapproval.containsKey(year)) {
						presapproval.put(year, new Polls(year));
					} 
					presapproval.get(year).insert(pollster, pollster_grade, approve, disapprove, thedate, days);
					//System.out.println(year + "," + pollster + "," + pollster_grade + "," + gop + "," + dem + "," + thedate + "," + days + "\n");
				}
				
				theline = br.readLine();
			}
			
			System.out.println("\n");
			for(Map.Entry<String, Polls> entry: presapproval.entrySet()) {
				entry.getValue().getPoll();
				System.out.println(entry.getKey() + "," + entry.getValue().getGOP() + "," + entry.getValue().getDem() + "," + entry.getValue().getGap());
			}			
			
			br.close();
		}catch(FileNotFoundException e){
			System.out.println("File not found: genericballotcsv.csv");
		} catch(IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println(theline);
			e.printStackTrace();
		} 

		
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("CDsDataset.csv")));
		
		bw.write("name,State,District,CD_Name,year,MedianAge,Male,White,Black,Hispanic,ForeignBorn,Married,HSGrad,BachGrad," + 
					"MedianIncome,Poverty,MedianEarningsHS,MedianEarningsBach,MedEarnDiff,Urbanicity,LFPR,Religiosity," + 
					"Evangelical,Catholic,Veteran,Cluster,GOP_Candidate,GOP_Incumbent,GOP_Result,Dem_Candidate,Dem_Incumbent," + 
					"Dem_Result,Total_Receipts_GOP,Total_Disbursement_GOP,COH_Ending_GOP,COH_Beginning_GOP,Debt_Owed_By_Committee_GOP," + 
					"Individual_Itemized_Contribution_GOP,Individual_Unitemized_Contribution_GOP,Individual_Contribution_GOP," + 
					"Other_Committee_Contribution_GOP,Party_Committee_Contribution_GOP,Total_Contribution_GOP,Transfer_From_Other_Authorized_Committee_GOP," + 
					"Total_Loan_GOP,Offset_To_Operating_Expenditure_GOP,Other_Receipts_GOP,Operating_Expenditure_GOP," + 
					"Transfer_To_Other_Authorized_Committee_GOP,Total_Loan_Repayment_GOP,Total_Contribution_Refund_GOP," + 
					"Other_Disbursements_GOP,Net_Contribution_GOP,Net_Operating_Expenditure_GOP,Prct_Receipts_From_Ind_Contr_GOP," +
					"Prct_Receipts_From_Committee_GOP,Burn_Rate_GOP,Total_Receipts_Dem,Total_Disbursement_Dem,COH_Ending_Dem,COH_Beginning_Dem,Debt_Owed_By_Committee_Dem," + 
					"Individual_Itemized_Contribution_Dem,Individual_Unitemized_Contribution_Dem,Individual_Contribution_Dem," +
					"Other_Committee_Contribution_Dem,Party_Committee_Contribution_Dem,Total_Contribution_Dem,"+ 
					"Transfer_From_Other_Authorized_Committee_Dem,Total_Loan_Dem,Offset_To_Operating_Expenditure_Dem,Other_Receipts_Dem," + 
					"Operating_Expenditure_Dem,Transfer_To_Other_Authorized_Committee_Dem,Total_Loan_Repayment_Dem," + 
					"Total_Contribution_Refund_Dem,Other_Disbursements_Dem,Net_Contribution_Dem,Net_Operating_Expenditure_Dem," + 
					"Prct_Receipts_From_Ind_Contr_Dem,Prct_Receipts_From_Committee_Dem,Burn_Rate_Dem,Prct_Total_Receipts_GOP," + 
					"Prct_Total_Disbursement_GOP,Prct_COH_GOP,Prct_Individual_Contribution_GOP,Prct_Committee_Contribution_GOP," +
					"Total_Receipts_Diff_GOP,Total_Disbursement_Diff_GOP,COH_Adv_GOP,Individual_Contribution_Adv_GOP,Committee_Contribution_Adv_GOP," +
					"PVI,President,President_Time,House,House_Time,CD_Time_Indicator,Midterm\n");
		
		int i = 0;
		for(Map.Entry<String,CDs> entry: CDs.entrySet()){
			bw.write(entry.getValue().toString());
			//System.out.println(i + ": " + entry.getValue().toString());
			i++;
		}
		bw.close();
		
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(new File("GenericBallot2018.csv")));
		
		bw2.write("Date,GOP,Dem,Gap\n");
		
		for(Map.Entry<Date, Polls> entry: genericballot18.entrySet()) {
			entry.getValue().getPoll();
			bw2.write(formatter.format(entry.getKey()) + "," + Math.round(entry.getValue().getGOP()*100.0)/100.0 + "," + Math.round(entry.getValue().getDem()*100.0)/100.0 + "," + Math.round(entry.getValue().getGap()*100.0)/100.0 + "\n");
		}
		bw2.close();
		
		BufferedWriter bw3 = new BufferedWriter(new FileWriter(new File("TrumpApproval.csv")));
		
		bw3.write("Date,Approve,Disapprove,Gap\n");
		
		for(Map.Entry<Date, Polls> entry: Trumpapproval.entrySet()) {
			entry.getValue().getPoll();
			bw3.write(formatter.format(entry.getKey()) + "," + Math.round(entry.getValue().getGOP()*100.0)/100.0 + "," + Math.round(entry.getValue().getDem()*100.0)/100.0 + "," + Math.round(entry.getValue().getGap()*100.0)/100.0 + "\n");
		}
		bw3.close();
		
		System.out.println(i);
	}

}
