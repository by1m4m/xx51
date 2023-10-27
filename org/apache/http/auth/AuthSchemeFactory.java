package org.apache.http.auth;

import org.apache.http.params.HttpParams;

public abstract interface AuthSchemeFactory
{
  public abstract AuthScheme newInstance(HttpParams paramHttpParams);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\auth\AuthSchemeFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */