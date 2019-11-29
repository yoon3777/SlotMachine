package 자바슬롯머신;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class DBConn {
	// 싱글톤 생성 시작
	private static DBConn instance = new DBConn();

	public static DBConn getInstance() {
		return instance;
	}

	DBConn() {
	};

	// DB 연결
	public Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("드라이버 적재 성공");
		} catch (ClassNotFoundException e1) {
			System.out.println("드라이버 적재 실패");
		} // 드라이버 적재

		String url = "jdbc:oracle:thin:@net.yjc.ac.kr:1521:orcl";
		String id = "s1501192";
		String pw = "p1501192";

		try {
			conn = DriverManager.getConnection(url, id, pw);
			System.out.println("데이터베이스 연결 성공.");
		} catch (SQLException e) {
			System.out.println("데이터 베이스 연결에 실패하였습니다..");
		}
		return conn;
	}

	// 유저를 체크함
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

					num = 1; // 아이디,비번 성공
				} else {
					num = 0; // 비번틀림
				}
			} else {
				num = -1; // 아이디 존재x
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

	// DB에서 로그인한 ID가 들고있는 금액을 들고 오는 메소드
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

	// 입력된 기록을 DB에서 가져와 프레임에 출력합니다.(쿼리문으로 정렬을 해서)
	public ArrayList selectRank() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs;
		RankBean rbean;
		ArrayList<RankBean> arr = new ArrayList<RankBean>();
		try {
			conn = getConnection();
			//users테이블에 money를 정렬해서 정렬한 가상의 테이블의 행 번호가 5까지인 것까지 보여준다.
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
			JOptionPane.showMessageDialog(null, "실패");
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

	// 게임이 끝나고 금액을 입력하는 부분
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
			//JOptionPane.showMessageDialog(null, "기록을 완료하였습니다.");
			System.out.println("기록 완료");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "기록에 실패하였습니다.");
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

	// 회원가입
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
				JOptionPane.showMessageDialog(null, "회원가입을 완료하였습니다.");
			} else {
				JOptionPane.showMessageDialog(null, "아이디가 존재합니다");
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "회원가입에 문제가 있습니다.");
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

	// 아이디 중복 조회
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
				return 0; // 아이디 있음

			} else {
				return 1;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "회원가입에 문제가 있습니다.2");
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
		return 1; // 아이디 없음
	}
	//냐옹이 걸렸을 때 파산하게 하는 메소드
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
			JOptionPane.showMessageDialog(null, "삭제가 안됐음");
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
