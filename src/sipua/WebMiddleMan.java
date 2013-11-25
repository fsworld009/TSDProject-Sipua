
package sipua;


public class WebMiddleMan implements TcpSocketEventListener, SipUAEventListener {
    private TcpSocket socket;
    private int tcpPort=10002;
    //private String remoteIp;
    MainWindow uiRef;
    SipUA sipRef;
    
    public WebMiddleMan(MainWindow ref,SipUA ua){
        uiRef= ref;
        sipRef = ua;
    }
    public void start(String ip){
        //remoteIp = ip;
        socket = new TcpSocket();
        socket.registerEventListener(this);
        socket.startServer(ip,tcpPort);
    }

    @Override
    public void onAccept() {

    }

    @Override
    public void onConnect() {
        
    }

    @Override
    public void onReceive(String msg) {
        if(msg.contains("CALL")){
            String[] addr = msg.split("\\s+");
            uiRef.call(addr[1],Integer.parseInt(addr[2]));
        }else if (msg.equals("CANCEL")){
            uiRef.closeCall();
        }else if(msg.equals("ACCEPT")){
            //uiRef;
        }
    }
    
    public void closeServer(){
        socket.closeServer();
    }

    @Override
    public void onCallAccepted() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        socket.send("ACCEPTED");
    }

    @Override
    public void onCallBye() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        socket.send("BYE");
    }

    @Override
    public void onCallCancel() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        socket.send("CANCELED");
    }

    @Override
    public void onCallConfirmed() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        socket.send("CONFIRMED");
    }

    @Override
    public void onCallInvite(String remoteAddr) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        socket.send("INVITE FROM "+remoteAddr);
    }

    @Override
    public void onCallRefused() {
        //throw new UnsuportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        socket.send("REFUSED");
    }

    @Override
    public void onCallRinging() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        socket.send("RING");
    }
    
}
