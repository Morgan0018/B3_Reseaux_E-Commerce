#include "SocketsUtilities.h"

char * host;
int port;
char * hostCheckIn;
int portCheckIn;
char *csvSep;
char *msgSep;

//Config
void getConf()
{
	FILE *fp;
	char buffer[500], *key, *value;

	if ((fp = fopen(CONFIGFILE, "rt")) == NULL)
		printf("Erreur d'ouverture du fichier de configuration.\n");
	else
	{
		while (fgets(buffer, 500, fp) != NULL)
		{
			printf("Ligne récupérée : %s\n", buffer);
			key = strtok(buffer, "=");
			value = strtok(NULL, "\n");
			if (strcmp("HOST", key) == 0) {
				host = (char*)malloc(sizeof(value));
				strcpy(host, value);	
			}
			else if (strcmp("PORT", key) == 0) port = atoi(value);
			else if (strcmp("HOST_CHECKIN", key) == 0) {
				hostCheckIn = (char*)malloc(sizeof(value));
				strcpy(hostCheckIn, value);
			}
			else if (strcmp("PORT_CHECKIN", key) == 0) portCheckIn = atoi(value);
			else if (strcmp("CSV_SEP", key) == 0) {
				csvSep = (char*)malloc(sizeof(value));
				strcpy(csvSep, value);
			}
			else if (strcmp("MSG_SEP", key) == 0) {
				msgSep = (char*)malloc(sizeof(value));
				strcpy(msgSep, value);
			}
			else printf("Clé de configuration inconnue (%s).\n", key);
		}
		fclose(fp);
	}
}

//Sockets Set-up
int initSocket()
{
	int hSocket = socket(AF_INET, SOCK_STREAM, 0);
	
	if(hSocket == -1)
	{
		printf("Erreur de creation de la socket %d\n", errno);
		exit(1);
	}
	else
	{
		printf("Creation de la socket %d OK\n", hSocket);
		return hSocket;
	}
}

void getHostInfos(struct hostent ** infosHost)
{
	struct in_addr addressIP;
	
	if (((*infosHost) = gethostbyname(host)) == 0)
	{
		printf("Erreur d'acquisition d'infos sur le host %d\n", errno);
		exit(1);
	} else printf("Acquisition infos host OK.\n");
	memcpy(&addressIP, (*infosHost)->h_addr, (*infosHost)->h_length);
	printf("Adresse IP = %s \n", inet_ntoa(addressIP));	
}

void prepStructSockAddr_in(struct sockaddr_in *addressSocket, struct hostent **infosHost)
{
	memset(addressSocket, 0, sizeof(struct sockaddr_in));
	(*addressSocket).sin_family = AF_INET;
	(*addressSocket).sin_port = htons(port);
	memcpy(&(addressSocket->sin_addr), (*infosHost)->h_addr, (*infosHost)->h_length);
}

//Receive data
int getMTU(int hSocket)
{
	int sizeS;
	unsigned int sizeO = sizeof(int);
	if (getsockopt(hSocket, IPPROTO_TCP, TCP_MAXSEG, &sizeS, &sizeO) == -1)
	{
		printf("Erreur getsockopt socket %d \n", errno);
		close(hSocket);
		exit(1);
	} else printf("getsockopt OK \t Taille max segment = %d \n", sizeS);
	return sizeS;
}

void receiveDataFixedLength(/*TODO*/)
{
	//TODO
}

int receiveDataVariableLength(int hSocket, int sizeS, char *msg)
{
	int sizeMsgRecv = 0, endDetected = 0, nbBytesRecv;
	char recvBuf[sizeS], tmpMsg[MAX_LONG_MSG];

	do //receive loop
	{
		if ((nbBytesRecv = recv(hSocket, recvBuf, sizeS, 0)) == -1)
		{
			printf("Erreur sur le recv de la socket connectee : %d", errno);
			close (hSocket); 
			exit(1);
		}

		endDetected = marqueurRecu(recvBuf, nbBytesRecv);
		memcpy((char*)tmpMsg + sizeMsgRecv, recvBuf, nbBytesRecv);
		sizeMsgRecv += nbBytesRecv;

		tmpMsg[sizeMsgRecv+1] = '\0'; //terminate the string
		//printf("Receive loop - Message reçu = ---%s---\n", tmpMsg);
	} while (nbBytesRecv != 0 && nbBytesRecv != -1 && !endDetected);

	if (sizeMsgRecv > 0)
	{
		tmpMsg[sizeMsgRecv-3] = '\0'; // replace \r (of \r\n) by 0 => terminate string
		printf("Message recu complet : ---%s---\n", tmpMsg);
		strcpy(msg, tmpMsg);
		//printf("Message copié : ---%s---\n", msg);
	}
	return sizeMsgRecv;
}

//Send data
int sendMsg(int hSocket, const char *msg)
{
	if (send(hSocket, msg, strlen(msg)+1, 0) == -1)
	{
		printf("Erreur sur le send : code %d\n", errno);
		close(hSocket);
		return -1;
	} else printf("Send OK.\n");
	return 0;
}

//Autres
void addEndChar(char *msg)
{
	int sizeMsg = strlen(msg);
	msg[sizeMsg] = '\r';
	msg[sizeMsg+1] = '\n';
	msg[sizeMsg+2] = '\0';
}

char marqueurRecu(char * m, int n) //look for \r\n sequence
{
	static char demiTrouve = 0;
	int i;
	char trouve = 0;
	
	if(demiTrouve == 1 && m[0] == '\n') return 1;
	else demiTrouve = 0;
	
	for(i = 0; i < n-1 && !trouve; i++) if(m[i] == '\r' && m[i+1] == '\n') trouve = 1;
	
	if(trouve) return 1;
	else if(m[n] == '\r') demiTrouve = 1;
	
	return 0;
}
