/*     */ package org.apache.commons.net.nntp;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
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
/*     */ public class Threader
/*     */ {
/*     */   public Threadable thread(List<? extends Threadable> messages)
/*     */   {
/*  45 */     return thread(messages);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Threadable thread(Iterable<? extends Threadable> messages)
/*     */   {
/*  56 */     if (messages == null) {
/*  57 */       return null;
/*     */     }
/*     */     
/*  60 */     HashMap<String, ThreadContainer> idTable = new HashMap();
/*     */     
/*     */ 
/*  63 */     for (Threadable t : messages) {
/*  64 */       if (!t.isDummy()) {
/*  65 */         buildContainer(t, idTable);
/*     */       }
/*     */     }
/*     */     
/*  69 */     if (idTable.isEmpty()) {
/*  70 */       return null;
/*     */     }
/*     */     
/*  73 */     ThreadContainer root = findRootSet(idTable);
/*  74 */     idTable.clear();
/*  75 */     idTable = null;
/*     */     
/*  77 */     pruneEmptyContainers(root);
/*     */     
/*  79 */     root.reverseChildren();
/*  80 */     gatherSubjects(root);
/*     */     
/*  82 */     if (root.next != null) {
/*  83 */       throw new RuntimeException("root node has a next:" + root);
/*     */     }
/*     */     
/*  86 */     for (ThreadContainer r = root.child; r != null; r = r.next) {
/*  87 */       if (r.threadable == null) {
/*  88 */         r.threadable = r.child.threadable.makeDummy();
/*     */       }
/*     */     }
/*     */     
/*  92 */     Threadable result = root.child == null ? null : root.child.threadable;
/*  93 */     root.flush();
/*     */     
/*  95 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void buildContainer(Threadable threadable, HashMap<String, ThreadContainer> idTable)
/*     */   {
/* 104 */     String id = threadable.messageThreadId();
/* 105 */     ThreadContainer container = (ThreadContainer)idTable.get(id);
/* 106 */     int bogusIdCount = 0;
/*     */     
/*     */ 
/*     */ 
/* 110 */     if (container != null) {
/* 111 */       if (container.threadable != null) {
/* 112 */         bogusIdCount++;
/* 113 */         id = "<Bogus-id:" + bogusIdCount + ">";
/* 114 */         container = null;
/*     */       }
/*     */       else
/*     */       {
/* 118 */         container.threadable = threadable;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 123 */     if (container == null) {
/* 124 */       container = new ThreadContainer();
/* 125 */       container.threadable = threadable;
/* 126 */       idTable.put(id, container);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 131 */     ThreadContainer parentRef = null;
/*     */     
/* 133 */     String[] references = threadable.messageThreadReferences();
/* 134 */     for (String refString : references)
/*     */     {
/* 136 */       ThreadContainer ref = (ThreadContainer)idTable.get(refString);
/*     */       
/*     */ 
/* 139 */       if (ref == null) {
/* 140 */         ref = new ThreadContainer();
/* 141 */         idTable.put(refString, ref);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 147 */       if ((parentRef != null) && (ref.parent == null) && (parentRef != ref) && (!ref.findChild(parentRef)))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 152 */         ref.parent = parentRef;
/* 153 */         ref.next = parentRef.child;
/* 154 */         parentRef.child = ref;
/*     */       }
/* 156 */       parentRef = ref;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 162 */     if ((parentRef != null) && ((parentRef == container) || (container.findChild(parentRef))))
/*     */     {
/*     */ 
/* 165 */       parentRef = null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 171 */     if (container.parent != null)
/*     */     {
/*     */ 
/* 174 */       ThreadContainer prev = null; for (ThreadContainer rest = container.parent.child; 
/* 175 */           rest != null; 
/* 176 */           rest = rest.next) {
/* 177 */         if (rest == container) {
/*     */           break;
/*     */         }
/* 176 */         prev = rest;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 182 */       if (rest == null) {
/* 183 */         throw new RuntimeException("Didnt find " + container + " in parent" + container.parent);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 191 */       if (prev == null) {
/* 192 */         container.parent.child = container.next;
/*     */       } else {
/* 194 */         prev.next = container.next;
/*     */       }
/*     */       
/* 197 */       container.next = null;
/* 198 */       container.parent = null;
/*     */     }
/*     */     
/*     */ 
/* 202 */     if (parentRef != null) {
/* 203 */       container.parent = parentRef;
/* 204 */       container.next = parentRef.child;
/* 205 */       parentRef.child = container;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ThreadContainer findRootSet(HashMap<String, ThreadContainer> idTable)
/*     */   {
/* 215 */     ThreadContainer root = new ThreadContainer();
/* 216 */     Iterator<Map.Entry<String, ThreadContainer>> iter = idTable.entrySet().iterator();
/*     */     
/* 218 */     while (iter.hasNext()) {
/* 219 */       Map.Entry<String, ThreadContainer> entry = (Map.Entry)iter.next();
/* 220 */       ThreadContainer c = (ThreadContainer)entry.getValue();
/* 221 */       if (c.parent == null) {
/* 222 */         if (c.next != null) {
/* 223 */           throw new RuntimeException("c.next is " + c.next.toString());
/*     */         }
/*     */         
/* 226 */         c.next = root.child;
/* 227 */         root.child = c;
/*     */       }
/*     */     }
/* 230 */     return root;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void pruneEmptyContainers(ThreadContainer parent)
/*     */   {
/* 239 */     ThreadContainer prev = null;ThreadContainer container = parent.child;ThreadContainer next = container.next;
/* 240 */     while (container != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 246 */       if ((container.threadable == null) && (container.child == null)) {
/* 247 */         if (prev == null) {
/* 248 */           parent.child = container.next;
/*     */         } else {
/* 250 */           prev.next = container.next;
/*     */         }
/*     */         
/*     */ 
/* 254 */         container = prev;
/*     */ 
/*     */ 
/*     */       }
/* 258 */       else if ((container.threadable == null) && (container.child != null) && ((container.parent != null) || (container.child.next == null)))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 265 */         ThreadContainer kids = container.child;
/*     */         
/*     */ 
/* 268 */         if (prev == null) {
/* 269 */           parent.child = kids;
/*     */         } else {
/* 271 */           prev.next = kids;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 277 */         for (ThreadContainer tail = kids; tail.next != null; tail = tail.next) {
/* 278 */           tail.parent = container.parent;
/*     */         }
/*     */         
/* 281 */         tail.parent = container.parent;
/* 282 */         tail.next = container.next;
/*     */         
/*     */ 
/*     */ 
/* 286 */         next = kids;
/*     */         
/*     */ 
/* 289 */         container = prev;
/* 290 */       } else if (container.child != null)
/*     */       {
/*     */ 
/* 293 */         pruneEmptyContainers(container);
/*     */       }
/* 241 */       prev = container;
/* 242 */       container = next;
/* 243 */       next = container == null ? null : container.next;
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
/*     */   private void gatherSubjects(ThreadContainer root)
/*     */   {
/* 305 */     int count = 0;
/*     */     
/* 307 */     for (ThreadContainer c = root.child; c != null; c = c.next) {
/* 308 */       count++;
/*     */     }
/*     */     
/*     */ 
/* 312 */     HashMap<String, ThreadContainer> subjectTable = new HashMap((int)(count * 1.2D), 0.9F);
/* 313 */     count = 0;
/*     */     
/* 315 */     for (ThreadContainer c = root.child; c != null; c = c.next) {
/* 316 */       Threadable threadable = c.threadable;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 321 */       if (threadable == null) {
/* 322 */         threadable = c.child.threadable;
/*     */       }
/*     */       
/* 325 */       String subj = threadable.simplifiedSubject();
/*     */       
/* 327 */       if ((subj != null) && (subj.length() != 0))
/*     */       {
/*     */ 
/*     */ 
/* 331 */         ThreadContainer old = (ThreadContainer)subjectTable.get(subj);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 340 */         if ((old == null) || ((c.threadable == null) && (old.threadable != null)) || ((old.threadable != null) && (old.threadable.subjectIsReply()) && (c.threadable != null) && (!c.threadable.subjectIsReply())))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 346 */           subjectTable.put(subj, c);
/* 347 */           count++;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 352 */     if (count == 0) {
/* 353 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 359 */     ThreadContainer prev = null;ThreadContainer c = root.child; for (ThreadContainer rest = c.next; 
/* 360 */         c != null; 
/* 361 */         rest = rest == null ? null : rest.next) {
/* 362 */       Threadable threadable = c.threadable;
/*     */       
/*     */ 
/* 365 */       if (threadable == null) {
/* 366 */         threadable = c.child.threadable;
/*     */       }
/*     */       
/* 369 */       String subj = threadable.simplifiedSubject();
/*     */       
/*     */ 
/* 372 */       if ((subj != null) && (subj.length() != 0))
/*     */       {
/*     */ 
/*     */ 
/* 376 */         ThreadContainer old = (ThreadContainer)subjectTable.get(subj);
/*     */         
/* 378 */         if (old != c)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 384 */           if (prev == null) {
/* 385 */             root.child = c.next;
/*     */           } else {
/* 387 */             prev.next = c.next;
/*     */           }
/* 389 */           c.next = null;
/*     */           
/* 391 */           if ((old.threadable == null) && (c.threadable == null))
/*     */           {
/*     */ 
/* 394 */             ThreadContainer tail = old.child;
/* 395 */             while ((tail != null) && (tail.next != null)) {
/* 396 */               tail = tail.next;
/*     */             }
/*     */             
/*     */ 
/* 400 */             if (tail != null) {
/* 401 */               tail.next = c.child;
/*     */             }
/*     */             
/* 404 */             for (tail = c.child; tail != null; tail = tail.next) {
/* 405 */               tail.parent = old;
/*     */             }
/*     */             
/* 408 */             c.child = null;
/* 409 */           } else if ((old.threadable == null) || ((c.threadable != null) && (c.threadable.subjectIsReply()) && (!old.threadable.subjectIsReply())))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 415 */             c.parent = old;
/* 416 */             c.next = old.child;
/* 417 */             old.child = c;
/*     */           }
/*     */           else
/*     */           {
/* 421 */             ThreadContainer newc = new ThreadContainer();
/* 422 */             newc.threadable = old.threadable;
/* 423 */             newc.child = old.child;
/*     */             
/* 425 */             for (ThreadContainer tail = newc.child; 
/* 426 */                 tail != null; 
/* 427 */                 tail = tail.next)
/*     */             {
/* 429 */               tail.parent = newc;
/*     */             }
/*     */             
/* 432 */             old.threadable = null;
/* 433 */             old.child = null;
/*     */             
/* 435 */             c.parent = old;
/* 436 */             newc.parent = old;
/*     */             
/*     */ 
/* 439 */             old.child = c;
/* 440 */             c.next = newc;
/*     */           }
/*     */           
/* 443 */           c = prev;
/*     */         }
/*     */       }
/* 361 */       prev = c;c = rest;
/*     */     }
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
/* 446 */     subjectTable.clear();
/* 447 */     subjectTable = null;
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
/*     */   @Deprecated
/*     */   public Threadable thread(Threadable[] messages)
/*     */   {
/* 463 */     if (messages == null) {
/* 464 */       return null;
/*     */     }
/* 466 */     return thread(Arrays.asList(messages));
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\nntp\Threader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */