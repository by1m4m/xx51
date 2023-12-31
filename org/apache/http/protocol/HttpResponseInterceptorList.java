package org.apache.http.protocol;

import java.util.List;
import org.apache.http.HttpResponseInterceptor;

public abstract interface HttpResponseInterceptorList
{
  public abstract void addResponseInterceptor(HttpResponseInterceptor paramHttpResponseInterceptor);
  
  public abstract void addResponseInterceptor(HttpResponseInterceptor paramHttpResponseInterceptor, int paramInt);
  
  public abstract int getResponseInterceptorCount();
  
  public abstract HttpResponseInterceptor getResponseInterceptor(int paramInt);
  
  public abstract void clearResponseInterceptors();
  
  public abstract void removeResponseInterceptorByClass(Class paramClass);
  
  public abstract void setInterceptors(List paramList);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\protocol\HttpResponseInterceptorList.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */