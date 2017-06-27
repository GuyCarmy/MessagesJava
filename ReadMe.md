A very very basic messages program that allows the server to send a message to all the registered clients.
a simple practice for UDP connection sockets (the use of multicast was not allowed, it would have made it alot easier)

The server and the client have a panel and a thread each, the thread deals with the constant communication and the panel is the GUI interface.

I only later realized I should have used another class as the controller between each panel and thread (of the server and the client classes)
as suggested by the MVC pattern.

Any comments or notes are welcome

