/*
 * 
 * user interface
 */
package sipua;

import java.util.Scanner;


public class SipUI{
    private SipUA sipUA=null;
    private Scanner userInput = new Scanner(System.in);
    
    

    
    
    public void start(){
        
        if(sipUA==null){
            sipUA = new SipUA();
            sipUA.start();
        }
        run();
    }
    
    public void run(){
        char option;
        while(true){
            System.out.println("SIPUA");
            System.out.println("c: Call");
            System.out.println("q: Quit");
            option = userInput.next().charAt(0);
            System.err.printf("%c\n",option);
            if(option=='c'){
                call();
            }else if(option=='q'){
                quit();
                
            }
        }
        
    }
    
    public void call(){
        System.out.println("Enter SIP Address (IP:Port)");
        String sipAddr;
        sipAddr = userInput.next();
        String[] sipAddrs = sipAddr.split(":");
        System.out.printf("%s %s\n",sipAddrs[0],sipAddrs[1]);
        
        
        return;
    }
    
    public void quit(){
        System.out.println("Quit");
        return;
    }
}
