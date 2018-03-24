.SILENT:

CC = g++ -Wall -m64 -D REENTRANT -D SocketsUnix
END = -lpthread -lsocket -lnsl
OBJS = SocketsUtilities.o AccessServerBag.o
PROG = AppCheckIn ServerCheckIn

all: $(PROG)

AppCheckIn: AppCheckIn.c AppCheckIn.h SocketsUtilities.o
			echo Creation AppCheckIn ...
			$(CC) AppCheckIn.c SocketsUtilities.o -o "$@" $(END)

ServerCheckIn: ServerCheckIn.c ServerCheckIn.h Threads.c SocketsUtilities.o AccessServerBag.o
			   echo Creation ServerCheckIn ...
			   $(CC) ServerCheckIn.c $(OBJS) -o "$@" $(END)

AccessServerBag.o: AccessServerBag.c AccessServerBag.h SocketsUtilities.o
				   echo Creation AccessServerBag.o ...
				   $(CC) -c AccessServerBag.c SocketsUtilities.o -o "$@" $(END)

SocketsUtilities.o:	SocketsUtilities.c SocketsUtilities.h
					echo Creation SocketsUtilities.o ...
					$(CC) -c SocketsUtilities.c

#nettoyage
clean:
		echo clean...
		@rm -f	$(OBJS)	core

clobber:	clean
		echo clobber...
		@rm -f tags $(PROG)

