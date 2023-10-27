/*    */ package com.fasterxml.jackson.databind.deser;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
/*    */ import com.fasterxml.jackson.core.JsonLocation;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class UnresolvedForwardReference
/*    */   extends JsonMappingException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private ReadableObjectId _roid;
/*    */   private List<UnresolvedId> _unresolvedIds;
/*    */   
/*    */   public UnresolvedForwardReference(String msg, JsonLocation loc, ReadableObjectId roid)
/*    */   {
/* 24 */     super(msg, loc);
/* 25 */     this._roid = roid;
/*    */   }
/*    */   
/*    */   public UnresolvedForwardReference(String msg)
/*    */   {
/* 30 */     super(msg);
/* 31 */     this._unresolvedIds = new ArrayList();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ReadableObjectId getRoid()
/*    */   {
/* 39 */     return this._roid;
/*    */   }
/*    */   
/*    */   public Object getUnresolvedId() {
/* 43 */     return this._roid.getKey().key;
/*    */   }
/*    */   
/*    */   public void addUnresolvedId(Object id, Class<?> type, JsonLocation where) {
/* 47 */     this._unresolvedIds.add(new UnresolvedId(id, type, where));
/*    */   }
/*    */   
/*    */   public List<UnresolvedId> getUnresolvedIds() {
/* 51 */     return this._unresolvedIds;
/*    */   }
/*    */   
/*    */ 
/*    */   public String getMessage()
/*    */   {
/* 57 */     String msg = super.getMessage();
/* 58 */     if (this._unresolvedIds == null) {
/* 59 */       return msg;
/*    */     }
/*    */     
/* 62 */     StringBuilder sb = new StringBuilder(msg);
/* 63 */     Iterator<UnresolvedId> iterator = this._unresolvedIds.iterator();
/* 64 */     while (iterator.hasNext()) {
/* 65 */       UnresolvedId unresolvedId = (UnresolvedId)iterator.next();
/* 66 */       sb.append(unresolvedId.toString());
/* 67 */       if (iterator.hasNext()) {
/* 68 */         sb.append(", ");
/*    */       }
/*    */     }
/* 71 */     sb.append('.');
/* 72 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\UnresolvedForwardReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */