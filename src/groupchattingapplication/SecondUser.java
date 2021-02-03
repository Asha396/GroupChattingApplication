package groupchattingapplication;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ScrollBarUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class SecondUser implements ActionListener, Runnable {

    JPanel header;
    JTextField message;
    JButton send;
    static JPanel screen;
    static JFrame frame = new JFrame();

    static Box vertical = Box.createVerticalBox();

    BufferedWriter writer;
    BufferedReader reader;

    SecondUser() {

        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        header = new JPanel();
        header.setLayout(null);
        header.setBackground(new Color(7, 94, 84));
        header.setBounds(0, 0, 450, 70);
        frame.add(header);

        ImageIcon arrow1 = new ImageIcon(ClassLoader.getSystemResource("groupchattingapplication/icons/arrow.png"));
        Image arrow2 = arrow1.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon arrow3 = new ImageIcon(arrow2);
        JLabel backArrow = new JLabel(arrow3);
        backArrow.setBounds(5, 17, 30, 30);
        header.add(backArrow);

        backArrow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent ae) {
                System.exit(0);
            }
        });

        ImageIcon dustin1 = new ImageIcon(ClassLoader.getSystemResource("groupchattingapplication/icons/strangerthings.jpg"));
        Image dustin2 = dustin1.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT);
        ImageIcon dustin3 = new ImageIcon(dustin2);
        JLabel senderImage = new JLabel(dustin3);
        senderImage.setBounds(40, 5, 60, 60);
        header.add(senderImage);

        JLabel senderName = new JLabel("Stranger Things");
        senderName.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        senderName.setForeground(Color.WHITE);
        senderName.setBounds(110, 15, 140, 20);
        header.add(senderName);

        JLabel senderStatus = new JLabel("Will, Mike, Dustin, Lucas, Eleven, Steve, Nancy, Max");
        senderStatus.setFont(new Font("SAN_SERIF", Font.PLAIN, 14));
        senderStatus.setForeground(Color.WHITE);
        senderStatus.setBounds(110, 35, 180, 20);
        header.add(senderStatus);

        ImageIcon video1 = new ImageIcon(ClassLoader.getSystemResource("groupchattingapplication/icons/video.png"));
        Image video2 = video1.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon video3 = new ImageIcon(video2);
        JLabel videoIcon = new JLabel(video3);
        videoIcon.setBounds(310, 20, 30, 30);
        header.add(videoIcon);

        ImageIcon phone1 = new ImageIcon(ClassLoader.getSystemResource("groupchattingapplication/icons/phone.png"));
        Image phone2 = phone1.getImage().getScaledInstance(30, 35, Image.SCALE_DEFAULT);
        ImageIcon phone3 = new ImageIcon(phone2);
        JLabel phoneIcon = new JLabel(phone3);
        phoneIcon.setBounds(360, 18, 30, 35);
        header.add(phoneIcon);

        ImageIcon dots1 = new ImageIcon(ClassLoader.getSystemResource("groupchattingapplication/icons/dots.png"));
        Image dots2 = dots1.getImage().getScaledInstance(10, 25, Image.SCALE_DEFAULT);
        ImageIcon dots3 = new ImageIcon(dots2);
        JLabel dotsIcon = new JLabel(dots3);
        dotsIcon.setBounds(410, 20, 10, 25);
        header.add(dotsIcon);

        screen = new JPanel();
        screen.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        //screen.setBounds(5, 75, 440, 570);
        //frame.add(screen);

        JScrollPane scroll = new JScrollPane(screen);
        scroll.setBounds(5, 75, 440, 570);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        ScrollBarUI ui = new BasicScrollBarUI() {
            @Override
            protected JButton createDecreaseButton(int i) {
                JButton button = super.createDecreaseButton(i);
                button.setBackground(new Color(7, 94, 84));
                button.setForeground(Color.WHITE);
                this.thumbColor = new Color(7, 94, 84);
                return button;
            }

            @Override
            protected JButton createIncreaseButton(int i) {
                JButton button = super.createIncreaseButton(i);
                button.setBackground(new Color(7, 94, 84));
                button.setForeground(Color.WHITE);
                this.thumbColor = new Color(7, 94, 84);
                return button;
            }
        };

        scroll.getVerticalScrollBar().setUI(ui);
        frame.add(scroll);

        message = new JTextField();
        message.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        message.setBounds(5, 650, 320, 45);
        frame.add(message);

        send = new JButton("Send");
        send.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        send.setForeground(Color.WHITE);
        send.setBounds(330, 650, 113, 45);
        send.setBackground(new Color(7, 94, 84));
        send.addActionListener(this);
        frame.add(send);

        frame.getContentPane().setBackground(Color.WHITE);
        frame.setLayout(null);
        frame.setSize(450, 700);
        frame.setLocation(490, 200);
        frame.setUndecorated(true);
        frame.setVisible(true);

        try {
            Socket socketClient = new Socket("localhost", 6001);
            writer = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));

        } catch (Exception e) {
            System.out.println("Exception in constructor (SecondUser) : " + e);
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String out = "Will : " + message.getText();
        try {
            sendTextToFile(out);
            JPanel chat = formatLabel(out);

            screen.setLayout(new BorderLayout());

            JPanel right = new JPanel(new BorderLayout());
            right.add(chat, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            screen.add(vertical, BorderLayout.PAGE_START);

            //screen.add(chat);
            writer.write(out);
            writer.write("\r\n");
            writer.flush();

        } catch (Exception e) {
            System.out.println("Exception in actionPerformed method (SecondUser) : " + e);
            e.printStackTrace();
        }
        message.setText("");
    }

    public void sendTextToFile(String message) throws FileNotFoundException {
        try (FileWriter file = new FileWriter("chat.txt", true);
                PrintWriter print = new PrintWriter(new BufferedWriter(file));) {
            print.println(message);
        } catch (Exception e) {
            System.out.println("Exception in sendTextToFile method (SecondUser) : " + e);
            e.printStackTrace();
        }
    }

    public static JPanel formatLabel(String out) {
        JPanel chat = new JPanel();
        chat.setLayout(new BoxLayout(chat, BoxLayout.Y_AXIS));

        JLabel text = new JLabel("<html><p style = \"width : 150px\">" + out + "</p></html>");
        text.setFont(new Font("Tahoma", Font.PLAIN, 16));
        text.setBackground(new Color(37, 211, 102));
        text.setOpaque(true);
        text.setBorder(new EmptyBorder(15, 15, 15, 15));

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));

        chat.add(text);
        chat.add(time);
        return chat;
    }

    public static void main(String[] args) {
        //new SecondUser().frame.setVisible(true);
        SecondUser user = new SecondUser();
        Thread thread = new Thread(user);
        thread.start();
    }

    @Override
    public void run() {
        try {
            String msg = "";
            while ((msg = reader.readLine()) != null) {
                // screen.append(msg + "\n");
                JPanel chat = formatLabel(msg);
                JPanel left = new JPanel(new BorderLayout());
                left.add(chat, BorderLayout.LINE_START);
                vertical.add(left);
                frame.validate();
            }
        } catch (Exception e) {
            System.out.println("Exception in run method (SecondUser) : " + e);
            e.printStackTrace();
        }
    }
}
