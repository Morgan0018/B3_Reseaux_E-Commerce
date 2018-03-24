#ACP
data <- read.table("Eaux1.txt", h=TRUE, sep="	", row.names=7)
data
summary(data)
acp <- PCA(data)
acp
acp$eig
barplot(acp$eig[,2], names=paste("d",1:nrow(acp$eig)))
plot(acp, select="cos2 0.7")
plot(acp, choix="var", select="cos2 0.7")

#AFC
#Exercice 1
data <- read.table("canabistan.csv", h=TRUE, sep=";", row.names=1)
dataAFC <- data[1:5,]
dataAFC
summary(dataAFC)
afc <- CA(dataAFC, col.sup=5)
afc
afc$eig
chi2 <- chisq.test(dataAFC)
chi2
chi2$residuals^2/chi2$stat*100

#exercice 2 (mais AFC)
data <- read.table("etude-agro-mais.csv", h=TRUE, sep=";", row.names=1)
summary(data)
tabcont <- xtabs(~Couleur+Enracinement, data)
tabcont
summary(tabcont)
afc <- CA(tabcont)
afc
afc$eig
chi2 <- chisq.test(tabcont)
chi2
chi2$residuals^2/chi2$stat*100

#ACM
var <- c("", "niveau_age", "pathologie", "astigmatisme", "larmes", "type")
mod <- c("young", "pre-presbyopic", "presbyopic", "myope", "hypermetrope", "no", "yes", "reduced", "normal", "rigides", "souples", "déconseilées")
data <- read.table("lentilles.csv", h=F, sep=";", row.names=1, col.names=var, colClasses=c(rep("factor", 6)))
summary(data)
td <- tab.disjonctif(data)
colnames(td) <- mod
td
acm <- MCA(data)
acm$eig
acm$var$cos2[,1:2]
plot(acm)

acm2 <- MCA(data, quali.sup=5)

#mais
data <- read.table("etude-agro-mais.csv", h=TRUE, sep=";", row.names=1)
quali <- c("Couleur", "Germination.epi", "Enracinement", "Verse", "Attaque", "Parcelle", "Verse.Traitement")
#ACM
dataACM <- subset(data, select=quali)
summary(dataACM)
dataNA <- na.omit(dataACM)
td <- tab.disjonctif(dataNA)
acm <- MCA(dataNA)
acm$eig
acm$var$cos2[,1:2]
plot(acm)

#ACP
dataACP <- data[1:13]
acp <- PCA(dataACP, quali.sup=c(5:10,12))
acp$eig
barplot(acp$eig[,2], names=paste("d",1:nrow(acp$eig)))
plot(acp, select="cos2 0.7")
plot(acp, choix="var", select="cos2 0.7")
