package sipua;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import local.net.RtpPacket;
import local.net.RtpSocket;
import org.zoolu.net.IpAddress;
import org.zoolu.net.UdpSocket;




public class VoiceChat {
    byte[] sendBuffer = new byte[1024];
    byte[] receiveBuffer = new byte[1024];
    String connectAddress;
    int connectPort;
    //SipURL recvSipURL;
    //NameAddress recvNameAddress;
    
    UdpSocket udpSocket;
    RtpSocket rtpSocket;
    
    boolean threadRunning = false;
    
    MicThread micThread;
    SpeakerThread speakerThread;
    SendThread sendThread;
    ReceiveThread receiveThread;
    
    public void init(String address, int port){
        connectAddress = address;
        connectPort = port;
        try {        
            udpSocket = new UdpSocket(connectPort);
            rtpSocket = new RtpSocket(udpSocket,IpAddress.getByName(connectAddress),connectPort);
        } catch (SocketException ex) {
            Logger.getLogger(VoiceChat.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(VoiceChat.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    public void start(){
        threadRunning = true;
        micThread = new MicThread();
        speakerThread = new SpeakerThread();
        sendThread = new SendThread();
        receiveThread = new ReceiveThread();
        micThread.start();
        speakerThread.start();
        sendThread.start();
        receiveThread.start();
    }
    
    private class MicThread extends Thread{
        @Override
        public void run(){
            while(threadRunning){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(VoiceChat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
    
    private class SpeakerThread extends Thread{
        @Override
        public void run(){
            while(threadRunning){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(VoiceChat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
    
    private class SendThread extends Thread{
        @Override
        public void run(){
            while(threadRunning){
                try {
                    String data = "123456789ABC";
                    byte[] byteData = data.getBytes();
                    //System.out.printf("%s", byteData);
                    try {
                        RtpPacket rtpPacket= new RtpPacket(byteData,byteData.length);
                        rtpSocket.send(rtpPacket);
                    } catch (IOException ex) {
                        Logger.getLogger(SipUA.class.getName()).log(Level.SEVERE, null, ex);
                        System.err.println("rtpSocket.send(new RtpPacket(data,4)) failed");
                    }
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(VoiceChat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
        
    private class ReceiveThread extends Thread{
        @Override
        public void run(){
            while(threadRunning){
                try {
                    byte[] byteData = new byte[12];
                    RtpPacket rtpPacket=new RtpPacket(byteData,12);
                    rtpSocket.receive(rtpPacket);
                    System.out.printf("RECV: %s\n",rtpPacket.getPacket());
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(VoiceChat.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(VoiceChat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
    
}
