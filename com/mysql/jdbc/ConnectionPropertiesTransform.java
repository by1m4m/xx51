package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;

public abstract interface ConnectionPropertiesTransform
{
  public abstract Properties transformProperties(Properties paramProperties)
    throws SQLException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\ConnectionPropertiesTransform.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */