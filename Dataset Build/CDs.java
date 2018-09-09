
public class CDs {
	
	private String name,State,CDNum,CD_Name,MedianAge,Male,White,Black,Hispanic,ForeignBorn,Married,
			HSGrad,BachGrad,MedianIncome,Poverty,MedianEarningsHS,MedianEarningsBach,MedEarnDiff,Urbanicity,
			LFPR,Religiosity,Evangelical,Catholic,Veteran,Cluster,fecid_gop,fecid_dem,can_gop,can_dem;
	private int year,inc_gop,inc_dem;
	private double gop_prct,dem_prct;
	private boolean gop,dem;

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
		gop = false;
		dem = false;
	}
	
	//insert FEC election results data
	public void insertResults(String fecid, int incumbency, String can_name, String party, double result) {
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
		
		if(party.equals("D") | party.equals("DEM")) {
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
	}
	
	//toString...return to write dataset
	public String toString(){
		//run check before returning
		check();
		
		return name + "," + State + "," + CDNum + "," + CD_Name + "," + year + "," + MedianAge + "," + Male + "," + White + "," +
					Black  + "," + Hispanic + "," + ForeignBorn + "," + Married + "," + HSGrad + "," + BachGrad + "," + MedianIncome
					 + "," + Poverty + "," + MedianEarningsHS + "," + MedianEarningsBach + "," + MedEarnDiff + "," + Urbanicity
					 + "," + LFPR + "," + Religiosity + "," + Evangelical + "," + Catholic + "," + Veteran + "," + Cluster + "," +
					 can_gop + "," + inc_gop + "," + gop_prct + "," + can_dem + "," + inc_dem + "," + dem_prct + "\n";
	}
}
