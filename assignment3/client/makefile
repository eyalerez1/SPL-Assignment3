# All Targets
all: client

# Tool invocations
# Executable "client" depends on the files ConnectionHandler.o and echoClient.o and encoder.o
client: bin/ConnectionHandler.o bin/echoClient.o bin/encoder.o 
	@echo 'Building target: client'
	@echo 'Invoking: C++ Linker'
	g++ -o bin/client bin/ConnectionHandler.o bin/echoClient.o bin/encoder.o -lboost_system -lboost_locale -lboost_thread 
	@echo 'Finished building target: client'
	@echo ' '

# Depends on the source and header files
bin/ConnectionHandler.o: src/ConnectionHandler.cpp
	g++ -g -c -Linclude -o bin/ConnectionHandler.o src/ConnectionHandler.cpp

# Depends on the source and header files 
bin/echoClient.o: src/echoClient.cpp
	g++ -g -c -Linclude -o bin/echoClient.o src/echoClient.cpp

# Depends on the source and header files 
bin/encoder.o: src/encoder/encoder.cpp
	g++ -g -c -Linclude -o bin/encoder.o src/encoder/encoder.cpp

#Clean the build directory
clean: 
	rm -f bin/*