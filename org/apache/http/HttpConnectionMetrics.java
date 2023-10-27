package org.apache.http;

public abstract interface HttpConnectionMetrics
{
  public abstract long getRequestCount();
  
  public abstract long getResponseCount();
  
  public abstract long getSentBytesCount();
  
  public abstract long getReceivedBytesCount();
  
  public abstract Object getMetric(String paramString);
  
  public abstract void reset();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\HttpConnectionMetrics.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */