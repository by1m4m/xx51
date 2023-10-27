/*     */ package com.google.api.client.util;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
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
/*     */ public class IOUtils
/*     */ {
/*     */   public static void copy(InputStream inputStream, OutputStream outputStream)
/*     */     throws IOException
/*     */   {
/*  63 */     copy(inputStream, outputStream, true);
/*     */   }
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
/*     */   public static void copy(InputStream inputStream, OutputStream outputStream, boolean closeInputStream)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  94 */       ByteStreams.copy(inputStream, outputStream);
/*     */     } finally {
/*  96 */       if (closeInputStream) {
/*  97 */         inputStream.close();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long computeLength(StreamingContent content)
/*     */     throws IOException
/*     */   {
/* 111 */     ByteCountingOutputStream countingStream = new ByteCountingOutputStream();
/*     */     try {
/* 113 */       content.writeTo(countingStream);
/*     */     } finally {
/* 115 */       countingStream.close();
/*     */     }
/* 117 */     return countingStream.count;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] serialize(Object value)
/*     */     throws IOException
/*     */   {
/* 127 */     ByteArrayOutputStream out = new ByteArrayOutputStream();
/* 128 */     serialize(value, out);
/* 129 */     return out.toByteArray();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void serialize(Object value, OutputStream outputStream)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 141 */       new ObjectOutputStream(outputStream).writeObject(value);
/*     */     } finally {
/* 143 */       outputStream.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <S extends Serializable> S deserialize(byte[] bytes)
/*     */     throws IOException
/*     */   {
/* 155 */     if (bytes == null) {
/* 156 */       return null;
/*     */     }
/* 158 */     return deserialize(new ByteArrayInputStream(bytes));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <S extends Serializable> S deserialize(InputStream inputStream)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 171 */       return (Serializable)new ObjectInputStream(inputStream).readObject();
/*     */     } catch (ClassNotFoundException exception) {
/* 173 */       IOException ioe = new IOException("Failed to deserialize object");
/* 174 */       ioe.initCause(exception);
/* 175 */       throw ioe;
/*     */     } finally {
/* 177 */       inputStream.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isSymbolicLink(File file)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 190 */       Class<?> filesClass = Class.forName("java.nio.file.Files");
/* 191 */       Class<?> pathClass = Class.forName("java.nio.file.Path");
/* 192 */       Object path = File.class.getMethod("toPath", new Class[0]).invoke(file, new Object[0]);
/* 193 */       return ((Boolean)filesClass.getMethod("isSymbolicLink", new Class[] { pathClass }).invoke(null, new Object[] { path })).booleanValue();
/*     */     }
/*     */     catch (InvocationTargetException exception) {
/* 196 */       Throwable cause = exception.getCause();
/* 197 */       Throwables.propagateIfPossible(cause, IOException.class);
/*     */       
/* 199 */       throw new RuntimeException(cause);
/*     */     }
/*     */     catch (ClassNotFoundException exception) {}catch (IllegalArgumentException exception) {}catch (SecurityException exception) {}catch (IllegalAccessException exception) {}catch (NoSuchMethodException exception) {}
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
/* 213 */     if (File.separatorChar == '\\') {
/* 214 */       return false;
/*     */     }
/* 216 */     File canonical = file;
/* 217 */     if (file.getParent() != null) {
/* 218 */       canonical = new File(file.getParentFile().getCanonicalFile(), file.getName());
/*     */     }
/* 220 */     return !canonical.getCanonicalFile().equals(canonical.getAbsoluteFile());
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\IOUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */