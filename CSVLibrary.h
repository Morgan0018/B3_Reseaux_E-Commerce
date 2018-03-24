#ifndef CSV_LIBRARY_H
#define CSV_LIBRARY_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <errno.h>

typedef struct _CSV_CELL
{
	char * data;
	struct _CSV_CELL * next;
} CSV_CELL;

CSV_CELL* readCsvLine(const char *fileName, char *sep, fpos_t *position);
int writeCsvLine(const char *fileName, char sep, CSV_CELL * listCells);
	//returns 1 if OK; 0 if not OK;

#endif //CSV_LIBRARY_H
