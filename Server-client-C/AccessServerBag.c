#include "AccessServerBag.h"

BAG_LINE *listBags = NULL;

int checkLoginInfo(char* login, char* pwd)
{
	int response = 0;
	CSV_CELL * listCells = NULL;
	fpos_t position = NULL;
	//printf("Recu : %s - %s \n", login, pwd);
	do
	{
		listCells = readCsvLine(LOGIN, csvSep, &position);
		if (listCells != NULL && strcmp(listCells->data, login) == 0)
		{
			listCells = listCells->next;
			if (strcmp(listCells->data, pwd) == 0) response = 1;
		}
	} while (listCells != NULL && response == 0);	
	return response;
}

char* checkFlightInfos()
{
	return "VOL 362 POWDER-AIRLINES - Peshawar 6h30";
}

int verifyTicket(char *numBillet, int nbAccomp, char *passengerId)
{
	int found = 0;
	CSV_CELL * listCells = NULL;
	fpos_t position = NULL;
	
	do
	{
		listCells = readCsvLine(TICKETS, csvSep, &position);
		if (listCells != NULL && strcmp(listCells->data, numBillet) == 0)
		{
			found = 1;
			listCells = listCells->next;
			strcpy(passengerId, listCells->data);
			for (int i = 0; listCells != NULL && i < nbAccomp; i++)
			{
				listCells = readCsvLine(TICKETS, csvSep, &position);
				if (listCells == NULL) return 2;
				listCells = listCells->next;
				if (strcmp(listCells->data, passengerId) != 0) return 2;
			}
			
		}
	} while (listCells != NULL && !found);
	if (!found) return 0;
	return 1;
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
		weight = atof(token);
		(*nbBag)++;
		(*totalWeight) += weight;
		if (*nbBag < 10) sprintf(bagRef, "%s00%d", bagId, *nbBag);
		else sprintf(bagRef, "%s0%d", bagId, *nbBag);
		tmp->ref = (char*)malloc(100);
		strcpy(tmp->ref, bagRef);		

		token = strtok(NULL, msgSep);
		tmp->type = (char*)malloc(20);
		if (strcmp(token, "Y") == 0 || strcmp(token, "O") == 0 
			|| strcmp(token, "y") == 0 || strcmp(token, "o") == 0)
			strcpy(tmp->type, "VALISE");
		else strcpy(tmp->type, "PASVALISE");
		
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
	CSV_CELL *listCells = (CSV_CELL*)malloc(sizeof(CSV_CELL));
	char *fileName = (char*)malloc(50), *token1, *token2;
	int ret = 1;
	
	if (listBags == NULL) return 0;
	tmp = listBags;

	token1 = strtok(numBillet, "-");
	token2 = strtok(NULL, "-");
	sprintf(fileName, "%s_%s_lug.csv", token1, token2);
	printf("Debug saveLuggage - fileName : ***%s*** \n", fileName);

	while (tmp != NULL && ret == 1)
	{
		listCells->data = (char*)malloc(100);
		strcpy(listCells->data, tmp->ref);
		listCells->next = (CSV_CELL*)malloc(sizeof(CSV_CELL));
		listCells->next->data = (char*)malloc(50);
		strcpy(listCells->next->data, tmp->type);
		if ((ret = writeCsvLine(fileName, csvSep[0], listCells)) == 0)
		{
			printf("Probleme au retour de writeCsvLine\n");
			return 0;
		}
		tmp = tmp->next;
	}
	listBags = NULL;
	return 1;
}
