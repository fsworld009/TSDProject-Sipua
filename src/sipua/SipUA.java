/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sipua;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.address.SipURL;
import org.zoolu.sip.message.MessageFactory;
import org.zoolu.sip.provider.SipProvider;

/**
 *
 * @author WorldFS
 */
public class SipUA {
    SipProvider sipProvider;
    
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
    
    public SipUA(int port){
        try {
            sipProvider = new SipProvider(InetAddress.getLocalHost().getHostAddress(),port);
        } catch (UnknownHostException ex) {
            //Logger.getLogger(SipUA.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("no such host");
        }
        
    }
    
    public void run(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter callee's ip address and port\n");
        String calleeIp = sc.next();
        int calleePort = sc.nextInt();
        
        
    }
}
