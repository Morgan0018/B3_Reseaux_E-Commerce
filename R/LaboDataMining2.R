#Prep
tab <- read.table("LaboDataMining2.csv", h=TRUE, sep=";")
names(tab)
summary(tab)

#Graph
with(tab, interaction.plot(molecule, mode, coeficient))
with(tab, interaction.plot(mode, molecule, coeficient))

#Treatment
model <- lm(coeficient~mode*molecule, data=tab)
model
an <- anova(model)
an
an[1:3,4]
an[1:3,5]
an[3,5] #interaction

#if > 5%
modelsi <- lm(coeficient~mode+molecule)
modelsi
ansi <- anova(modelsi)
ansi
pvalues <- ansi$"Pr(>F)"
pvalues

sapply(tab, levels)$"mode"
sapply(tab, levels)$"molecule"

sub <- subset(tab, molecule=="AlphaGrosseTete")
sub
mean(subset(sub, mode=="voie orale")$"coeficient")
mean(subset(sub, mode=="injection")$"coeficient")
