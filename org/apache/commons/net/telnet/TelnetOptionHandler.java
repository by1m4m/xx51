/*     */ package org.apache.commons.net.telnet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TelnetOptionHandler
/*     */ {
/*  33 */   private int optionCode = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  38 */   private boolean initialLocal = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  43 */   private boolean initialRemote = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  48 */   private boolean acceptLocal = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  53 */   private boolean acceptRemote = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  58 */   private boolean doFlag = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  63 */   private boolean willFlag = false;
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
/*     */   public TelnetOptionHandler(int optcode, boolean initlocal, boolean initremote, boolean acceptlocal, boolean acceptremote)
/*     */   {
/*  83 */     this.optionCode = optcode;
/*  84 */     this.initialLocal = initlocal;
/*  85 */     this.initialRemote = initremote;
/*  86 */     this.acceptLocal = acceptlocal;
/*  87 */     this.acceptRemote = acceptremote;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getOptionCode()
/*     */   {
/*  98 */     return this.optionCode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getAcceptLocal()
/*     */   {
/* 109 */     return this.acceptLocal;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getAcceptRemote()
/*     */   {
/* 120 */     return this.acceptRemote;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAcceptLocal(boolean accept)
/*     */   {
/* 131 */     this.acceptLocal = accept;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAcceptRemote(boolean accept)
/*     */   {
/* 142 */     this.acceptRemote = accept;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getInitLocal()
/*     */   {
/* 153 */     return this.initialLocal;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getInitRemote()
/*     */   {
/* 164 */     return this.initialRemote;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInitLocal(boolean init)
/*     */   {
/* 175 */     this.initialLocal = init;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInitRemote(boolean init)
/*     */   {
/* 186 */     this.initialRemote = init;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[] answerSubnegotiation(int[] suboptionData, int suboptionLength)
/*     */   {
/* 204 */     return null;
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
/*     */ 
/*     */   public int[] startSubnegotiationLocal()
/*     */   {
/* 220 */     return null;
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
/*     */ 
/*     */   public int[] startSubnegotiationRemote()
/*     */   {
/* 236 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean getWill()
/*     */   {
/* 247 */     return this.willFlag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void setWill(boolean state)
/*     */   {
/* 258 */     this.willFlag = state;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean getDo()
/*     */   {
/* 269 */     return this.doFlag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void setDo(boolean state)
/*     */   {
/* 281 */     this.doFlag = state;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\telnet\TelnetOptionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */