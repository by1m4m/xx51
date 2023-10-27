package org.eclipse.jetty.util.security;

public abstract interface CredentialProvider
{
  public abstract Credential getCredential(String paramString);
  
  public abstract String getPrefix();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\security\CredentialProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */