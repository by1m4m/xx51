/*     */ package com.sun.jna.platform.dnd;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Point;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.UnsupportedFlavorException;
/*     */ import java.awt.dnd.DropTarget;
/*     */ import java.awt.dnd.DropTargetDragEvent;
/*     */ import java.awt.dnd.DropTargetDropEvent;
/*     */ import java.awt.dnd.DropTargetEvent;
/*     */ import java.awt.dnd.DropTargetListener;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public abstract class DropHandler
/*     */   implements DropTargetListener
/*     */ {
/*     */   private int acceptedActions;
/*     */   private List<DataFlavor> acceptedFlavors;
/*     */   private DropTarget dropTarget;
/*  92 */   private boolean active = true;
/*     */   
/*     */ 
/*     */   private DropTargetPainter painter;
/*     */   
/*     */   private String lastAction;
/*     */   
/*     */ 
/*     */   public DropHandler(Component c, int acceptedActions)
/*     */   {
/* 102 */     this(c, acceptedActions, new DataFlavor[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DropHandler(Component c, int acceptedActions, DataFlavor[] acceptedFlavors)
/*     */   {
/* 113 */     this(c, acceptedActions, acceptedFlavors, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DropHandler(Component c, int acceptedActions, DataFlavor[] acceptedFlavors, DropTargetPainter painter)
/*     */   {
/* 126 */     this.acceptedActions = acceptedActions;
/* 127 */     this.acceptedFlavors = Arrays.asList(acceptedFlavors);
/* 128 */     this.painter = painter;
/* 129 */     this.dropTarget = new DropTarget(c, acceptedActions, this, this.active);
/*     */   }
/*     */   
/*     */   protected DropTarget getDropTarget() {
/* 133 */     return this.dropTarget;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isActive()
/*     */   {
/* 139 */     return this.active;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setActive(boolean active)
/*     */   {
/* 146 */     this.active = active;
/* 147 */     if (this.dropTarget != null) {
/* 148 */       this.dropTarget.setActive(active);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getDropActionsForFlavors(DataFlavor[] dataFlavors)
/*     */   {
/* 162 */     return this.acceptedActions;
/*     */   }
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
/*     */   protected int getDropAction(DropTargetEvent e)
/*     */   {
/* 181 */     int currentAction = 0;
/* 182 */     int sourceActions = 0;
/* 183 */     Point location = null;
/* 184 */     DataFlavor[] flavors = new DataFlavor[0];
/* 185 */     if ((e instanceof DropTargetDragEvent)) {
/* 186 */       DropTargetDragEvent ev = (DropTargetDragEvent)e;
/* 187 */       currentAction = ev.getDropAction();
/* 188 */       sourceActions = ev.getSourceActions();
/* 189 */       flavors = ev.getCurrentDataFlavors();
/* 190 */       location = ev.getLocation();
/*     */     }
/* 192 */     else if ((e instanceof DropTargetDropEvent)) {
/* 193 */       DropTargetDropEvent ev = (DropTargetDropEvent)e;
/* 194 */       currentAction = ev.getDropAction();
/* 195 */       sourceActions = ev.getSourceActions();
/* 196 */       flavors = ev.getCurrentDataFlavors();
/* 197 */       location = ev.getLocation();
/*     */     }
/* 199 */     if (isSupported(flavors)) {
/* 200 */       int availableActions = getDropActionsForFlavors(flavors);
/* 201 */       currentAction = getDropAction(e, currentAction, sourceActions, availableActions);
/* 202 */       if ((currentAction != 0) && 
/* 203 */         (canDrop(e, currentAction, location))) {
/* 204 */         return currentAction;
/*     */       }
/*     */     }
/*     */     
/* 208 */     return 0;
/*     */   }
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
/*     */   protected int getDropAction(DropTargetEvent e, int currentAction, int sourceActions, int acceptedActions)
/*     */   {
/* 229 */     boolean modifiersActive = modifiersActive(currentAction);
/* 230 */     if (((currentAction & acceptedActions) == 0) && (!modifiersActive))
/*     */     {
/* 232 */       int action = acceptedActions & sourceActions;
/* 233 */       currentAction = action;
/*     */     }
/* 235 */     else if (modifiersActive) {
/* 236 */       int action = currentAction & acceptedActions & sourceActions;
/* 237 */       if (action != currentAction) {
/* 238 */         currentAction = action;
/*     */       }
/*     */     }
/* 241 */     return currentAction;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean modifiersActive(int dropAction)
/*     */   {
/* 252 */     int mods = DragHandler.getModifiers();
/* 253 */     if (mods == -1) {
/* 254 */       if ((dropAction == 1073741824) || (dropAction == 1))
/*     */       {
/* 256 */         return true;
/*     */       }
/*     */       
/*     */ 
/* 260 */       return false;
/*     */     }
/* 262 */     return mods != 0;
/*     */   }
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
/*     */   private void describe(String type, DropTargetEvent e) {}
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
/*     */   protected int acceptOrReject(DropTargetDragEvent e)
/*     */   {
/* 297 */     int action = getDropAction(e);
/* 298 */     if (action != 0)
/*     */     {
/*     */ 
/* 301 */       e.acceptDrag(action);
/*     */     }
/*     */     else {
/* 304 */       e.rejectDrag();
/*     */     }
/* 306 */     return action;
/*     */   }
/*     */   
/*     */   public void dragEnter(DropTargetDragEvent e) {
/* 310 */     describe("enter(tgt)", e);
/* 311 */     int action = acceptOrReject(e);
/* 312 */     paintDropTarget(e, action, e.getLocation());
/*     */   }
/*     */   
/*     */   public void dragOver(DropTargetDragEvent e) {
/* 316 */     describe("over(tgt)", e);
/* 317 */     int action = acceptOrReject(e);
/* 318 */     paintDropTarget(e, action, e.getLocation());
/*     */   }
/*     */   
/*     */   public void dragExit(DropTargetEvent e) {
/* 322 */     describe("exit(tgt)", e);
/* 323 */     paintDropTarget(e, 0, null);
/*     */   }
/*     */   
/*     */   public void dropActionChanged(DropTargetDragEvent e) {
/* 327 */     describe("change(tgt)", e);
/* 328 */     int action = acceptOrReject(e);
/* 329 */     paintDropTarget(e, action, e.getLocation());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drop(DropTargetDropEvent e)
/*     */   {
/* 337 */     describe("drop(tgt)", e);
/* 338 */     int action = getDropAction(e);
/* 339 */     if (action != 0) {
/* 340 */       e.acceptDrop(action);
/*     */       try {
/* 342 */         drop(e, action);
/*     */         
/* 344 */         e.dropComplete(true);
/*     */       }
/*     */       catch (Exception ex) {
/* 347 */         e.dropComplete(false);
/*     */       }
/*     */     }
/*     */     else {
/* 351 */       e.rejectDrop();
/*     */     }
/* 353 */     paintDropTarget(e, 0, e.getLocation());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isSupported(DataFlavor[] flavors)
/*     */   {
/* 363 */     Set<DataFlavor> set = new HashSet(Arrays.asList(flavors));
/* 364 */     set.retainAll(this.acceptedFlavors);
/* 365 */     return !set.isEmpty();
/*     */   }
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
/*     */   protected void paintDropTarget(DropTargetEvent e, int action, Point location)
/*     */   {
/* 382 */     if (this.painter != null) {
/* 383 */       this.painter.paintDropTarget(e, action, location);
/*     */     }
/*     */   }
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
/*     */   protected boolean canDrop(DropTargetEvent e, int action, Point location)
/*     */   {
/* 398 */     return true;
/*     */   }
/*     */   
/*     */   protected abstract void drop(DropTargetDropEvent paramDropTargetDropEvent, int paramInt)
/*     */     throws UnsupportedFlavorException, IOException;
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\platform\dnd\DropHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */