package org.apache.http.entity;

import java.io.IOException;
import java.io.OutputStream;

public abstract interface ContentProducer
{
  public abstract void writeTo(OutputStream paramOutputStream)
    throws IOException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\entity\ContentProducer.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */