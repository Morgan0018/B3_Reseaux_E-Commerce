#exercice 1
#tableau de contingence
tabcont <- read.table("taches_menageres.txt", h=TRUE, sep="	")
summary(tabcont)
#graphique
tab <- t(tabcont)
par(mfrow=c(4,1))
barplot(tab[1,], main="Wife")
barplot(tab[2,], main="Alternating")
barplot(tab[3,], main="Husband")
barplot(tab[4,], main="Jointly")
#test chi-carré
chi2 <- chisq.test(tabcont)
round(chi2$residuals^2/chi2$stat*100, 1)

#exercice 2
#tableau de contingence
data <- read.table("salaires.txt", h=TRUE, sep="", colClasses=c(rep("numeric", 5), rep("factor", 2) ) )
tabcont <- xtabs(~minority+sex, data)
tabcont
summary(tabcont)
#graphique
par(mfrow=c(2,1))
barplot(tabcont[1,], main="Homme")
barplot(tabcont[2,], main="Femme")
#test chi-carré
chi2 <- chisq.test(tabcont)
round(chi2$residuals^2/chi2$stat*100, 1)
