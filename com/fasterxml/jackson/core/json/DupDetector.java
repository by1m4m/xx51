/*    */ package com.fasterxml.jackson.core.json;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.core.JsonLocation;
/*    */ import com.fasterxml.jackson.core.JsonParseException;
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import java.util.HashSet;
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
/*    */ public class DupDetector
/*    */ {
/*    */   protected final Object _source;
/*    */   protected String _firstName;
/*    */   protected String _secondName;
/*    */   protected HashSet<String> _seen;
/*    */   
/*    */   private DupDetector(Object src)
/*    */   {
/* 37 */     this._source = src;
/*    */   }
/*    */   
/*    */   public static DupDetector rootDetector(JsonParser p) {
/* 41 */     return new DupDetector(p);
/*    */   }
/*    */   
/*    */   public static DupDetector rootDetector(JsonGenerator g) {
/* 45 */     return new DupDetector(g);
/*    */   }
/*    */   
/*    */   public DupDetector child() {
/* 49 */     return new DupDetector(this._source);
/*    */   }
/*    */   
/*    */   public void reset() {
/* 53 */     this._firstName = null;
/* 54 */     this._secondName = null;
/* 55 */     this._seen = null;
/*    */   }
/*    */   
/*    */   public JsonLocation findLocation()
/*    */   {
/* 60 */     if ((this._source instanceof JsonParser)) {
/* 61 */       return ((JsonParser)this._source).getCurrentLocation();
/*    */     }
/*    */     
/* 64 */     return null;
/*    */   }
/*    */   
/*    */   public boolean isDup(String name) throws JsonParseException
/*    */   {
/* 69 */     if (this._firstName == null) {
/* 70 */       this._firstName = name;
/* 71 */       return false;
/*    */     }
/* 73 */     if (name.equals(this._firstName)) {
/* 74 */       return true;
/*    */     }
/* 76 */     if (this._secondName == null) {
/* 77 */       this._secondName = name;
/* 78 */       return false;
/*    */     }
/* 80 */     if (name.equals(this._secondName)) {
/* 81 */       return true;
/*    */     }
/* 83 */     if (this._seen == null) {
/* 84 */       this._seen = new HashSet(16);
/* 85 */       this._seen.add(this._firstName);
/* 86 */       this._seen.add(this._secondName);
/*    */     }
/* 88 */     return !this._seen.add(name);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\json\DupDetector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */