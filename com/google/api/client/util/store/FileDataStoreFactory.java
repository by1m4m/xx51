/*     */ package com.google.api.client.util.store;
/*     */ 
/*     */ import com.google.api.client.util.IOUtils;
/*     */ import com.google.api.client.util.Maps;
/*     */ import com.google.api.client.util.Throwables;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.logging.Logger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileDataStoreFactory
/*     */   extends AbstractDataStoreFactory
/*     */ {
/*  44 */   private static final Logger LOGGER = Logger.getLogger(FileDataStoreFactory.class.getName());
/*     */   
/*     */ 
/*     */   private final File dataDirectory;
/*     */   
/*     */ 
/*     */   public FileDataStoreFactory(File dataDirectory)
/*     */     throws IOException
/*     */   {
/*  53 */     dataDirectory = dataDirectory.getCanonicalFile();
/*  54 */     this.dataDirectory = dataDirectory;
/*     */     String str;
/*  56 */     if (IOUtils.isSymbolicLink(dataDirectory)) {
/*  57 */       str = String.valueOf(String.valueOf(dataDirectory));throw new IOException(31 + str.length() + "unable to use a symbolic link: " + str);
/*     */     }
/*     */     
/*  60 */     if ((!dataDirectory.exists()) && (!dataDirectory.mkdirs())) {
/*  61 */       str = String.valueOf(String.valueOf(dataDirectory));throw new IOException(28 + str.length() + "unable to create directory: " + str);
/*     */     }
/*  63 */     setPermissionsToOwnerOnly(dataDirectory);
/*     */   }
/*     */   
/*     */   public final File getDataDirectory()
/*     */   {
/*  68 */     return this.dataDirectory;
/*     */   }
/*     */   
/*     */   protected <V extends Serializable> DataStore<V> createDataStore(String id) throws IOException
/*     */   {
/*  73 */     return new FileDataStore(this, this.dataDirectory, id);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static class FileDataStore<V extends Serializable>
/*     */     extends AbstractMemoryDataStore<V>
/*     */   {
/*     */     private final File dataFile;
/*     */     
/*     */ 
/*     */ 
/*     */     FileDataStore(FileDataStoreFactory dataStore, File dataDirectory, String id)
/*     */       throws IOException
/*     */     {
/*  89 */       super(id);
/*  90 */       this.dataFile = new File(dataDirectory, id);
/*     */       
/*  92 */       if (IOUtils.isSymbolicLink(this.dataFile)) {
/*  93 */         String str = String.valueOf(String.valueOf(this.dataFile));throw new IOException(31 + str.length() + "unable to use a symbolic link: " + str);
/*     */       }
/*     */       
/*  96 */       if (this.dataFile.createNewFile()) {
/*  97 */         this.keyValueMap = Maps.newHashMap();
/*     */         
/*  99 */         save();
/*     */       }
/*     */       else {
/* 102 */         this.keyValueMap = ((HashMap)IOUtils.deserialize(new FileInputStream(this.dataFile)));
/*     */       }
/*     */     }
/*     */     
/*     */     void save() throws IOException
/*     */     {
/* 108 */       IOUtils.serialize(this.keyValueMap, new FileOutputStream(this.dataFile));
/*     */     }
/*     */     
/*     */     public FileDataStoreFactory getDataStoreFactory()
/*     */     {
/* 113 */       return (FileDataStoreFactory)super.getDataStoreFactory();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static void setPermissionsToOwnerOnly(File file)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 128 */       Method setReadable = File.class.getMethod("setReadable", new Class[] { Boolean.TYPE, Boolean.TYPE });
/* 129 */       Method setWritable = File.class.getMethod("setWritable", new Class[] { Boolean.TYPE, Boolean.TYPE });
/* 130 */       Method setExecutable = File.class.getMethod("setExecutable", new Class[] { Boolean.TYPE, Boolean.TYPE });
/* 131 */       if (((Boolean)setReadable.invoke(file, new Object[] { Boolean.valueOf(false), Boolean.valueOf(false) })).booleanValue()) if (((Boolean)setWritable.invoke(file, new Object[] { Boolean.valueOf(false), Boolean.valueOf(false) })).booleanValue()) if (((Boolean)setExecutable.invoke(file, new Object[] { Boolean.valueOf(false), Boolean.valueOf(false) })).booleanValue()) {
/*     */             break label211;
/*     */           }
/* 134 */       String str = String.valueOf(String.valueOf(file));LOGGER.warning(44 + str.length() + "unable to change permissions for everybody: " + str);
/*     */       label211:
/* 136 */       if (((Boolean)setReadable.invoke(file, new Object[] { Boolean.valueOf(true), Boolean.valueOf(true) })).booleanValue()) if (((Boolean)setWritable.invoke(file, new Object[] { Boolean.valueOf(true), Boolean.valueOf(true) })).booleanValue()) if (((Boolean)setExecutable.invoke(file, new Object[] { Boolean.valueOf(true), Boolean.valueOf(true) })).booleanValue()) {
/*     */             return;
/*     */           }
/* 139 */       str = String.valueOf(String.valueOf(file));LOGGER.warning(40 + str.length() + "unable to change permissions for owner: " + str);
/*     */     }
/*     */     catch (InvocationTargetException exception) {
/* 142 */       cause = exception.getCause();
/* 143 */       Throwables.propagateIfPossible(cause, IOException.class);
/*     */       
/* 145 */       throw new RuntimeException(cause);
/*     */     } catch (NoSuchMethodException exception) {
/* 147 */       Throwable cause = String.valueOf(String.valueOf(file));LOGGER.warning(93 + cause.length() + "Unable to set permissions for " + cause + ", likely because you are running a version of Java prior to 1.6");
/*     */     }
/*     */     catch (SecurityException exception) {}catch (IllegalAccessException exception) {}catch (IllegalArgumentException exception) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\store\FileDataStoreFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */