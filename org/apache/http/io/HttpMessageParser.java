package org.apache.http.io;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;

public abstract interface HttpMessageParser
{
  public abstract HttpMessage parse()
    throws IOException, HttpException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\io\HttpMessageParser.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */