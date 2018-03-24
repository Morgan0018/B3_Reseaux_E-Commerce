#include "AccessServerBag.h"

BAG_LINE *listBags = NULL;
int hSocketCheckIn, sizeS;

void connectToServerCheckIn()
{
	int ret;
	struct hostent *infosHost;
	struct in_addr addressIP;
	struct sockaddr_in addressSocket;
	unsigned int sizeSockaddr_in = sizeof(struct sockaddr_in);

	//create client socket for server check-in
	hSocketCheckIn = initSocket();
	if( (infosHost = gethostbyname(hostCheckIn) ) == 0) {
		printf("Erreur d'acquisition d'infos sur le host distant %d\n", errno);
		exit(1);
	} else printf("Acquisition infos host OK \n");
	memcpy(&addressIP, infosHost->h_addr, infosHost->h_length);
	printf("Adresse IP = %s \n", inet_ntoa(addressIP));
	// prep sockaddr_in
	memset(&addressSocket, 0, sizeof(struct sockaddr_in));
	addressSocket.sin_family = AF_INET;
	addressSocket.sin_port = htons(portCheckIn);
	memcpy(&addressSocket.sin_addr, infosHost->h_addr, infosHost->h_length);

	if ((ret = connect(hSocketCheckIn, (struct sockaddr *)&addressSocket,
		 sizeSockaddr_in)) == -1) {
		printf("Erreur de connexion au serveur ! (code : %d)\n", errno);
		close(hSocketCheckIn);
		exit(1);
	} else printf("Connexion au serveur OK\n");
	
	sizeS = getMTU(hSocketCheckIn);
}

int checkLoginInfo(char* login, char* pwd)
{
	int response = 0;
	char toSend[MAX_LONG_MSG], rcv[MAX_LONG_MSG], *tok;
	
	sprintf(toSend, "%d%c%s%c%s", LOGIN_OFFICER, '$', login, '#', pwd);
	int s = strlen(toSend);
	toSend[s] = '\n';
	toSend[s+1] = '\0';
	sendMsg(hSocketCheckIn, toSend);

	receiveDataVariableLength(hSocketCheckIn, sizeS, rcv);
	tok = strtok(rcv, "$");
	printf("Reponse : %s \n", tok);
	printf("\tinfos : %s \n", strtok(NULL, "#") );

	int code = atoi(tok);
	if (code == LOGIN_OK) response = 1;
	return response;
}

char* checkFlightInfos()
{
	char toSend[MAX_LONG_MSG], rcv[MAX_LONG_MSG], *tok, *flightInfos;

	sprintf(toSend, "%d%c%s", FLIGHT_INFO, '$', "NULL");
	int s = strlen(toSend);
	toSend[s] = '\n';
	toSend[s+1] = '\0';
	sendMsg(hSocketCheckIn, toSend);

	receiveDataVariableLength(hSocketCheckIn, sizeS, rcv);
	tok = strtok(rcv, "$");
	printf("Reponse : %s \n", tok);
	flightInfos = strtok(NULL, "#");
	printf("\tinfos : %s \n", flightInfos);

	return flightInfos;
	//return "VOL 362 POWDER-AIRLINES - Peshawar 6h30";
}

int verifyTicket(char *numBillet, int nbAccomp, char *passengerId)
{
	int response = 0;
	char toSend[MAX_LONG_MSG], rcv[MAX_LONG_MSG], *tok;

	sprintf(toSend, "%d%c%s%c%d", CHECK_TICKETS, '$', numBillet, '#', nbAccomp);
	int s = strlen(toSend);
	toSend[s] = '\n';
	toSend[s+1] = '\0';
	printf("Debug : %s\n", toSend);
	sendMsg(hSocketCheckIn, toSend);

	receiveDataVariableLength(hSocketCheckIn, sizeS, rcv);
	tok = strtok(rcv, "$");
	printf("Reponse : %s \n", tok);
	passengerId = strtok(NULL, "#");
	printf("\tinfos : %s \n", passengerId);

	int code = atoi(tok);
	if (code == CHECK_TICKETS_OK) response = 1;
	return response;
}

int addLuggage(char *numBillet, char *passengerId, char *bagString, 
				int *nbBag, float *totalWeight)
{
	char *bagId = (char*)malloc(200), *token, *bagRef = (char*)malloc(200);
	float weight;
	BAG_LINE *tmp;

	token = strtok(numBillet, "-");
	sprintf(bagId, "%s-%s-%s-", token, passengerId, strtok(NULL, "\n"));

	listBags = (BAG_LINE *)malloc(sizeof(BAG_LINE));
	tmp = listBags;

	*nbBag = 0;
	*totalWeight = 0.0;
	token = strtok(bagString, msgSep);
	do
	{
		//bag weight
		strcpy(tmp->weight, token);
		weight = atof(token);
		(*totalWeight) += weight;
		//bag Id
		(*nbBag)++;
		if (*nbBag < 10) sprintf(bagRef, "%s00%d", bagId, *nbBag);
		else sprintf(bagRef, "%s0%d", bagId, *nbBag);
		tmp->ref = (char*)malloc(100);
		strcpy(tmp->ref, bagRef);		
		//bag type
		token = strtok(NULL, msgSep);
		tmp->type = (char*)malloc(20);
		if (strcmp(token, "Y") == 0 || strcmp(token, "O") == 0 
			|| strcmp(token, "y") == 0 || strcmp(token, "o") == 0)
			strcpy(tmp->type, "VALISE");
		else strcpy(tmp->type, "PASVALISE");
		//next bag
		if ((token = strtok(NULL, msgSep)) == NULL) tmp->next = NULL;
		else
		{
			tmp->next = (BAG_LINE *)malloc(sizeof(BAG_LINE));
			tmp = tmp->next;
		}
	} while (token != NULL);
	return 1;
}

int saveLuggage(char *numBillet)
{
	BAG_LINE *tmp;
	int response = 0;
	//int hSocketCheckIn = connectToServerCheckIn();
	//int sizeS = getMTU(hSocketCheckIn);
	char toSend[MAX_LONG_MSG], rcv[MAX_LONG_MSG], *tok;
	
	if (listBags == NULL) return 0;
	tmp = listBags;

	sprintf(toSend, "%d%c%s%c", SAVE_LUGGAGE, '$', numBillet, '#');
	while (tmp != NULL)
	{
		sprintf(toSend, "%s%s%c%s%c%s%c", toSend, tmp->ref, '@',
					tmp->type, '@', tmp->weight, '#');
		tmp = tmp->next;
	}
	printf("Debug : %s\n", toSend);
	int s = strlen(toSend);
	toSend[s] = '\n';
	toSend[s+1] = '\0';
	printf("Debug : %s\n", toSend);
	sendMsg(hSocketCheckIn, toSend);

	receiveDataVariableLength(hSocketCheckIn, sizeS, rcv);
	tok = strtok(rcv, "$");
	printf("Reponse : %s \n", tok);
	printf("\tinfos : %s \n", strtok(NULL, "#") );

	int code = atoi(tok);
	if (code == SAVE_LUGGAGE_OK) response = 1;
	listBags = NULL;
	return response;
}
