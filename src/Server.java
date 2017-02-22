/**
 * Created by tcaron on 22/02/2017.
 */

import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ClientInfoStatus;
import java.util.ArrayList;


public class Server extends Thread{
    private static int port = 6666;
    private static ArrayList<BufferedWriter> clientsList;
    private static ServerSocket srv;
    private String name;
    private Socket skt;
    private InputStream inStream;
    private InputStreamReader inStreamReader;
    private BufferedReader bfReader;

    public Server (Socket skt){

        this.skt = skt;

        try{

            inStream = skt.getInputStream();
            inStreamReader = new InputStreamReader(inStream);
            bfReader = new BufferedReader(inStreamReader);
        }

        catch(IOException e ){

            e.printStackTrace();
        }

    }
    /*
     Method to send message to everyone
    */
    public void sendToAll(BufferedWriter bwOutput,String msg) throws IOException{

        BufferedWriter bwS;

        for (BufferedWriter bw : clientsList){
            bwS = (BufferedWriter)bw;
            if(!(bwOutput == bwS)){
                bw.write(name+ " : " + msg + "\r\n");
                bw.flush();
            }
        }

    }

    /*
     Method to run the server
    */
    public void run(){

     try {
         String msg;
         OutputStream outStream = this.skt.getOutputStream();
         Writer outWriter = new OutputStreamWriter(outStream);
         BufferedWriter bfWriter = new BufferedWriter(outWriter);
         clientsList.add(bfWriter);
         name = msg = bfReader.readLine();

         while(!"Logout".equalsIgnoreCase(msg) && msg != null)
         {
             msg = bfReader.readLine();
             sendToAll(bfWriter,msg);
             System.out.println(msg);
         }


     }
     catch(Exception e){
         e.printStackTrace();
     }

    }

    /*** * Method main *
     * @param args */
    public static void main(String []args) {

    try{

        JLabel lblMessage = new JLabel("Server Port : " + port );
        srv = new ServerSocket(port);
        clientsList = new ArrayList<>();
        Object[] txt = {lblMessage};
        JOptionPane.showMessageDialog(null, txt);

        System.out.print(lblMessage.getText());
        while(true){
            System.out.println("Waiting for connection...");
            System.out.println(clientsList);
            Socket skt =srv.accept();
            System.out.println("Client connected .. ");
            Thread t = new Server(skt);
            t.start();
        }

    }

    catch(IOException e){
        e.printStackTrace();
    }
    }
}
