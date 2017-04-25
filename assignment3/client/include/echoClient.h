/*
 * echoClient.h
 *
 *  Created on: Jan 10, 2016
 *      Author: eyalere
 */

#ifndef ECHOCLIENT_H_
#define ECHOCLIENT_H_

class echoClient {
	
public:
	echoClient();
	virtual ~echoClient();
	void listenerFunc(ConnectionHandler &connectionHandler);
};

#endif /* ECHOCLIENT_H_ */
