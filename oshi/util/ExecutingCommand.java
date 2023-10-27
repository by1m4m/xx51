/*     */ package oshi.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExecutingCommand
/*     */ {
/*  38 */   private static final Logger LOG = LoggerFactory.getLogger(ExecutingCommand.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<String> runNative(String cmdToRun)
/*     */   {
/*  52 */     String[] cmd = cmdToRun.split(" ");
/*  53 */     return runNative(cmd);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<String> runNative(String[] cmdToRunWithArgs)
/*     */   {
/*  65 */     Process p = null;
/*     */     try {
/*  67 */       p = Runtime.getRuntime().exec(cmdToRunWithArgs);
/*     */     } catch (SecurityException|IOException e) {
/*  69 */       LOG.trace("", e);
/*  70 */       return new ArrayList(0);
/*     */     }
/*     */     
/*  73 */     ArrayList<String> sa = new ArrayList();
/*  74 */     try { BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));Throwable localThrowable3 = null;
/*     */       try { String line;
/*  76 */         while ((line = reader.readLine()) != null) {
/*  77 */           sa.add(line);
/*     */         }
/*  79 */         p.waitFor();
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/*  74 */         localThrowable3 = localThrowable1;throw localThrowable1;
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/*  80 */         if (reader != null) if (localThrowable3 != null) try { reader.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else reader.close();
/*  81 */       } } catch (InterruptedException|IOException e) { LOG.trace("", e);
/*  82 */       return new ArrayList(0);
/*     */     }
/*  84 */     return sa;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getFirstAnswer(String cmd2launch)
/*     */   {
/*  95 */     return getAnswerAt(cmd2launch, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getAnswerAt(String cmd2launch, int answerIdx)
/*     */   {
/* 110 */     List<String> sa = runNative(cmd2launch);
/*     */     
/* 112 */     if ((answerIdx >= 0) && (answerIdx < sa.size())) {
/* 113 */       return (String)sa.get(answerIdx);
/*     */     }
/* 115 */     return "";
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\util\ExecutingCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */