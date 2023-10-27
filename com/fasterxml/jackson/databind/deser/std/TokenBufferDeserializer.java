/*    */ package com.fasterxml.jackson.databind.deser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*    */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*    */ import java.io.IOException;
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
/*    */ @JacksonStdImpl
/*    */ public class TokenBufferDeserializer
/*    */   extends StdScalarDeserializer<TokenBuffer>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public TokenBufferDeserializer()
/*    */   {
/* 27 */     super(TokenBuffer.class);
/*    */   }
/*    */   
/*    */   public TokenBuffer deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
/* 31 */     return createBufferInstance(jp).deserialize(jp, ctxt);
/*    */   }
/*    */   
/*    */   protected TokenBuffer createBufferInstance(JsonParser jp) {
/* 35 */     return new TokenBuffer(jp);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\TokenBufferDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */