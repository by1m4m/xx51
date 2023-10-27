package org.apache.commons.net.ftp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public abstract interface FTPFileEntryParser
{
  public abstract FTPFile parseFTPEntry(String paramString);
  
  public abstract String readNextEntry(BufferedReader paramBufferedReader)
    throws IOException;
  
  public abstract List<String> preParse(List<String> paramList);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\ftp\FTPFileEntryParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */