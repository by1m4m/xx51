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
/*     */ public class WindowSizeOptionHandler
/*     */   extends TelnetOptionHandler
/*     */ {
/*  30 */   private int m_nWidth = 80;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  35 */   private int m_nHeight = 24;
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
/*     */   protected static final int WINDOW_SIZE = 31;
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
/*     */   public WindowSizeOptionHandler(int nWidth, int nHeight, boolean initlocal, boolean initremote, boolean acceptlocal, boolean acceptremote)
/*     */   {
/*  63 */     super(31, initlocal, initremote, acceptlocal, acceptremote);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  71 */     this.m_nWidth = nWidth;
/*  72 */     this.m_nHeight = nHeight;
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
/*     */   public WindowSizeOptionHandler(int nWidth, int nHeight)
/*     */   {
/*  86 */     super(31, false, false, false, false);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  94 */     this.m_nWidth = nWidth;
/*  95 */     this.m_nHeight = nHeight;
/*     */   }
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
/* 107 */     int nCompoundWindowSize = this.m_nWidth * 65536 + this.m_nHeight;
/* 108 */     int nResponseSize = 5;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 113 */     if (this.m_nWidth % 256 == 255) {
/* 114 */       nResponseSize++;
/*     */     }
/*     */     
/* 117 */     if (this.m_nWidth / 256 == 255) {
/* 118 */       nResponseSize++;
/*     */     }
/*     */     
/* 121 */     if (this.m_nHeight % 256 == 255) {
/* 122 */       nResponseSize++;
/*     */     }
/*     */     
/* 125 */     if (this.m_nHeight / 256 == 255) {
/* 126 */       nResponseSize++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 132 */     int[] response = new int[nResponseSize];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 142 */     response[0] = 31;
/*     */     
/*     */ 
/* 145 */     int nIndex = 1; for (int nShift = 24; 
/* 146 */         nIndex < nResponseSize; 
/* 147 */         nShift -= 8)
/*     */     {
/* 149 */       int nTurnedOnBits = 255;
/* 150 */       nTurnedOnBits <<= nShift;
/* 151 */       response[nIndex] = ((nCompoundWindowSize & nTurnedOnBits) >>> nShift);
/*     */       
/* 153 */       if (response[nIndex] == 255) {
/* 154 */         nIndex++;
/* 155 */         response[nIndex] = 255;
/*     */       }
/* 147 */       nIndex++;
/*     */     }
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
/* 159 */     return response;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\telnet\WindowSizeOptionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */