package 자바슬롯머신;

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
	
	private JTextField userid; // 아이디입력
	private JPasswordField userpw; // 비밀번호입력

	private JButton loginBtn;
	private JButton signupBtn;
	
	private String id = ""; // field에서 입력받은 값을 저장하는 변수
	private String pw = ""; // field에서 입력받은 값을 저장하는 변수
	
	
	private DBConn db;
	

	public Logon_Frame() {
		setTitle("로그인 프레임");
		setSize(280, 150);
		setLocationRelativeTo(null); //화면 정중앙 코드
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		idLabel = new JLabel("아이디 "); //아이디 라벨
		idLabel.setBounds(10, 10, 80, 25);
		this.add(idLabel);
			
		pwLabel = new JLabel("비밀번호"); //비밀번호 라벨
		pwLabel.setBounds(10, 40, 80, 25);
	    this.add(pwLabel);
	    
	    userid = new JTextField(10); // id를 받아오기 위한 텍스트필드
		userid.setBounds(100, 10, 160, 25);
		this.add(userid);

		userpw = new JPasswordField(10); // 비밀번호를 받아오기 위한 비밀번호 필드
		userpw.setBounds(100, 40, 160, 25);
		this.add(userpw);
	    
		loginBtn = new JButton("로그인"); // 로그인 버튼
		loginBtn.setBounds(10, 80, 100, 25);
		this.add(loginBtn);
		
		setVisible(true);
		
		loginBtn.addActionListener(new ActionListener() {   //로그인 버튼에 리스너 설정
	          @Override
	          public void actionPerformed(ActionEvent e) {

	             int num = loginCheck();   //DB에서 로그인 성공하면 1을 반환
	             if(num == 1) {
	                JOptionPane.showMessageDialog(null, "로그인 성공!");
	           
	                db = DBConn.getInstance();
	                int money = db.getMoney(id);
	                dispose();
	                new SlotMachine_Frame(id, money);
	             }
	             else if(num == 0) {
	                JOptionPane.showMessageDialog(null, "비밀번호가 맞지 않습니다.");
	             }
	             else if(num == -1) {
	                JOptionPane.showMessageDialog(null, "아이디가 존재하지 않습니다.");
	             }
	             else if(num == 2) {
	                JOptionPane.showMessageDialog(null, "빈칸을 채워주세요");
	             }
	          }
	       });
		
		signupBtn = new JButton("회원가입"); // 회원가입 버튼
		signupBtn.setBounds(155, 80, 100, 25);
		this.add(signupBtn);
		signupBtn.addActionListener(new ActionListener() { // 회원가입 버튼에 리스터 설정
			@Override
			public void actionPerformed(ActionEvent e) {
				memberInsert(); // 회원가입
				//JOptionPane.showMessageDialog(null, "어림도없지");
			}
		});
	}
		
		 public int loginCheck() {
		      id = userid.getText();   //입력한 id값 불러오기
		      pw = String.valueOf(userpw.getPassword());   //입력한 passwd값 불러오기

		      if(id.equals("") || pw.equals("")) {
		         return 2;
		      }
		      db = DBConn.getInstance();
		      int num = db.select(id, pw);   //db에서 로그인 값 가져오기
		      return num;
		   }
	   
	   public void memberInsert() {
		      id = userid.getText();   //입력한 id값 불러오기
		      pw = String.valueOf(userpw.getPassword());   //입력한 passwd값 불러오기

		      if((id.equals(""))||(pw.equals(""))) {
		         JOptionPane.showMessageDialog(null, "칸을 다 채워주세요");
		         return ;
		      }

		      db = DBConn.getInstance();
		      db.insertMember(id, pw);   //db에 id,pw 저장하기
		
		}
	}


