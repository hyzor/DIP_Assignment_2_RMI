# DIP Assignment 2 (RMI)

---------------------------------
 Runtime VM arguments (See NOTE)
---------------------------------
-Djava.security.policy=file:${workspace_loc}/DIP_Assignment_2_RMI_Server/security.policy   -Djava.rmi.server.codebase=file:${workspace_loc}/DIP_Assignment_2_RMI_Server/bin/  

-Djava.security.policy=file:${workspace_loc}/DIP_Assignment_2_RMI_Server/security.policy   -Djava.rmi.server.codebase=file:${workspace_loc}/DIP_Assignment_2_RMI_Interface/bin/  

NOTE: java.rmi.server.codebase is completely ignored when set in either the server or client, only when starting the rmiregistry we can affect this property.

---------------------------------
 Rmiregistry
---------------------------------
rmiregistry -J-Djava.rmi.server.codebase=file:////home/jesper/Dev/Projects/workspace_eclipse/DIP_Assignment_2_RMI_Interface/bin/ &

rmiregistry -J-Djava.rmi.server.codebase=file:Database.jar &

For convenience, a JAR-file is automatically generated and put into the appropriate sub-path of DIP_Assignment_2_RMI_Interface/bin/

---------------------------------
 SCP commands
---------------------------------
scp DatabaseServer.jar cudateam5@147.46.242.21:/home/cudateam5/assign2/RMI/  
scp DatabaseClient.jar cudateam5@147.46.242.21:/home/cudateam5/assign2/RMI/  
scp Database.jar cudateam5@147.46.242.21:/home/cudateam5/assign2/RMI/  

scp DIP_Assignment_2_RMI_Server/bin/DatabaseServer.jar cudateam5@147.46.242.21:/home/cudateam5/assign2/RMI/  
scp DIP_Assignment_2_RMI_Client/bin/DatabaseClient.jar cudateam5@147.46.242.21:/home/cudateam5/assign2/RMI/  
scp DIP_Assignment_2_RMI_Interface/bin/Database.jar cudateam5@147.46.242.21:/home/cudateam5/assign2/RMI/  

---------------------------------
 Execution
---------------------------------
[Running server locally]
Running server locally: java -cp DatabaseServer.jar:Database.jar -Djava.security.policy=file:security.policy com.server.db.DatabaseServer Data/MyDB_10000_2

[Running server remotely]
java -cp DatabaseServer.jar:Database.jar -Djava.security.policy=file:security.policy -Djava.rmi.server.hostname=147.46.242.21 com.server.db.DatabaseServer Data/MyDB_10000_2

[Running client and connecting to some IP-address]
java -cp DatabaseClient.jar:Database.jar -Djava.security.policy=file:security.policy com.client.db.DatabaseClient 147.46.242.21 0 1 Data/tracegen_10000_rand

[Running client and connecting to some IP-address, using separate folders]
java -cp DIP_Assignment_2_RMI_Client/bin/DatabaseClient.jar:DIP_Assignment_2_RMI_Interface/bin/Database.jar -Djava.security.policy=file:security.policy com.client.db.DatabaseClient 147.46.242.21 0 1 DIP_Assignment_2_RMI_Client/Data/tracegen_10000_rand

---------------------------------
 Execution - Benchmarking
---------------------------------
SEQUENTIAL READ, 10000 records:
time java -cp DIP_Assignment_2_RMI_Client/bin/DatabaseClient.jar:DIP_Assignment_2_RMI_Interface/bin/Database.jar -Djava.security.policy=file:security.policy com.client.db.DatabaseClient 147.46.242.21 0 1 DIP_Assignment_2_RMI_Client/Data/tracegen_10000_rand

RANDOM READ, 10000 records:
time java -cp DIP_Assignment_2_RMI_Client/bin/DatabaseClient.jar:DIP_Assignment_2_RMI_Interface/bin/Database.jar -Djava.security.policy=file:security.policy com.client.db.DatabaseClient 147.46.242.21 0 0 DIP_Assignment_2_RMI_Client/Data/tracegen_10000_rand

WRITE, 10000 records:
time java -cp DIP_Assignment_2_RMI_Client/bin/DatabaseClient.jar:DIP_Assignment_2_RMI_Interface/bin/Database.jar -Djava.security.policy=file:security.policy com.client.db.DatabaseClient 147.46.242.21 1 1 DIP_Assignment_2_RMI_Client/Data/tracegen_10000_rand

---------------------------------
 HOW TO RUN
---------------------------------
1. Open terminal in the 'RMI' folder
2. Start 'rmiregistry' using: rmiregistry -J-Djava.rmi.server.codebase=file:Database.jar &
3. Start the server by using: java -cp DatabaseServer.jar:Database.jar -Djava.security.policy=file:security.policy -Djava.rmi.server.hostname=147.46.242.21 com.server.db.DatabaseServer Data/MyDB_10000_2
4. Run a client with: java -cp DatabaseClient.jar:Database.jar -Djava.security.policy=file:security.policy com.client.db.DatabaseClient 147.46.242.21 0 1 Data/tracegen_10000_rand

NOTE:
The client uses 4 arguments: IP_ADDRESS WRITE_FLAG (0 means reading, 1 means writing) SEQUENTAL_FLAG (0 means random access, 1 means sequential access) TRACE_FILE (Only used for random access)
