/*     */ package com.google.common.eventbus;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Queues;
/*     */ import java.util.Iterator;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class Dispatcher
/*     */ {
/*     */   static Dispatcher perThreadDispatchQueue()
/*     */   {
/*  47 */     return new PerThreadQueuedDispatcher(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static Dispatcher legacyAsync()
/*     */   {
/*  57 */     return new LegacyAsyncDispatcher(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static Dispatcher immediate()
/*     */   {
/*  66 */     return ImmediateDispatcher.INSTANCE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   abstract void dispatch(Object paramObject, Iterator<Subscriber> paramIterator);
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class PerThreadQueuedDispatcher
/*     */     extends Dispatcher
/*     */   {
/*  78 */     private final ThreadLocal<Queue<Event>> queue = new ThreadLocal()
/*     */     {
/*     */       protected Queue<Dispatcher.PerThreadQueuedDispatcher.Event> initialValue()
/*     */       {
/*  82 */         return Queues.newArrayDeque();
/*     */       }
/*     */     };
/*     */     
/*     */ 
/*  87 */     private final ThreadLocal<Boolean> dispatching = new ThreadLocal()
/*     */     {
/*     */       protected Boolean initialValue()
/*     */       {
/*  91 */         return Boolean.valueOf(false);
/*     */       }
/*     */     };
/*     */     
/*     */     void dispatch(Object event, Iterator<Subscriber> subscribers)
/*     */     {
/*  97 */       Preconditions.checkNotNull(event);
/*  98 */       Preconditions.checkNotNull(subscribers);
/*  99 */       Queue<Event> queueForThread = (Queue)this.queue.get();
/* 100 */       queueForThread.offer(new Event(event, subscribers, null));
/*     */       
/* 102 */       if (!((Boolean)this.dispatching.get()).booleanValue()) {
/* 103 */         this.dispatching.set(Boolean.valueOf(true));
/*     */         try {
/*     */           Event nextEvent;
/* 106 */           if ((nextEvent = (Event)queueForThread.poll()) != null) {
/* 107 */             while (nextEvent.subscribers.hasNext()) {
/* 108 */               ((Subscriber)nextEvent.subscribers.next()).dispatchEvent(nextEvent.event);
/*     */             }
/*     */           }
/*     */         } finally {
/* 112 */           this.dispatching.remove();
/* 113 */           this.queue.remove();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private static final class Event {
/*     */       private final Object event;
/*     */       private final Iterator<Subscriber> subscribers;
/*     */       
/*     */       private Event(Object event, Iterator<Subscriber> subscribers) {
/* 123 */         this.event = event;
/* 124 */         this.subscribers = subscribers;
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
/*     */ 
/*     */ 
/*     */   private static final class LegacyAsyncDispatcher
/*     */     extends Dispatcher
/*     */   {
/* 152 */     private final ConcurrentLinkedQueue<EventWithSubscriber> queue = Queues.newConcurrentLinkedQueue();
/*     */     
/*     */     void dispatch(Object event, Iterator<Subscriber> subscribers)
/*     */     {
/* 156 */       Preconditions.checkNotNull(event);
/* 157 */       while (subscribers.hasNext()) {
/* 158 */         this.queue.add(new EventWithSubscriber(event, (Subscriber)subscribers.next(), null));
/*     */       }
/*     */       
/*     */       EventWithSubscriber e;
/* 162 */       while ((e = (EventWithSubscriber)this.queue.poll()) != null) {
/* 163 */         e.subscriber.dispatchEvent(e.event);
/*     */       }
/*     */     }
/*     */     
/*     */     private static final class EventWithSubscriber {
/*     */       private final Object event;
/*     */       private final Subscriber subscriber;
/*     */       
/*     */       private EventWithSubscriber(Object event, Subscriber subscriber) {
/* 172 */         this.event = event;
/* 173 */         this.subscriber = subscriber;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ImmediateDispatcher extends Dispatcher
/*     */   {
/* 180 */     private static final ImmediateDispatcher INSTANCE = new ImmediateDispatcher();
/*     */     
/*     */     void dispatch(Object event, Iterator<Subscriber> subscribers)
/*     */     {
/* 184 */       Preconditions.checkNotNull(event);
/* 185 */       while (subscribers.hasNext()) {
/* 186 */         ((Subscriber)subscribers.next()).dispatchEvent(event);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\eventbus\Dispatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */