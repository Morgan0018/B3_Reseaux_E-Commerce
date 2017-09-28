#ifndef SOCKETS_UTILITIES_H
#define SOCKETS_UTILITIES_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#include <unistd.h>
#include <sys/types.h>
#include <errno.h>

#ifdef SocketsUnix
	//pour les sockets
	#include <sys/socket.h>
	#include <netdb.h>

	// pour les conversion d'adresse et de format
	#include <netinet/in.h>
	#include <netinet/tcp.h>
	#include <arpa/inet.h>
#endif // SocketsUnix

#define MAX_LONG_MSG	5000

//constantes du protocole
#define LOGIN_OFFICER	1
#define LOGOUT_OFFICER	2
#define FLIGHT_INFOS	3
#define CHECK_TICKET	4
#define CHECK_LUGGAGE	5
#define PAYMENT_DONE	6

#define EOC		"END_OF_CONNEXION"
#define DOC		"DENY_OF_CONNEXION"

//Config
#define CONFIGFILE	"server_checkin.conf"
extern char * host;
extern int port;
extern char *csvSep;
extern char *msgSep;
void getConf();

//mise en place des sockets
int initSocket();
void getHostInfos(struct hostent ** infosHost);
void prepStructSockAddr_in(struct sockaddr_in *addressSocket, struct hostent **infosHost);

//Receive data
int getMTU(int hSocket);
void receiveDataFixedLength(); //TODO
int receiveDataVariableLength(int hSocket, int sizeS, char *msg); 
	//returns sizeMsgRecv

//Send data
int sendMsg(int hSocket, const char *msg); 
	//returns -1 & close socket if error on send; 0 if OK;

//Autres
void addEndChar(char *msg);
char marqueurRecu(char *, int);

#endif //SOCKETS_UTILITIES_H
