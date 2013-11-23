/*
 * Main GUI
 */
package sipua;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;


public class MainWindow extends JFrame {
    
    private JTextPane logPane;
    private JTextPane msgPane;
    private JTextField inputField;
    private JButton okButton;
    private JButton cancelButton;
    private GUIActionListener listener;
    
    public MainWindow(){
        super("Simple Sip UA");
        //this.mainWinRef = mainWinRef;
        //init();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(360,420);
        this.setResizable(false);
        this.addWindowListener(new closeEventWindowListener());
        initComponents();
    }
    
    private void initComponents(){
        inputField = new JTextField(15);
        msgPane = new JTextPane();
        logPane = new JTextPane();
        okButton = new JButton("Call");
        cancelButton = new JButton("Cancel");
        listener = new GUIActionListener();
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        
        
        
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.ipady = 150;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(msgPane,gbc);
        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(inputField);
        inputPanel.add(okButton);
        inputPanel.add(cancelButton);
        
        okButton.addActionListener(listener);
        
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
        gbc.ipady = 150;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(logPane,gbc);
        
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
    
    private class closeEventWindowListener extends WindowAdapter{
        @Override
        public void windowClosing(WindowEvent e) {
            
        }
    }
    
    private class GUIActionListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == MainWindow.this.okButton){
                    System.out.println("ok button");
                }
            }
    }
    

}
