package com.mysql.jdbc;

import java.sql.SQLException;

public abstract interface SocketMetadata
{
  public abstract boolean isLocallyConnected(ConnectionImpl paramConnectionImpl)
    throws SQLException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\SocketMetadata.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */