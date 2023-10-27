package org.apache.http.io;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;

public abstract interface HttpMessageWriter
{
  public abstract void write(HttpMessage paramHttpMessage)
    throws IOException, HttpException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\io\HttpMessageWriter.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */