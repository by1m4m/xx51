/*    */ package org.eclipse.jetty.http;
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
/*    */ public class CompressedContentFormat
/*    */ {
/* 25 */   public static final CompressedContentFormat GZIP = new CompressedContentFormat("gzip", ".gz");
/* 26 */   public static final CompressedContentFormat BR = new CompressedContentFormat("br", ".br");
/* 27 */   public static final CompressedContentFormat[] NONE = new CompressedContentFormat[0];
/*    */   
/*    */   public final String _encoding;
/*    */   public final String _extension;
/*    */   public final String _etag;
/*    */   public final String _etagQuote;
/*    */   public final PreEncodedHttpField _contentEncoding;
/*    */   
/*    */   public CompressedContentFormat(String encoding, String extension)
/*    */   {
/* 37 */     this._encoding = encoding;
/* 38 */     this._extension = extension;
/* 39 */     this._etag = ("--" + encoding);
/* 40 */     this._etagQuote = (this._etag + "\"");
/* 41 */     this._contentEncoding = new PreEncodedHttpField(HttpHeader.CONTENT_ENCODING, encoding);
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 47 */     if (!(o instanceof CompressedContentFormat))
/* 48 */       return false;
/* 49 */     CompressedContentFormat ccf = (CompressedContentFormat)o;
/* 50 */     if ((this._encoding == null) && (ccf._encoding != null))
/* 51 */       return false;
/* 52 */     if ((this._extension == null) && (ccf._extension != null)) {
/* 53 */       return false;
/*    */     }
/* 55 */     return (this._encoding.equalsIgnoreCase(ccf._encoding)) && (this._extension.equalsIgnoreCase(ccf._extension));
/*    */   }
/*    */   
/*    */   public static boolean tagEquals(String etag, String tag)
/*    */   {
/* 60 */     if (etag.equals(tag)) {
/* 61 */       return true;
/*    */     }
/* 63 */     int dashdash = tag.indexOf("--");
/* 64 */     if ((dashdash > 0) && (dashdash == etag.length() - 1))
/* 65 */       return etag.regionMatches(0, tag, 0, dashdash);
/* 66 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\CompressedContentFormat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */