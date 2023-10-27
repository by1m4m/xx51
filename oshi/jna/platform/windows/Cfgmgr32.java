/*    */ package oshi.jna.platform.windows;
/*    */ 
/*    */ import com.sun.jna.Library;
/*    */ import com.sun.jna.Native;
/*    */ import com.sun.jna.ptr.IntByReference;
/*    */ import com.sun.jna.ptr.NativeLongByReference;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract interface Cfgmgr32
/*    */   extends Library
/*    */ {
/* 34 */   public static final Cfgmgr32 INSTANCE = (Cfgmgr32)Native.loadLibrary("Cfgmgr32", Cfgmgr32.class, W32APIOptions.DEFAULT_OPTIONS);
/*    */   
/*    */   public abstract int CM_Get_Parent(IntByReference paramIntByReference, int paramInt1, int paramInt2);
/*    */   
/*    */   public abstract int CM_Get_Child(IntByReference paramIntByReference, int paramInt1, int paramInt2);
/*    */   
/*    */   public abstract int CM_Get_Sibling(IntByReference paramIntByReference, int paramInt1, int paramInt2);
/*    */   
/*    */   public abstract int CM_Get_Device_ID(int paramInt1, char[] paramArrayOfChar, int paramInt2, int paramInt3);
/*    */   
/*    */   public abstract int CM_Get_Device_ID_Size(NativeLongByReference paramNativeLongByReference, int paramInt1, int paramInt2);
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\jna\platform\windows\Cfgmgr32.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */