#ifndef SERVER_CHECKIN_H
#define SERVER_CHECKIN_H

#include <pthread.h>

#include "SocketsUtilities.h"
#include "AccessServerBag.h"

#define MAXTHREAD	5

#define PRICE_PER_KG	5.0

//Threads
#define AfficheThread(num, msg) printf("\e[0;34m th_%s >\e[0m %s \n", num, msg)

pthread_t tabThreadHandle[MAXTHREAD];
pthread_mutex_t mutexIndiceCourant;
pthread_cond_t condIndiceCourant;
int indiceCourant = -1;

char * getThreadIdentity();
short fctThread(int * param);

//Sockets
int tabSocketConnected[MAXTHREAD]; // handle des sockets pour clients
int *tabSockets[3];

void closeAllSockets();

#endif //SERVER_CHECKIN_H
