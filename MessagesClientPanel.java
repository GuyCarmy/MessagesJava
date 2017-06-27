import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Guy Carmy on 25/06/2017.
 * ID: 301726154
 *
 * this is the panel for the client side on the messages program
 * it contains the GUI for the user
 * all the connection is dealt on the other thread (MessagesClientThread)
 */
public class MessagesClientPanel extends Panel {
    //panel components
    private JTextArea textArea;
    private JScrollPane jsp;
    private JButton clearBtn;
    private JButton joinBtn;
    private JButton leaveBtn;
    private Panel menu;
    //the MessagesClientThread handles with the UDP
    private MessagesClientThread clientThread;


    //constructor
    public MessagesClientPanel(){
        //setting the panel
        textArea=new JTextArea(10,30);
        textArea.setEditable(false);
        jsp=new JScrollPane(textArea);
        clearBtn=new JButton("Clear");
        joinBtn=new JButton("Join");
        leaveBtn=new JButton("Leave");
        clearBtn.addActionListener(new BtnListener());
        joinBtn.addActionListener(new BtnListener());
        leaveBtn.addActionListener(new BtnListener());
        this.setLayout(new BorderLayout());
        this.add(jsp,BorderLayout.CENTER);
        menu=new Panel(new FlowLayout());
        menu.add(clearBtn);
        menu.add(joinBtn);
        menu.add(leaveBtn);
        this.add(menu,BorderLayout.SOUTH);
        //creating the MessagesClientThread
        clientThread=new MessagesClientThread(this);

    }

    //a basic method so the clientThread can as the panel to edit the (private) text area
    public void appendMsg(String msg){
        textArea.append(msg);
    }

    //the listener for all the buttons
    private class BtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()){
                case "Clear":
                    //just clear the text component
                    textArea.setText("");
                    break;
                case "Join":
                    //input from user is the host name
                    String input=JOptionPane.showInputDialog("Please enter the name of the server you want to join");
                    //letting the clientThread know the host name
                    clientThread.setServer(input);
                    //start the thread
                    clientThread.start();
                    break;
                case "Leave":
                    //client thread deals with the connection
                    clientThread.leaveServer();
                    break;
            }

        }
    }

    //in the main the frame is created and the panel added to it
    public static void main(String[]args){
        MessagesClientPanel client= new MessagesClientPanel();
        JFrame frame=new JFrame();
        frame.setSize(400,400);
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.add(client);
        frame.setVisible(true);

    }
}
