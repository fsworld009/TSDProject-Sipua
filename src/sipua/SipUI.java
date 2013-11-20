/*
 * 
 * user interface
 */
package sipua;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SipUI {
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
        System.out.println("Call");
        return;
    }
    
    public void quit(){
        System.out.println("Quit");
        return;
    }
}
