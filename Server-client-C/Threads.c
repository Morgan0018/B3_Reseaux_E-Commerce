#include "ServerCheckIn.h"

char * getThreadIdentity()
{
	char *buf = (char*)malloc(30);
	sprintf(buf, "%d.%u", getpid(), pthread_self());
	return buf;
}

short fctThread(int * param)
{
	//Variables
	int hSocketService, sizeS = *param, iCliTraite, endDialog, sizeMsgRecv, 
		action, ret, valide, comPrec = 0;
	char *buffer = (char*)malloc(MAX_LONG_MSG), *numThread = getThreadIdentity();
	char numBillet[20], passengerId[20];
	char msgServer[MAX_LONG_MSG], msgClient[MAX_LONG_MSG];
	char *token = (char*)malloc(1000);
	

	while (1)
	{
		//wait for client
		pthread_mutex_lock(&mutexIndiceCourant);
		while (indiceCourant == -1)
			pthread_cond_wait(&condIndiceCourant, &mutexIndiceCourant);

		iCliTraite = indiceCourant;
		indiceCourant = -1;
		hSocketService = tabSocketConnected[iCliTraite];

		pthread_mutex_unlock(&mutexIndiceCourant);

		sprintf(buffer, "Je m'occupe du numero %d", iCliTraite);
		AfficheThread(numThread, buffer);

		endDialog = 0;
		do //Dialog loop
		{
			//receive client message
			memset(msgClient, 0, sizeof(msgClient));
			sizeMsgRecv = receiveDataVariableLength(hSocketService, sizeS, msgClient);
			if (sizeMsgRecv == 0) // the client is gone
			{
				AfficheThread(numThread, "Le client est parti !!");
				endDialog = 1;
				break;
			} else AfficheThread(numThread, "Recv socket OK");

			sprintf(buffer, "Message reçu : %s", msgClient);
			AfficheThread(numThread, buffer);

			//handle client message
			if (strcmp(msgClient, EOC) == 0) // Client is done
			{
				AfficheThread(numThread, "Le client a fini.");
				endDialog = 1; 
				break;
			}
			token = strtok(msgClient, msgSep);
			action = atoi(token);
			valide = 1;
			switch (action)
			{
				case LOGIN_OFFICER: AfficheThread(numThread, "LOGIN_OFFICER");
					if(comPrec != 0 && comPrec != LOGOUT_OFFICER) valide = 0;
					else
					{ 
						char *login = strtok(NULL, msgSep);
						char *pwd = strtok(NULL, msgSep);
						ret = checkLoginInfo(login, pwd);
						if (ret == 1){
							comPrec = action;
							sprintf(msgServer, "Y");
						}
						else sprintf(msgServer, "N");
					}
					break;
				case LOGOUT_OFFICER: AfficheThread(numThread, "LOGOUT_OFFICER");
						comPrec = action;
						sprintf(msgServer, EOC);
					break;
				case FLIGHT_INFOS: AfficheThread(numThread, "FLIGHT_INFOS");
					if (comPrec != LOGIN_OFFICER) valide = 0;
					else
					{
						char *tmpBuf = checkFlightInfos();
						if (tmpBuf != NULL){
							comPrec = action;
							sprintf(msgServer, "Y%c%s", msgSep[0], tmpBuf);
						}
						else sprintf(msgServer, "N%c%s", msgSep[0], 
							"Impossible de récupérer les informations.");
					}
					break;
				case CHECK_TICKET: AfficheThread(numThread, "CHECK_TICKET");
					if (comPrec != FLIGHT_INFOS && comPrec != PAYMENT_DONE 
							&& comPrec != CHECK_LUGGAGE) valide = 0;
					else
					{
						strcpy(numBillet, strtok(NULL, msgSep));
						int nbAccomp = atoi(strtok(NULL, msgSep));
						ret = verifyTicket(numBillet, nbAccomp, passengerId);
						if (ret == 0) sprintf(msgServer, "N%c%s", msgSep[0],
							"Le numéro de billet est invalide.");
						else if (ret == 1){
							comPrec = action;
							sprintf(msgServer, "Y");
						}
						else if (ret == 2) sprintf(msgServer, "N%c%s", msgSep[0],
							"Trop d'accompagnants.");
					}
					break;
				case CHECK_LUGGAGE: AfficheThread(numThread, "CHECK_LUGGAGE");
					if (comPrec != CHECK_TICKET) valide = 0;
					else
					{
						token = strtok(NULL, "\n");
						int nbBag;
						float totalWeight;
						ret = addLuggage(numBillet, passengerId, token, 
										&nbBag, &totalWeight);
						if (ret == 0) sprintf(msgServer, "N%c%s", msgSep[0], "Probleme avec les bagages.");
						else
						{
							if (totalWeight <= (20.0*nbBag))
							{
								ret = saveLuggage(numBillet);
								if (ret == 1){
									comPrec = action;
									sprintf(msgServer, "Y%c%f", msgSep[0], totalWeight);
								}
								else sprintf(msgServer, "N%c%s", msgSep[0], "Probleme de sauvegarde.");
							}
							else
							{
								float excess = totalWeight - (20.0*nbBag);
								float sup = excess * PRICE_PER_KG;
								sprintf(msgServer, "E%c%f%c%f%c%f", msgSep[0], 
									totalWeight, msgSep[0], excess, msgSep[0], sup);
							}
						}
					}
					break;
				case PAYMENT_DONE: AfficheThread(numThread, "PAYMENT_DONE");
					if (comPrec != CHECK_LUGGAGE) valide = 0;
					else
					{
						ret = saveLuggage(numBillet);
						if (ret == 1){
							comPrec = action;
							sprintf(msgServer, "Y%c", msgSep[0]);
						}
						else sprintf(msgServer, "N%c%s", msgSep[0], "Probleme de sauvegarde.");
					}
					break;
				default: sprintf(msgServer, EOC);
			} //end switch
			
			if (valide == 0) sprintf(msgServer, "N%cCommande non valide", msgSep[0]);
			
			//Send msgServer
			sprintf(buffer, "Reponse : %s", msgServer);
			AfficheThread(numThread, buffer);
			addEndChar(msgServer);
			if (sendMsg(hSocketService, msgServer) == -1) endDialog = 1;
			memset(msgServer, 0 , sizeof(msgServer));
		} while (!endDialog);
		
		//Free thread
		comPrec = 0;
		pthread_mutex_lock(&mutexIndiceCourant);
		tabSocketConnected[iCliTraite] = -1;
		pthread_mutex_unlock(&mutexIndiceCourant);
		AfficheThread(numThread, "J'attends un autre client...");
		
	} //fin while(1)
	
	AfficheThread(numThread, "Je m'arrete...");
	close(hSocketService);
	pthread_exit(&numThread);
}






