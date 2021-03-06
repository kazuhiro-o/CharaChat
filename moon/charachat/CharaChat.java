package moon.charachat;
import java.util.Properties;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CharaChat extends JFrame implements ActionListener{

	private final static String Title = "キャラチャット";
    //private final static JLabel PaneLabel = new JLabel(new ImageIcon("G:/MyProject/Java/charachat/pane.png"));
    private final static JLabel Usage = new JLabel("女の子とチャットで話してみましょう", JLabel.LEFT);
	private final static JButton Send = new JButton("送信");
    private final static int limit = 80;
    private final static Font answerFont = new Font("MS Pゴシック", Font.BOLD, 24);
    private final static Font messageFont = new Font("MS Pゴシック", Font.PLAIN, 24);
    private final static Font labelFont = new Font("MS Pゴシック", Font.BOLD, 16);
    private final static Properties gohsts = new Properties();
    private static String[] gohstList = null;
	private JPanel panel = new JPanel();
    private JPanel imagePanel = new JPanel();
    private JTextArea log = new JTextArea(20, 50);
    private JPanel emotePanel = new JPanel();
    private JScrollPane logPane = new JScrollPane(log, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel logPanel = new JPanel();
	private JLabel emoteLabel = new JLabel();
    private ImageIcon emote = new ImageIcon("emote.png");
    private JLabel faceLabel = new JLabel();
    private ImageIcon face = new ImageIcon("face.png");
    private JPanel outputPanel = new JPanel();
	private JTextArea answer = new JTextArea(50, 8);
    private JScrollPane answerPane = new JScrollPane(answer, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel inputPanel = new JPanel();
	private JTextArea message = new JTextArea(50, 8);

    public static void main(String[] args) throws Exception{
        CharaChat.loadGohsts();
		CharaChat window = new CharaChat();
        String gohst = null;
        do{
            gohst = moon.util.Util.selectBox("ゴーストを選択して下さい。", gohstList);
        }while(gohst == "");
        File dic = new File(gohsts.getProperty(gohst));
        Gohst.load(window, dic);        
		while (window.isShowing()){
			try{
				Thread.sleep(1000);
			}
			catch(InterruptedException e){
			}
            Gohst.storeUser();
		}
		System.exit(0);
    }

	public CharaChat(){
		super(CharaChat.Title);
		this.setResizable(false);
		this.setAlwaysOnTop(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//this.setLocationRelativeTo(null);
        this.setMaximumSize(new Dimension(1200, 900));
		this.setSize(1200, 900);
        this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));
        this.panel.setMaximumSize(new Dimension(1200, 900));
		this.panel.setSize(1200, 900);
        this.imagePanel.setLayout(new BoxLayout(this.imagePanel, BoxLayout.X_AXIS));
        this.imagePanel.setMaximumSize(new Dimension(1200, 500));
        this.imagePanel.setSize(1200, 500);
        this.logPanel.setLayout(new BoxLayout(this.logPanel, BoxLayout.X_AXIS));
        this.logPanel.setMaximumSize(new Dimension(200, 300));
        this.logPanel.setSize(200, 300);
        this.emotePanel.setLayout(new BoxLayout(this.emotePanel, BoxLayout.Y_AXIS));
        this.emotePanel.setMaximumSize(new Dimension(200, 500));
        this.emotePanel.setSize(200, 500);
        this.outputPanel.setLayout(new BoxLayout(this.outputPanel, BoxLayout.X_AXIS));
        this.outputPanel.setMaximumSize(new Dimension(1200, 100));
        this.outputPanel.setSize(1200, 100);
        this.inputPanel.setLayout(new BoxLayout(this.inputPanel, BoxLayout.X_AXIS));
        this.inputPanel.setMaximumSize(new Dimension(1200, 100));
        this.inputPanel.setSize(1200, 100);
		this.answer.setLineWrap(true);
		this.answer.setEditable(false);
        this.emotePanel.add(this.logPane);
        this.emoteLabel.setIcon(this.emote);
        this.emotePanel.add(this.emoteLabel);
        this.faceLabel.setIcon(this.face);
        this.imagePanel.add(this.emotePanel);
        this.imagePanel.add(this.faceLabel);
        this.answer.setFont(CharaChat.answerFont);
		this.outputPanel.add(this.answerPane);
		CharaChat.Send.addActionListener(this);
        this.message.setFont(CharaChat.messageFont);
		this.inputPanel.add(this.message);
        CharaChat.Send.setFont(CharaChat.labelFont);
		this.inputPanel.add(CharaChat.Send);
        this.panel.add(this.imagePanel);
        this.panel.add(this.outputPanel);
        CharaChat.Usage.setFont(CharaChat.labelFont);
		this.panel.add(CharaChat.Usage);
        this.panel.add(this.inputPanel);
		this.getContentPane().add(this.panel);
		this.setVisible(true);
	}

    public void printLog(String str){
        this.log.append(str + "\n");
    }

    public void setFace(String filename){
        this.face = new ImageIcon(filename);
        this.faceLabel.setIcon(this.face);
        //this.faceLabel.repaint();
    }

    public void setEmote(String filename){
        this.emote = new ImageIcon(filename);
        this.emoteLabel.setIcon(this.emote);
        //this.emoteLabel.repaint();
    }

	public synchronized void actionPerformed(ActionEvent e){
		JButton button = (JButton)e.getSource();
		String str = this.message.getText();
		if(str.length() <= CharaChat.limit){
            this.answer.append("[" + User.getFirstName() + "] " + str + "\n");
            this.answer.paintImmediately(this.answer.getBounds());
            this.message.setText("");
            this.message.paintImmediately(this.message.getBounds());
            try{
                Thread.sleep(2000);
            }
            catch(InterruptedException ie){

            }
			this.answer.append("[" + Gohst.getFirstName() + "] " + Gohst.sendMessage(str) + "\n");
            this.answer.paintImmediately(this.answer.getBounds());
		}
        else{
            this.answer.append("文章が長すぎます\n");
        }
	}

    private static void loadGohsts(){
        String path = System.getProperty("user.dir");
        File file = new File(path + "\\gohsts.ini");
        try{
            FileReader fr = new FileReader(file);
            gohsts.load(fr);
        }
        catch(Exception e){
            System.out.println("gohsts.iniがありません");
            System.exit(0);
        }
        gohstList = gohsts.keySet().toArray(new String[gohsts.size()]);
    }

}