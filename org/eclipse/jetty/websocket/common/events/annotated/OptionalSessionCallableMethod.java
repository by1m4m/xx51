/*    */ package org.eclipse.jetty.websocket.common.events.annotated;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.io.Reader;
/*    */ import java.lang.reflect.Method;
/*    */ import org.eclipse.jetty.websocket.api.Session;
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
/*    */ public class OptionalSessionCallableMethod
/*    */   extends CallableMethod
/*    */ {
/*    */   private final boolean wantsSession;
/*    */   private final boolean streaming;
/*    */   
/*    */   public OptionalSessionCallableMethod(Class<?> pojo, Method method)
/*    */   {
/* 37 */     super(pojo, method);
/*    */     
/* 39 */     boolean foundConnection = false;
/* 40 */     boolean foundStreaming = false;
/*    */     
/* 42 */     if (this.paramTypes != null)
/*    */     {
/* 44 */       for (Class<?> paramType : this.paramTypes)
/*    */       {
/* 46 */         if (Session.class.isAssignableFrom(paramType))
/*    */         {
/* 48 */           foundConnection = true;
/*    */         }
/* 50 */         if ((Reader.class.isAssignableFrom(paramType)) || (InputStream.class.isAssignableFrom(paramType)))
/*    */         {
/* 52 */           foundStreaming = true;
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 57 */     this.wantsSession = foundConnection;
/* 58 */     this.streaming = foundStreaming;
/*    */   }
/*    */   
/*    */   public void call(Object obj, Session connection, Object... args)
/*    */   {
/* 63 */     if (this.wantsSession)
/*    */     {
/* 65 */       Object[] fullArgs = new Object[args.length + 1];
/* 66 */       fullArgs[0] = connection;
/* 67 */       System.arraycopy(args, 0, fullArgs, 1, args.length);
/* 68 */       call(obj, fullArgs);
/*    */     }
/*    */     else
/*    */     {
/* 72 */       call(obj, args);
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean isSessionAware()
/*    */   {
/* 78 */     return this.wantsSession;
/*    */   }
/*    */   
/*    */   public boolean isStreaming()
/*    */   {
/* 83 */     return this.streaming;
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 89 */     return String.format("%s[%s]", new Object[] { getClass().getSimpleName(), this.method.toGenericString() });
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\events\annotated\OptionalSessionCallableMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */