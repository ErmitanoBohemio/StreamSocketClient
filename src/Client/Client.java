/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author OscarFabianHP
 */

//este programa se ejecuta al tener la app Server.java y ServerTest.java (Client and Server Interaction Project) ejecutandose 
public class Client extends JFrame{
    
    private JTextField enterField; //enters information from user
    private JTextArea displayArea; //display information to user
    private ObjectOutputStream output; //output stream to server
    private ObjectInputStream input; //input stream from server
    private Socket client; //socket to communicate with server
    
    private String message = "";
    private String chatServer;
    
    //initialize chatServer and ser up GUI
    public Client(String host){
        super("Client");
        
        chatServer = host; //set server to which this client connects
        
        enterField = new JTextField(); //create enterField
        enterField.setEditable(false);
        enterField.addActionListener(new ActionListener() {
            //send message to server
            @Override
            public void actionPerformed(ActionEvent event) {
                sendData(event.getActionCommand()); //getActionCommand obtiene la cadena en el objeto enterField 
                enterField.setText("");
            }
        });
        add(enterField, BorderLayout.NORTH);
        
        displayArea = new JTextArea();//create displayArea
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        setSize(300, 150);
        setVisible(true);
    }
    
    //connect to server and process messages from server
    //processes messages received from the server and closes the connection when comunication is complete
    public void runClient(){
        try{ //connect to server, get streams, process connection 
            connectToServer(); //create a Socket to make connection
            getStreams(); //get the input and output streams
            processConnection(); //process connection 
        } 
        catch(EOFException eofException){
            displayMessage("\nClient terminated connection");
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
        finally{ //close connection even if Exception occurred
            closeConnection();  //close connection
        }
        
    }
    
    //connect to server
    private void connectToServer() throws IOException{
        //create Socket to make connection to server
        client = new Socket(InetAddress.getByName(chatServer), 12345);  //normally, this first argument would be the IP Address of another computer; the second argument must match the port number at which the server is waiting for connections
        /*InetAddress static method getByName return an InetAddress object containing the IP Address specified as a command-line
        argument to the application (or 127.0.0.1 if none was specified). Method getByName can receive a String containing either 
        the actual IP address or the host name of the server. The first argument also could have been written other ways. for
        the localhost address 127.0.0.1, the first argument could be specified with either of the following expressions:
        InetAddress.getByName("localhost")
        InetAddress.getLocalHost()*/
        
        //display connection information
        displayMessage("Connected to: " + 
                client.getInetAddress().getHostName());
    }
    
    //get streams to send and receive data
    private void getStreams() throws IOException{
        //set up output stream for objects
        output = new ObjectOutputStream(client.getOutputStream()); //the client uses an ObjectOutputStream to send data to the server
        output.flush();
        //set up input stream for objects
        input = new ObjectInputStream(client.getInputStream()); //the client uses an ObjectInputStream to receive data from the server
        
        displayMessage("\nGot I/O streams\n");
    }
    
    //process connection with server
    //receive and display messages sent from the server
    private void processConnection() throws IOException{
        //enable enterField so client user can send message
        setTextFieldEditable(true);
        do{
            
            try { //read messages and display it
                message = (String) input.readObject(); //read new message
                displayMessage("\n"+message); //display message
            } 
            catch (ClassNotFoundException classNotFoundException) {
                displayMessage("\nUnknown object type received");
            }
        }while(!message.equals("SERVER>>> TERMINATE"));
    }
    
    private void sendData(String message){
        try {
            output.writeObject("CLIENT>>> " + message);
            output.flush(); //flush data to output
            displayMessage("\nCLIENT>>> "+message);
        } catch (IOException ioException) {
            displayMessage("\nError writing object");
        }
    }

    //manipulates displayArea in the event-dispatch thread
    //display messages in the application's text area
    private void displayMessage(final String messageToDisplay) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() { //updates displayArea
                displayArea.append(messageToDisplay);
            }
        });
        
    }

    //manipulates enterField in the event-dispatch thread
    private void setTextFieldEditable(final boolean editable) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() { //sets enterField's editability
                enterField.setEditable(editable);
            }
        });
    }

    //close streams and socket
    private void closeConnection() {
        displayMessage("\nClosing connection");
        setTextFieldEditable(false); //disable enterField
        try {
            output.close(); //close aoutput stream
            input.close(); //close input stream
            client.close(); //close socket
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
}
