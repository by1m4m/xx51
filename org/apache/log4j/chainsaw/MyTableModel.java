/*     */ package org.apache.log4j.chainsaw;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import javax.swing.table.AbstractTableModel;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.apache.log4j.Priority;
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
/*     */ class MyTableModel
/*     */   extends AbstractTableModel
/*     */ {
/*  42 */   private static final Logger LOG = Logger.getLogger(MyTableModel.class);
/*     */   
/*     */ 
/*  45 */   private static final Comparator MY_COMP = new Comparator()
/*     */   {
/*     */     public int compare(Object aObj1, Object aObj2)
/*     */     {
/*  49 */       if ((aObj1 == null) && (aObj2 == null))
/*  50 */         return 0;
/*  51 */       if (aObj1 == null)
/*  52 */         return -1;
/*  53 */       if (aObj2 == null) {
/*  54 */         return 1;
/*     */       }
/*     */       
/*     */ 
/*  58 */       EventDetails le1 = (EventDetails)aObj1;
/*  59 */       EventDetails le2 = (EventDetails)aObj2;
/*     */       
/*  61 */       if (le1.getTimeStamp() < le2.getTimeStamp()) {
/*  62 */         return 1;
/*     */       }
/*     */       
/*  65 */       return -1;
/*     */     }
/*     */   };
/*     */   
/*     */   private class Processor implements Runnable
/*     */   {
/*     */     Processor(MyTableModel.1 x1)
/*     */     {
/*  73 */       this();
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/*     */       for (;;) {
/*     */         try {
/*  80 */           Thread.sleep(1000L);
/*     */         }
/*     */         catch (InterruptedException e) {}
/*     */         
/*     */ 
/*  85 */         synchronized (MyTableModel.this.mLock) {
/*  86 */           if (!MyTableModel.this.mPaused)
/*     */           {
/*     */ 
/*     */ 
/*  90 */             boolean toHead = true;
/*  91 */             boolean needUpdate = false;
/*  92 */             Iterator it = MyTableModel.this.mPendingEvents.iterator();
/*  93 */             while (it.hasNext()) {
/*  94 */               EventDetails event = (EventDetails)it.next();
/*  95 */               MyTableModel.this.mAllEvents.add(event);
/*  96 */               toHead = (toHead) && (event == MyTableModel.this.mAllEvents.first());
/*  97 */               needUpdate = (needUpdate) || (MyTableModel.this.matchFilter(event));
/*     */             }
/*  99 */             MyTableModel.this.mPendingEvents.clear();
/*     */             
/* 101 */             if (needUpdate) {
/* 102 */               MyTableModel.this.updateFilteredEvents(toHead);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private Processor() {}
/*     */   }
/*     */   
/* 112 */   private static final String[] COL_NAMES = { "Time", "Priority", "Trace", "Category", "NDC", "Message" };
/*     */   
/*     */ 
/*     */ 
/* 116 */   private static final EventDetails[] EMPTY_LIST = new EventDetails[0];
/*     */   
/*     */ 
/* 119 */   private static final DateFormat DATE_FORMATTER = DateFormat.getDateTimeInstance(3, 2);
/*     */   
/*     */ 
/*     */ 
/* 123 */   private final Object mLock = new Object();
/*     */   
/* 125 */   private final SortedSet mAllEvents = new TreeSet(MY_COMP);
/*     */   
/* 127 */   private EventDetails[] mFilteredEvents = EMPTY_LIST;
/*     */   
/* 129 */   private final List mPendingEvents = new ArrayList();
/*     */   
/* 131 */   private boolean mPaused = false;
/*     */   
/*     */ 
/* 134 */   private String mThreadFilter = "";
/*     */   
/* 136 */   private String mMessageFilter = "";
/*     */   
/* 138 */   private String mNDCFilter = "";
/*     */   
/* 140 */   private String mCategoryFilter = "";
/*     */   
/* 142 */   private Priority mPriorityFilter = Priority.DEBUG;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   MyTableModel()
/*     */   {
/* 150 */     Thread t = new Thread(new Processor(null));
/* 151 */     t.setDaemon(true);
/* 152 */     t.start();
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public int getRowCount()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 6	org/apache/log4j/chainsaw/MyTableModel:mLock	Ljava/lang/Object;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 19	org/apache/log4j/chainsaw/MyTableModel:mFilteredEvents	[Lorg/apache/log4j/chainsaw/EventDetails;
/*     */     //   11: arraylength
/*     */     //   12: aload_1
/*     */     //   13: monitorexit
/*     */     //   14: ireturn
/*     */     //   15: astore_2
/*     */     //   16: aload_1
/*     */     //   17: monitorexit
/*     */     //   18: aload_2
/*     */     //   19: athrow
/*     */     // Line number table:
/*     */     //   Java source line #162	-> byte code offset #0
/*     */     //   Java source line #163	-> byte code offset #7
/*     */     //   Java source line #164	-> byte code offset #15
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	20	0	this	MyTableModel
/*     */     //   5	12	1	Ljava/lang/Object;	Object
/*     */     //   15	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	14	15	finally
/*     */     //   15	18	15	finally
/*     */   }
/*     */   
/*     */   public int getColumnCount()
/*     */   {
/* 170 */     return COL_NAMES.length;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getColumnName(int aCol)
/*     */   {
/* 176 */     return COL_NAMES[aCol];
/*     */   }
/*     */   
/*     */ 
/*     */   public Class getColumnClass(int aCol)
/*     */   {
/* 182 */     return Object.class;
/*     */   }
/*     */   
/*     */   public Object getValueAt(int aRow, int aCol)
/*     */   {
/* 187 */     synchronized (this.mLock) {
/* 188 */       EventDetails event = this.mFilteredEvents[aRow];
/*     */       
/* 190 */       if (aCol == 0)
/* 191 */         return DATE_FORMATTER.format(new Date(event.getTimeStamp()));
/* 192 */       if (aCol == 1)
/* 193 */         return event.getPriority();
/* 194 */       if (aCol == 2) {
/* 195 */         return event.getThrowableStrRep() == null ? Boolean.FALSE : Boolean.TRUE;
/*     */       }
/* 197 */       if (aCol == 3)
/* 198 */         return event.getCategoryName();
/* 199 */       if (aCol == 4) {
/* 200 */         return event.getNDC();
/*     */       }
/* 202 */       return event.getMessage();
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
/*     */   public void setPriorityFilter(Priority aPriority)
/*     */   {
/* 217 */     synchronized (this.mLock) {
/* 218 */       this.mPriorityFilter = aPriority;
/* 219 */       updateFilteredEvents(false);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setThreadFilter(String aStr)
/*     */   {
/* 229 */     synchronized (this.mLock) {
/* 230 */       this.mThreadFilter = aStr.trim();
/* 231 */       updateFilteredEvents(false);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMessageFilter(String aStr)
/*     */   {
/* 241 */     synchronized (this.mLock) {
/* 242 */       this.mMessageFilter = aStr.trim();
/* 243 */       updateFilteredEvents(false);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setNDCFilter(String aStr)
/*     */   {
/* 253 */     synchronized (this.mLock) {
/* 254 */       this.mNDCFilter = aStr.trim();
/* 255 */       updateFilteredEvents(false);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCategoryFilter(String aStr)
/*     */   {
/* 265 */     synchronized (this.mLock) {
/* 266 */       this.mCategoryFilter = aStr.trim();
/* 267 */       updateFilteredEvents(false);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addEvent(EventDetails aEvent)
/*     */   {
/* 277 */     synchronized (this.mLock) {
/* 278 */       this.mPendingEvents.add(aEvent);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/* 286 */     synchronized (this.mLock) {
/* 287 */       this.mAllEvents.clear();
/* 288 */       this.mFilteredEvents = new EventDetails[0];
/* 289 */       this.mPendingEvents.clear();
/* 290 */       fireTableDataChanged();
/*     */     }
/*     */   }
/*     */   
/*     */   public void toggle()
/*     */   {
/* 296 */     synchronized (this.mLock) {
/* 297 */       this.mPaused = (!this.mPaused);
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public boolean isPaused()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 6	org/apache/log4j/chainsaw/MyTableModel:mLock	Ljava/lang/Object;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 5	org/apache/log4j/chainsaw/MyTableModel:mPaused	Z
/*     */     //   11: aload_1
/*     */     //   12: monitorexit
/*     */     //   13: ireturn
/*     */     //   14: astore_2
/*     */     //   15: aload_1
/*     */     //   16: monitorexit
/*     */     //   17: aload_2
/*     */     //   18: athrow
/*     */     // Line number table:
/*     */     //   Java source line #303	-> byte code offset #0
/*     */     //   Java source line #304	-> byte code offset #7
/*     */     //   Java source line #305	-> byte code offset #14
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	19	0	this	MyTableModel
/*     */     //   5	11	1	Ljava/lang/Object;	Object
/*     */     //   14	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	13	14	finally
/*     */     //   14	17	14	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public EventDetails getEventDetails(int aRow)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 6	org/apache/log4j/chainsaw/MyTableModel:mLock	Ljava/lang/Object;
/*     */     //   4: dup
/*     */     //   5: astore_2
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 19	org/apache/log4j/chainsaw/MyTableModel:mFilteredEvents	[Lorg/apache/log4j/chainsaw/EventDetails;
/*     */     //   11: iload_1
/*     */     //   12: aaload
/*     */     //   13: aload_2
/*     */     //   14: monitorexit
/*     */     //   15: areturn
/*     */     //   16: astore_3
/*     */     //   17: aload_2
/*     */     //   18: monitorexit
/*     */     //   19: aload_3
/*     */     //   20: athrow
/*     */     // Line number table:
/*     */     //   Java source line #315	-> byte code offset #0
/*     */     //   Java source line #316	-> byte code offset #7
/*     */     //   Java source line #317	-> byte code offset #16
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	21	0	this	MyTableModel
/*     */     //   0	21	1	aRow	int
/*     */     //   5	13	2	Ljava/lang/Object;	Object
/*     */     //   16	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	15	16	finally
/*     */     //   16	19	16	finally
/*     */   }
/*     */   
/*     */   private void updateFilteredEvents(boolean aInsertedToFront)
/*     */   {
/* 331 */     long start = System.currentTimeMillis();
/* 332 */     List filtered = new ArrayList();
/* 333 */     int size = this.mAllEvents.size();
/* 334 */     Iterator it = this.mAllEvents.iterator();
/*     */     
/* 336 */     while (it.hasNext()) {
/* 337 */       EventDetails event = (EventDetails)it.next();
/* 338 */       if (matchFilter(event)) {
/* 339 */         filtered.add(event);
/*     */       }
/*     */     }
/*     */     
/* 343 */     EventDetails lastFirst = this.mFilteredEvents.length == 0 ? null : this.mFilteredEvents[0];
/*     */     
/*     */ 
/* 346 */     this.mFilteredEvents = ((EventDetails[])filtered.toArray(EMPTY_LIST));
/*     */     
/* 348 */     if ((aInsertedToFront) && (lastFirst != null)) {
/* 349 */       int index = filtered.indexOf(lastFirst);
/* 350 */       if (index < 1) {
/* 351 */         LOG.warn("In strange state");
/* 352 */         fireTableDataChanged();
/*     */       } else {
/* 354 */         fireTableRowsInserted(0, index - 1);
/*     */       }
/*     */     } else {
/* 357 */       fireTableDataChanged();
/*     */     }
/*     */     
/* 360 */     long end = System.currentTimeMillis();
/* 361 */     LOG.debug("Total time [ms]: " + (end - start) + " in update, size: " + size);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean matchFilter(EventDetails aEvent)
/*     */   {
/* 372 */     if ((aEvent.getPriority().isGreaterOrEqual(this.mPriorityFilter)) && (aEvent.getThreadName().indexOf(this.mThreadFilter) >= 0) && (aEvent.getCategoryName().indexOf(this.mCategoryFilter) >= 0) && ((this.mNDCFilter.length() == 0) || ((aEvent.getNDC() != null) && (aEvent.getNDC().indexOf(this.mNDCFilter) >= 0))))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 379 */       String rm = aEvent.getMessage();
/* 380 */       if (rm == null)
/*     */       {
/* 382 */         return this.mMessageFilter.length() == 0;
/*     */       }
/* 384 */       return rm.indexOf(this.mMessageFilter) >= 0;
/*     */     }
/*     */     
/*     */ 
/* 388 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\log4j\chainsaw\MyTableModel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */