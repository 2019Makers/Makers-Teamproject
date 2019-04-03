package JavaNetworkExercise;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
 
import java.text.SimpleDateFormat;
 
import java.util.Scanner;
import java.util.Date;
 
public class EchoClient {
    
    private Socket             soc;    // ������ ������ ����
    private BufferedReader     br;        // ������ ���� �޼����� ���� ����
    private PrintWriter     pw;        // ������ �޼����� ���� ������
    private Scanner            scan;    // ����ڰ� �Է��� �����͸� ���� ��ĳ��
    
    public EchoClient(){
        init();
    }
    
    public void init(){
        
        try{
            soc = new Socket("54.180.42.62", 8888);    // �������� ��Ʈ�� ���� ������ ���ӽõ�
            System.out.println(getTime() + "Accept to Server Success!!");
            
            // ����� �����κ��� �����͸� �޾ƿ� �غ� �Ѵ�
            br = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            // ����� ������ �����͸� ���� �غ� �Ѵ�
            pw = new PrintWriter(soc.getOutputStream());
            
            scan = new Scanner(System.in);
            
            System.out.println("now, you can chat!!");
            String inputData = "";
            
            while(!inputData.equals("exit")){
                System.out.printf(getTime()+"to Server > ");
                // ����ڰ� �Է��� ������ �о�� ������ �����ϴ�
                
                inputData = scan.nextLine();
                pw.println(inputData);
                pw.flush(); // ����Ʈ������ �޸𸮸� �ʱ�ȭ���� ���ο� �ִ� �����͸� ������ ����
                System.out.println(getTime() + "from Server > "+br.readLine());
            }
            
            soc.close();
            
        }catch(ConnectException ce){
            System.out.println("������ �����ϰ� �����ʾ� ���α׷��� �����մϴ�...");
            System.exit(0);
        
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public String getTime(){
        SimpleDateFormat f = new SimpleDateFormat("[hh:mm:ss]");
        return f.format(new Date());
    }
 
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        
        new EchoClient();
    }
 
}