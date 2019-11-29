package �ڹٽ��Ըӽ�;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

public class Rank_Frame extends JFrame {
	private JButton ExitBtn; // ������ ��ư
	private ArrayList<RankBean> arr = new ArrayList<RankBean>();
	private JLabel recid; // ������ ���� ��

	private Font Tfont = new Font("monospaced", Font.BOLD, 30); // ����ǥ���� ��Ʈ
	private Font Lfont = new Font("monospaced", Font.ITALIC, 15); // ���� ��Ʈ ��Ʈ

	public Rank_Frame() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); //����ǥ�� xǥ�ø� ������ ��� �������� ������ �ʽ��ϴ�.

		setSize(700, 500);
		setLocationRelativeTo(null); // ȭ�� ���߾� �ڵ�

		// ��ü�� ��� ū �г�
		JPanel panel4 = new JPanel();
		panel4.setLayout(new GridLayout(3, 0));

		// ������ ��ü��� ���� �ִ� �г�
		JPanel panel1 = new JPanel();
		panel1.setLayout(new FlowLayout());
		JLabel recLabel = new JLabel("��ü ���");
		recLabel.setForeground(Color.red);
		recLabel.setFont(Tfont);
		panel1.add(recLabel);

		// ����ǥ�� �����ִ� �г�
		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(5, 0));

		DBConn db = DBConn.getInstance();
		arr = db.selectRank(); // ArrayList�� DB���� ������ ������ �����մϴ�.

		//������ �������� Label�� �־ ����մϴ�.
		for (int i = 0; i < arr.size(); i++) {
			recid = new JLabel(
					i + 1 + "��  -> " + arr.get(i).getUser_id() + "        ��: " + String.valueOf(arr.get(i).getMoney()));
			
			recid.setHorizontalAlignment(JLabel.CENTER); //�� ��� ����
			recid.setFont(Lfont); // �󺧿� ��Ʈ ������
			panel2.add(recid);
		}

		// ������ ��ư�� �ִ� �г�
		JPanel panel3 = new JPanel();
		ExitBtn = new JButton("������");
		panel3.setLayout(null);
		ExitBtn.setBounds(310, 100, 80, 25);
		panel3.add(ExitBtn);

		panel4.add(panel1);
		panel4.add(panel2);
		panel4.add(panel3);
		this.add(panel4);

		setVisible(true);

		ExitBtn.addActionListener(new ActionListener() { // ������ ��ư ������ ����
			@Override
			public void actionPerformed(ActionEvent e) {

				dispose();
			}
		});
	}
}
