package org.apache.commons.net.nntp;

public abstract interface Threadable
{
  public abstract boolean isDummy();
  
  public abstract String messageThreadId();
  
  public abstract String[] messageThreadReferences();
  
  public abstract String simplifiedSubject();
  
  public abstract boolean subjectIsReply();
  
  public abstract void setChild(Threadable paramThreadable);
  
  public abstract void setNext(Threadable paramThreadable);
  
  public abstract Threadable makeDummy();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\nntp\Threadable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */