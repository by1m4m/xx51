package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;

public abstract interface Extension
{
  public abstract void init(Connection paramConnection, Properties paramProperties)
    throws SQLException;
  
  public abstract void destroy();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\Extension.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */