package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;

public abstract interface StatementInterceptor
  extends Extension
{
  public abstract void init(Connection paramConnection, Properties paramProperties)
    throws SQLException;
  
  public abstract ResultSetInternalMethods preProcess(String paramString, Statement paramStatement, Connection paramConnection)
    throws SQLException;
  
  public abstract ResultSetInternalMethods postProcess(String paramString, Statement paramStatement, ResultSetInternalMethods paramResultSetInternalMethods, Connection paramConnection)
    throws SQLException;
  
  public abstract boolean executeTopLevelOnly();
  
  public abstract void destroy();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\StatementInterceptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */