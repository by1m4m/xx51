/*    */ package com.fasterxml.jackson.databind.util;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ViewMatcher
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 11 */   protected static final ViewMatcher EMPTY = new ViewMatcher();
/*    */   
/* 13 */   public boolean isVisibleForView(Class<?> activeView) { return false; }
/*    */   
/*    */   public static ViewMatcher construct(Class<?>[] views)
/*    */   {
/* 17 */     if (views == null) {
/* 18 */       return EMPTY;
/*    */     }
/* 20 */     switch (views.length) {
/*    */     case 0: 
/* 22 */       return EMPTY;
/*    */     case 1: 
/* 24 */       return new Single(views[0]);
/*    */     }
/* 26 */     return new Multi(views);
/*    */   }
/*    */   
/*    */ 
/*    */   private static final class Single
/*    */     extends ViewMatcher
/*    */   {
/*    */     private static final long serialVersionUID = 1L;
/*    */     
/*    */     private final Class<?> _view;
/*    */     
/*    */ 
/*    */     public Single(Class<?> v)
/*    */     {
/* 40 */       this._view = v;
/*    */     }
/*    */     
/* 43 */     public boolean isVisibleForView(Class<?> activeView) { return (activeView == this._view) || (this._view.isAssignableFrom(activeView)); }
/*    */   }
/*    */   
/*    */   private static final class Multi
/*    */     extends ViewMatcher implements Serializable
/*    */   {
/*    */     private static final long serialVersionUID = 1L;
/*    */     private final Class<?>[] _views;
/*    */     
/*    */     public Multi(Class<?>[] v)
/*    */     {
/* 54 */       this._views = v;
/*    */     }
/*    */     
/*    */     public boolean isVisibleForView(Class<?> activeView)
/*    */     {
/* 59 */       int i = 0; for (int len = this._views.length; i < len; i++) {
/* 60 */         Class<?> view = this._views[i];
/* 61 */         if ((activeView == view) || (view.isAssignableFrom(activeView))) {
/* 62 */           return true;
/*    */         }
/*    */       }
/* 65 */       return false;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\util\ViewMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */