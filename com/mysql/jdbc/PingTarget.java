package com.mysql.jdbc;

import java.sql.SQLException;

public abstract interface PingTarget
{
  public abstract void doPing()
    throws SQLException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\PingTarget.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */