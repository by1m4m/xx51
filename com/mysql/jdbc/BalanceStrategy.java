package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract interface BalanceStrategy
  extends Extension
{
  public abstract ConnectionImpl pickConnection(LoadBalancingConnectionProxy paramLoadBalancingConnectionProxy, List<String> paramList, Map<String, ConnectionImpl> paramMap, long[] paramArrayOfLong, int paramInt)
    throws SQLException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\BalanceStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */