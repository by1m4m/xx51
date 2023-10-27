package com.google.api.client.http;

import com.google.api.client.util.Beta;
import java.io.IOException;

@Deprecated
@Beta
public abstract interface BackOffPolicy
{
  public static final long STOP = -1L;
  
  public abstract boolean isBackOffRequired(int paramInt);
  
  public abstract void reset();
  
  public abstract long getNextBackOffMillis()
    throws IOException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\BackOffPolicy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */