/*     */ package com.fasterxml.jackson.core.util;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultIndenter
/*     */   extends DefaultPrettyPrinter.NopIndenter
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public static final String SYS_LF;
/*     */   
/*     */   static
/*     */   {
/*     */     String lf;
/*     */     try
/*     */     {
/*  22 */       lf = System.getProperty("line.separator");
/*     */     } catch (Throwable t) {
/*  24 */       lf = "\n";
/*     */     }
/*  26 */     SYS_LF = lf;
/*     */   }
/*     */   
/*  29 */   public static final DefaultIndenter SYSTEM_LINEFEED_INSTANCE = new DefaultIndenter("  ", SYS_LF);
/*     */   
/*     */   private static final int INDENT_LEVELS = 16;
/*     */   
/*     */   private final char[] indents;
/*     */   
/*     */   private final int charsPerLevel;
/*     */   
/*     */   private final String eol;
/*     */   
/*     */ 
/*     */   public DefaultIndenter()
/*     */   {
/*  42 */     this("  ", SYS_LF);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultIndenter(String indent, String eol)
/*     */   {
/*  50 */     this.charsPerLevel = indent.length();
/*     */     
/*  52 */     this.indents = new char[indent.length() * 16];
/*  53 */     int offset = 0;
/*  54 */     for (int i = 0; i < 16; i++) {
/*  55 */       indent.getChars(0, indent.length(), this.indents, offset);
/*  56 */       offset += indent.length();
/*     */     }
/*     */     
/*  59 */     this.eol = eol;
/*     */   }
/*     */   
/*     */   public DefaultIndenter withLinefeed(String lf)
/*     */   {
/*  64 */     if (lf.equals(this.eol)) {
/*  65 */       return this;
/*     */     }
/*  67 */     return new DefaultIndenter(getIndent(), lf);
/*     */   }
/*     */   
/*     */   public DefaultIndenter withIndent(String indent)
/*     */   {
/*  72 */     if (indent.equals(getIndent())) {
/*  73 */       return this;
/*     */     }
/*  75 */     return new DefaultIndenter(indent, this.eol);
/*     */   }
/*     */   
/*     */   public boolean isInline() {
/*  79 */     return false;
/*     */   }
/*     */   
/*     */   public void writeIndentation(JsonGenerator jg, int level) throws IOException
/*     */   {
/*  84 */     jg.writeRaw(this.eol);
/*  85 */     if (level > 0) {
/*  86 */       level *= this.charsPerLevel;
/*  87 */       while (level > this.indents.length) {
/*  88 */         jg.writeRaw(this.indents, 0, this.indents.length);
/*  89 */         level -= this.indents.length;
/*     */       }
/*  91 */       jg.writeRaw(this.indents, 0, level);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getEol() {
/*  96 */     return this.eol;
/*     */   }
/*     */   
/*     */   public String getIndent() {
/* 100 */     return new String(this.indents, 0, this.charsPerLevel);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\util\DefaultIndenter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */