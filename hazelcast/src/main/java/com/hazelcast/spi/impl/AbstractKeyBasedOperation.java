/*
 * Copyright (c) 2008-2012, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.spi.impl;

import com.hazelcast.instance.ThreadContext;
import com.hazelcast.nio.Data;
import com.hazelcast.spi.AbstractOperation;
import com.hazelcast.spi.KeyBasedOperation;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class AbstractKeyBasedOperation extends AbstractOperation implements KeyBasedOperation {
    protected Data dataKey = null;
    protected int threadId = -1;
    protected long timeout = -1;

    public AbstractKeyBasedOperation(Data dataKey) {
        this.dataKey = dataKey;
        threadId = ThreadContext.get().getThreadId();
    }

    public AbstractKeyBasedOperation() {
        threadId = ThreadContext.get().getThreadId();
    }

    public Data getKey() {
        return dataKey;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public int getKeyHash() {
        return dataKey != null ? dataKey.getPartitionHash() : 0;
    }

    public void writeInternal(DataOutput out) throws IOException {
        dataKey.writeData(out);
        out.writeInt(threadId);
        out.writeLong(timeout);
    }

    public void readInternal(DataInput in) throws IOException {
        dataKey = new Data();
        dataKey.readData(in);
        threadId = in.readInt();
        timeout = in.readLong();
    }
}
