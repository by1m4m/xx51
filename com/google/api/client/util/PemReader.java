/*     */ package com.google.api.client.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public final class PemReader
/*     */ {
/*  58 */   private static final Pattern BEGIN_PATTERN = Pattern.compile("-----BEGIN ([A-Z ]+)-----");
/*  59 */   private static final Pattern END_PATTERN = Pattern.compile("-----END ([A-Z ]+)-----");
/*     */   
/*     */ 
/*     */   private BufferedReader reader;
/*     */   
/*     */ 
/*     */ 
/*     */   public PemReader(Reader reader)
/*     */   {
/*  68 */     this.reader = new BufferedReader(reader);
/*     */   }
/*     */   
/*     */   public Section readNextSection() throws IOException
/*     */   {
/*  73 */     return readNextSection(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Section readNextSection(String titleToLookFor)
/*     */     throws IOException
/*     */   {
/*  83 */     String title = null;
/*  84 */     StringBuilder keyBuilder = null;
/*     */     for (;;) {
/*  86 */       String line = this.reader.readLine();
/*  87 */       if (line == null) {
/*  88 */         Preconditions.checkArgument(title == null, "missing end tag (%s)", new Object[] { title });
/*  89 */         return null;
/*     */       }
/*  91 */       if (keyBuilder == null) {
/*  92 */         Matcher m = BEGIN_PATTERN.matcher(line);
/*  93 */         if (m.matches()) {
/*  94 */           String curTitle = m.group(1);
/*  95 */           if ((titleToLookFor == null) || (curTitle.equals(titleToLookFor))) {
/*  96 */             keyBuilder = new StringBuilder();
/*  97 */             title = curTitle;
/*     */           }
/*     */         }
/*     */       } else {
/* 101 */         Matcher m = END_PATTERN.matcher(line);
/* 102 */         if (m.matches()) {
/* 103 */           String endTitle = m.group(1);
/* 104 */           Preconditions.checkArgument(endTitle.equals(title), "end tag (%s) doesn't match begin tag (%s)", new Object[] { endTitle, title });
/*     */           
/* 106 */           return new Section(title, Base64.decodeBase64(keyBuilder.toString()));
/*     */         }
/* 108 */         keyBuilder.append(line);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Section readFirstSectionAndClose(Reader reader)
/*     */     throws IOException
/*     */   {
/* 120 */     return readFirstSectionAndClose(reader, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Section readFirstSectionAndClose(Reader reader, String titleToLookFor)
/*     */     throws IOException
/*     */   {
/* 133 */     PemReader pemReader = new PemReader(reader);
/*     */     try {
/* 135 */       return pemReader.readNextSection(titleToLookFor);
/*     */     } finally {
/* 137 */       pemReader.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 149 */     this.reader.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class Section
/*     */   {
/*     */     private final String title;
/*     */     
/*     */ 
/*     */     private final byte[] base64decodedBytes;
/*     */     
/*     */ 
/*     */ 
/*     */     Section(String title, byte[] base64decodedBytes)
/*     */     {
/* 166 */       this.title = ((String)Preconditions.checkNotNull(title));
/* 167 */       this.base64decodedBytes = ((byte[])Preconditions.checkNotNull(base64decodedBytes));
/*     */     }
/*     */     
/*     */     public String getTitle()
/*     */     {
/* 172 */       return this.title;
/*     */     }
/*     */     
/*     */     public byte[] getBase64DecodedBytes()
/*     */     {
/* 177 */       return this.base64decodedBytes;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\PemReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */