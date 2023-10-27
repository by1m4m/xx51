/*     */ package com.sun.jna.platform;
/*     */ 
/*     */ import com.sun.jna.Platform;
/*     */ import com.sun.jna.platform.unix.X11;
/*     */ import com.sun.jna.platform.unix.X11.Display;
/*     */ import com.sun.jna.platform.unix.X11.KeySym;
/*     */ import com.sun.jna.platform.win32.User32;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.HeadlessException;
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
/*     */ public class KeyboardUtils
/*     */ {
/*     */   static final NativeKeyboardUtils INSTANCE;
/*     */   
/*     */   static
/*     */   {
/*  37 */     if (GraphicsEnvironment.isHeadless()) {
/*  38 */       throw new HeadlessException("KeyboardUtils requires a keyboard");
/*     */     }
/*  40 */     if (Platform.isWindows()) {
/*  41 */       INSTANCE = new W32KeyboardUtils(null);
/*     */     } else {
/*  43 */       if (Platform.isMac()) {
/*  44 */         INSTANCE = new MacKeyboardUtils(null);
/*     */         
/*  46 */         throw new UnsupportedOperationException("No support (yet) for " + System.getProperty("os.name"));
/*     */       }
/*     */       
/*  49 */       INSTANCE = new X11KeyboardUtils(null);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean isPressed(int keycode, int location) {
/*  54 */     return INSTANCE.isPressed(keycode, location);
/*     */   }
/*     */   
/*  57 */   public static boolean isPressed(int keycode) { return INSTANCE.isPressed(keycode); }
/*     */   
/*     */ 
/*     */   private static abstract class NativeKeyboardUtils {
/*     */     public abstract boolean isPressed(int paramInt1, int paramInt2);
/*     */     
/*  63 */     public boolean isPressed(int keycode) { return isPressed(keycode, 0); }
/*     */   }
/*     */   
/*     */   private static class W32KeyboardUtils extends KeyboardUtils.NativeKeyboardUtils {
/*  67 */     private W32KeyboardUtils() { super(); }
/*     */     
/*  69 */     private int toNative(int code, int loc) { if (((code >= 65) && (code <= 90)) || ((code >= 48) && (code <= 57)))
/*     */       {
/*  71 */         return code;
/*     */       }
/*  73 */       if (code == 16) {
/*  74 */         if ((loc & 0x3) != 0) {
/*  75 */           return 161;
/*     */         }
/*  77 */         if ((loc & 0x2) != 0) {
/*  78 */           return 160;
/*     */         }
/*  80 */         return 16;
/*     */       }
/*  82 */       if (code == 17) {
/*  83 */         if ((loc & 0x3) != 0) {
/*  84 */           return 163;
/*     */         }
/*  86 */         if ((loc & 0x2) != 0) {
/*  87 */           return 162;
/*     */         }
/*  89 */         return 17;
/*     */       }
/*  91 */       if (code == 18) {
/*  92 */         if ((loc & 0x3) != 0) {
/*  93 */           return 165;
/*     */         }
/*  95 */         if ((loc & 0x2) != 0) {
/*  96 */           return 164;
/*     */         }
/*  98 */         return 18;
/*     */       }
/* 100 */       return 0;
/*     */     }
/*     */     
/* 103 */     public boolean isPressed(int keycode, int location) { User32 lib = User32.INSTANCE;
/* 104 */       return (lib.GetAsyncKeyState(toNative(keycode, location)) & 0x8000) != 0;
/*     */     } }
/*     */   
/* 107 */   private static class MacKeyboardUtils extends KeyboardUtils.NativeKeyboardUtils { private MacKeyboardUtils() { super(); }
/*     */     
/* 109 */     public boolean isPressed(int keycode, int location) { return false; }
/*     */   }
/*     */   
/* 112 */   private static class X11KeyboardUtils extends KeyboardUtils.NativeKeyboardUtils { private X11KeyboardUtils() { super(); }
/*     */     
/*     */     private int toKeySym(int code, int location)
/*     */     {
/* 116 */       if ((code >= 65) && (code <= 90))
/* 117 */         return 97 + (code - 65);
/* 118 */       if ((code >= 48) && (code <= 57))
/* 119 */         return 48 + (code - 48);
/* 120 */       if (code == 16) {
/* 121 */         if ((location & 0x3) != 0)
/* 122 */           return 65505;
/* 123 */         return 65505;
/*     */       }
/* 125 */       if (code == 17) {
/* 126 */         if ((location & 0x3) != 0)
/* 127 */           return 65508;
/* 128 */         return 65507;
/*     */       }
/* 130 */       if (code == 18) {
/* 131 */         if ((location & 0x3) != 0)
/* 132 */           return 65514;
/* 133 */         return 65513;
/*     */       }
/* 135 */       if (code == 157) {
/* 136 */         if ((location & 0x3) != 0)
/* 137 */           return 65512;
/* 138 */         return 65511;
/*     */       }
/* 140 */       return 0;
/*     */     }
/*     */     
/* 143 */     public boolean isPressed(int keycode, int location) { X11 lib = X11.INSTANCE;
/* 144 */       X11.Display dpy = lib.XOpenDisplay(null);
/* 145 */       if (dpy == null) {
/* 146 */         throw new Error("Can't open X Display");
/*     */       }
/*     */       try {
/* 149 */         byte[] keys = new byte[32];
/*     */         
/* 151 */         lib.XQueryKeymap(dpy, keys);
/* 152 */         int keysym = toKeySym(keycode, location);
/* 153 */         for (int code = 5; code < 256; code++) {
/* 154 */           int idx = code / 8;
/* 155 */           int shift = code % 8;
/* 156 */           if ((keys[idx] & 1 << shift) != 0) {
/* 157 */             int sym = lib.XKeycodeToKeysym(dpy, (byte)code, 0).intValue();
/* 158 */             if (sym == keysym) {
/* 159 */               return true;
/*     */             }
/*     */           }
/*     */         }
/*     */       } finally {
/* 164 */         lib.XCloseDisplay(dpy);
/*     */       }
/* 166 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\platform\KeyboardUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */