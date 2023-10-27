/*    */ package org.eclipse.jetty.util.component;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import org.eclipse.jetty.util.IO;
/*    */ import org.eclipse.jetty.util.log.Log;
/*    */ import org.eclipse.jetty.util.log.Logger;
/*    */ import org.eclipse.jetty.util.resource.Resource;
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
/*    */ public class FileDestroyable
/*    */   implements Destroyable
/*    */ {
/* 34 */   private static final Logger LOG = Log.getLogger(FileDestroyable.class);
/* 35 */   final List<File> _files = new ArrayList();
/*    */   
/*    */ 
/*    */   public FileDestroyable() {}
/*    */   
/*    */   public FileDestroyable(String file)
/*    */     throws IOException
/*    */   {
/* 43 */     this._files.add(Resource.newResource(file).getFile());
/*    */   }
/*    */   
/*    */   public FileDestroyable(File file)
/*    */   {
/* 48 */     this._files.add(file);
/*    */   }
/*    */   
/*    */   public void addFile(String file) throws IOException
/*    */   {
/* 53 */     Resource r = Resource.newResource(file);Throwable localThrowable1 = null;
/*    */     try {
/* 55 */       this._files.add(r.getFile());
/*    */     }
/*    */     catch (Throwable localThrowable)
/*    */     {
/* 53 */       localThrowable1 = localThrowable;throw localThrowable;
/*    */     }
/*    */     finally {
/* 56 */       if (r != null) $closeResource(localThrowable1, r);
/*    */     }
/*    */   }
/*    */   
/*    */   public void addFile(File file) {
/* 61 */     this._files.add(file);
/*    */   }
/*    */   
/*    */   public void addFiles(Collection<File> files)
/*    */   {
/* 66 */     this._files.addAll(files);
/*    */   }
/*    */   
/*    */   public void removeFile(String file) throws IOException
/*    */   {
/* 71 */     Resource r = Resource.newResource(file);Throwable localThrowable1 = null;
/*    */     try {
/* 73 */       this._files.remove(r.getFile());
/*    */     }
/*    */     catch (Throwable localThrowable)
/*    */     {
/* 71 */       localThrowable1 = localThrowable;throw localThrowable;
/*    */     }
/*    */     finally {
/* 74 */       if (r != null) $closeResource(localThrowable1, r);
/*    */     }
/*    */   }
/*    */   
/*    */   public void removeFile(File file) {
/* 79 */     this._files.remove(file);
/*    */   }
/*    */   
/*    */ 
/*    */   public void destroy()
/*    */   {
/* 85 */     for (File file : this._files)
/*    */     {
/* 87 */       if (file.exists())
/*    */       {
/* 89 */         if (LOG.isDebugEnabled())
/* 90 */           LOG.debug("Destroy {}", new Object[] { file });
/* 91 */         IO.delete(file);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\component\FileDestroyable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */