package org.apache.commons.net.ftp.parser;

import java.text.ParseException;
import java.util.Calendar;

public abstract interface FTPTimestampParser
{
  public static final String DEFAULT_SDF = "MMM d yyyy";
  public static final String DEFAULT_RECENT_SDF = "MMM d HH:mm";
  
  public abstract Calendar parseTimestamp(String paramString)
    throws ParseException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\ftp\parser\FTPTimestampParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */