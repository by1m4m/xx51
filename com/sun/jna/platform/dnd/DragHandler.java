/*     */ package com.sun.jna.platform.dnd;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Component;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.Image;
/*     */ import java.awt.Point;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.awt.dnd.DragGestureEvent;
/*     */ import java.awt.dnd.DragGestureListener;
/*     */ import java.awt.dnd.DragSource;
/*     */ import java.awt.dnd.DragSourceContext;
/*     */ import java.awt.dnd.DragSourceDragEvent;
/*     */ import java.awt.dnd.DragSourceDropEvent;
/*     */ import java.awt.dnd.DragSourceEvent;
/*     */ import java.awt.dnd.DragSourceListener;
/*     */ import java.awt.dnd.DragSourceMotionListener;
/*     */ import java.awt.dnd.DropTargetDragEvent;
/*     */ import java.awt.dnd.DropTargetDropEvent;
/*     */ import java.awt.dnd.DropTargetEvent;
/*     */ import java.awt.dnd.InvalidDnDOperationException;
/*     */ import java.awt.event.InputEvent;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JColorChooser;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.text.JTextComponent;
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
/*     */ public abstract class DragHandler
/*     */   implements DragSourceListener, DragSourceMotionListener, DragGestureListener
/*     */ {
/* 103 */   public static final Dimension MAX_GHOST_SIZE = new Dimension(250, 250);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final float DEFAULT_GHOST_ALPHA = 0.5F;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int UNKNOWN_MODIFIERS = -1;
/*     */   
/*     */ 
/*     */ 
/* 116 */   public static final Transferable UNKNOWN_TRANSFERABLE = null;
/*     */   
/*     */ 
/*     */   protected static final int MOVE = 2;
/*     */   
/*     */ 
/*     */   protected static final int COPY = 1;
/*     */   
/*     */ 
/*     */   protected static final int LINK = 1073741824;
/*     */   
/*     */   protected static final int NONE = 0;
/*     */   
/*     */   static final int MOVE_MASK = 64;
/*     */   
/* 131 */   static final boolean OSX = System.getProperty("os.name").toLowerCase().indexOf("mac") != -1;
/*     */   
/* 133 */   static final int COPY_MASK = OSX ? 512 : 128;
/*     */   
/*     */ 
/* 136 */   static final int LINK_MASK = OSX ? 768 : 192;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static final int KEY_MASK = 9152;
/*     */   
/*     */ 
/*     */ 
/* 145 */   private static int modifiers = -1;
/* 146 */   private static Transferable transferable = UNKNOWN_TRANSFERABLE;
/*     */   
/*     */ 
/*     */   private int supportedActions;
/*     */   
/*     */ 
/*     */ 
/*     */   static int getModifiers()
/*     */   {
/* 155 */     return modifiers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Transferable getTransferable(DropTargetEvent e)
/*     */   {
/* 165 */     if ((e instanceof DropTargetDragEvent))
/*     */     {
/*     */       try
/*     */       {
/* 169 */         return (Transferable)e.getClass().getMethod("getTransferable", (Class[])null).invoke(e, (Object[])null);
/*     */ 
/*     */ 
/*     */       }
/*     */       catch (Exception ex) {}
/*     */     }
/* 175 */     else if ((e instanceof DropTargetDropEvent)) {
/* 176 */       return ((DropTargetDropEvent)e).getTransferable();
/*     */     }
/* 178 */     return transferable;
/*     */   }
/*     */   
/*     */ 
/* 182 */   private boolean fixCursor = true;
/*     */   private Component dragSource;
/*     */   private GhostedDragImage ghost;
/*     */   private Point imageOffset;
/* 186 */   private Dimension maxGhostSize = MAX_GHOST_SIZE;
/* 187 */   private float ghostAlpha = 0.5F;
/*     */   
/*     */   private String lastAction;
/*     */   
/*     */   private boolean moved;
/*     */   
/*     */   protected DragHandler(Component dragSource, int actions)
/*     */   {
/* 195 */     this.dragSource = dragSource;
/* 196 */     this.supportedActions = actions;
/*     */     try {
/* 198 */       String alpha = System.getProperty("DragHandler.alpha");
/* 199 */       if (alpha != null) {
/*     */         try {
/* 201 */           this.ghostAlpha = Float.parseFloat(alpha);
/*     */         }
/*     */         catch (NumberFormatException e) {}
/*     */       }
/* 205 */       String max = System.getProperty("DragHandler.maxDragImageSize");
/* 206 */       if (max != null) {
/* 207 */         String[] size = max.split("x");
/* 208 */         if (size.length == 2) {
/*     */           try
/*     */           {
/* 211 */             this.maxGhostSize = new Dimension(Integer.parseInt(size[0]), Integer.parseInt(size[1]));
/*     */           }
/*     */           catch (NumberFormatException e) {}
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (SecurityException e) {}
/*     */     
/* 219 */     disableSwingDragSupport(dragSource);
/* 220 */     DragSource src = DragSource.getDefaultDragSource();
/* 221 */     src.createDefaultDragGestureRecognizer(dragSource, this.supportedActions, this);
/*     */   }
/*     */   
/*     */   private void disableSwingDragSupport(Component comp) {
/* 225 */     if ((comp instanceof JTree)) {
/* 226 */       ((JTree)comp).setDragEnabled(false);
/*     */     }
/* 228 */     else if ((comp instanceof JList)) {
/* 229 */       ((JList)comp).setDragEnabled(false);
/*     */     }
/* 231 */     else if ((comp instanceof JTable)) {
/* 232 */       ((JTable)comp).setDragEnabled(false);
/*     */     }
/* 234 */     else if ((comp instanceof JTextComponent)) {
/* 235 */       ((JTextComponent)comp).setDragEnabled(false);
/*     */     }
/* 237 */     else if ((comp instanceof JColorChooser)) {
/* 238 */       ((JColorChooser)comp).setDragEnabled(false);
/*     */     }
/* 240 */     else if ((comp instanceof JFileChooser)) {
/* 241 */       ((JFileChooser)comp).setDragEnabled(false);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canDrag(DragGestureEvent e)
/*     */   {
/* 252 */     int mods = e.getTriggerEvent().getModifiersEx() & 0x23C0;
/* 253 */     if (mods == 64)
/* 254 */       return (this.supportedActions & 0x2) != 0;
/* 255 */     if (mods == COPY_MASK)
/* 256 */       return (this.supportedActions & 0x1) != 0;
/* 257 */     if (mods == LINK_MASK)
/* 258 */       return (this.supportedActions & 0x40000000) != 0;
/* 259 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void setModifiers(int mods)
/*     */   {
/* 266 */     modifiers = mods;
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
/*     */   protected abstract Transferable getTransferable(DragGestureEvent paramDragGestureEvent);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Icon getDragIcon(DragGestureEvent e, Point srcOffset)
/*     */   {
/* 288 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void dragStarted(DragGestureEvent e) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void dragGestureRecognized(DragGestureEvent e)
/*     */   {
/* 302 */     if (((e.getDragAction() & this.supportedActions) != 0) && 
/* 303 */       (canDrag(e))) {
/* 304 */       setModifiers(e.getTriggerEvent().getModifiersEx() & 0x23C0);
/* 305 */       Transferable transferable = getTransferable(e);
/* 306 */       if (transferable == null)
/* 307 */         return;
/*     */       try {
/* 309 */         Point srcOffset = new Point(0, 0);
/* 310 */         Icon icon = getDragIcon(e, srcOffset);
/* 311 */         Point origin = e.getDragOrigin();
/*     */         
/* 313 */         this.imageOffset = new Point(srcOffset.x - origin.x, srcOffset.y - origin.y);
/*     */         
/* 315 */         Icon dragIcon = scaleDragIcon(icon, this.imageOffset);
/* 316 */         Cursor cursor = null;
/* 317 */         if ((dragIcon != null) && (DragSource.isDragImageSupported())) {
/* 318 */           GraphicsConfiguration gc = e.getComponent().getGraphicsConfiguration();
/* 319 */           e.startDrag(cursor, createDragImage(gc, dragIcon), this.imageOffset, transferable, this);
/*     */         }
/*     */         else
/*     */         {
/* 323 */           if (dragIcon != null) {
/* 324 */             Point screen = this.dragSource.getLocationOnScreen();
/* 325 */             screen.translate(origin.x, origin.y);
/* 326 */             Point cursorOffset = new Point(-this.imageOffset.x, -this.imageOffset.y);
/*     */             
/* 328 */             this.ghost = new GhostedDragImage(this.dragSource, dragIcon, getImageLocation(screen), cursorOffset);
/* 329 */             this.ghost.setAlpha(this.ghostAlpha);
/*     */           }
/* 331 */           e.startDrag(cursor, transferable, this);
/*     */         }
/* 333 */         dragStarted(e);
/* 334 */         this.moved = false;
/* 335 */         e.getDragSource().addDragSourceMotionListener(this);
/* 336 */         transferable = transferable;
/*     */       }
/*     */       catch (InvalidDnDOperationException ex) {
/* 339 */         if (this.ghost != null) {
/* 340 */           this.ghost.dispose();
/* 341 */           this.ghost = null;
/*     */         }
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Icon scaleDragIcon(Icon icon, Point imageOffset)
/*     */   {
/* 367 */     return icon;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Image createDragImage(GraphicsConfiguration gc, Icon icon)
/*     */   {
/* 377 */     int w = icon.getIconWidth();
/* 378 */     int h = icon.getIconHeight();
/* 379 */     BufferedImage image = gc.createCompatibleImage(w, h, 3);
/* 380 */     Graphics2D g = (Graphics2D)image.getGraphics();
/* 381 */     g.setComposite(AlphaComposite.Clear);
/* 382 */     g.fillRect(0, 0, w, h);
/*     */     
/* 384 */     g.setComposite(AlphaComposite.getInstance(2, this.ghostAlpha));
/* 385 */     icon.paintIcon(this.dragSource, g, 0, 0);
/* 386 */     g.dispose();
/* 387 */     return image;
/*     */   }
/*     */   
/*     */   private int reduce(int actions)
/*     */   {
/* 392 */     if (((actions & 0x2) != 0) && (actions != 2)) {
/* 393 */       return 2;
/*     */     }
/* 395 */     if (((actions & 0x1) != 0) && (actions != 1)) {
/* 396 */       return 1;
/*     */     }
/* 398 */     return actions;
/*     */   }
/*     */   
/*     */   protected Cursor getCursorForAction(int actualAction) {
/* 402 */     switch (actualAction) {
/*     */     case 2: 
/* 404 */       return DragSource.DefaultMoveDrop;
/*     */     case 1: 
/* 406 */       return DragSource.DefaultCopyDrop;
/*     */     case 1073741824: 
/* 408 */       return DragSource.DefaultLinkDrop;
/*     */     }
/* 410 */     return DragSource.DefaultMoveNoDrop;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getAcceptableDropAction(int targetActions)
/*     */   {
/* 419 */     return reduce(this.supportedActions & targetActions);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getDropAction(DragSourceEvent ev)
/*     */   {
/* 427 */     if ((ev instanceof DragSourceDragEvent)) {
/* 428 */       DragSourceDragEvent e = (DragSourceDragEvent)ev;
/* 429 */       return e.getDropAction();
/*     */     }
/* 431 */     if ((ev instanceof DragSourceDropEvent)) {
/* 432 */       return ((DragSourceDropEvent)ev).getDropAction();
/*     */     }
/* 434 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int adjustDropAction(DragSourceEvent ev)
/*     */   {
/* 443 */     int action = getDropAction(ev);
/* 444 */     if ((ev instanceof DragSourceDragEvent)) {
/* 445 */       DragSourceDragEvent e = (DragSourceDragEvent)ev;
/* 446 */       if (action == 0) {
/* 447 */         int mods = e.getGestureModifiersEx() & 0x23C0;
/* 448 */         if (mods == 0) {
/* 449 */           action = getAcceptableDropAction(e.getTargetActions());
/*     */         }
/*     */       }
/*     */     }
/* 453 */     return action;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updateCursor(DragSourceEvent ev)
/*     */   {
/* 461 */     if (!this.fixCursor)
/* 462 */       return;
/* 463 */     Cursor cursor = getCursorForAction(adjustDropAction(ev));
/* 464 */     ev.getDragSourceContext().setCursor(cursor);
/*     */   }
/*     */   
/*     */   static String actionString(int action) {
/* 468 */     switch (action) {
/* 469 */     case 2:  return "MOVE";
/* 470 */     case 3:  return "MOVE|COPY";
/* 471 */     case 1073741826:  return "MOVE|LINK";
/* 472 */     case 1073741827:  return "MOVE|COPY|LINK";
/* 473 */     case 1:  return "COPY";
/* 474 */     case 1073741825:  return "COPY|LINK";
/* 475 */     case 1073741824:  return "LINK"; }
/* 476 */     return "NONE";
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
/*     */   private void describe(String type, DragSourceEvent e) {}
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
/*     */   public void dragDropEnd(DragSourceDropEvent e)
/*     */   {
/* 502 */     describe("end", e);
/* 503 */     setModifiers(-1);
/* 504 */     transferable = UNKNOWN_TRANSFERABLE;
/* 505 */     if (this.ghost != null) {
/* 506 */       if (e.getDropSuccess()) {
/* 507 */         this.ghost.dispose();
/*     */       }
/*     */       else {
/* 510 */         this.ghost.returnToOrigin();
/*     */       }
/* 512 */       this.ghost = null;
/*     */     }
/* 514 */     DragSource src = e.getDragSourceContext().getDragSource();
/* 515 */     src.removeDragSourceMotionListener(this);
/* 516 */     this.moved = false;
/*     */   }
/*     */   
/*     */   private Point getImageLocation(Point where) {
/* 520 */     where.translate(this.imageOffset.x, this.imageOffset.y);
/* 521 */     return where;
/*     */   }
/*     */   
/*     */   public void dragEnter(DragSourceDragEvent e) {
/* 525 */     describe("enter", e);
/* 526 */     if (this.ghost != null) {
/* 527 */       this.ghost.move(getImageLocation(e.getLocation()));
/*     */     }
/* 529 */     updateCursor(e);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void dragMouseMoved(DragSourceDragEvent e)
/*     */   {
/* 537 */     describe("move", e);
/* 538 */     if (this.ghost != null) {
/* 539 */       this.ghost.move(getImageLocation(e.getLocation()));
/*     */     }
/* 541 */     if (this.moved)
/* 542 */       updateCursor(e);
/* 543 */     this.moved = true;
/*     */   }
/*     */   
/*     */   public void dragOver(DragSourceDragEvent e) {
/* 547 */     describe("over", e);
/* 548 */     if (this.ghost != null) {
/* 549 */       this.ghost.move(getImageLocation(e.getLocation()));
/*     */     }
/* 551 */     updateCursor(e);
/*     */   }
/*     */   
/*     */   public void dragExit(DragSourceEvent e) {
/* 555 */     describe("exit", e);
/*     */   }
/*     */   
/*     */   public void dropActionChanged(DragSourceDragEvent e) {
/* 559 */     describe("change", e);
/* 560 */     setModifiers(e.getGestureModifiersEx() & 0x23C0);
/* 561 */     if (this.ghost != null) {
/* 562 */       this.ghost.move(getImageLocation(e.getLocation()));
/*     */     }
/* 564 */     updateCursor(e);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\platform\dnd\DragHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */