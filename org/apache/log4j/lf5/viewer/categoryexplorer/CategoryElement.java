/*    */ package org.apache.log4j.lf5.viewer.categoryexplorer;
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
/*    */ public class CategoryElement
/*    */ {
/*    */   protected String _categoryTitle;
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
/*    */   public CategoryElement() {}
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
/*    */   public CategoryElement(String title)
/*    */   {
/* 51 */     this._categoryTitle = title;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getTitle()
/*    */   {
/* 59 */     return this._categoryTitle;
/*    */   }
/*    */   
/*    */   public void setTitle(String title) {
/* 63 */     this._categoryTitle = title;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\log4j\lf5\viewer\categoryexplorer\CategoryElement.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */