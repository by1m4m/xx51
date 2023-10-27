package org.apache.http.auth;

import org.apache.http.Header;
import org.apache.http.HttpRequest;

public abstract interface AuthScheme
{
  public abstract void processChallenge(Header paramHeader)
    throws MalformedChallengeException;
  
  public abstract String getSchemeName();
  
  public abstract String getParameter(String paramString);
  
  public abstract String getRealm();
  
  public abstract boolean isConnectionBased();
  
  public abstract boolean isComplete();
  
  public abstract Header authenticate(Credentials paramCredentials, HttpRequest paramHttpRequest)
    throws AuthenticationException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\auth\AuthScheme.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */