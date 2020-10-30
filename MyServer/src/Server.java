/*
Name:Ajith Sivadas
Student ID: 1001829098

Citation :I have watched and followed this youtube video for the server side programming 
            https://www.youtube.com/watch?v=rd272SCl-XE
*/

import java.net.*;
import java.io.*;
import  java.lang.Math.*;
import java.util.concurrent.ThreadLocalRandom;
import javax. swing.*;
import java.util.*;
import java.util.Timer; 
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.lang.model.SourceVersion;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ajith sivadas
 */



public class Server extends javax.swing.JFrame {

    /**
     * Creates new form Server with dialogue box showing the status of the server
     * The number of the clients online
     */
    ServerSocket s1; // The socket which we are creating 
    DefaultListModel online_list_model; // Used to store the currently active clients in the server 
    HashMap client_call = new HashMap();//Used to store the username and the socket info of 
                                        //different clients connected to the server 
    Timer task;// Used to send the number to the client after every 10 sec 
    Object key_1;// used to store the object of the currently send client 
    public Server() {
        try{
        initComponents();
        s1 = new ServerSocket(4004);// Running on the local host port number 4004
        this.Status.setText("The Server Has Started");//Initates the UI as the Server has stated 
        new ClientAccept().start() ;//The client data is ready to be stored int the server 
        online_list_model = new DefaultListModel();
        Online_Text_Area.setModel(online_list_model);// setting the value of the clients online 
        }
        catch(Exception ex)
        {
            ex.printStackTrace();// printing the exceptions if anything goes wrong 
        }
    }
    
    class ClientAccept extends Thread{
         /**
     * Checks for various client accepting conditions which is mentioned 
     * 
     */
        public void run(){
        while(true){
            try{
                
                Socket s = s1.accept();// this is where the new incoming clients are accepted
                String i = new DataInputStream(s.getInputStream()).readUTF();
                if(client_call.containsKey(i))//Here checks if the client is already present or not 
                {
  
                    DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                    //If it is there then the following message is shown 
                    dout.writeUTF("This User Name is already been used. Use another Username");
                }
                //Here checks and makes sure that the number of clients remains 3
                else if(client_call.size()>=3){
                    DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                    dout.writeUTF("The Maximum Number of connections have exceeded Only 3 connections are allowed");
                }
                else// if all the conditions pass then the clients are conencted to the server 
                {
                    client_call.put(i,s);
                    //Shows in the message box in the UI that user has joined the server 
                    Message_Box.append(i+" Have joined the server \n");
                    DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                    dout.writeUTF("");
                    new MsgRead(s,i).start();
                    new PrepareClientList().start();
                }
                //Timer class is called only if 3 clients are connected 
                Timer timer = new Timer();
                if(client_call.size()==3)
                {
                    
                    task = new Timer();
                    //calling the timer class every 10 sec which will 
                    //perform the task in the timer class 
                    task.scheduleAtFixedRate(new TimerTaskex(),0,10*1000);
                    
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
    public class TimerTaskex extends TimerTask
         /**
     * Checks and picks a random integer and a random client to send the values 
     * 
     */    
    {

        @Override
        public void run() {
            //Updated the Current online list by clients online 
            online_list_model.addElement(key_1);
            // Randomly chooses a integer between 3 to 9 to send to the client
            int t = (ThreadLocalRandom.current().nextInt(6) + 3 );
            //conversion of the integer value to string 
            String s=String.valueOf(t);
            //Randomly selecting one of the client for sending the integer 
            Object[] keys = client_call.keySet().toArray();
            Object key = keys[new Random().nextInt(keys.length)];
            // removing the element which has choosen for random suspention from the current online list  
            key_1=key;
            online_list_model.removeElement(key);
            
            try {
                //sending it to the client 
                new DataOutputStream(((Socket)client_call.get(key)).getOutputStream()).writeUTF(s);
                
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        }
        
    }
    
    class MsgRead extends Thread{
           /**
     * For displaying the values in the Message box in the client  
     * 
     */   
        Socket s;
        String ID;
        private MsgRead(Socket s, String i){
            this.s=s;//socket information 
            this.ID=i;
        }
        
        public void run(){
            //Checking if there are clients in the server 
            while(!client_call.isEmpty()){
                try{
                    String i = new DataInputStream(s.getInputStream()).readUTF();
                    //Displaying in the servers message box
                    Message_Box.append(i);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }
    class PrepareClientList extends Thread{
      /**
     * For Preparing the clients online list and then displaying the current list  
     * 
     */ 
        public void run(){
            try{
                //Initial clear if junk values are there
                online_list_model.clear();
                String ids = "";
                //Getting the all names of the clients from the client call  
                Set k = client_call.keySet();
                Iterator itr = k.iterator();
              
                
                itr=k.iterator();
                //iterating through the clients till there is an element next 
                while(itr.hasNext())
                {
                    String key = (String)itr.next();
                    try
                    {
                        //updating it on the online list to Display in the UI
                        online_list_model.addElement(key);
                    }
                    catch(Exception ex)
                    {   
                        //If any exception comes then remove that client 
                         client_call.remove(key);
                         Message_Box.append(key+": removed!"); 
                    }
                }
                
                
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Message_Box = new javax.swing.JTextArea();
        Server_Status = new javax.swing.JLabel();
        Status = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Online_Text_Area = new javax.swing.JList<>();
        Online_keyword = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Myserver");

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        Message_Box.setColumns(20);
        Message_Box.setRows(5);
        jScrollPane1.setViewportView(Message_Box);

        Server_Status.setFont(new java.awt.Font("American Typewriter", 1, 24)); // NOI18N
        Server_Status.setText("Server Status:");

        Status.setFont(new java.awt.Font("American Typewriter", 0, 13)); // NOI18N
        Status.setText("......................................");

        jScrollPane3.setViewportView(Online_Text_Area);

        Online_keyword.setFont(new java.awt.Font("American Typewriter", 1, 24)); // NOI18N
        Online_keyword.setText("Online");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(Server_Status, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Status, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(64, 64, 64)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Online_keyword)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Server_Status)
                    .addComponent(Status, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Online_keyword))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Server().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea Message_Box;
    private javax.swing.JList<String> Online_Text_Area;
    private javax.swing.JLabel Online_keyword;
    private javax.swing.JLabel Server_Status;
    private javax.swing.JLabel Status;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
}
