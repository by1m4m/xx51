/*    */ package org.apache.http.impl.cookie;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.apache.http.HeaderElement;
/*    */ import org.apache.http.NameValuePair;
/*    */ import org.apache.http.ParseException;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.message.BasicHeaderElement;
/*    */ import org.apache.http.message.BasicHeaderValueParser;
/*    */ import org.apache.http.message.ParserCursor;
/*    */ import org.apache.http.util.CharArrayBuffer;
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
/*    */ @Immutable
/*    */ public class NetscapeDraftHeaderParser
/*    */ {
/* 50 */   public static final NetscapeDraftHeaderParser DEFAULT = new NetscapeDraftHeaderParser();
/*    */   
/* 52 */   private static final char[] DELIMITERS = { ';' };
/*    */   
/*    */   private final BasicHeaderValueParser nvpParser;
/*    */   
/*    */   public NetscapeDraftHeaderParser()
/*    */   {
/* 58 */     this.nvpParser = BasicHeaderValueParser.DEFAULT;
/*    */   }
/*    */   
/*    */   public HeaderElement parseHeader(CharArrayBuffer buffer, ParserCursor cursor)
/*    */     throws ParseException
/*    */   {
/* 64 */     if (buffer == null) {
/* 65 */       throw new IllegalArgumentException("Char array buffer may not be null");
/*    */     }
/* 67 */     if (cursor == null) {
/* 68 */       throw new IllegalArgumentException("Parser cursor may not be null");
/*    */     }
/* 70 */     NameValuePair nvp = this.nvpParser.parseNameValuePair(buffer, cursor, DELIMITERS);
/* 71 */     List<NameValuePair> params = new ArrayList();
/* 72 */     while (!cursor.atEnd()) {
/* 73 */       NameValuePair param = this.nvpParser.parseNameValuePair(buffer, cursor, DELIMITERS);
/* 74 */       params.add(param);
/*    */     }
/* 76 */     return new BasicHeaderElement(nvp.getName(), nvp.getValue(), (NameValuePair[])params.toArray(new NameValuePair[params.size()]));
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\impl\cookie\NetscapeDraftHeaderParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */