.SILENT:

CC = g++ -Wall -m64 -D REENTRANT -D SocketsUnix
END = -lpthread -lsocket -lnsl
OBJS = CSVLibrary.o SocketsUtilities.o AccessServerBag.o
PROG = AppCheckIn ServerCheckIn

all: $(PROG)

AppCheckIn: AppCheckIn.c AppCheckIn.h SocketsUtilities.o
			echo Creation AppCheckIn ...
			$(CC) AppCheckIn.c SocketsUtilities.o -o "$@" $(END)

ServerCheckIn: ServerCheckIn.c ServerCheckIn.h Threads.c SocketsUtilities.o AccessServerBag.o
			   echo	Creation ServerCheckIn ...
			   $(CC) ServerCheckIn.c $(OBJS) -o "$@" $(END)

AccessServerBag.o: AccessServerBag.c AccessServerBag.h SocketsUtilities.o CSVLibrary.o
				   echo	Creation AccessServerBag.o ...
				   $(CC) -c AccessServerBag.c SocketsUtilities.o CSVLibrary.o -o "$@" $(END) # -c ????

SocketsUtilities.o:	SocketsUtilities.c SocketsUtilities.h
					echo	Creation SocketsUtilities.o ...
					$(CC) -c SocketsUtilities.c

CSVLibrary.o: CSVLibrary.c CSVLibrary.h
			  echo	Creation CSVLibrary.o ...
			  $(CC) -c CSVLibrary.c

#nettoyage
clean:
		echo clean...
		@rm -f	$(OBJS)	core

clobber:	clean
		echo clobber...
		@rm -f tags $(PROG)

