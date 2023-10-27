package org.eclipse.jetty.http;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;
import org.eclipse.jetty.util.resource.Resource;

public abstract interface HttpContent
{
  public abstract HttpField getContentType();
  
  public abstract String getContentTypeValue();
  
  public abstract String getCharacterEncoding();
  
  public abstract MimeTypes.Type getMimeType();
  
  public abstract HttpField getContentEncoding();
  
  public abstract String getContentEncodingValue();
  
  public abstract HttpField getContentLength();
  
  public abstract long getContentLengthValue();
  
  public abstract HttpField getLastModified();
  
  public abstract String getLastModifiedValue();
  
  public abstract HttpField getETag();
  
  public abstract String getETagValue();
  
  public abstract ByteBuffer getIndirectBuffer();
  
  public abstract ByteBuffer getDirectBuffer();
  
  public abstract Resource getResource();
  
  public abstract InputStream getInputStream()
    throws IOException;
  
  public abstract ReadableByteChannel getReadableByteChannel()
    throws IOException;
  
  public abstract void release();
  
  public abstract Map<CompressedContentFormat, ? extends HttpContent> getPrecompressedContents();
  
  public static abstract interface ContentFactory
  {
    public abstract HttpContent getContent(String paramString, int paramInt)
      throws IOException;
  }
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\HttpContent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */