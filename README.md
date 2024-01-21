# Mendonca-messenger
Project that send and receive user messages through the network.


### Introduction: The Mendonca Chat is an application that allows users in the same local network or in if deploy it in the cloud, to communicate by text messages.

### How to run:
In order to run this application you are going to need to have Postgrees database and ActiveMQ running. 
Postgrees is using for authentication and register new users in the system. 
ActiveMQ it is required for deliver the messages between the users logged in the application.
The files, which contains the configuration, relate to these two components are: application.properties and ChatMendoncaBean.java
Once the application is running, you are going to need to register users by reach the end-point called /security/register passing the follow payload
{

"userName":"username",
"password":"12345",
"role":"user"
}

After save the users in the application reach the end-point called  /chat with your credentials, than you are going to end up to the chat page.
To send a message just select the user in “Send Message to” combo box menu and type the text messages. 

Technologies used Java 17, Apache ActiveMQ 5.18.0, postgres:9.5, Junit 5 and Mokito 4.5.1
