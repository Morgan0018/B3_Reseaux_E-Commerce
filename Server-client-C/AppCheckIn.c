#include "AppCheckIn.h"

int main()
{
	int ret;
	unsigned int sizeSockaddr_in = sizeof(struct sockaddr_in);
	struct hostent *infosHost;
	struct sockaddr_in addressSocket;
	char *flightInfos = (char*)malloc(1000), c;
	
	getConf();
	hSocket = initSocket(); //Create socket
	getHostInfos(&infosHost); //Get machine info
	prepStructSockAddr_in(&addressSocket, &infosHost); //prep Socket
	
	//Try to connect
	if (connect(hSocket, (struct sockaddr *)&addressSocket, sizeSockaddr_in) == -1)
	{
		printf("Erreur de connexion au serveur ! (code : %d)\n", errno);
		close(hSocket);
		exit(1);
	} else printf("Connexion au serveur OK\n");
	
	sizeS = getMTU(hSocket);

	do //Login loop
	{
		ret = login();
		if (ret == 0) printf("Mauvais login ou mot de passe.\n");
		else if (ret == -1)
		{
			printf("Probleme avec le serveur\n");
			exit(1);
		} else printf("Login OK\n");
	} while (ret == 0);
	
	//Show flight infos
	ret = getFlightInfos(flightInfos);
	if (ret == -1 || ret == 0) exit(1);
	
	do //CheckIn passengers loop
	{
		printf("---%s---\n", flightInfos);

		do //Ask for ticket number & number of people
		{
			ret = checkTicket();
			if (ret == -1)
			{
				printf("Probleme avec le serveur\n");
				exit(1);
			}
		} while (ret == 0);

		do //Check luggage & handle payment if needed
		{
			ret = checkLuggage();
			if (ret == -1)
			{
				printf("Probleme avec le serveur\n");
				exit(1);
			}
		} while (ret == 0);

		printf("Passager suivant ? ");
		c = getchar();
	} while (toupper(c) == 'Y' || toupper(c) == 'O'); //CheckIn passengers loop

	logout();
	
	close(hSocket);
	exit(0);
}

short LireChaine(char *pc, short max)
{
	short i = 0;
	char c;
	
	max = max - 1;
	fflush(stdin);
	c = getchar();
	
	while(c != '\n' && i < max)
	{
		*pc = c;
		pc++;
		i++;
		c = getchar();
	}
	
	if(c == '\n')
		*pc = '\0';
	else
		i = -1;
	
	return i;
}

int sendMsgAndReceiveServerResponse(char *msgToSend, char *msgServer)
{
	int sizeMsgRecv;

	//Send Msg
	addEndChar(msgToSend);
	if (sendMsg(hSocket, msgToSend) == -1) return -1;
	//Receive response
	sizeMsgRecv = receiveDataVariableLength(hSocket, sizeS, msgServer);
	if (sizeMsgRecv == 0)
	{
		printf("Probleme serveur\n");
		return -1;
	}
	//printf("debug : ---%s---\n", msgServer);
	if (strcmp(msgServer, DOC) == 0)
	{
		printf("Le serveur est occupé. Revenez plus tard.\n");
		return -1;
	}
	//TODO : handle EOC from server
	return 0;
}

int login()
{
	char *msg = (char*)malloc(MAX_LONG_MSG-3),
		 *buffer = (char*)malloc(MAX_LONG_MSG),
		 msgServer[MAX_LONG_MSG];

	//Make Msg
	sprintf(msg, "%d%c", LOGIN_OFFICER, msgSep[0]);

	printf("Entrez votre nom d'utilisateur : ");
	LireChaine(buffer, MAX_LONG_MSG);
	sprintf(msg, "%s%s%c", msg, buffer, msgSep[0]);

	printf("Entrez votre mot de passe : ");
	LireChaine(buffer, MAX_LONG_MSG);
	sprintf(msg, "%s%s%c", msg, buffer, msgSep[0]);

	if (sendMsgAndReceiveServerResponse(msg, msgServer) == -1) return -1;
	//Handle msg
	if (strcmp(msgServer, "Y") == 0) return 1;
	else return 0;
}

int getFlightInfos(char *flightInfos)
{
	char *msg = (char*)malloc(MAX_LONG_MSG-3),
		 msgServer[MAX_LONG_MSG], *token;

	//Make Msg
	sprintf(msg, "%d%c", FLIGHT_INFOS, msgSep[0]);

	if (sendMsgAndReceiveServerResponse(msg, msgServer) == -1)
		return -1;	
	//Handle msg
	token = strtok(msgServer, msgSep);
	if (strcmp(token, "Y") == 0)
	{
		strcpy(flightInfos, strtok(NULL, "\n"));
		return 1;
	}
	else
	{
		if (strcmp(token, "N") == 0)
			printf("Message du serveur : %s\n", strtok(NULL, msgSep));
		else printf("Code de retour inconnu.\n");
		return 0;
	}
}

int checkTicket()
{
	char *msg = (char*)malloc(MAX_LONG_MSG-3),
		 *buffer = (char*)malloc(MAX_LONG_MSG),
		 msgServer[MAX_LONG_MSG], *token;

	//Make Msg
	sprintf(msg, "%d%c", CHECK_TICKET, msgSep[0]);

	printf("Numéro de billet ? ");
	LireChaine(buffer, MAX_LONG_MSG);
	sprintf(msg, "%s%s%c", msg, buffer, msgSep[0]);

	printf("Nombre d'accompagnant ? ");
	LireChaine(buffer, MAX_LONG_MSG);
	sprintf(msg, "%s%s%c", msg, buffer, msgSep[0]);

	if (sendMsgAndReceiveServerResponse(msg, msgServer) == -1) return -1;
	//Handle msg
	token = strtok(msgServer, msgSep);
	if (strcmp(token, "Y") == 0) return 1;
	else
	{
		printf("Message du serveur : %s\n", strtok(NULL, "\n"));
		return 0;
	}
}

int checkLuggage()
{
	char *msg = (char*)malloc(MAX_LONG_MSG-3),
		 *buffer = (char*)malloc(MAX_LONG_MSG),
		 msgServer[MAX_LONG_MSG], *token, c;

	//Make Msg
	sprintf(msg, "%d%c", CHECK_LUGGAGE, msgSep[0]);
	printf("Poids du bagage n°1 <Enter si fini> : ");
	LireChaine(buffer, MAX_LONG_MSG);
	printf("Debug - buffer : ---%s---\n", buffer);
	int i = 2;
	while (strcmp(buffer, "\0") != 0)
	{
		sprintf(msg, "%s%s%c", msg, buffer, msgSep[0]);
		printf("Valise ? ");
		c = getchar();
		sprintf(msg, "%s%c%c", msg, c, msgSep[0]);
		printf("Poids du bagage n°%d <Enter si fini> : ", i);
		LireChaine(buffer, MAX_LONG_MSG);
		i++;
	}
	printf("Debug : ---%s---\n", msg);
	if (i == 2) return 1;

	if (sendMsgAndReceiveServerResponse(msg, msgServer) == -1) return -1;
	//Handle msg
	token = strtok(msgServer, msgSep);
	if (strcmp(token, "Y") == 0 || strcmp(token, "E") == 0)
	{
		printf("Poids total bagages : %.2f kg\n", atof(strtok(NULL, msgSep)));
		if (strcmp(token, "Y") == 0) return 1;
		else
		{
			printf("Excédent poids : %.2f kg\n", atof(strtok(NULL, msgSep)));
			printf("Supplément à payer : %.2f eur\n", atof(strtok(NULL, msgSep)));
			printf("Paiement effectué ? ");
			c = getchar();

			sprintf(msg, "%d%c", PAYMENT_DONE, msgSep[0]);
			if (sendMsgAndReceiveServerResponse(msg, msgServer) == -1) return -1;
			//Handle msg
			printf("Debug - msgServer : ---%s---\n", msgServer);
			token = strtok(msgServer, msgSep);
			printf("Debug - token : ---%s---\n", token);
			if (strcmp(token, "Y") == 0) return 1;
		}
	}
	printf("Message du serveur : %s\n", strtok(NULL, msgSep));
	return 0;
}

void logout()
{
	char *msg = (char*)malloc(MAX_LONG_MSG-3), msgServer[MAX_LONG_MSG];

	sprintf(msg, "%d%c", LOGOUT_OFFICER, msgSep[0]);
	if (sendMsgAndReceiveServerResponse(msg, msgServer) == -1)
	{
		printf("Oups\n");
	}
}
