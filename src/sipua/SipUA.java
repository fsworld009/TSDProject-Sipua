/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sipua;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zoolu.sip.provider.SipProvider;

/**
 *
 * @author WorldFS
 */
public class SipUA {
    SipProvider sipProvider;
    
    public SipUA(int port){
        try {
            SipProvider sipProvider = new SipProvider(InetAddress.getLocalHost().getHostAddress(),port);
        } catch (UnknownHostException ex) {
            //Logger.getLogger(SipUA.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("no such host");
        }
        
    }
}
