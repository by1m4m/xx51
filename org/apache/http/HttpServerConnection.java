package org.apache.http;

import java.io.IOException;

public abstract interface HttpServerConnection
  extends HttpConnection
{
  public abstract HttpRequest receiveRequestHeader()
    throws HttpException, IOException;
  
  public abstract void receiveRequestEntity(HttpEntityEnclosingRequest paramHttpEntityEnclosingRequest)
    throws HttpException, IOException;
  
  public abstract void sendResponseHeader(HttpResponse paramHttpResponse)
    throws HttpException, IOException;
  
  public abstract void sendResponseEntity(HttpResponse paramHttpResponse)
    throws HttpException, IOException;
  
  public abstract void flush()
    throws IOException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\HttpServerConnection.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */