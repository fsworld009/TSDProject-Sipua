/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sipua;

import java.io.File;
import java.io.FileNotFoundException;
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
    
    @Override
    public void onCallAccepted(Call call, java.lang.String sdp, Message resp){
        super.onCallAccepted(call, sdp, resp);
        System.err.println("Accepted: "+resp);
        call.ackWithAnswer("I GOT IT");
        //Caller starts its thread here
    }
    
    @Override
    public void onCallInvite(Call call,NameAddress callee, NameAddress caller, java.lang.String sdp, Message invite){
        super.onCallInvite(call, callee, caller, sdp, invite);
        System.err.println("Invite: "+invite);
        
        
        
    }
    
    @Override
    public void onCallRinging(Call call,Message resp){
        super.onCallRinging(call, resp);
        System.err.println("onCallRinging: "+resp);
        
    }
    
    @Override
    public void onCallConfirmed(Call call, java.lang.String sdp, Message ack){
        super.onCallConfirmed(call, sdp, ack);
        System.err.println("onCallConfirmed: "+ack);
        //Callee starts its thread here
        
    }
    
    private void initNetVariables(Scanner sc){
 
        myPort = sc.nextInt();
        
        try {
            myIpAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            //Logger.getLogger(SipUA.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        mySipURL = new SipURL(myIpAddress,myPort);
        myNameAddress = new NameAddress(mySipURL);
    }
    
    private void initSipProvider(){
        sipProvider = new SipProvider(myIpAddress,myPort){
            @Override
            public synchronized void onReceivedMessage(Transport transport,Message msg){
                super.onReceivedMessage(transport,msg);
                System.err.printf("Receive: %s\n",msg);
                if(msg.isInvite()){
                     myCall = new Call(sipProvider,msg,SipUA.this);
                     myCall.ring();
                     myCall.accept("LET'S TALK");
                }
            }
            
            
        };
        
        System.out.printf("my sip address: %s:%d\n", sipProvider.getViaAddress(), sipProvider.getPort());
    }
    
    public void run(){       
        Scanner sc=null;
        try {
            sc = new Scanner(new File("input.txt"));
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(SipUA.class.getName()).log(Level.SEVERE, null, ex);
        }
        initNetVariables(sc);
        initSipProvider();
        
        
        String recvAddress = sc.next();
        if(recvAddress.equals("server")){
            System.out.println("I'm a server");
        }else{
            int recvPort = sc.nextInt();
            SipURL recvSipURL = new SipURL(recvAddress,recvPort);
            NameAddress recvNameAddress = new NameAddress(recvSipURL);


            System.out.printf("Call %s:%d\n",recvAddress,recvPort);

            myCall = new Call(sipProvider,myNameAddress,this);
            myCall.call(recvNameAddress);
        }
        
    }


}
