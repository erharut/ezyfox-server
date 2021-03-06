package com.tvd12.ezyfoxserver.statistics;

import com.tvd12.ezyfoxserver.util.EzyTimes;

public class EzyNetworkBytesFrameSecond extends EzyNetworkBytesFrame {
    private static final long serialVersionUID = -3656272442982428721L;

    public EzyNetworkBytesFrameSecond() {
        super();
    }
    
    public EzyNetworkBytesFrameSecond(long startTime) {
        super(startTime);
    }
    
    @Override
    protected long getExistsTime() {
        return EzyTimes.MILIS_OF_SECOND;
    }
    
    public EzyNetworkBytesFrame nextFrame() {
        return new EzyNetworkBytesFrameSecond(endTime);
    }
}
