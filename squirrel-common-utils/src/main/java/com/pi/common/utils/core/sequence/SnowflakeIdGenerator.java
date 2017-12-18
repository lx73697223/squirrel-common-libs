package com.pi.common.utils.core.sequence;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class SnowflakeIdGenerator implements UniqueIdGenerator {

    /**
     * 开始时间戳, 生成Id时使用这个值作为被减数
     */
    private final long TWEPOCH = 1513512626564L;

    private final int INSTANCE_ID_BITS = 6;

    private final int SEQUENCE_BITS = 10;

    private final int INSTANCE_ID_SHIFT = SEQUENCE_BITS;

    private final int TIMESTAMP_SHIFT = SEQUENCE_BITS + INSTANCE_ID_BITS;

    private final int SEQUENCE_MASK = ~(-1 << SEQUENCE_BITS);

    private final AtomicReference<Sequence> SEQUENCE = new AtomicReference<>();

    private int instanceId;

    public SnowflakeIdGenerator(int instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public Long next() {
        Sequence currentSequence, nextSequence;
        do {
            currentSequence = SEQUENCE.get();
            long currentTimestamp = currentTimestamp();

            if (currentSequence != null && currentSequence.getTimestamp() >= currentTimestamp) {
                if (currentSequence.getTimestamp() != currentTimestamp) {
                    throw new RuntimeException(
                            String.format("Clock is moving backwards. Rejecting requests for %d milliseconds. instanceId:%d",
                                    currentSequence.getTimestamp() - currentTimestamp, this.instanceId));
                }

                int nextValue = currentSequence.getValue() + 1 & SEQUENCE_MASK;
                if (nextValue == 0) {
                    currentTimestamp = waitForNextTimestamp();
                }
                nextSequence = new SnowflakeIdGenerator.Sequence(nextValue, currentTimestamp);
            } else {
                nextSequence = new SnowflakeIdGenerator.Sequence(0, currentTimestamp);
            }
        } while (!SEQUENCE.compareAndSet(currentSequence, nextSequence));

        return nextSequence.getId();
    }

    private long currentTimestamp() {
        return new Date().getTime();
    }

    private long waitForNextTimestamp() {
        while (currentTimestamp() <= SEQUENCE.get().getTimestamp()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
        }
        return currentTimestamp();
    }

    private class Sequence {

        private final int value;

        private final long timestamp;

        Sequence(int value, long timestamp) {
            this.value = value;
            this.timestamp = timestamp;
        }

        int getValue() {
            return value;
        }

        long getTimestamp() {
            return timestamp;
        }

        long getId() {
            return ((timestamp - TWEPOCH) << TIMESTAMP_SHIFT) | (instanceId << INSTANCE_ID_SHIFT) | value;
        }
    }

}
