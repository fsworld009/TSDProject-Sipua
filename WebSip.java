//Reference the required Java libraries
//package applet;
 import java.applet.Applet; 
 import java.awt.*; 
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
//import local.net.RtpPacket;
//import sipua.VoiceChat;

//import local.net.RtpSocket;
//import org.zoolu.net.IpAddress;
//import org.zoolu.net.UdpSocket;
//import sipua.VoiceChat;

 
 //The applet code
 public class WebSip extends Applet {
     //VoiceChat vc = new VoiceChat();
     //vc.init();
    //MicThread micThread;
    //SpeakerThread speakerThread;
    //SendThread sendThread;
    //ReceiveThread receiveThread;

    AudioFormat audioFormat;
    DataLine.Info recordLineInfo;
    TargetDataLine recordLine;

    DataLine.Info playLineInfo;
    SourceDataLine playLine;
    
    boolean threadRunning =false;
    
    int bufferSize;
    byte[] sendBuffer;
    byte[] receiveBuffer;
     public void init(){
         //vc.init("192.168.2.3", 10002);
     }
     
     public void paint(Graphics g) {
 
       //Draw a rectangle width=250, height=100
       g.drawRect(0,0,250,100); 
 
       //Set the color to blue
       g.setColor(Color.blue); 
 
       //Write the message to the web page
       g.drawString("Look at me, I'm a Java Applet!",10,50); 
    }
     
     public void start(){
         init("",10);
         //micThread = new MicThread();
         threadRunning=true;
         //micThread.start();
            recordLine.start();
            playLine.start();
            int count;
            while(threadRunning){
                try {
                    count = recordLine.read(sendBuffer, 0, sendBuffer.length);
                    if (count > 0) {
                      //System.out.printf("RECORDED: %s\n", sendBuffer);
                      //RtpPacket rtpPacket= new RtpPacket(sendBuffer,sendBuffer.length);
                      //rtpSocket.send(rtpPacket);
                      //recordLine.drain();
                        playLine.write(sendBuffer, 0, bufferSize);
                    }
                    Thread.sleep(5);
                } catch (InterruptedException ex) {
                    Logger.getLogger(WebSip.class.getName()).log(Level.SEVERE, null, ex);
                //} catch (IOException ex) {
                //    Logger.getLogger(VoiceChat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
     }
     
     
    public void init(String address, int port){
        //connectAddress = address;
        //connectPort = port;
        try {        
            //udpSocket = new UdpSocket(connectPort);
            //rtpSocket = new RtpSocket(udpSocket,IpAddress.getByName(connectAddress),connectPort);
            
            
            audioFormat = new AudioFormat(8000,8,2,true,true);
            recordLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            recordLine = (TargetDataLine)AudioSystem.getLine(recordLineInfo);
            //System.out.printf("%b",AudioSystem.isLineSupported(Port.Info.MICROPHONE));
            //recordLine =  AudioSystem.getLine(Port.Info.MICROPHONE).;
            
            
            //Line line;
            //line = AudioSystem.getLine(Port.Info.MICROPHONE);
            //recordLine = (TargetDataLine) AudioSystem.getLine(line.getLineInfo());
            /*if(recordLine == null){
                    System.out.println("Did not worked");
                }else{
                    System.out.println("It worked");
                }*/
            
            bufferSize = (int)audioFormat.getSampleRate() * audioFormat.getFrameSize();
            System.out.printf("fsdfs %d %f %d", bufferSize,audioFormat.getSampleRate(),audioFormat.getFrameSize());
            //bufferSize = 4000;
            sendBuffer = new byte[bufferSize];
            receiveBuffer = new byte[bufferSize];
            
            
            playLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            playLine =  (SourceDataLine)AudioSystem.getLine(playLineInfo);
            
            
            
        //} catch (SocketException ex) {
        //    Logger.getLogger(VoiceChat.class.getName()).log(Level.SEVERE, null, ex);
        //} catch (UnknownHostException ex) {
        //    Logger.getLogger(VoiceChat.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(WebSip.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    } 
    
    
    /*
    private class MicThread extends Thread{
        @Override
        public void run(){
            try {
                recordLine.open(audioFormat);
            } catch (LineUnavailableException ex) {
                Logger.getLogger(WebSip.class.getName()).log(Level.SEVERE, null, ex);
            }
            recordLine.start();
            //playLine.start();
            int count;
            while(threadRunning){
                try {
                    count = recordLine.read(sendBuffer, 0, sendBuffer.length);
                    if (count > 0) {
                      //System.out.printf("RECORDED: %s\n", sendBuffer);
                      //RtpPacket rtpPacket= new RtpPacket(sendBuffer,sendBuffer.length);
                      //rtpSocket.send(rtpPacket);
                      //recordLine.drain();
                        //playLine.write(sendBuffer, 0, bufferSize);
                    }
                    Thread.sleep(5);
                } catch (InterruptedException ex) {
                    Logger.getLogger(WebSip.class.getName()).log(Level.SEVERE, null, ex);
                //} catch (IOException ex) {
                //    Logger.getLogger(VoiceChat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //recordLine.drain();
            //recordLine.close();
            System.out.println("Voice send close");
        }
        
    }*/
     
 } 