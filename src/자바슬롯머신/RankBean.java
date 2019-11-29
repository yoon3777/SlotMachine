package 자바슬롯머신;

//DB에서 가져올 아이디와 금액을 setter, getter를 사용해 저장합니다.
public class RankBean {
	private String user_id;
	private int money;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	
}