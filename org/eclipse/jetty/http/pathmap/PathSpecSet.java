/*    */ package org.eclipse.jetty.http.pathmap;
/*    */ 
/*    */ import java.util.AbstractSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.function.Predicate;
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
/*    */ public class PathSpecSet
/*    */   extends AbstractSet<String>
/*    */   implements Predicate<String>
/*    */ {
/* 32 */   private final PathMappings<Boolean> specs = new PathMappings();
/*    */   
/*    */ 
/*    */   public boolean test(String s)
/*    */   {
/* 37 */     return this.specs.getMatch(s) != null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public int size()
/*    */   {
/* 44 */     return this.specs.size();
/*    */   }
/*    */   
/*    */   private PathSpec asPathSpec(Object o)
/*    */   {
/* 49 */     if (o == null)
/*    */     {
/* 51 */       return null;
/*    */     }
/* 53 */     if ((o instanceof PathSpec))
/*    */     {
/* 55 */       return (PathSpec)o;
/*    */     }
/* 57 */     if ((o instanceof String))
/*    */     {
/* 59 */       return PathMappings.asPathSpec((String)o);
/*    */     }
/* 61 */     return PathMappings.asPathSpec(o.toString());
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean add(String s)
/*    */   {
/* 67 */     return this.specs.put(PathMappings.asPathSpec(s), Boolean.TRUE);
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean remove(Object o)
/*    */   {
/* 73 */     return this.specs.remove(asPathSpec(o));
/*    */   }
/*    */   
/*    */ 
/*    */   public void clear()
/*    */   {
/* 79 */     this.specs.reset();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Iterator<String> iterator()
/*    */   {
/* 86 */     final Iterator<MappedResource<Boolean>> iterator = this.specs.iterator();
/* 87 */     new Iterator()
/*    */     {
/*    */ 
/*    */       public boolean hasNext()
/*    */       {
/* 92 */         return iterator.hasNext();
/*    */       }
/*    */       
/*    */ 
/*    */       public String next()
/*    */       {
/* 98 */         return ((MappedResource)iterator.next()).getPathSpec().getDeclaration();
/*    */       }
/*    */     };
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\pathmap\PathSpecSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */