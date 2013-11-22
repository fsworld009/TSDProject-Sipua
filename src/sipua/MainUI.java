/*
 * 
 * user interface
 */
package sipua;

import java.util.Scanner;


public class MainUI{
    private SipUA sipUA=null;
    private Scanner userInput = new Scanner(System.in);
    
    

    
    
    public void start(){
        
        if(sipUA==null){
            sipUA = new SipUA(this);
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
        sipUA.call(sipAddrs[0],Integer.parseInt(sipAddrs[1]));
        
        
        return;
    }
    
    public boolean called(String addr){
        System.out.printf("A call from %s\n",addr);
        System.out.printf("y=answer n=deny\n");
        char option = userInput.next().charAt(0);
        if(option=='y'){
            return true;
        }else{
            return false;
        }
        
    }
    
    public void quit(){
        System.out.println("Quit");
        return;
    }
}
