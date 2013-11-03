/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sipua;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.address.SipURL;
import org.zoolu.sip.call.Call;
import org.zoolu.sip.call.CallListenerAdapter;
import org.zoolu.sip.message.Message;
import org.zoolu.sip.message.MessageFactory;
import org.zoolu.sip.provider.SipProvider;
import org.zoolu.sip.provider.SipProviderListener;
import org.zoolu.sip.provider.Transport;

/**
 *
 * @author WorldFS
 */
public class SipUA extends CallListenerAdapter{
    SipProvider sipProvider;
    String myIpAddress="";
    SipURL mySipURL;
    NameAddress myNameAddress;
    Call myCall;
    int myPort;
    
    /*
     *         SipURL mySipURL = new SipURL("localhost",myPort);
        NameAddress myNameAddress = new NameAddress(mySipURL);
        
        SipURL recvSipURL = new SipURL("localhost",recvPort);
        NameAddress recvNameAddress = new NameAddress(recvSipURL);
        
        
        //sipProvider.setOutboundProxy(mySipURL);
        //sipProvider.
       
        System.out.printf("my sip address: %s:%d\n", sipProvider.getViaAddress(), sipProvider.getPort());
        //System.out.printf("bound to: %s\n", sipProvider.getOutboundProxy().toString());
        String msg;
        while(true){
            System.out.println("Enter Message: ");
            msg = sc.next();
            if(msg.equals("quit")){
                sipProvider.halt();
                break;
                
            }
            sipProvider.sendMessage(MessageFactory.createMessageRequest(sipProvider, recvNameAddress, myNameAddress, "subject", "type", msg));
        }
     * 
     */
    
    public SipUA(){

    }
    
    public void onCallInvite(Call call,NameAddress callee, NameAddress caller, java.lang.String sdp, Message invite){
        System.err.println(invite);
        
    }
    
    public void run(){
        /*
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Enter caller's port \n");
        int port = sc.nextInt();
        
        
        //try {
            //InetAddress.getLocalHost().getHostAddress()
            sipProvider = new SipProvider("localhost",port){
                
                public void onReceivedMessage(SipProvider sip_provider, Message message) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        System.err.println(message.getBody());
                }
                
            };
            //sipProvider.
        //} catch (UnknownHostException ex) {
            //Logger.getLogger(SipUA.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("no such host");
        //}
        
        
        
        System.out.println("Enter callee's ip address and port\n");
        String calleeIp = sc.next();
        int calleePort = sc.nextInt();
        
        System.out.printf("%d %s %d\n",port,calleeIp,calleePort);
        
        NameAddress myAddr = new NameAddress("localhost");
        call = new Call(sipProvider, myAddr,this);
        
        SipURL calleeSipURL = new SipURL(calleeIp,calleePort);
        NameAddress calleeNameAddress = new NameAddress(calleeSipURL);
        call.call(calleeNameAddress);
        
        
        //sipProvider.sendMessage(MessageFactory.createMessageRequest(sipProvider, calleeNameAddress, myAddr, "subject", "type", "AAAAAAA"));
        */
        
        
        Scanner sc = new Scanner(System.in);
        System.out.printf("Enter port number:");
        myPort = sc.nextInt();
        
        /*int myPort;
        int recvPort;
        
        if(node==1){
            myPort = 10000;
            recvPort = 10001;
            
        }else{
            myPort = 10001;
            recvPort = 10000;
            
            
        }*/
        
        
        try {
            //TcpServer tcpServer = new TcpServer(10000,null);
            myIpAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(SipUA.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        sipProvider = new SipProvider(myIpAddress,myPort){
            @Override
            public synchronized void onReceivedMessage(Transport transport,Message msg){
                super.onReceivedMessage(transport,msg);
                System.err.printf("Receive: %s\n",msg);
            }
            
            
        };
        
        mySipURL = new SipURL(myIpAddress,myPort);
        myNameAddress = new NameAddress(mySipURL);
        
        System.out.printf("my sip address: %s:%d\n", sipProvider.getViaAddress(), sipProvider.getPort());
        
        
        
        System.out.println("Enter callee's ip address and port number:");
        String recvAddress = sc.next();
        int recvPort = sc.nextInt();
        SipURL recvSipURL = new SipURL(recvAddress,recvPort);
        NameAddress recvNameAddress = new NameAddress(recvSipURL);
        
        
        System.out.printf("Call %s:%d\n",recvAddress,recvPort);
        //sipProvider.setOutboundProxy(mySipURL);
        //sipProvider.
       
        
        //System.out.printf("bound to: %s\n", sipProvider.getOutboundProxy().toString());
        /*String msg;
        while(true){
            System.out.println("Enter Message: ");
            msg = sc.next();
            if(msg.equals("quit")){
                sipProvider.halt();
                break;
                
            }
            sipProvider.sendMessage(MessageFactory.createMessageRequest(sipProvider, recvNameAddress, myNameAddress, "subject", "type", msg));
        }*/
        
        myCall = new Call(sipProvider,myNameAddress,this);
        myCall.call(recvNameAddress);
        
        
    }


}
