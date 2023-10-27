/*    */ package com.sun.jna.platform.wince;
/*    */ 
/*    */ import com.sun.jna.Native;
/*    */ import com.sun.jna.platform.win32.WinNT;
/*    */ import com.sun.jna.win32.W32APIOptions;
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
/*    */ public abstract interface CoreDLL
/*    */   extends WinNT
/*    */ {
/* 25 */   public static final CoreDLL INSTANCE = (CoreDLL)Native.loadLibrary("coredll", CoreDLL.class, W32APIOptions.UNICODE_OPTIONS);
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\platform\wince\CoreDLL.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */