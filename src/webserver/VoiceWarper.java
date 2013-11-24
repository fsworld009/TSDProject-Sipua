
package webserver;

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
import local.net.RtpPacket;
import local.net.RtpSocket;
import org.zoolu.net.IpAddress;
import org.zoolu.net.UdpSocket;
import sipua.VoiceChat;


public class VoiceWarper {
    private boolean threadRunning=false;
    private UdpSocket udpSocket;
    private RtpSocket rtpSocket;
    private int bufferSize;
    private byte[] sendBuffer;
    private byte[] receiveBuffer;
    private SendThread sendThread;
    private ReceiveThread receiveThread;
    
    String connectAddress;
    int connectPort;
    
    public void init(String address, int port){
        connectAddress = address;
        connectPort = port;
        try {        
            udpSocket = new UdpSocket(connectPort);
            rtpSocket = new RtpSocket(udpSocket,IpAddress.getByName(connectAddress),connectPort);

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
            
            bufferSize = 128;   //bufer size
            //System.out.printf("fsdfs %d %f %d", bufferSize,audioFormat.getSampleRate(),audioFormat.getFrameSize());
            //bufferSize = 4000;
            sendBuffer = new byte[bufferSize];
            receiveBuffer = new byte[bufferSize];
            
            
            
        } catch (SocketException ex) {
            Logger.getLogger(VoiceChat.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(VoiceChat.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }
    
    public void start(){
        threadRunning = true;
        //sendThread = new SendThread();
        receiveThread = new ReceiveThread();
        //sendThread = new SendThread();
        //receiveThread = new ReceiveThread();
        
        
        
        
        //sendThread.start();
        receiveThread.start();
    }
    
    private class SendThread extends Thread{
        @Override
        public void run(){
            int count;
            while(threadRunning){
                try {
                      //get data
                      RtpPacket rtpPacket= new RtpPacket(sendBuffer,sendBuffer.length);
                      rtpSocket.send(rtpPacket);
                      //recordLine.drain();
                    Thread.sleep(5);
                } catch (InterruptedException ex) {
                    Logger.getLogger(VoiceChat.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(VoiceChat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //recordLine.drain();
            //recordLine.close();
            System.out.println("Voice send close");
        }
        
    }
    
    private class ReceiveThread extends Thread{
        @Override
        public void run(){


            while(threadRunning){
                try {
                    RtpPacket rtpPacket=new RtpPacket(receiveBuffer,bufferSize);
                    rtpSocket.receive(rtpPacket);
                    System.out.printf("RECV: %s\n",rtpPacket.getPacket());
                    
                    //send to VoiceChat

                    Thread.sleep(5);
                } catch (InterruptedException ex) {
                    Logger.getLogger(VoiceChat.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(VoiceChat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //playLine.drain();
            //playLine.close();
            System.out.println("Voice recv close");
        }
        
    }
}
