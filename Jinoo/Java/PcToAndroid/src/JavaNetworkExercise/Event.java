package JavaNetworkExercise;

import java.awt.Button;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Event {
	TextArea out_txt;
	public static void main(String args[]) {
		new Event().proc();
	}
	
	public void proc() {
		Frame frm = new Frame("Test");
		
		Button btn = new Button("이벤트 테스트");
		out_txt = new TextArea();
		
		frm.setSize(150, 150);
		frm.add("South", btn);
		frm.add(out_txt, "North");
		frm.setVisible(true);
		
		btn.addActionListener(new Proc_Button());
	}
	
	class Proc_Button implements ActionListener{
		public void actionPerformed(ActionEvent ev) {
			out_txt.append("\n 단추 눌려짐...");
		}
	}
}
