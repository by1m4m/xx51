/*    */ package oshi.hardware.common;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import oshi.hardware.Display;
/*    */ import oshi.util.EdidUtil;
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
/*    */ public abstract class AbstractDisplay
/*    */   implements Display
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected byte[] edid;
/*    */   
/*    */   protected AbstractDisplay(byte[] edid)
/*    */   {
/* 36 */     this.edid = edid;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public byte[] getEdid()
/*    */   {
/* 44 */     return Arrays.copyOf(this.edid, this.edid.length);
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 49 */     return EdidUtil.toString(this.edid);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\common\AbstractDisplay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */