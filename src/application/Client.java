package application;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.net.*;
import java.io.*;

public class Client implements ActionListener{
    JTextField text;
    static JPanel a1;
    static Box vertical = Box.createVerticalBox();
    static DataOutputStream dout;
    static JFrame f = new JFrame();

    Client(){
        f.setLayout(null);
        JPanel p1 = new JPanel(); //Overhead panel
        p1.setBackground(Color.DARK_GRAY);
        p1.setBounds(0, 0, 450, 70);
        p1.setLayout(null);
        f.add(p1);

        ImageIcon i1_1 = new ImageIcon(ClassLoader.getSystemResource("icons/backicon.png"));
        Image i1_2 = i1_1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i1_3 = new ImageIcon(i1_2);
        JLabel back = new JLabel(i1_3);
        back.setBounds(5, 20, 25, 25);
        p1.add(back); //Adding Back button to overhead panel

        back.addMouseListener(new MouseAdapter(){ //Method to exit the application upon click of the Back button
            public void mouseClicked(MouseEvent ae){
                System.exit(0);
            }
        });

        ImageIcon i2_1 = new ImageIcon(ClassLoader.getSystemResource("icons/user.png")); //Adding and formatting user DP
        Image i2_2 = i2_1.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        ImageIcon i2_3 = new ImageIcon(i2_2);
        JLabel profile = new JLabel(i2_3);
        profile.setBounds(5, 20, 50, 50);
        p1.add(profile);

        JLabel name = new JLabel("User 2"); //Adding Username to overhead panel
        name.setBounds(110, 15, 100, 18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        p1.add(name);

        JLabel status = new JLabel("Online"); //Adding Username to overhead panel
        status.setBounds(110, 35, 100, 18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF", Font.BOLD, 14));
        p1.add(status);

        a1 = new JPanel(); //Text Area background for displaying messages
        a1.setBounds(5, 75, 440 ,570);
        a1.setBackground(Color.DARK_GRAY);
        f.add(a1);

        text = new JTextField(); //Message writing area
        text.setBounds(5, 655, 310, 40);
        text.setBackground(Color.DARK_GRAY);
        text.setForeground(Color.WHITE);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f.add(text);

        JButton send = new JButton("Send"); //Adding Send button to send messages
        send.setBounds(320, 655, 123, 40);
        send.setBackground(new Color(51, 153, 102));
        send.setForeground(Color.WHITE);
        send.addActionListener(this);
        send.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
       f.add(send);

        f.setSize(450, 700);//Setting Window size of our chat application
        f.setLocation(1090, 0);
        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.BLACK);//Chat area background color

        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae){
        try {
            String out = text.getText();

            JPanel p2 = formatLabel(out);

            a1.setLayout(new BorderLayout());

            JPanel right = new JPanel(new BorderLayout());
            right.add(p2, BorderLayout.LINE_END);
            right.setBackground(Color.DARK_GRAY);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            a1.add(vertical, BorderLayout.PAGE_START);

            dout.writeUTF(out);

            text.setText("");

            f.repaint();
            f.invalidate();
            f.validate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static JPanel formatLabel(String out){
        JPanel panel = new JPanel ();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.DARK_GRAY);

        JLabel output = new JLabel(out);
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(new Color(51, 153, 102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15, 15, 15, 20));

        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel time = new JLabel();
        time.setForeground(Color.WHITE);
        time.setText(sdf.format(cal.getTime()));

        panel.add(time);

        return panel;
    }

    public static void main(String[] args){
        new Client();

        try {
            Socket s = new Socket ("127.0.0.1", 6001);
            DataInputStream din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());

            while(true) {
                a1.setLayout(new BorderLayout());
                String msg = din.readUTF();
                JPanel panel = formatLabel(msg);

                JPanel left = new JPanel(new BorderLayout());
                left.setBackground(Color.DARK_GRAY);
                left.add(panel, BorderLayout.LINE_START);
                vertical.add(left);

                vertical.add(Box.createVerticalStrut(15));
                a1.add(vertical, BorderLayout.PAGE_START);
                f.validate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
