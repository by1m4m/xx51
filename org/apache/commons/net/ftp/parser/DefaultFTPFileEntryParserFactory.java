/*     */ package org.apache.commons.net.ftp.parser;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.net.ftp.Configurable;
/*     */ import org.apache.commons.net.ftp.FTPClientConfig;
/*     */ import org.apache.commons.net.ftp.FTPFileEntryParser;
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
/*     */ public class DefaultFTPFileEntryParserFactory
/*     */   implements FTPFileEntryParserFactory
/*     */ {
/*     */   private static final String JAVA_IDENTIFIER = "\\p{javaJavaIdentifierStart}(\\p{javaJavaIdentifierPart})*";
/*     */   private static final String JAVA_QUALIFIED_NAME = "(\\p{javaJavaIdentifierStart}(\\p{javaJavaIdentifierPart})*\\.)+\\p{javaJavaIdentifierStart}(\\p{javaJavaIdentifierPart})*";
/*  46 */   private static final Pattern JAVA_QUALIFIED_NAME_PATTERN = Pattern.compile("(\\p{javaJavaIdentifierStart}(\\p{javaJavaIdentifierPart})*\\.)+\\p{javaJavaIdentifierStart}(\\p{javaJavaIdentifierPart})*");
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
/*     */   public FTPFileEntryParser createFileEntryParser(String key)
/*     */   {
/*  91 */     if (key == null) {
/*  92 */       throw new ParserInitializationException("Parser key cannot be null");
/*     */     }
/*  94 */     return createFileEntryParser(key, null);
/*     */   }
/*     */   
/*     */   private FTPFileEntryParser createFileEntryParser(String key, FTPClientConfig config)
/*     */   {
/*  99 */     FTPFileEntryParser parser = null;
/*     */     
/*     */ 
/* 102 */     if (JAVA_QUALIFIED_NAME_PATTERN.matcher(key).matches()) {
/*     */       try
/*     */       {
/* 105 */         Class<?> parserClass = Class.forName(key);
/*     */         try {
/* 107 */           parser = (FTPFileEntryParser)parserClass.newInstance();
/*     */         } catch (ClassCastException e) {
/* 109 */           throw new ParserInitializationException(parserClass.getName() + " does not implement the interface " + "org.apache.commons.net.ftp.FTPFileEntryParser.", e);
/*     */         }
/*     */         catch (Exception e)
/*     */         {
/* 113 */           throw new ParserInitializationException("Error initializing parser", e);
/*     */         } catch (ExceptionInInitializerError e) {
/* 115 */           throw new ParserInitializationException("Error initializing parser", e);
/*     */         }
/*     */       }
/*     */       catch (ClassNotFoundException e) {}
/*     */     }
/*     */     
/*     */ 
/* 122 */     if (parser == null) {
/* 123 */       String ukey = key.toUpperCase(Locale.ENGLISH);
/* 124 */       if (ukey.indexOf("UNIX_LTRIM") >= 0)
/*     */       {
/* 126 */         parser = new UnixFTPEntryParser(config, true);
/*     */ 
/*     */       }
/* 129 */       else if (ukey.indexOf("UNIX") >= 0)
/*     */       {
/* 131 */         parser = new UnixFTPEntryParser(config, false);
/*     */       }
/* 133 */       else if (ukey.indexOf("VMS") >= 0)
/*     */       {
/* 135 */         parser = new VMSVersioningFTPEntryParser(config);
/*     */       }
/* 137 */       else if (ukey.indexOf("WINDOWS") >= 0)
/*     */       {
/* 139 */         parser = createNTFTPEntryParser(config);
/*     */       }
/* 141 */       else if (ukey.indexOf("OS/2") >= 0)
/*     */       {
/* 143 */         parser = new OS2FTPEntryParser(config);
/*     */       }
/* 145 */       else if ((ukey.indexOf("OS/400") >= 0) || (ukey.indexOf("AS/400") >= 0))
/*     */       {
/*     */ 
/* 148 */         parser = createOS400FTPEntryParser(config);
/*     */       }
/* 150 */       else if (ukey.indexOf("MVS") >= 0)
/*     */       {
/* 152 */         parser = new MVSFTPEntryParser();
/*     */       }
/* 154 */       else if (ukey.indexOf("NETWARE") >= 0)
/*     */       {
/* 156 */         parser = new NetwareFTPEntryParser(config);
/*     */       }
/* 158 */       else if (ukey.indexOf("MACOS PETER") >= 0)
/*     */       {
/* 160 */         parser = new MacOsPeterFTPEntryParser(config);
/*     */       }
/* 162 */       else if (ukey.indexOf("TYPE: L8") >= 0)
/*     */       {
/*     */ 
/*     */ 
/* 166 */         parser = new UnixFTPEntryParser(config);
/*     */       }
/*     */       else
/*     */       {
/* 170 */         throw new ParserInitializationException("Unknown parser type: " + key);
/*     */       }
/*     */     }
/*     */     
/* 174 */     if ((parser instanceof Configurable)) {
/* 175 */       ((Configurable)parser).configure(config);
/*     */     }
/* 177 */     return parser;
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
/*     */   public FTPFileEntryParser createFileEntryParser(FTPClientConfig config)
/*     */     throws ParserInitializationException
/*     */   {
/* 205 */     String key = config.getServerSystemKey();
/* 206 */     return createFileEntryParser(key, config);
/*     */   }
/*     */   
/*     */ 
/*     */   public FTPFileEntryParser createUnixFTPEntryParser()
/*     */   {
/* 212 */     return new UnixFTPEntryParser();
/*     */   }
/*     */   
/*     */   public FTPFileEntryParser createVMSVersioningFTPEntryParser()
/*     */   {
/* 217 */     return new VMSVersioningFTPEntryParser();
/*     */   }
/*     */   
/*     */   public FTPFileEntryParser createNetwareFTPEntryParser() {
/* 221 */     return new NetwareFTPEntryParser();
/*     */   }
/*     */   
/*     */   public FTPFileEntryParser createNTFTPEntryParser()
/*     */   {
/* 226 */     return createNTFTPEntryParser(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private FTPFileEntryParser createNTFTPEntryParser(FTPClientConfig config)
/*     */   {
/* 238 */     if ((config != null) && ("WINDOWS".equals(config.getServerSystemKey())))
/*     */     {
/*     */ 
/* 241 */       return new NTFTPEntryParser(config);
/*     */     }
/*     */     
/* 244 */     FTPClientConfig config2 = config != null ? new FTPClientConfig(config) : null;
/* 245 */     return new CompositeFileEntryParser(new FTPFileEntryParser[] { new NTFTPEntryParser(config), new UnixFTPEntryParser(config2, (config2 != null) && ("UNIX_LTRIM".equals(config2.getServerSystemKey()))) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FTPFileEntryParser createOS2FTPEntryParser()
/*     */   {
/* 256 */     return new OS2FTPEntryParser();
/*     */   }
/*     */   
/*     */   public FTPFileEntryParser createOS400FTPEntryParser()
/*     */   {
/* 261 */     return createOS400FTPEntryParser(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private FTPFileEntryParser createOS400FTPEntryParser(FTPClientConfig config)
/*     */   {
/* 273 */     if ((config != null) && ("OS/400".equals(config.getServerSystemKey())))
/*     */     {
/*     */ 
/* 276 */       return new OS400FTPEntryParser(config);
/*     */     }
/*     */     
/* 279 */     FTPClientConfig config2 = config != null ? new FTPClientConfig(config) : null;
/* 280 */     return new CompositeFileEntryParser(new FTPFileEntryParser[] { new OS400FTPEntryParser(config), new UnixFTPEntryParser(config2, (config2 != null) && ("UNIX_LTRIM".equals(config2.getServerSystemKey()))) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FTPFileEntryParser createMVSEntryParser()
/*     */   {
/* 291 */     return new MVSFTPEntryParser();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\ftp\parser\DefaultFTPFileEntryParserFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */