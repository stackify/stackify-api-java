package com.stackify.api.common.log;

import com.stackify.api.LogMsgGroup;

public interface LogTransport {

    void send(final LogMsgGroup group) throws Exception;

}
