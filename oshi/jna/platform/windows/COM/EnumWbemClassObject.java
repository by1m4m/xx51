/*    */ package oshi.jna.platform.windows.COM;
/*    */ 
/*    */ import com.sun.jna.NativeLong;
/*    */ import com.sun.jna.Pointer;
/*    */ import com.sun.jna.platform.win32.COM.Unknown;
/*    */ import com.sun.jna.platform.win32.WinNT.HRESULT;
/*    */ import com.sun.jna.ptr.LongByReference;
/*    */ import com.sun.jna.ptr.PointerByReference;
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
/*    */ public class EnumWbemClassObject
/*    */   extends Unknown
/*    */ {
/*    */   public static final int WBEM_FLAG_RETURN_IMMEDIATELY = 16;
/*    */   public static final int WBEM_FLAG_FORWARD_ONLY = 32;
/*    */   public static final int WBEM_INFINITE = -1;
/*    */   
/*    */   public EnumWbemClassObject(Pointer pvInstance)
/*    */   {
/* 34 */     super(pvInstance);
/*    */   }
/*    */   
/*    */ 
/*    */   public WinNT.HRESULT Next(NativeLong lTimeOut, NativeLong uCount, PointerByReference ppObjects, LongByReference puReturned)
/*    */   {
/* 40 */     return (WinNT.HRESULT)_invokeNativeObject(4, new Object[] { getPointer(), lTimeOut, uCount, ppObjects, puReturned }, WinNT.HRESULT.class);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\jna\platform\windows\COM\EnumWbemClassObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */