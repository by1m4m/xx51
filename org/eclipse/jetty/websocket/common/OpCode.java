/*     */ package org.eclipse.jetty.websocket.common;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class OpCode
/*     */ {
/*     */   public static final byte CONTINUATION = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final byte TEXT = 1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final byte BINARY = 2;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final byte CLOSE = 8;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final byte PING = 9;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final byte PONG = 10;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final byte UNDEFINED = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isControlFrame(byte opcode)
/*     */   {
/*  72 */     return opcode >= 8;
/*     */   }
/*     */   
/*     */   public static boolean isDataFrame(byte opcode)
/*     */   {
/*  77 */     return (opcode == 1) || (opcode == 2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isKnown(byte opcode)
/*     */   {
/*  89 */     return (opcode == 0) || (opcode == 1) || (opcode == 2) || (opcode == 8) || (opcode == 9) || (opcode == 10);
/*     */   }
/*     */   
/*     */   public static String name(byte opcode)
/*     */   {
/*  94 */     switch (opcode)
/*     */     {
/*     */     case -1: 
/*  97 */       return "NO-OP";
/*     */     case 0: 
/*  99 */       return "CONTINUATION";
/*     */     case 1: 
/* 101 */       return "TEXT";
/*     */     case 2: 
/* 103 */       return "BINARY";
/*     */     case 8: 
/* 105 */       return "CLOSE";
/*     */     case 9: 
/* 107 */       return "PING";
/*     */     case 10: 
/* 109 */       return "PONG";
/*     */     }
/* 111 */     return "NON-SPEC[" + opcode + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\OpCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */