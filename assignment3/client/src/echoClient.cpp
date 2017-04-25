#include <stdlib.h>
#include <boost/locale.hpp>
#include "../include/encoder/utf8.h"
#include "../include/encoder/encoder.h"
#include "../include/ConnectionHandler.h"
#include <boost/thread/thread.hpp>

void listenerFunc(ConnectionHandler *connectionHandler) {
	std::string msg;
	while(connectionHandler->isActive()) {
		connectionHandler->getLine(msg);
		std::cout<< msg << std::endl;
		if(msg=="SYSMSG QUIT ACCEPTED\n") {
			connectionHandler->closeConnection();
			std::cout<< "Press Enter to quit."<<std::endl;
		}
		msg="";
	}
}

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/

int main (int argc, char *argv[]) {
    std::string host ="127.0.0.1";
    short port = 9000;

    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }


	boost::thread listener(listenerFunc,&connectionHandler);

    while (connectionHandler.isActive()) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
		std::string line(buf);
		int len=line.length();
		if(len==0 && !connectionHandler.isActive()) {
		break;
		}
        if (!connectionHandler.sendLine(line)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
    }
    connectionHandler.close();
    return 0;
}

