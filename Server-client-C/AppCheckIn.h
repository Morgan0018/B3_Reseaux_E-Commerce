#ifndef APP_CHECK_IN_H
#define APP_CHECK_IN_H

#include "SocketsUtilities.h"

int hSocket, sizeS;

short LireChaine(char *pc, short max);
int sendMsgAndReceiveServerResponse(char *msgToSend, char *msgServer);
	//returns -1 if error; 0 if OK;

//Protocol handling
int login(); //returns 1 if login OK; 0 if bad login; -1 if error;
int getFlightInfos(char *flightInfos); //returns -1 if error; 1 if OK; 0 if not OK;
int checkTicket(); //returns -1 if error; 0 if not OK; 1 if OK;
int checkLuggage(); //returns -1 if error; 0 if not OK; 1 if OK;
void logout();

#endif //APP_CHECK_IN_H
