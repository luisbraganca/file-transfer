# TLS/SSL FileTransfer

A JavaFX Application (Client) that sends/receives a file over a concurrent server using TLS/SSL with a self-signed certificate.

## Preview

![First screenshot of the application](https://raw.githubusercontent.com/luisbraganca/file-transfer/master/Screenshots/preview1.png)

![Second screenshot of the application](https://raw.githubusercontent.com/luisbraganca/file-transfer/master/Screenshots/preview2.png)

![Third screenshot of the application](https://raw.githubusercontent.com/luisbraganca/file-transfer/master/Screenshots/preview3.png)

## Technical details

This application allows you to send/receive a file over a concurrent server using the [TLS/SSL](https://en.wikipedia.org/wiki/Transport_Layer_Security) protocol with a self-signed certificate. It's assigned a temporary ID (String with 4 alphanumeric chars) to the file receiver client, which can be used by the file sender.

### Functionalities

* File send (or safely cancel it without crashing the application)
* File reception (or safely cancel it without crashing the application)
* Select the file using a file chooser
* Concurrent server
* Server: Background thread infinitely pinging all the receptor-type sockets and removing the non-responding ones, acting like a cleaner
* Server: Logger
* Certificate support
* Embedded web browser on the "about" pane that opens this GitHub page

## Getting started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

It's highly recommended that you edit this project using IntelliJ IDEA from JetBrains that can be downloaded [here](https://www.jetbrains.com/idea/) since it was developed using that same tool.

### Certificates

To generate a a self-signed certificate, locate your keytool on your Java istallation folder and run it.
Example (Windows cmd line):
```
"\Program Files\Java\jdk1.8.0_131\bin\keytool.exe" -genkey -alias filetransfer -keystore keystore.jks
```
Now we have a certificate under the name of "filetransfer" on the keystore file named "keystore.jks".
To export the Public Key Certificate, run:
```
"\Program Files\Java\jdk1.8.0_131\bin\keytool.exe" -exportcert -keystore keystore.jks -alias filetransfer -file filetransfer.cer
```
Now you're able to import it on the client keystore:
```
"\Program Files\Java\jdk1.8.0_131\bin\keytool.exe" -import -file filetransfer.cer -alias filetransfer -keystore truststore.jks
```

### Resources

* Icon

The icon used was made by:
[Fabián Alexis](https://github.com/fabianalexisinostroza/Antu) [CC BY-SA 3.0](https://creativecommons.org/licenses/by-sa/3.0), via Wikimedia Commons

## Notes

If you're only looking for the send/reception code without the user interface, you can find it [here (send)](https://github.com/luisbraganca/file-transfer/blob/master/SSLClientFX/src/net/FileSend.java), and [here (receive)](https://github.com/luisbraganca/file-transfer/blob/master/SSLClientFX/src/net/FileReception.java) fully commented.

Usage examples:
```java
FileReception fileReception = new FileReception("Example.pdf");
System.out.println(fileReception.getId()); // The server generated Id
fileReception.start();
```
```java
new FileSend("Example.pdf").start();
```
The rest of the project isn't that much commented but it's mainly JavaFX operations.

## Authors

* **Luís Bragança Silva** - *Initial work*
* **Pedro Fernandes Costa** - *Initial work*
* **João Marques Capinha** - *Initial work*
