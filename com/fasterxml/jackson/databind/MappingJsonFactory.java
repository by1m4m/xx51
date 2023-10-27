/*    */ package com.fasterxml.jackson.databind;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonFactory;
/*    */ import com.fasterxml.jackson.core.format.InputAccessor;
/*    */ import com.fasterxml.jackson.core.format.MatchStrength;
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
/*    */ public class MappingJsonFactory
/*    */   extends JsonFactory
/*    */ {
/*    */   private static final long serialVersionUID = -6744103724013275513L;
/*    */   
/*    */   public MappingJsonFactory()
/*    */   {
/* 25 */     this(null);
/*    */   }
/*    */   
/*    */   public MappingJsonFactory(ObjectMapper mapper)
/*    */   {
/* 30 */     super(mapper);
/* 31 */     if (mapper == null) {
/* 32 */       setCodec(new ObjectMapper(this));
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final ObjectMapper getCodec()
/*    */   {
/* 41 */     return (ObjectMapper)this._objectCodec;
/*    */   }
/*    */   
/*    */ 
/*    */   public JsonFactory copy()
/*    */   {
/* 47 */     _checkInvalidCopy(MappingJsonFactory.class);
/*    */     
/* 49 */     return new MappingJsonFactory(null);
/*    */   }
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
/*    */   public String getFormatName()
/*    */   {
/* 67 */     return "JSON";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public MatchStrength hasFormat(InputAccessor acc)
/*    */     throws IOException
/*    */   {
/* 76 */     if (getClass() == MappingJsonFactory.class) {
/* 77 */       return hasJSONFormat(acc);
/*    */     }
/* 79 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\MappingJsonFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */