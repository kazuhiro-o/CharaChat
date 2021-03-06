package moon.util;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextArea;

public class Util{

	public static String inputBox(String msg){
		return JOptionPane.showInputDialog(new DefaultKeyboardFocusManager().getActiveWindow(), msg);
	}

	public static String selectBox(String title, String[] args){
		int width = 0;
		int height = 0;
		for(int i = 0; i < args.length; i++){
			if(width < args[i].length()){
				width = args[i].length();
			}
			height++;
		}
		width = width * 23 + 20;
		height = height * 40 + 30;
		MyFrame dialog = new MyFrame(title);
		JPanel panel = new JPanel();
		dialog.setLocationRelativeTo(null);
		dialog.setSize(width + 50, height);
		panel.setSize(width + 50, height);
		JButton[] button = new JButton[args.length];
		for(int i = 0; i < args.length; i++){
			button[i] = new JButton(args[i]);
			button[i].setPreferredSize(new Dimension(width, 30));
			button[i].addActionListener(dialog);
			panel.add(button[i]);
		}
		dialog.getContentPane().add(panel, BorderLayout.CENTER);
		dialog.setVisible(true);
		while (dialog.isShowing()){
			try{
				Thread.sleep(1000);
			}
			catch(InterruptedException e){
			}
		}
		for(int i = 0; i < args.length; i++){
			if(button[i].equals(dialog.Button)){
				return new String(args[i]);
			}
		}
		return "";
	}

}

class MyFrame extends JDialog implements ActionListener{

	JButton Button = null;

	MyFrame(){
		super();
		setResizable(false);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	MyFrame(String title){
		super(new MyFrame(), title);
	}

	public void actionPerformed(ActionEvent e){
		Button = (JButton)e.getSource();
		this.dispose();
	}
	
}
