/*    */ package io.netty.handler.codec.stomp;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract interface StompHeaders
/*    */   extends Headers<CharSequence, CharSequence, StompHeaders>
/*    */ {
/* 31 */   public static final AsciiString ACCEPT_VERSION = AsciiString.cached("accept-version");
/* 32 */   public static final AsciiString HOST = AsciiString.cached("host");
/* 33 */   public static final AsciiString LOGIN = AsciiString.cached("login");
/* 34 */   public static final AsciiString PASSCODE = AsciiString.cached("passcode");
/* 35 */   public static final AsciiString HEART_BEAT = AsciiString.cached("heart-beat");
/* 36 */   public static final AsciiString VERSION = AsciiString.cached("version");
/* 37 */   public static final AsciiString SESSION = AsciiString.cached("session");
/* 38 */   public static final AsciiString SERVER = AsciiString.cached("server");
/* 39 */   public static final AsciiString DESTINATION = AsciiString.cached("destination");
/* 40 */   public static final AsciiString ID = AsciiString.cached("id");
/* 41 */   public static final AsciiString ACK = AsciiString.cached("ack");
/* 42 */   public static final AsciiString TRANSACTION = AsciiString.cached("transaction");
/* 43 */   public static final AsciiString RECEIPT = AsciiString.cached("receipt");
/* 44 */   public static final AsciiString MESSAGE_ID = AsciiString.cached("message-id");
/* 45 */   public static final AsciiString SUBSCRIPTION = AsciiString.cached("subscription");
/* 46 */   public static final AsciiString RECEIPT_ID = AsciiString.cached("receipt-id");
/* 47 */   public static final AsciiString MESSAGE = AsciiString.cached("message");
/* 48 */   public static final AsciiString CONTENT_LENGTH = AsciiString.cached("content-length");
/* 49 */   public static final AsciiString CONTENT_TYPE = AsciiString.cached("content-type");
/*    */   
/*    */   public abstract String getAsString(CharSequence paramCharSequence);
/*    */   
/*    */   public abstract List<String> getAllAsString(CharSequence paramCharSequence);
/*    */   
/*    */   public abstract Iterator<Map.Entry<String, String>> iteratorAsString();
/*    */   
/*    */   public abstract boolean contains(CharSequence paramCharSequence1, CharSequence paramCharSequence2, boolean paramBoolean);
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\stomp\StompHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */