package �ڹٽ��Ըӽ�;

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

	private int haveMoney; // ������ �ִ� ��
	private int batting = 0; // ������ ��

	private DBConn db = DBConn.getInstance();;

	// 10������ ���� ���ϴ� �������� ������ �ִ� Ŭ����
	private DecimalFormat df = new DecimalFormat("#,##0"); // 1000���� �޸�(,)

	private Timer[] timer = new Timer[3]; // Ÿ�̸�
	private int counter = 0; 
	private Timer babe; // Ÿ�̸�2

	private Random random = new Random(); //Ȯ�� ���� 

	private JLabel TitleL, havingL, battingL, idL;
	private JLabel[] ImageL = new JLabel[3]; // �̹����� ���� ��

	private JPanel contentPane; //����̹��� ���� �г�

	private JButton RankBtn, LeverBtn, ExitBtn, LOutBtn;
	private JButton[] BattingBtn = new JButton[3];

	private ImageIcon Ticon; // ���� ���� �̹���
	private ImageIcon Bicon; //��濡 ���� �̹���
	private ImageIcon[] iconArr = new ImageIcon[8]; // �̹����󺧿� ���� �̹���

	private Font font = new Font("monospaced", Font.BOLD, 30); // ������Ʈ

	BevelBorder border = new BevelBorder(BevelBorder.RAISED); // ��
	EtchedBorder eborder = new EtchedBorder(EtchedBorder.LOWERED);// ��

	private String id;

	public SlotMachine_Frame(String id, int money) {
		setTitle("���� ȭ��");
		setSize(800, 600);
		setLocationRelativeTo(null); // ȭ�� ���߾� �ڵ�
		
		Bicon = new ImageIcon("imgs/bim.jpg"); // ��� �̹���
		//��� �̹���
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

		//�α��� �� �� �Է��� id�� �޾Ƽ� id�� �����Ѵ�.
		this.id = id;
		//�α����� id�� �ݾ��� haveMoney�� �����Ѵ�.
		this.haveMoney = money;

		Ticon = new ImageIcon("imgs/Title.png"); // ���� ����
		Image TiconImg = Ticon.getImage();
		Image TticonImg = TiconImg.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		ImageIcon icon = new ImageIcon(TticonImg);
		// ImageIcon ��ü ��ü�� ũ�⸦ ������ �� ���
		// ImageIcon ��ü�� Image����, ����� Image�� ũ�⿡ �°� ��ȯ�Ͽ� ���ο� Image��ü ����
		// ���ο� Image��ü�� ImageIcon��ü�� ����

		for (int i = 0; i < iconArr.length; i++) { // ����� �̹������� �迭�� �ִ´�
			iconArr[i] = new ImageIcon("imgs/slot" + (i + 1) + ".jpg");
		}

		//���� ��
		TitleL = new JLabel("���α� ���� �ӽ� ", icon, JLabel.CENTER);
		TitleL.setBounds(230, 10, 350, 50);
		TitleL.setFont(font);
		TitleL.setBorder(new EtchedBorder(Color.white, Color.blue)); // ����� �׵θ� ����
		add(TitleL);

		//����ǥ�� �� �� �ִ� ����ǥ ��ư
		RankBtn = new JButton("����ǥ");
		RankBtn.setBounds(60, 20, 75, 25);
		RankBtn.setFocusPainted(false);
		add(RankBtn);
		RankBtn.addActionListener(new ActionListener() { // ����ǥ ��ư�� ������ ����

			@Override
			public void actionPerformed(ActionEvent e) {
				new Rank_Frame();
			}
		});
		
		//������ ������ �� �ִ� ������ ��ư
		ExitBtn = new JButton("������");
		ExitBtn.setBounds(650, 20, 75, 25);
		ExitBtn.setFocusPainted(false);
		add(ExitBtn);
		ExitBtn.addActionListener(new ActionListener() { // ������ ��ư�� ������ ����

			@Override
			public void actionPerformed(ActionEvent e) {

				int result = JOptionPane.showConfirmDialog(null, "�����?", "����", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);

				if (result == 0) {
					dispose();
				}
			}
		});

		// ���� �ݾ��� �����ִ� ��
		havingL = new JLabel("����ݾ� : " + df.format(haveMoney) + "��");
		havingL.setBounds(320, 360, 200, 25);
		havingL.setForeground(Color.white);
		add(havingL);

		// ������ �� �ݾ��� �����ִ� �ݾ�
		battingL = new JLabel("���ñݾ� : " + df.format(batting) + "��");
		battingL.setBounds(320, 400, 200, 25);
		battingL.setForeground(Color.white);
		add(battingL);

		// ���� �α��ε� ���̵� �����ִ� ��
		idL = new JLabel();
		idL.setBounds(280, 520, 140, 25);
		idL.setText(" ���̵� : " + id);
		idL.setForeground(Color.white);
		idL.setBorder(new EtchedBorder(Color.black, Color.white));
		add(idL);

		// �α���ȭ������ ���ư��� ���ִ� �α׾ƿ� ��ư
		LOutBtn = new JButton("�α׾ƿ�");
		LOutBtn.setBounds(450, 520, 90, 25);
		LOutBtn.setFocusPainted(false);
		add(LOutBtn);
		LOutBtn.addActionListener(new ActionListener() { //�α׾ƿ� ��ư�� ������ ����

			@Override
			public void actionPerformed(ActionEvent e) {

				int result = JOptionPane.showConfirmDialog(null, "�α׾ƿ��Ͻðڽ��ϱ�?", "����", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);

				if (result == 0) {
					dispose();
					new Logon_Frame();
				}
			}
		});

		// ������ �������ִ� �����̹����� ���� ��ư
		LeverBtn = new JButton(new ImageIcon("imgs/LeverOff.png"));
		LeverBtn.setBounds(630, 340, 95, 160);
		LeverBtn.setBorderPainted(false); // ��ư �ܰ��� ����
		LeverBtn.setContentAreaFilled(false); // ��ư ���뿪�� ä��� ����
		LeverBtn.addActionListener(this);
		add(LeverBtn);

		// �̹�����, ��ž��ư, ���ù�ư ����ϴ� for��
		for (int i = 0, Ix = 40, BBx = 280; i < 3; i++, Ix += 235, BBx += 80) { // x��ǥ�� �����ְ� for������ 
			// �÷����鼭 ���
			// ���� ȭ�鿡 �̹����� ����ϴ� ��
			ImageL[i] = new JLabel(iconArr[0]);
			ImageL[i].setBounds(Ix, 70, 230, 210);
			add(ImageL[i]);

			// ���� ��ư ���
			if (i == 0) {
				BattingBtn[i] = new JButton("1��");
				BattingBtn[i].setBounds(BBx, 440, 70, 60);
				BattingBtn[i].setFocusPainted(false);
				BattingBtn[i].addActionListener(this);
				add(BattingBtn[i]);
			} else if (i == 1) {
				BattingBtn[i] = new JButton("10��");
				BattingBtn[i].setBounds(BBx, 440, 70, 60);
				BattingBtn[i].setFocusPainted(false);
				BattingBtn[i].addActionListener(this);
				add(BattingBtn[i]);
			} else {
				BattingBtn[i] = new JButton("100��");
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
		// ���۹�ư�� ���� ���(������ �ؾ� �����)
		if (source == LeverBtn) {
			if (batting > 0) {
				LeverBtn.setIcon(new ImageIcon("imgs/LeverOn.png")); //���� ��ư�� ������� LeverOn�̹����� �ٲ�
				LeverBtn.setEnabled(false); //������ư ������ Ŭ���� �� ���� ��
				for (int i = 0; i < 3; i++) {
					timer[i] = new Timer();
					timer[i].schedule(new SlotTask(ImageL[i]), 0, 15); // (������ �۾�, �����ð�, ���� ����)
					BattingBtn[i].setEnabled(false);
				}
				babe = new Timer();
			} else { // ������ ���ϰ� ������ư�� �������
				JOptionPane.showMessageDialog(null, "������ �ϼ���");
			}
		} else {
			// ���� ��ư ���� ���
			for (int i = 0; i < 3; i++) {
				if (BattingBtn[i] == source) {
					// ����ݾ׿��� ���ñݾ� ����, ���ñݾ׿� ������ �ݾ׸�ŭ ���ϸ� ����
					if (0 <= haveMoney - BATTING_MONEY[i] && batting + BATTING_MONEY[i] <= MAX_MONEY) {
						// �ݾ� ����
						batting += BATTING_MONEY[i];
						haveMoney -= BATTING_MONEY[i];

						battingL.setText("���ó���:" + df.format(batting) + "��");
						havingL.setText("�����ݾ�:" + df.format(haveMoney) + "��");
					}
					return;
				}
			}

		}
	}

	// �׸� ��, ���Ḹŭ �ݾ� �޴� �޼ҵ�
	private void checkMatched() {
		// �׸� ���ؼ� �� �� ������� �˾Ƴ�
		int count = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = i + 1; j < 3; j++) {
				if (ImageL[i].getIcon() == ImageL[j].getIcon()) {
					count++; //���� �׸���ŭ ī��Ʈ�� �ö󰣴�.
				}
			}
		}
		// �� �� ���� count�� �־ switch������ �˾Ƴ���
		switch (count) {
		case 0:
			JOptionPane.showMessageDialog(null, "���Դϴ�!");
			break;
		case 1:
			// 2�� ������ �� �ݾ�
			JOptionPane.showMessageDialog(null, "�����մϴ�!!  2�踦 ȹ���ϼ̽��ϴ�!");
			haveMoney = haveMoney + batting * 2;
			break;
		case 3:
			// 3�� ������ �� �ݾ�
			if (ImageL[0].getIcon() == iconArr[6]) {
				// �Ŀ��� 3���� �߸� ���̵� ����
				db.deleteId(id);
				JOptionPane.showMessageDialog(null, "�Ļ�Ǿ����ϴ�.");
				dispose();
				new Logon_Frame();

			} else if (ImageL[0].getIcon() == iconArr[7]) {
				// �Ͽ��� 3���� �� ��
				JOptionPane.showMessageDialog(null, "�����մϴ�!!  ���ñݾ��� 20�踦 ȹ���ϼ̽��ϴ�!");
				haveMoney = haveMoney + batting * 20;
			} else
				// ���α� ���� 3���� �� ��
				JOptionPane.showMessageDialog(null, "�����մϴ�!!  4�踦 ȹ���ϼ̽��ϴ�!");
			haveMoney = haveMoney + batting * 4;
			break;
		}

		batting = 0;

		battingL.setText("���ó���:" + batting + "��");
		havingL.setText("�����ݾ�:" + haveMoney + "��");

		for (int j = 0; j < 3; j++) {
			BattingBtn[j].setEnabled(true);
		}
	}

	// ���Ե��ư��� ����
	class SlotTask extends TimerTask {
		private int status = 0;
		private JLabel lb;

		public SlotTask(JLabel lb) {
			this.lb = lb;
		}

		public void run() {
			// Ȯ��
			status = random.nextInt(14) + 1;
			if (status != 14 && status != 13) {
				lb.setIcon(iconArr[status % 6]);
			} else if (status == 14) { // �Ŀ��� Ȯ��
				lb.setIcon(iconArr[6]);
			} else if (status == 13) { // �Ͽ��� Ȯ��
				lb.setIcon(iconArr[7]);
			}
			// �����尡 �����ϸ� counter�� �ø���.
			counter++;
			// 230�� �� 1��° �̹��� ��ž
			if (counter >= 230) {
				timer[0].cancel();

			}
			// 300�� �� 2��° �̹��� ��ž
			if (counter >= 300) {
				timer[1].cancel();
				// 3��° �̹��� �����带 ���߰�
				timer[2].cancel();
				// ���ο� ������� 3��° �̹����� �ٽ� �����Ѵ�.
				babe.schedule(new SlowTask(ImageL[2]), 0, 300);
			}
		}

		// 3��° �̹����� ���� ������
		class SlowTask extends TimerTask {
			private int status = 0;
			private JLabel lb;
			private int Slowcounter = 0;

			public SlowTask(JLabel lb) {
				this.lb = lb;
			}

			public void run() {
				// Ȯ��
				status = random.nextInt(14) + 1;
				if (status != 13 && status != 14) {
					lb.setIcon(iconArr[status % 6]);
				} else if (status == 14) { // �Ŀ��� Ȯ��
					lb.setIcon(iconArr[6]);
				} else if (status == 13) { // �Ͽ��� Ȯ��
					lb.setIcon(iconArr[7]);
				}
				Slowcounter++;
				// 3��° �̹��� ��ž
				if (Slowcounter >= 7) {
					babe.cancel();

					// ��÷�ݾ� üũ
					checkMatched();
					// DB�� ���� �ݾ� ������Ʈ
					db.updateMoney(id, haveMoney);
					LeverBtn.setEnabled(true); //������ư�� �ٽ� Ŭ���� �� �ְ���
					LeverBtn.setIcon(new ImageIcon("imgs/LeverOff.png"));// ������� ������ư �̹����� �ٽ� LeverOff�� �ٲ�
					counter = 0;

				}
			}

			public int getStatus() {
				return status;
			}
		}
	}
}