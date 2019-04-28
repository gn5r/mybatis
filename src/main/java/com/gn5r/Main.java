package com.gn5r;

import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class Main {

	private static final String url = "jdbc:mysql://localhost:3306/sample?serverTimezone=JST";
	private static final String user = "gn5r";
	private static final String password = "";
	private static final String sql = "select * from tutorial;";
	private static final String insert = "insert into tutorial (name,email,password)values(?,?,?)";

	public static void main(String[] args) {
		Connection connection = null;
//		connection = connectSQL();
//		getTable(connection);
//		
//		System.out.println("データの追加");
//		insertTable(connection);
//		getTable(connection);

		getMyBatis();
	}

	private static Connection connectSQL() {
		Connection con = null;

		try {
			con = DriverManager.getConnection(url, user, password);
			return con;
		} catch (SQLException e) {
			e.printStackTrace();
			return con;
		}
	}

	private static void getTable(Connection con) {
		try {
			PreparedStatement ps = con.prepareStatement(sql);

			ResultSet result = ps.executeQuery();

			while (result.next()) {
				System.out.println("ID:" + String.valueOf(result.getInt("id")) + "　Name:" + result.getString("name")
						+ "　Email:" + result.getString("email") + "　Password:" + result.getString("password") + "\n作成日:"
						+ result.getDate("created") + result.getTime("created") + "　更新日:" + result.getDate("modified")
						+ result.getTime("modified"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	private static void insertTable(Connection con) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("名前を入力:");
		String name = scanner.next();

		System.out.println("メールアドレスを入力:");
		String email = scanner.next();

		System.out.println("パスワードを入力:");
		String password = scanner.next();

		try {
			PreparedStatement ps = con.prepareStatement(insert);
			ps.setString(1, name);
			ps.setString(2, email);
			ps.setString(3, password);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void getMyBatis() {
		try {
			Reader reader = Resources.getResourceAsReader("mybatis-Config.xml");
			SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader);

			SqlSession session = factory.openSession();
			List<Tutorial> list = session.selectList("com.gn5r.TutorialMapper.select");

			for (Tutorial t : list) {
				System.out.println("--------------------");
				System.out.printf("名前:%s Email:%s\n作成日:%s\n更新日:%s\n",
						t.getName(), t.getEmail(), t.getCreated(), t.getModified());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
