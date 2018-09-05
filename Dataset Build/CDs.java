
public class CDs {
	
	private String name,State,CDNum,CD_Name,MedianAge,Male,White,Black,Hispanic,ForeignBorn,Married,
			HSGrad,BachGrad,MedianIncome,Poverty,MedianEarningsHS,MedianEarningsBach,MedEarnDiff,Urbanicity,
			LFPR,Religiosity,Evangelical,Catholic,Veteran,Cluster;
	private int year;

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
	}
}
