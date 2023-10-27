/*    */ package oshi.jna.platform.windows.COM;
/*    */ 
/*    */ import com.sun.jna.NativeLong;
/*    */ import com.sun.jna.Pointer;
/*    */ import com.sun.jna.platform.win32.COM.Unknown;
/*    */ import com.sun.jna.platform.win32.Variant.VARIANT.ByReference;
/*    */ import com.sun.jna.platform.win32.WTypes.BSTR;
/*    */ import com.sun.jna.platform.win32.WinNT.HRESULT;
/*    */ import com.sun.jna.ptr.LongByReference;
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
/*    */ public class WbemClassObject
/*    */   extends Unknown
/*    */ {
/*    */   public WbemClassObject(Pointer pvInstance)
/*    */   {
/* 32 */     super(pvInstance);
/*    */   }
/*    */   
/*    */ 
/*    */   public WinNT.HRESULT Get(WTypes.BSTR wszName, NativeLong lFlags, Variant.VARIANT.ByReference pVal, Pointer pvtType, LongByReference plFlavor)
/*    */   {
/* 38 */     return (WinNT.HRESULT)_invokeNativeObject(4, new Object[] { getPointer(), wszName, lFlags, pVal, pvtType, plFlavor }, WinNT.HRESULT.class);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\jna\platform\windows\COM\WbemClassObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */