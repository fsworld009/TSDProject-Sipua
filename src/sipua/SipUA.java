/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sipua;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import local.net.RtpPacket;
import local.net.RtpSocket;
import org.zoolu.net.IpAddress;
import org.zoolu.net.UdpSocket;
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
    

    
    UdpSocket udpSocket;
    RtpSocket rtpSocket;
    
    String recvAddress;
    int recvPort;
    SipURL recvSipURL;
    NameAddress recvNameAddress;
    
    VoiceChat voiceChat;
    
    int rtpPort;
 
    
    public SipUA(){

    }
    
    @Override
    public void onCallAccepted(Call call, java.lang.String sdp, Message resp){
        super.onCallAccepted(call, sdp, resp);
        System.err.println("Accepted: "+resp);
        call.ackWithAnswer("I GOT IT");
        //Caller starts its voice chat here
        initVoiceChat();
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
        //Callee starts its voice chat here
        initVoiceChat();
    }
    
    private void initVoiceChat(){
        voiceChat = new VoiceChat();
        voiceChat.init(recvAddress, rtpPort);
        voiceChat.start();

    }
    
    private void readConfig(){
         Scanner sc=null;
        try {
            sc = new Scanner(new File("input.txt"));
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(SipUA.class.getName()).log(Level.SEVERE, null, ex);
        }
        myPort = sc.nextInt();
        
        try {
            myIpAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            //Logger.getLogger(SipUA.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        mySipURL = new SipURL(myIpAddress,myPort);
        myNameAddress = new NameAddress(mySipURL);
        
        recvAddress = sc.next();
        recvPort = sc.nextInt();
        rtpPort = sc.nextInt();
    }
    
    private void initSipProvider(){
        sipProvider = new SipProvider(myIpAddress,myPort){
            @Override
            public synchronized void onReceivedMessage(Transport transport,Message msg){
                super.onReceivedMessage(transport,msg);
                System.err.printf("Receive: %s\n",msg);
                if(msg.isInvite()){
                    myCall = new Call(sipProvider,msg,SipUA.this);


                    //init recv variables
                    recvAddress = msg.getRemoteAddress();
                    recvPort = msg.getRemotePort();
                    recvSipURL = new SipURL(recvAddress,recvPort);
                    recvNameAddress = new NameAddress(recvSipURL);
                     


                    System.out.printf("Receive invite from %s:%d\n",recvAddress,recvPort);
                    myCall.ring();
                    myCall.accept("LET'S TALK");
                }
            }
            
            
        };
        
        System.out.printf("my sip address: %s:%d\n", sipProvider.getViaAddress(), sipProvider.getPort());
    }
    
    public void start(){       

        readConfig();
        initSipProvider();
        
        if(recvAddress.equals("server")){
            System.out.println("I'm a server");
        }else{
            recvSipURL = new SipURL(recvAddress,recvPort);
            recvNameAddress = new NameAddress(recvSipURL);

            System.out.printf("Call %s:%d\n",recvAddress,recvPort);

            myCall = new Call(sipProvider,myNameAddress,this);
            myCall.call(recvNameAddress);
        }
        
    }


}
