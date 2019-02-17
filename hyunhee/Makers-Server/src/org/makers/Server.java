package org.makers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import static org.makers.utils.ReceiveMessage.*;

public class Server extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private final int PORT = 3000;

    private final int x_size = 250;// frame 가로크기
    private final int y_size = 180; // frame 세로크기
    private int x_screenSize; // screen 가로크기
    private int y_screenSize; // screen 세로크기

    private Container contentPane;
    private JFrame frame;
    private JPanel panel;
    private JLabel ipLabel;
    private JComboBox ipList;
    private JButton Start;
    private JButton Stop;

    private ThreadServer threadServer;

    public void run() {

        init();
        setLocalIp();

    }

    private void init() {

        // frame 생성 및 크기와 위치 설정
        frame = new JFrame("Server");
        contentPane = frame.getContentPane();
        frame.setPreferredSize(new Dimension(x_size, y_size));
        Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
        x_screenSize = res.width;
        y_screenSize = res.height;
        frame.setLocation((x_screenSize - x_size) / 2, (y_screenSize - y_size) / 2);

        panel = new JPanel();
        panel.setLayout(null);
        contentPane.add(panel);

        ipLabel = new JLabel("IP");
        ipLabel.setBounds(20, 10, 200, 30);

        ipList = new JComboBox();
        ipList.setBounds(20, 40, 200, 30);

        Start = new JButton("Start");
        Start.setBounds(20, 90, 90, 30);
        Start.addActionListener(this);

        Stop = new JButton("Stop");
        Stop.setBounds(130, 90, 90, 30);
        Stop.addActionListener(this);
        Stop.setEnabled(false);

        panel.add(ipLabel);
        panel.add(ipList);
        panel.add(Start);
        panel.add(Stop);

        // frame 속성
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if ((JButton) e.getSource() == Start) {
            threadServer = new ThreadServer();
            threadServer.start();
            Start.setEnabled(false);
            Stop.setEnabled(true);
        }
        if ((JButton) e.getSource() == Stop) {
            threadServer.interrupt();
            Stop.setEnabled(false);
            Start.setEnabled(true);
        }
    }

    private void setLocalIp() {

        InetAddress ia[] = null;
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            ia = InetAddress.getAllByName(hostName);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < ia.length; i++) {
            InetAddress inet = ia[i];
            ipList.addItem((new StringBuilder()).append(inet.getHostAddress()).toString());
        }
    }

    class ThreadServer extends Thread {

        private Robot robot;
        private PointerInfo mouse;
        private Point location;
        private String str[];

        private ServerSocket serverSocket;
        private Socket socket;

        private BufferedReader in;

        public ThreadServer() {
            super();
            try {
                robot = new Robot();
            } catch (AWTException e) {
                e.printStackTrace();
            }
            try {
                serverSocket = new ServerSocket(PORT);
                System.out.println("Server Start");
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void interrupt() {
            try {
                if(socket != null)
                    socket.close();
                serverSocket.close();
                System.out.println("Server Stop");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            System.out.println("waiting client...");
            try {
                socket = serverSocket.accept();
                System.out.println("connect client");

                InetAddress ip = socket.getInetAddress();
                System.out.println(ip + "접속");
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                do {
                    String msg = null;
                    msg = in.readLine();

                    if (msg != null) {
                        process(msg.trim());
                    }
                } while (true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void process(String msg) {
            mouse = MouseInfo.getPointerInfo();
            location = mouse.getLocation();

            int x = (int) location.getX();
            int y = (int) location.getY();

            str = msg.split(":");

            System.out.println(str[0]);
            if (str[0].compareTo(LeftClick) == 0) {
                robot.mousePress(16);
                robot.mouseRelease(16);
            } else if (str[0].compareTo(RightClick) == 0) {
                robot.mousePress(4);
                robot.mouseRelease(4);
            } else if (str[0].compareTo(MoveMouse) == 0) {
                robot.mouseMove(x + Integer.parseInt(str[1]), y + Integer.parseInt(str[2]));
            }
        }
    }
}