/*    */ package com.sun.jna.platform;
/*    */ 
/*    */ import com.sun.jna.platform.win32.WinDef.HWND;
/*    */ import java.awt.Rectangle;
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
/*    */ 
/*    */ 
/*    */ public class DesktopWindow
/*    */ {
/*    */   private WinDef.HWND hwnd;
/*    */   private String title;
/*    */   private String filePath;
/*    */   private Rectangle locAndSize;
/*    */   
/*    */   public DesktopWindow(WinDef.HWND hwnd, String title, String filePath, Rectangle locAndSize)
/*    */   {
/* 43 */     this.hwnd = hwnd;
/* 44 */     this.title = title;
/* 45 */     this.filePath = filePath;
/* 46 */     this.locAndSize = locAndSize;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public WinDef.HWND getHWND()
/*    */   {
/* 53 */     return this.hwnd;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getTitle()
/*    */   {
/* 60 */     return this.title;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getFilePath()
/*    */   {
/* 67 */     return this.filePath;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Rectangle getLocAndSize()
/*    */   {
/* 74 */     return this.locAndSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\platform\DesktopWindow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */