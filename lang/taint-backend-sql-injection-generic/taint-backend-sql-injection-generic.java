/**
 * OWASP Benchmark v1.2
 *
 * <p>This file is part of the Open Web Application Security Project (OWASP) Benchmark Project. For
 * details, please see <a
 * href="https://owasp.org/www-project-benchmark/">https://owasp.org/www-project-benchmark/</a>.
 *
 * <p>The OWASP Benchmark is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, version 2.
 *
 * <p>The OWASP Benchmark is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details.
 *
 * @author Dave Wichers
 * @created 2015
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/sqli-00/BenchmarkTest00008")
public class bad1 extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// some code
		response.setContentType("text/html;charset=UTF-8");

		String param = "";
		if (request.getHeader("BenchmarkTest00008") != null) {
			param = request.getHeader("BenchmarkTest00008");
		}

		// URL Decode the header value since req.getHeader() doesn't. Unlike
		// req.getParameter().
		param = java.net.URLDecoder.decode(param, "UTF-8");

		String sql = "{call " + param + "}";

		try {
			java.sql.Connection connection = org.owasp.benchmark.helpers.DatabaseHelper.getSqlConnection();
			// ruleid: taint-backend-sql-injection-generic
			java.sql.CallableStatement statement = connection.prepareCall(sql);
			java.sql.ResultSet rs = statement.executeQuery();
			org.owasp.benchmark.helpers.DatabaseHelper.printResults(rs, sql, response);

		} catch (java.sql.SQLException e) {
			if (org.owasp.benchmark.helpers.DatabaseHelper.hideSQLErrors) {
				response.getWriter().println("Error processing request.");
				return;
			} else
				throw new ServletException(e);
		}
	}
}

@WebServlet(value = "/sqli-00/BenchmarkTest00018")
public class bad2 extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// some code
		response.setContentType("text/html;charset=UTF-8");

		String param = "";
		java.util.Enumeration<String> headers = request.getHeaders("BenchmarkTest00018");

		if (headers != null && headers.hasMoreElements()) {
			param = headers.nextElement(); // just grab first element
		}

		// URL Decode the header value since req.getHeaders() doesn't. Unlike
		// req.getParameters().
		param = java.net.URLDecoder.decode(param, "UTF-8");

		String sql = "INSERT INTO users (username, password) VALUES ('foo','" + param + "')";

		try {
			java.sql.Statement statement = org.owasp.benchmark.helpers.DatabaseHelper.getSqlStatement();
			// ruleid: taint-backend-sql-injection-generic
			int count = statement.executeUpdate(sql);
			org.owasp.benchmark.helpers.DatabaseHelper.outputUpdateComplete(sql, response);
		} catch (java.sql.SQLException e) {
			if (org.owasp.benchmark.helpers.DatabaseHelper.hideSQLErrors) {
				response.getWriter().println("Error processing request.");
				return;
			} else
				throw new ServletException(e);
		}
	}
}

@WebServlet(value = "/sqli-00/BenchmarkTest00024")
public class bad3 extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// some code
		response.setContentType("text/html;charset=UTF-8");

		String param = request.getParameter("BenchmarkTest00024");
		if (param == null)
			param = "";

		String sql = "SELECT * from USERS where USERNAME=? and PASSWORD='" + param + "'";

		try {
			java.sql.Connection connection = org.owasp.benchmark.helpers.DatabaseHelper.getSqlConnection();
			// ruleid: taint-backend-sql-injection-generic
			java.sql.PreparedStatement statement = connection.prepareStatement(
					// ruleid: taint-backend-sql-injection-generic
					sql,
					java.sql.ResultSet.TYPE_FORWARD_ONLY,
					java.sql.ResultSet.CONCUR_READ_ONLY,
					java.sql.ResultSet.CLOSE_CURSORS_AT_COMMIT);
			statement.setString(1, "foo");
			statement.execute();
			org.owasp.benchmark.helpers.DatabaseHelper.printResults(statement, sql, response);
		} catch (java.sql.SQLException e) {
			if (org.owasp.benchmark.helpers.DatabaseHelper.hideSQLErrors) {
				response.getWriter().println("Error processing request.");
				return;
			} else
				throw new ServletException(e);
		}
	}
}

@WebServlet(value = "/sqli-00/BenchmarkTest00025")
public class bad4 extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// some code
		response.setContentType("text/html;charset=UTF-8");

		String param = request.getParameter("BenchmarkTest00025");
		if (param == null)
			param = "";

		String sql = "SELECT userid from USERS where USERNAME='foo' and PASSWORD='" + param + "'";
		try {
			// No longer needed - covered by JDBC specific rule
			//
			// Long results =
			// org.owasp.benchmark.helpers.DatabaseHelper.JDBCtemplate.queryForLong(sql);
			// Long results =
			// org.owasp.benchmark.helpers.DatabaseHelper.JDBCtemplate.queryForObject(
			// sql, Long.class);
			response.getWriter().println("Your results are: " + String.valueOf(results));
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			response.getWriter()
					.println(
							"No results returned for query: "
									+ org.owasp.esapi.ESAPI.encoder().encodeForHTML(sql));
		} catch (org.springframework.dao.DataAccessException e) {
			if (org.owasp.benchmark.helpers.DatabaseHelper.hideSQLErrors) {
				response.getWriter().println("Error processing request.");
			} else
				throw new ServletException(e);
		}
	}
}

@WebServlet(value = "/sqli-00/BenchmarkTest00026")
public class bad5 extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// some code
		response.setContentType("text/html;charset=UTF-8");

		String param = request.getParameter("BenchmarkTest00026");
		if (param == null)
			param = "";

		String sql = "SELECT  * from USERS where USERNAME='foo' and PASSWORD='" + param + "'";
		try {
			// It is not necessary to cover this as it is covered by the JDBC specific rule
			//
			// org.springframework.jdbc.support.rowset.SqlRowSet results =
			// org.owasp.benchmark.helpers.DatabaseHelper.JDBCtemplate.queryForRowSet(sql);
			// response.getWriter().println("Your results are: ");

			// System.out.println("Your results are");
			while (results.next()) {
				response.getWriter()
						.println(
								org.owasp.esapi.ESAPI
										.encoder()
										.encodeForHTML(results.getString("USERNAME"))
										+ " ");
				// System.out.println(results.getString("USERNAME"));
			}
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			response.getWriter()
					.println(
							"No results returned for query: "
									+ org.owasp.esapi.ESAPI.encoder().encodeForHTML(sql));
		} catch (org.springframework.dao.DataAccessException e) {
			if (org.owasp.benchmark.helpers.DatabaseHelper.hideSQLErrors) {
				response.getWriter().println("Error processing request.");
			} else
				throw new ServletException(e);
		}
	}
}

@WebServlet(value = "/sqli-00/BenchmarkTest00008")
public class bad1 extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// some code
		response.setContentType("text/html;charset=UTF-8");

		String param = "test";

		String sql = "{call " + param + "}";

		try {
			java.sql.Connection connection = org.owasp.benchmark.helpers.DatabaseHelper.getSqlConnection();
			// ok: taint-backend-sql-injection-generic
			java.sql.CallableStatement statement = connection.prepareCall(sql);
			java.sql.ResultSet rs = statement.executeQuery();
			org.owasp.benchmark.helpers.DatabaseHelper.printResults(rs, sql, response);

		} catch (java.sql.SQLException e) {
			if (org.owasp.benchmark.helpers.DatabaseHelper.hideSQLErrors) {
				response.getWriter().println("Error processing request.");
				return;
			} else
				throw new ServletException(e);
		}
	}
}

public class Util {

	private static final long serialVersionUID = 1L;

	public static void executeSql(String sql) {
		java.sql.Connection connection = org.owasp.benchmark.helpers.DatabaseHelper.getSqlConnection();
		// ok: taint-backend-sql-injection-generic
		connection.prepareCall(sql).executeQuery();
	}

}

public class Util {

	private static final long serialVersionUID = 1L;

	public static void executeSql(String sql) {
		java.sql.Connection connection = org.owasp.benchmark.helpers.DatabaseHelper.getSqlConnection();
		// ok: taint-backend-sql-injection-generic
		connection.prepareCall(sql).executeQuery();
	}

}

@WebServlet(value = "/sqli-03/BenchmarkTest01811")
public class BenchmarkTest01811 extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		org.owasp.benchmark.helpers.SeparateClassRequest scr = new org.owasp.benchmark.helpers.SeparateClassRequest(
				request);
		String param = scr.getTheValue("BenchmarkTest01811");

		String bar = new Test().doSomething(request, param);

		String sql = "SELECT TOP 1 userid from USERS where USERNAME='foo' and PASSWORD='" + bar + "'";
		try {
			// ruleid: taint-backend-sql-injection-generic
			java.util.Map<String, Object> results = org.owasp.benchmark.helpers.DatabaseHelper.JDBCtemplate .queryForMap(sql);
			response.getWriter().println("Your results are: ");

			// System.out.println("Your results are");
			response.getWriter()
					.println(org.owasp.esapi.ESAPI.encoder().encodeForHTML(results.toString()));
			// System.out.println(results.toString());
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			response.getWriter()
					.println(
							"No results returned for query: "
									+ org.owasp.esapi.ESAPI.encoder().encodeForHTML(sql));
		} catch (org.springframework.dao.DataAccessException e) {
			if (org.owasp.benchmark.helpers.DatabaseHelper.hideSQLErrors) {
				response.getWriter().println("Error processing request.");
			} else
				throw new ServletException(e);
		}
	} // end doPost

	private class Test {

		public String doSomething(HttpServletRequest request, String param)
				throws ServletException, IOException {

			String bar = "safe!";
			java.util.HashMap<String, Object> map31047 = new java.util.HashMap<String, Object>();
			map31047.put("keyA-31047", "a_Value"); // put some stuff in the collection
			map31047.put("keyB-31047", param); // put it in a collection
			map31047.put("keyC", "another_Value"); // put some stuff in the collection
			bar = (String) map31047.get("keyB-31047"); // get it back out
			bar = (String) map31047.get("keyA-31047"); // get safe value back out

			return bar;
		}
	} // end innerclass Test
} // end DataflowThruInnerClass
