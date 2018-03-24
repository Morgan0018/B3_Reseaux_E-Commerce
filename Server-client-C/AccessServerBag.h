#ifndef ACCESS_SERVER_BAG_H
#define ACCESS_SERVER_BAG_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "SocketsUtilities.h"
#include "CSVLibrary.h"

#define LOGIN	"login.csv"
#define TICKETS	"tickets.csv"

typedef struct _BAG_LINE
{
	char *ref;
	char *type;
	struct _BAG_LINE *next;
} BAG_LINE;
extern BAG_LINE *listBags;

int checkLoginInfo(char* login, char* pwd); //returns 1 if login/pwd is OK
char* checkFlightInfos();
int verifyTicket(char *numBillet, int nbAccomp, char *passengerId);
	//returns 0 if numBillet not found; 2 if nbAccomp not OK; 1 if OK;
int addLuggage(char *numBillet, char *passengerId, char *bagString, 
				int *nbBag, float *totalWeight);
	//returns 1 if OK; 0 if not OK;
int saveLuggage(char *numBillet); //returns 1 if OK; 0 if not OK;

#endif //ACCESS_SERVER_BAG_H
