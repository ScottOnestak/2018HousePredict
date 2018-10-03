
public class CDs {
	
	private String name,State,CDNum,CD_Name,MedianAge,Male,White,Black,Hispanic,ForeignBorn,Married,
			HSGrad,BachGrad,MedianIncome,Poverty,MedianEarningsHS,MedianEarningsBach,MedEarnDiff,Urbanicity,
			LFPR,Religiosity,Evangelical,Catholic,Veteran,Cluster,fecid_gop,fecid_dem,can_gop,can_dem,President,House;
	private int year,inc_gop,inc_dem,type,President_Time,House_Time,CD_Time_Indicator;
	String gop_prctstr,dem_prctstr;
	private boolean gop,dem,gopfec,demfec;
	private double gop_prct,dem_prct,Total_Receipts_GOP,Total_Disbursement_GOP,COH_Ending_GOP,COH_Beginning_GOP,
		Debt_Owed_By_Committee_GOP,Individual_Itemized_Contribution_GOP,Individual_Unitemized_Contribution_GOP,
		Individual_Contribution_GOP,Other_Committee_Contribution_GOP,Party_Committee_Contribution_GOP,Total_Contribution_GOP,
		Transfer_From_Other_Authorized_Committee_GOP,Total_Loan_GOP,Offset_To_Operating_Expenditure_GOP,
		Other_Receipts_GOP,Operating_Expenditure_GOP,Transfer_To_Other_Authorized_Committee_GOP,Total_Loan_Repayment_GOP,
		Total_Contribution_Refund_GOP,Other_Disbursements_GOP,Net_Contribution_GOP,Net_Operating_Expenditure_GOP,
		Prct_Receipts_From_Ind_Contr_GOP,Prct_Receipts_From_Committee_GOP,Burn_Rate_GOP,
		Total_Receipts_Dem,Total_Disbursement_Dem,COH_Ending_Dem,COH_Beginning_Dem,
		Debt_Owed_By_Committee_Dem,Individual_Itemized_Contribution_Dem,Individual_Unitemized_Contribution_Dem,
		Individual_Contribution_Dem,Other_Committee_Contribution_Dem,Party_Committee_Contribution_Dem,Total_Contribution_Dem,
		Transfer_From_Other_Authorized_Committee_Dem,Total_Loan_Dem,Offset_To_Operating_Expenditure_Dem,
		Other_Receipts_Dem,Operating_Expenditure_Dem,Transfer_To_Other_Authorized_Committee_Dem,Total_Loan_Repayment_Dem,
		Total_Contribution_Refund_Dem,Other_Disbursements_Dem,Net_Contribution_Dem,Net_Operating_Expenditure_Dem,
		Prct_Receipts_From_Ind_Contr_Dem,Prct_Receipts_From_Committee_Dem,Burn_Rate_Dem,
		Prct_Total_Receipts_GOP,Prct_Total_Disbursement_GOP,Prct_COH_GOP,Prct_Individual_Contribution_GOP,Prct_Committee_Contribution_GOP,
		Total_Receipts_Diff_GOP,Total_Disbursement_Diff_GOP,COH_Adv_GOP,Individual_Contribution_Adv_GOP,Committee_Contribution_Adv_GOP,
		pvi;
	

	//constructor
	public CDs(String name,String State,String CDNum,String CD_Name,int year,String MedianAge,String Male,
			String White,String Black,String Hispanic,String ForeignBorn,String Married,String HSGrad,
			String BachGrad,String MedianIncome,String Poverty,String MedianEarningsHS,String MedianEarningsBach,
			String MedEarnDiff,String Urbanicity,String LFPR,String Religiosity,String Evangelical,
			String Catholic,String Veteran,String Cluster){
		this.name = name;
		this.State = State;
		this.CDNum = CDNum;
		this.CD_Name = CD_Name;
		this.year = year;
		this.MedianAge = MedianAge;
		this.Male = Male;
		this.White = White;
		this.Black = Black;
		this.Hispanic = Hispanic;
		this.ForeignBorn = ForeignBorn;
		this.Married = Married;
		this.HSGrad = HSGrad;
		this.BachGrad = BachGrad;
		this.MedianIncome = MedianIncome;
		this.Poverty = Poverty;
		this.MedianEarningsHS = MedianEarningsHS;
		this.MedianEarningsBach = MedianEarningsBach;
		this.MedEarnDiff = MedEarnDiff;
		this.Urbanicity = Urbanicity;
		this.LFPR = LFPR;
		this.Religiosity = Religiosity;
		this.Evangelical = Evangelical;
		this.Catholic = Catholic;
		this.Veteran = Veteran;
		this.Cluster = Cluster;	
		
		//set candidate values to false
		gop = false; gopfec = false;
		dem = false; demfec = false;
		type = 0;
	}
	
	//insert FEC election results data... 2008 - 2016
	public void insertResults(String fecid, int incumbency, String can_name, String party, double result, int itype) {
		if(type != 3 | itype == 3) {
			type = itype;
			
			if(party.equals("R") | party.equals("REP")) {
				if(gop==false) {
					gop = true;
					fecid_gop = fecid;
					inc_gop = incumbency;
					can_gop = can_name;
					gop_prct = result;
				} else {
					gop_prct += result;
				}
				
			} 
			
			if(party.equals("D") | party.equals("DEM") | party.equals("DFL") | party.equals("DNL")) {
				if(dem==false) {
					dem = true;
					fecid_dem = fecid;
					inc_dem = incumbency;
					can_dem = can_name;
					dem_prct = result;
				} else {
					dem_prct += result;
				}
			}
		}			
	}
	
	//2018 candidate insert method
	public void insert2018(String fecid, int incumbency, String can_name, String party) {
	
		if(party.equals("Republican")) {
			gop = true;
			fecid_gop = fecid;
			inc_gop = incumbency;
			can_gop = can_name;
		}
		
		if(party.equals("Democratic")) {
			dem = true;
			fecid_dem = fecid;
			inc_dem = incumbency;
			can_dem = can_name;
		}
	}
	
	public void insertFEC(String fecid,String party,Double Total_Receipts,Double Total_Disbursement,Double COH_Ending,
			Double COH_Beginning,Double Debt_Owed_By_Committee,Double Individual_Itemized_Contribution,
			Double Individual_Unitemized_Contribution,Double Individual_Contribution,Double Other_Committee_Contribution,
			Double Party_Committee_Contribution,Double Total_Contribution,Double Transfer_From_Other_Authorized_Committee,
			Double Total_Loan,Double Offset_To_Operating_Expenditure,Double Other_Receipts,Double Operating_Expenditure,
			Double Transfer_To_Other_Authorized_Committee,Double Total_Loan_Repayment,Double Total_Contribution_Refund,
			Double Other_Disbursements,Double Net_Contribution,Double Net_Operating_Expenditure) {
		//System.out.println(fecid_gop + "," + fecid_dem);
		if(fecid.equals(fecid_gop)) {
			//System.out.println("GOP Match");
			gopfec = true;
			Total_Receipts_GOP = Total_Receipts;
			Total_Disbursement_GOP = Total_Disbursement;
			COH_Ending_GOP = COH_Ending;
			COH_Beginning_GOP = COH_Beginning;
			Debt_Owed_By_Committee_GOP = Debt_Owed_By_Committee;
			Individual_Itemized_Contribution_GOP = Individual_Itemized_Contribution;
			Individual_Unitemized_Contribution_GOP = Individual_Unitemized_Contribution;
			Individual_Contribution_GOP = Individual_Contribution;
			Other_Committee_Contribution_GOP = Other_Committee_Contribution;
			Party_Committee_Contribution_GOP = Party_Committee_Contribution;
			Total_Contribution_GOP = Total_Contribution;
			Transfer_From_Other_Authorized_Committee_GOP = Transfer_From_Other_Authorized_Committee;
			Total_Loan_GOP = Total_Loan;
			Offset_To_Operating_Expenditure_GOP = Offset_To_Operating_Expenditure;
			Other_Receipts_GOP = Other_Receipts;
			Operating_Expenditure_GOP = Operating_Expenditure;
			Transfer_To_Other_Authorized_Committee_GOP = Transfer_To_Other_Authorized_Committee;
			Total_Loan_Repayment_GOP = Total_Loan_Repayment;
			Total_Contribution_Refund_GOP = Total_Contribution_Refund;
			Other_Disbursements_GOP = Other_Disbursements;
			Net_Contribution_GOP = Net_Contribution;
			Net_Operating_Expenditure_GOP = Net_Operating_Expenditure;
			
			//total receipts %
			if(Total_Receipts_GOP > 0) {
				Prct_Receipts_From_Ind_Contr_GOP = Individual_Contribution_GOP / Total_Receipts_GOP * 100;
				Prct_Receipts_From_Committee_GOP = Other_Committee_Contribution_GOP / Total_Receipts_GOP * 100;
				Burn_Rate_GOP = Total_Disbursement_GOP / (Total_Receipts_GOP + Total_Disbursement_GOP) * 100;
			} else {
				Prct_Receipts_From_Ind_Contr_GOP = 0;
				Prct_Receipts_From_Committee_GOP = 0;
				Burn_Rate_GOP = 0;
			}
		}
		
		if(fecid.equals(fecid_dem)) {
			//System.out.println("Dem Match");
			demfec = true;
			Total_Receipts_Dem = Total_Receipts;
			Total_Disbursement_Dem = Total_Disbursement;
			COH_Ending_Dem = COH_Ending;
			COH_Beginning_Dem = COH_Beginning;
			Debt_Owed_By_Committee_Dem = Debt_Owed_By_Committee;
			Individual_Itemized_Contribution_Dem = Individual_Itemized_Contribution;
			Individual_Unitemized_Contribution_Dem = Individual_Unitemized_Contribution;
			Individual_Contribution_Dem = Individual_Contribution;
			Other_Committee_Contribution_Dem = Other_Committee_Contribution;
			Party_Committee_Contribution_Dem = Party_Committee_Contribution;
			Total_Contribution_Dem = Total_Contribution;
			Transfer_From_Other_Authorized_Committee_Dem = Transfer_From_Other_Authorized_Committee;
			Total_Loan_Dem = Total_Loan;
			Offset_To_Operating_Expenditure_Dem = Offset_To_Operating_Expenditure;
			Other_Receipts_Dem = Other_Receipts;
			Operating_Expenditure_Dem = Operating_Expenditure;
			Transfer_To_Other_Authorized_Committee_Dem = Transfer_To_Other_Authorized_Committee;
			Total_Loan_Repayment_Dem = Total_Loan_Repayment;
			Total_Contribution_Refund_Dem = Total_Contribution_Refund;
			Other_Disbursements_Dem = Other_Disbursements;
			Net_Contribution_Dem = Net_Contribution;
			Net_Operating_Expenditure_Dem = Net_Operating_Expenditure;
			
			//total receipts %
			if(Total_Receipts_Dem > 0) {
				Prct_Receipts_From_Ind_Contr_Dem = Individual_Contribution_Dem / Total_Receipts_Dem * 100;
				Prct_Receipts_From_Committee_Dem = Other_Committee_Contribution_Dem / Total_Receipts_Dem * 100;
				Burn_Rate_Dem = Total_Disbursement_Dem / (Total_Receipts_Dem + Total_Disbursement_Dem) * 100;
			} else {
				Prct_Receipts_From_Ind_Contr_Dem = 0;
				Prct_Receipts_From_Committee_Dem = 0;
				Burn_Rate_Dem = 0;
			}
		}
		
	}
	
	//insert PVI method
	public void insertPVI(double pvi) {
		this.pvi = pvi;
	}
	
	//check if both a democrat and republican candidate... if not, fill in NA and 0s for dataset before writing
	public void check() {
		if(gop==false) {
			can_gop = "NA";
			inc_gop = 0;
			gop_prct = 0;
		}
		
		if(dem==false) {
			can_dem = "NA";
			inc_dem = 0;
			dem_prct = 0;
		}
		
		if(gopfec==false) {
			Total_Receipts_GOP = 0;
			Total_Disbursement_GOP = 0;
			COH_Ending_GOP = 0;
			COH_Beginning_GOP = 0;
			Debt_Owed_By_Committee_GOP = 0;
			Individual_Itemized_Contribution_GOP = 0;
			Individual_Unitemized_Contribution_GOP = 0;
			Individual_Contribution_GOP = 0;
			Other_Committee_Contribution_GOP = 0;
			Party_Committee_Contribution_GOP = 0;
			Total_Contribution_GOP = 0;
			Transfer_From_Other_Authorized_Committee_GOP = 0;
			Total_Loan_GOP = 0;
			Offset_To_Operating_Expenditure_GOP = 0;
			Other_Receipts_GOP = 0;
			Operating_Expenditure_GOP = 0;
			Transfer_To_Other_Authorized_Committee_GOP = 0;
			Total_Loan_Repayment_GOP = 0;
			Total_Contribution_Refund_GOP = 0;
			Other_Disbursements_GOP = 0;
			Net_Contribution_GOP = 0;
			Net_Operating_Expenditure_GOP = 0;
			Prct_Receipts_From_Ind_Contr_GOP = 0;
			Prct_Receipts_From_Committee_GOP = 0;
		}
		
		if(demfec==false) {
			Total_Receipts_Dem = 0;
			Total_Disbursement_Dem = 0;
			COH_Ending_Dem = 0;
			COH_Beginning_Dem = 0;
			Debt_Owed_By_Committee_Dem = 0;
			Individual_Itemized_Contribution_Dem = 0;
			Individual_Unitemized_Contribution_Dem = 0;
			Individual_Contribution_Dem = 0;
			Other_Committee_Contribution_Dem = 0;
			Party_Committee_Contribution_Dem = 0;
			Total_Contribution_Dem = 0;
			Transfer_From_Other_Authorized_Committee_Dem = 0;
			Total_Loan_Dem = 0;
			Offset_To_Operating_Expenditure_Dem = 0;
			Other_Receipts_Dem = 0;
			Operating_Expenditure_Dem = 0;
			Transfer_To_Other_Authorized_Committee_Dem = 0;
			Total_Loan_Repayment_Dem = 0;
			Total_Contribution_Refund_Dem = 0;
			Other_Disbursements_Dem = 0;
			Net_Contribution_Dem = 0;
			Net_Operating_Expenditure_Dem = 0;
			Prct_Receipts_From_Ind_Contr_Dem = 0;
			Prct_Receipts_From_Committee_Dem = 0;
		}
		
		if((Total_Receipts_GOP+Total_Receipts_Dem)>0) {
			Prct_Total_Receipts_GOP = (Total_Receipts_GOP / (Total_Receipts_GOP+Total_Receipts_Dem)) * 100;
		} else {
			Prct_Total_Receipts_GOP = -1;
		}
		
		if((Total_Disbursement_GOP+Total_Disbursement_Dem)>0) {
			Prct_Total_Disbursement_GOP = (Total_Disbursement_GOP / (Total_Disbursement_GOP+Total_Disbursement_Dem)) * 100;
		} else {
			Prct_Total_Disbursement_GOP = -1;
		}
		
		if((COH_Ending_GOP+COH_Ending_Dem)>0) {
			Prct_COH_GOP = (COH_Ending_GOP / (COH_Ending_GOP+COH_Ending_Dem)) * 100;
		} else {
			Prct_COH_GOP = -1;
		}
		
		if((Individual_Contribution_GOP+Individual_Contribution_Dem)>0) {
			Prct_Individual_Contribution_GOP = (Individual_Contribution_GOP / (Individual_Contribution_GOP+Individual_Contribution_Dem)) * 100;
		} else {
			Prct_Individual_Contribution_GOP = -1;
		}
		
		if((Other_Committee_Contribution_GOP+Other_Committee_Contribution_Dem)>0) {
			Prct_Committee_Contribution_GOP = (Other_Committee_Contribution_GOP / (Other_Committee_Contribution_GOP+Other_Committee_Contribution_Dem)) * 100;
		} else {
			Prct_Committee_Contribution_GOP = -1;
		}
		
		Total_Receipts_Diff_GOP = Total_Receipts_GOP - Total_Receipts_Dem;
		Total_Disbursement_Diff_GOP = Total_Disbursement_GOP - Total_Disbursement_Dem;
		COH_Adv_GOP = COH_Ending_GOP - COH_Ending_Dem;
		Individual_Contribution_Adv_GOP = Individual_Contribution_GOP - Individual_Contribution_Dem;
		Committee_Contribution_Adv_GOP = Other_Committee_Contribution_GOP - Other_Committee_Contribution_Dem;
		
		//if 2018... results should be NA
		if(year == 2018) {
			gop_prctstr = "NA";
			dem_prctstr = "NA";
		} else {
			gop_prctstr = Double.toString(gop_prct);
			dem_prctstr = Double.toString(dem_prct);
		}
		
		//pres party in power
		if(year == 2006 | year == 2008 | year == 2018) {
			President = "R";
		} else {
			President = "D";
		}
		
		//pres time in office
		if(year == 2006) {
			President_Time = 6;
		} else if(year == 2008) {
			President_Time = 8;
		} else if(year == 2010) {
			President_Time = -2;
		} else if(year == 2012) {
			President_Time = -4;
		} else if(year == 2014) {
			President_Time = -6;
		} else if(year == 2016) {
			President_Time = -8;
		} else {
			President_Time = 2;
		}
		
		//part in control house
		if(year == 2008 | year == 2010) {
			House = "D";
		} else {
			House = "R";
		}
		
		//time control of house
		if(year == 2006) {
			House_Time = 12;
		} else if(year == 2008) {
			House_Time = -2;
		} else if(year == 2010) {
			House_Time = -4;
		} else if(year == 2012) {
			House_Time = 2;
		} else if(year == 2014) {
			House_Time = 4;
		} else if(year == 2016) {
			House_Time = 6;
		} else {
			House_Time = 8;
		}
		
		//cd time indicator... new vs. old districts
		if(year > 2010) {
			CD_Time_Indicator = 1;
		} else {
			CD_Time_Indicator = 0;
		}
	}
	
	//toString...return to write dataset
	public String toString(){
		//run check before returning
		check();
		
		return name + "," + State + "," + CDNum + "," + CD_Name + "," + year + "," + MedianAge + "," + Male + "," + White + "," +
					Black  + "," + Hispanic + "," + ForeignBorn + "," + Married + "," + HSGrad + "," + BachGrad + "," + MedianIncome
					 + "," + Poverty + "," + MedianEarningsHS + "," + MedianEarningsBach + "," + MedEarnDiff + "," + Urbanicity
					 + "," + LFPR + "," + Religiosity + "," + Evangelical + "," + Catholic + "," + Veteran + "," + Cluster + "," +
					 can_gop + "," + inc_gop + "," + gop_prctstr + "," + can_dem + "," + inc_dem + "," + dem_prctstr + "," +
					 Total_Receipts_GOP + "," + Total_Disbursement_GOP + "," + COH_Ending_GOP  + "," +  COH_Beginning_GOP + "," +
					 Debt_Owed_By_Committee_GOP + "," + Individual_Itemized_Contribution_GOP  + "," + Individual_Unitemized_Contribution_GOP
					 + "," + Individual_Contribution_GOP + "," + Other_Committee_Contribution_GOP + "," + 
					 Party_Committee_Contribution_GOP + "," + Total_Contribution_GOP + "," + Transfer_From_Other_Authorized_Committee_GOP
					 + "," + Total_Loan_GOP + "," + Offset_To_Operating_Expenditure_GOP + "," + Other_Receipts_GOP + "," + 
					 Operating_Expenditure_GOP + "," + Transfer_To_Other_Authorized_Committee_GOP + "," +  Total_Loan_Repayment_GOP
					 + "," + Total_Contribution_Refund_GOP + "," + Other_Disbursements_GOP + "," + Net_Contribution_GOP + "," + 
					 Net_Operating_Expenditure_GOP + "," + Prct_Receipts_From_Ind_Contr_GOP + "," + Prct_Receipts_From_Committee_GOP + "," + Burn_Rate_GOP + "," +
					 Total_Receipts_Dem + "," + Total_Disbursement_Dem + "," + COH_Ending_Dem  + "," +  COH_Beginning_Dem + "," +
					 Debt_Owed_By_Committee_Dem + "," + Individual_Itemized_Contribution_Dem  + "," + Individual_Unitemized_Contribution_Dem
					 + "," + Individual_Contribution_Dem + "," + Other_Committee_Contribution_Dem + "," + 
					 Party_Committee_Contribution_Dem + "," + Total_Contribution_Dem + "," + Transfer_From_Other_Authorized_Committee_Dem
					 + "," + Total_Loan_Dem + "," + Offset_To_Operating_Expenditure_Dem + "," + Other_Receipts_Dem + "," + 
					 Operating_Expenditure_Dem + "," + Transfer_To_Other_Authorized_Committee_Dem + "," +  Total_Loan_Repayment_Dem
					 + "," + Total_Contribution_Refund_Dem + "," + Other_Disbursements_Dem + "," + Net_Contribution_Dem + "," + 
					 Net_Operating_Expenditure_Dem + "," + Prct_Receipts_From_Ind_Contr_Dem + "," + Prct_Receipts_From_Committee_Dem + "," + Burn_Rate_Dem + "," + 
					 Prct_Total_Receipts_GOP + "," + Prct_Total_Disbursement_GOP + "," + Prct_COH_GOP + "," + Prct_Individual_Contribution_GOP
					 + "," + Prct_Committee_Contribution_GOP + "," + Total_Receipts_Diff_GOP + "," + Total_Disbursement_Diff_GOP + "," +
					 COH_Adv_GOP + "," + Individual_Contribution_Adv_GOP + "," + Committee_Contribution_Adv_GOP + "," + pvi + "," +
					 President + "," + President_Time + "," + House + "," + House_Time + "," + CD_Time_Indicator + "\n";
	}
}
