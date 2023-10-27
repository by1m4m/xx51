/*      */ package io.netty.handler.codec.http2;
/*      */ 
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.channel.ChannelPromise;
/*      */ import io.netty.util.collection.IntObjectHashMap;
/*      */ import io.netty.util.collection.IntObjectMap;
/*      */ import io.netty.util.collection.IntObjectMap.PrimitiveEntry;
/*      */ import io.netty.util.concurrent.Future;
/*      */ import io.netty.util.concurrent.Promise;
/*      */ import io.netty.util.concurrent.UnaryPromiseNotifier;
/*      */ import io.netty.util.internal.EmptyArrays;
/*      */ import io.netty.util.internal.ObjectUtil;
/*      */ import io.netty.util.internal.logging.InternalLogger;
/*      */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*      */ import java.util.ArrayDeque;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
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
/*      */ public class DefaultHttp2Connection
/*      */   implements Http2Connection
/*      */ {
/*   65 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultHttp2Connection.class);
/*      */   
/*   67 */   final IntObjectMap<Http2Stream> streamMap = new IntObjectHashMap();
/*   68 */   final PropertyKeyRegistry propertyKeyRegistry = new PropertyKeyRegistry(null);
/*   69 */   final ConnectionStream connectionStream = new ConnectionStream();
/*      */   
/*      */ 
/*      */ 
/*      */   final DefaultEndpoint<Http2LocalFlowController> localEndpoint;
/*      */   
/*      */ 
/*      */ 
/*      */   final DefaultEndpoint<Http2RemoteFlowController> remoteEndpoint;
/*      */   
/*      */ 
/*      */ 
/*   81 */   final List<Http2Connection.Listener> listeners = new ArrayList(4);
/*      */   
/*      */   final ActiveStreams activeStreams;
/*      */   
/*      */   Promise<Void> closePromise;
/*      */   
/*      */ 
/*      */   public DefaultHttp2Connection(boolean server)
/*      */   {
/*   90 */     this(server, 100);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DefaultHttp2Connection(boolean server, int maxReservedStreams)
/*      */   {
/*   99 */     this.activeStreams = new ActiveStreams(this.listeners);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  105 */     this.localEndpoint = new DefaultEndpoint(server, server ? Integer.MAX_VALUE : maxReservedStreams);
/*  106 */     this.remoteEndpoint = new DefaultEndpoint(!server, maxReservedStreams);
/*      */     
/*      */ 
/*  109 */     this.streamMap.put(this.connectionStream.id(), this.connectionStream);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   final boolean isClosed()
/*      */   {
/*  116 */     return this.closePromise != null;
/*      */   }
/*      */   
/*      */   public Future<Void> close(Promise<Void> promise)
/*      */   {
/*  121 */     ObjectUtil.checkNotNull(promise, "promise");
/*      */     
/*      */ 
/*  124 */     if (this.closePromise != null) {
/*  125 */       if (this.closePromise != promise)
/*      */       {
/*  127 */         if (((promise instanceof ChannelPromise)) && (((ChannelPromise)this.closePromise).isVoid())) {
/*  128 */           this.closePromise = promise;
/*      */         } else
/*  130 */           this.closePromise.addListener(new UnaryPromiseNotifier(promise));
/*      */       }
/*      */     } else {
/*  133 */       this.closePromise = promise;
/*      */     }
/*  135 */     if (isStreamMapEmpty()) {
/*  136 */       promise.trySuccess(null);
/*  137 */       return promise;
/*      */     }
/*      */     
/*  140 */     Iterator<IntObjectMap.PrimitiveEntry<Http2Stream>> itr = this.streamMap.entries().iterator();
/*      */     
/*      */ 
/*  143 */     if (this.activeStreams.allowModifications()) {
/*  144 */       this.activeStreams.incrementPendingIterations();
/*      */       try {
/*  146 */         while (itr.hasNext()) {
/*  147 */           DefaultStream stream = (DefaultStream)((IntObjectMap.PrimitiveEntry)itr.next()).value();
/*  148 */           if (stream.id() != 0)
/*      */           {
/*      */ 
/*      */ 
/*  152 */             stream.close(itr);
/*      */           }
/*      */         }
/*      */       } finally {
/*  156 */         this.activeStreams.decrementPendingIterations();
/*      */       }
/*      */     } else {
/*  159 */       while (itr.hasNext()) {
/*  160 */         Http2Stream stream = (Http2Stream)((IntObjectMap.PrimitiveEntry)itr.next()).value();
/*  161 */         if (stream.id() != 0)
/*      */         {
/*      */ 
/*  164 */           stream.close();
/*      */         }
/*      */       }
/*      */     }
/*  168 */     return this.closePromise;
/*      */   }
/*      */   
/*      */   public void addListener(Http2Connection.Listener listener)
/*      */   {
/*  173 */     this.listeners.add(listener);
/*      */   }
/*      */   
/*      */   public void removeListener(Http2Connection.Listener listener)
/*      */   {
/*  178 */     this.listeners.remove(listener);
/*      */   }
/*      */   
/*      */   public boolean isServer()
/*      */   {
/*  183 */     return this.localEndpoint.isServer();
/*      */   }
/*      */   
/*      */   public Http2Stream connectionStream()
/*      */   {
/*  188 */     return this.connectionStream;
/*      */   }
/*      */   
/*      */   public Http2Stream stream(int streamId)
/*      */   {
/*  193 */     return (Http2Stream)this.streamMap.get(streamId);
/*      */   }
/*      */   
/*      */   public boolean streamMayHaveExisted(int streamId)
/*      */   {
/*  198 */     return (this.remoteEndpoint.mayHaveCreatedStream(streamId)) || (this.localEndpoint.mayHaveCreatedStream(streamId));
/*      */   }
/*      */   
/*      */   public int numActiveStreams()
/*      */   {
/*  203 */     return this.activeStreams.size();
/*      */   }
/*      */   
/*      */   public Http2Stream forEachActiveStream(Http2StreamVisitor visitor) throws Http2Exception
/*      */   {
/*  208 */     return this.activeStreams.forEachActiveStream(visitor);
/*      */   }
/*      */   
/*      */   public Http2Connection.Endpoint<Http2LocalFlowController> local()
/*      */   {
/*  213 */     return this.localEndpoint;
/*      */   }
/*      */   
/*      */   public Http2Connection.Endpoint<Http2RemoteFlowController> remote()
/*      */   {
/*  218 */     return this.remoteEndpoint;
/*      */   }
/*      */   
/*      */   public boolean goAwayReceived()
/*      */   {
/*  223 */     return this.localEndpoint.lastStreamKnownByPeer >= 0;
/*      */   }
/*      */   
/*      */   public void goAwayReceived(int lastKnownStream, long errorCode, ByteBuf debugData) throws Http2Exception
/*      */   {
/*  228 */     if ((this.localEndpoint.lastStreamKnownByPeer() >= 0) && (this.localEndpoint.lastStreamKnownByPeer() < lastKnownStream)) {
/*  229 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "lastStreamId MUST NOT increase. Current value: %d new value: %d", new Object[] {
/*  230 */         Integer.valueOf(this.localEndpoint.lastStreamKnownByPeer()), Integer.valueOf(lastKnownStream) });
/*      */     }
/*      */     
/*  233 */     this.localEndpoint.lastStreamKnownByPeer(lastKnownStream);
/*  234 */     for (int i = 0; i < this.listeners.size(); i++) {
/*      */       try {
/*  236 */         ((Http2Connection.Listener)this.listeners.get(i)).onGoAwayReceived(lastKnownStream, errorCode, debugData);
/*      */       } catch (Throwable cause) {
/*  238 */         logger.error("Caught Throwable from listener onGoAwayReceived.", cause);
/*      */       }
/*      */     }
/*      */     
/*  242 */     closeStreamsGreaterThanLastKnownStreamId(lastKnownStream, this.localEndpoint);
/*      */   }
/*      */   
/*      */   public boolean goAwaySent()
/*      */   {
/*  247 */     return this.remoteEndpoint.lastStreamKnownByPeer >= 0;
/*      */   }
/*      */   
/*      */   public boolean goAwaySent(int lastKnownStream, long errorCode, ByteBuf debugData) throws Http2Exception
/*      */   {
/*  252 */     if (this.remoteEndpoint.lastStreamKnownByPeer() >= 0)
/*      */     {
/*      */ 
/*  255 */       if (lastKnownStream == this.remoteEndpoint.lastStreamKnownByPeer()) {
/*  256 */         return false;
/*      */       }
/*  258 */       if (lastKnownStream > this.remoteEndpoint.lastStreamKnownByPeer()) {
/*  259 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Last stream identifier must not increase between sending multiple GOAWAY frames (was '%d', is '%d').", new Object[] {
/*      */         
/*  261 */           Integer.valueOf(this.remoteEndpoint.lastStreamKnownByPeer()), Integer.valueOf(lastKnownStream) });
/*      */       }
/*      */     }
/*      */     
/*  265 */     this.remoteEndpoint.lastStreamKnownByPeer(lastKnownStream);
/*  266 */     for (int i = 0; i < this.listeners.size(); i++) {
/*      */       try {
/*  268 */         ((Http2Connection.Listener)this.listeners.get(i)).onGoAwaySent(lastKnownStream, errorCode, debugData);
/*      */       } catch (Throwable cause) {
/*  270 */         logger.error("Caught Throwable from listener onGoAwaySent.", cause);
/*      */       }
/*      */     }
/*      */     
/*  274 */     closeStreamsGreaterThanLastKnownStreamId(lastKnownStream, this.remoteEndpoint);
/*  275 */     return true;
/*      */   }
/*      */   
/*      */   private void closeStreamsGreaterThanLastKnownStreamId(final int lastKnownStream, final DefaultEndpoint<?> endpoint) throws Http2Exception
/*      */   {
/*  280 */     forEachActiveStream(new Http2StreamVisitor()
/*      */     {
/*      */       public boolean visit(Http2Stream stream) {
/*  283 */         if ((stream.id() > lastKnownStream) && (endpoint.isValidStreamId(stream.id()))) {
/*  284 */           stream.close();
/*      */         }
/*  286 */         return true;
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean isStreamMapEmpty()
/*      */   {
/*  295 */     return this.streamMap.size() == 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   void removeStream(DefaultStream stream, Iterator<?> itr)
/*      */   {
/*      */     boolean removed;
/*      */     
/*      */     boolean removed;
/*      */     
/*  306 */     if (itr == null) {
/*  307 */       removed = this.streamMap.remove(stream.id()) != null;
/*      */     } else {
/*  309 */       itr.remove();
/*  310 */       removed = true;
/*      */     }
/*      */     
/*  313 */     if (removed) {
/*  314 */       for (int i = 0; i < this.listeners.size(); i++) {
/*      */         try {
/*  316 */           ((Http2Connection.Listener)this.listeners.get(i)).onStreamRemoved(stream);
/*      */         } catch (Throwable cause) {
/*  318 */           logger.error("Caught Throwable from listener onStreamRemoved.", cause);
/*      */         }
/*      */       }
/*      */       
/*  322 */       if ((this.closePromise != null) && (isStreamMapEmpty())) {
/*  323 */         this.closePromise.trySuccess(null);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   static Http2Stream.State activeState(int streamId, Http2Stream.State initialState, boolean isLocal, boolean halfClosed) throws Http2Exception
/*      */   {
/*  330 */     switch (initialState) {
/*      */     case IDLE: 
/*  332 */       return halfClosed ? Http2Stream.State.HALF_CLOSED_REMOTE : isLocal ? Http2Stream.State.HALF_CLOSED_LOCAL : Http2Stream.State.OPEN;
/*      */     case RESERVED_LOCAL: 
/*  334 */       return Http2Stream.State.HALF_CLOSED_REMOTE;
/*      */     case RESERVED_REMOTE: 
/*  336 */       return Http2Stream.State.HALF_CLOSED_LOCAL;
/*      */     }
/*  338 */     throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, "Attempting to open a stream in an invalid state: " + initialState, new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */   void notifyHalfClosed(Http2Stream stream)
/*      */   {
/*  344 */     for (int i = 0; i < this.listeners.size(); i++) {
/*      */       try {
/*  346 */         ((Http2Connection.Listener)this.listeners.get(i)).onStreamHalfClosed(stream);
/*      */       } catch (Throwable cause) {
/*  348 */         logger.error("Caught Throwable from listener onStreamHalfClosed.", cause);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   void notifyClosed(Http2Stream stream) {
/*  354 */     for (int i = 0; i < this.listeners.size(); i++) {
/*      */       try {
/*  356 */         ((Http2Connection.Listener)this.listeners.get(i)).onStreamClosed(stream);
/*      */       } catch (Throwable cause) {
/*  358 */         logger.error("Caught Throwable from listener onStreamClosed.", cause);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public Http2Connection.PropertyKey newKey()
/*      */   {
/*  365 */     return this.propertyKeyRegistry.newKey();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final DefaultPropertyKey verifyKey(Http2Connection.PropertyKey key)
/*      */   {
/*  376 */     return ((DefaultPropertyKey)ObjectUtil.checkNotNull((DefaultPropertyKey)key, "key")).verifyConnection(this);
/*      */   }
/*      */   
/*      */ 
/*      */   private class DefaultStream
/*      */     implements Http2Stream
/*      */   {
/*      */     private static final byte META_STATE_SENT_RST = 1;
/*      */     private static final byte META_STATE_SENT_HEADERS = 2;
/*      */     private static final byte META_STATE_SENT_TRAILERS = 4;
/*      */     private static final byte META_STATE_SENT_PUSHPROMISE = 8;
/*      */     private static final byte META_STATE_RECV_HEADERS = 16;
/*      */     private static final byte META_STATE_RECV_TRAILERS = 32;
/*      */     private final int id;
/*  390 */     private final PropertyMap properties = new PropertyMap(null);
/*      */     private Http2Stream.State state;
/*      */     private byte metaState;
/*      */     
/*      */     DefaultStream(int id, Http2Stream.State state) {
/*  395 */       this.id = id;
/*  396 */       this.state = state;
/*      */     }
/*      */     
/*      */     public final int id()
/*      */     {
/*  401 */       return this.id;
/*      */     }
/*      */     
/*      */     public final Http2Stream.State state()
/*      */     {
/*  406 */       return this.state;
/*      */     }
/*      */     
/*      */     public boolean isResetSent()
/*      */     {
/*  411 */       return (this.metaState & 0x1) != 0;
/*      */     }
/*      */     
/*      */     public Http2Stream resetSent()
/*      */     {
/*  416 */       this.metaState = ((byte)(this.metaState | 0x1));
/*  417 */       return this;
/*      */     }
/*      */     
/*      */     public Http2Stream headersSent(boolean isInformational)
/*      */     {
/*  422 */       if (!isInformational) {
/*  423 */         this.metaState = ((byte)(this.metaState | (isHeadersSent() ? 4 : 2)));
/*      */       }
/*  425 */       return this;
/*      */     }
/*      */     
/*      */     public boolean isHeadersSent()
/*      */     {
/*  430 */       return (this.metaState & 0x2) != 0;
/*      */     }
/*      */     
/*      */     public boolean isTrailersSent()
/*      */     {
/*  435 */       return (this.metaState & 0x4) != 0;
/*      */     }
/*      */     
/*      */     public Http2Stream headersReceived(boolean isInformational)
/*      */     {
/*  440 */       if (!isInformational) {
/*  441 */         this.metaState = ((byte)(this.metaState | (isHeadersReceived() ? 32 : 16)));
/*      */       }
/*  443 */       return this;
/*      */     }
/*      */     
/*      */     public boolean isHeadersReceived()
/*      */     {
/*  448 */       return (this.metaState & 0x10) != 0;
/*      */     }
/*      */     
/*      */     public boolean isTrailersReceived()
/*      */     {
/*  453 */       return (this.metaState & 0x20) != 0;
/*      */     }
/*      */     
/*      */     public Http2Stream pushPromiseSent()
/*      */     {
/*  458 */       this.metaState = ((byte)(this.metaState | 0x8));
/*  459 */       return this;
/*      */     }
/*      */     
/*      */     public boolean isPushPromiseSent()
/*      */     {
/*  464 */       return (this.metaState & 0x8) != 0;
/*      */     }
/*      */     
/*      */     public final <V> V setProperty(Http2Connection.PropertyKey key, V value)
/*      */     {
/*  469 */       return (V)this.properties.add(DefaultHttp2Connection.this.verifyKey(key), value);
/*      */     }
/*      */     
/*      */     public final <V> V getProperty(Http2Connection.PropertyKey key)
/*      */     {
/*  474 */       return (V)this.properties.get(DefaultHttp2Connection.this.verifyKey(key));
/*      */     }
/*      */     
/*      */     public final <V> V removeProperty(Http2Connection.PropertyKey key)
/*      */     {
/*  479 */       return (V)this.properties.remove(DefaultHttp2Connection.this.verifyKey(key));
/*      */     }
/*      */     
/*      */     public Http2Stream open(boolean halfClosed) throws Http2Exception
/*      */     {
/*  484 */       this.state = DefaultHttp2Connection.activeState(this.id, this.state, isLocal(), halfClosed);
/*  485 */       if (!createdBy().canOpenStream()) {
/*  486 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Maximum active streams violated for this endpoint.", new Object[0]);
/*      */       }
/*      */       
/*  489 */       activate();
/*  490 */       return this;
/*      */     }
/*      */     
/*      */ 
/*      */     void activate()
/*      */     {
/*  496 */       if (this.state == Http2Stream.State.HALF_CLOSED_LOCAL) {
/*  497 */         headersSent(false);
/*  498 */       } else if (this.state == Http2Stream.State.HALF_CLOSED_REMOTE) {
/*  499 */         headersReceived(false);
/*      */       }
/*  501 */       DefaultHttp2Connection.this.activeStreams.activate(this);
/*      */     }
/*      */     
/*      */     Http2Stream close(Iterator<?> itr) {
/*  505 */       if (this.state == Http2Stream.State.CLOSED) {
/*  506 */         return this;
/*      */       }
/*      */       
/*  509 */       this.state = Http2Stream.State.CLOSED;
/*      */       
/*  511 */       createdBy().numStreams -= 1;
/*  512 */       DefaultHttp2Connection.this.activeStreams.deactivate(this, itr);
/*  513 */       return this;
/*      */     }
/*      */     
/*      */     public Http2Stream close()
/*      */     {
/*  518 */       return close(null);
/*      */     }
/*      */     
/*      */     public Http2Stream closeLocalSide()
/*      */     {
/*  523 */       switch (DefaultHttp2Connection.2.$SwitchMap$io$netty$handler$codec$http2$Http2Stream$State[this.state.ordinal()]) {
/*      */       case 4: 
/*  525 */         this.state = Http2Stream.State.HALF_CLOSED_LOCAL;
/*  526 */         DefaultHttp2Connection.this.notifyHalfClosed(this);
/*  527 */         break;
/*      */       case 5: 
/*      */         break;
/*      */       default: 
/*  531 */         close();
/*      */       }
/*      */       
/*  534 */       return this;
/*      */     }
/*      */     
/*      */     public Http2Stream closeRemoteSide()
/*      */     {
/*  539 */       switch (DefaultHttp2Connection.2.$SwitchMap$io$netty$handler$codec$http2$Http2Stream$State[this.state.ordinal()]) {
/*      */       case 4: 
/*  541 */         this.state = Http2Stream.State.HALF_CLOSED_REMOTE;
/*  542 */         DefaultHttp2Connection.this.notifyHalfClosed(this);
/*  543 */         break;
/*      */       case 6: 
/*      */         break;
/*      */       default: 
/*  547 */         close();
/*      */       }
/*      */       
/*  550 */       return this;
/*      */     }
/*      */     
/*      */     DefaultHttp2Connection.DefaultEndpoint<? extends Http2FlowController> createdBy() {
/*  554 */       return DefaultHttp2Connection.this.localEndpoint.isValidStreamId(this.id) ? DefaultHttp2Connection.this.localEndpoint : DefaultHttp2Connection.this.remoteEndpoint;
/*      */     }
/*      */     
/*      */     final boolean isLocal() {
/*  558 */       return DefaultHttp2Connection.this.localEndpoint.isValidStreamId(this.id);
/*      */     }
/*      */     
/*      */     private class PropertyMap
/*      */     {
/*      */       private PropertyMap() {}
/*      */       
/*  565 */       Object[] values = EmptyArrays.EMPTY_OBJECTS;
/*      */       
/*      */       <V> V add(DefaultHttp2Connection.DefaultPropertyKey key, V value) {
/*  568 */         resizeIfNecessary(key.index);
/*      */         
/*  570 */         V prevValue = this.values[key.index];
/*  571 */         this.values[key.index] = value;
/*  572 */         return prevValue;
/*      */       }
/*      */       
/*      */       <V> V get(DefaultHttp2Connection.DefaultPropertyKey key)
/*      */       {
/*  577 */         if (key.index >= this.values.length) {
/*  578 */           return null;
/*      */         }
/*  580 */         return (V)this.values[key.index];
/*      */       }
/*      */       
/*      */       <V> V remove(DefaultHttp2Connection.DefaultPropertyKey key)
/*      */       {
/*  585 */         V prevValue = null;
/*  586 */         if (key.index < this.values.length) {
/*  587 */           prevValue = this.values[key.index];
/*  588 */           this.values[key.index] = null;
/*      */         }
/*  590 */         return prevValue;
/*      */       }
/*      */       
/*      */       void resizeIfNecessary(int index) {
/*  594 */         if (index >= this.values.length) {
/*  595 */           this.values = Arrays.copyOf(this.values, DefaultHttp2Connection.this.propertyKeyRegistry.size());
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private final class ConnectionStream
/*      */     extends DefaultHttp2Connection.DefaultStream
/*      */   {
/*      */     ConnectionStream()
/*      */     {
/*  606 */       super(0, Http2Stream.State.IDLE);
/*      */     }
/*      */     
/*      */     public boolean isResetSent()
/*      */     {
/*  611 */       return false;
/*      */     }
/*      */     
/*      */     DefaultHttp2Connection.DefaultEndpoint<? extends Http2FlowController> createdBy()
/*      */     {
/*  616 */       return null;
/*      */     }
/*      */     
/*      */     public Http2Stream resetSent()
/*      */     {
/*  621 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public Http2Stream open(boolean halfClosed)
/*      */     {
/*  626 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public Http2Stream close()
/*      */     {
/*  631 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public Http2Stream closeLocalSide()
/*      */     {
/*  636 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public Http2Stream closeRemoteSide()
/*      */     {
/*  641 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public Http2Stream headersSent(boolean isInformational)
/*      */     {
/*  646 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean isHeadersSent()
/*      */     {
/*  651 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public Http2Stream pushPromiseSent()
/*      */     {
/*  656 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean isPushPromiseSent()
/*      */     {
/*  661 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final class DefaultEndpoint<F extends Http2FlowController>
/*      */     implements Http2Connection.Endpoint<F>
/*      */   {
/*      */     private final boolean server;
/*      */     
/*      */ 
/*      */ 
/*      */     private int nextStreamIdToCreate;
/*      */     
/*      */ 
/*      */ 
/*      */     private int nextReservationStreamId;
/*      */     
/*      */ 
/*      */ 
/*  683 */     private int lastStreamKnownByPeer = -1;
/*  684 */     private boolean pushToAllowed = true;
/*      */     private F flowController;
/*      */     private int maxStreams;
/*      */     private int maxActiveStreams;
/*      */     private final int maxReservedStreams;
/*      */     int numActiveStreams;
/*      */     int numStreams;
/*      */     
/*      */     DefaultEndpoint(boolean server, int maxReservedStreams)
/*      */     {
/*  694 */       this.server = server;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  700 */       if (server) {
/*  701 */         this.nextStreamIdToCreate = 2;
/*  702 */         this.nextReservationStreamId = 0;
/*      */       } else {
/*  704 */         this.nextStreamIdToCreate = 1;
/*      */         
/*  706 */         this.nextReservationStreamId = 1;
/*      */       }
/*      */       
/*      */ 
/*  710 */       this.pushToAllowed = (!server);
/*  711 */       this.maxActiveStreams = Integer.MAX_VALUE;
/*  712 */       this.maxReservedStreams = ObjectUtil.checkPositiveOrZero(maxReservedStreams, "maxReservedStreams");
/*  713 */       updateMaxStreams();
/*      */     }
/*      */     
/*      */     public int incrementAndGetNextStreamId()
/*      */     {
/*  718 */       return this.nextReservationStreamId >= 0 ? this.nextReservationStreamId += 2 : this.nextReservationStreamId;
/*      */     }
/*      */     
/*      */     private void incrementExpectedStreamId(int streamId) {
/*  722 */       if ((streamId > this.nextReservationStreamId) && (this.nextReservationStreamId >= 0)) {
/*  723 */         this.nextReservationStreamId = streamId;
/*      */       }
/*  725 */       this.nextStreamIdToCreate = (streamId + 2);
/*  726 */       this.numStreams += 1;
/*      */     }
/*      */     
/*      */     public boolean isValidStreamId(int streamId)
/*      */     {
/*  731 */       if (streamId > 0) {} return this.server == ((streamId & 0x1) == 0);
/*      */     }
/*      */     
/*      */     public boolean mayHaveCreatedStream(int streamId)
/*      */     {
/*  736 */       return (isValidStreamId(streamId)) && (streamId <= lastStreamCreated());
/*      */     }
/*      */     
/*      */     public boolean canOpenStream()
/*      */     {
/*  741 */       return this.numActiveStreams < this.maxActiveStreams;
/*      */     }
/*      */     
/*      */     public DefaultHttp2Connection.DefaultStream createStream(int streamId, boolean halfClosed) throws Http2Exception
/*      */     {
/*  746 */       Http2Stream.State state = DefaultHttp2Connection.activeState(streamId, Http2Stream.State.IDLE, isLocal(), halfClosed);
/*      */       
/*  748 */       checkNewStreamAllowed(streamId, state);
/*      */       
/*      */ 
/*  751 */       DefaultHttp2Connection.DefaultStream stream = new DefaultHttp2Connection.DefaultStream(DefaultHttp2Connection.this, streamId, state);
/*      */       
/*  753 */       incrementExpectedStreamId(streamId);
/*      */       
/*  755 */       addStream(stream);
/*      */       
/*  757 */       stream.activate();
/*  758 */       return stream;
/*      */     }
/*      */     
/*      */     public boolean created(Http2Stream stream)
/*      */     {
/*  763 */       return ((stream instanceof DefaultHttp2Connection.DefaultStream)) && (((DefaultHttp2Connection.DefaultStream)stream).createdBy() == this);
/*      */     }
/*      */     
/*      */     public boolean isServer()
/*      */     {
/*  768 */       return this.server;
/*      */     }
/*      */     
/*      */     public DefaultHttp2Connection.DefaultStream reservePushStream(int streamId, Http2Stream parent) throws Http2Exception
/*      */     {
/*  773 */       if (parent == null) {
/*  774 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Parent stream missing", new Object[0]);
/*      */       }
/*  776 */       if (isLocal() ? !parent.state().localSideOpen() : !parent.state().remoteSideOpen()) {
/*  777 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream %d is not open for sending push promise", new Object[] { Integer.valueOf(parent.id()) });
/*      */       }
/*  779 */       if (!opposite().allowPushTo()) {
/*  780 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Server push not allowed to opposite endpoint", new Object[0]);
/*      */       }
/*  782 */       Http2Stream.State state = isLocal() ? Http2Stream.State.RESERVED_LOCAL : Http2Stream.State.RESERVED_REMOTE;
/*  783 */       checkNewStreamAllowed(streamId, state);
/*      */       
/*      */ 
/*  786 */       DefaultHttp2Connection.DefaultStream stream = new DefaultHttp2Connection.DefaultStream(DefaultHttp2Connection.this, streamId, state);
/*      */       
/*  788 */       incrementExpectedStreamId(streamId);
/*      */       
/*      */ 
/*  791 */       addStream(stream);
/*  792 */       return stream;
/*      */     }
/*      */     
/*      */     private void addStream(DefaultHttp2Connection.DefaultStream stream)
/*      */     {
/*  797 */       DefaultHttp2Connection.this.streamMap.put(stream.id(), stream);
/*      */       
/*      */ 
/*  800 */       for (int i = 0; i < DefaultHttp2Connection.this.listeners.size(); i++) {
/*      */         try {
/*  802 */           ((Http2Connection.Listener)DefaultHttp2Connection.this.listeners.get(i)).onStreamAdded(stream);
/*      */         } catch (Throwable cause) {
/*  804 */           DefaultHttp2Connection.logger.error("Caught Throwable from listener onStreamAdded.", cause);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public void allowPushTo(boolean allow)
/*      */     {
/*  811 */       if ((allow) && (this.server)) {
/*  812 */         throw new IllegalArgumentException("Servers do not allow push");
/*      */       }
/*  814 */       this.pushToAllowed = allow;
/*      */     }
/*      */     
/*      */     public boolean allowPushTo()
/*      */     {
/*  819 */       return this.pushToAllowed;
/*      */     }
/*      */     
/*      */     public int numActiveStreams()
/*      */     {
/*  824 */       return this.numActiveStreams;
/*      */     }
/*      */     
/*      */     public int maxActiveStreams()
/*      */     {
/*  829 */       return this.maxActiveStreams;
/*      */     }
/*      */     
/*      */     public void maxActiveStreams(int maxActiveStreams)
/*      */     {
/*  834 */       this.maxActiveStreams = maxActiveStreams;
/*  835 */       updateMaxStreams();
/*      */     }
/*      */     
/*      */     public int lastStreamCreated()
/*      */     {
/*  840 */       return this.nextStreamIdToCreate > 1 ? this.nextStreamIdToCreate - 2 : 0;
/*      */     }
/*      */     
/*      */     public int lastStreamKnownByPeer()
/*      */     {
/*  845 */       return this.lastStreamKnownByPeer;
/*      */     }
/*      */     
/*      */     private void lastStreamKnownByPeer(int lastKnownStream) {
/*  849 */       this.lastStreamKnownByPeer = lastKnownStream;
/*      */     }
/*      */     
/*      */     public F flowController()
/*      */     {
/*  854 */       return this.flowController;
/*      */     }
/*      */     
/*      */     public void flowController(F flowController)
/*      */     {
/*  859 */       this.flowController = ((Http2FlowController)ObjectUtil.checkNotNull(flowController, "flowController"));
/*      */     }
/*      */     
/*      */     public Http2Connection.Endpoint<? extends Http2FlowController> opposite()
/*      */     {
/*  864 */       return isLocal() ? DefaultHttp2Connection.this.remoteEndpoint : DefaultHttp2Connection.this.localEndpoint;
/*      */     }
/*      */     
/*      */     private void updateMaxStreams() {
/*  868 */       this.maxStreams = ((int)Math.min(2147483647L, this.maxActiveStreams + this.maxReservedStreams));
/*      */     }
/*      */     
/*      */     private void checkNewStreamAllowed(int streamId, Http2Stream.State state) throws Http2Exception {
/*  872 */       assert (state != Http2Stream.State.IDLE);
/*  873 */       if ((this.lastStreamKnownByPeer >= 0) && (streamId > this.lastStreamKnownByPeer)) {
/*  874 */         throw Http2Exception.streamError(streamId, Http2Error.REFUSED_STREAM, "Cannot create stream %d greater than Last-Stream-ID %d from GOAWAY.", new Object[] {
/*      */         
/*  876 */           Integer.valueOf(streamId), Integer.valueOf(this.lastStreamKnownByPeer) });
/*      */       }
/*  878 */       if (!isValidStreamId(streamId)) {
/*  879 */         if (streamId < 0) {
/*  880 */           throw new Http2NoMoreStreamIdsException();
/*      */         }
/*  882 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Request stream %d is not correct for %s connection", new Object[] { Integer.valueOf(streamId), this.server ? "server" : "client" });
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  887 */       if (streamId < this.nextStreamIdToCreate) {
/*  888 */         throw Http2Exception.closedStreamError(Http2Error.PROTOCOL_ERROR, "Request stream %d is behind the next expected stream %d", new Object[] {
/*  889 */           Integer.valueOf(streamId), Integer.valueOf(this.nextStreamIdToCreate) });
/*      */       }
/*  891 */       if (this.nextStreamIdToCreate <= 0) {
/*  892 */         throw Http2Exception.connectionError(Http2Error.REFUSED_STREAM, "Stream IDs are exhausted for this endpoint.", new Object[0]);
/*      */       }
/*  894 */       boolean isReserved = (state == Http2Stream.State.RESERVED_LOCAL) || (state == Http2Stream.State.RESERVED_REMOTE);
/*  895 */       if (((!isReserved) && (!canOpenStream())) || ((isReserved) && (this.numStreams >= this.maxStreams))) {
/*  896 */         throw Http2Exception.streamError(streamId, Http2Error.REFUSED_STREAM, "Maximum active streams violated for this endpoint.", new Object[0]);
/*      */       }
/*  898 */       if (DefaultHttp2Connection.this.isClosed()) {
/*  899 */         throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, "Attempted to create stream id %d after connection was closed", new Object[] {
/*  900 */           Integer.valueOf(streamId) });
/*      */       }
/*      */     }
/*      */     
/*      */     private boolean isLocal() {
/*  905 */       return this == DefaultHttp2Connection.this.localEndpoint;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static abstract interface Event
/*      */   {
/*      */     public abstract void process();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final class ActiveStreams
/*      */   {
/*      */     private final List<Http2Connection.Listener> listeners;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  929 */     private final Queue<DefaultHttp2Connection.Event> pendingEvents = new ArrayDeque(4);
/*  930 */     private final Set<Http2Stream> streams = new LinkedHashSet();
/*      */     private int pendingIterations;
/*      */     
/*      */     public ActiveStreams() {
/*  934 */       this.listeners = listeners;
/*      */     }
/*      */     
/*      */     public int size() {
/*  938 */       return this.streams.size();
/*      */     }
/*      */     
/*      */     public void activate(final DefaultHttp2Connection.DefaultStream stream) {
/*  942 */       if (allowModifications()) {
/*  943 */         addToActiveStreams(stream);
/*      */       } else {
/*  945 */         this.pendingEvents.add(new DefaultHttp2Connection.Event()
/*      */         {
/*      */           public void process() {
/*  948 */             DefaultHttp2Connection.ActiveStreams.this.addToActiveStreams(stream);
/*      */           }
/*      */         });
/*      */       }
/*      */     }
/*      */     
/*      */     public void deactivate(final DefaultHttp2Connection.DefaultStream stream, final Iterator<?> itr) {
/*  955 */       if ((allowModifications()) || (itr != null)) {
/*  956 */         removeFromActiveStreams(stream, itr);
/*      */       } else {
/*  958 */         this.pendingEvents.add(new DefaultHttp2Connection.Event()
/*      */         {
/*      */           public void process() {
/*  961 */             DefaultHttp2Connection.ActiveStreams.this.removeFromActiveStreams(stream, itr);
/*      */           }
/*      */         });
/*      */       }
/*      */     }
/*      */     
/*      */     public Http2Stream forEachActiveStream(Http2StreamVisitor visitor) throws Http2Exception {
/*  968 */       incrementPendingIterations();
/*      */       try {
/*  970 */         for (Http2Stream stream : this.streams) {
/*  971 */           if (!visitor.visit(stream)) {
/*  972 */             return stream;
/*      */           }
/*      */         }
/*  975 */         return null;
/*      */       } finally {
/*  977 */         decrementPendingIterations();
/*      */       }
/*      */     }
/*      */     
/*      */     void addToActiveStreams(DefaultHttp2Connection.DefaultStream stream) {
/*  982 */       if (this.streams.add(stream))
/*      */       {
/*  984 */         stream.createdBy().numActiveStreams += 1;
/*      */         
/*  986 */         for (int i = 0; i < this.listeners.size(); i++) {
/*      */           try {
/*  988 */             ((Http2Connection.Listener)this.listeners.get(i)).onStreamActive(stream);
/*      */           } catch (Throwable cause) {
/*  990 */             DefaultHttp2Connection.logger.error("Caught Throwable from listener onStreamActive.", cause);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     void removeFromActiveStreams(DefaultHttp2Connection.DefaultStream stream, Iterator<?> itr) {
/*  997 */       if (this.streams.remove(stream))
/*      */       {
/*  999 */         stream.createdBy().numActiveStreams -= 1;
/* 1000 */         DefaultHttp2Connection.this.notifyClosed(stream);
/*      */       }
/* 1002 */       DefaultHttp2Connection.this.removeStream(stream, itr);
/*      */     }
/*      */     
/*      */     boolean allowModifications() {
/* 1006 */       return this.pendingIterations == 0;
/*      */     }
/*      */     
/*      */     void incrementPendingIterations() {
/* 1010 */       this.pendingIterations += 1;
/*      */     }
/*      */     
/*      */     void decrementPendingIterations() {
/* 1014 */       this.pendingIterations -= 1;
/* 1015 */       if (allowModifications()) {
/*      */         for (;;) {
/* 1017 */           DefaultHttp2Connection.Event event = (DefaultHttp2Connection.Event)this.pendingEvents.poll();
/* 1018 */           if (event == null) {
/*      */             break;
/*      */           }
/*      */           try {
/* 1022 */             event.process();
/*      */           } catch (Throwable cause) {
/* 1024 */             DefaultHttp2Connection.logger.error("Caught Throwable while processing pending ActiveStreams$Event.", cause);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   final class DefaultPropertyKey
/*      */     implements Http2Connection.PropertyKey
/*      */   {
/*      */     final int index;
/*      */     
/*      */     DefaultPropertyKey(int index)
/*      */     {
/* 1038 */       this.index = index;
/*      */     }
/*      */     
/*      */     DefaultPropertyKey verifyConnection(Http2Connection connection) {
/* 1042 */       if (connection != DefaultHttp2Connection.this) {
/* 1043 */         throw new IllegalArgumentException("Using a key that was not created by this connection");
/*      */       }
/* 1045 */       return this;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final class PropertyKeyRegistry
/*      */   {
/* 1058 */     final List<DefaultHttp2Connection.DefaultPropertyKey> keys = new ArrayList(4);
/*      */     
/*      */     private PropertyKeyRegistry() {}
/*      */     
/*      */     DefaultHttp2Connection.DefaultPropertyKey newKey()
/*      */     {
/* 1064 */       DefaultHttp2Connection.DefaultPropertyKey key = new DefaultHttp2Connection.DefaultPropertyKey(DefaultHttp2Connection.this, this.keys.size());
/* 1065 */       this.keys.add(key);
/* 1066 */       return key;
/*      */     }
/*      */     
/*      */     int size() {
/* 1070 */       return this.keys.size();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\DefaultHttp2Connection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */