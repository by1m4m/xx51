/*     */ package org.eclipse.jetty.websocket.common.events.annotated;
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
/*     */ public class EventMethods
/*     */ {
/*     */   private Class<?> pojoClass;
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
/*  27 */   public EventMethod onConnect = null;
/*  28 */   public EventMethod onClose = null;
/*  29 */   public EventMethod onBinary = null;
/*  30 */   public EventMethod onText = null;
/*  31 */   public EventMethod onError = null;
/*  32 */   public EventMethod onFrame = null;
/*     */   
/*     */   public EventMethods(Class<?> pojoClass)
/*     */   {
/*  36 */     this.pojoClass = pojoClass;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/*  42 */     if (this == obj)
/*     */     {
/*  44 */       return true;
/*     */     }
/*  46 */     if (obj == null)
/*     */     {
/*  48 */       return false;
/*     */     }
/*  50 */     if (getClass() != obj.getClass())
/*     */     {
/*  52 */       return false;
/*     */     }
/*  54 */     EventMethods other = (EventMethods)obj;
/*  55 */     if (this.pojoClass == null)
/*     */     {
/*  57 */       if (other.pojoClass != null)
/*     */       {
/*  59 */         return false;
/*     */       }
/*     */     }
/*  62 */     else if (!this.pojoClass.getName().equals(other.pojoClass.getName()))
/*     */     {
/*  64 */       return false;
/*     */     }
/*  66 */     return true;
/*     */   }
/*     */   
/*     */   public Class<?> getPojoClass()
/*     */   {
/*  71 */     return this.pojoClass;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  77 */     int prime = 31;
/*  78 */     int result = 1;
/*  79 */     result = 31 * result + (this.pojoClass == null ? 0 : this.pojoClass.getName().hashCode());
/*  80 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/*  86 */     StringBuilder builder = new StringBuilder();
/*  87 */     builder.append("EventMethods [pojoClass=");
/*  88 */     builder.append(this.pojoClass);
/*  89 */     builder.append(", onConnect=");
/*  90 */     builder.append(this.onConnect);
/*  91 */     builder.append(", onClose=");
/*  92 */     builder.append(this.onClose);
/*  93 */     builder.append(", onBinary=");
/*  94 */     builder.append(this.onBinary);
/*  95 */     builder.append(", onText=");
/*  96 */     builder.append(this.onText);
/*  97 */     builder.append(", onException=");
/*  98 */     builder.append(this.onError);
/*  99 */     builder.append(", onFrame=");
/* 100 */     builder.append(this.onFrame);
/* 101 */     builder.append("]");
/* 102 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\events\annotated\EventMethods.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */