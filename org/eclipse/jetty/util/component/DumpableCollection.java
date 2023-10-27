/*    */ package org.eclipse.jetty.util.component;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Collection;
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
/*    */ public class DumpableCollection
/*    */   implements Dumpable
/*    */ {
/*    */   private final String _name;
/*    */   private final Collection<?> _collection;
/*    */   
/*    */   public DumpableCollection(String name, Collection<?> collection)
/*    */   {
/* 31 */     this._name = name;
/* 32 */     this._collection = collection;
/*    */   }
/*    */   
/*    */ 
/*    */   public String dump()
/*    */   {
/* 38 */     return ContainerLifeCycle.dump(this);
/*    */   }
/*    */   
/*    */   public void dump(Appendable out, String indent)
/*    */     throws IOException
/*    */   {
/* 44 */     out.append(this._name).append(System.lineSeparator());
/* 45 */     if (this._collection != null) {
/* 46 */       ContainerLifeCycle.dump(out, indent, new Collection[] { this._collection });
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\component\DumpableCollection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */