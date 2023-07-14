# SharedWhiteBoard
- A whiteboard that allows multiple users to edit and shares the modifications on the canvas.
- Build based on Java.
- RMI structure used as the communication portal
- Front end is built based on the Java swing.


# System architecture
The overall system architecture is based on RMI implementation, a TCP port has been used to
form the connection between client and server. The server is initiated by declaring its port
number to the registry. Further clients can connect to the server via the RMI Registry by
inputting the activated port number and user name.
The Remote interface has mainly been used to trigger the methods stored on the Client-side
from the Server side, or start the methods stored on the Server-side from the Client side.
Every time a method is invoked would let one side go to the remote interface and process the
request via the RMI portal.
The Server should be initiated first to create the Remote interface and bind the RMI registry to
be prepared to let users make the connection. Clients have been sorted as manager and
non-manager, they have access to use the methods stored at the Server via the Remote
portal

