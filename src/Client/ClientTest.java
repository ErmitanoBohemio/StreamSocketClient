/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client;

import javax.swing.JFrame;

/**
 *
 * @author OscarFabianHP
 */
public class ClientTest {
    public static void main(String[] args) {
        Client application; //declare client application
        
        if(args.length == 0)
            application = new Client("127.0.0.1"); //connect to localhost
        else
            application = new Client(args[0]); //use args to connect
        
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.runClient();
    }
}
