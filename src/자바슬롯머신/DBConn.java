package �ڹٽ��Ըӽ�;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class DBConn {
	// �̱��� ���� ����
	private static DBConn instance = new DBConn();

	public static DBConn getInstance() {
		return instance;
	}

	DBConn() {
	};

	// DB ����
	public Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("����̹� ���� ����");
		} catch (ClassNotFoundException e1) {
			System.out.println("����̹� ���� ����");
		} // ����̹� ����

		String url = "jdbc:oracle:thin:@net.yjc.ac.kr:1521:orcl";
		String id = "s1501192";
		String pw = "p1501192";

		try {
			conn = DriverManager.getConnection(url, id, pw);
			System.out.println("�����ͺ��̽� ���� ����.");
		} catch (SQLException e) {
			System.out.println("������ ���̽� ���ῡ �����Ͽ����ϴ�..");
		}
		return conn;
	}

	// ������ üũ��
	public int select(String id, String pw) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int num = 0;

		try {
			conn = getConnection();
			String sql = "select * from users where user_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				String dbpw = rs.getString("user_pw");
				if (pw.equals(dbpw)) {

					num = 1; // ���̵�,��� ����
				} else {
					num = 0; // ���Ʋ��
				}
			} else {
				num = -1; // ���̵� ����x
			}
		} catch (SQLException e) {
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		return num;
	}

	// DB���� �α����� ID�� ����ִ� �ݾ��� ��� ���� �޼ҵ�
	public int getMoney(String id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int money = 0;
		try {
			conn = getConnection();
			String sql = "select money from users where user_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				money = rs.getInt("money");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return money;
	}

	// �Էµ� ����� DB���� ������ �����ӿ� ����մϴ�.(���������� ������ �ؼ�)
	public ArrayList selectRank() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs;
		RankBean rbean;
		ArrayList<RankBean> arr = new ArrayList<RankBean>();
		try {
			conn = getConnection();
			//users���̺� money�� �����ؼ� ������ ������ ���̺��� �� ��ȣ�� 5������ �ͱ��� �����ش�.
			String sql = "select * from (select * from users order by money DESC) where rownum <= 5";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				rbean = new RankBean();
				rbean.setUser_id(rs.getString("user_id"));
				rbean.setMoney(rs.getInt("money"));
				arr.add(rbean);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "����");
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		return arr;
	}

	// ������ ������ �ݾ��� �Է��ϴ� �κ�
	public void updateMoney(String id, int money) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			//System.out.println(id);
			//System.out.println(money);
			conn = getConnection();
			String sql = "update users set money=? where user_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, money);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
			//JOptionPane.showMessageDialog(null, "����� �Ϸ��Ͽ����ϴ�.");
			System.out.println("��� �Ϸ�");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "��Ͽ� �����Ͽ����ϴ�.");
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	// ȸ������
	public void insertMember(String id, String passwd) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			int check = memberCheck(id, passwd);

			if (check == 1) {
				conn = getConnection();
				String sql = "insert into users values(?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				pstmt.setString(2, passwd);
				pstmt.setInt(3, 500000);
				pstmt.executeUpdate();
				JOptionPane.showMessageDialog(null, "ȸ�������� �Ϸ��Ͽ����ϴ�.");
			} else {
				JOptionPane.showMessageDialog(null, "���̵� �����մϴ�");
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "ȸ�����Կ� ������ �ֽ��ϴ�.");
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	// ���̵� �ߺ� ��ȸ
	public int memberCheck(String id, String passwd) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			String sql = "select * from users where user_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if (rs.next() == true) {
				return 0; // ���̵� ����

			} else {
				return 1;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "ȸ�����Կ� ������ �ֽ��ϴ�.2");
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		return 1; // ���̵� ����
	}
	//�Ŀ��� �ɷ��� �� �Ļ��ϰ� �ϴ� �޼ҵ�
	public void deleteId(String id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			String sql = "delete from users where user_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "������ �ȵ���");
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}
}
