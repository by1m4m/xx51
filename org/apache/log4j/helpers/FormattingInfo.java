/*    */ package org.apache.log4j.helpers;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FormattingInfo
/*    */ {
/* 31 */   int min = -1;
/* 32 */   int max = Integer.MAX_VALUE;
/* 33 */   boolean leftAlign = false;
/*    */   
/*    */   void reset() {
/* 36 */     this.min = -1;
/* 37 */     this.max = Integer.MAX_VALUE;
/* 38 */     this.leftAlign = false;
/*    */   }
/*    */   
/*    */   void dump() {
/* 42 */     LogLog.debug("min=" + this.min + ", max=" + this.max + ", leftAlign=" + this.leftAlign);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\log4j\helpers\FormattingInfo.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */