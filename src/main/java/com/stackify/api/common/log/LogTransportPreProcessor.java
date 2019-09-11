package com.stackify.api.common.log;

import com.stackify.api.ErrorItem;
import com.stackify.api.LogMsg;
import com.stackify.api.LogMsgGroup;
import com.stackify.api.common.mask.Masker;
import com.stackify.api.common.util.SkipJsonUtil;

import java.util.Map;

public class LogTransportPreProcessor {

    private final Masker masker;
    private final boolean skipJson;

    /**
     * @param masker   Message Masker
     * @param skipJson Messages detected w/ json will have the #SKIPJSON tag added
     */
    public LogTransportPreProcessor(final Masker masker,
                                    final boolean skipJson) {
        this.masker = masker;
        this.skipJson = skipJson;
    }

    public void execute(final LogMsgGroup group) {
        executeMask(group);
        executeSkipJsonTag(group);
    }

    private void executeSkipJsonTag(final LogMsgGroup group) {
        if (skipJson) {
            if (group.getMsgs().size() > 0) {
                for (LogMsg logMsg : group.getMsgs()) {
                    if (logMsg.getEx() != null) {
                        executeSkipJsonTag(logMsg.getEx().getError());
                    }
                    logMsg.setData(SkipJsonUtil.execute(logMsg.getData()));
                    logMsg.setMsg(SkipJsonUtil.execute(logMsg.getMsg()));
                }
            }
        }
    }

    private void executeSkipJsonTag(final ErrorItem errorItem) {
        if (skipJson) {
            if (errorItem != null) {
                errorItem.setMessage(SkipJsonUtil.execute(errorItem.getMessage()));
                if (errorItem.getData() != null) {
                    for (Map.Entry<String, String> entry : errorItem.getData().entrySet()) {
                        entry.setValue(SkipJsonUtil.execute(entry.getValue()));
                    }
                }
                executeSkipJsonTag(errorItem.getInnerError());
            }
        }
    }

    private void executeMask(final ErrorItem errorItem) {
        if (errorItem != null) {
            errorItem.setMessage(masker.mask(errorItem.getMessage()));
            if (errorItem.getData() != null) {
                for (Map.Entry<String, String> entry : errorItem.getData().entrySet()) {
                    entry.setValue(masker.mask(entry.getValue()));
                }
            }
            executeMask(errorItem.getInnerError());
        }
    }

    /**
     * Applies masking to passed in LogMsgGroup.
     */
    private void executeMask(final LogMsgGroup group) {
        if (masker != null) {
            if (group.getMsgs() != null && group.getMsgs().size() > 0) {
                for (LogMsg logMsg : group.getMsgs()) {
                    if (logMsg.getEx() != null) {
                        executeMask(logMsg.getEx().getError());
                    }
                    logMsg.setData(masker.mask(logMsg.getData()));
                    logMsg.setMsg(masker.mask(logMsg.getMsg()));
                }
            }
        }
    }


}
