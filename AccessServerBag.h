#ifndef ACCESS_SERVER_BAG_H
#define ACCESS_SERVER_BAG_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "SocketsUtilities.h"

#define LOGOUT			0
#define LOGIN			1
#define FLIGHT_INFO		2
#define CHECK_TICKETS	3
#define SAVE_LUGGAGE	4

#define LOGOUT_OK			200
#define LOGIN_OK			201
#define FLIGHT_INFO_OK		202
#define CHECK_TICKETS_OK	203
#define SAVE_LUGGAGE_OK		204

#define LOGOUT_FAIL			400
#define LOGIN_FAIL			401
#define FLIGHT_INFO_FAIL	402
#define CHECK_TICKETS_FAIL	403
#define SAVE_LUGGAGE_FAIL	404

typedef struct _BAG_LINE
{
	char *ref;
	char *type;
	char *weight;
	struct _BAG_LINE *next;
} BAG_LINE;
extern BAG_LINE *listBags;

extern int hSocketCheckIn, sizeS;

void connectToServerCheckIn();

int checkLoginInfo(char* login, char* pwd); //returns 1 if login/pwd is OK
char* checkFlightInfos();
int verifyTicket(char *numBillet, int nbAccomp, char *passengerId);
	//returns 0 if numBillet not found; 2 if nbAccomp not OK; 1 if OK;
int addLuggage(char *numBillet, char *passengerId, char *bagString, 
				int *nbBag, float *totalWeight);
	//returns 1 if OK; 0 if not OK;
int saveLuggage(char *numBillet); //returns 1 if OK; 0 if not OK;

#endif //ACCESS_SERVER_BAG_H
