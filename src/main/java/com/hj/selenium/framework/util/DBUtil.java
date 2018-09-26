/**
 * 
 */
package com.hj.selenium.framework.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author David
 * 
 */
//TODO: need refactoring or adding hibernate support etc..
public class DBUtil
{
	private static final Logger logger = LoggerFactory.getLogger(DBUtil.class);
	
	private static ComboPooledDataSource ds = new ComboPooledDataSource("oracle");
	
	/**
	 * Connects to DB using JDBC driver. Supports H2, MySQL, Oracle DB.
	 * Has inputs for selecting db type, db url (IP, port, SID, etc), username, password
	 * @return Connection to the DB, in order to query it
	 * @throws Exception
	 * @author Ben Yong
	 */
	public void dbConnect(String sql, JDBCCallback callback) throws Exception
	{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			conn = ds.getConnection();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			callback.executeSQL(rs);
		}
		catch (Exception e)
		{
			logger.error("DB Connection Error: " + e.getMessage());
			throw e;
		}
		finally
		{
			closeResultSet(rs);
			closeStatement(stmt);
			closeConnection(conn);
		}
	}
	
	/**
	 * closes the ResultSet
	 * @param rs
	 */
	private void closeResultSet(ResultSet rs)
	{
		try
		{
			if (rs != null)
			{
				rs.close();
			}
		}
		catch (Exception e)
		{
			logger.debug("error occurs while closing ResultSet: " + e.getMessage());
		};
	}
	
	/**
	 * closes the statement.
	 * @param stmt
	 */
	private void closeStatement(PreparedStatement stmt)
	{
		try
		{
			if (stmt != null)
			{
				stmt.close();
			}
		}
		catch (Exception e)
		{
			logger.debug("error occurs while closing Statement: " + e.getMessage());
		};
	}
	
	/**
	 * Disconnect from the DB. Has inputs for the DB Connection, in order to close it
	 * @throws Exception
	 * @author Ben Yong
	 */
	private void closeConnection(Connection conn)
	{
		try
		{
			if (null != conn)
			{
				conn.close();
			}
		}
		catch (SQLException e)
		{
			logger.debug("DB Disconnect Error! " + e.getMessage());
		}
	}
	
	/**
	 * This is the JDBC callback interface used for the client to implement its own JDBC operation.
	 * 
	 * @author David
	 * 
	 */
	public interface JDBCCallback
	{
		public void executeSQL(ResultSet rs) throws SQLException;
	}
	
}
