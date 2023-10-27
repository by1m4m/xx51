/*      */ package org.apache.commons.net.ftp;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.Reader;
/*      */ import java.net.Inet6Address;
/*      */ import java.net.InetAddress;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.ServerSocket;
/*      */ import java.net.Socket;
/*      */ import java.net.SocketException;
/*      */ import java.net.SocketTimeoutException;
/*      */ import java.net.UnknownHostException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Locale;
/*      */ import java.util.Properties;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import javax.net.ServerSocketFactory;
/*      */ import javax.net.SocketFactory;
/*      */ import org.apache.commons.net.MalformedServerReplyException;
/*      */ import org.apache.commons.net.ftp.parser.DefaultFTPFileEntryParserFactory;
/*      */ import org.apache.commons.net.ftp.parser.FTPFileEntryParserFactory;
/*      */ import org.apache.commons.net.ftp.parser.MLSxEntryParser;
/*      */ import org.apache.commons.net.io.CRLFLineReader;
/*      */ import org.apache.commons.net.io.CopyStreamAdapter;
/*      */ import org.apache.commons.net.io.CopyStreamEvent;
/*      */ import org.apache.commons.net.io.CopyStreamListener;
/*      */ import org.apache.commons.net.io.FromNetASCIIInputStream;
/*      */ import org.apache.commons.net.io.SocketInputStream;
/*      */ import org.apache.commons.net.io.SocketOutputStream;
/*      */ import org.apache.commons.net.io.ToNetASCIIOutputStream;
/*      */ import org.apache.commons.net.io.Util;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class FTPClient
/*      */   extends FTP
/*      */   implements Configurable
/*      */ {
/*      */   public static final String FTP_SYSTEM_TYPE = "org.apache.commons.net.ftp.systemType";
/*      */   public static final String FTP_SYSTEM_TYPE_DEFAULT = "org.apache.commons.net.ftp.systemType.default";
/*      */   public static final String SYSTEM_TYPE_PROPERTIES = "/systemType.properties";
/*      */   public static final int ACTIVE_LOCAL_DATA_CONNECTION_MODE = 0;
/*      */   public static final int ACTIVE_REMOTE_DATA_CONNECTION_MODE = 1;
/*      */   public static final int PASSIVE_LOCAL_DATA_CONNECTION_MODE = 2;
/*      */   public static final int PASSIVE_REMOTE_DATA_CONNECTION_MODE = 3;
/*      */   private int __dataConnectionMode;
/*      */   private int __dataTimeout;
/*      */   private int __passivePort;
/*      */   private String __passiveHost;
/*      */   private final Random __random;
/*      */   private int __activeMinPort;
/*      */   private int __activeMaxPort;
/*      */   private InetAddress __activeExternalHost;
/*      */   private InetAddress __reportActiveExternalHost;
/*      */   private InetAddress __passiveLocalHost;
/*      */   private int __fileType;
/*      */   private int __fileFormat;
/*      */   private int __fileStructure;
/*      */   private int __fileTransferMode;
/*      */   private boolean __remoteVerificationEnabled;
/*      */   private long __restartOffset;
/*      */   private FTPFileEntryParserFactory __parserFactory;
/*      */   private int __bufferSize;
/*      */   private int __sendDataSocketBufferSize;
/*      */   private int __receiveDataSocketBufferSize;
/*      */   private boolean __listHiddenFiles;
/*      */   private boolean __useEPSVwithIPv4;
/*      */   private String __systemName;
/*      */   private FTPFileEntryParser __entryParser;
/*      */   private String __entryParserKey;
/*      */   private FTPClientConfig __configuration;
/*      */   private CopyStreamListener __copyStreamListener;
/*      */   private long __controlKeepAliveTimeout;
/*  406 */   private int __controlKeepAliveReplyTimeout = 1000;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  412 */   private HostnameResolver __passiveNatWorkaroundStrategy = new NatServerResolverImpl(this);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  418 */   private static final Pattern __PARMS_PAT = Pattern.compile("(\\d{1,3},\\d{1,3},\\d{1,3},\\d{1,3}),(\\d{1,3}),(\\d{1,3})");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  423 */   private boolean __autodetectEncoding = false;
/*      */   
/*      */   private HashMap<String, Set<String>> __featuresMap;
/*      */   
/*      */   private static class PropertiesSingleton
/*      */   {
/*      */     static final Properties PROPERTIES;
/*      */     
/*      */     static
/*      */     {
/*  433 */       InputStream resourceAsStream = FTPClient.class.getResourceAsStream("/systemType.properties");
/*  434 */       Properties p = null;
/*  435 */       if (resourceAsStream != null) {
/*  436 */         p = new Properties();
/*      */         try {
/*  438 */           p.load(resourceAsStream);
/*      */           
/*      */ 
/*      */           try
/*      */           {
/*  443 */             resourceAsStream.close();
/*      */           }
/*      */           catch (IOException e) {}
/*      */           
/*      */ 
/*      */ 
/*  449 */           PROPERTIES = p;
/*      */         }
/*      */         catch (IOException e) {}finally
/*      */         {
/*      */           try
/*      */           {
/*  443 */             resourceAsStream.close();
/*      */           }
/*      */           catch (IOException e) {}
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static Properties getOverrideProperties()
/*      */   {
/*  454 */     return PropertiesSingleton.PROPERTIES;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FTPClient()
/*      */   {
/*  477 */     __initDefaults();
/*  478 */     this.__dataTimeout = -1;
/*  479 */     this.__remoteVerificationEnabled = true;
/*  480 */     this.__parserFactory = new DefaultFTPFileEntryParserFactory();
/*  481 */     this.__configuration = null;
/*  482 */     this.__listHiddenFiles = false;
/*  483 */     this.__useEPSVwithIPv4 = false;
/*  484 */     this.__random = new Random();
/*  485 */     this.__passiveLocalHost = null;
/*      */   }
/*      */   
/*      */ 
/*      */   private void __initDefaults()
/*      */   {
/*  491 */     this.__dataConnectionMode = 0;
/*  492 */     this.__passiveHost = null;
/*  493 */     this.__passivePort = -1;
/*  494 */     this.__activeExternalHost = null;
/*  495 */     this.__reportActiveExternalHost = null;
/*  496 */     this.__activeMinPort = 0;
/*  497 */     this.__activeMaxPort = 0;
/*  498 */     this.__fileType = 0;
/*  499 */     this.__fileStructure = 7;
/*  500 */     this.__fileFormat = 4;
/*  501 */     this.__fileTransferMode = 10;
/*  502 */     this.__restartOffset = 0L;
/*  503 */     this.__systemName = null;
/*  504 */     this.__entryParser = null;
/*  505 */     this.__entryParserKey = "";
/*  506 */     this.__featuresMap = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static String __parsePathname(String reply)
/*      */   {
/*  528 */     String param = reply.substring(4);
/*  529 */     if (param.startsWith("\"")) {
/*  530 */       StringBuilder sb = new StringBuilder();
/*  531 */       boolean quoteSeen = false;
/*      */       
/*  533 */       for (int i = 1; i < param.length(); i++) {
/*  534 */         char ch = param.charAt(i);
/*  535 */         if (ch == '"') {
/*  536 */           if (quoteSeen) {
/*  537 */             sb.append(ch);
/*  538 */             quoteSeen = false;
/*      */           }
/*      */           else {
/*  541 */             quoteSeen = true;
/*      */           }
/*      */         } else {
/*  544 */           if (quoteSeen) {
/*  545 */             return sb.toString();
/*      */           }
/*  547 */           sb.append(ch);
/*      */         }
/*      */       }
/*  550 */       if (quoteSeen) {
/*  551 */         return sb.toString();
/*      */       }
/*      */     }
/*      */     
/*  555 */     return param;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _parsePassiveModeReply(String reply)
/*      */     throws MalformedServerReplyException
/*      */   {
/*  566 */     Matcher m = __PARMS_PAT.matcher(reply);
/*  567 */     if (!m.find()) {
/*  568 */       throw new MalformedServerReplyException("Could not parse passive host information.\nServer Reply: " + reply);
/*      */     }
/*      */     
/*      */ 
/*  572 */     this.__passiveHost = m.group(1).replace(',', '.');
/*      */     
/*      */     try
/*      */     {
/*  576 */       int oct1 = Integer.parseInt(m.group(2));
/*  577 */       int oct2 = Integer.parseInt(m.group(3));
/*  578 */       this.__passivePort = (oct1 << 8 | oct2);
/*      */     }
/*      */     catch (NumberFormatException e)
/*      */     {
/*  582 */       throw new MalformedServerReplyException("Could not parse passive port information.\nServer Reply: " + reply);
/*      */     }
/*      */     
/*      */ 
/*  586 */     if (this.__passiveNatWorkaroundStrategy != null) {
/*      */       try {
/*  588 */         String passiveHost = this.__passiveNatWorkaroundStrategy.resolve(this.__passiveHost);
/*  589 */         if (!this.__passiveHost.equals(passiveHost)) {
/*  590 */           fireReplyReceived(0, "[Replacing PASV mode reply address " + this.__passiveHost + " with " + passiveHost + "]\n");
/*      */           
/*  592 */           this.__passiveHost = passiveHost;
/*      */         }
/*      */       } catch (UnknownHostException e) {
/*  595 */         throw new MalformedServerReplyException("Could not parse passive host information.\nServer Reply: " + reply);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void _parseExtendedPassiveModeReply(String reply)
/*      */     throws MalformedServerReplyException
/*      */   {
/*  604 */     reply = reply.substring(reply.indexOf('(') + 1, reply.indexOf(')')).trim();
/*      */     
/*      */ 
/*      */ 
/*  608 */     char delim1 = reply.charAt(0);
/*  609 */     char delim2 = reply.charAt(1);
/*  610 */     char delim3 = reply.charAt(2);
/*  611 */     char delim4 = reply.charAt(reply.length() - 1);
/*      */     
/*  613 */     if ((delim1 != delim2) || (delim2 != delim3) || (delim3 != delim4))
/*      */     {
/*  615 */       throw new MalformedServerReplyException("Could not parse extended passive host information.\nServer Reply: " + reply);
/*      */     }
/*      */     
/*      */     int port;
/*      */     
/*      */     try
/*      */     {
/*  622 */       port = Integer.parseInt(reply.substring(3, reply.length() - 1));
/*      */     }
/*      */     catch (NumberFormatException e)
/*      */     {
/*  626 */       throw new MalformedServerReplyException("Could not parse extended passive host information.\nServer Reply: " + reply);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  632 */     this.__passiveHost = getRemoteAddress().getHostAddress();
/*  633 */     this.__passivePort = port;
/*      */   }
/*      */   
/*      */   private boolean __storeFile(FTPCmd command, String remote, InputStream local)
/*      */     throws IOException
/*      */   {
/*  639 */     return _storeFile(command.getCommand(), remote, local);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _storeFile(String command, String remote, InputStream local)
/*      */     throws IOException
/*      */   {
/*  653 */     Socket socket = _openDataConnection_(command, remote);
/*      */     
/*  655 */     if (socket == null) {
/*  656 */       return false;
/*      */     }
/*      */     
/*      */     OutputStream output;
/*      */     OutputStream output;
/*  661 */     if (this.__fileType == 0) {
/*  662 */       output = new ToNetASCIIOutputStream(getBufferedOutputStream(socket.getOutputStream()));
/*      */     } else {
/*  664 */       output = getBufferedOutputStream(socket.getOutputStream());
/*      */     }
/*      */     
/*  667 */     CSL csl = null;
/*  668 */     if (this.__controlKeepAliveTimeout > 0L) {
/*  669 */       csl = new CSL(this, this.__controlKeepAliveTimeout, this.__controlKeepAliveReplyTimeout);
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/*  675 */       Util.copyStream(local, output, getBufferSize(), -1L, __mergeListeners(csl), false);
/*      */ 
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/*      */ 
/*  681 */       Util.closeQuietly(socket);
/*  682 */       if (csl != null) {
/*  683 */         csl.cleanUp();
/*      */       }
/*  685 */       throw e;
/*      */     }
/*      */     
/*  688 */     output.close();
/*  689 */     socket.close();
/*  690 */     if (csl != null) {
/*  691 */       csl.cleanUp();
/*      */     }
/*      */     
/*  694 */     boolean ok = completePendingCommand();
/*  695 */     return ok;
/*      */   }
/*      */   
/*      */   private OutputStream __storeFileStream(FTPCmd command, String remote)
/*      */     throws IOException
/*      */   {
/*  701 */     return _storeFileStream(command.getCommand(), remote);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected OutputStream _storeFileStream(String command, String remote)
/*      */     throws IOException
/*      */   {
/*  714 */     Socket socket = _openDataConnection_(command, remote);
/*      */     
/*  716 */     if (socket == null) {
/*  717 */       return null;
/*      */     }
/*      */     OutputStream output;
/*      */     OutputStream output;
/*  721 */     if (this.__fileType == 0)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  729 */       output = new ToNetASCIIOutputStream(getBufferedOutputStream(socket.getOutputStream()));
/*      */     } else {
/*  731 */       output = socket.getOutputStream();
/*      */     }
/*  733 */     return new SocketOutputStream(socket, output);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   protected Socket _openDataConnection_(int command, String arg)
/*      */     throws IOException
/*      */   {
/*  760 */     return _openDataConnection_(FTPCommand.getCommand(command), arg);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Socket _openDataConnection_(FTPCmd command, String arg)
/*      */     throws IOException
/*      */   {
/*  785 */     return _openDataConnection_(command.getCommand(), arg);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Socket _openDataConnection_(String command, String arg)
/*      */     throws IOException
/*      */   {
/*  810 */     if ((this.__dataConnectionMode != 0) && (this.__dataConnectionMode != 2))
/*      */     {
/*  812 */       return null;
/*      */     }
/*      */     
/*  815 */     boolean isInet6Address = getRemoteAddress() instanceof Inet6Address;
/*      */     
/*      */     Socket socket;
/*      */     
/*  819 */     if (this.__dataConnectionMode == 0)
/*      */     {
/*      */ 
/*      */ 
/*  823 */       ServerSocket server = this._serverSocketFactory_.createServerSocket(getActivePort(), 1, getHostAddress());
/*      */       
/*      */ 
/*      */ 
/*      */       try
/*      */       {
/*      */         Socket localSocket1;
/*      */         
/*      */ 
/*      */ 
/*  833 */         if (isInet6Address) {
/*  834 */           if (!FTPReply.isPositiveCompletion(eprt(getReportHostAddress(), server.getLocalPort()))) {
/*  835 */             return null;
/*      */           }
/*      */         }
/*  838 */         else if (!FTPReply.isPositiveCompletion(port(getReportHostAddress(), server.getLocalPort()))) {
/*  839 */           return null;
/*      */         }
/*      */         
/*      */ 
/*  843 */         if ((this.__restartOffset > 0L) && (!restart(this.__restartOffset))) {
/*  844 */           return null;
/*      */         }
/*      */         
/*  847 */         if (!FTPReply.isPositivePreliminary(sendCommand(command, arg))) {
/*  848 */           return null;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  855 */         if (this.__dataTimeout >= 0) {
/*  856 */           server.setSoTimeout(this.__dataTimeout);
/*      */         }
/*  858 */         Socket socket = server.accept();
/*      */         
/*      */ 
/*  861 */         if (this.__dataTimeout >= 0) {
/*  862 */           socket.setSoTimeout(this.__dataTimeout);
/*      */         }
/*  864 */         if (this.__receiveDataSocketBufferSize > 0) {
/*  865 */           socket.setReceiveBufferSize(this.__receiveDataSocketBufferSize);
/*      */         }
/*  867 */         if (this.__sendDataSocketBufferSize > 0) {
/*  868 */           socket.setSendBufferSize(this.__sendDataSocketBufferSize);
/*      */         }
/*      */       } finally {
/*  871 */         server.close();
/*      */ 
/*      */ 
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/*  884 */       boolean attemptEPSV = (isUseEPSVwithIPv4()) || (isInet6Address);
/*  885 */       if ((attemptEPSV) && (epsv() == 229))
/*      */       {
/*  887 */         _parseExtendedPassiveModeReply((String)this._replyLines.get(0));
/*      */       }
/*      */       else
/*      */       {
/*  891 */         if (isInet6Address) {
/*  892 */           return null;
/*      */         }
/*      */         
/*  895 */         if (pasv() != 227) {
/*  896 */           return null;
/*      */         }
/*  898 */         _parsePassiveModeReply((String)this._replyLines.get(0));
/*      */       }
/*      */       
/*  901 */       socket = this._socketFactory_.createSocket();
/*  902 */       if (this.__receiveDataSocketBufferSize > 0) {
/*  903 */         socket.setReceiveBufferSize(this.__receiveDataSocketBufferSize);
/*      */       }
/*  905 */       if (this.__sendDataSocketBufferSize > 0) {
/*  906 */         socket.setSendBufferSize(this.__sendDataSocketBufferSize);
/*      */       }
/*  908 */       if (this.__passiveLocalHost != null) {
/*  909 */         socket.bind(new InetSocketAddress(this.__passiveLocalHost, 0));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  916 */       if (this.__dataTimeout >= 0) {
/*  917 */         socket.setSoTimeout(this.__dataTimeout);
/*      */       }
/*      */       
/*  920 */       socket.connect(new InetSocketAddress(this.__passiveHost, this.__passivePort), this.connectTimeout);
/*  921 */       if ((this.__restartOffset > 0L) && (!restart(this.__restartOffset)))
/*      */       {
/*  923 */         socket.close();
/*  924 */         return null;
/*      */       }
/*      */       
/*  927 */       if (!FTPReply.isPositivePreliminary(sendCommand(command, arg)))
/*      */       {
/*  929 */         socket.close();
/*  930 */         return null;
/*      */       }
/*      */     }
/*      */     
/*  934 */     if ((this.__remoteVerificationEnabled) && (!verifyRemote(socket)))
/*      */     {
/*  936 */       socket.close();
/*      */       
/*  938 */       throw new IOException("Host attempting data connection " + socket.getInetAddress().getHostAddress() + " is not same as server " + getRemoteAddress().getHostAddress());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  943 */     return socket;
/*      */   }
/*      */   
/*      */ 
/*      */   protected void _connectAction_()
/*      */     throws IOException
/*      */   {
/*  950 */     _connectAction_(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _connectAction_(Reader socketIsReader)
/*      */     throws IOException
/*      */   {
/*  962 */     super._connectAction_(socketIsReader);
/*  963 */     __initDefaults();
/*      */     
/*      */ 
/*  966 */     if (this.__autodetectEncoding)
/*      */     {
/*  968 */       ArrayList<String> oldReplyLines = new ArrayList(this._replyLines);
/*  969 */       int oldReplyCode = this._replyCode;
/*  970 */       if ((hasFeature("UTF8")) || (hasFeature("UTF-8")))
/*      */       {
/*  972 */         setControlEncoding("UTF-8");
/*  973 */         this._controlInput_ = new CRLFLineReader(new InputStreamReader(this._input_, getControlEncoding()));
/*      */         
/*  975 */         this._controlOutput_ = new BufferedWriter(new OutputStreamWriter(this._output_, getControlEncoding()));
/*      */       }
/*      */       
/*      */ 
/*  979 */       this._replyLines.clear();
/*  980 */       this._replyLines.addAll(oldReplyLines);
/*  981 */       this._replyCode = oldReplyCode;
/*  982 */       this._newReplyString = true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDataTimeout(int timeout)
/*      */   {
/*  999 */     this.__dataTimeout = timeout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setParserFactory(FTPFileEntryParserFactory parserFactory)
/*      */   {
/* 1012 */     this.__parserFactory = parserFactory;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void disconnect()
/*      */     throws IOException
/*      */   {
/* 1025 */     super.disconnect();
/* 1026 */     __initDefaults();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRemoteVerificationEnabled(boolean enable)
/*      */   {
/* 1041 */     this.__remoteVerificationEnabled = enable;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isRemoteVerificationEnabled()
/*      */   {
/* 1053 */     return this.__remoteVerificationEnabled;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean login(String username, String password)
/*      */     throws IOException
/*      */   {
/* 1073 */     user(username);
/*      */     
/* 1075 */     if (FTPReply.isPositiveCompletion(this._replyCode)) {
/* 1076 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1081 */     if (!FTPReply.isPositiveIntermediate(this._replyCode)) {
/* 1082 */       return false;
/*      */     }
/*      */     
/* 1085 */     return FTPReply.isPositiveCompletion(pass(password));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean login(String username, String password, String account)
/*      */     throws IOException
/*      */   {
/* 1109 */     user(username);
/*      */     
/* 1111 */     if (FTPReply.isPositiveCompletion(this._replyCode)) {
/* 1112 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1117 */     if (!FTPReply.isPositiveIntermediate(this._replyCode)) {
/* 1118 */       return false;
/*      */     }
/*      */     
/* 1121 */     pass(password);
/*      */     
/* 1123 */     if (FTPReply.isPositiveCompletion(this._replyCode)) {
/* 1124 */       return true;
/*      */     }
/*      */     
/* 1127 */     if (!FTPReply.isPositiveIntermediate(this._replyCode)) {
/* 1128 */       return false;
/*      */     }
/*      */     
/* 1131 */     return FTPReply.isPositiveCompletion(acct(account));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean logout()
/*      */     throws IOException
/*      */   {
/* 1148 */     return FTPReply.isPositiveCompletion(quit());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean changeWorkingDirectory(String pathname)
/*      */     throws IOException
/*      */   {
/* 1167 */     return FTPReply.isPositiveCompletion(cwd(pathname));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean changeToParentDirectory()
/*      */     throws IOException
/*      */   {
/* 1185 */     return FTPReply.isPositiveCompletion(cdup());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean structureMount(String pathname)
/*      */     throws IOException
/*      */   {
/* 1204 */     return FTPReply.isPositiveCompletion(smnt(pathname));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean reinitialize()
/*      */     throws IOException
/*      */   {
/* 1223 */     rein();
/*      */     
/* 1225 */     if ((FTPReply.isPositiveCompletion(this._replyCode)) || ((FTPReply.isPositivePreliminary(this._replyCode)) && (FTPReply.isPositiveCompletion(getReply()))))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 1230 */       __initDefaults();
/*      */       
/* 1232 */       return true;
/*      */     }
/*      */     
/* 1235 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void enterLocalActiveMode()
/*      */   {
/* 1250 */     this.__dataConnectionMode = 0;
/* 1251 */     this.__passiveHost = null;
/* 1252 */     this.__passivePort = -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void enterLocalPassiveMode()
/*      */   {
/* 1273 */     this.__dataConnectionMode = 2;
/*      */     
/*      */ 
/* 1276 */     this.__passiveHost = null;
/* 1277 */     this.__passivePort = -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean enterRemoteActiveMode(InetAddress host, int port)
/*      */     throws IOException
/*      */   {
/* 1308 */     if (FTPReply.isPositiveCompletion(port(host, port)))
/*      */     {
/* 1310 */       this.__dataConnectionMode = 1;
/* 1311 */       this.__passiveHost = null;
/* 1312 */       this.__passivePort = -1;
/* 1313 */       return true;
/*      */     }
/* 1315 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean enterRemotePassiveMode()
/*      */     throws IOException
/*      */   {
/* 1342 */     if (pasv() != 227) {
/* 1343 */       return false;
/*      */     }
/*      */     
/* 1346 */     this.__dataConnectionMode = 3;
/* 1347 */     _parsePassiveModeReply((String)this._replyLines.get(0));
/*      */     
/* 1349 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getPassiveHost()
/*      */   {
/* 1366 */     return this.__passiveHost;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getPassivePort()
/*      */   {
/* 1383 */     return this.__passivePort;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getDataConnectionMode()
/*      */   {
/* 1396 */     return this.__dataConnectionMode;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getActivePort()
/*      */   {
/* 1406 */     if ((this.__activeMinPort > 0) && (this.__activeMaxPort >= this.__activeMinPort))
/*      */     {
/* 1408 */       if (this.__activeMaxPort == this.__activeMinPort) {
/* 1409 */         return this.__activeMaxPort;
/*      */       }
/*      */       
/* 1412 */       return this.__random.nextInt(this.__activeMaxPort - this.__activeMinPort + 1) + this.__activeMinPort;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1417 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private InetAddress getHostAddress()
/*      */   {
/* 1429 */     if (this.__activeExternalHost != null)
/*      */     {
/* 1431 */       return this.__activeExternalHost;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1436 */     return getLocalAddress();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private InetAddress getReportHostAddress()
/*      */   {
/* 1449 */     if (this.__reportActiveExternalHost != null) {
/* 1450 */       return this.__reportActiveExternalHost;
/*      */     }
/* 1452 */     return getHostAddress();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setActivePortRange(int minPort, int maxPort)
/*      */   {
/* 1465 */     this.__activeMinPort = minPort;
/* 1466 */     this.__activeMaxPort = maxPort;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setActiveExternalIPAddress(String ipAddress)
/*      */     throws UnknownHostException
/*      */   {
/* 1479 */     this.__activeExternalHost = InetAddress.getByName(ipAddress);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPassiveLocalIPAddress(String ipAddress)
/*      */     throws UnknownHostException
/*      */   {
/* 1491 */     this.__passiveLocalHost = InetAddress.getByName(ipAddress);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPassiveLocalIPAddress(InetAddress inetAddress)
/*      */   {
/* 1502 */     this.__passiveLocalHost = inetAddress;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public InetAddress getPassiveLocalIPAddress()
/*      */   {
/* 1513 */     return this.__passiveLocalHost;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setReportActiveExternalIPAddress(String ipAddress)
/*      */     throws UnknownHostException
/*      */   {
/* 1527 */     this.__reportActiveExternalHost = InetAddress.getByName(ipAddress);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean setFileType(int fileType)
/*      */     throws IOException
/*      */   {
/* 1559 */     if (FTPReply.isPositiveCompletion(type(fileType)))
/*      */     {
/* 1561 */       this.__fileType = fileType;
/* 1562 */       this.__fileFormat = 4;
/* 1563 */       return true;
/*      */     }
/* 1565 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean setFileType(int fileType, int formatOrByteSize)
/*      */     throws IOException
/*      */   {
/* 1608 */     if (FTPReply.isPositiveCompletion(type(fileType, formatOrByteSize)))
/*      */     {
/* 1610 */       this.__fileType = fileType;
/* 1611 */       this.__fileFormat = formatOrByteSize;
/* 1612 */       return true;
/*      */     }
/* 1614 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean setFileStructure(int structure)
/*      */     throws IOException
/*      */   {
/* 1636 */     if (FTPReply.isPositiveCompletion(stru(structure)))
/*      */     {
/* 1638 */       this.__fileStructure = structure;
/* 1639 */       return true;
/*      */     }
/* 1641 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean setFileTransferMode(int mode)
/*      */     throws IOException
/*      */   {
/* 1663 */     if (FTPReply.isPositiveCompletion(mode(mode)))
/*      */     {
/* 1665 */       this.__fileTransferMode = mode;
/* 1666 */       return true;
/*      */     }
/* 1668 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean remoteRetrieve(String filename)
/*      */     throws IOException
/*      */   {
/* 1689 */     if ((this.__dataConnectionMode == 1) || (this.__dataConnectionMode == 3))
/*      */     {
/* 1691 */       return FTPReply.isPositivePreliminary(retr(filename));
/*      */     }
/* 1693 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean remoteStore(String filename)
/*      */     throws IOException
/*      */   {
/* 1716 */     if ((this.__dataConnectionMode == 1) || (this.__dataConnectionMode == 3))
/*      */     {
/* 1718 */       return FTPReply.isPositivePreliminary(stor(filename));
/*      */     }
/* 1720 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean remoteStoreUnique(String filename)
/*      */     throws IOException
/*      */   {
/* 1744 */     if ((this.__dataConnectionMode == 1) || (this.__dataConnectionMode == 3))
/*      */     {
/* 1746 */       return FTPReply.isPositivePreliminary(stou(filename));
/*      */     }
/* 1748 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean remoteStoreUnique()
/*      */     throws IOException
/*      */   {
/* 1772 */     if ((this.__dataConnectionMode == 1) || (this.__dataConnectionMode == 3))
/*      */     {
/* 1774 */       return FTPReply.isPositivePreliminary(stou());
/*      */     }
/* 1776 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean remoteAppend(String filename)
/*      */     throws IOException
/*      */   {
/* 1800 */     if ((this.__dataConnectionMode == 1) || (this.__dataConnectionMode == 3))
/*      */     {
/* 1802 */       return FTPReply.isPositivePreliminary(appe(filename));
/*      */     }
/* 1804 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean completePendingCommand()
/*      */     throws IOException
/*      */   {
/* 1853 */     return FTPReply.isPositiveCompletion(getReply());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean retrieveFile(String remote, OutputStream local)
/*      */     throws IOException
/*      */   {
/* 1885 */     return _retrieveFile(FTPCmd.RETR.getCommand(), remote, local);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _retrieveFile(String command, String remote, OutputStream local)
/*      */     throws IOException
/*      */   {
/* 1899 */     Socket socket = _openDataConnection_(command, remote);
/*      */     
/* 1901 */     if (socket == null) {
/* 1902 */       return false;
/*      */     }
/*      */     InputStream input;
/*      */     InputStream input;
/* 1906 */     if (this.__fileType == 0) {
/* 1907 */       input = new FromNetASCIIInputStream(getBufferedInputStream(socket.getInputStream()));
/*      */     } else {
/* 1909 */       input = getBufferedInputStream(socket.getInputStream());
/*      */     }
/*      */     
/* 1912 */     CSL csl = null;
/* 1913 */     if (this.__controlKeepAliveTimeout > 0L) {
/* 1914 */       csl = new CSL(this, this.__controlKeepAliveTimeout, this.__controlKeepAliveReplyTimeout);
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/* 1920 */       Util.copyStream(input, local, getBufferSize(), -1L, __mergeListeners(csl), false);
/*      */     }
/*      */     finally
/*      */     {
/* 1924 */       Util.closeQuietly(input);
/* 1925 */       Util.closeQuietly(socket);
/* 1926 */       if (csl != null) {
/* 1927 */         csl.cleanUp();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1932 */     boolean ok = completePendingCommand();
/* 1933 */     return ok;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public InputStream retrieveFileStream(String remote)
/*      */     throws IOException
/*      */   {
/* 1967 */     return _retrieveFileStream(FTPCmd.RETR.getCommand(), remote);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected InputStream _retrieveFileStream(String command, String remote)
/*      */     throws IOException
/*      */   {
/* 1980 */     Socket socket = _openDataConnection_(command, remote);
/*      */     
/* 1982 */     if (socket == null) {
/* 1983 */       return null;
/*      */     }
/*      */     InputStream input;
/*      */     InputStream input;
/* 1987 */     if (this.__fileType == 0)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1995 */       input = new FromNetASCIIInputStream(getBufferedInputStream(socket.getInputStream()));
/*      */     } else {
/* 1997 */       input = socket.getInputStream();
/*      */     }
/* 1999 */     return new SocketInputStream(socket, input);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean storeFile(String remote, InputStream local)
/*      */     throws IOException
/*      */   {
/* 2030 */     return __storeFile(FTPCmd.STOR, remote, local);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public OutputStream storeFileStream(String remote)
/*      */     throws IOException
/*      */   {
/* 2064 */     return __storeFileStream(FTPCmd.STOR, remote);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean appendFile(String remote, InputStream local)
/*      */     throws IOException
/*      */   {
/* 2095 */     return __storeFile(FTPCmd.APPE, remote, local);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public OutputStream appendFileStream(String remote)
/*      */     throws IOException
/*      */   {
/* 2128 */     return __storeFileStream(FTPCmd.APPE, remote);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean storeUniqueFile(String remote, InputStream local)
/*      */     throws IOException
/*      */   {
/* 2160 */     return __storeFile(FTPCmd.STOU, remote, local);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public OutputStream storeUniqueFileStream(String remote)
/*      */     throws IOException
/*      */   {
/* 2196 */     return __storeFileStream(FTPCmd.STOU, remote);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean storeUniqueFile(InputStream local)
/*      */     throws IOException
/*      */   {
/* 2225 */     return __storeFile(FTPCmd.STOU, null, local);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public OutputStream storeUniqueFileStream()
/*      */     throws IOException
/*      */   {
/* 2258 */     return __storeFileStream(FTPCmd.STOU, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean allocate(int bytes)
/*      */     throws IOException
/*      */   {
/* 2276 */     return FTPReply.isPositiveCompletion(allo(bytes));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean features()
/*      */     throws IOException
/*      */   {
/* 2297 */     return FTPReply.isPositiveCompletion(feat());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] featureValues(String feature)
/*      */     throws IOException
/*      */   {
/* 2312 */     if (!initFeatureMap()) {
/* 2313 */       return null;
/*      */     }
/* 2315 */     Set<String> entries = (Set)this.__featuresMap.get(feature.toUpperCase(Locale.ENGLISH));
/* 2316 */     if (entries != null) {
/* 2317 */       return (String[])entries.toArray(new String[entries.size()]);
/*      */     }
/* 2319 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String featureValue(String feature)
/*      */     throws IOException
/*      */   {
/* 2335 */     String[] values = featureValues(feature);
/* 2336 */     if (values != null) {
/* 2337 */       return values[0];
/*      */     }
/* 2339 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasFeature(String feature)
/*      */     throws IOException
/*      */   {
/* 2355 */     if (!initFeatureMap()) {
/* 2356 */       return false;
/*      */     }
/* 2358 */     return this.__featuresMap.containsKey(feature.toUpperCase(Locale.ENGLISH));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasFeature(String feature, String value)
/*      */     throws IOException
/*      */   {
/* 2377 */     if (!initFeatureMap()) {
/* 2378 */       return false;
/*      */     }
/* 2380 */     Set<String> entries = (Set)this.__featuresMap.get(feature.toUpperCase(Locale.ENGLISH));
/* 2381 */     if (entries != null) {
/* 2382 */       return entries.contains(value);
/*      */     }
/* 2384 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean initFeatureMap()
/*      */     throws IOException
/*      */   {
/* 2391 */     if (this.__featuresMap == null)
/*      */     {
/* 2393 */       int replyCode = feat();
/* 2394 */       if (replyCode == 530) {
/* 2395 */         return false;
/*      */       }
/* 2397 */       boolean success = FTPReply.isPositiveCompletion(replyCode);
/*      */       
/* 2399 */       this.__featuresMap = new HashMap();
/* 2400 */       if (!success) {
/* 2401 */         return false;
/*      */       }
/* 2403 */       for (String l : getReplyStrings()) {
/* 2404 */         if (l.startsWith(" "))
/*      */         {
/* 2406 */           String value = "";
/* 2407 */           int varsep = l.indexOf(' ', 1);
/* 2408 */           if (varsep > 0) {
/* 2409 */             String key = l.substring(1, varsep);
/* 2410 */             value = l.substring(varsep + 1);
/*      */           } else {
/* 2412 */             key = l.substring(1);
/*      */           }
/* 2414 */           String key = key.toUpperCase(Locale.ENGLISH);
/* 2415 */           Set<String> entries = (Set)this.__featuresMap.get(key);
/* 2416 */           if (entries == null) {
/* 2417 */             entries = new HashSet();
/* 2418 */             this.__featuresMap.put(key, entries);
/*      */           }
/* 2420 */           entries.add(value);
/*      */         }
/*      */       }
/*      */     }
/* 2424 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean allocate(int bytes, int recordSize)
/*      */     throws IOException
/*      */   {
/* 2443 */     return FTPReply.isPositiveCompletion(allo(bytes, recordSize));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean doCommand(String command, String params)
/*      */     throws IOException
/*      */   {
/* 2465 */     return FTPReply.isPositiveCompletion(sendCommand(command, params));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] doCommandAsStrings(String command, String params)
/*      */     throws IOException
/*      */   {
/* 2486 */     boolean success = FTPReply.isPositiveCompletion(sendCommand(command, params));
/* 2487 */     if (success) {
/* 2488 */       return getReplyStrings();
/*      */     }
/* 2490 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FTPFile mlistFile(String pathname)
/*      */     throws IOException
/*      */   {
/* 2504 */     boolean success = FTPReply.isPositiveCompletion(sendCommand(FTPCmd.MLST, pathname));
/* 2505 */     if (success) {
/* 2506 */       String reply = getReplyStrings()[1];
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2511 */       if ((reply.length() < 3) || (reply.charAt(0) != ' ')) {
/* 2512 */         throw new MalformedServerReplyException("Invalid server reply (MLST): '" + reply + "'");
/*      */       }
/* 2514 */       String entry = reply.substring(1);
/* 2515 */       return MLSxEntryParser.parseEntry(entry);
/*      */     }
/* 2517 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FTPFile[] mlistDir()
/*      */     throws IOException
/*      */   {
/* 2530 */     return mlistDir(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FTPFile[] mlistDir(String pathname)
/*      */     throws IOException
/*      */   {
/* 2543 */     FTPListParseEngine engine = initiateMListParsing(pathname);
/* 2544 */     return engine.getFiles();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FTPFile[] mlistDir(String pathname, FTPFileFilter filter)
/*      */     throws IOException
/*      */   {
/* 2558 */     FTPListParseEngine engine = initiateMListParsing(pathname);
/* 2559 */     return engine.getFiles(filter);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean restart(long offset)
/*      */     throws IOException
/*      */   {
/* 2583 */     this.__restartOffset = 0L;
/* 2584 */     return FTPReply.isPositiveIntermediate(rest(Long.toString(offset)));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRestartOffset(long offset)
/*      */   {
/* 2605 */     if (offset >= 0L) {
/* 2606 */       this.__restartOffset = offset;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getRestartOffset()
/*      */   {
/* 2618 */     return this.__restartOffset;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean rename(String from, String to)
/*      */     throws IOException
/*      */   {
/* 2639 */     if (!FTPReply.isPositiveIntermediate(rnfr(from))) {
/* 2640 */       return false;
/*      */     }
/*      */     
/* 2643 */     return FTPReply.isPositiveCompletion(rnto(to));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean abort()
/*      */     throws IOException
/*      */   {
/* 2661 */     return FTPReply.isPositiveCompletion(abor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean deleteFile(String pathname)
/*      */     throws IOException
/*      */   {
/* 2679 */     return FTPReply.isPositiveCompletion(dele(pathname));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean removeDirectory(String pathname)
/*      */     throws IOException
/*      */   {
/* 2698 */     return FTPReply.isPositiveCompletion(rmd(pathname));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean makeDirectory(String pathname)
/*      */     throws IOException
/*      */   {
/* 2719 */     return FTPReply.isPositiveCompletion(mkd(pathname));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String printWorkingDirectory()
/*      */     throws IOException
/*      */   {
/* 2738 */     if (pwd() != 257) {
/* 2739 */       return null;
/*      */     }
/*      */     
/* 2742 */     return __parsePathname((String)this._replyLines.get(this._replyLines.size() - 1));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean sendSiteCommand(String arguments)
/*      */     throws IOException
/*      */   {
/* 2760 */     return FTPReply.isPositiveCompletion(site(arguments));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSystemType()
/*      */     throws IOException
/*      */   {
/* 2791 */     if (this.__systemName == null) {
/* 2792 */       if (FTPReply.isPositiveCompletion(syst()))
/*      */       {
/* 2794 */         this.__systemName = ((String)this._replyLines.get(this._replyLines.size() - 1)).substring(4);
/*      */       }
/*      */       else {
/* 2797 */         String systDefault = System.getProperty("org.apache.commons.net.ftp.systemType.default");
/* 2798 */         if (systDefault != null) {
/* 2799 */           this.__systemName = systDefault;
/*      */         } else {
/* 2801 */           throw new IOException("Unable to determine system type - response: " + getReplyString());
/*      */         }
/*      */       }
/*      */     }
/* 2805 */     return this.__systemName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String listHelp()
/*      */     throws IOException
/*      */   {
/* 2825 */     if (FTPReply.isPositiveCompletion(help())) {
/* 2826 */       return getReplyString();
/*      */     }
/* 2828 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String listHelp(String command)
/*      */     throws IOException
/*      */   {
/* 2848 */     if (FTPReply.isPositiveCompletion(help(command))) {
/* 2849 */       return getReplyString();
/*      */     }
/* 2851 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean sendNoOp()
/*      */     throws IOException
/*      */   {
/* 2870 */     return FTPReply.isPositiveCompletion(noop());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] listNames(String pathname)
/*      */     throws IOException
/*      */   {
/* 2905 */     Socket socket = _openDataConnection_(FTPCmd.NLST, getListArguments(pathname));
/*      */     
/* 2907 */     if (socket == null) {
/* 2908 */       return null;
/*      */     }
/*      */     
/* 2911 */     BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), getControlEncoding()));
/*      */     
/*      */ 
/* 2914 */     ArrayList<String> results = new ArrayList();
/*      */     String line;
/* 2916 */     while ((line = reader.readLine()) != null) {
/* 2917 */       results.add(line);
/*      */     }
/*      */     
/* 2920 */     reader.close();
/* 2921 */     socket.close();
/*      */     
/* 2923 */     if (completePendingCommand())
/*      */     {
/* 2925 */       String[] names = new String[results.size()];
/* 2926 */       return (String[])results.toArray(names);
/*      */     }
/*      */     
/* 2929 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] listNames()
/*      */     throws IOException
/*      */   {
/* 2956 */     return listNames(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FTPFile[] listFiles(String pathname)
/*      */     throws IOException
/*      */   {
/* 3016 */     FTPListParseEngine engine = initiateListParsing((String)null, pathname);
/* 3017 */     return engine.getFiles();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FTPFile[] listFiles()
/*      */     throws IOException
/*      */   {
/* 3069 */     return listFiles((String)null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FTPFile[] listFiles(String pathname, FTPFileFilter filter)
/*      */     throws IOException
/*      */   {
/* 3084 */     FTPListParseEngine engine = initiateListParsing((String)null, pathname);
/* 3085 */     return engine.getFiles(filter);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FTPFile[] listDirectories()
/*      */     throws IOException
/*      */   {
/* 3133 */     return listDirectories((String)null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FTPFile[] listDirectories(String parent)
/*      */     throws IOException
/*      */   {
/* 3181 */     return listFiles(parent, FTPFileFilters.DIRECTORIES);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FTPListParseEngine initiateListParsing()
/*      */     throws IOException
/*      */   {
/* 3220 */     return initiateListParsing((String)null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FTPListParseEngine initiateListParsing(String pathname)
/*      */     throws IOException
/*      */   {
/* 3276 */     return initiateListParsing((String)null, pathname);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FTPListParseEngine initiateListParsing(String parserKey, String pathname)
/*      */     throws IOException
/*      */   {
/* 3338 */     __createParser(parserKey);
/* 3339 */     return initiateListParsing(this.__entryParser, pathname);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   void __createParser(String parserKey)
/*      */     throws IOException
/*      */   {
/* 3347 */     if ((this.__entryParser == null) || ((parserKey != null) && (!this.__entryParserKey.equals(parserKey)))) {
/* 3348 */       if (null != parserKey)
/*      */       {
/*      */ 
/* 3351 */         this.__entryParser = this.__parserFactory.createFileEntryParser(parserKey);
/*      */         
/* 3353 */         this.__entryParserKey = parserKey;
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/* 3358 */       else if ((null != this.__configuration) && (this.__configuration.getServerSystemKey().length() > 0)) {
/* 3359 */         this.__entryParser = this.__parserFactory.createFileEntryParser(this.__configuration);
/*      */         
/* 3361 */         this.__entryParserKey = this.__configuration.getServerSystemKey();
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/* 3367 */         String systemType = System.getProperty("org.apache.commons.net.ftp.systemType");
/* 3368 */         if (systemType == null) {
/* 3369 */           systemType = getSystemType();
/* 3370 */           Properties override = getOverrideProperties();
/* 3371 */           if (override != null) {
/* 3372 */             String newType = override.getProperty(systemType);
/* 3373 */             if (newType != null) {
/* 3374 */               systemType = newType;
/*      */             }
/*      */           }
/*      */         }
/* 3378 */         if (null != this.__configuration) {
/* 3379 */           this.__entryParser = this.__parserFactory.createFileEntryParser(new FTPClientConfig(systemType, this.__configuration));
/*      */         } else {
/* 3381 */           this.__entryParser = this.__parserFactory.createFileEntryParser(systemType);
/*      */         }
/* 3383 */         this.__entryParserKey = systemType;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private FTPListParseEngine initiateListParsing(FTPFileEntryParser parser, String pathname)
/*      */     throws IOException
/*      */   {
/* 3409 */     Socket socket = _openDataConnection_(FTPCmd.LIST, getListArguments(pathname));
/*      */     
/* 3411 */     FTPListParseEngine engine = new FTPListParseEngine(parser, this.__configuration);
/* 3412 */     if (socket == null)
/*      */     {
/* 3414 */       return engine;
/*      */     }
/*      */     try
/*      */     {
/* 3418 */       engine.readServerList(socket.getInputStream(), getControlEncoding());
/*      */     }
/*      */     finally {
/* 3421 */       Util.closeQuietly(socket);
/*      */     }
/*      */     
/* 3424 */     completePendingCommand();
/* 3425 */     return engine;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private FTPListParseEngine initiateMListParsing(String pathname)
/*      */     throws IOException
/*      */   {
/* 3437 */     Socket socket = _openDataConnection_(FTPCmd.MLSD, pathname);
/* 3438 */     FTPListParseEngine engine = new FTPListParseEngine(MLSxEntryParser.getInstance(), this.__configuration);
/* 3439 */     if (socket == null)
/*      */     {
/* 3441 */       return engine;
/*      */     }
/*      */     try
/*      */     {
/* 3445 */       engine.readServerList(socket.getInputStream(), getControlEncoding());
/*      */     }
/*      */     finally {
/* 3448 */       Util.closeQuietly(socket);
/* 3449 */       completePendingCommand();
/*      */     }
/* 3451 */     return engine;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getListArguments(String pathname)
/*      */   {
/* 3460 */     if (getListHiddenFiles())
/*      */     {
/* 3462 */       if (pathname != null)
/*      */       {
/* 3464 */         StringBuilder sb = new StringBuilder(pathname.length() + 3);
/* 3465 */         sb.append("-a ");
/* 3466 */         sb.append(pathname);
/* 3467 */         return sb.toString();
/*      */       }
/*      */       
/*      */ 
/* 3471 */       return "-a";
/*      */     }
/*      */     
/*      */ 
/* 3475 */     return pathname;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getStatus()
/*      */     throws IOException
/*      */   {
/* 3493 */     if (FTPReply.isPositiveCompletion(stat())) {
/* 3494 */       return getReplyString();
/*      */     }
/* 3496 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getStatus(String pathname)
/*      */     throws IOException
/*      */   {
/* 3516 */     if (FTPReply.isPositiveCompletion(stat(pathname))) {
/* 3517 */       return getReplyString();
/*      */     }
/* 3519 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getModificationTime(String pathname)
/*      */     throws IOException
/*      */   {
/* 3535 */     if (FTPReply.isPositiveCompletion(mdtm(pathname))) {
/* 3536 */       return getReplyStrings()[0].substring(4);
/*      */     }
/* 3538 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FTPFile mdtmFile(String pathname)
/*      */     throws IOException
/*      */   {
/* 3555 */     if (FTPReply.isPositiveCompletion(mdtm(pathname))) {
/* 3556 */       String reply = getReplyStrings()[0].substring(4);
/* 3557 */       FTPFile file = new FTPFile();
/* 3558 */       file.setName(pathname);
/* 3559 */       file.setRawListing(reply);
/* 3560 */       file.setTimestamp(MLSxEntryParser.parseGMTdateTime(reply));
/* 3561 */       return file;
/*      */     }
/* 3563 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean setModificationTime(String pathname, String timeval)
/*      */     throws IOException
/*      */   {
/* 3585 */     return FTPReply.isPositiveCompletion(mfmt(pathname, timeval));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBufferSize(int bufSize)
/*      */   {
/* 3595 */     this.__bufferSize = bufSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getBufferSize()
/*      */   {
/* 3603 */     return this.__bufferSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSendDataSocketBufferSize(int bufSize)
/*      */   {
/* 3614 */     this.__sendDataSocketBufferSize = bufSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getSendDataSocketBufferSize()
/*      */   {
/* 3623 */     return this.__sendDataSocketBufferSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setReceieveDataSocketBufferSize(int bufSize)
/*      */   {
/* 3634 */     this.__receiveDataSocketBufferSize = bufSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getReceiveDataSocketBufferSize()
/*      */   {
/* 3643 */     return this.__receiveDataSocketBufferSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void configure(FTPClientConfig config)
/*      */   {
/* 3656 */     this.__configuration = config;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setListHiddenFiles(boolean listHiddenFiles)
/*      */   {
/* 3669 */     this.__listHiddenFiles = listHiddenFiles;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getListHiddenFiles()
/*      */   {
/* 3678 */     return this.__listHiddenFiles;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isUseEPSVwithIPv4()
/*      */   {
/* 3688 */     return this.__useEPSVwithIPv4;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUseEPSVwithIPv4(boolean selected)
/*      */   {
/* 3707 */     this.__useEPSVwithIPv4 = selected;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCopyStreamListener(CopyStreamListener listener)
/*      */   {
/* 3718 */     this.__copyStreamListener = listener;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CopyStreamListener getCopyStreamListener()
/*      */   {
/* 3728 */     return this.__copyStreamListener;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setControlKeepAliveTimeout(long controlIdle)
/*      */   {
/* 3740 */     this.__controlKeepAliveTimeout = (controlIdle * 1000L);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getControlKeepAliveTimeout()
/*      */   {
/* 3749 */     return this.__controlKeepAliveTimeout / 1000L;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setControlKeepAliveReplyTimeout(int timeout)
/*      */   {
/* 3760 */     this.__controlKeepAliveReplyTimeout = timeout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getControlKeepAliveReplyTimeout()
/*      */   {
/* 3769 */     return this.__controlKeepAliveReplyTimeout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public void setPassiveNatWorkaround(boolean enabled)
/*      */   {
/* 3787 */     if (enabled) {
/* 3788 */       this.__passiveNatWorkaroundStrategy = new NatServerResolverImpl(this);
/*      */     } else {
/* 3790 */       this.__passiveNatWorkaroundStrategy = null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPassiveNatWorkaroundStrategy(HostnameResolver resolver)
/*      */   {
/* 3805 */     this.__passiveNatWorkaroundStrategy = resolver;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static abstract interface HostnameResolver
/*      */   {
/*      */     public abstract String resolve(String paramString)
/*      */       throws UnknownHostException;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static class NatServerResolverImpl
/*      */     implements FTPClient.HostnameResolver
/*      */   {
/*      */     private FTPClient client;
/*      */     
/*      */ 
/*      */ 
/*      */     public NatServerResolverImpl(FTPClient client)
/*      */     {
/* 3827 */       this.client = client;
/*      */     }
/*      */     
/*      */     public String resolve(String hostname) throws UnknownHostException
/*      */     {
/* 3832 */       String newHostname = hostname;
/* 3833 */       InetAddress host = InetAddress.getByName(newHostname);
/*      */       
/* 3835 */       if (host.isSiteLocalAddress()) {
/* 3836 */         InetAddress remote = this.client.getRemoteAddress();
/* 3837 */         if (!remote.isSiteLocalAddress()) {
/* 3838 */           newHostname = remote.getHostAddress();
/*      */         }
/*      */       }
/* 3841 */       return newHostname;
/*      */     }
/*      */   }
/*      */   
/*      */   private OutputStream getBufferedOutputStream(OutputStream outputStream) {
/* 3846 */     if (this.__bufferSize > 0) {
/* 3847 */       return new BufferedOutputStream(outputStream, this.__bufferSize);
/*      */     }
/* 3849 */     return new BufferedOutputStream(outputStream);
/*      */   }
/*      */   
/*      */   private InputStream getBufferedInputStream(InputStream inputStream) {
/* 3853 */     if (this.__bufferSize > 0) {
/* 3854 */       return new BufferedInputStream(inputStream, this.__bufferSize);
/*      */     }
/* 3856 */     return new BufferedInputStream(inputStream);
/*      */   }
/*      */   
/*      */ 
/*      */   private static class CSL
/*      */     implements CopyStreamListener
/*      */   {
/*      */     private final FTPClient parent;
/*      */     private final long idle;
/*      */     private final int currentSoTimeout;
/* 3866 */     private long time = System.currentTimeMillis();
/*      */     private int notAcked;
/*      */     
/*      */     CSL(FTPClient parent, long idleTime, int maxWait) throws SocketException {
/* 3870 */       this.idle = idleTime;
/* 3871 */       this.parent = parent;
/* 3872 */       this.currentSoTimeout = parent.getSoTimeout();
/* 3873 */       parent.setSoTimeout(maxWait);
/*      */     }
/*      */     
/*      */     public void bytesTransferred(CopyStreamEvent event)
/*      */     {
/* 3878 */       bytesTransferred(event.getTotalBytesTransferred(), event.getBytesTransferred(), event.getStreamSize());
/*      */     }
/*      */     
/*      */ 
/*      */     public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize)
/*      */     {
/* 3884 */       long now = System.currentTimeMillis();
/* 3885 */       if (now - this.time > this.idle) {
/*      */         try {
/* 3887 */           this.parent.__noop();
/*      */         } catch (SocketTimeoutException e) {
/* 3889 */           this.notAcked += 1;
/*      */         }
/*      */         catch (IOException e) {}
/*      */         
/* 3893 */         this.time = now;
/*      */       }
/*      */     }
/*      */     
/*      */     void cleanUp() throws IOException {
/*      */       try {
/* 3899 */         while (this.notAcked-- > 0) {
/* 3900 */           this.parent.__getReplyNoReport();
/*      */         }
/*      */       } finally {
/* 3903 */         this.parent.setSoTimeout(this.currentSoTimeout);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private CopyStreamListener __mergeListeners(CopyStreamListener local)
/*      */   {
/* 3917 */     if (local == null) {
/* 3918 */       return this.__copyStreamListener;
/*      */     }
/* 3920 */     if (this.__copyStreamListener == null) {
/* 3921 */       return local;
/*      */     }
/*      */     
/* 3924 */     CopyStreamAdapter merged = new CopyStreamAdapter();
/* 3925 */     merged.addCopyStreamListener(local);
/* 3926 */     merged.addCopyStreamListener(this.__copyStreamListener);
/* 3927 */     return merged;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAutodetectUTF8(boolean autodetect)
/*      */   {
/* 3939 */     this.__autodetectEncoding = autodetect;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getAutodetectUTF8()
/*      */   {
/* 3948 */     return this.__autodetectEncoding;
/*      */   }
/*      */   
/*      */   FTPFileEntryParser getEntryParser()
/*      */   {
/* 3953 */     return this.__entryParser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public String getSystemName()
/*      */     throws IOException
/*      */   {
/* 3966 */     if ((this.__systemName == null) && (FTPReply.isPositiveCompletion(syst()))) {
/* 3967 */       this.__systemName = ((String)this._replyLines.get(this._replyLines.size() - 1)).substring(4);
/*      */     }
/* 3969 */     return this.__systemName;
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\ftp\FTPClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */