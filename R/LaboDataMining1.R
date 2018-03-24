#Prep
tab <- read.table("LaboDataMining1.csv", h=TRUE, sep=";")
names(tab)
summary(tab)

#Graph
boxplot(Mesure~Traitement, data=tab)

#Treatment
model <- lm(Mesure~Traitement, data=tab)
model
an <- anova(model)
an
an$"F value"[1]
an$"Pr(>F)"[1]
sum <- summary(model)
sum
sum$coefficients[,4]
names(sum$coefficients[,4])

detach(tab)
