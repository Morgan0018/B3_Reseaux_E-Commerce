#include "ServerCheckIn.h"
#include "Threads.c"

//Sockets
void closeAllSockets()
{
	int *pSocConnected;
	
	if(*(tabSockets[0]) != -1) close(*(tabSockets[0]));
	
	if(*(tabSockets[1]) != -1) close(*(tabSockets[1]));
	
	pSocConnected = tabSockets[2];
	for(short i = 0 ; i < MAXTHREAD ; i++, pSocConnected++)
	{
		if(*pSocConnected != -1) close(*pSocConnected);
	}
}

int main(int argc, char const *argv[])
{
	//Variables
	int hSocketEcoute, hSocketService;
	struct hostent *infosHost;
	struct sockaddr_in addressSocket;
	int i, j, sizeS;
	unsigned int sizeSockaddr_in = sizeof(struct sockaddr_in);
	//char msgServer[MAX_LONG_MSG];

	//Init
	getConf();
	pthread_mutex_init(&mutexIndiceCourant, NULL);
	pthread_cond_init(&condIndiceCourant, NULL);

	tabSockets[0] = &hSocketEcoute;
	tabSockets[1] = &hSocketService;
	tabSockets[2] = &tabSocketConnected[0];
	for(i = 0; i < MAXTHREAD; i++) tabSocketConnected[i] = -1;
	
	connectToServerCheckIn(); //Connect to Java server for database access

	hSocketEcoute = initSocket(); //Create Socket
	getHostInfos(&infosHost); //Get machine info
	prepStructSockAddr_in(&addressSocket, &infosHost); //prep Socket

	//bind Socket
	if (bind(hSocketEcoute, (struct sockaddr *)&addressSocket, 
		sizeof(struct sockaddr_in)) == -1)
	{
		printf("Erreur sur le bind de la Socket d'écoute %d : code d'erreur %d\n", 
			hSocketEcoute, errno);
		exit(1);
	} else printf("Bind adresse et port socket d'ecoute %d OK\n", hSocketEcoute);

	sizeS = getMTU(hSocketEcoute); //find MTU (for variable length strings)

	//Create threads
	for (i = 0; i < MAXTHREAD; ++i)
	{
		pthread_create(&tabThreadHandle[i], NULL, (void* (*)(void*))fctThread, &sizeS);
		printf("Thread secondaire %d lancé.\n", i);
		pthread_detach(tabThreadHandle[i]);
	}

	while (1) //client connexion loop
	{
		if (listen(hSocketEcoute, SOMAXCONN) == -1)
		{
			printf("Erreur sur le listen de la socket %d\n", errno);
			closeAllSockets();
			exit(1);
		} else printf("Listen socket OK.\n");

		if ((hSocketService = accept(hSocketEcoute, 
			(struct sockaddr*)&addressSocket, &sizeSockaddr_in)) == -1)
		{
			printf("Erreur sur le accept de la socket %d : code d'erreur %d\n", hSocketEcoute, errno);
			closeAllSockets();
			exit(1);
		} else printf("Accept socket OK \n");

		//find free service socket
		for (j = 0; j < MAXTHREAD && tabSocketConnected[j] != -1; j++);
		if (j == MAXTHREAD)
		{
			printf("Plus de connexion disponible.\n");
			if (sendMsg(hSocketService, DOC) == -1)
			{
				closeAllSockets();
				exit(1);
			}
			else close(hSocketService);
		}
		else // free connexion available
		{
			printf("Connexion sur la socket %d\n", j);
			pthread_mutex_lock(&mutexIndiceCourant);
			tabSocketConnected[j] = hSocketService;
			indiceCourant = j;

			pthread_mutex_unlock(&mutexIndiceCourant);
			pthread_cond_signal(&condIndiceCourant);
		}
	} //end while

	//close socket
	close(hSocketEcoute);
	printf("Socket serveur fermee\n");
	
	printf("Fin du thread principal\n");
	exit(0);
}
