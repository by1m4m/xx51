/*    */ package com.google.api.client.http;
/*    */ 
/*    */ import com.google.api.client.util.Preconditions;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.InputStream;
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
/*    */ public final class FileContent
/*    */   extends AbstractInputStreamContent
/*    */ {
/*    */   private final File file;
/*    */   
/*    */   public FileContent(String type, File file)
/*    */   {
/* 57 */     super(type);
/* 58 */     this.file = ((File)Preconditions.checkNotNull(file));
/*    */   }
/*    */   
/*    */   public long getLength() {
/* 62 */     return this.file.length();
/*    */   }
/*    */   
/*    */   public boolean retrySupported() {
/* 66 */     return true;
/*    */   }
/*    */   
/*    */   public InputStream getInputStream() throws FileNotFoundException
/*    */   {
/* 71 */     return new FileInputStream(this.file);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public File getFile()
/*    */   {
/* 80 */     return this.file;
/*    */   }
/*    */   
/*    */   public FileContent setType(String type)
/*    */   {
/* 85 */     return (FileContent)super.setType(type);
/*    */   }
/*    */   
/*    */   public FileContent setCloseInputStream(boolean closeInputStream)
/*    */   {
/* 90 */     return (FileContent)super.setCloseInputStream(closeInputStream);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\FileContent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */