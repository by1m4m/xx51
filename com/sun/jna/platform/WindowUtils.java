/*      */ package com.sun.jna.platform;
/*      */ 
/*      */ import com.sun.jna.Memory;
/*      */ import com.sun.jna.Native;
/*      */ import com.sun.jna.NativeLong;
/*      */ import com.sun.jna.Platform;
/*      */ import com.sun.jna.Pointer;
/*      */ import com.sun.jna.platform.unix.X11;
/*      */ import com.sun.jna.platform.unix.X11.Display;
/*      */ import com.sun.jna.platform.unix.X11.GC;
/*      */ import com.sun.jna.platform.unix.X11.Pixmap;
/*      */ import com.sun.jna.platform.unix.X11.VisualID;
/*      */ import com.sun.jna.platform.unix.X11.Window;
/*      */ import com.sun.jna.platform.unix.X11.WindowByReference;
/*      */ import com.sun.jna.platform.unix.X11.XImage;
/*      */ import com.sun.jna.platform.unix.X11.XRectangle;
/*      */ import com.sun.jna.platform.unix.X11.XVisualInfo;
/*      */ import com.sun.jna.platform.unix.X11.XWindowAttributes;
/*      */ import com.sun.jna.platform.unix.X11.Xext;
/*      */ import com.sun.jna.platform.unix.X11.Xrender;
/*      */ import com.sun.jna.platform.unix.X11.Xrender.XRenderDirectFormat;
/*      */ import com.sun.jna.platform.unix.X11.Xrender.XRenderPictFormat;
/*      */ import com.sun.jna.platform.win32.GDI32;
/*      */ import com.sun.jna.platform.win32.Kernel32;
/*      */ import com.sun.jna.platform.win32.Psapi;
/*      */ import com.sun.jna.platform.win32.User32;
/*      */ import com.sun.jna.platform.win32.Win32Exception;
/*      */ import com.sun.jna.platform.win32.WinDef.DWORD;
/*      */ import com.sun.jna.platform.win32.WinDef.DWORDByReference;
/*      */ import com.sun.jna.platform.win32.WinDef.HBITMAP;
/*      */ import com.sun.jna.platform.win32.WinDef.HDC;
/*      */ import com.sun.jna.platform.win32.WinDef.HICON;
/*      */ import com.sun.jna.platform.win32.WinDef.HRGN;
/*      */ import com.sun.jna.platform.win32.WinDef.HWND;
/*      */ import com.sun.jna.platform.win32.WinDef.POINT;
/*      */ import com.sun.jna.platform.win32.WinDef.RECT;
/*      */ import com.sun.jna.platform.win32.WinGDI.BITMAP;
/*      */ import com.sun.jna.platform.win32.WinGDI.BITMAPINFO;
/*      */ import com.sun.jna.platform.win32.WinGDI.BITMAPINFOHEADER;
/*      */ import com.sun.jna.platform.win32.WinGDI.ICONINFO;
/*      */ import com.sun.jna.platform.win32.WinNT.HANDLE;
/*      */ import com.sun.jna.platform.win32.WinUser.BLENDFUNCTION;
/*      */ import com.sun.jna.platform.win32.WinUser.SIZE;
/*      */ import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;
/*      */ import com.sun.jna.ptr.ByteByReference;
/*      */ import com.sun.jna.ptr.IntByReference;
/*      */ import com.sun.jna.ptr.PointerByReference;
/*      */ import java.awt.AWTEvent;
/*      */ import java.awt.AlphaComposite;
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dialog;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Frame;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.GraphicsConfiguration;
/*      */ import java.awt.GraphicsDevice;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.AWTEventListener;
/*      */ import java.awt.event.ComponentEvent;
/*      */ import java.awt.event.ComponentListener;
/*      */ import java.awt.event.ContainerEvent;
/*      */ import java.awt.event.HierarchyEvent;
/*      */ import java.awt.event.HierarchyListener;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.WindowAdapter;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.geom.Area;
/*      */ import java.awt.geom.PathIterator;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.image.Raster;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JLayeredPane;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JRootPane;
/*      */ import javax.swing.RootPaneContainer;
/*      */ import javax.swing.SwingUtilities;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class WindowUtils
/*      */ {
/*      */   private static final String TRANSPARENT_OLD_BG = "transparent-old-bg";
/*      */   private static final String TRANSPARENT_OLD_OPAQUE = "transparent-old-opaque";
/*      */   private static final String TRANSPARENT_ALPHA = "transparent-alpha";
/*  151 */   public static final Shape MASK_NONE = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class HeavyweightForcer
/*      */     extends Window
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private final boolean packed;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public HeavyweightForcer(Window parent)
/*      */     {
/*  173 */       super();
/*  174 */       pack();
/*  175 */       this.packed = true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public boolean isVisible()
/*      */     {
/*  182 */       return this.packed;
/*      */     }
/*      */     
/*      */     public Rectangle getBounds() {
/*  186 */       return getOwner().getBounds();
/*      */     }
/*      */   }
/*      */   
/*      */   protected static class RepaintTrigger
/*      */     extends JComponent
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */     
/*      */     protected class Listener extends WindowAdapter implements ComponentListener, HierarchyListener, AWTEventListener
/*      */     {
/*      */       protected Listener() {}
/*      */       
/*      */       public void windowOpened(WindowEvent e)
/*      */       {
/*  201 */         WindowUtils.RepaintTrigger.this.repaint();
/*      */       }
/*      */       
/*      */       public void componentHidden(ComponentEvent e) {}
/*      */       
/*      */       public void componentMoved(ComponentEvent e) {}
/*      */       
/*      */       public void componentResized(ComponentEvent e) {
/*  209 */         WindowUtils.RepaintTrigger.this.setSize(WindowUtils.RepaintTrigger.this.getParent().getSize());
/*  210 */         WindowUtils.RepaintTrigger.this.repaint();
/*      */       }
/*      */       
/*      */       public void componentShown(ComponentEvent e) {
/*  214 */         WindowUtils.RepaintTrigger.this.repaint();
/*      */       }
/*      */       
/*      */       public void hierarchyChanged(HierarchyEvent e) {
/*  218 */         WindowUtils.RepaintTrigger.this.repaint();
/*      */       }
/*      */       
/*      */       public void eventDispatched(AWTEvent e) {
/*  222 */         if ((e instanceof MouseEvent)) {
/*  223 */           Component src = ((MouseEvent)e).getComponent();
/*  224 */           if ((src != null) && 
/*  225 */             (SwingUtilities.isDescendingFrom(src, WindowUtils.RepaintTrigger.this.content))) {
/*  226 */             MouseEvent me = SwingUtilities.convertMouseEvent(src, (MouseEvent)e, WindowUtils.RepaintTrigger.this.content);
/*  227 */             Component c = SwingUtilities.getDeepestComponentAt(WindowUtils.RepaintTrigger.this.content, me.getX(), me.getY());
/*  228 */             if (c != null) {
/*  229 */               WindowUtils.RepaintTrigger.this.setCursor(c.getCursor());
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  236 */     private final Listener listener = createListener();
/*      */     private final JComponent content;
/*      */     private Rectangle dirty;
/*      */     
/*  240 */     public RepaintTrigger(JComponent content) { this.content = content; }
/*      */     
/*      */     public void addNotify()
/*      */     {
/*  244 */       super.addNotify();
/*  245 */       Window w = SwingUtilities.getWindowAncestor(this);
/*  246 */       setSize(getParent().getSize());
/*  247 */       w.addComponentListener(this.listener);
/*  248 */       w.addWindowListener(this.listener);
/*  249 */       Toolkit.getDefaultToolkit().addAWTEventListener(this.listener, 48L);
/*      */     }
/*      */     
/*      */     public void removeNotify() {
/*  253 */       Toolkit.getDefaultToolkit().removeAWTEventListener(this.listener);
/*  254 */       Window w = SwingUtilities.getWindowAncestor(this);
/*  255 */       w.removeComponentListener(this.listener);
/*  256 */       w.removeWindowListener(this.listener);
/*  257 */       super.removeNotify();
/*      */     }
/*      */     
/*      */     protected void paintComponent(Graphics g)
/*      */     {
/*  262 */       Rectangle bounds = g.getClipBounds();
/*  263 */       if ((this.dirty == null) || (!this.dirty.contains(bounds))) {
/*  264 */         if (this.dirty == null) {
/*  265 */           this.dirty = bounds;
/*      */         }
/*      */         else {
/*  268 */           this.dirty = this.dirty.union(bounds);
/*      */         }
/*  270 */         this.content.repaint(this.dirty);
/*      */       }
/*      */       else {
/*  273 */         this.dirty = null;
/*      */       }
/*      */     }
/*      */     
/*      */     protected Listener createListener() {
/*  278 */       return new Listener();
/*      */     }
/*      */   }
/*      */   
/*      */   public static abstract class NativeWindowUtils
/*      */   {
/*      */     protected abstract class TransparentContentPane extends JPanel implements AWTEventListener {
/*      */       private static final long serialVersionUID = 1L;
/*      */       private boolean transparent;
/*      */       
/*      */       public TransparentContentPane(Container oldContent) {
/*  289 */         super();
/*  290 */         add(oldContent, "Center");
/*  291 */         setTransparent(true);
/*  292 */         if ((oldContent instanceof JPanel))
/*  293 */           ((JComponent)oldContent).setOpaque(false);
/*      */       }
/*      */       
/*      */       public void addNotify() {
/*  297 */         super.addNotify();
/*  298 */         Toolkit.getDefaultToolkit().addAWTEventListener(this, 2L);
/*      */       }
/*      */       
/*  301 */       public void removeNotify() { Toolkit.getDefaultToolkit().removeAWTEventListener(this);
/*  302 */         super.removeNotify();
/*      */       }
/*      */       
/*  305 */       public void setTransparent(boolean transparent) { this.transparent = transparent;
/*  306 */         setOpaque(!transparent);
/*  307 */         setDoubleBuffered(!transparent);
/*  308 */         repaint();
/*      */       }
/*      */       
/*  311 */       public void eventDispatched(AWTEvent e) { if ((e.getID() == 300) && 
/*  312 */           (SwingUtilities.isDescendingFrom(((ContainerEvent)e).getChild(), this))) {
/*  313 */           Component child = ((ContainerEvent)e).getChild();
/*  314 */           WindowUtils.NativeWindowUtils.this.setDoubleBuffered(child, false);
/*      */         }
/*      */       }
/*      */       
/*  318 */       public void paint(Graphics gr) { if (this.transparent) {
/*  319 */           Rectangle r = gr.getClipBounds();
/*  320 */           int w = r.width;
/*  321 */           int h = r.height;
/*  322 */           if ((getWidth() > 0) && (getHeight() > 0)) {
/*  323 */             BufferedImage buf = new BufferedImage(w, h, 3);
/*      */             
/*      */ 
/*  326 */             Graphics2D g = buf.createGraphics();
/*  327 */             g.setComposite(AlphaComposite.Clear);
/*  328 */             g.fillRect(0, 0, w, h);
/*  329 */             g.dispose();
/*      */             
/*  331 */             g = buf.createGraphics();
/*  332 */             g.translate(-r.x, -r.y);
/*  333 */             super.paint(g);
/*  334 */             g.dispose();
/*      */             
/*  336 */             paintDirect(buf, r);
/*      */           }
/*      */         }
/*      */         else {
/*  340 */           super.paint(gr);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */       protected abstract void paintDirect(BufferedImage paramBufferedImage, Rectangle paramRectangle);
/*      */     }
/*      */     
/*      */ 
/*      */     protected Window getWindow(Component c)
/*      */     {
/*  351 */       return (c instanceof Window) ? (Window)c : SwingUtilities.getWindowAncestor(c);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     protected void whenDisplayable(Component w, final Runnable action)
/*      */     {
/*  358 */       if ((w.isDisplayable()) && ((!WindowUtils.Holder.requiresVisible) || (w.isVisible()))) {
/*  359 */         action.run();
/*      */       }
/*  361 */       else if (WindowUtils.Holder.requiresVisible) {
/*  362 */         getWindow(w).addWindowListener(new WindowAdapter() {
/*      */           public void windowOpened(WindowEvent e) {
/*  364 */             e.getWindow().removeWindowListener(this);
/*  365 */             action.run();
/*      */           }
/*      */           
/*  368 */           public void windowClosed(WindowEvent e) { e.getWindow().removeWindowListener(this);
/*      */           }
/*      */ 
/*      */         });
/*      */       }
/*      */       else
/*      */       {
/*  375 */         w.addHierarchyListener(new HierarchyListener() {
/*      */           public void hierarchyChanged(HierarchyEvent e) {
/*  377 */             if (((e.getChangeFlags() & 0x2) != 0L) && 
/*  378 */               (e.getComponent().isDisplayable())) {
/*  379 */               e.getComponent().removeHierarchyListener(this);
/*  380 */               action.run();
/*      */             }
/*      */           }
/*      */         });
/*      */       }
/*      */     }
/*      */     
/*      */     protected Raster toRaster(Shape mask) {
/*  388 */       Raster raster = null;
/*  389 */       if (mask != WindowUtils.MASK_NONE) {
/*  390 */         Rectangle bounds = mask.getBounds();
/*  391 */         if ((bounds.width > 0) && (bounds.height > 0)) {
/*  392 */           BufferedImage clip = new BufferedImage(bounds.x + bounds.width, bounds.y + bounds.height, 12);
/*      */           
/*      */ 
/*      */ 
/*  396 */           Graphics2D g = clip.createGraphics();
/*  397 */           g.setColor(Color.black);
/*  398 */           g.fillRect(0, 0, bounds.x + bounds.width, bounds.y + bounds.height);
/*  399 */           g.setColor(Color.white);
/*  400 */           g.fill(mask);
/*  401 */           raster = clip.getRaster();
/*      */         }
/*      */       }
/*  404 */       return raster;
/*      */     }
/*      */     
/*      */     protected Raster toRaster(Component c, Icon mask) {
/*  408 */       Raster raster = null;
/*  409 */       if (mask != null)
/*      */       {
/*  411 */         Rectangle bounds = new Rectangle(0, 0, mask.getIconWidth(), mask.getIconHeight());
/*  412 */         BufferedImage clip = new BufferedImage(bounds.width, bounds.height, 2);
/*      */         
/*      */ 
/*  415 */         Graphics2D g = clip.createGraphics();
/*  416 */         g.setComposite(AlphaComposite.Clear);
/*  417 */         g.fillRect(0, 0, bounds.width, bounds.height);
/*  418 */         g.setComposite(AlphaComposite.SrcOver);
/*  419 */         mask.paintIcon(c, g, 0, 0);
/*  420 */         raster = clip.getAlphaRaster();
/*      */       }
/*  422 */       return raster;
/*      */     }
/*      */     
/*      */     protected Shape toShape(Raster raster) {
/*  426 */       final Area area = new Area(new Rectangle(0, 0, 0, 0));
/*  427 */       RasterRangesUtils.outputOccupiedRanges(raster, new RasterRangesUtils.RangesOutput() {
/*      */         public boolean outputRange(int x, int y, int w, int h) {
/*  429 */           area.add(new Area(new Rectangle(x, y, w, h)));
/*  430 */           return true;
/*      */         }
/*  432 */       });
/*  433 */       return area;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setWindowAlpha(Window w, float alpha) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isWindowAlphaSupported()
/*      */     {
/*  446 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */     public GraphicsConfiguration getAlphaCompatibleGraphicsConfiguration()
/*      */     {
/*  452 */       GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
/*  453 */       GraphicsDevice dev = env.getDefaultScreenDevice();
/*  454 */       return dev.getDefaultConfiguration();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setWindowTransparent(Window w, boolean transparent) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected void setDoubleBuffered(Component root, boolean buffered)
/*      */     {
/*  467 */       if ((root instanceof JComponent)) {
/*  468 */         ((JComponent)root).setDoubleBuffered(buffered);
/*      */       }
/*  470 */       if (((root instanceof JRootPane)) && (buffered)) {
/*  471 */         ((JRootPane)root).setDoubleBuffered(true);
/*      */       }
/*  473 */       else if ((root instanceof Container)) {
/*  474 */         Component[] kids = ((Container)root).getComponents();
/*  475 */         for (int i = 0; i < kids.length; i++) {
/*  476 */           setDoubleBuffered(kids[i], buffered);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     protected void setLayersTransparent(Window w, boolean transparent)
/*      */     {
/*  483 */       Color bg = transparent ? new Color(0, 0, 0, 0) : null;
/*  484 */       if ((w instanceof RootPaneContainer)) {
/*  485 */         RootPaneContainer rpc = (RootPaneContainer)w;
/*  486 */         JRootPane root = rpc.getRootPane();
/*  487 */         JLayeredPane lp = root.getLayeredPane();
/*  488 */         Container c = root.getContentPane();
/*  489 */         JComponent content = (c instanceof JComponent) ? (JComponent)c : null;
/*      */         
/*  491 */         if (transparent) {
/*  492 */           lp.putClientProperty("transparent-old-opaque", 
/*  493 */             Boolean.valueOf(lp.isOpaque()));
/*  494 */           lp.setOpaque(false);
/*  495 */           root.putClientProperty("transparent-old-opaque", 
/*  496 */             Boolean.valueOf(root.isOpaque()));
/*  497 */           root.setOpaque(false);
/*  498 */           if (content != null) {
/*  499 */             content.putClientProperty("transparent-old-opaque", 
/*  500 */               Boolean.valueOf(content.isOpaque()));
/*  501 */             content.setOpaque(false);
/*      */           }
/*  503 */           root.putClientProperty("transparent-old-bg", root
/*  504 */             .getParent().getBackground());
/*      */         }
/*      */         else {
/*  507 */           lp.setOpaque(Boolean.TRUE.equals(lp.getClientProperty("transparent-old-opaque")));
/*  508 */           lp.putClientProperty("transparent-old-opaque", null);
/*  509 */           root.setOpaque(Boolean.TRUE.equals(root.getClientProperty("transparent-old-opaque")));
/*  510 */           root.putClientProperty("transparent-old-opaque", null);
/*  511 */           if (content != null) {
/*  512 */             content.setOpaque(Boolean.TRUE.equals(content.getClientProperty("transparent-old-opaque")));
/*  513 */             content.putClientProperty("transparent-old-opaque", null);
/*      */           }
/*  515 */           bg = (Color)root.getClientProperty("transparent-old-bg");
/*  516 */           root.putClientProperty("transparent-old-bg", null);
/*      */         }
/*      */       }
/*  519 */       w.setBackground(bg);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     protected void setMask(Component c, Raster raster)
/*      */     {
/*  526 */       throw new UnsupportedOperationException("Window masking is not available");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected void setWindowMask(Component w, Raster raster)
/*      */     {
/*  535 */       if (w.isLightweight())
/*  536 */         throw new IllegalArgumentException("Component must be heavyweight: " + w);
/*  537 */       setMask(w, raster);
/*      */     }
/*      */     
/*      */     public void setWindowMask(Component w, Shape mask)
/*      */     {
/*  542 */       setWindowMask(w, toRaster(mask));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setWindowMask(Component w, Icon mask)
/*      */     {
/*  550 */       setWindowMask(w, toRaster(w, mask));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected void setForceHeavyweightPopups(Window w, boolean force)
/*      */     {
/*  559 */       if (!(w instanceof WindowUtils.HeavyweightForcer)) {
/*  560 */         Window[] owned = w.getOwnedWindows();
/*  561 */         for (int i = 0; i < owned.length; i++) {
/*  562 */           if ((owned[i] instanceof WindowUtils.HeavyweightForcer)) {
/*  563 */             if (force)
/*  564 */               return;
/*  565 */             owned[i].dispose();
/*      */           }
/*      */         }
/*  568 */         Boolean b = Boolean.valueOf(System.getProperty("jna.force_hw_popups", "true"));
/*  569 */         if ((force) && (b.booleanValue())) {
/*  570 */           new WindowUtils.HeavyweightForcer(w);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected BufferedImage getWindowIcon(WinDef.HWND hwnd)
/*      */     {
/*  589 */       throw new UnsupportedOperationException("This platform is not supported, yet.");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected Dimension getIconSize(WinDef.HICON hIcon)
/*      */     {
/*  605 */       throw new UnsupportedOperationException("This platform is not supported, yet.");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected List<DesktopWindow> getAllWindows(boolean onlyVisibleWindows)
/*      */     {
/*  626 */       throw new UnsupportedOperationException("This platform is not supported, yet.");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected String getWindowTitle(WinDef.HWND hwnd)
/*      */     {
/*  642 */       throw new UnsupportedOperationException("This platform is not supported, yet.");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected String getProcessFilePath(WinDef.HWND hwnd)
/*      */     {
/*  659 */       throw new UnsupportedOperationException("This platform is not supported, yet.");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected Rectangle getWindowLocationAndSize(WinDef.HWND hwnd)
/*      */     {
/*  674 */       throw new UnsupportedOperationException("This platform is not supported, yet.");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static class Holder
/*      */   {
/*      */     public static boolean requiresVisible;
/*      */     
/*      */     public static final WindowUtils.NativeWindowUtils INSTANCE;
/*      */     
/*      */     static
/*      */     {
/*  687 */       if (Platform.isWindows()) {
/*  688 */         INSTANCE = new WindowUtils.W32WindowUtils(null);
/*      */       }
/*  690 */       else if (Platform.isMac()) {
/*  691 */         INSTANCE = new WindowUtils.MacWindowUtils(null);
/*      */       }
/*  693 */       else if (Platform.isX11()) {
/*  694 */         INSTANCE = new WindowUtils.X11WindowUtils(null);
/*      */         
/*  696 */         requiresVisible = System.getProperty("java.version").matches("^1\\.4\\..*");
/*      */       }
/*      */       else {
/*  699 */         String os = System.getProperty("os.name");
/*  700 */         throw new UnsupportedOperationException("No support for " + os);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static NativeWindowUtils getInstance() {
/*  706 */     return Holder.INSTANCE;
/*      */   }
/*      */   
/*      */   private static class W32WindowUtils extends WindowUtils.NativeWindowUtils {
/*      */     private WinDef.HWND getHWnd(Component w) {
/*  711 */       WinDef.HWND hwnd = new WinDef.HWND();
/*  712 */       hwnd.setPointer(Native.getComponentPointer(w));
/*  713 */       return hwnd;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isWindowAlphaSupported()
/*      */     {
/*  721 */       return Boolean.getBoolean("sun.java2d.noddraw");
/*      */     }
/*      */     
/*      */     private boolean usingUpdateLayeredWindow(Window w)
/*      */     {
/*  726 */       if ((w instanceof RootPaneContainer)) {
/*  727 */         JRootPane root = ((RootPaneContainer)w).getRootPane();
/*  728 */         return root.getClientProperty("transparent-old-bg") != null;
/*      */       }
/*  730 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private void storeAlpha(Window w, byte alpha)
/*      */     {
/*  737 */       if ((w instanceof RootPaneContainer)) {
/*  738 */         JRootPane root = ((RootPaneContainer)w).getRootPane();
/*  739 */         Byte b = alpha == -1 ? null : new Byte(alpha);
/*  740 */         root.putClientProperty("transparent-alpha", b);
/*      */       }
/*      */     }
/*      */     
/*      */     private byte getAlpha(Window w)
/*      */     {
/*  746 */       if ((w instanceof RootPaneContainer)) {
/*  747 */         JRootPane root = ((RootPaneContainer)w).getRootPane();
/*  748 */         Byte b = (Byte)root.getClientProperty("transparent-alpha");
/*  749 */         if (b != null) {
/*  750 */           return b.byteValue();
/*      */         }
/*      */       }
/*  753 */       return -1;
/*      */     }
/*      */     
/*      */     public void setWindowAlpha(final Window w, final float alpha) {
/*  757 */       if (!isWindowAlphaSupported()) {
/*  758 */         throw new UnsupportedOperationException("Set sun.java2d.noddraw=true to enable transparent windows");
/*      */       }
/*  760 */       whenDisplayable(w, new Runnable() {
/*      */         public void run() {
/*  762 */           WinDef.HWND hWnd = WindowUtils.W32WindowUtils.this.getHWnd(w);
/*  763 */           User32 user = User32.INSTANCE;
/*  764 */           int flags = user.GetWindowLong(hWnd, -20);
/*  765 */           byte level = (byte)((int)(255.0F * alpha) & 0xFF);
/*  766 */           if (WindowUtils.W32WindowUtils.this.usingUpdateLayeredWindow(w))
/*      */           {
/*      */ 
/*  769 */             WinUser.BLENDFUNCTION blend = new WinUser.BLENDFUNCTION();
/*  770 */             blend.SourceConstantAlpha = level;
/*  771 */             blend.AlphaFormat = 1;
/*  772 */             user.UpdateLayeredWindow(hWnd, null, null, null, null, null, 0, blend, 2);
/*      */ 
/*      */ 
/*      */           }
/*  776 */           else if (alpha == 1.0F) {
/*  777 */             flags &= 0xFFF7FFFF;
/*  778 */             user.SetWindowLong(hWnd, -20, flags);
/*      */           }
/*      */           else {
/*  781 */             flags |= 0x80000;
/*  782 */             user.SetWindowLong(hWnd, -20, flags);
/*  783 */             user.SetLayeredWindowAttributes(hWnd, 0, level, 2);
/*      */           }
/*      */           
/*  786 */           WindowUtils.W32WindowUtils.this.setForceHeavyweightPopups(w, alpha != 1.0F);
/*  787 */           WindowUtils.W32WindowUtils.this.storeAlpha(w, level);
/*      */         }
/*      */       });
/*      */     }
/*      */     
/*      */ 
/*      */     private class W32TransparentContentPane
/*      */       extends WindowUtils.NativeWindowUtils.TransparentContentPane
/*      */     {
/*      */       private static final long serialVersionUID = 1L;
/*      */       
/*      */       private WinDef.HDC memDC;
/*      */       private WinDef.HBITMAP hBitmap;
/*      */       private Pointer pbits;
/*      */       private Dimension bitmapSize;
/*      */       
/*  803 */       public W32TransparentContentPane(Container content) { super(content); }
/*      */       
/*      */       private void disposeBackingStore() {
/*  806 */         GDI32 gdi = GDI32.INSTANCE;
/*  807 */         if (this.hBitmap != null) {
/*  808 */           gdi.DeleteObject(this.hBitmap);
/*  809 */           this.hBitmap = null;
/*      */         }
/*  811 */         if (this.memDC != null) {
/*  812 */           gdi.DeleteDC(this.memDC);
/*  813 */           this.memDC = null;
/*      */         }
/*      */       }
/*      */       
/*  817 */       public void removeNotify() { super.removeNotify();
/*  818 */         disposeBackingStore();
/*      */       }
/*      */       
/*  821 */       public void setTransparent(boolean transparent) { super.setTransparent(transparent);
/*  822 */         if (!transparent) {
/*  823 */           disposeBackingStore();
/*      */         }
/*      */       }
/*      */       
/*      */       protected void paintDirect(BufferedImage buf, Rectangle bounds) {
/*  828 */         Window win = SwingUtilities.getWindowAncestor(this);
/*  829 */         GDI32 gdi = GDI32.INSTANCE;
/*  830 */         User32 user = User32.INSTANCE;
/*  831 */         int x = bounds.x;
/*  832 */         int y = bounds.y;
/*  833 */         Point origin = SwingUtilities.convertPoint(this, x, y, win);
/*  834 */         int w = bounds.width;
/*  835 */         int h = bounds.height;
/*  836 */         int ww = win.getWidth();
/*  837 */         int wh = win.getHeight();
/*  838 */         WinDef.HDC screenDC = user.GetDC(null);
/*  839 */         WinNT.HANDLE oldBitmap = null;
/*      */         try {
/*  841 */           if (this.memDC == null) {
/*  842 */             this.memDC = gdi.CreateCompatibleDC(screenDC);
/*      */           }
/*  844 */           if ((this.hBitmap == null) || (!win.getSize().equals(this.bitmapSize))) {
/*  845 */             if (this.hBitmap != null) {
/*  846 */               gdi.DeleteObject(this.hBitmap);
/*  847 */               this.hBitmap = null;
/*      */             }
/*  849 */             WinGDI.BITMAPINFO bmi = new WinGDI.BITMAPINFO();
/*  850 */             bmi.bmiHeader.biWidth = ww;
/*  851 */             bmi.bmiHeader.biHeight = wh;
/*  852 */             bmi.bmiHeader.biPlanes = 1;
/*  853 */             bmi.bmiHeader.biBitCount = 32;
/*  854 */             bmi.bmiHeader.biCompression = 0;
/*  855 */             bmi.bmiHeader.biSizeImage = (ww * wh * 4);
/*  856 */             PointerByReference ppbits = new PointerByReference();
/*  857 */             this.hBitmap = gdi.CreateDIBSection(this.memDC, bmi, 0, ppbits, null, 0);
/*      */             
/*      */ 
/*  860 */             this.pbits = ppbits.getValue();
/*  861 */             this.bitmapSize = new Dimension(ww, wh);
/*      */           }
/*  863 */           oldBitmap = gdi.SelectObject(this.memDC, this.hBitmap);
/*  864 */           Raster raster = buf.getData();
/*  865 */           int[] pixel = new int[4];
/*  866 */           int[] bits = new int[w];
/*  867 */           for (int row = 0; row < h; row++) {
/*  868 */             for (int col = 0; col < w; col++) {
/*  869 */               raster.getPixel(col, row, pixel);
/*  870 */               int alpha = (pixel[3] & 0xFF) << 24;
/*  871 */               int red = pixel[2] & 0xFF;
/*  872 */               int green = (pixel[1] & 0xFF) << 8;
/*  873 */               int blue = (pixel[0] & 0xFF) << 16;
/*  874 */               bits[col] = (alpha | red | green | blue);
/*      */             }
/*  876 */             int v = wh - (origin.y + row) - 1;
/*  877 */             this.pbits.write((v * ww + origin.x) * 4, bits, 0, bits.length);
/*      */           }
/*  879 */           WinUser.SIZE winSize = new WinUser.SIZE();
/*  880 */           winSize.cx = win.getWidth();
/*  881 */           winSize.cy = win.getHeight();
/*  882 */           WinDef.POINT winLoc = new WinDef.POINT();
/*  883 */           winLoc.x = win.getX();
/*  884 */           winLoc.y = win.getY();
/*  885 */           WinDef.POINT srcLoc = new WinDef.POINT();
/*  886 */           WinUser.BLENDFUNCTION blend = new WinUser.BLENDFUNCTION();
/*  887 */           WinDef.HWND hWnd = WindowUtils.W32WindowUtils.this.getHWnd(win);
/*      */           
/*  889 */           ByteByReference bref = new ByteByReference();
/*  890 */           IntByReference iref = new IntByReference();
/*  891 */           byte level = WindowUtils.W32WindowUtils.this.getAlpha(win);
/*      */           try
/*      */           {
/*  894 */             if ((user.GetLayeredWindowAttributes(hWnd, null, bref, iref)) && 
/*  895 */               ((iref.getValue() & 0x2) != 0)) {
/*  896 */               level = bref.getValue();
/*      */             }
/*      */           }
/*      */           catch (UnsatisfiedLinkError e) {}
/*      */           
/*  901 */           blend.SourceConstantAlpha = level;
/*  902 */           blend.AlphaFormat = 1;
/*  903 */           user.UpdateLayeredWindow(hWnd, screenDC, winLoc, winSize, this.memDC, srcLoc, 0, blend, 2);
/*      */         }
/*      */         finally {
/*  906 */           user.ReleaseDC(null, screenDC);
/*  907 */           if ((this.memDC != null) && (oldBitmap != null)) {
/*  908 */             gdi.SelectObject(this.memDC, oldBitmap);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setWindowTransparent(final Window w, final boolean transparent)
/*      */     {
/*  919 */       if (!(w instanceof RootPaneContainer)) {
/*  920 */         throw new IllegalArgumentException("Window must be a RootPaneContainer");
/*      */       }
/*  922 */       if (!isWindowAlphaSupported()) {
/*  923 */         throw new UnsupportedOperationException("Set sun.java2d.noddraw=true to enable transparent windows");
/*      */       }
/*      */       
/*  926 */       boolean isTransparent = (w.getBackground() != null) && (w.getBackground().getAlpha() == 0);
/*  927 */       if (transparent == isTransparent)
/*  928 */         return;
/*  929 */       whenDisplayable(w, new Runnable() {
/*      */         public void run() {
/*  931 */           User32 user = User32.INSTANCE;
/*  932 */           WinDef.HWND hWnd = WindowUtils.W32WindowUtils.this.getHWnd(w);
/*  933 */           int flags = user.GetWindowLong(hWnd, -20);
/*  934 */           JRootPane root = ((RootPaneContainer)w).getRootPane();
/*  935 */           JLayeredPane lp = root.getLayeredPane();
/*  936 */           Container content = root.getContentPane();
/*  937 */           if ((content instanceof WindowUtils.W32WindowUtils.W32TransparentContentPane)) {
/*  938 */             ((WindowUtils.W32WindowUtils.W32TransparentContentPane)content).setTransparent(transparent);
/*      */           }
/*  940 */           else if (transparent) {
/*  941 */             WindowUtils.W32WindowUtils.W32TransparentContentPane w32content = new WindowUtils.W32WindowUtils.W32TransparentContentPane(WindowUtils.W32WindowUtils.this, content);
/*      */             
/*  943 */             root.setContentPane(w32content);
/*  944 */             lp.add(new WindowUtils.RepaintTrigger(w32content), JLayeredPane.DRAG_LAYER);
/*      */           }
/*      */           
/*  947 */           if ((transparent) && (!WindowUtils.W32WindowUtils.this.usingUpdateLayeredWindow(w))) {
/*  948 */             flags |= 0x80000;
/*  949 */             user.SetWindowLong(hWnd, -20, flags);
/*      */           }
/*  951 */           else if ((!transparent) && (WindowUtils.W32WindowUtils.this.usingUpdateLayeredWindow(w))) {
/*  952 */             flags &= 0xFFF7FFFF;
/*  953 */             user.SetWindowLong(hWnd, -20, flags);
/*      */           }
/*  955 */           WindowUtils.W32WindowUtils.this.setLayersTransparent(w, transparent);
/*  956 */           WindowUtils.W32WindowUtils.this.setForceHeavyweightPopups(w, transparent);
/*  957 */           WindowUtils.W32WindowUtils.this.setDoubleBuffered(w, !transparent);
/*      */         }
/*      */       });
/*      */     }
/*      */     
/*      */     public void setWindowMask(Component w, Shape mask) {
/*  963 */       if (((mask instanceof Area)) && (((Area)mask).isPolygonal())) {
/*  964 */         setMask(w, (Area)mask);
/*      */       }
/*      */       else {
/*  967 */         super.setWindowMask(w, mask);
/*      */       }
/*      */     }
/*      */     
/*      */     private void setWindowRegion(final Component w, final WinDef.HRGN hrgn)
/*      */     {
/*  973 */       whenDisplayable(w, new Runnable() {
/*      */         public void run() {
/*  975 */           GDI32 gdi = GDI32.INSTANCE;
/*  976 */           User32 user = User32.INSTANCE;
/*  977 */           WinDef.HWND hWnd = WindowUtils.W32WindowUtils.this.getHWnd(w);
/*      */           try {
/*  979 */             user.SetWindowRgn(hWnd, hrgn, true);
/*  980 */             WindowUtils.W32WindowUtils.this.setForceHeavyweightPopups(WindowUtils.W32WindowUtils.this.getWindow(w), hrgn != null);
/*      */           }
/*      */           finally {
/*  983 */             gdi.DeleteObject(hrgn);
/*      */           }
/*      */         }
/*      */       });
/*      */     }
/*      */     
/*      */     private void setMask(Component w, Area area)
/*      */     {
/*  991 */       GDI32 gdi = GDI32.INSTANCE;
/*  992 */       PathIterator pi = area.getPathIterator(null);
/*  993 */       int mode = pi.getWindingRule() == 1 ? 2 : 1;
/*      */       
/*  995 */       float[] coords = new float[6];
/*  996 */       List<WinDef.POINT> points = new ArrayList();
/*  997 */       int size = 0;
/*  998 */       List<Integer> sizes = new ArrayList();
/*  999 */       while (!pi.isDone()) {
/* 1000 */         int type = pi.currentSegment(coords);
/* 1001 */         if (type == 0) {
/* 1002 */           size = 1;
/* 1003 */           points.add(new WinDef.POINT((int)coords[0], (int)coords[1]));
/*      */         }
/* 1005 */         else if (type == 1) {
/* 1006 */           size++;
/* 1007 */           points.add(new WinDef.POINT((int)coords[0], (int)coords[1]));
/*      */         }
/* 1009 */         else if (type == 4) {
/* 1010 */           sizes.add(new Integer(size));
/*      */         }
/*      */         else {
/* 1013 */           throw new RuntimeException("Area is not polygonal: " + area);
/*      */         }
/* 1015 */         pi.next();
/*      */       }
/* 1017 */       WinDef.POINT[] lppt = (WinDef.POINT[])new WinDef.POINT().toArray(points.size());
/* 1018 */       WinDef.POINT[] pts = (WinDef.POINT[])points.toArray(new WinDef.POINT[points.size()]);
/* 1019 */       for (int i = 0; i < lppt.length; i++) {
/* 1020 */         lppt[i].x = pts[i].x;
/* 1021 */         lppt[i].y = pts[i].y;
/*      */       }
/* 1023 */       int[] counts = new int[sizes.size()];
/* 1024 */       for (int i = 0; i < counts.length; i++) {
/* 1025 */         counts[i] = ((Integer)sizes.get(i)).intValue();
/*      */       }
/* 1027 */       WinDef.HRGN hrgn = gdi.CreatePolyPolygonRgn(lppt, counts, counts.length, mode);
/* 1028 */       setWindowRegion(w, hrgn);
/*      */     }
/*      */     
/*      */     protected void setMask(Component w, Raster raster) {
/* 1032 */       GDI32 gdi = GDI32.INSTANCE;
/*      */       
/* 1034 */       final WinDef.HRGN region = raster != null ? gdi.CreateRectRgn(0, 0, 0, 0) : null;
/* 1035 */       if (region != null) {
/* 1036 */         final WinDef.HRGN tempRgn = gdi.CreateRectRgn(0, 0, 0, 0);
/*      */         try {
/* 1038 */           RasterRangesUtils.outputOccupiedRanges(raster, new RasterRangesUtils.RangesOutput() {
/*      */             public boolean outputRange(int x, int y, int w, int h) {
/* 1040 */               GDI32 gdi = GDI32.INSTANCE;
/* 1041 */               gdi.SetRectRgn(tempRgn, x, y, x + w, y + h);
/* 1042 */               return gdi.CombineRgn(region, region, tempRgn, 2) != 0;
/*      */             }
/*      */           });
/*      */         }
/*      */         finally {
/* 1047 */           gdi.DeleteObject(tempRgn);
/*      */         }
/*      */       }
/* 1050 */       setWindowRegion(w, region);
/*      */     }
/*      */     
/*      */ 
/*      */     public BufferedImage getWindowIcon(WinDef.HWND hwnd)
/*      */     {
/* 1056 */       WinDef.DWORDByReference hIconNumber = new WinDef.DWORDByReference();
/* 1057 */       long result = User32.INSTANCE.SendMessageTimeout(hwnd, 127, 1L, 0L, 2, 500, hIconNumber);
/*      */       
/*      */ 
/* 1060 */       if (result == 0L) {
/* 1061 */         result = User32.INSTANCE.SendMessageTimeout(hwnd, 127, 0L, 0L, 2, 500, hIconNumber);
/*      */       }
/*      */       
/* 1064 */       if (result == 0L) {
/* 1065 */         result = User32.INSTANCE.SendMessageTimeout(hwnd, 127, 2L, 0L, 2, 500, hIconNumber);
/*      */       }
/*      */       
/* 1068 */       if (result == 0L) {
/* 1069 */         result = User32.INSTANCE.GetClassLongPtr(hwnd, -14);
/*      */         
/* 1071 */         hIconNumber.getValue().setValue(result);
/*      */       }
/* 1073 */       if (result == 0L) {
/* 1074 */         result = User32.INSTANCE.GetClassLongPtr(hwnd, -34);
/*      */         
/* 1076 */         hIconNumber.getValue().setValue(result);
/*      */       }
/* 1078 */       if (result == 0L) {
/* 1079 */         return null;
/*      */       }
/*      */       
/*      */ 
/* 1083 */       WinDef.HICON hIcon = new WinDef.HICON(new Pointer(hIconNumber.getValue().longValue()));
/* 1084 */       Dimension iconSize = getIconSize(hIcon);
/* 1085 */       if ((iconSize.width == 0) || (iconSize.height == 0)) {
/* 1086 */         return null;
/*      */       }
/* 1088 */       int width = iconSize.width;
/* 1089 */       int height = iconSize.height;
/* 1090 */       short depth = 24;
/*      */       
/* 1092 */       byte[] lpBitsColor = new byte[width * height * 24 / 8];
/* 1093 */       Pointer lpBitsColorPtr = new Memory(lpBitsColor.length);
/* 1094 */       byte[] lpBitsMask = new byte[width * height * 24 / 8];
/* 1095 */       Pointer lpBitsMaskPtr = new Memory(lpBitsMask.length);
/* 1096 */       WinGDI.BITMAPINFO bitmapInfo = new WinGDI.BITMAPINFO();
/* 1097 */       WinGDI.BITMAPINFOHEADER hdr = new WinGDI.BITMAPINFOHEADER();
/*      */       
/* 1099 */       bitmapInfo.bmiHeader = hdr;
/* 1100 */       hdr.biWidth = width;
/* 1101 */       hdr.biHeight = height;
/* 1102 */       hdr.biPlanes = 1;
/* 1103 */       hdr.biBitCount = 24;
/* 1104 */       hdr.biCompression = 0;
/* 1105 */       hdr.write();
/* 1106 */       bitmapInfo.write();
/*      */       
/* 1108 */       WinDef.HDC hDC = User32.INSTANCE.GetDC(null);
/* 1109 */       WinGDI.ICONINFO iconInfo = new WinGDI.ICONINFO();
/* 1110 */       User32.INSTANCE.GetIconInfo(hIcon, iconInfo);
/* 1111 */       iconInfo.read();
/* 1112 */       GDI32.INSTANCE.GetDIBits(hDC, iconInfo.hbmColor, 0, height, lpBitsColorPtr, bitmapInfo, 0);
/*      */       
/* 1114 */       lpBitsColorPtr.read(0L, lpBitsColor, 0, lpBitsColor.length);
/* 1115 */       GDI32.INSTANCE.GetDIBits(hDC, iconInfo.hbmMask, 0, height, lpBitsMaskPtr, bitmapInfo, 0);
/*      */       
/* 1117 */       lpBitsMaskPtr.read(0L, lpBitsMask, 0, lpBitsMask.length);
/* 1118 */       BufferedImage image = new BufferedImage(width, height, 2);
/*      */       
/*      */ 
/*      */ 
/* 1122 */       int x = 0;int y = height - 1;
/* 1123 */       for (int i = 0; i < lpBitsColor.length; i += 3) {
/* 1124 */         int b = lpBitsColor[i] & 0xFF;
/* 1125 */         int g = lpBitsColor[(i + 1)] & 0xFF;
/* 1126 */         int r = lpBitsColor[(i + 2)] & 0xFF;
/* 1127 */         int a = 255 - lpBitsMask[i] & 0xFF;
/* 1128 */         int argb = a << 24 | r << 16 | g << 8 | b;
/* 1129 */         image.setRGB(x, y, argb);
/* 1130 */         x = (x + 1) % width;
/* 1131 */         if (x == 0) {
/* 1132 */           y--;
/*      */         }
/*      */       }
/* 1135 */       User32.INSTANCE.ReleaseDC(null, hDC);
/*      */       
/* 1137 */       return image;
/*      */     }
/*      */     
/*      */     public Dimension getIconSize(WinDef.HICON hIcon)
/*      */     {
/* 1142 */       WinGDI.ICONINFO iconInfo = new WinGDI.ICONINFO();
/*      */       try {
/* 1144 */         if (!User32.INSTANCE.GetIconInfo(hIcon, iconInfo))
/* 1145 */           return new Dimension();
/* 1146 */         iconInfo.read();
/*      */         
/* 1148 */         WinGDI.BITMAP bmp = new WinGDI.BITMAP();
/* 1149 */         Dimension localDimension2; if ((iconInfo.hbmColor != null) && 
/* 1150 */           (iconInfo.hbmColor.getPointer() != Pointer.NULL)) {
/* 1151 */           int nWrittenBytes = GDI32.INSTANCE.GetObject(iconInfo.hbmColor, bmp
/* 1152 */             .size(), bmp.getPointer());
/* 1153 */           bmp.read();
/* 1154 */           if (nWrittenBytes > 0)
/*      */           {
/* 1156 */             return new Dimension(bmp.bmWidth.intValue(), bmp.bmHeight.intValue()); }
/* 1157 */         } else if ((iconInfo.hbmMask != null) && 
/* 1158 */           (iconInfo.hbmMask.getPointer() != Pointer.NULL)) {
/* 1159 */           int nWrittenBytes = GDI32.INSTANCE.GetObject(iconInfo.hbmMask, bmp
/* 1160 */             .size(), bmp.getPointer());
/* 1161 */           bmp.read();
/* 1162 */           if (nWrittenBytes > 0)
/* 1163 */             return new Dimension(bmp.bmWidth.intValue(), bmp.bmHeight.intValue() / 2);
/*      */         }
/*      */       } finally {
/* 1166 */         if ((iconInfo.hbmColor != null) && 
/* 1167 */           (iconInfo.hbmColor.getPointer() != Pointer.NULL))
/* 1168 */           GDI32.INSTANCE.DeleteObject(iconInfo.hbmColor);
/* 1169 */         if ((iconInfo.hbmMask != null) && 
/* 1170 */           (iconInfo.hbmMask.getPointer() != Pointer.NULL)) {
/* 1171 */           GDI32.INSTANCE.DeleteObject(iconInfo.hbmMask);
/*      */         }
/*      */       }
/* 1174 */       return new Dimension();
/*      */     }
/*      */     
/*      */ 
/*      */     public List<DesktopWindow> getAllWindows(final boolean onlyVisibleWindows)
/*      */     {
/* 1180 */       final List<DesktopWindow> result = new LinkedList();
/*      */       
/* 1182 */       WinUser.WNDENUMPROC lpEnumFunc = new WinUser.WNDENUMPROC()
/*      */       {
/*      */         public boolean callback(WinDef.HWND hwnd, Pointer arg1)
/*      */         {
/*      */           try {
/* 1187 */             boolean visible = (!onlyVisibleWindows) || (User32.INSTANCE.IsWindowVisible(hwnd));
/* 1188 */             if (visible) {
/* 1189 */               String title = WindowUtils.W32WindowUtils.this.getWindowTitle(hwnd);
/* 1190 */               String filePath = WindowUtils.W32WindowUtils.this.getProcessFilePath(hwnd);
/* 1191 */               Rectangle locAndSize = WindowUtils.W32WindowUtils.this.getWindowLocationAndSize(hwnd);
/* 1192 */               result.add(new DesktopWindow(hwnd, title, filePath, locAndSize));
/*      */             }
/*      */           }
/*      */           catch (Exception e) {
/* 1196 */             e.printStackTrace();
/*      */           }
/*      */           
/* 1199 */           return true;
/*      */         }
/*      */       };
/*      */       
/* 1203 */       if (!User32.INSTANCE.EnumWindows(lpEnumFunc, null)) {
/* 1204 */         throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
/*      */       }
/* 1206 */       return result;
/*      */     }
/*      */     
/*      */ 
/*      */     public String getWindowTitle(WinDef.HWND hwnd)
/*      */     {
/* 1212 */       int requiredLength = User32.INSTANCE.GetWindowTextLength(hwnd) + 1;
/* 1213 */       char[] title = new char[requiredLength];
/* 1214 */       int length = User32.INSTANCE.GetWindowText(hwnd, title, title.length);
/*      */       
/*      */ 
/* 1217 */       return Native.toString(Arrays.copyOfRange(title, 0, length));
/*      */     }
/*      */     
/*      */     public String getProcessFilePath(WinDef.HWND hwnd)
/*      */     {
/* 1222 */       char[] filePath = new char['Ё'];
/* 1223 */       IntByReference pid = new IntByReference();
/* 1224 */       User32.INSTANCE.GetWindowThreadProcessId(hwnd, pid);
/*      */       
/* 1226 */       WinNT.HANDLE process = Kernel32.INSTANCE.OpenProcess(1040, false, pid
/*      */       
/* 1228 */         .getValue());
/* 1229 */       if ((process == null) && 
/* 1230 */         (Kernel32.INSTANCE.GetLastError() != 5)) {
/* 1231 */         throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
/*      */       }
/* 1233 */       int length = Psapi.INSTANCE.GetModuleFileNameExW(process, null, filePath, filePath.length);
/*      */       
/* 1235 */       if ((length == 0) && 
/* 1236 */         (Kernel32.INSTANCE.GetLastError() != 6)) {
/* 1237 */         throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
/*      */       }
/* 1239 */       return Native.toString(filePath).trim();
/*      */     }
/*      */     
/*      */     public Rectangle getWindowLocationAndSize(WinDef.HWND hwnd)
/*      */     {
/* 1244 */       WinDef.RECT lpRect = new WinDef.RECT();
/* 1245 */       if (!User32.INSTANCE.GetWindowRect(hwnd, lpRect)) {
/* 1246 */         throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
/*      */       }
/*      */       
/* 1249 */       return new Rectangle(lpRect.left, lpRect.top, Math.abs(lpRect.right - lpRect.left), Math.abs(lpRect.bottom - lpRect.top));
/*      */     }
/*      */   }
/*      */   
/*      */   private static class MacWindowUtils extends WindowUtils.NativeWindowUtils { private static final String WDRAG = "apple.awt.draggableWindowBackground";
/*      */     
/* 1255 */     public boolean isWindowAlphaSupported() { return true; }
/*      */     
/*      */     private OSXMaskingContentPane installMaskingPane(Window w)
/*      */     {
/*      */       OSXMaskingContentPane content;
/* 1260 */       if ((w instanceof RootPaneContainer))
/*      */       {
/* 1262 */         RootPaneContainer rpc = (RootPaneContainer)w;
/* 1263 */         Container oldContent = rpc.getContentPane();
/* 1264 */         OSXMaskingContentPane content; if ((oldContent instanceof OSXMaskingContentPane)) {
/* 1265 */           content = (OSXMaskingContentPane)oldContent;
/*      */         }
/*      */         else {
/* 1268 */           OSXMaskingContentPane content = new OSXMaskingContentPane(oldContent);
/*      */           
/* 1270 */           rpc.setContentPane(content);
/*      */         }
/*      */       }
/*      */       else {
/* 1274 */         Component oldContent = w.getComponentCount() > 0 ? w.getComponent(0) : null;
/* 1275 */         OSXMaskingContentPane content; if ((oldContent instanceof OSXMaskingContentPane)) {
/* 1276 */           content = (OSXMaskingContentPane)oldContent;
/*      */         }
/*      */         else {
/* 1279 */           content = new OSXMaskingContentPane(oldContent);
/* 1280 */           w.add(content);
/*      */         }
/*      */       }
/* 1283 */       return content;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setWindowTransparent(Window w, boolean transparent)
/*      */     {
/* 1296 */       boolean isTransparent = (w.getBackground() != null) && (w.getBackground().getAlpha() == 0);
/* 1297 */       if (transparent != isTransparent) {
/* 1298 */         setBackgroundTransparent(w, transparent, "setWindowTransparent");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     private void fixWindowDragging(Window w, String context)
/*      */     {
/* 1305 */       if ((w instanceof RootPaneContainer)) {
/* 1306 */         JRootPane p = ((RootPaneContainer)w).getRootPane();
/* 1307 */         Boolean oldDraggable = (Boolean)p.getClientProperty("apple.awt.draggableWindowBackground");
/* 1308 */         if (oldDraggable == null) {
/* 1309 */           p.putClientProperty("apple.awt.draggableWindowBackground", Boolean.FALSE);
/* 1310 */           if (w.isDisplayable()) {
/* 1311 */             System.err.println(context + "(): To avoid content dragging, " + context + "() must be called before the window is realized, or " + "apple.awt.draggableWindowBackground" + " must be set to Boolean.FALSE before the window is realized.  If you really want content dragging, set " + "apple.awt.draggableWindowBackground" + " on the window's root pane to Boolean.TRUE before calling " + context + "() to hide this message.");
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setWindowAlpha(final Window w, final float alpha)
/*      */     {
/* 1326 */       if ((w instanceof RootPaneContainer)) {
/* 1327 */         JRootPane p = ((RootPaneContainer)w).getRootPane();
/* 1328 */         p.putClientProperty("Window.alpha", new Float(alpha));
/* 1329 */         fixWindowDragging(w, "setWindowAlpha");
/*      */       }
/* 1331 */       whenDisplayable(w, new Runnable() {
/*      */         public void run() {
/* 1333 */           Object peer = w.getPeer();
/*      */           
/*      */           try
/*      */           {
/* 1337 */             peer.getClass().getMethod("setAlpha", new Class[] { Float.TYPE }).invoke(peer, new Object[] { new Float(alpha) });
/*      */           }
/*      */           catch (Exception e) {}
/*      */         }
/*      */       });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     protected void setWindowMask(Component w, Raster raster)
/*      */     {
/* 1348 */       if (raster != null) {
/* 1349 */         setWindowMask(w, toShape(raster));
/*      */       }
/*      */       else {
/* 1352 */         setWindowMask(w, new Rectangle(0, 0, w.getWidth(), w
/* 1353 */           .getHeight()));
/*      */       }
/*      */     }
/*      */     
/*      */     public void setWindowMask(Component c, Shape shape) {
/* 1358 */       if ((c instanceof Window)) {
/* 1359 */         Window w = (Window)c;
/* 1360 */         OSXMaskingContentPane content = installMaskingPane(w);
/* 1361 */         content.setMask(shape);
/* 1362 */         setBackgroundTransparent(w, shape != WindowUtils.MASK_NONE, "setWindowMask");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     private static class OSXMaskingContentPane
/*      */       extends JPanel
/*      */     {
/*      */       private static final long serialVersionUID = 1L;
/*      */       
/*      */       private Shape shape;
/*      */       
/*      */ 
/*      */       public OSXMaskingContentPane(Component oldContent)
/*      */       {
/* 1377 */         super();
/* 1378 */         if (oldContent != null) {
/* 1379 */           add(oldContent, "Center");
/*      */         }
/*      */       }
/*      */       
/*      */       public void setMask(Shape shape) {
/* 1384 */         this.shape = shape;
/* 1385 */         repaint();
/*      */       }
/*      */       
/*      */       public void paint(Graphics graphics) {
/* 1389 */         Graphics2D g = (Graphics2D)graphics.create();
/* 1390 */         g.setComposite(AlphaComposite.Clear);
/* 1391 */         g.fillRect(0, 0, getWidth(), getHeight());
/* 1392 */         g.dispose();
/* 1393 */         if (this.shape != null) {
/* 1394 */           g = (Graphics2D)graphics.create();
/* 1395 */           g.setClip(this.shape);
/* 1396 */           super.paint(g);
/* 1397 */           g.dispose();
/*      */         }
/*      */         else {
/* 1400 */           super.paint(graphics);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     private void setBackgroundTransparent(Window w, boolean transparent, String context)
/*      */     {
/* 1407 */       JRootPane rp = (w instanceof RootPaneContainer) ? ((RootPaneContainer)w).getRootPane() : null;
/* 1408 */       if (transparent) {
/* 1409 */         if (rp != null) {
/* 1410 */           rp.putClientProperty("transparent-old-bg", w.getBackground());
/*      */         }
/* 1412 */         w.setBackground(new Color(0, 0, 0, 0));
/*      */ 
/*      */       }
/* 1415 */       else if (rp != null) {
/* 1416 */         Color bg = (Color)rp.getClientProperty("transparent-old-bg");
/*      */         
/*      */ 
/*      */ 
/* 1420 */         if (bg != null) {
/* 1421 */           bg = new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), bg.getAlpha());
/*      */         }
/* 1423 */         w.setBackground(bg);
/* 1424 */         rp.putClientProperty("transparent-old-bg", null);
/*      */       }
/*      */       else {
/* 1427 */         w.setBackground(null);
/*      */       }
/*      */       
/* 1430 */       fixWindowDragging(w, context); } }
/*      */   
/*      */   private static class X11WindowUtils extends WindowUtils.NativeWindowUtils { private boolean didCheck;
/*      */     private long[] alphaVisualIDs;
/*      */     private static final long OPAQUE = 4294967295L;
/*      */     private static final String OPACITY = "_NET_WM_WINDOW_OPACITY";
/*      */     
/* 1437 */     private static X11.Pixmap createBitmap(X11.Display dpy, X11.Window win, Raster raster) { X11 x11 = X11.INSTANCE;
/* 1438 */       Rectangle bounds = raster.getBounds();
/* 1439 */       int width = bounds.x + bounds.width;
/* 1440 */       int height = bounds.y + bounds.height;
/* 1441 */       X11.Pixmap pm = x11.XCreatePixmap(dpy, win, width, height, 1);
/* 1442 */       X11.GC gc = x11.XCreateGC(dpy, pm, new NativeLong(0L), null);
/* 1443 */       if (gc == null) {
/* 1444 */         return null;
/*      */       }
/* 1446 */       x11.XSetForeground(dpy, gc, new NativeLong(0L));
/* 1447 */       x11.XFillRectangle(dpy, pm, gc, 0, 0, width, height);
/* 1448 */       List<Rectangle> rlist = new ArrayList();
/*      */       try {
/* 1450 */         RasterRangesUtils.outputOccupiedRanges(raster, new RasterRangesUtils.RangesOutput() {
/*      */           public boolean outputRange(int x, int y, int w, int h) {
/* 1452 */             this.val$rlist.add(new Rectangle(x, y, w, h));
/* 1453 */             return true;
/*      */           }
/*      */           
/* 1456 */         });
/* 1457 */         X11.XRectangle[] rects = (X11.XRectangle[])new X11.XRectangle().toArray(rlist.size());
/* 1458 */         for (int i = 0; i < rects.length; i++) {
/* 1459 */           Rectangle r = (Rectangle)rlist.get(i);
/* 1460 */           rects[i].x = ((short)r.x);
/* 1461 */           rects[i].y = ((short)r.y);
/* 1462 */           rects[i].width = ((short)r.width);
/* 1463 */           rects[i].height = ((short)r.height);
/*      */           
/* 1465 */           Pointer p = rects[i].getPointer();
/* 1466 */           p.setShort(0L, (short)r.x);
/* 1467 */           p.setShort(2L, (short)r.y);
/* 1468 */           p.setShort(4L, (short)r.width);
/* 1469 */           p.setShort(6L, (short)r.height);
/* 1470 */           rects[i].setAutoSynch(false);
/*      */         }
/*      */         
/* 1473 */         int UNMASKED = 1;
/* 1474 */         x11.XSetForeground(dpy, gc, new NativeLong(1L));
/* 1475 */         x11.XFillRectangles(dpy, pm, gc, rects, rects.length);
/*      */       }
/*      */       finally {
/* 1478 */         x11.XFreeGC(dpy, gc);
/*      */       }
/* 1480 */       return pm;
/*      */     }
/*      */     
/*      */     private X11WindowUtils() {
/* 1484 */       this.alphaVisualIDs = new long[0];
/*      */     }
/*      */     
/* 1487 */     public boolean isWindowAlphaSupported() { return getAlphaVisualIDs().length > 0; }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private static long getVisualID(GraphicsConfiguration config)
/*      */     {
/*      */       try
/*      */       {
/* 1496 */         Object o = config.getClass().getMethod("getVisual", (Class[])null).invoke(config, (Object[])null);
/* 1497 */         return ((Number)o).longValue();
/*      */       }
/*      */       catch (Exception e) {
/* 1500 */         e.printStackTrace(); }
/* 1501 */       return -1L;
/*      */     }
/*      */     
/*      */ 
/*      */     public GraphicsConfiguration getAlphaCompatibleGraphicsConfiguration()
/*      */     {
/* 1507 */       if (isWindowAlphaSupported())
/*      */       {
/* 1509 */         GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
/* 1510 */         GraphicsDevice[] devices = env.getScreenDevices();
/* 1511 */         for (int i = 0; i < devices.length; i++)
/*      */         {
/* 1513 */           GraphicsConfiguration[] configs = devices[i].getConfigurations();
/* 1514 */           for (int j = 0; j < configs.length; j++) {
/* 1515 */             long visualID = getVisualID(configs[j]);
/* 1516 */             long[] ids = getAlphaVisualIDs();
/* 1517 */             for (int k = 0; k < ids.length; k++) {
/* 1518 */               if (visualID == ids[k]) {
/* 1519 */                 return configs[j];
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1525 */       return super.getAlphaCompatibleGraphicsConfiguration();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private synchronized long[] getAlphaVisualIDs()
/*      */     {
/* 1533 */       if (this.didCheck) {
/* 1534 */         return this.alphaVisualIDs;
/*      */       }
/* 1536 */       this.didCheck = true;
/* 1537 */       X11 x11 = X11.INSTANCE;
/* 1538 */       X11.Display dpy = x11.XOpenDisplay(null);
/* 1539 */       if (dpy == null)
/* 1540 */         return this.alphaVisualIDs;
/* 1541 */       X11.XVisualInfo info = null;
/*      */       try {
/* 1543 */         int screen = x11.XDefaultScreen(dpy);
/* 1544 */         X11.XVisualInfo template = new X11.XVisualInfo();
/* 1545 */         template.screen = screen;
/* 1546 */         template.depth = 32;
/* 1547 */         template.c_class = 4;
/* 1548 */         NativeLong mask = new NativeLong(14L);
/*      */         
/*      */ 
/* 1551 */         IntByReference pcount = new IntByReference();
/* 1552 */         info = x11.XGetVisualInfo(dpy, mask, template, pcount);
/* 1553 */         if (info != null) {
/* 1554 */           List<X11.VisualID> list = new ArrayList();
/*      */           
/* 1556 */           X11.XVisualInfo[] infos = (X11.XVisualInfo[])info.toArray(pcount.getValue());
/* 1557 */           for (int i = 0; i < infos.length; i++)
/*      */           {
/* 1559 */             X11.Xrender.XRenderPictFormat format = X11.Xrender.INSTANCE.XRenderFindVisualFormat(dpy, infos[i].visual);
/*      */             
/* 1561 */             if ((format.type == 1) && (format.direct.alphaMask != 0))
/*      */             {
/* 1563 */               list.add(infos[i].visualid);
/*      */             }
/*      */           }
/* 1566 */           this.alphaVisualIDs = new long[list.size()];
/* 1567 */           for (int i = 0; i < this.alphaVisualIDs.length; i++) {
/* 1568 */             this.alphaVisualIDs[i] = ((Number)list.get(i)).longValue();
/*      */           }
/* 1570 */           return this.alphaVisualIDs;
/*      */         }
/*      */       }
/*      */       finally {
/* 1574 */         if (info != null) {
/* 1575 */           x11.XFree(info.getPointer());
/*      */         }
/* 1577 */         x11.XCloseDisplay(dpy);
/*      */       }
/* 1579 */       return this.alphaVisualIDs;
/*      */     }
/*      */     
/*      */     private static X11.Window getContentWindow(Window w, X11.Display dpy, X11.Window win, Point offset)
/*      */     {
/* 1584 */       if ((((w instanceof Frame)) && (!((Frame)w).isUndecorated())) || (((w instanceof Dialog)) && 
/* 1585 */         (!((Dialog)w).isUndecorated()))) {
/* 1586 */         X11 x11 = X11.INSTANCE;
/* 1587 */         X11.WindowByReference rootp = new X11.WindowByReference();
/* 1588 */         X11.WindowByReference parentp = new X11.WindowByReference();
/* 1589 */         PointerByReference childrenp = new PointerByReference();
/* 1590 */         IntByReference countp = new IntByReference();
/* 1591 */         x11.XQueryTree(dpy, win, rootp, parentp, childrenp, countp);
/* 1592 */         Pointer p = childrenp.getValue();
/* 1593 */         int[] ids = p.getIntArray(0L, countp.getValue());
/* 1594 */         int[] arrayOfInt1 = ids;int i = arrayOfInt1.length;int j = 0; if (j < i) { int id = arrayOfInt1[j];
/*      */           
/* 1596 */           X11.Window child = new X11.Window(id);
/* 1597 */           X11.XWindowAttributes xwa = new X11.XWindowAttributes();
/* 1598 */           x11.XGetWindowAttributes(dpy, child, xwa);
/* 1599 */           offset.x = (-xwa.x);
/* 1600 */           offset.y = (-xwa.y);
/* 1601 */           win = child;
/*      */         }
/*      */         
/* 1604 */         if (p != null) {
/* 1605 */           x11.XFree(p);
/*      */         }
/*      */       }
/* 1608 */       return win;
/*      */     }
/*      */     
/*      */     private static X11.Window getDrawable(Component w) {
/* 1612 */       int id = (int)Native.getComponentID(w);
/* 1613 */       if (id == 0)
/* 1614 */         return null;
/* 1615 */       return new X11.Window(id);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void setWindowAlpha(final Window w, final float alpha)
/*      */     {
/* 1622 */       if (!isWindowAlphaSupported()) {
/* 1623 */         throw new UnsupportedOperationException("This X11 display does not provide a 32-bit visual");
/*      */       }
/* 1625 */       Runnable action = new Runnable() {
/*      */         public void run() {
/* 1627 */           X11 x11 = X11.INSTANCE;
/* 1628 */           X11.Display dpy = x11.XOpenDisplay(null);
/* 1629 */           if (dpy == null)
/* 1630 */             return;
/*      */           try {
/* 1632 */             X11.Window win = WindowUtils.X11WindowUtils.getDrawable(w);
/* 1633 */             if (alpha == 1.0F) {
/* 1634 */               x11.XDeleteProperty(dpy, win, x11
/* 1635 */                 .XInternAtom(dpy, "_NET_WM_WINDOW_OPACITY", false));
/*      */             }
/*      */             else
/*      */             {
/* 1639 */               int opacity = (int)((alpha * 4.2949673E9F) & 0xFFFFFFFFFFFFFFFF);
/* 1640 */               IntByReference patom = new IntByReference(opacity);
/* 1641 */               x11.XChangeProperty(dpy, win, x11
/* 1642 */                 .XInternAtom(dpy, "_NET_WM_WINDOW_OPACITY", false), X11.XA_CARDINAL, 32, 0, patom
/*      */                 
/*      */ 
/*      */ 
/* 1646 */                 .getPointer(), 1);
/*      */             }
/*      */           }
/*      */           finally {
/* 1650 */             x11.XCloseDisplay(dpy);
/*      */           }
/*      */         }
/* 1653 */       };
/* 1654 */       whenDisplayable(w, action);
/*      */     }
/*      */     
/*      */     private static abstract interface PixmapSource { public abstract X11.Pixmap getPixmap(X11.Display paramDisplay, X11.Window paramWindow); }
/*      */     
/*      */     private class X11TransparentContentPane extends WindowUtils.NativeWindowUtils.TransparentContentPane { private static final long serialVersionUID = 1L;
/*      */       
/* 1661 */       public X11TransparentContentPane(Container oldContent) { super(oldContent); }
/*      */       
/*      */ 
/*      */       private Memory buffer;
/*      */       private int[] pixels;
/* 1666 */       private final int[] pixel = new int[4];
/*      */       
/*      */ 
/*      */       protected void paintDirect(BufferedImage buf, Rectangle bounds)
/*      */       {
/* 1671 */         Window window = SwingUtilities.getWindowAncestor(this);
/* 1672 */         X11 x11 = X11.INSTANCE;
/* 1673 */         X11.Display dpy = x11.XOpenDisplay(null);
/* 1674 */         X11.Window win = WindowUtils.X11WindowUtils.getDrawable(window);
/* 1675 */         Point offset = new Point();
/* 1676 */         win = WindowUtils.X11WindowUtils.getContentWindow(window, dpy, win, offset);
/* 1677 */         X11.GC gc = x11.XCreateGC(dpy, win, new NativeLong(0L), null);
/*      */         
/* 1679 */         Raster raster = buf.getData();
/* 1680 */         int w = bounds.width;
/* 1681 */         int h = bounds.height;
/* 1682 */         if ((this.buffer == null) || (this.buffer.size() != w * h * 4)) {
/* 1683 */           this.buffer = new Memory(w * h * 4);
/* 1684 */           this.pixels = new int[w * h];
/*      */         }
/* 1686 */         for (int y = 0; y < h; y++) {
/* 1687 */           for (int x = 0; x < w; x++) {
/* 1688 */             raster.getPixel(x, y, this.pixel);
/* 1689 */             int alpha = this.pixel[3] & 0xFF;
/* 1690 */             int red = this.pixel[2] & 0xFF;
/* 1691 */             int green = this.pixel[1] & 0xFF;
/* 1692 */             int blue = this.pixel[0] & 0xFF;
/*      */             
/*      */ 
/* 1695 */             this.pixels[(y * w + x)] = (alpha << 24 | blue << 16 | green << 8 | red);
/*      */           }
/*      */         }
/* 1698 */         X11.XWindowAttributes xwa = new X11.XWindowAttributes();
/* 1699 */         x11.XGetWindowAttributes(dpy, win, xwa);
/*      */         
/* 1701 */         X11.XImage image = x11.XCreateImage(dpy, xwa.visual, 32, 2, 0, this.buffer, w, h, 32, w * 4);
/*      */         
/* 1703 */         this.buffer.write(0L, this.pixels, 0, this.pixels.length);
/* 1704 */         offset.x += bounds.x;
/* 1705 */         offset.y += bounds.y;
/* 1706 */         x11.XPutImage(dpy, win, gc, image, 0, 0, offset.x, offset.y, w, h);
/*      */         
/* 1708 */         x11.XFree(image.getPointer());
/* 1709 */         x11.XFreeGC(dpy, gc);
/* 1710 */         x11.XCloseDisplay(dpy);
/*      */       }
/*      */     }
/*      */     
/*      */     public void setWindowTransparent(final Window w, final boolean transparent)
/*      */     {
/* 1716 */       if (!(w instanceof RootPaneContainer)) {
/* 1717 */         throw new IllegalArgumentException("Window must be a RootPaneContainer");
/*      */       }
/* 1719 */       if (!isWindowAlphaSupported()) {
/* 1720 */         throw new UnsupportedOperationException("This X11 display does not provide a 32-bit visual");
/*      */       }
/*      */       
/* 1723 */       if (!w.getGraphicsConfiguration().equals(getAlphaCompatibleGraphicsConfiguration())) {
/* 1724 */         throw new IllegalArgumentException("Window GraphicsConfiguration '" + w.getGraphicsConfiguration() + "' does not support transparency");
/*      */       }
/*      */       
/* 1727 */       boolean isTransparent = (w.getBackground() != null) && (w.getBackground().getAlpha() == 0);
/* 1728 */       if (transparent == isTransparent)
/* 1729 */         return;
/* 1730 */       whenDisplayable(w, new Runnable() {
/*      */         public void run() {
/* 1732 */           JRootPane root = ((RootPaneContainer)w).getRootPane();
/* 1733 */           JLayeredPane lp = root.getLayeredPane();
/* 1734 */           Container content = root.getContentPane();
/* 1735 */           if ((content instanceof WindowUtils.X11WindowUtils.X11TransparentContentPane)) {
/* 1736 */             ((WindowUtils.X11WindowUtils.X11TransparentContentPane)content).setTransparent(transparent);
/*      */           }
/* 1738 */           else if (transparent) {
/* 1739 */             WindowUtils.X11WindowUtils.X11TransparentContentPane x11content = new WindowUtils.X11WindowUtils.X11TransparentContentPane(WindowUtils.X11WindowUtils.this, content);
/*      */             
/* 1741 */             root.setContentPane(x11content);
/* 1742 */             lp.add(new WindowUtils.RepaintTrigger(x11content), JLayeredPane.DRAG_LAYER);
/*      */           }
/*      */           
/* 1745 */           WindowUtils.X11WindowUtils.this.setLayersTransparent(w, transparent);
/* 1746 */           WindowUtils.X11WindowUtils.this.setForceHeavyweightPopups(w, transparent);
/* 1747 */           WindowUtils.X11WindowUtils.this.setDoubleBuffered(w, !transparent);
/*      */         }
/*      */       });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private void setWindowShape(final Window w, final PixmapSource src)
/*      */     {
/* 1757 */       Runnable action = new Runnable() {
/*      */         public void run() {
/* 1759 */           X11 x11 = X11.INSTANCE;
/* 1760 */           X11.Display dpy = x11.XOpenDisplay(null);
/* 1761 */           if (dpy == null) {
/* 1762 */             return;
/*      */           }
/* 1764 */           X11.Pixmap pm = null;
/*      */           try {
/* 1766 */             X11.Window win = WindowUtils.X11WindowUtils.getDrawable(w);
/* 1767 */             pm = src.getPixmap(dpy, win);
/* 1768 */             X11.Xext ext = X11.Xext.INSTANCE;
/* 1769 */             ext.XShapeCombineMask(dpy, win, 0, 0, 0, pm == null ? X11.Pixmap.None : pm, 0);
/*      */ 
/*      */           }
/*      */           finally
/*      */           {
/* 1774 */             if (pm != null) {
/* 1775 */               x11.XFreePixmap(dpy, pm);
/*      */             }
/* 1777 */             x11.XCloseDisplay(dpy);
/*      */           }
/* 1779 */           WindowUtils.X11WindowUtils.this.setForceHeavyweightPopups(WindowUtils.X11WindowUtils.this.getWindow(w), pm != null);
/*      */         }
/* 1781 */       };
/* 1782 */       whenDisplayable(w, action);
/*      */     }
/*      */     
/*      */     protected void setMask(Component w, final Raster raster) {
/* 1786 */       setWindowShape(getWindow(w), new PixmapSource() {
/*      */         public X11.Pixmap getPixmap(X11.Display dpy, X11.Window win) {
/* 1788 */           return raster != null ? WindowUtils.X11WindowUtils.createBitmap(dpy, win, raster) : null;
/*      */         }
/*      */       });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void setWindowMask(Window w, Shape mask)
/*      */   {
/* 1800 */     getInstance().setWindowMask(w, mask);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void setComponentMask(Component c, Shape mask)
/*      */   {
/* 1809 */     getInstance().setWindowMask(c, mask);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void setWindowMask(Window w, Icon mask)
/*      */   {
/* 1818 */     getInstance().setWindowMask(w, mask);
/*      */   }
/*      */   
/*      */   public static boolean isWindowAlphaSupported()
/*      */   {
/* 1823 */     return getInstance().isWindowAlphaSupported();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static GraphicsConfiguration getAlphaCompatibleGraphicsConfiguration()
/*      */   {
/* 1831 */     return getInstance().getAlphaCompatibleGraphicsConfiguration();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void setWindowAlpha(Window w, float alpha)
/*      */   {
/* 1849 */     getInstance().setWindowAlpha(w, Math.max(0.0F, Math.min(alpha, 1.0F)));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void setWindowTransparent(Window w, boolean transparent)
/*      */   {
/* 1865 */     getInstance().setWindowTransparent(w, transparent);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static BufferedImage getWindowIcon(WinDef.HWND hwnd)
/*      */   {
/* 1878 */     return getInstance().getWindowIcon(hwnd);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Dimension getIconSize(WinDef.HICON hIcon)
/*      */   {
/* 1890 */     return getInstance().getIconSize(hIcon);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static List<DesktopWindow> getAllWindows(boolean onlyVisibleWindows)
/*      */   {
/* 1908 */     return getInstance().getAllWindows(onlyVisibleWindows);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getWindowTitle(WinDef.HWND hwnd)
/*      */   {
/* 1921 */     return getInstance().getWindowTitle(hwnd);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getProcessFilePath(WinDef.HWND hwnd)
/*      */   {
/* 1935 */     return getInstance().getProcessFilePath(hwnd);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Rectangle getWindowLocationAndSize(WinDef.HWND hwnd)
/*      */   {
/* 1947 */     return getInstance().getWindowLocationAndSize(hwnd);
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\platform\WindowUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */