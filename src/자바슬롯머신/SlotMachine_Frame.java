package 자바슬롯머신;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

public class SlotMachine_Frame extends JFrame implements ActionListener {
	private final static long MAX_MONEY = 1000000000000000000L;
	private final static int[] BATTING_MONEY = { 10000, 100000, 1000000 };

	private int haveMoney; // 가지고 있는 돈
	private int batting = 0; // 배팅한 돈

	private DBConn db = DBConn.getInstance();;

	// 10진수의 값을 원하는 포멧으로 변형해 주는 클래스
	private DecimalFormat df = new DecimalFormat("#,##0"); // 1000단위 콤마(,)

	private Timer[] timer = new Timer[3]; // 타이머
	private int counter = 0; 
	private Timer babe; // 타이머2

	private Random random = new Random(); //확률 설정 

	private JLabel TitleL, havingL, battingL, idL;
	private JLabel[] ImageL = new JLabel[3]; // 이미지가 나올 라벨

	private JPanel contentPane; //배경이미지 넣을 패널

	private JButton RankBtn, LeverBtn, ExitBtn, LOutBtn;
	private JButton[] BattingBtn = new JButton[3];

	private ImageIcon Ticon; // 제목에 넣을 이미지
	private ImageIcon Bicon; //배경에 넣을 이미지
	private ImageIcon[] iconArr = new ImageIcon[8]; // 이미지라벨에 넣을 이미지

	private Font font = new Font("monospaced", Font.BOLD, 30); // 제목폰트

	BevelBorder border = new BevelBorder(BevelBorder.RAISED); // 라벨
	EtchedBorder eborder = new EtchedBorder(EtchedBorder.LOWERED);// 라벨

	private String id;

	public SlotMachine_Frame(String id, int money) {
		setTitle("게임 화면");
		setSize(800, 600);
		setLocationRelativeTo(null); // 화면 정중앙 코드
		
		Bicon = new ImageIcon("imgs/bim.jpg"); // 배경 이미지
		//배경 이미지
		contentPane = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(Bicon.getImage(), 0, 0, 800, 600, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		setContentPane(contentPane);
		
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//로그인 할 때 입력한 id를 받아서 id에 저장한다.
		this.id = id;
		//로그인한 id에 금액을 haveMoney에 저장한다.
		this.haveMoney = money;

		Ticon = new ImageIcon("imgs/Title.png"); // 제목 사진
		Image TiconImg = Ticon.getImage();
		Image TticonImg = TiconImg.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		ImageIcon icon = new ImageIcon(TticonImg);
		// ImageIcon 객체 자체의 크기를 조절할 수 없어서
		// ImageIcon 객체의 Image추출, 추출된 Image를 크기에 맞게 변환하여 새로운 Image객체 생성
		// 새로운 Image객체로 ImageIcon객체를 생성

		for (int i = 0; i < iconArr.length; i++) { // 출력할 이미지들을 배열에 넣는다
			iconArr[i] = new ImageIcon("imgs/slot" + (i + 1) + ".jpg");
		}

		//제목 라벨
		TitleL = new JLabel("꼬부기 슬롯 머신 ", icon, JLabel.CENTER);
		TitleL.setBounds(230, 10, 350, 50);
		TitleL.setFont(font);
		TitleL.setBorder(new EtchedBorder(Color.white, Color.blue)); // 제목라벨 테두리 색깔
		add(TitleL);

		//순위표를 볼 수 있는 순위표 버튼
		RankBtn = new JButton("순위표");
		RankBtn.setBounds(60, 20, 75, 25);
		RankBtn.setFocusPainted(false);
		add(RankBtn);
		RankBtn.addActionListener(new ActionListener() { // 순위표 버튼에 리스너 설정

			@Override
			public void actionPerformed(ActionEvent e) {
				new Rank_Frame();
			}
		});
		
		//게임을 종료할 수 있는 나가기 버튼
		ExitBtn = new JButton("나가기");
		ExitBtn.setBounds(650, 20, 75, 25);
		ExitBtn.setFocusPainted(false);
		add(ExitBtn);
		ExitBtn.addActionListener(new ActionListener() { // 나가기 버튼에 리스너 설정

			@Override
			public void actionPerformed(ActionEvent e) {

				int result = JOptionPane.showConfirmDialog(null, "리얼로?", "종료", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);

				if (result == 0) {
					dispose();
				}
			}
		});

		// 현재 금액을 보여주는 라벨
		havingL = new JLabel("현재금액 : " + df.format(haveMoney) + "원");
		havingL.setBounds(320, 360, 200, 25);
		havingL.setForeground(Color.white);
		add(havingL);

		// 배팅을 한 금액을 보여주는 금액
		battingL = new JLabel("배팅금액 : " + df.format(batting) + "원");
		battingL.setBounds(320, 400, 200, 25);
		battingL.setForeground(Color.white);
		add(battingL);

		// 지금 로그인된 아이디를 보여주는 라벨
		idL = new JLabel();
		idL.setBounds(280, 520, 140, 25);
		idL.setText(" 아이디 : " + id);
		idL.setForeground(Color.white);
		idL.setBorder(new EtchedBorder(Color.black, Color.white));
		add(idL);

		// 로그인화면으로 돌아가게 해주는 로그아웃 버튼
		LOutBtn = new JButton("로그아웃");
		LOutBtn.setBounds(450, 520, 90, 25);
		LOutBtn.setFocusPainted(false);
		add(LOutBtn);
		LOutBtn.addActionListener(new ActionListener() { //로그아웃 버튼에 리스너 설정

			@Override
			public void actionPerformed(ActionEvent e) {

				int result = JOptionPane.showConfirmDialog(null, "로그아웃하시겠습니까?", "종료", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);

				if (result == 0) {
					dispose();
					new Logon_Frame();
				}
			}
		});

		// 게임을 시작해주는 레버이미지를 넣은 버튼
		LeverBtn = new JButton(new ImageIcon("imgs/LeverOff.png"));
		LeverBtn.setBounds(630, 340, 95, 160);
		LeverBtn.setBorderPainted(false); // 버튼 외곽선 삭제
		LeverBtn.setContentAreaFilled(false); // 버튼 내용역역 채우기 안함
		LeverBtn.addActionListener(this);
		add(LeverBtn);

		// 이미지라벨, 스탑버튼, 배팅버튼 출력하는 for문
		for (int i = 0, Ix = 40, BBx = 280; i < 3; i++, Ix += 235, BBx += 80) { // x좌표를 정해주고 for문으로 
			// 늘려가면서 출력
			// 메인 화면에 이미지를 출력하는 라벨
			ImageL[i] = new JLabel(iconArr[0]);
			ImageL[i].setBounds(Ix, 70, 230, 210);
			add(ImageL[i]);

			// 배팅 버튼 출력
			if (i == 0) {
				BattingBtn[i] = new JButton("1만");
				BattingBtn[i].setBounds(BBx, 440, 70, 60);
				BattingBtn[i].setFocusPainted(false);
				BattingBtn[i].addActionListener(this);
				add(BattingBtn[i]);
			} else if (i == 1) {
				BattingBtn[i] = new JButton("10만");
				BattingBtn[i].setBounds(BBx, 440, 70, 60);
				BattingBtn[i].setFocusPainted(false);
				BattingBtn[i].addActionListener(this);
				add(BattingBtn[i]);
			} else {
				BattingBtn[i] = new JButton("100만");
				BattingBtn[i].setBounds(BBx, 440, 70, 60);
				BattingBtn[i].setFocusPainted(false);
				BattingBtn[i].addActionListener(this);
				add(BattingBtn[i]);
			}
		}

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		// 시작버튼을 누룰 경우(배팅을 해야 실행됨)
		if (source == LeverBtn) {
			if (batting > 0) {
				LeverBtn.setIcon(new ImageIcon("imgs/LeverOn.png")); //레버 버튼을 누를경우 LeverOn이미지로 바꿈
				LeverBtn.setEnabled(false); //레버버튼 누르면 클릭할 수 없게 함
				for (int i = 0; i < 3; i++) {
					timer[i] = new Timer();
					timer[i].schedule(new SlotTask(ImageL[i]), 0, 15); // (지정한 작업, 일정시간, 일정 간격)
					BattingBtn[i].setEnabled(false);
				}
				babe = new Timer();
			} else { // 배팅을 안하고 레버버튼을 누를경우
				JOptionPane.showMessageDialog(null, "배팅을 하세요");
			}
		} else {
			// 배팅 버튼 누를 경우
			for (int i = 0; i < 3; i++) {
				if (BattingBtn[i] == source) {
					// 현재금액에서 배팅금액 빼고, 배팅금액에 배팅한 금액만큼 더하면 실행
					if (0 <= haveMoney - BATTING_MONEY[i] && batting + BATTING_MONEY[i] <= MAX_MONEY) {
						// 금액 조절
						batting += BATTING_MONEY[i];
						haveMoney -= BATTING_MONEY[i];

						battingL.setText("배팅내역:" + df.format(batting) + "원");
						havingL.setText("보유금액:" + df.format(haveMoney) + "원");
					}
					return;
				}
			}

		}
	}

	// 그림 비교, 맞춘만큼 금액 받는 메소드
	private void checkMatched() {
		// 그림 비교해서 몇 개 맞췄는지 알아냄
		int count = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = i + 1; j < 3; j++) {
				if (ImageL[i].getIcon() == ImageL[j].getIcon()) {
					count++; //맞춘 그림만큼 카운트가 올라간다.
				}
			}
		}
		// 비교 한 값을 count에 넣어서 switch문으로 알아낸다
		switch (count) {
		case 0:
			JOptionPane.showMessageDialog(null, "꽝입니다!");
			break;
		case 1:
			// 2개 맞췄을 때 금액
			JOptionPane.showMessageDialog(null, "축하합니다!!  2배를 획득하셨습니다!");
			haveMoney = haveMoney + batting * 2;
			break;
		case 3:
			// 3개 맞췄을 때 금액
			if (ImageL[0].getIcon() == iconArr[6]) {
				// 냐옹이 3장이 뜨면 아이디 삭제
				db.deleteId(id);
				JOptionPane.showMessageDialog(null, "파산되었습니다.");
				dispose();
				new Logon_Frame();

			} else if (ImageL[0].getIcon() == iconArr[7]) {
				// 하연수 3장이 뜰 때
				JOptionPane.showMessageDialog(null, "축하합니다!!  배팅금액의 20배를 획득하셨습니다!");
				haveMoney = haveMoney + batting * 20;
			} else
				// 꼬부기 사진 3장이 뜰 때
				JOptionPane.showMessageDialog(null, "축하합니다!!  4배를 획득하셨습니다!");
			haveMoney = haveMoney + batting * 4;
			break;
		}

		batting = 0;

		battingL.setText("배팅내역:" + batting + "원");
		havingL.setText("보유금액:" + haveMoney + "원");

		for (int j = 0; j < 3; j++) {
			BattingBtn[j].setEnabled(true);
		}
	}

	// 슬롯돌아가는 로직
	class SlotTask extends TimerTask {
		private int status = 0;
		private JLabel lb;

		public SlotTask(JLabel lb) {
			this.lb = lb;
		}

		public void run() {
			// 확률
			status = random.nextInt(14) + 1;
			if (status != 14 && status != 13) {
				lb.setIcon(iconArr[status % 6]);
			} else if (status == 14) { // 냐옹이 확률
				lb.setIcon(iconArr[6]);
			} else if (status == 13) { // 하연수 확률
				lb.setIcon(iconArr[7]);
			}
			// 쓰레드가 시작하면 counter를 올린다.
			counter++;
			// 230일 때 1번째 이미지 스탑
			if (counter >= 230) {
				timer[0].cancel();

			}
			// 300일 때 2번째 이미지 스탑
			if (counter >= 300) {
				timer[1].cancel();
				// 3번째 이미지 쓰레드를 멈추고
				timer[2].cancel();
				// 새로운 쓰레드로 3번째 이미지를 다시 시작한다.
				babe.schedule(new SlowTask(ImageL[2]), 0, 300);
			}
		}

		// 3번째 이미지를 위한 쓰레드
		class SlowTask extends TimerTask {
			private int status = 0;
			private JLabel lb;
			private int Slowcounter = 0;

			public SlowTask(JLabel lb) {
				this.lb = lb;
			}

			public void run() {
				// 확률
				status = random.nextInt(14) + 1;
				if (status != 13 && status != 14) {
					lb.setIcon(iconArr[status % 6]);
				} else if (status == 14) { // 냐옹이 확률
					lb.setIcon(iconArr[6]);
				} else if (status == 13) { // 하연수 확률
					lb.setIcon(iconArr[7]);
				}
				Slowcounter++;
				// 3번째 이미지 스탑
				if (Slowcounter >= 7) {
					babe.cancel();

					// 당첨금액 체크
					checkMatched();
					// DB에 현재 금액 업데이트
					db.updateMoney(id, haveMoney);
					LeverBtn.setEnabled(true); //레버버튼을 다시 클릭할 수 있게함
					LeverBtn.setIcon(new ImageIcon("imgs/LeverOff.png"));// 끝나고면 레버버튼 이미지를 다시 LeverOff로 바꿈
					counter = 0;

				}
			}

			public int getStatus() {
				return status;
			}
		}
	}
}