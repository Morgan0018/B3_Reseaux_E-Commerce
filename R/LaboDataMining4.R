#prep
tab <- read.table("RdtFromage.txt", h=TRUE, sep="	")
summary(tab)

#MAT
model <- lm(RFESC~MAT, data=tab)
model

#graph
plot(RFESC~MAT, data=tab)
abline(model)

subset(tab, select=c("RFESC", "MAT"))

#treatment
sum <- summary(model)
sum
sqrt(sum$adj.r.squared)
sum$coefficients[2,4]

#CNE
model <- lm(RFESC~CNE, data=tab)
model

#graph
plot(RFESC~CNE, data=tab)
abline(model)

#treatement
sum <- summary(model)
sum
sqrt(sum$adj.r.squared)
sum$coefficients[2,4]

#NPN
model <- lm(RFESC~NPN, data=tab)
model

#graph
plot(RFESC~NPN, data=tab)
abline(model)

#treatment
sum <- summary(model)
sum
sqrt(abs(sum$adj.r.squared))
sum$coefficients[2,4]
