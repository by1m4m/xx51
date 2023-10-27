package com.mysql.jdbc;

import java.sql.SQLException;

public abstract interface LoadBalancedConnection
  extends MySQLConnection
{
  public abstract boolean addHost(String paramString)
    throws SQLException;
  
  public abstract void removeHost(String paramString)
    throws SQLException;
  
  public abstract void removeHostWhenNotInUse(String paramString)
    throws SQLException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\LoadBalancedConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */