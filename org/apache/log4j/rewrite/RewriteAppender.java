/*     */ package org.apache.log4j.rewrite;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ import org.apache.log4j.Appender;
/*     */ import org.apache.log4j.AppenderSkeleton;
/*     */ import org.apache.log4j.helpers.AppenderAttachableImpl;
/*     */ import org.apache.log4j.spi.AppenderAttachable;
/*     */ import org.apache.log4j.spi.LoggingEvent;
/*     */ import org.apache.log4j.spi.OptionHandler;
/*     */ import org.apache.log4j.xml.DOMConfigurator;
/*     */ import org.apache.log4j.xml.UnrecognizedElementHandler;
/*     */ import org.w3c.dom.Element;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RewriteAppender
/*     */   extends AppenderSkeleton
/*     */   implements AppenderAttachable, UnrecognizedElementHandler
/*     */ {
/*     */   private RewritePolicy policy;
/*     */   private final AppenderAttachableImpl appenders;
/*     */   
/*     */   public RewriteAppender()
/*     */   {
/*  51 */     this.appenders = new AppenderAttachableImpl();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void append(LoggingEvent event)
/*     */   {
/*  58 */     LoggingEvent rewritten = event;
/*  59 */     if (this.policy != null) {
/*  60 */       rewritten = this.policy.rewrite(event);
/*     */     }
/*  62 */     if (rewritten != null) {
/*  63 */       synchronized (this.appenders) {
/*  64 */         this.appenders.appendLoopOnAppenders(rewritten);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addAppender(Appender newAppender)
/*     */   {
/*  75 */     synchronized (this.appenders) {
/*  76 */       this.appenders.addAppender(newAppender);
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public Enumeration getAllAppenders()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 9	org/apache/log4j/rewrite/RewriteAppender:appenders	Lorg/apache/log4j/helpers/AppenderAttachableImpl;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 9	org/apache/log4j/rewrite/RewriteAppender:appenders	Lorg/apache/log4j/helpers/AppenderAttachableImpl;
/*     */     //   11: invokevirtual 14	org/apache/log4j/helpers/AppenderAttachableImpl:getAllAppenders	()Ljava/util/Enumeration;
/*     */     //   14: aload_1
/*     */     //   15: monitorexit
/*     */     //   16: areturn
/*     */     //   17: astore_2
/*     */     //   18: aload_1
/*     */     //   19: monitorexit
/*     */     //   20: aload_2
/*     */     //   21: athrow
/*     */     // Line number table:
/*     */     //   Java source line #85	-> byte code offset #0
/*     */     //   Java source line #86	-> byte code offset #7
/*     */     //   Java source line #87	-> byte code offset #17
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	22	0	this	RewriteAppender
/*     */     //   5	14	1	Ljava/lang/Object;	Object
/*     */     //   17	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	16	17	finally
/*     */     //   17	20	17	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public Appender getAppender(String name)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 9	org/apache/log4j/rewrite/RewriteAppender:appenders	Lorg/apache/log4j/helpers/AppenderAttachableImpl;
/*     */     //   4: dup
/*     */     //   5: astore_2
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 9	org/apache/log4j/rewrite/RewriteAppender:appenders	Lorg/apache/log4j/helpers/AppenderAttachableImpl;
/*     */     //   11: aload_1
/*     */     //   12: invokevirtual 15	org/apache/log4j/helpers/AppenderAttachableImpl:getAppender	(Ljava/lang/String;)Lorg/apache/log4j/Appender;
/*     */     //   15: aload_2
/*     */     //   16: monitorexit
/*     */     //   17: areturn
/*     */     //   18: astore_3
/*     */     //   19: aload_2
/*     */     //   20: monitorexit
/*     */     //   21: aload_3
/*     */     //   22: athrow
/*     */     // Line number table:
/*     */     //   Java source line #97	-> byte code offset #0
/*     */     //   Java source line #98	-> byte code offset #7
/*     */     //   Java source line #99	-> byte code offset #18
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	23	0	this	RewriteAppender
/*     */     //   0	23	1	name	String
/*     */     //   5	15	2	Ljava/lang/Object;	Object
/*     */     //   18	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	17	18	finally
/*     */     //   18	21	18	finally
/*     */   }
/*     */   
/*     */   public void close()
/*     */   {
/* 108 */     this.closed = true;
/*     */     
/*     */ 
/*     */ 
/* 112 */     synchronized (this.appenders) {
/* 113 */       Enumeration iter = this.appenders.getAllAppenders();
/*     */       
/* 115 */       if (iter != null) {
/* 116 */         while (iter.hasMoreElements()) {
/* 117 */           Object next = iter.nextElement();
/*     */           
/* 119 */           if ((next instanceof Appender)) {
/* 120 */             ((Appender)next).close();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public boolean isAttached(Appender appender)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 9	org/apache/log4j/rewrite/RewriteAppender:appenders	Lorg/apache/log4j/helpers/AppenderAttachableImpl;
/*     */     //   4: dup
/*     */     //   5: astore_2
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 9	org/apache/log4j/rewrite/RewriteAppender:appenders	Lorg/apache/log4j/helpers/AppenderAttachableImpl;
/*     */     //   11: aload_1
/*     */     //   12: invokevirtual 21	org/apache/log4j/helpers/AppenderAttachableImpl:isAttached	(Lorg/apache/log4j/Appender;)Z
/*     */     //   15: aload_2
/*     */     //   16: monitorexit
/*     */     //   17: ireturn
/*     */     //   18: astore_3
/*     */     //   19: aload_2
/*     */     //   20: monitorexit
/*     */     //   21: aload_3
/*     */     //   22: athrow
/*     */     // Line number table:
/*     */     //   Java source line #133	-> byte code offset #0
/*     */     //   Java source line #134	-> byte code offset #7
/*     */     //   Java source line #135	-> byte code offset #18
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	23	0	this	RewriteAppender
/*     */     //   0	23	1	appender	Appender
/*     */     //   5	15	2	Ljava/lang/Object;	Object
/*     */     //   18	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	17	18	finally
/*     */     //   18	21	18	finally
/*     */   }
/*     */   
/*     */   public boolean requiresLayout()
/*     */   {
/* 142 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void removeAllAppenders()
/*     */   {
/* 149 */     synchronized (this.appenders) {
/* 150 */       this.appenders.removeAllAppenders();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeAppender(Appender appender)
/*     */   {
/* 159 */     synchronized (this.appenders) {
/* 160 */       this.appenders.removeAppender(appender);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeAppender(String name)
/*     */   {
/* 169 */     synchronized (this.appenders) {
/* 170 */       this.appenders.removeAppender(name);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setRewritePolicy(RewritePolicy rewritePolicy)
/*     */   {
/* 176 */     this.policy = rewritePolicy;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean parseUnrecognizedElement(Element element, Properties props)
/*     */     throws Exception
/*     */   {
/* 183 */     String nodeName = element.getNodeName();
/* 184 */     if ("rewritePolicy".equals(nodeName)) {
/* 185 */       Object rewritePolicy = DOMConfigurator.parseElement(element, props, RewritePolicy.class);
/*     */       
/*     */ 
/* 188 */       if (rewritePolicy != null) {
/* 189 */         if ((rewritePolicy instanceof OptionHandler)) {
/* 190 */           ((OptionHandler)rewritePolicy).activateOptions();
/*     */         }
/* 192 */         setRewritePolicy((RewritePolicy)rewritePolicy);
/*     */       }
/* 194 */       return true;
/*     */     }
/* 196 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\log4j\rewrite\RewriteAppender.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */