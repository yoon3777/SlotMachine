package �ڹٽ��Ըӽ�;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Logon_Frame extends JFrame{
	
	private JLabel idLabel;
	private JLabel pwLabel;
	
	private JTextField userid; // ���̵��Է�
	private JPasswordField userpw; // ��й�ȣ�Է�

	private JButton loginBtn;
	private JButton signupBtn;
	
	private String id = ""; // field���� �Է¹��� ���� �����ϴ� ����
	private String pw = ""; // field���� �Է¹��� ���� �����ϴ� ����
	
	
	private DBConn db;
	

	public Logon_Frame() {
		setTitle("�α��� ������");
		setSize(280, 150);
		setLocationRelativeTo(null); //ȭ�� ���߾� �ڵ�
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		idLabel = new JLabel("���̵� "); //���̵� ��
		idLabel.setBounds(10, 10, 80, 25);
		this.add(idLabel);
			
		pwLabel = new JLabel("��й�ȣ"); //��й�ȣ ��
		pwLabel.setBounds(10, 40, 80, 25);
	    this.add(pwLabel);
	    
	    userid = new JTextField(10); // id�� �޾ƿ��� ���� �ؽ�Ʈ�ʵ�
		userid.setBounds(100, 10, 160, 25);
		this.add(userid);

		userpw = new JPasswordField(10); // ��й�ȣ�� �޾ƿ��� ���� ��й�ȣ �ʵ�
		userpw.setBounds(100, 40, 160, 25);
		this.add(userpw);
	    
		loginBtn = new JButton("�α���"); // �α��� ��ư
		loginBtn.setBounds(10, 80, 100, 25);
		this.add(loginBtn);
		
		setVisible(true);
		
		loginBtn.addActionListener(new ActionListener() {   //�α��� ��ư�� ������ ����
	          @Override
	          public void actionPerformed(ActionEvent e) {

	             int num = loginCheck();   //DB���� �α��� �����ϸ� 1�� ��ȯ
	             if(num == 1) {
	                JOptionPane.showMessageDialog(null, "�α��� ����!");
	           
	                db = DBConn.getInstance();
	                int money = db.getMoney(id);
	                dispose();
	                new SlotMachine_Frame(id, money);
	             }
	             else if(num == 0) {
	                JOptionPane.showMessageDialog(null, "��й�ȣ�� ���� �ʽ��ϴ�.");
	             }
	             else if(num == -1) {
	                JOptionPane.showMessageDialog(null, "���̵� �������� �ʽ��ϴ�.");
	             }
	             else if(num == 2) {
	                JOptionPane.showMessageDialog(null, "��ĭ�� ä���ּ���");
	             }
	          }
	       });
		
		signupBtn = new JButton("ȸ������"); // ȸ������ ��ư
		signupBtn.setBounds(155, 80, 100, 25);
		this.add(signupBtn);
		signupBtn.addActionListener(new ActionListener() { // ȸ������ ��ư�� ������ ����
			@Override
			public void actionPerformed(ActionEvent e) {
				memberInsert(); // ȸ������
				//JOptionPane.showMessageDialog(null, "�������");
			}
		});
	}
		
		 public int loginCheck() {
		      id = userid.getText();   //�Է��� id�� �ҷ�����
		      pw = String.valueOf(userpw.getPassword());   //�Է��� passwd�� �ҷ�����

		      if(id.equals("") || pw.equals("")) {
		         return 2;
		      }
		      db = DBConn.getInstance();
		      int num = db.select(id, pw);   //db���� �α��� �� ��������
		      return num;
		   }
	   
	   public void memberInsert() {
		      id = userid.getText();   //�Է��� id�� �ҷ�����
		      pw = String.valueOf(userpw.getPassword());   //�Է��� passwd�� �ҷ�����

		      if((id.equals(""))||(pw.equals(""))) {
		         JOptionPane.showMessageDialog(null, "ĭ�� �� ä���ּ���");
		         return ;
		      }

		      db = DBConn.getInstance();
		      db.insertMember(id, pw);   //db�� id,pw �����ϱ�
		
		}
	}


