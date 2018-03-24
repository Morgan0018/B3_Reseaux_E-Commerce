#Prep
tab <- read.table("LaboDataMining3.csv", h=TRUE, sep=";")
summary(tab)

#Graph ??
plot(temps~volume, data=tab)
plot(temps~nombre.de.grandes.pièces, data=tab)

#Treatment
model <- lm(temps~., data=tab)
model
sum <- summary(model)
sum
sqrt(sum$adj.r.squared)
pf(sum$fstatistic[1], sum$fstatistic[2], sum$fstatistic[3], lower.tail=FALSE)

# if p-value < 5% (1% ?)
sum$coefficients[1,4] #Pr(>|t|) de l'intercept

# if p-value (intercept) > 1%
modelsi <- lm(temps~.-1, data=tab)
modelsi
sumsi <- summary(modelsi)
sumsi
sqrt(sumsi$adj.r.squared)
pf(sumsi$fstatistic[1], sumsi$fstatistic[2], sumsi$fstatistic[3], lower.tail=FALSE)

sumsi$coefficients[1,4]
# if p-value (volume) > 5%
modelsB1 <- lm(temps~.-1-volume, data=tab) #lm(temps~nombre.de.grandes.pièces-1, data=tab)
modelsB1
sumsB1 <- summary(modelsB1)
sumsB1

sumsi$coefficients[2,4]
# if p-value (nombre.de.grandes.pièces) > 5%
modelsB2 <- lm(temps~volume-1, data=tab)
modelsB2
sumsB2 <- summary(modelsB1)
sumsB2
