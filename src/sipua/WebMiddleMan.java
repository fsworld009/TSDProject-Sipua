
package sipua;


public class WebMiddleMan implements TcpSocketEventListener {
    private TcpSocket socket;
    private int tcpPort=10002;
    //private String remoteIp;
    MainWindow uiRef;
    
    public WebMiddleMan(MainWindow ref){
        uiRef= ref;
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
        if(msg.indexOf("CALL")==0){
            String[] addr = msg.split("\\s+");
            uiRef.call(addr[1],Integer.parseInt(addr[2]));
        }
    }
    
    public void closeServer(){
        socket.closeServer();
    }
    
}
