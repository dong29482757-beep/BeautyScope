package kr.ac.kopo.loader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/** 최초 관리자 계정 1개를 생성한다 (admin / admin1234). 실행: mvn exec:java -Dexec.mainClass=... */
public class AdminSeeder {

	private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
	private static final String JDBC_USER = "hr";
	private static final String JDBC_PASSWORD = "hr";

	public static void main(String[] args) throws Exception {
		String hashed = new BCryptPasswordEncoder().encode("admin1234");

		try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
			try (PreparedStatement check = conn.prepareStatement("SELECT COUNT(*) FROM t_member WHERE member_id = 'admin'")) {
				var rs = check.executeQuery();
				rs.next();
				if (rs.getInt(1) > 0) {
					System.out.println("admin 계정이 이미 있습니다.");
					return;
				}
			}
			try (PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO t_member (member_id, password, nickname, email, role, join_type) "
							+ "VALUES ('admin', ?, '관리자', 'admin@beautyscope.local', 'ADMIN', 'LOCAL')")) {
				ps.setString(1, hashed);
				ps.executeUpdate();
				System.out.println("admin 계정 생성 완료 (id=admin, pw=admin1234)");
			}
		}
	}
}
