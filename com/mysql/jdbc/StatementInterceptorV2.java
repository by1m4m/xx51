package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;

public abstract interface StatementInterceptorV2
  extends Extension
{
  public abstract void init(Connection paramConnection, Properties paramProperties)
    throws SQLException;
  
  public abstract ResultSetInternalMethods preProcess(String paramString, Statement paramStatement, Connection paramConnection)
    throws SQLException;
  
  public abstract boolean executeTopLevelOnly();
  
  public abstract void destroy();
  
  public abstract ResultSetInternalMethods postProcess(String paramString, Statement paramStatement, ResultSetInternalMethods paramResultSetInternalMethods, Connection paramConnection, int paramInt, boolean paramBoolean1, boolean paramBoolean2, SQLException paramSQLException)
    throws SQLException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\StatementInterceptorV2.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */