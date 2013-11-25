/*
 * Main GUI
 */
package sipua;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.call.Call;
import org.zoolu.sip.message.Message;
import webserver.WebServer;

public class MainWindow extends JFrame {
    
    private JTextPane logPane;
    private JTextPane msgPane;
    private JTextField inputField;
    private JButton okButton;
    private JButton cancelButton;
    private GUIActionListener listener;
    private SipUA sipUA;
    private int state;  //0=caller, 1=callee
    private WebServer webServer;
    private WebMiddleMan webMiddleMan;
    private String remoteIp;
    private int remoteRtpPort = 10003;
    private boolean remoteControl=false;
    
    public MainWindow(){
        super("Simple Sip UA");
        //this.mainWinRef = mainWinRef;
        //init();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(360,480);
        this.setResizable(false);
        this.addWindowListener(new closeEventWindowListener());
        initComponents();
        sipUA = new SipUA(this);
        sipUA.start();
        webServer=new WebServer(this);
        webServer.start();
        //
    }
    
    public void called(final Call call,final NameAddress callee,final NameAddress caller,final java.lang.String sdp, final Message invite){
        //JDialog dialog = new JDialog(this,"You got a call from"+addr);
        //dialog.set
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if(JOptionPane.showConfirmDialog(null,"You got a call from"+invite.getRemoteAddress(),"New call",JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION){
                    sipUA.acceptCall(call,callee,caller,sdp,invite);
                }else{
                    sipUA.refuseCall(call,callee,caller,sdp,invite);
                }
            }
        });

        
    }
    
    private void initComponents(){
        inputField = new JTextField(15);
        msgPane = new JTextPane();
        msgPane.setEditable(false);
        JScrollPane msgScrollPane = new JScrollPane(msgPane);
        //msgScrollPane.setPreferredSize( new Dimension( 360, 200 ) );
        
        logPane = new JTextPane();
        logPane.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logPane);
        okButton = new JButton("Call");
        cancelButton = new JButton("Close");
        listener = new GUIActionListener();
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        
        
        
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.ipady = 150;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(msgScrollPane,gbc);
        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(inputField);
        inputPanel.add(okButton);
        inputPanel.add(cancelButton);
        
        okButton.addActionListener(listener);
        cancelButton.addActionListener(listener);
        
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weighty = 0.5;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(inputPanel,gbc);
        
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.5;
        gbc.ipady = 200;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(logScrollPane,gbc);
        
    }
    
    public void appendLog(final String newLog){     
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    logPane.getDocument().insertString(logPane.getDocument().getLength(), newLog, null);
                } catch (BadLocationException ex) {
                    System.err.println("BadLocationException");
                }
            }
        });
    }
    
    public void appendMsg(final String newLog){     
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    msgPane.getDocument().insertString(msgPane.getDocument().getLength(), newLog, null);
                } catch (BadLocationException ex) {
                    System.err.println("BadLocationException");
                }
            }
        });
    }
    
    private class closeEventWindowListener extends WindowAdapter{
        @Override
        public void windowClosing(WindowEvent e) {
            
        }
    }
    
    private class GUIActionListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!remoteControl){
                    if(e.getSource() == MainWindow.this.okButton){
                        String[] sipAddrs = inputField.getText().split("\\s+");
                        if(sipAddrs.length==2){
                            call(sipAddrs[0],Integer.parseInt(sipAddrs[1]));
                        }else{
                            appendMsg("Input format error\n");
                        }
                    }else if(e.getSource() == MainWindow.this.cancelButton){
                        closeCall();
                    }
                }else{
                    appendMsg("You are being remote controlled by "+remoteIp+"\n");
                }
            }
    }
    
    public void call(String ip,int port){
        sipUA.call(ip,port);
    }
    
    public void closeCall(){
        sipUA.closeCall();
    }
    
    public void setRemoteIp(String ip){
        remoteIp = ip;
        System.out.println(ip);
        
        if(webMiddleMan == null){
            webMiddleMan = new WebMiddleMan(this,sipUA);
        }else{
            webMiddleMan.closeServer();
         }
        
        sipUA.remoteRTPAddress(remoteIp,remoteRtpPort);   //need improve
        webMiddleMan.start(remoteIp);
    }
    
    public void remoteLogin(){
        appendMsg("Remote control from "+remoteIp+"\n");

            
        //temp
        sipUA.addEventListener(webMiddleMan);
        remoteControl = true;

    }
    
    public void remoteLogout(){
        sipUA.removeEventListener();
        remoteControl = false;
        webMiddleMan.closeAcceptSocket();
        appendMsg("Remote user logoutted\n");
    }
    

}
