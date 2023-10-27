/*     */ package com.sun.jna.platform.dnd;
/*     */ 
/*     */ import com.sun.jna.platform.WindowUtils;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.geom.Area;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.Timer;
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
/*     */ public class GhostedDragImage
/*     */ {
/*     */   private static final float DEFAULT_ALPHA = 0.5F;
/*     */   private Window dragImage;
/*     */   private Point origin;
/*     */   private static final int SLIDE_INTERVAL = 33;
/*     */   
/*     */   public GhostedDragImage(Component dragSource, final Icon icon, Point initialScreenLoc, final Point cursorOffset)
/*     */   {
/*  57 */     Window parent = (dragSource instanceof Window) ? (Window)dragSource : SwingUtilities.getWindowAncestor(dragSource);
/*     */     
/*  59 */     GraphicsConfiguration gc = parent.getGraphicsConfiguration();
/*  60 */     this.dragImage = new Window(JOptionPane.getRootFrame(), gc) {
/*     */       private static final long serialVersionUID = 1L;
/*     */       
/*  63 */       public void paint(Graphics g) { icon.paintIcon(this, g, 0, 0); }
/*     */       
/*     */       public Dimension getPreferredSize() {
/*  66 */         return new Dimension(icon.getIconWidth(), icon.getIconHeight());
/*     */       }
/*     */       
/*  69 */       public Dimension getMinimumSize() { return getPreferredSize(); }
/*     */       
/*     */       public Dimension getMaximumSize() {
/*  72 */         return getPreferredSize();
/*     */       }
/*  74 */     };
/*  75 */     this.dragImage.setFocusableWindowState(false);
/*  76 */     this.dragImage.setName("###overrideRedirect###");
/*  77 */     Icon dragIcon = new Icon() {
/*     */       public int getIconHeight() {
/*  79 */         return icon.getIconHeight();
/*     */       }
/*     */       
/*  82 */       public int getIconWidth() { return icon.getIconWidth(); }
/*     */       
/*     */       public void paintIcon(Component c, Graphics g, int x, int y) {
/*  85 */         g = g.create();
/*  86 */         Area area = new Area(new Rectangle(x, y, getIconWidth(), getIconHeight()));
/*     */         
/*  88 */         area.subtract(new Area(new Rectangle(x + cursorOffset.x - 1, y + cursorOffset.y - 1, 3, 3)));
/*  89 */         g.setClip(area);
/*  90 */         icon.paintIcon(c, g, x, y);
/*  91 */         g.dispose();
/*     */       }
/*     */       
/*  94 */     };
/*  95 */     this.dragImage.pack();
/*  96 */     WindowUtils.setWindowMask(this.dragImage, dragIcon);
/*  97 */     WindowUtils.setWindowAlpha(this.dragImage, 0.5F);
/*  98 */     move(initialScreenLoc);
/*  99 */     this.dragImage.setVisible(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setAlpha(float alpha)
/*     */   {
/* 106 */     WindowUtils.setWindowAlpha(this.dragImage, alpha);
/*     */   }
/*     */   
/*     */   public void dispose()
/*     */   {
/* 111 */     this.dragImage.dispose();
/* 112 */     this.dragImage = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void move(Point screenLocation)
/*     */   {
/* 119 */     if (this.origin == null) {
/* 120 */       this.origin = screenLocation;
/*     */     }
/* 122 */     this.dragImage.setLocation(screenLocation.x, screenLocation.y);
/*     */   }
/*     */   
/*     */ 
/*     */   public void returnToOrigin()
/*     */   {
/* 128 */     final Timer timer = new Timer(33, null);
/* 129 */     timer.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 131 */         Point location = GhostedDragImage.this.dragImage.getLocationOnScreen();
/* 132 */         Point dst = new Point(GhostedDragImage.this.origin);
/* 133 */         int dx = (dst.x - location.x) / 2;
/* 134 */         int dy = (dst.y - location.y) / 2;
/* 135 */         if ((dx != 0) || (dy != 0)) {
/* 136 */           location.translate(dx, dy);
/* 137 */           GhostedDragImage.this.move(location);
/*     */         }
/*     */         else {
/* 140 */           timer.stop();
/* 141 */           GhostedDragImage.this.dispose();
/*     */         }
/*     */       }
/* 144 */     });
/* 145 */     timer.setInitialDelay(0);
/* 146 */     timer.start();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\platform\dnd\GhostedDragImage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */