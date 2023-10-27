/*      */ package org.apache.commons.net.ftp;
/*      */ 
/*      */ import java.io.BufferedReader;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.Reader;
/*      */ import java.net.Inet4Address;
/*      */ import java.net.Inet6Address;
/*      */ import java.net.InetAddress;
/*      */ import java.net.Socket;
/*      */ import java.net.SocketException;
/*      */ import java.net.SocketTimeoutException;
/*      */ import java.util.ArrayList;
/*      */ import org.apache.commons.net.MalformedServerReplyException;
/*      */ import org.apache.commons.net.ProtocolCommandSupport;
/*      */ import org.apache.commons.net.SocketClient;
/*      */ import org.apache.commons.net.io.CRLFLineReader;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class FTP
/*      */   extends SocketClient
/*      */ {
/*      */   public static final int DEFAULT_DATA_PORT = 20;
/*      */   public static final int DEFAULT_PORT = 21;
/*      */   public static final int ASCII_FILE_TYPE = 0;
/*      */   public static final int EBCDIC_FILE_TYPE = 1;
/*      */   public static final int BINARY_FILE_TYPE = 2;
/*      */   public static final int LOCAL_FILE_TYPE = 3;
/*      */   public static final int NON_PRINT_TEXT_FORMAT = 4;
/*      */   public static final int TELNET_TEXT_FORMAT = 5;
/*      */   public static final int CARRIAGE_CONTROL_TEXT_FORMAT = 6;
/*      */   public static final int FILE_STRUCTURE = 7;
/*      */   public static final int RECORD_STRUCTURE = 8;
/*      */   public static final int PAGE_STRUCTURE = 9;
/*      */   public static final int STREAM_TRANSFER_MODE = 10;
/*      */   public static final int BLOCK_TRANSFER_MODE = 11;
/*      */   public static final int COMPRESSED_TRANSFER_MODE = 12;
/*      */   public static final String DEFAULT_CONTROL_ENCODING = "ISO-8859-1";
/*      */   public static final int REPLY_CODE_LEN = 3;
/*      */   private static final String __modes = "AEILNTCFRPSBC";
/*      */   protected int _replyCode;
/*      */   protected ArrayList<String> _replyLines;
/*      */   protected boolean _newReplyString;
/*      */   protected String _replyString;
/*      */   protected String _controlEncoding;
/*      */   protected ProtocolCommandSupport _commandSupport_;
/*  236 */   protected boolean strictMultilineParsing = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  244 */   private boolean strictReplyParsing = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected BufferedReader _controlInput_;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected BufferedWriter _controlOutput_;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FTP()
/*      */   {
/*  272 */     setDefaultPort(21);
/*  273 */     this._replyLines = new ArrayList();
/*  274 */     this._newReplyString = false;
/*  275 */     this._replyString = null;
/*  276 */     this._controlEncoding = "ISO-8859-1";
/*  277 */     this._commandSupport_ = new ProtocolCommandSupport(this);
/*      */   }
/*      */   
/*      */   private boolean __strictCheck(String line, String code)
/*      */   {
/*  282 */     return (!line.startsWith(code)) || (line.charAt(3) != ' ');
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean __lenientCheck(String line)
/*      */   {
/*  291 */     return (line.length() <= 3) || (line.charAt(3) == '-') || (!Character.isDigit(line.charAt(0)));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void __getReply()
/*      */     throws IOException
/*      */   {
/*  300 */     __getReply(true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void __getReplyNoReport()
/*      */     throws IOException
/*      */   {
/*  311 */     __getReply(false);
/*      */   }
/*      */   
/*      */ 
/*      */   private void __getReply(boolean reportReply)
/*      */     throws IOException
/*      */   {
/*  318 */     this._newReplyString = true;
/*  319 */     this._replyLines.clear();
/*      */     
/*  321 */     String line = this._controlInput_.readLine();
/*      */     
/*  323 */     if (line == null) {
/*  324 */       throw new FTPConnectionClosedException("Connection closed without indication.");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  330 */     int length = line.length();
/*  331 */     if (length < 3) {
/*  332 */       throw new MalformedServerReplyException("Truncated server reply: " + line);
/*      */     }
/*      */     
/*      */ 
/*  336 */     String code = null;
/*      */     try
/*      */     {
/*  339 */       code = line.substring(0, 3);
/*  340 */       this._replyCode = Integer.parseInt(code);
/*      */     }
/*      */     catch (NumberFormatException e)
/*      */     {
/*  344 */       throw new MalformedServerReplyException("Could not parse response code.\nServer Reply: " + line);
/*      */     }
/*      */     
/*      */ 
/*  348 */     this._replyLines.add(line);
/*      */     
/*      */ 
/*  351 */     if (length > 3) {
/*  352 */       char sep = line.charAt(3);
/*      */       
/*  354 */       if (sep == '-') {
/*      */         do {
/*      */           for (;;) {
/*  357 */             line = this._controlInput_.readLine();
/*      */             
/*  359 */             if (line == null) {
/*  360 */               throw new FTPConnectionClosedException("Connection closed without indication.");
/*      */             }
/*      */             
/*      */ 
/*  364 */             this._replyLines.add(line);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  370 */             if (!isStrictMultilineParsing()) break; if (!__strictCheck(line, code)) break label296; } } while (__lenientCheck(line));
/*      */       }
/*  372 */       else if (isStrictReplyParsing()) {
/*  373 */         if (length == 4)
/*  374 */           throw new MalformedServerReplyException("Truncated server reply: '" + line + "'");
/*  375 */         if (sep != ' ')
/*  376 */           throw new MalformedServerReplyException("Invalid server reply: '" + line + "'");
/*      */       }
/*      */     } else { label296:
/*  379 */       if (isStrictReplyParsing()) {
/*  380 */         throw new MalformedServerReplyException("Truncated server reply: '" + line + "'");
/*      */       }
/*      */     }
/*  383 */     if (reportReply) {
/*  384 */       fireReplyReceived(this._replyCode, getReplyString());
/*      */     }
/*      */     
/*  387 */     if (this._replyCode == 421) {
/*  388 */       throw new FTPConnectionClosedException("FTP response 421 received.  Server closed connection.");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _connectAction_()
/*      */     throws IOException
/*      */   {
/*  399 */     _connectAction_(null);
/*      */   }
/*      */   
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
/*  412 */     super._connectAction_();
/*  413 */     if (socketIsReader == null) {
/*  414 */       this._controlInput_ = new CRLFLineReader(new InputStreamReader(this._input_, getControlEncoding()));
/*      */     }
/*      */     else {
/*  417 */       this._controlInput_ = new CRLFLineReader(socketIsReader);
/*      */     }
/*  419 */     this._controlOutput_ = new BufferedWriter(new OutputStreamWriter(this._output_, getControlEncoding()));
/*      */     
/*  421 */     if (this.connectTimeout > 0) {
/*  422 */       int original = this._socket_.getSoTimeout();
/*  423 */       this._socket_.setSoTimeout(this.connectTimeout);
/*      */       try {
/*  425 */         __getReply();
/*      */         
/*  427 */         if (FTPReply.isPositivePreliminary(this._replyCode)) {
/*  428 */           __getReply();
/*      */         }
/*      */       } catch (SocketTimeoutException e) {
/*  431 */         IOException ioe = new IOException("Timed out waiting for initial connect reply");
/*  432 */         ioe.initCause(e);
/*  433 */         throw ioe;
/*      */       } finally {
/*  435 */         this._socket_.setSoTimeout(original);
/*      */       }
/*      */     } else {
/*  438 */       __getReply();
/*      */       
/*  440 */       if (FTPReply.isPositivePreliminary(this._replyCode)) {
/*  441 */         __getReply();
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
/*      */   public void setControlEncoding(String encoding)
/*      */   {
/*  458 */     this._controlEncoding = encoding;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getControlEncoding()
/*      */   {
/*  467 */     return this._controlEncoding;
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
/*      */   public void disconnect()
/*      */     throws IOException
/*      */   {
/*  483 */     super.disconnect();
/*  484 */     this._controlInput_ = null;
/*  485 */     this._controlOutput_ = null;
/*  486 */     this._newReplyString = false;
/*  487 */     this._replyString = null;
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
/*      */   public int sendCommand(String command, String args)
/*      */     throws IOException
/*      */   {
/*  513 */     if (this._controlOutput_ == null) {
/*  514 */       throw new IOException("Connection is not open");
/*      */     }
/*      */     
/*  517 */     String message = __buildMessage(command, args);
/*      */     
/*  519 */     __send(message);
/*      */     
/*  521 */     fireCommandSent(command, message);
/*      */     
/*  523 */     __getReply();
/*  524 */     return this._replyCode;
/*      */   }
/*      */   
/*      */   private String __buildMessage(String command, String args) {
/*  528 */     StringBuilder __commandBuffer = new StringBuilder();
/*      */     
/*  530 */     __commandBuffer.append(command);
/*      */     
/*  532 */     if (args != null)
/*      */     {
/*  534 */       __commandBuffer.append(' ');
/*  535 */       __commandBuffer.append(args);
/*      */     }
/*  537 */     __commandBuffer.append("\r\n");
/*  538 */     return __commandBuffer.toString();
/*      */   }
/*      */   
/*      */   private void __send(String message) throws IOException, FTPConnectionClosedException, SocketException
/*      */   {
/*      */     try {
/*  544 */       this._controlOutput_.write(message);
/*  545 */       this._controlOutput_.flush();
/*      */     }
/*      */     catch (SocketException e)
/*      */     {
/*  549 */       if (!isConnected())
/*      */       {
/*  551 */         throw new FTPConnectionClosedException("Connection unexpectedly closed.");
/*      */       }
/*      */       
/*      */ 
/*  555 */       throw e;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void __noop()
/*      */     throws IOException
/*      */   {
/*  568 */     String msg = __buildMessage(FTPCmd.NOOP.getCommand(), null);
/*  569 */     __send(msg);
/*  570 */     __getReplyNoReport();
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
/*      */   @Deprecated
/*      */   public int sendCommand(int command, String args)
/*      */     throws IOException
/*      */   {
/*  598 */     return sendCommand(FTPCommand.getCommand(command), args);
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
/*      */   public int sendCommand(FTPCmd command)
/*      */     throws IOException
/*      */   {
/*  622 */     return sendCommand(command, null);
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
/*      */   public int sendCommand(FTPCmd command, String args)
/*      */     throws IOException
/*      */   {
/*  648 */     return sendCommand(command.getCommand(), args);
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
/*      */   public int sendCommand(String command)
/*      */     throws IOException
/*      */   {
/*  671 */     return sendCommand(command, null);
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
/*      */   public int sendCommand(int command)
/*      */     throws IOException
/*      */   {
/*  696 */     return sendCommand(command, null);
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
/*      */   public int getReplyCode()
/*      */   {
/*  710 */     return this._replyCode;
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
/*      */   public int getReply()
/*      */     throws IOException
/*      */   {
/*  732 */     __getReply();
/*  733 */     return this._replyCode;
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
/*      */   public String[] getReplyStrings()
/*      */   {
/*  746 */     return (String[])this._replyLines.toArray(new String[this._replyLines.size()]);
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
/*      */   public String getReplyString()
/*      */   {
/*  760 */     if (!this._newReplyString) {
/*  761 */       return this._replyString;
/*      */     }
/*      */     
/*  764 */     StringBuilder buffer = new StringBuilder(256);
/*      */     
/*  766 */     for (String line : this._replyLines) {
/*  767 */       buffer.append(line);
/*  768 */       buffer.append("\r\n");
/*      */     }
/*      */     
/*  771 */     this._newReplyString = false;
/*      */     
/*  773 */     return this._replyString = buffer.toString();
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
/*      */   public int user(String username)
/*      */     throws IOException
/*      */   {
/*  793 */     return sendCommand(FTPCmd.USER, username);
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
/*      */   public int pass(String password)
/*      */     throws IOException
/*      */   {
/*  811 */     return sendCommand(FTPCmd.PASS, password);
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
/*      */   public int acct(String account)
/*      */     throws IOException
/*      */   {
/*  830 */     return sendCommand(FTPCmd.ACCT, account);
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
/*      */   public int abor()
/*      */     throws IOException
/*      */   {
/*  849 */     return sendCommand(FTPCmd.ABOR);
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
/*      */   public int cwd(String directory)
/*      */     throws IOException
/*      */   {
/*  868 */     return sendCommand(FTPCmd.CWD, directory);
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
/*      */   public int cdup()
/*      */     throws IOException
/*      */   {
/*  886 */     return sendCommand(FTPCmd.CDUP);
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
/*      */   public int quit()
/*      */     throws IOException
/*      */   {
/*  904 */     return sendCommand(FTPCmd.QUIT);
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
/*      */   public int rein()
/*      */     throws IOException
/*      */   {
/*  922 */     return sendCommand(FTPCmd.REIN);
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
/*      */   public int smnt(String dir)
/*      */     throws IOException
/*      */   {
/*  941 */     return sendCommand(FTPCmd.SMNT, dir);
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
/*      */   public int port(InetAddress host, int port)
/*      */     throws IOException
/*      */   {
/*  962 */     StringBuilder info = new StringBuilder(24);
/*      */     
/*  964 */     info.append(host.getHostAddress().replace('.', ','));
/*  965 */     int num = port >>> 8;
/*  966 */     info.append(',');
/*  967 */     info.append(num);
/*  968 */     info.append(',');
/*  969 */     num = port & 0xFF;
/*  970 */     info.append(num);
/*      */     
/*  972 */     return sendCommand(FTPCmd.PORT, info.toString());
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
/*      */   public int eprt(InetAddress host, int port)
/*      */     throws IOException
/*      */   {
/* 1002 */     StringBuilder info = new StringBuilder();
/*      */     
/*      */ 
/*      */ 
/* 1006 */     String h = host.getHostAddress();
/* 1007 */     int num = h.indexOf("%");
/* 1008 */     if (num > 0) {
/* 1009 */       h = h.substring(0, num);
/*      */     }
/*      */     
/* 1012 */     info.append("|");
/*      */     
/* 1014 */     if ((host instanceof Inet4Address)) {
/* 1015 */       info.append("1");
/* 1016 */     } else if ((host instanceof Inet6Address)) {
/* 1017 */       info.append("2");
/*      */     }
/* 1019 */     info.append("|");
/* 1020 */     info.append(h);
/* 1021 */     info.append("|");
/* 1022 */     info.append(port);
/* 1023 */     info.append("|");
/*      */     
/* 1025 */     return sendCommand(FTPCmd.EPRT, info.toString());
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
/*      */   public int pasv()
/*      */     throws IOException
/*      */   {
/* 1045 */     return sendCommand(FTPCmd.PASV);
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
/*      */   public int epsv()
/*      */     throws IOException
/*      */   {
/* 1066 */     return sendCommand(FTPCmd.EPSV);
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
/*      */   public int type(int fileType, int formatOrByteSize)
/*      */     throws IOException
/*      */   {
/* 1088 */     StringBuilder arg = new StringBuilder();
/*      */     
/* 1090 */     arg.append("AEILNTCFRPSBC".charAt(fileType));
/* 1091 */     arg.append(' ');
/* 1092 */     if (fileType == 3) {
/* 1093 */       arg.append(formatOrByteSize);
/*      */     } else {
/* 1095 */       arg.append("AEILNTCFRPSBC".charAt(formatOrByteSize));
/*      */     }
/*      */     
/* 1098 */     return sendCommand(FTPCmd.TYPE, arg.toString());
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
/*      */   public int type(int fileType)
/*      */     throws IOException
/*      */   {
/* 1119 */     return sendCommand(FTPCmd.TYPE, "AEILNTCFRPSBC".substring(fileType, fileType + 1));
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
/*      */   public int stru(int structure)
/*      */     throws IOException
/*      */   {
/* 1140 */     return sendCommand(FTPCmd.STRU, "AEILNTCFRPSBC".substring(structure, structure + 1));
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
/*      */   public int mode(int mode)
/*      */     throws IOException
/*      */   {
/* 1161 */     return sendCommand(FTPCmd.MODE, "AEILNTCFRPSBC".substring(mode, mode + 1));
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
/*      */   public int retr(String pathname)
/*      */     throws IOException
/*      */   {
/* 1184 */     return sendCommand(FTPCmd.RETR, pathname);
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
/*      */   public int stor(String pathname)
/*      */     throws IOException
/*      */   {
/* 1207 */     return sendCommand(FTPCmd.STOR, pathname);
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
/*      */   public int stou()
/*      */     throws IOException
/*      */   {
/* 1228 */     return sendCommand(FTPCmd.STOU);
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
/*      */   public int stou(String pathname)
/*      */     throws IOException
/*      */   {
/* 1251 */     return sendCommand(FTPCmd.STOU, pathname);
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
/*      */   public int appe(String pathname)
/*      */     throws IOException
/*      */   {
/* 1274 */     return sendCommand(FTPCmd.APPE, pathname);
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
/*      */   public int allo(int bytes)
/*      */     throws IOException
/*      */   {
/* 1293 */     return sendCommand(FTPCmd.ALLO, Integer.toString(bytes));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int feat()
/*      */     throws IOException
/*      */   {
/* 1306 */     return sendCommand(FTPCmd.FEAT);
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
/*      */   public int allo(int bytes, int recordSize)
/*      */     throws IOException
/*      */   {
/* 1326 */     return sendCommand(FTPCmd.ALLO, Integer.toString(bytes) + " R " + Integer.toString(recordSize));
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
/*      */   public int rest(String marker)
/*      */     throws IOException
/*      */   {
/* 1346 */     return sendCommand(FTPCmd.REST, marker);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int mdtm(String file)
/*      */     throws IOException
/*      */   {
/* 1358 */     return sendCommand(FTPCmd.MDTM, file);
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
/*      */   public int mfmt(String pathname, String timeval)
/*      */     throws IOException
/*      */   {
/* 1381 */     return sendCommand(FTPCmd.MFMT, timeval + " " + pathname);
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
/*      */   public int rnfr(String pathname)
/*      */     throws IOException
/*      */   {
/* 1401 */     return sendCommand(FTPCmd.RNFR, pathname);
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
/*      */   public int rnto(String pathname)
/*      */     throws IOException
/*      */   {
/* 1420 */     return sendCommand(FTPCmd.RNTO, pathname);
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
/*      */   public int dele(String pathname)
/*      */     throws IOException
/*      */   {
/* 1439 */     return sendCommand(FTPCmd.DELE, pathname);
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
/*      */   public int rmd(String pathname)
/*      */     throws IOException
/*      */   {
/* 1458 */     return sendCommand(FTPCmd.RMD, pathname);
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
/*      */   public int mkd(String pathname)
/*      */     throws IOException
/*      */   {
/* 1477 */     return sendCommand(FTPCmd.MKD, pathname);
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
/*      */   public int pwd()
/*      */     throws IOException
/*      */   {
/* 1495 */     return sendCommand(FTPCmd.PWD);
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
/*      */   public int list()
/*      */     throws IOException
/*      */   {
/* 1516 */     return sendCommand(FTPCmd.LIST);
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
/*      */   public int list(String pathname)
/*      */     throws IOException
/*      */   {
/* 1539 */     return sendCommand(FTPCmd.LIST, pathname);
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
/*      */   public int mlsd()
/*      */     throws IOException
/*      */   {
/* 1561 */     return sendCommand(FTPCmd.MLSD);
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
/*      */   public int mlsd(String path)
/*      */     throws IOException
/*      */   {
/* 1585 */     return sendCommand(FTPCmd.MLSD, path);
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
/*      */   public int mlst()
/*      */     throws IOException
/*      */   {
/* 1607 */     return sendCommand(FTPCmd.MLST);
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
/*      */   public int mlst(String path)
/*      */     throws IOException
/*      */   {
/* 1631 */     return sendCommand(FTPCmd.MLST, path);
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
/*      */   public int nlst()
/*      */     throws IOException
/*      */   {
/* 1652 */     return sendCommand(FTPCmd.NLST);
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
/*      */   public int nlst(String pathname)
/*      */     throws IOException
/*      */   {
/* 1675 */     return sendCommand(FTPCmd.NLST, pathname);
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
/*      */   public int site(String parameters)
/*      */     throws IOException
/*      */   {
/* 1694 */     return sendCommand(FTPCmd.SITE, parameters);
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
/*      */   public int syst()
/*      */     throws IOException
/*      */   {
/* 1712 */     return sendCommand(FTPCmd.SYST);
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
/*      */   public int stat()
/*      */     throws IOException
/*      */   {
/* 1730 */     return sendCommand(FTPCmd.STAT);
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
/*      */   public int stat(String pathname)
/*      */     throws IOException
/*      */   {
/* 1749 */     return sendCommand(FTPCmd.STAT, pathname);
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
/*      */   public int help()
/*      */     throws IOException
/*      */   {
/* 1767 */     return sendCommand(FTPCmd.HELP);
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
/*      */   public int help(String command)
/*      */     throws IOException
/*      */   {
/* 1786 */     return sendCommand(FTPCmd.HELP, command);
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
/*      */   public int noop()
/*      */     throws IOException
/*      */   {
/* 1804 */     return sendCommand(FTPCmd.NOOP);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isStrictMultilineParsing()
/*      */   {
/* 1813 */     return this.strictMultilineParsing;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setStrictMultilineParsing(boolean strictMultilineParsing)
/*      */   {
/* 1822 */     this.strictMultilineParsing = strictMultilineParsing;
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
/*      */   public boolean isStrictReplyParsing()
/*      */   {
/* 1836 */     return this.strictReplyParsing;
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
/*      */   public void setStrictReplyParsing(boolean strictReplyParsing)
/*      */   {
/* 1852 */     this.strictReplyParsing = strictReplyParsing;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ProtocolCommandSupport getCommandSupport()
/*      */   {
/* 1860 */     return this._commandSupport_;
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\ftp\FTP.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */