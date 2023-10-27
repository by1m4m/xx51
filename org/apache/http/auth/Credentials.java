package org.apache.http.auth;

import java.security.Principal;

public abstract interface Credentials
{
  public abstract Principal getUserPrincipal();
  
  public abstract String getPassword();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\auth\Credentials.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */