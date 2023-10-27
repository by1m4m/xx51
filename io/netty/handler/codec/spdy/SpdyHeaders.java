/*    */ package io.netty.handler.codec.spdy;
/*    */ 
/*    */ import io.netty.handler.codec.Headers;
/*    */ import io.netty.util.AsciiString;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map.Entry;
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
/*    */ public abstract interface SpdyHeaders
/*    */   extends Headers<CharSequence, CharSequence, SpdyHeaders>
/*    */ {
/*    */   public abstract String getAsString(CharSequence paramCharSequence);
/*    */   
/*    */   public abstract List<String> getAllAsString(CharSequence paramCharSequence);
/*    */   
/*    */   public abstract Iterator<Map.Entry<String, String>> iteratorAsString();
/*    */   
/*    */   public abstract boolean contains(CharSequence paramCharSequence1, CharSequence paramCharSequence2, boolean paramBoolean);
/*    */   
/*    */   public static final class HttpNames
/*    */   {
/* 38 */     public static final AsciiString HOST = AsciiString.cached(":host");
/*    */     
/*    */ 
/*    */ 
/* 42 */     public static final AsciiString METHOD = AsciiString.cached(":method");
/*    */     
/*    */ 
/*    */ 
/* 46 */     public static final AsciiString PATH = AsciiString.cached(":path");
/*    */     
/*    */ 
/*    */ 
/* 50 */     public static final AsciiString SCHEME = AsciiString.cached(":scheme");
/*    */     
/*    */ 
/*    */ 
/* 54 */     public static final AsciiString STATUS = AsciiString.cached(":status");
/*    */     
/*    */ 
/*    */ 
/* 58 */     public static final AsciiString VERSION = AsciiString.cached(":version");
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\spdy\SpdyHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */