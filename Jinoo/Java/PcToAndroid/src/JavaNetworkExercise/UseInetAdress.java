package JavaNetworkExercise;

import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;

public class UseInetAdress {
	TextField in_txt;
	TextArea out_txt;

	public static void main(String args[]) {
		new UseInetAdress().proc();
	}

	public void proc() {
		JFrame frm = new JFrame("UseInetAdress");

		in_txt = new TextField("도메인명 : ");
		in_txt.addActionListener(new Search_A());
		out_txt = new TextArea();
		
		frm.add(out_txt, "Center");
		frm.add(in_txt, "South");
		frm.setSize(250, 250);
		frm.setVisible(true);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	class Search_A implements ActionListener{
		public void actionPerformed(ActionEvent evt) {
			String host = in_txt.getText();
			
			try {
				InetAddress addr = InetAddress.getByName(host);
				
				out_txt.append("\n 검색된 IP주소: " + addr.getHostAddress());
				
			}catch(UnknownHostException excp){
				out_txt.append("\n getByName() 에러!");
			}
		}
	}
}
