package org.apache.http.client;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;

public abstract interface CredentialsProvider
{
  public abstract void setCredentials(AuthScope paramAuthScope, Credentials paramCredentials);
  
  public abstract Credentials getCredentials(AuthScope paramAuthScope);
  
  public abstract void clear();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\client\CredentialsProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */