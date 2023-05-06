/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kafka.log.es;

import sdk.elastic.stream.api.AppendResult;
import sdk.elastic.stream.api.FetchResult;
import sdk.elastic.stream.api.KeyValue;
import sdk.elastic.stream.api.RecordBatch;
import sdk.elastic.stream.api.RecordBatchWithContext;
import sdk.elastic.stream.api.Stream;
import sdk.elastic.stream.client.common.FutureUtil;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class DefaultElasticStreamSegment implements ElasticStreamSegment {
    /**
     * the real start offset of this segment in the stream.
     */
    private final long startOffsetInStream;
    private final Stream stream;
    private long nextOffset;
    private boolean sealed = false;

    public DefaultElasticStreamSegment(Stream stream, long startOffsetInStream) {
        this(stream, startOffsetInStream, 0);
    }

    public DefaultElasticStreamSegment(Stream stream, long startOffsetInStream, long endOffsetInStream) {
        this.stream = stream;
        this.nextOffset = endOffsetInStream - startOffsetInStream;
        this.startOffsetInStream = startOffsetInStream;
        System.out.println("new segment: " + startOffsetInStream + " in stream " + stream.streamId());
    }

    @Override
    public CompletableFuture<AppendResult> append(RecordBatch recordBatch) {
        if (sealed) {
            return FutureUtil.failedFuture(new IllegalStateException("stream segment " + this + " is sealed"));
        }
        nextOffset += recordBatch.count();
        return stream.append(recordBatch).thenApply(AppendResultWrapper::new);
    }

    @Override
    public CompletableFuture<FetchResult> fetch(long startOffset, int maxBytesHint) {
        return stream.fetch(startOffsetInStream + startOffset, maxBytesHint).thenApply(FetchResultWrapper::new);
    }

    @Override
    public long nextOffset() {
        return nextOffset;
    }

    @Override
    public long startOffsetInStream() {
        return startOffsetInStream;
    }

    @Override
    public void destroy() {
        // TODO: update ElasticLogMeta and persist meta
    }

    @Override
    public void seal() {
        this.sealed = true;
    }

    @Override
    public String toString() {
        // TODO: stream info
        return "DefaultElasticStreamSegment{" +
                "startOffsetInStream=" + startOffsetInStream +
                ", stream=" + stream +
                ", nextOffset=" + nextOffset +
                ", sealed=" + sealed +
                '}';
    }

    class AppendResultWrapper implements AppendResult {
        private final AppendResult inner;

        public AppendResultWrapper(AppendResult inner) {
            this.inner = inner;
        }

        @Override
        public long baseOffset() {
            return inner.baseOffset() - startOffsetInStream;
        }
    }

    class FetchResultWrapper implements FetchResult {
        private final List<RecordBatchWithContext> recordBatchList;

        public FetchResultWrapper(FetchResult fetchResult) {
            this.recordBatchList = fetchResult.recordBatchList().stream().map(RecordBatchWithContextWrapper::new).collect(Collectors.toList());
        }

        @Override
        public List<RecordBatchWithContext> recordBatchList() {
            return recordBatchList;
        }
    }

    class RecordBatchWithContextWrapper implements RecordBatchWithContext {
        private final RecordBatchWithContext inner;

        public RecordBatchWithContextWrapper(RecordBatchWithContext inner) {
            this.inner = inner;
        }

        @Override
        public long baseOffset() {
            return inner.baseOffset() - startOffsetInStream;
        }

        @Override
        public long lastOffset() {
            return inner.lastOffset() - startOffsetInStream;
        }

        @Override
        public int count() {
            return inner.count();
        }

        @Override
        public long baseTimestamp() {
            return inner.baseTimestamp();
        }

        @Override
        public List<KeyValue> properties() {
            return inner.properties();
        }

        @Override
        public ByteBuffer rawPayload() {
            return inner.rawPayload();
        }
    }
}