/*    */ package org.eclipse.jetty.util;
/*    */ 
/*    */ import java.util.Set;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IncludeExclude<ITEM>
/*    */   extends IncludeExcludeSet<ITEM, ITEM>
/*    */ {
/*    */   public IncludeExclude() {}
/*    */   
/*    */   public <SET extends Set<ITEM>> IncludeExclude(Class<SET> setClass)
/*    */   {
/* 41 */     super(setClass);
/*    */   }
/*    */   
/*    */ 
/*    */   public <SET extends Set<ITEM>> IncludeExclude(Set<ITEM> includeSet, Predicate<ITEM> includePredicate, Set<ITEM> excludeSet, Predicate<ITEM> excludePredicate)
/*    */   {
/* 47 */     super(includeSet, includePredicate, excludeSet, excludePredicate);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\IncludeExclude.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */