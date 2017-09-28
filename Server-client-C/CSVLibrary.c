#include "CSVLibrary.h"

CSV_CELL* readCsvLine(const char *fileName, char *sep, fpos_t *position)
{
	FILE *fp;
	CSV_CELL *listCells = NULL, *tmpCell;
	char buffer[1000], *token;

	if ((fp = fopen(fileName, "rt")) == NULL)
		printf("Erreur d'ouverture du fichier\n");
	else
	{
		//printf("Fichier %s ouvert\n", fileName);
		//set the pointer to the right line
		if (position != NULL && fsetpos(fp, position) != 0)
			printf("Erreur de déplacement : %d\n", errno);
		else
		{
			if (fgets(buffer, 1000, fp) == NULL) //read the line
				printf("Fin de lecture.\n");
			else
			{
				fgetpos(fp, position); //set the new position
				//initialise the list
				listCells = (CSV_CELL *)malloc(sizeof(CSV_CELL));
				tmpCell = listCells;
				//separate the "cells"
				token = strtok(buffer, sep);
				int i = 1;
				do
				{
					tmpCell->data = (char*)malloc(500);
					strcpy(tmpCell->data, token);
					if ((token = strtok(NULL, sep)) != NULL)
					{
						tmpCell->next = (CSV_CELL *)malloc(sizeof(CSV_CELL));
						tmpCell = tmpCell->next;
					}
					else tmpCell->next = NULL;
					i++;
				} while (token != NULL);
			}
		}
		fclose(fp);
		return listCells;
	}
	return NULL;
}

int writeCsvLine(const char *fileName, char sep, CSV_CELL * listCells)
{
	FILE *fp;
	char *buffer = (char*)malloc(5000);

	if ((fp = fopen(fileName, "at")) == NULL)
		printf("Erreur d'ouverture du fichier\n");
	{
		//printf("Fichier %s ouvert\n", fileName);
		do
		{
			sprintf(buffer, "%s%s%c", buffer, listCells->data, sep);
			//printf("Debug : ---%s---\n", buffer);
			listCells = listCells->next;
		} while (listCells != NULL);

		fprintf(fp, "%s\n", buffer);

		fclose(fp);
		return 1;
	}
	return 0;
}
