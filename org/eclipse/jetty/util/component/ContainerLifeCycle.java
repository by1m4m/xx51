/*     */ package org.eclipse.jetty.util.component;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import org.eclipse.jetty.util.MultiException;
/*     */ import org.eclipse.jetty.util.annotation.ManagedObject;
/*     */ import org.eclipse.jetty.util.annotation.ManagedOperation;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
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
/*     */ @ManagedObject("Implementation of Container and LifeCycle")
/*     */ public class ContainerLifeCycle
/*     */   extends AbstractLifeCycle
/*     */   implements Container, Destroyable, Dumpable
/*     */ {
/*  80 */   private static final Logger LOG = Log.getLogger(ContainerLifeCycle.class);
/*  81 */   private final List<Bean> _beans = new CopyOnWriteArrayList();
/*  82 */   private final List<Container.Listener> _listeners = new CopyOnWriteArrayList();
/*     */   
/*     */   private boolean _doStarted;
/*     */   
/*     */   private boolean _destroyed;
/*     */   
/*     */ 
/*     */   protected void doStart()
/*     */     throws Exception
/*     */   {
/*  92 */     if (this._destroyed) {
/*  93 */       throw new IllegalStateException("Destroyed container cannot be restarted");
/*     */     }
/*     */     
/*  96 */     this._doStarted = true;
/*     */     
/*     */ 
/*  99 */     for (Bean b : this._beans)
/*     */     {
/* 101 */       if ((b._bean instanceof LifeCycle))
/*     */       {
/* 103 */         LifeCycle l = (LifeCycle)b._bean;
/* 104 */         switch (b._managed)
/*     */         {
/*     */         case MANAGED: 
/* 107 */           if (!l.isRunning()) {
/* 108 */             start(l);
/*     */           }
/*     */           break;
/*     */         case AUTO: 
/* 112 */           if (l.isRunning()) {
/* 113 */             unmanage(b);
/*     */           }
/*     */           else {
/* 116 */             manage(b);
/* 117 */             start(l);
/*     */           }
/*     */           
/*     */ 
/*     */           break;
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */     
/* 127 */     super.doStart();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void start(LifeCycle l)
/*     */     throws Exception
/*     */   {
/* 138 */     l.start();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void stop(LifeCycle l)
/*     */     throws Exception
/*     */   {
/* 149 */     l.stop();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doStop()
/*     */     throws Exception
/*     */   {
/* 158 */     this._doStarted = false;
/* 159 */     super.doStop();
/* 160 */     List<Bean> reverse = new ArrayList(this._beans);
/* 161 */     Collections.reverse(reverse);
/* 162 */     MultiException mex = new MultiException();
/* 163 */     for (Bean b : reverse)
/*     */     {
/* 165 */       if ((b._managed == Managed.MANAGED) && ((b._bean instanceof LifeCycle)))
/*     */       {
/* 167 */         LifeCycle l = (LifeCycle)b._bean;
/*     */         try
/*     */         {
/* 170 */           stop(l);
/*     */         }
/*     */         catch (Throwable th)
/*     */         {
/* 174 */           mex.add(th);
/*     */         }
/*     */       }
/*     */     }
/* 178 */     mex.ifExceptionThrow();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void destroy()
/*     */   {
/* 187 */     this._destroyed = true;
/* 188 */     List<Bean> reverse = new ArrayList(this._beans);
/* 189 */     Collections.reverse(reverse);
/* 190 */     for (Bean b : reverse)
/*     */     {
/* 192 */       if (((b._bean instanceof Destroyable)) && ((b._managed == Managed.MANAGED) || (b._managed == Managed.POJO)))
/*     */       {
/* 194 */         Destroyable d = (Destroyable)b._bean;
/*     */         try
/*     */         {
/* 197 */           d.destroy();
/*     */         }
/*     */         catch (Throwable th)
/*     */         {
/* 201 */           LOG.warn(th);
/*     */         }
/*     */       }
/*     */     }
/* 205 */     this._beans.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean contains(Object bean)
/*     */   {
/* 214 */     for (Bean b : this._beans)
/* 215 */       if (b._bean == bean)
/* 216 */         return true;
/* 217 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isManaged(Object bean)
/*     */   {
/* 227 */     for (Bean b : this._beans)
/* 228 */       if (b._bean == bean)
/* 229 */         return b.isManaged();
/* 230 */     return false;
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
/*     */   public boolean addBean(Object o)
/*     */   {
/* 247 */     if ((o instanceof LifeCycle))
/*     */     {
/* 249 */       LifeCycle l = (LifeCycle)o;
/* 250 */       return addBean(o, l.isRunning() ? Managed.UNMANAGED : Managed.AUTO);
/*     */     }
/*     */     
/* 253 */     return addBean(o, Managed.POJO);
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
/*     */   public boolean addBean(Object o, boolean managed)
/*     */   {
/* 266 */     if ((o instanceof LifeCycle))
/* 267 */       return addBean(o, managed ? Managed.MANAGED : Managed.UNMANAGED);
/* 268 */     return addBean(o, managed ? Managed.POJO : Managed.UNMANAGED);
/*     */   }
/*     */   
/*     */   public boolean addBean(Object o, Managed managed)
/*     */   {
/* 273 */     if ((o == null) || (contains(o))) {
/* 274 */       return false;
/*     */     }
/* 276 */     Bean new_bean = new Bean(o, null);
/*     */     
/*     */ 
/* 279 */     if ((o instanceof Container.Listener)) {
/* 280 */       addEventListener((Container.Listener)o);
/*     */     }
/*     */     
/* 283 */     this._beans.add(new_bean);
/*     */     
/*     */ 
/* 286 */     for (Container.Listener l : this._listeners) {
/* 287 */       l.beanAdded(this, o);
/*     */     }
/*     */     try
/*     */     {
/* 291 */       switch (managed)
/*     */       {
/*     */       case UNMANAGED: 
/* 294 */         unmanage(new_bean);
/* 295 */         break;
/*     */       
/*     */       case MANAGED: 
/* 298 */         manage(new_bean);
/*     */         
/* 300 */         if ((isStarting()) && (this._doStarted))
/*     */         {
/* 302 */           LifeCycle l = (LifeCycle)o;
/* 303 */           if (!l.isRunning())
/* 304 */             start(l); }
/* 305 */         break;
/*     */       
/*     */ 
/*     */       case AUTO: 
/* 309 */         if ((o instanceof LifeCycle))
/*     */         {
/* 311 */           LifeCycle l = (LifeCycle)o;
/* 312 */           if (isStarting())
/*     */           {
/* 314 */             if (l.isRunning()) {
/* 315 */               unmanage(new_bean);
/* 316 */             } else if (this._doStarted)
/*     */             {
/* 318 */               manage(new_bean);
/* 319 */               start(l);
/*     */             }
/*     */             else {
/* 322 */               new_bean._managed = Managed.AUTO;
/*     */             }
/* 324 */           } else if (isStarted()) {
/* 325 */             unmanage(new_bean);
/*     */           } else {
/* 327 */             new_bean._managed = Managed.AUTO;
/*     */           }
/*     */         } else {
/* 330 */           new_bean._managed = Managed.POJO; }
/* 331 */         break;
/*     */       
/*     */       case POJO: 
/* 334 */         new_bean._managed = Managed.POJO;
/*     */       }
/*     */     }
/*     */     catch (RuntimeException|Error e)
/*     */     {
/* 339 */       throw e;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 343 */       throw new RuntimeException(e);
/*     */     }
/*     */     
/* 346 */     if (LOG.isDebugEnabled()) {
/* 347 */       LOG.debug("{} added {}", new Object[] { this, new_bean });
/*     */     }
/* 349 */     return true;
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
/*     */   public void addManaged(LifeCycle lifecycle)
/*     */   {
/* 362 */     addBean(lifecycle, true);
/*     */     try
/*     */     {
/* 365 */       if ((isRunning()) && (!lifecycle.isRunning())) {
/* 366 */         start(lifecycle);
/*     */       }
/*     */     }
/*     */     catch (RuntimeException|Error e) {
/* 370 */       throw e;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 374 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void addEventListener(Container.Listener listener)
/*     */   {
/* 381 */     if (this._listeners.contains(listener)) {
/* 382 */       return;
/*     */     }
/* 384 */     this._listeners.add(listener);
/*     */     
/*     */ 
/* 387 */     for (Bean b : this._beans)
/*     */     {
/* 389 */       listener.beanAdded(this, b._bean);
/*     */       
/*     */ 
/* 392 */       if (((listener instanceof Container.InheritedListener)) && (b.isManaged()) && ((b._bean instanceof Container)))
/*     */       {
/* 394 */         if ((b._bean instanceof ContainerLifeCycle)) {
/* 395 */           ((ContainerLifeCycle)b._bean).addBean(listener, false);
/*     */         } else {
/* 397 */           ((Container)b._bean).addBean(listener);
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
/*     */   public void manage(Object bean)
/*     */   {
/* 411 */     for (Bean b : this._beans)
/*     */     {
/* 413 */       if (b._bean == bean)
/*     */       {
/* 415 */         manage(b);
/* 416 */         return;
/*     */       }
/*     */     }
/* 419 */     throw new IllegalArgumentException("Unknown bean " + bean);
/*     */   }
/*     */   
/*     */   private void manage(Bean bean)
/*     */   {
/* 424 */     if (bean._managed != Managed.MANAGED)
/*     */     {
/* 426 */       bean._managed = Managed.MANAGED;
/*     */       
/* 428 */       if ((bean._bean instanceof Container))
/*     */       {
/* 430 */         for (Container.Listener l : this._listeners)
/*     */         {
/* 432 */           if ((l instanceof Container.InheritedListener))
/*     */           {
/* 434 */             if ((bean._bean instanceof ContainerLifeCycle)) {
/* 435 */               ((ContainerLifeCycle)bean._bean).addBean(l, false);
/*     */             } else {
/* 437 */               ((Container)bean._bean).addBean(l);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 442 */       if ((bean._bean instanceof AbstractLifeCycle))
/*     */       {
/* 444 */         ((AbstractLifeCycle)bean._bean).setStopTimeout(getStopTimeout());
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
/*     */   public void unmanage(Object bean)
/*     */   {
/* 458 */     for (Bean b : this._beans)
/*     */     {
/* 460 */       if (b._bean == bean)
/*     */       {
/* 462 */         unmanage(b);
/* 463 */         return;
/*     */       }
/*     */     }
/* 466 */     throw new IllegalArgumentException("Unknown bean " + bean);
/*     */   }
/*     */   
/*     */   private void unmanage(Bean bean)
/*     */   {
/* 471 */     if (bean._managed != Managed.UNMANAGED)
/*     */     {
/* 473 */       if ((bean._managed == Managed.MANAGED) && ((bean._bean instanceof Container)))
/*     */       {
/* 475 */         for (Container.Listener l : this._listeners)
/*     */         {
/* 477 */           if ((l instanceof Container.InheritedListener))
/* 478 */             ((Container)bean._bean).removeBean(l);
/*     */         }
/*     */       }
/* 481 */       bean._managed = Managed.UNMANAGED;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Collection<Object> getBeans()
/*     */   {
/* 488 */     return getBeans(Object.class);
/*     */   }
/*     */   
/*     */   public void setBeans(Collection<Object> beans)
/*     */   {
/* 493 */     for (Object bean : beans) {
/* 494 */       addBean(bean);
/*     */     }
/*     */   }
/*     */   
/*     */   public <T> Collection<T> getBeans(Class<T> clazz)
/*     */   {
/* 500 */     ArrayList<T> beans = new ArrayList();
/* 501 */     for (Bean b : this._beans)
/*     */     {
/* 503 */       if (clazz.isInstance(b._bean))
/* 504 */         beans.add(clazz.cast(b._bean));
/*     */     }
/* 506 */     return beans;
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> T getBean(Class<T> clazz)
/*     */   {
/* 512 */     for (Bean b : this._beans)
/*     */     {
/* 514 */       if (clazz.isInstance(b._bean))
/* 515 */         return (T)clazz.cast(b._bean);
/*     */     }
/* 517 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeBeans()
/*     */   {
/* 525 */     ArrayList<Bean> beans = new ArrayList(this._beans);
/* 526 */     for (Bean b : beans) {
/* 527 */       remove(b);
/*     */     }
/*     */   }
/*     */   
/*     */   private Bean getBean(Object o) {
/* 532 */     for (Bean b : this._beans)
/*     */     {
/* 534 */       if (b._bean == o)
/* 535 */         return b;
/*     */     }
/* 537 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean removeBean(Object o)
/*     */   {
/* 543 */     Bean b = getBean(o);
/* 544 */     return (b != null) && (remove(b));
/*     */   }
/*     */   
/*     */   private boolean remove(Bean bean)
/*     */   {
/* 549 */     if (this._beans.remove(bean))
/*     */     {
/* 551 */       boolean wasManaged = bean.isManaged();
/*     */       
/* 553 */       unmanage(bean);
/*     */       
/* 555 */       for (Container.Listener l : this._listeners) {
/* 556 */         l.beanRemoved(this, bean._bean);
/*     */       }
/* 558 */       if ((bean._bean instanceof Container.Listener)) {
/* 559 */         removeEventListener((Container.Listener)bean._bean);
/*     */       }
/*     */       
/* 562 */       if ((wasManaged) && ((bean._bean instanceof LifeCycle)))
/*     */       {
/*     */         try
/*     */         {
/* 566 */           stop((LifeCycle)bean._bean);
/*     */         }
/*     */         catch (RuntimeException|Error e)
/*     */         {
/* 570 */           throw e;
/*     */         }
/*     */         catch (Exception e)
/*     */         {
/* 574 */           throw new RuntimeException(e);
/*     */         }
/*     */       }
/* 577 */       return true;
/*     */     }
/* 579 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void removeEventListener(Container.Listener listener)
/*     */   {
/* 585 */     if (this._listeners.remove(listener))
/*     */     {
/*     */ 
/* 588 */       for (Bean b : this._beans)
/*     */       {
/* 590 */         listener.beanRemoved(this, b._bean);
/*     */         
/* 592 */         if (((listener instanceof Container.InheritedListener)) && (b.isManaged()) && ((b._bean instanceof Container))) {
/* 593 */           ((Container)b._bean).removeBean(listener);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setStopTimeout(long stopTimeout)
/*     */   {
/* 601 */     super.setStopTimeout(stopTimeout);
/* 602 */     for (Bean bean : this._beans)
/*     */     {
/* 604 */       if ((bean.isManaged()) && ((bean._bean instanceof AbstractLifeCycle))) {
/* 605 */         ((AbstractLifeCycle)bean._bean).setStopTimeout(stopTimeout);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @ManagedOperation("Dump the object to stderr")
/*     */   public void dumpStdErr()
/*     */   {
/*     */     try
/*     */     {
/* 618 */       dump(System.err, "");
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 622 */       LOG.warn(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @ManagedOperation("Dump the object to a string")
/*     */   public String dump()
/*     */   {
/* 630 */     return dump(this);
/*     */   }
/*     */   
/*     */   public static String dump(Dumpable dumpable)
/*     */   {
/* 635 */     StringBuilder b = new StringBuilder();
/*     */     try
/*     */     {
/* 638 */       dumpable.dump(b, "");
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 642 */       LOG.warn(e);
/*     */     }
/* 644 */     return b.toString();
/*     */   }
/*     */   
/*     */   public void dump(Appendable out) throws IOException
/*     */   {
/* 649 */     dump(out, "");
/*     */   }
/*     */   
/*     */   protected void dumpThis(Appendable out) throws IOException
/*     */   {
/* 654 */     out.append(String.valueOf(this)).append(" - ").append(getState()).append("\n");
/*     */   }
/*     */   
/*     */   public static void dumpObject(Appendable out, Object o) throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 661 */       if ((o instanceof LifeCycle)) {
/* 662 */         out.append(String.valueOf(o)).append(" - ").append(AbstractLifeCycle.getState((LifeCycle)o)).append("\n");
/*     */       } else {
/* 664 */         out.append(String.valueOf(o)).append("\n");
/*     */       }
/*     */     }
/*     */     catch (Throwable th) {
/* 668 */       out.append(" => ").append(th.toString()).append('\n');
/*     */     }
/*     */   }
/*     */   
/*     */   public void dump(Appendable out, String indent)
/*     */     throws IOException
/*     */   {
/* 675 */     dumpBeans(out, indent, new Collection[0]);
/*     */   }
/*     */   
/*     */   protected void dumpBeans(Appendable out, String indent, Collection<?>... collections) throws IOException
/*     */   {
/* 680 */     dumpThis(out);
/* 681 */     int size = this._beans.size();
/* 682 */     for (c : collections)
/* 683 */       size += c.size();
/* 684 */     int i = 0;
/* 685 */     for (Object localObject1 = this._beans.iterator(); ((Iterator)localObject1).hasNext();) { b = (Bean)((Iterator)localObject1).next();
/*     */       
/* 687 */       i++;
/* 688 */       switch (b._managed)
/*     */       {
/*     */       case POJO: 
/* 691 */         out.append(indent).append(" +- ");
/* 692 */         if ((b._bean instanceof Dumpable)) {
/* 693 */           ((Dumpable)b._bean).dump(out, indent + (i == size ? "    " : " |  "));
/*     */         } else
/* 695 */           dumpObject(out, b._bean);
/* 696 */         break;
/*     */       
/*     */       case MANAGED: 
/* 699 */         out.append(indent).append(" += ");
/* 700 */         if ((b._bean instanceof Dumpable)) {
/* 701 */           ((Dumpable)b._bean).dump(out, indent + (i == size ? "    " : " |  "));
/*     */         } else
/* 703 */           dumpObject(out, b._bean);
/* 704 */         break;
/*     */       
/*     */       case UNMANAGED: 
/* 707 */         out.append(indent).append(" +~ ");
/* 708 */         dumpObject(out, b._bean);
/* 709 */         break;
/*     */       
/*     */       case AUTO: 
/* 712 */         out.append(indent).append(" +? ");
/* 713 */         if ((b._bean instanceof Dumpable)) {
/* 714 */           ((Dumpable)b._bean).dump(out, indent + (i == size ? "    " : " |  "));
/*     */         } else
/* 716 */           dumpObject(out, b._bean);
/*     */         break;
/*     */       }
/*     */     }
/*     */     Bean b;
/* 721 */     localObject1 = collections;Collection<?> localCollection1 = localObject1.length; for (Collection<?> c = 0; c < localCollection1; c++) { Collection<?> c = localObject1[c];
/*     */       
/* 723 */       for (Object o : c)
/*     */       {
/* 725 */         i++;
/* 726 */         out.append(indent).append(" +> ");
/* 727 */         if ((o instanceof Dumpable)) {
/* 728 */           ((Dumpable)o).dump(out, indent + (i == size ? "    " : " |  "));
/*     */         } else {
/* 730 */           dumpObject(out, o);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void dump(Appendable out, String indent, Collection<?>... collections) throws IOException {
/* 737 */     if (collections.length == 0)
/* 738 */       return;
/* 739 */     int size = 0;
/* 740 */     Collection<?>[] arrayOfCollection1 = collections;int i = arrayOfCollection1.length; for (Collection<?> localCollection1 = 0; localCollection1 < i; localCollection1++) { c = arrayOfCollection1[localCollection1];
/* 741 */       size += c.size(); }
/* 742 */     if (size == 0) {
/* 743 */       return;
/*     */     }
/* 745 */     int i = 0;
/* 746 */     Collection<?>[] arrayOfCollection2 = collections;localCollection1 = arrayOfCollection2.length; for (Collection<?> c = 0; c < localCollection1; c++) { Collection<?> c = arrayOfCollection2[c];
/*     */       
/* 748 */       for (Object o : c)
/*     */       {
/* 750 */         i++;
/* 751 */         out.append(indent).append(" +- ");
/*     */         
/* 753 */         if ((o instanceof Dumpable)) {
/* 754 */           ((Dumpable)o).dump(out, indent + (i == size ? "    " : " |  "));
/*     */         } else
/* 756 */           dumpObject(out, o);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/* 761 */   static enum Managed { POJO,  MANAGED,  UNMANAGED,  AUTO;
/*     */     
/*     */     private Managed() {} }
/*     */   
/*     */   private static class Bean { private final Object _bean;
/* 766 */     private volatile ContainerLifeCycle.Managed _managed = ContainerLifeCycle.Managed.POJO;
/*     */     
/*     */     private Bean(Object b)
/*     */     {
/* 770 */       if (b == null)
/* 771 */         throw new NullPointerException();
/* 772 */       this._bean = b;
/*     */     }
/*     */     
/*     */     public boolean isManaged()
/*     */     {
/* 777 */       return this._managed == ContainerLifeCycle.Managed.MANAGED;
/*     */     }
/*     */     
/*     */     public boolean isManageable()
/*     */     {
/* 782 */       switch (ContainerLifeCycle.1.$SwitchMap$org$eclipse$jetty$util$component$ContainerLifeCycle$Managed[this._managed.ordinal()])
/*     */       {
/*     */       case 1: 
/* 785 */         return true;
/*     */       case 2: 
/* 787 */         return ((this._bean instanceof LifeCycle)) && (((LifeCycle)this._bean).isStopped());
/*     */       }
/* 789 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public String toString()
/*     */     {
/* 796 */       return String.format("{%s,%s}", new Object[] { this._bean, this._managed });
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateBean(Object oldBean, Object newBean)
/*     */   {
/* 802 */     if (newBean != oldBean)
/*     */     {
/* 804 */       if (oldBean != null)
/* 805 */         removeBean(oldBean);
/* 806 */       if (newBean != null) {
/* 807 */         addBean(newBean);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateBean(Object oldBean, Object newBean, boolean managed) {
/* 813 */     if (newBean != oldBean)
/*     */     {
/* 815 */       if (oldBean != null)
/* 816 */         removeBean(oldBean);
/* 817 */       if (newBean != null) {
/* 818 */         addBean(newBean, managed);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateBeans(Object[] oldBeans, Object[] newBeans)
/*     */   {
/* 825 */     if (oldBeans != null) {
/*     */       label78:
/* 827 */       for (Object o : oldBeans)
/*     */       {
/* 829 */         if (newBeans != null)
/*     */         {
/* 831 */           for (Object n : newBeans)
/* 832 */             if (o == n)
/*     */               break label78;
/*     */         }
/* 835 */         removeBean(o);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 840 */     if (newBeans != null) {
/*     */       label162:
/* 842 */       for (Object n : newBeans)
/*     */       {
/* 844 */         if (oldBeans != null)
/*     */         {
/* 846 */           for (Object o : oldBeans)
/* 847 */             if (o == n)
/*     */               break label162;
/*     */         }
/* 850 */         addBean(n);
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
/*     */   public <T> Collection<T> getContainedBeans(Class<T> clazz)
/*     */   {
/* 864 */     Set<T> beans = new HashSet();
/* 865 */     getContainedBeans(clazz, beans);
/* 866 */     return beans;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected <T> void getContainedBeans(Class<T> clazz, Collection<T> beans)
/*     */   {
/* 876 */     beans.addAll(getBeans(clazz));
/* 877 */     for (Container c : getBeans(Container.class))
/*     */     {
/* 879 */       Bean bean = getBean(c);
/* 880 */       if ((bean != null) && (bean.isManageable()))
/*     */       {
/* 882 */         if ((c instanceof ContainerLifeCycle)) {
/* 883 */           ((ContainerLifeCycle)c).getContainedBeans(clazz, beans);
/*     */         } else {
/* 885 */           beans.addAll(c.getContainedBeans(clazz));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\component\ContainerLifeCycle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */