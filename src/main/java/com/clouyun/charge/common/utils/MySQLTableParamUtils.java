package com.clouyun.charge.common.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 描述: 控制器类保存更新数据库<br>
 * Data字段包含数据库属性字段映射<br>
 * 生成API前端查看所需参数
 * 版权: Copyright (c) 2017
 * 公司: 科陆电子
 * 作者: yangshuai <yangshuai@szclou.com>
 * 版本: 1.0
 * 创建日期: 2017年04月11日
 */
public class MySQLTableParamUtils {

    /**
     * 获取数据库连接
     */
	public static Connection getMySQLConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager
				.getConnection(
						"jdbc:mysql://10.98.94.218:3306/cloudplatform?useUnicode=true&characterEncoding=utf8&useSSL=false&allowMultiQueries=true",
						"clouduser", "123456");
		return conn;
	}

    /**
     * 获取创建表语句
     * @param tableName
     * @return
     * @throws Exception
     */
	public static String getCommentByTableName(String tableName)
			throws Exception {
		String comment = "";
		Connection conn = getMySQLConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + tableName);
		if (rs != null && rs.next()) {
			String createDDL = rs.getString(2);
			comment = parse(createDDL);
		}
		rs.close();
		stmt.close();
		conn.close();
		return comment;
	}

    /**
     * 读取数据库表结构
     * @param tableName
     * @throws Exception
     */
	public static void getTableNameProperties(String tableName)
			throws Exception {
		Connection conn = getMySQLConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("show full columns from " + tableName);
//		System.out.println("【" + tableName + "】");
		while (rs.next()) {
			// 字段名称 rs.getString("Field")
			// 数据类型 rs.getString("Type")
            // 注释 rs.getString("Comment")
            System.out.println("* @apiParam {"+typeProcessor(rs.getString("Type"))+"} "+underline2Camel(rs.getString("Field"), true)
                    + " " + rs.getString("Comment"));
		}
		rs.close();
		stmt.close();
		conn.close();
	}

	/**
	 * 分页列表出参
	 * @param tableName
	 * @throws Exception
	 */
	public static void getTableNameListOut(String tableName)
			throws Exception {
		Connection conn = getMySQLConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("show full columns from " + tableName);
		System.out.println("* @apiSuccess {Object[]} data.list 分页数据对象数组");
		while (rs.next()) {
			// 字段名称 rs.getString("Field")
			// 数据类型 rs.getString("Type")
			// 注释 rs.getString("Comment")
			System.out.println("* @apiSuccess {"+typeProcessor(rs.getString("Type"))+"} data.list."+underline2Camel(rs.getString("Field"), true)
					+ " " + rs.getString("Comment"));
		}
		rs.close();
		stmt.close();
		conn.close();
	}

	/**
	 * 数据详情出参
	 * @param tableName
	 * @throws Exception
	 */
	public static void getTableNameDetailOut(String tableName)
			throws Exception {
		Connection conn = getMySQLConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("show full columns from " + tableName);
		while (rs.next()) {
			// 字段名称 rs.getString("Field")
			// 数据类型 rs.getString("Type")
			// 注释 rs.getString("Comment")
			System.out.println("* @apiSuccess {"+typeProcessor(rs.getString("Type"))+"} data."+underline2Camel(rs.getString("Field"), true)
					+ " " + rs.getString("Comment"));
		}
		rs.close();
		stmt.close();
		conn.close();
	}

    /**
     *
     * @param all
     * @return
     */
	public static String parse(String all) {
		String comment = "";
		int index = all.indexOf("COMMENT='");
		if (index < 0) {
			return "";
		}
		comment = all.substring(index + 9);
		comment = comment.substring(0, comment.length() - 1);
		return comment;
	}

    /**
     * 数据库类型转换java类型(有后续需求自行往上加)
     * @param sqlType 数据库类型
     * @return java类型
     */
	public static String typeProcessor(String sqlType) {
		sqlType = sqlType.toLowerCase();
		String newType;
		if (sqlType.contains("varchar"))
			//newType = "String";
			newType = sqlType.replace("varchar","String");
		else if (sqlType.contains("char"))
			newType = sqlType.replace("char","String");
			//newType = "String";
		else if (sqlType.contains("text"))
			newType = sqlType.replace("text","String");
		else if (sqlType.contains("bigint"))
			newType = sqlType.replace("bigint","Long");
			//newType = "Long";
		else if (sqlType.contains("smallint"))
			newType = sqlType.replace("smallint","Int");
			//newType = "Int";
		else if (sqlType.contains("int"))
			newType = sqlType.replace("int","Int");
			//newType = "Int";
		else if (sqlType.contains("integer"))
			newType = sqlType.replace("integer","Int");
			//newType = "Int";
		else if (sqlType.contains("decimal"))
			newType = sqlType.replace("decimal","Double");
			//newType = "double";
		else if (sqlType.contains("timestmp"))
			newType = sqlType.replace("timestmp","Date");
			//newType = "Date";
		else if (sqlType.contains("datetime"))
			newType = sqlType.replace("datetime","Date");
			//newType = "Date";
		else
			newType = sqlType;

		return newType;
	}

    /**
     * 下划线转驼峰法
     * @param line 源字符串
     * @param smallCamel 大小驼峰,是否为小驼峰
     * @return 转换后的字符串
     */
	public static String underline2Camel(String line, boolean smallCamel) {
		if (line == null || "".equals(line)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
		Matcher matcher = pattern.matcher(line);
		while (matcher.find()) {
			String word = matcher.group();
			sb.append(smallCamel && matcher.start() == 0 ? Character
					.toLowerCase(word.charAt(0)) : Character.toUpperCase(word
					.charAt(0)));
			int index = word.lastIndexOf('_');
			if (index > 0) {
				sb.append(word.substring(1, index).toLowerCase());
			} else {
				sb.append(word.substring(1).toLowerCase());
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		String tableName = "sys_role";
		// String comment = getCommentByTableName(tableName);
		// System.out.println("Table Name: " + tableName + ", Comment: " + comment);
        getTableNameProperties(tableName);// 入参字段输出
        //getTableNameListOut(tableName);// 列表出参字段输出
        //getTableNameDetailOut(tableName);// 详情出参字段输出
	}
}