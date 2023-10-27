/*    */ package oshi.jna.platform.mac;
/*    */ 
/*    */ import com.sun.jna.Library;
/*    */ import com.sun.jna.Native;
/*    */ import com.sun.jna.NativeLong;
/*    */ import com.sun.jna.Pointer;
/*    */ import com.sun.jna.PointerType;
/*    */ import com.sun.jna.ptr.ByReference;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract interface CoreFoundation
/*    */   extends Library
/*    */ {
/* 37 */   public static final CoreFoundation INSTANCE = (CoreFoundation)Native.loadLibrary("CoreFoundation", CoreFoundation.class);
/*    */   public static final int UTF_8 = 134217984;
/*    */   
/*    */   public abstract int CFArrayGetCount(CFArrayRef paramCFArrayRef);
/*    */   
/*    */   public abstract CFStringRef CFStringCreateWithCharacters(Object paramObject, char[] paramArrayOfChar, NativeLong paramNativeLong);
/*    */   
/*    */   public abstract CFMutableDictionaryRef CFDictionaryCreateMutable(CFAllocatorRef paramCFAllocatorRef, int paramInt, Pointer paramPointer1, Pointer paramPointer2);
/*    */   
/*    */   public abstract void CFRelease(PointerType paramPointerType);
/*    */   
/*    */   public abstract void CFDictionarySetValue(CFMutableDictionaryRef paramCFMutableDictionaryRef, PointerType paramPointerType1, PointerType paramPointerType2);
/*    */   
/*    */   public abstract Pointer CFDictionaryGetValue(CFDictionaryRef paramCFDictionaryRef, CFStringRef paramCFStringRef);
/*    */   
/*    */   public abstract boolean CFDictionaryGetValueIfPresent(CFDictionaryRef paramCFDictionaryRef, CFStringRef paramCFStringRef, PointerType paramPointerType);
/*    */   
/*    */   public abstract boolean CFStringGetCString(Pointer paramPointer1, Pointer paramPointer2, long paramLong, int paramInt);
/*    */   
/*    */   public abstract boolean CFBooleanGetValue(Pointer paramPointer);
/*    */   
/*    */   public abstract CFTypeRef CFArrayGetValueAtIndex(CFArrayRef paramCFArrayRef, int paramInt);
/*    */   
/*    */   public abstract void CFNumberGetValue(Pointer paramPointer, int paramInt, ByReference paramByReference);
/*    */   
/*    */   public abstract long CFStringGetLength(Pointer paramPointer);
/*    */   
/*    */   public abstract long CFStringGetMaximumSizeForEncoding(long paramLong, int paramInt);
/*    */   
/*    */   public abstract CFAllocatorRef CFAllocatorGetDefault();
/*    */   
/*    */   public abstract int CFDataGetLength(CFTypeRef paramCFTypeRef);
/*    */   
/*    */   public abstract PointerByReference CFDataGetBytePtr(CFTypeRef paramCFTypeRef);
/*    */   
/*    */   public static class CFDataRef extends CoreFoundation.CFTypeRef
/*    */   {}
/*    */   
/*    */   public static class CFStringRef extends PointerType {
/* 76 */     public static CFStringRef toCFString(String s) { char[] chars = s.toCharArray();
/* 77 */       int length = chars.length;
/* 78 */       return CoreFoundation.INSTANCE.CFStringCreateWithCharacters(null, chars, new NativeLong(length));
/*    */     }
/*    */   }
/*    */   
/*    */   public static class CFAllocatorRef
/*    */     extends PointerType
/*    */   {}
/*    */   
/*    */   public static class CFMutableDictionaryRef
/*    */     extends CoreFoundation.CFDictionaryRef
/*    */   {}
/*    */   
/*    */   public static class CFDictionaryRef
/*    */     extends PointerType
/*    */   {}
/*    */   
/*    */   public static class CFArrayRef
/*    */     extends PointerType
/*    */   {}
/*    */   
/*    */   public static class CFBooleanRef
/*    */     extends PointerType
/*    */   {}
/*    */   
/*    */   public static class CFNumberRef
/*    */     extends PointerType
/*    */   {}
/*    */   
/*    */   public static class CFTypeRef
/*    */     extends PointerType
/*    */   {}
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\jna\platform\mac\CoreFoundation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */