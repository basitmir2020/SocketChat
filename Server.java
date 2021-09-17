import java.net.*;
import java.io.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.*;
import javax.swing.SwingConstants;
class Server extends JFrame{

    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    private JLabel heading = new JLabel("Server Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);
    
    public Server(){
        try {
            // server = new ServerSocket(7777);
            // System.out.println("Server Is Ready To Accept Connection!");
            // System.out.println("Waiting......");
            // socket = server.accept();

            // br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // out = new PrintWriter(socket.getOutputStream());
            // //CreateGUI();
            // //handleEvents();
            // //startReading();
            // // startWriting();

        } catch (Exception e) {
            System.out.println("Port is Already Used!");
        }
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
              if(e.getKeyCode() == 10){
                  String contentToSend = messageInput.getText();
                  messageArea.append("Me :" +contentToSend+"\n");
                  out.println(contentToSend);
                  out.flush();
                  messageInput.setText("");
                  messageInput.requestFocus();
              }
            }
        });
    }

    private void CreateGUI(){
        this.setTitle("Chat App");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font); 

        heading.setIcon(new ImageIcon("server.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        this.setLayout(new BorderLayout());

        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);
    }

    public void startReading(){
        Runnable r1 = ()->{
            try{
                while(true){
                    String msg = br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("Client Left!");
                        JOptionPane.showMessageDialog(this,"Client Left!");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    System.out.println("Client : "+msg );
                    messageArea.append("Client : " + msg+"\n");
                }
            }catch (Exception e) {
                System.out.println("Connection Closed!"); 
            }
        };
        new Thread(r1).start();
    }

    public void startWriting(){
        Runnable r2 = ()->{
            try {
                while(!socket.isClosed()){
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();

                    out.println(content); 
                    out.flush();

                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Connection Closed!"); 
            }
        };
        new Thread(r2).start();
    }
    public static void main(String[] args) {
        System.out.println("Server Is Running.....");
        new Server();
    }
}