#Scott Onestak
#House Clustering and Dataset Prep

#Packages
library(dplyr)
library(tidyr)
library(ggplot2)
library(stringr)

#Read in data
Dist2018 = read.csv("election data files/PresResults2018CDs.csv",header=TRUE)
Dist2000 = read.csv("election data files/PresResults2000CDs.csv",header=TRUE)
HouseResults = read.delim("election data files/1976-2016-house.tab",sep="\t",header=TRUE)
Demographics = read.csv("election data files/BasicDemographics.csv",header=TRUE)
PA = read.csv("election data files/PA_demographics.csv",header=TRUE)
PA_religion = read.csv("election data files/PA_religion.csv",header=T)

PA$MedianEarnings_Bach = as.numeric(as.character(PA$MedianEarnings_Bach))
PA$MedianEarnings_GradProf = as.numeric(as.character(PA$MedianEarnings_GradProf))
PA$MedianEarnings_HS = as.numeric(as.character(PA$MedianEarnings_HS))
PA$MedianEarnings_LessHS = as.numeric(as.character(PA$MedianEarnings_LessHS))
PA$MedianEarnings_SomeCollAssoc = as.numeric(as.character(PA$MedianEarnings_SomeCollAssoc))
PA$MedianIncome = as.numeric(as.character(PA$MedianIncome))
PA$MedianAge = as.numeric(as.character(PA$MedianAge))
PA$UrbanBase = as.numeric(as.character(PA$UrbanBase))
PA$Urban = as.numeric(as.character(PA$Urban))
PA$LFPR = as.numeric(as.character(PA$LFPR))

#Pres Distribution
Dist2018$AvgGOPPres = (Dist2018$GOP16 + Dist2018$GOP12) / 2
Dist2000$AvgGOPPres = (Dist2000$GOP08 + Dist2000$GOP04) / 2

plot(density(Dist2018$AvgGOPPres), xlim=c(0,100),ylim=c(0,.03),main="GOP Average Vote Share",xlab="RED=2010s, BLUE=2000s",col="red")
par(new=T)
plot(density(Dist2000$AvgGOPPres), xlim=c(0,100), ylim=c(0,.03),main="GOP Average Vote Share",xlab="",col="blue")

Districts = rbind(Dist2018[,c(1,2,3,20)],Dist2000[,c(1,2,3,23)])

#House Results
options("scipen"=100, "digits"=2)

HouseResults$district[HouseResults$district == 0] = 1

HouseCDs = HouseResults %>%
  filter(year >= 2004, party %in% c("republican","democrat"),special==FALSE) %>%
  mutate(percent = (candidatevotes/totalvotes)*100) %>%
  dplyr::select(year,state_po,district,party,percent) %>%
  rename(.,state = state_po) %>%
  arrange(state,district,year,party) %>%
  group_by(year,state,district,party) %>%
  filter(percent==max(percent)) %>%
  spread(party,percent)

HouseCDs$democrat[is.na(HouseCDs$democrat)== TRUE] = 0
HouseCDs$republican[is.na(HouseCDs$republican)== TRUE] = 0
HouseCDs$GOPCDAdv = HouseCDs$republican - HouseCDs$democrat
HouseCDs$GOPCDAdv[abs(HouseCDs$GOPCDAdv) >= 90] = NA

#PA Aggregate
PA_agg = PA %>% dplyr::select(-Id,-Id2,-Geography,-borough,-County,-State) %>%
  mutate(VotingAge=rowSums(PA[,c("Pop18to24","Pop25to35","Pop35to45","Pop45to55","Pop55to65","Pop65to75","Pop75Over")],na.rm=T),
         Under35 = rowSums(PA[,c("Pop18to24","Pop25to35")],na.rm=T),
         Over65 = rowSums(PA[,c("Pop65to75","Pop75Over")],na.rm=T),
         BachGrad = rowSums(PA[,c("Bachelors","GradProf")],na.rm=T),
         Poverty = rowSums(PA[,c("Poverty100to149","Poverty150UP")],na.rm=T),
         MedianEarnings_BachW = MedianEarnings_Bach * Bachelors,
         MedianEarnings_HSW = MedianEarnings_HS * HSGrad,
         LFPRw = LFPR * VeteranBase) %>%
  group_by(Congressional.District) %>%
  summarize(TotalPop = sum(TotalPop,na.rm=T),
            VotingAge = sum(VotingAge,na.rm=T),
            Under35 = sum(Under35,na.rm=T),
            Over65 = sum(Over65,na.rm=T),
            MedianAge = mean(MedianAge,na.rm=T),
            Male = sum(Male,na.rm=T),
            RaceBase = sum(RaceBase,na.rm=T),
            WhiteNoHispanic = sum(WhiteNoHispanic,na.rm=T),
            Black = sum(Black,na.rm=T),
            Hispanic = sum(Hispanic,na.rm=T),
            ForeignBorn = sum(ForeignBorn,na.rm=T),
            MaritalBase = sum(MaritalBase,na.rm=T),
            NeverMarried = sum(NeverMarried,na.rm=T),
            Married = sum(Married,na.rm=T),
            EducationBase = sum(EducationBase,na.rm=T),
            LessThanHS = sum(LessThanHS,na.rm=T),
            HSGrad = sum(HSGrad,na.rm=T),
            SomeCollORAssoc = sum(SomeCollORAssoc,na.rm=T),
            Bachelors = sum(Bachelors,na.rm=T),
            GradProf = sum(GradProf,na.rm=T),
            BachGrad = sum(BachGrad,na.rm=T),
            MedianIncome = mean(MedianIncome,na.rm=T),
            PovertyBase = sum(PovertyBase,na.rm=T),
            Poverty = sum(Poverty,na.rm=T),
            MedianEarnings_HSW = sum(MedianEarnings_HSW,na.rm=T),
            MedianEarnings_BachW = sum(MedianEarnings_BachW,na.rm=T),
            UrbanBase = sum(UrbanBase,na.rm=T),
            Urban = sum(Urban,na.rm=T),
            VeteranBase = sum(VeteranBase,na.rm=T),
            Veterans = sum(Veterans,na.rm=T),
            LFPRw = sum(LFPRw,na.rm=T)) %>%
  mutate(MedianEarnings_HS = MedianEarnings_HSW / HSGrad,
         MedianEarnings_Bach = MedianEarnings_BachW / Bachelors,
         MedianEarnings_HSBach_Diff = MedianEarnings_Bach - MedianEarnings_HS,
         Urbanicity = Urban / UrbanBase * 100,
         LFPR = LFPRw / VeteranBase) %>%
  dplyr::select(Congressional.District,TotalPop,VotingAge,Under35,Over65,MedianAge,Male,RaceBase,WhiteNoHispanic,
                Black,Hispanic,ForeignBorn,MaritalBase,NeverMarried,Married,EducationBase,LessThanHS,HSGrad,SomeCollORAssoc,Bachelors,
                GradProf,BachGrad,MedianIncome,PovertyBase,Poverty,MedianEarnings_HS,
                MedianEarnings_Bach,MedianEarnings_HSBach_Diff,Urbanicity,Veterans,VeteranBase,RaceBase,MaritalBase,EducationBase,
                PovertyBase,LFPR)
colnames(PA_agg)[1] = "Congressional.District.Number"
PA_agg$State = "PA"
PA_agg$CD_Name = ifelse(PA_agg$Congressional.District.Number < 10,paste(PA_agg$State,"0",PA_agg$Congressional.District.Number,sep=''), paste(PA_agg$State,PA_agg$Congressional.District.Number,sep=''))
PA_agg = PA_agg %>% dplyr::select(State,Congressional.District.Number,CD_Name, everything())
PA_agg = left_join(PA_agg,PA_religion,by="CD_Name")

#Total US
CDs = Demographics %>%
  mutate(VotingAge=rowSums(Demographics[,c("Pop18to24","Pop25to35","Pop35to45","Pop45to55","Pop55to65","Pop65to75","Pop75Over")],na.rm=T),
         Under35 = rowSums(Demographics[,c("Pop18to24","Pop25to35")],na.rm=T),
         Over65 = rowSums(Demographics[,c("Pop65to75","Pop75Over")],na.rm=T),
         BachGrad = rowSums(Demographics[,c("Bachelors","GradProf")],na.rm=T),
         Poverty = rowSums(Demographics[,c("Poverty100to149","Poverty150UP")],na.rm=T),
         MedianEarnings_HSBach_Diff = MedianEarnings_Bach - MedianEarnings_HS) %>%
  dplyr::select(State,Congressional.District.Number,CD_Name,TotalPop,VotingAge,Under35,Over65,MedianAge,Male,
                RaceBase,WhiteNoHispanic,Black,Hispanic,ForeignBorn,MaritalBase,NeverMarried,Married,EducationBase,LessThanHS,HSGrad,
                SomeCollORAssoc,Bachelors,GradProf,BachGrad,MedianIncome,PovertyBase,Poverty,MedianEarnings_HS,
                MedianEarnings_Bach,MedianEarnings_HSBach_Diff,Urbanicity,Veterans,VeteranBase,RaceBase,MaritalBase,EducationBase,
                PovertyBase,LFPR,Religiosity,Evangelical,RomanCatholic)
CDs = rbind(CDs,PA_agg)

Districts = Districts[,c(3,4)]
Districts$Name = as.character(Districts$Name)

CDs = CDs %>%
  left_join(.,Districts,by=c("CD_Name" = "Name")) %>%
  mutate(Under35 = Under35 / VotingAge * 100,
         Over65 = Over65 / VotingAge * 100,
         Male = Male / TotalPop * 100,
         WhiteNoHispanic = WhiteNoHispanic / RaceBase * 100,
         Black = Black / RaceBase * 100,
         Hispanic = Hispanic / RaceBase * 100,
         NeverMarried = NeverMarried / MaritalBase * 100,
         Married = Married / MaritalBase * 100,
         LessThanHS = LessThanHS / EducationBase * 100,
         HSGrad = HSGrad / EducationBase * 100,
         SomeCollORAssoc = SomeCollORAssoc / EducationBase * 100,
         Bachelors = Bachelors / EducationBase * 100,
         GradProf = GradProf / EducationBase * 100,
         BachGrad = BachGrad / EducationBase * 100,
         Poverty = ((PovertyBase - Poverty) / PovertyBase * 100),
         ForeignBorn = ForeignBorn / TotalPop * 100,
         Veteran = Veterans / VeteranBase * 100) %>%
  dplyr::select(-VotingAge,-TotalPop,-RaceBase,-MaritalBase,-EducationBase,-PovertyBase,-Veterans,-VeteranBase)

#Plots and Plots and Plots
plot(density(CDs$Under35),main="% Under 35",col="blue")
plot(density(CDs$Over65),main="% Over 65",col="blue")
plot(density(CDs$MedianAge),main="Median Age",col="blue")
plot(density(CDs$Male),main="% Male",col="blue")
plot(density(CDs$WhiteNoHispanic),main="% White - No Hispanic",col="blue")
plot(density(CDs$Black),main="% Black",col="blue")
plot(density(CDs$Hispanic),main = "% Hispanic",col="blue")
plot(density(CDs$ForeignBorn),main="% Foreign Born",col="blue")
plot(density(CDs$NeverMarried),main="% Single - Never Married",col="blue")
plot(density(CDs$Married),main="% Married",col="blue")
plot(density(CDs$LessThanHS),main="% Less Than HS Education",col="blue")
plot(density(CDs$HSGrad),main="% HS Education",col="blue")
plot(density(CDs$SomeCollORAssoc),main="% Some College or Associates Degree Education",col="blue")
plot(density(CDs$Bachelors),main="% Bachelors Degree",col="blue")
plot(density(CDs$GradProf),main="% Graduate and Doctorate Degrees",col="blue")
plot(density(CDs$BachGrad),main="% Bachelors and Up",col="blue")
plot(density(CDs$MedianIncome),main="Median Income",col="blue")
plot(density(CDs$Poverty),main="% Below Poverty Line",col="blue")
plot(density(CDs$MedianEarnings_HS),main="Median Earnings - HS Edu",col="blue")
plot(density(CDs$MedianEarnings_Bach),main="Median Earnings - Bachelors",col="blue")
plot(density(CDs$MedianEarnings_HSBach_Diff),main="Median Earnings Differential (Bachelors - HS Edu)",col="blue")
plot(density(CDs$AvgGOPPres),main="Average GOP %",col="blue")
plot(density(CDs$Urbanicity),main="Urbanicity",col="blue")
plot(density(CDs$LFPR),main="LFPR",col="blue")
plot(density(CDs$Veteran),main="Veteran",col="blue")
plot(density(CDs$Religiosity),main="Religiosity (% Belonging to Congregation)",col="blue")
plot(density(CDs$Evangelical),main="Evangelical %",col="blue")
plot(density(CDs$RomanCatholic),main="Roman Catholic %",col="blue")

#Normalize Data
CDs_sub = CDs[,-c(4,5,10,12,14,16,17,18)]
norm = cbind(CDs_sub[,c(1:3)],scale(CDs_sub[,c(4:23)]))

plot(norm[,c(4:9,22)],pch=20)
plot(norm[,c(10:16,22)],pch=20)
plot(norm[,c(17:23)],pch=20)

#PCA
pcs = princomp(norm[,c(4:23)])
plot(pcs)
plot(pcs, type='l')
summary(pcs)

pcs2 = prcomp(norm[c(4:23)])
comp <- data.frame(pcs2$x[,1:7])
plot(comp, pch=16)

#K-Means Cluster
set.seed(412)
km = matrix(nrow=10,ncol=2)
for(i in seq(1:10)){
  kmeans = kmeans(comp,centers = i,iter.max = 1000,nstart=25)
  km[i,1] = i
  km[i,2] = kmeans$tot.withinss
}
km = as.data.frame(km)
colnames(km) = c("Clusters","TotWithinSS")
plot(km$Clusters,km$TotWithinSS,type = "b")

##choose 7
kmeans = kmeans(comp,centers = 7,iter.max = 1000,nstart=25)
norm$cluster = kmeans$cluster

#Plots and Plots and Plots of Clusters
palette(c("blue","green","skyblue","purple","orange","red","gold"))
plot(norm[,c(4:10,22)],col=norm$cluster,pch=16)
plot(norm[,c(11:16,22)],col=norm$cluster,pch=16)
plot(norm[,c(17:23)],col=norm$cluster,pch=16)

#Export Cluster
clusters = norm[,c(3,24)]
write.csv(clusters,file="clusters.csv",row.names = F)

#Cluster Analysis
CDs$cluster = kmeans$cluster
CDs$cluster = as.factor(CDs$cluster)

ggplot(CDs, aes(x=cluster, y=MedianAge,col=cluster)) + 
  geom_boxplot() + 
  scale_color_manual(values=c("deepskyblue","darkorange","firebrick2","blue4","darkorchid3","goldenrod1","green3")) +
  ggtitle("Median Age") + theme(plot.title = element_text(hjust = 0.5))

ggplot(CDs, aes(x=cluster, y=Male,col=cluster)) + 
  geom_boxplot() + 
  scale_color_manual(values=c("deepskyblue","darkorange","firebrick2","blue4","darkorchid3","goldenrod1","green3"))+
  ggtitle("Male") + theme(plot.title = element_text(hjust = 0.5))

ggplot(CDs, aes(x=cluster, y=WhiteNoHispanic,col=cluster)) + 
  geom_boxplot() + 
  scale_color_manual(values=c("deepskyblue","darkorange","firebrick2","blue4","darkorchid3","goldenrod1","green3"))+
  ggtitle("White - No Hispanic %") + theme(plot.title = element_text(hjust = 0.5))

ggplot(CDs, aes(x=cluster, y=Black,col=cluster)) + 
  geom_boxplot() + 
  scale_color_manual(values=c("deepskyblue","darkorange","firebrick2","blue4","darkorchid3","goldenrod1","green3"))+
  ggtitle("Black %") + theme(plot.title = element_text(hjust = 0.5))

ggplot(CDs, aes(x=cluster, y=Hispanic,col=cluster)) + 
  geom_boxplot() + 
  scale_color_manual(values=c("deepskyblue","darkorange","firebrick2","blue4","darkorchid3","goldenrod1","green3"))+
  ggtitle("Hispanic %") + theme(plot.title = element_text(hjust = 0.5))

ggplot(CDs, aes(x=cluster, y=ForeignBorn,col=cluster)) + 
  geom_boxplot() + 
  scale_color_manual(values=c("deepskyblue","darkorange","firebrick2","blue4","darkorchid3","goldenrod1","green3"))+
  ggtitle("Foreign Born %") + theme(plot.title = element_text(hjust = 0.5))

ggplot(CDs, aes(x=cluster, y=Married,col=cluster)) + 
  geom_boxplot() + 
  scale_color_manual(values=c("deepskyblue","darkorange","firebrick2","blue4","darkorchid3","goldenrod1","green3"))+
  ggtitle("Married %") + theme(plot.title = element_text(hjust = 0.5))

ggplot(CDs, aes(x=cluster, y=HSGrad,col=cluster)) + 
  geom_boxplot() + 
  scale_color_manual(values=c("deepskyblue","darkorange","firebrick2","blue4","darkorchid3","goldenrod1","green3"))+
  ggtitle("High School Grad %") + theme(plot.title = element_text(hjust = 0.5))

ggplot(CDs, aes(x=cluster, y=Bachelors,col=cluster)) + 
  geom_boxplot() + 
  scale_color_manual(values=c("deepskyblue","darkorange","firebrick2","blue4","darkorchid3","goldenrod1","green3"))+
  ggtitle("Bachelors %") + theme(plot.title = element_text(hjust = 0.5))

ggplot(CDs, aes(x=cluster, y=GradProf,col=cluster)) + 
  geom_boxplot() + 
  scale_color_manual(values=c("deepskyblue","darkorange","firebrick2","blue4","darkorchid3","goldenrod1","green3"))+
  ggtitle("Graduate/PhD %") + theme(plot.title = element_text(hjust = 0.5))

ggplot(CDs, aes(x=cluster, y=BachGrad,col=cluster)) + 
  geom_boxplot() + 
  scale_color_manual(values=c("deepskyblue","darkorange","firebrick2","blue4","darkorchid3","goldenrod1","green3"))+
  ggtitle("Bachelors + Grad/PhD %") + theme(plot.title = element_text(hjust = 0.5))

ggplot(CDs, aes(x=cluster, y=MedianIncome,col=cluster)) + 
  geom_boxplot() + 
  scale_color_manual(values=c("deepskyblue","darkorange","firebrick2","blue4","darkorchid3","goldenrod1","green3"))+
  ggtitle("Median Income") + theme(plot.title = element_text(hjust = 0.5))

ggplot(CDs, aes(x=cluster, y=Poverty,col=cluster)) + 
  geom_boxplot() + 
  scale_color_manual(values=c("deepskyblue","darkorange","firebrick2","blue4","darkorchid3","goldenrod1","green3"))+
  ggtitle("% Living Below Poverty Line") + theme(plot.title = element_text(hjust = 0.5))

ggplot(CDs, aes(x=cluster, y=MedianEarnings_HS,col=cluster)) + 
  geom_boxplot() + 
  scale_color_manual(values=c("deepskyblue","darkorange","firebrick2","blue4","darkorchid3","goldenrod1","green3"))+
  ggtitle("Median Earnings - HS Graduate") + theme(plot.title = element_text(hjust = 0.5))

ggplot(CDs, aes(x=cluster, y=MedianEarnings_Bach,col=cluster)) + 
  geom_boxplot() + 
  scale_color_manual(values=c("deepskyblue","darkorange","firebrick2","blue4","darkorchid3","goldenrod1","green3"))+
  ggtitle("Median Earnings - Bachelors") + theme(plot.title = element_text(hjust = 0.5))

ggplot(CDs, aes(x=cluster, y=MedianEarnings_HSBach_Diff,col=cluster)) + 
  geom_boxplot() + 
  scale_color_manual(values=c("deepskyblue","darkorange","firebrick2","blue4","darkorchid3","goldenrod1","green3"))+
  ggtitle("Median Earnings Differential (Bachelors - HS)") + theme(plot.title = element_text(hjust = 0.5))

ggplot(CDs, aes(x=cluster, y=Urbanicity,col=cluster)) + 
  geom_boxplot() + 
  scale_color_manual(values=c("deepskyblue","darkorange","firebrick2","blue4","darkorchid3","goldenrod1","green3"))+
  ggtitle("Urbanicity") + theme(plot.title = element_text(hjust = 0.5))

ggplot(CDs, aes(x=cluster, y=LFPR,col=cluster)) + 
  geom_boxplot() + 
  scale_color_manual(values=c("deepskyblue","darkorange","firebrick2","blue4","darkorchid3","goldenrod1","green3"))+
  ggtitle("Labor Force Participartion Rate") + theme(plot.title = element_text(hjust = 0.5))

ggplot(CDs, aes(x=cluster, y=Religiosity,col=cluster)) + 
  geom_boxplot() + 
  scale_color_manual(values=c("deepskyblue","darkorange","firebrick2","blue4","darkorchid3","goldenrod1","green3"))+
  ggtitle("Religiosity (% Belonging to a Congregation)") + theme(plot.title = element_text(hjust = 0.5))

ggplot(CDs, aes(x=cluster, y=Evangelical,col=cluster)) + 
  geom_boxplot() + 
  scale_color_manual(values=c("deepskyblue","darkorange","firebrick2","blue4","darkorchid3","goldenrod1","green3"))+
  ggtitle("Evangelical %") + theme(plot.title = element_text(hjust = 0.5))

ggplot(CDs, aes(x=cluster, y=RomanCatholic,col=cluster)) + 
  geom_boxplot() + 
  scale_color_manual(values=c("deepskyblue","darkorange","firebrick2","blue4","darkorchid3","goldenrod1","green3"))+
  ggtitle("Roman Catholic %") + theme(plot.title = element_text(hjust = 0.5))

ggplot(CDs, aes(x=cluster, y=AvgGOPPres,col=cluster)) + 
  geom_boxplot() + 
  scale_color_manual(values=c("deepskyblue","darkorange","firebrick2","blue4","darkorchid3","goldenrod1","green3"))+
  ggtitle("Average GOP Presidential Vote") + theme(plot.title = element_text(hjust = 0.5))

ggplot(CDs, aes(x=cluster, y=Veteran,col=cluster)) + 
  geom_boxplot() + 
  scale_color_manual(values=c("deepskyblue","darkorange","firebrick2","blue4","darkorchid3","goldenrod1","green3"))+
  ggtitle("Veteran %") + theme(plot.title = element_text(hjust = 0.5))

#Write Demographics
write.csv(CDs,file="CDs.csv",row.names = F)

#Closest CDs List
CDsClosest = cbind(CDs[,c(1,2,3)],comp) %>% left_join(.,clusters,by="CD_Name")
CDsClosest$length = nchar(CDsClosest$CD_Name)
CDsClosest$last = str_sub(CDsClosest$CD_Name,5)

CDs2000s = CDsClosest %>% filter(length > 4 & last == 'o')
CDs2010s = CDsClosest %>% filter(length <= 4 & !State %in% c("FL","NC","PA"))
CDsNCFLr = CDsClosest %>% filter(length > 4 & last == 'r' & State %in% c("FL","NC"))
CDsPAr = CDsClosest %>% filter(length > 4 & last == 'r' & State == "PA")
CDsNCFL = CDsClosest %>% filter(length <= 4 & State %in% c("FL","NC"))
CDsPA = CDsClosest %>% filter(length <= 4 & State == "PA")

CDs2008 = CDs2000s
CDs2008$year = 2008
CDs2010 = CDs2000s
CDs2010$year = 2010
CDs2012 = CDs2010s
CDs2012$year = 2012
CDs2014 = CDs2010s
CDs2014$year = 2014
CDs2016 = CDs2010s
CDs2016$year = 2016
CDs2018 = CDs2010s
CDs2018$year = 2018
CDsNCFL2012 = CDsNCFLr
CDsNCFL2012$year = 2012
CDsNCFL2014 = CDsNCFLr
CDsNCFL2014$year = 2014
CDsNCFL2016 = CDsNCFL 
CDsNCFL2016$year = 2016
CDsNCFL2018 = CDsNCFL
CDsNCFL2018$year = 2018
CDsPA2012 = CDsPAr
CDsPA2012$year = 2012
CDsPA2014 = CDsPAr
CDsPA2014$year = 2014
CDsPA2016 = CDsPAr
CDsPA2016$year = 2016
CDsPA2018 = CDsPA
CDsPA2018$year = 2018

CDsMatch = do.call("rbind", list(CDs2008,CDs2010,CDs2012,CDs2014,CDs2016,CDs2018,CDsNCFL2012,CDsNCFL2014,CDsNCFL2016,CDsNCFL2018,CDsPA2012,
                                 CDsPA2014,CDsPA2016,CDsPA2018)) %>% dplyr::select(.,-c("length","last"))

CDsMatch$closest1 = NA
CDsMatch$closest2 = NA
CDsMatch$closest3 = NA
CDsMatch$closest4 = NA
CDsMatch$closest5 = NA
CDsMatch$closest6 = NA
CDsMatch$closest7 = NA
CDsMatch$closest8 = NA
CDsMatch$closest9 = NA
CDsMatch$closest10 = NA

CDsMatch$dist1 = 10000
CDsMatch$dist2 = 10000
CDsMatch$dist3 = 10000
CDsMatch$dist4 = 10000
CDsMatch$dist5 = 10000
CDsMatch$dist6 = 10000
CDsMatch$dist7 = 10000
CDsMatch$dist8 = 10000
CDsMatch$dist9 = 10000
CDsMatch$dist10 = 10000

CDsMatch$State = as.character(CDsMatch$State)

for(i in seq(from=1,to=dim(CDsMatch)[1],by=1)){
  for(j in seq(from=1,to=dim(CDsMatch)[1],by=1)){
    if(CDsMatch[i,3]!=CDsMatch[j,3]){
      if(CDsMatch[i,12]==CDsMatch[j,12]){
        if(CDsMatch[i,11]==CDsMatch[j,11]){
          allowed = TRUE
          if(CDsMatch[j,12]==2014&CDsMatch[j,1] %in% c("FL","NC")){
            allowed = FALSE
          }
          if(CDsMatch[j,12]==2016&CDsMatch[j,1]=="PA"){
            allowed = FALSE
          }
          
          if(allowed==TRUE){
            dist = 0
            for(k in seq(from=4,to=10,by=1)){
              dist = dist + abs(CDsMatch[i,k] - CDsMatch[j,k])^2
            }
            
            if(dist<CDsMatch[i,23]){
              CDsMatch[i,22] = CDsMatch[i,21]
              CDsMatch[i,21] = CDsMatch[i,20]
              CDsMatch[i,20] = CDsMatch[i,19]
              CDsMatch[i,19] = CDsMatch[i,18]
              CDsMatch[i,18] = CDsMatch[i,17]
              CDsMatch[i,17] = CDsMatch[i,16]
              CDsMatch[i,16] = CDsMatch[i,15]
              CDsMatch[i,15] = CDsMatch[i,14]
              CDsMatch[i,14] = CDsMatch[i,13]
              CDsMatch[i,13] = CDsMatch[j,3]
              
              CDsMatch[i,32] = CDsMatch[i,31]
              CDsMatch[i,31] = CDsMatch[i,30]
              CDsMatch[i,30] = CDsMatch[i,29]
              CDsMatch[i,29] = CDsMatch[i,28]
              CDsMatch[i,28] = CDsMatch[i,27]
              CDsMatch[i,27] = CDsMatch[i,26]
              CDsMatch[i,26] = CDsMatch[i,25]
              CDsMatch[i,25] = CDsMatch[i,24]
              CDsMatch[i,24] = CDsMatch[i,23]
              CDsMatch[i,23] = dist
            } else if(dist<CDsMatch[i,24]){
              CDsMatch[i,22] = CDsMatch[i,21]
              CDsMatch[i,21] = CDsMatch[i,20]
              CDsMatch[i,20] = CDsMatch[i,19]
              CDsMatch[i,19] = CDsMatch[i,18]
              CDsMatch[i,18] = CDsMatch[i,17]
              CDsMatch[i,17] = CDsMatch[i,16]
              CDsMatch[i,16] = CDsMatch[i,15]
              CDsMatch[i,15] = CDsMatch[i,14]
              CDsMatch[i,14] = CDsMatch[j,3]
              
              CDsMatch[i,32] = CDsMatch[i,31]
              CDsMatch[i,31] = CDsMatch[i,30]
              CDsMatch[i,30] = CDsMatch[i,29]
              CDsMatch[i,29] = CDsMatch[i,28]
              CDsMatch[i,28] = CDsMatch[i,27]
              CDsMatch[i,27] = CDsMatch[i,26]
              CDsMatch[i,26] = CDsMatch[i,25]
              CDsMatch[i,25] = CDsMatch[i,24]
              CDsMatch[i,24] = dist
            } else if(dist<CDsMatch[i,25]){
              CDsMatch[i,22] = CDsMatch[i,21]
              CDsMatch[i,21] = CDsMatch[i,20]
              CDsMatch[i,20] = CDsMatch[i,19]
              CDsMatch[i,19] = CDsMatch[i,18]
              CDsMatch[i,18] = CDsMatch[i,17]
              CDsMatch[i,17] = CDsMatch[i,16]
              CDsMatch[i,16] = CDsMatch[i,15]
              CDsMatch[i,15] = CDsMatch[j,3]
              
              CDsMatch[i,32] = CDsMatch[i,31]
              CDsMatch[i,31] = CDsMatch[i,30]
              CDsMatch[i,30] = CDsMatch[i,29]
              CDsMatch[i,29] = CDsMatch[i,28]
              CDsMatch[i,28] = CDsMatch[i,27]
              CDsMatch[i,27] = CDsMatch[i,26]
              CDsMatch[i,26] = CDsMatch[i,25]
              CDsMatch[i,25] = dist
            } else if(dist<CDsMatch[i,26]){
              CDsMatch[i,22] = CDsMatch[i,21]
              CDsMatch[i,21] = CDsMatch[i,20]
              CDsMatch[i,20] = CDsMatch[i,19]
              CDsMatch[i,19] = CDsMatch[i,18]
              CDsMatch[i,18] = CDsMatch[i,17]
              CDsMatch[i,17] = CDsMatch[i,16]
              CDsMatch[i,16] = CDsMatch[j,3]
              
              CDsMatch[i,32] = CDsMatch[i,31]
              CDsMatch[i,31] = CDsMatch[i,30]
              CDsMatch[i,30] = CDsMatch[i,29]
              CDsMatch[i,29] = CDsMatch[i,28]
              CDsMatch[i,28] = CDsMatch[i,27]
              CDsMatch[i,27] = CDsMatch[i,26]
              CDsMatch[i,26] = dist
            } else if(dist<CDsMatch[i,27]){
              CDsMatch[i,22] = CDsMatch[i,21]
              CDsMatch[i,21] = CDsMatch[i,20]
              CDsMatch[i,20] = CDsMatch[i,19]
              CDsMatch[i,19] = CDsMatch[i,18]
              CDsMatch[i,18] = CDsMatch[i,17]
              CDsMatch[i,17] = CDsMatch[j,3]
              
              CDsMatch[i,32] = CDsMatch[i,31]
              CDsMatch[i,31] = CDsMatch[i,30]
              CDsMatch[i,30] = CDsMatch[i,29]
              CDsMatch[i,29] = CDsMatch[i,28]
              CDsMatch[i,28] = CDsMatch[i,27]
              CDsMatch[i,27] = dist
            } else if(dist<CDsMatch[i,28]){
              CDsMatch[i,22] = CDsMatch[i,21]
              CDsMatch[i,21] = CDsMatch[i,20]
              CDsMatch[i,20] = CDsMatch[i,19]
              CDsMatch[i,19] = CDsMatch[i,18]
              CDsMatch[i,18] = CDsMatch[j,3]
              
              CDsMatch[i,32] = CDsMatch[i,31]
              CDsMatch[i,31] = CDsMatch[i,30]
              CDsMatch[i,30] = CDsMatch[i,29]
              CDsMatch[i,29] = CDsMatch[i,28]
              CDsMatch[i,28] = dist
            } else if(dist<CDsMatch[i,29]){
              CDsMatch[i,22] = CDsMatch[i,21]
              CDsMatch[i,21] = CDsMatch[i,20]
              CDsMatch[i,20] = CDsMatch[i,19]
              CDsMatch[i,19] = CDsMatch[j,3]
              
              CDsMatch[i,32] = CDsMatch[i,31]
              CDsMatch[i,31] = CDsMatch[i,30]
              CDsMatch[i,30] = CDsMatch[i,29]
              CDsMatch[i,29] = dist
            } else if(dist<CDsMatch[i,30]){
              CDsMatch[i,22] = CDsMatch[i,21]
              CDsMatch[i,21] = CDsMatch[i,20]
              CDsMatch[i,20] = CDsMatch[j,3]
              
              CDsMatch[i,32] = CDsMatch[i,31]
              CDsMatch[i,31] = CDsMatch[i,30]
              CDsMatch[i,30] = dist
            } else if(dist<CDsMatch[i,31]){
              CDsMatch[i,22] = CDsMatch[i,21]
              CDsMatch[i,21] = CDsMatch[j,3]
              
              CDsMatch[i,32] = CDsMatch[i,31]
              CDsMatch[i,31] = dist
            } else if(dist<CDsMatch[i,32]){
              CDsMatch[i,22] = CDsMatch[j,3]
              
              CDsMatch[i,32] = dist
            }
          }
        }
      }
    }
  }
}

#Write Closests CDs
write.csv(CDsMatch,file="CDsMatch.csv",row.names = F)
