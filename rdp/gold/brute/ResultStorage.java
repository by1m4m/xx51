/*     */ package rdp.gold.brute;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.log4j.Logger;
/*     */ 
/*     */ 
/*     */ public class ResultStorage
/*     */ {
/*  18 */   private static final Logger logger = Logger.getLogger(ResultStorage.class);
/*  19 */   private static final Queue<String> validQueue = new ConcurrentLinkedQueue();
/*  20 */   private static final Queue<String> invalidQueue = new ConcurrentLinkedQueue();
/*  21 */   private static final Queue<String> notSupportedIpQueue = new ConcurrentLinkedQueue();
/*     */   private static File fileValid;
/*     */   private static File fileLog;
/*     */   private static FileOutputStream fileOutputStreamValid;
/*     */   private static FileOutputStream fileOutputStreamLog;
/*  26 */   private static AtomicInteger counterValid = new AtomicInteger();
/*  27 */   private static AtomicInteger counterInvalid = new AtomicInteger();
/*  28 */   private static List<Class> allowedContextLog = new ArrayList();
/*     */   
/*     */   static {
/*  31 */     if (Config.IS_WRITE_RESULT_TO_FILE) {
/*     */       try {
/*  33 */         fileValid = new File(Config.WRITE_RESULT_TO_FILE);
/*  34 */         if (!fileValid.exists()) {
/*  35 */           fileValid.createNewFile();
/*     */         }
/*     */         
/*  38 */         fileOutputStreamValid = new FileOutputStream(fileValid);
/*     */       } catch (FileNotFoundException e) {
/*  40 */         logger.error(e);
/*     */         
/*  42 */         System.exit(1);
/*     */       } catch (IOException e) {
/*  44 */         logger.error(e);
/*     */         
/*  46 */         System.exit(1);
/*     */       }
/*     */     }
/*     */     
/*  50 */     if (Config.IS_ENABLE_DEBUG.booleanValue()) {
/*     */       try {
/*  52 */         fileLog = new File(Config.LOG_PATH);
/*  53 */         if (!fileLog.exists()) {
/*  54 */           fileLog.createNewFile();
/*     */         }
/*     */         
/*  57 */         fileOutputStreamLog = new FileOutputStream(fileLog);
/*     */       } catch (FileNotFoundException e) {
/*  59 */         logger.error(e);
/*     */         
/*  61 */         System.exit(1);
/*     */       } catch (IOException e) {
/*  63 */         logger.error(e);
/*     */         
/*  65 */         System.exit(1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static Queue<String> getValidQueue() {
/*  71 */     return validQueue;
/*     */   }
/*     */   
/*     */   public static Queue<String> getInvalidQueue() {
/*  75 */     return invalidQueue;
/*     */   }
/*     */   
/*     */   public static Queue<String> getNotSupportedIpQueue() {
/*  79 */     return notSupportedIpQueue;
/*     */   }
/*     */   
/*     */   public static void saveSuccess(String host, String login, String password) {
/*  83 */     StringBuilder resultStringBuilder = new StringBuilder();
/*     */     
/*  85 */     resultStringBuilder.append(host);
/*  86 */     resultStringBuilder.append(";");
/*  87 */     resultStringBuilder.append(login);
/*  88 */     resultStringBuilder.append(";");
/*  89 */     resultStringBuilder.append(password);
/*     */     
/*  91 */     validQueue.add(resultStringBuilder.toString());
/*  92 */     counterValid.incrementAndGet();
/*     */     
/*  94 */     if (Config.IS_WRITE_RESULT_TO_FILE) {
/*  95 */       synchronized (counterValid) {
/*  96 */         writeSuccess((resultStringBuilder.toString() + "\r\n").getBytes());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void saveSuccess(String ip) {
/* 102 */     validQueue.add(ip);
/* 103 */     counterValid.incrementAndGet();
/*     */     
/* 105 */     if (Config.IS_WRITE_RESULT_TO_FILE) {
/* 106 */       synchronized (validQueue) {
/* 107 */         writeSuccess((ip + "\r\n").getBytes());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void saveInvalid(String ip) {
/* 113 */     invalidQueue.add(ip);
/* 114 */     counterInvalid.incrementAndGet();
/*     */   }
/*     */   
/*     */   public static void saveNotSupportedIp(String ip) {
/* 118 */     notSupportedIpQueue.add(ip);
/*     */   }
/*     */   
/*     */   public static void saveLog(String log) {
/* 122 */     saveLog(log, null);
/*     */   }
/*     */   
/*     */   public static void saveLog(String log, Class context) {
/* 126 */     if (Config.IS_ENABLE_DEBUG.booleanValue()) {
/* 127 */       synchronized (fileOutputStreamLog) {
/*     */         try {
/* 129 */           if (allowedContextLog.contains(context)) {
/* 130 */             fileOutputStreamLog.write(("[" + String.format("%tc", new Object[] { new Date() }) + "]\t[" + context.getName() + "]\t" + log + "\r\n").getBytes());
/*     */           }
/*     */         } catch (IOException e) {
/* 133 */           reinitFileOutputStreamLog();
/*     */           
/* 135 */           logger.error("Failed to write log info", e);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void writeSuccess(byte[] bytes) {
/*     */     try {
/* 143 */       fileOutputStreamValid.write(bytes);
/*     */     } catch (IOException e) {
/* 145 */       reinitFileOutputStreamValid();
/*     */       
/* 147 */       logger.error("Failed to write valid info", e);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void reinitFileOutputStreamValid() {
/*     */     try {
/* 153 */       synchronized (fileOutputStreamValid) {
/* 154 */         fileOutputStreamValid.close();
/*     */       }
/*     */     } catch (IOException e) {
/* 157 */       logger.error("Failed close fileOutputStreamValid", e);
/*     */     }
/*     */     try
/*     */     {
/* 161 */       synchronized (fileOutputStreamValid) {
/* 162 */         fileOutputStreamValid = new FileOutputStream(fileValid, true);
/*     */       }
/*     */     } catch (IOException e) {
/* 165 */       logger.error("Failed open fileOutputStreamValid", e);
/*     */       
/* 167 */       System.exit(1);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void reinitFileOutputStreamLog() {
/*     */     try {
/* 173 */       synchronized (fileOutputStreamLog) {
/* 174 */         fileOutputStreamLog.close();
/*     */       }
/*     */     } catch (IOException e) {
/* 177 */       logger.error("Failed close fileOutputStreamLog", e);
/*     */     }
/*     */     try
/*     */     {
/* 181 */       synchronized (fileOutputStreamLog) {
/* 182 */         fileOutputStreamLog = new FileOutputStream(fileLog, true);
/*     */       }
/*     */     } catch (IOException e) {
/* 185 */       logger.error("Failed open fileOutputStreamLog", e);
/*     */       
/* 187 */       System.exit(1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\ResultStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */