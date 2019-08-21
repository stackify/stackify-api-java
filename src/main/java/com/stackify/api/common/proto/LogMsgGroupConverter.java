package com.stackify.api.common.proto;

import com.stackify.api.Container;
import com.stackify.api.EnvironmentDetail;
import com.stackify.api.ErrorItem;
import com.stackify.api.Kubernetes;
import com.stackify.api.LogMsg;
import com.stackify.api.LogMsgGroup;
import com.stackify.api.StackifyError;
import com.stackify.api.TraceFrame;
import com.stackify.api.WebRequestDetail;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles mapping/converting com.stackify.api.LogMsgGroup (api) to StackifyProto.LogGroup (protobuf).
 */
public class LogMsgGroupConverter {

    private static StackifyProto.LogGroup.Log.Error.ErrorItem.TraceFrame convert(@NonNull final TraceFrame traceFrame) {
        StackifyProto.LogGroup.Log.Error.ErrorItem.TraceFrame.Builder builder = StackifyProto.LogGroup.Log.Error.ErrorItem.TraceFrame.newBuilder();

        if (traceFrame.getCodeFileName() != null) {
            builder.setCodeFilename(traceFrame.getCodeFileName());
        }
        if (traceFrame.getLineNum() != null) {
            builder.setLineNumber(traceFrame.getLineNum());
        }
        if (traceFrame.getMethod() != null) {
            builder.setMethod(traceFrame.getMethod());
        }

        return builder.build();
    }

    private static StackifyProto.LogGroup.Log.Error.ErrorItem convert(@NonNull final ErrorItem errorItem) {
        StackifyProto.LogGroup.Log.Error.ErrorItem.Builder builder = StackifyProto.LogGroup.Log.Error.ErrorItem.newBuilder();

        if (errorItem.getMessage() != null) {
            builder.setMessage(errorItem.getMessage());
        }
        if (errorItem.getErrorType() != null) {
            builder.setErrorType(errorItem.getErrorType());
        }
        if (errorItem.getErrorTypeCode() != null) {
            builder.setErrorTypeCode(errorItem.getErrorTypeCode());
        }
        if (errorItem.getSourceMethod() != null) {
            builder.setSourceMethod(errorItem.getSourceMethod());
        }
        if (errorItem.getInnerError() != null) {
            builder.setInnerError(convert(errorItem.getInnerError()));
        }
        if (errorItem.getData() != null && errorItem.getData().size() > 0) {
            builder.putAllData(errorItem.getData());
        }
        if (errorItem.getStackTrace() != null && errorItem.getStackTrace().size() > 0) {
            List<StackifyProto.LogGroup.Log.Error.ErrorItem.TraceFrame> stackTrace = new ArrayList<>();
            for (TraceFrame traceFrame : errorItem.getStackTrace()) {
                stackTrace.add(convert(traceFrame));
            }
            builder.addAllStacktrace(stackTrace);
        }

        return builder.build();
    }

    private static StackifyProto.LogGroup.Log.Error.WebRequestDetail convert(@NonNull final WebRequestDetail webRequestDetail) {
        StackifyProto.LogGroup.Log.Error.WebRequestDetail.Builder builder = StackifyProto.LogGroup.Log.Error.WebRequestDetail.newBuilder();

        if (webRequestDetail.getUserIpAddress() != null) {
            builder.setUserIpAddress(webRequestDetail.getUserIpAddress());
        }
        if (webRequestDetail.getHttpMethod() != null) {
            builder.setHttpMethod(webRequestDetail.getHttpMethod());
        }
        if (webRequestDetail.getRequestProtocol() != null) {
            builder.setRequestProtocol(webRequestDetail.getRequestProtocol());
        }
        if (webRequestDetail.getRequestUrl() != null) {
            builder.setRequestUrl(webRequestDetail.getRequestUrl());
        }
        if (webRequestDetail.getRequestUrlRoot() != null) {
            builder.setRequestUrlRoot(webRequestDetail.getRequestUrlRoot());
        }
        if (webRequestDetail.getReferralUrl() != null) {
            builder.setReferralUrl(webRequestDetail.getReferralUrl());
        }
        if (webRequestDetail.getHeaders() != null) {
            builder.putAllHeaders(webRequestDetail.getHeaders());
        }
        if (webRequestDetail.getCookies() != null) {
            builder.putAllCookies(webRequestDetail.getCookies());
        }
        if (webRequestDetail.getQueryString() != null) {
            builder.putAllQuerystring(webRequestDetail.getQueryString());
        }
        if (webRequestDetail.getPostData() != null) {
            builder.putAllPostData(webRequestDetail.getPostData());
        }
        if (webRequestDetail.getSessionData() != null) {
            builder.putAllSessionData(webRequestDetail.getSessionData());
        }
        if (webRequestDetail.getPostDataRaw() != null) {
            builder.setPostDataRaw(webRequestDetail.getPostDataRaw());
        }
        if (webRequestDetail.getMvcAction() != null) {
            builder.setMvcAction(webRequestDetail.getMvcAction());
        }
        if (webRequestDetail.getMvcController() != null) {
            builder.setMvcController(webRequestDetail.getMvcController());
        }
        if (webRequestDetail.getMvcArea() != null) {
            builder.setMvcArea(webRequestDetail.getMvcArea());
        }

        return builder.build();
    }

    private static StackifyProto.LogGroup.Log.Error.EnvironmentDetail convert(@NonNull final EnvironmentDetail environmentDetail) {
        StackifyProto.LogGroup.Log.Error.EnvironmentDetail.Builder builder = StackifyProto.LogGroup.Log.Error.EnvironmentDetail.newBuilder();

        if (environmentDetail.getDeviceName() != null) {
            builder.setDeviceName(environmentDetail.getDeviceName());
        }
        if (environmentDetail.getAppName() != null) {
            builder.setApplicationName(environmentDetail.getAppName());
        }
        if (environmentDetail.getAppLocation() != null) {
            builder.setApplicationLocation(environmentDetail.getAppLocation());
        }
        if (environmentDetail.getConfiguredAppName() != null) {
            builder.setConfiguredApplicationName(environmentDetail.getConfiguredAppName());
        }
        if (environmentDetail.getConfiguredEnvironmentName() != null) {
            builder.setConfiguredEnvironmentName(environmentDetail.getConfiguredEnvironmentName());
        }

        return builder.build();
    }

    private static StackifyProto.LogGroup.Log.Error convert(@NonNull final StackifyError stackifyError) {
        StackifyProto.LogGroup.Log.Error.Builder builder = StackifyProto.LogGroup.Log.Error.newBuilder();

        if (stackifyError.getEnvironmentDetail() != null) {
            builder.setEnvironmentDetail(convert(stackifyError.getEnvironmentDetail()));
        }
        if (stackifyError.getOccurredEpochMillis() != null) {
            builder.setDateMillis(stackifyError.getOccurredEpochMillis().getTime());
        }
        if (stackifyError.getError() != null) {
            builder.setErrorItem(convert(stackifyError.getError()));
        }
        if (stackifyError.getWebRequestDetail() != null) {
            builder.setWebRequestDetail(convert(stackifyError.getWebRequestDetail()));
        }
        if (stackifyError.getServerVariables() != null) {
            builder.putAllServerVariables(stackifyError.getServerVariables());
        }
        if (stackifyError.getCustomerName() != null) {
            builder.setCustomerName(stackifyError.getCustomerName());
        }
        if (stackifyError.getUserName() != null) {
            builder.setUsername(stackifyError.getUserName());
        }

        return builder.build();
    }

    private static StackifyProto.LogGroup.Log convert(@NonNull final LogMsg logMsg) {
        StackifyProto.LogGroup.Log.Builder builder = StackifyProto.LogGroup.Log.newBuilder();

        if (logMsg.getMsg() != null) {
            builder.setMessage(logMsg.getMsg());
        }
        if (logMsg.getData() != null) {
            builder.setData(logMsg.getData());
        }
        if (logMsg.getEx() != null) {
            builder.setError(convert(logMsg.getEx()));
        }
        if (logMsg.getTh() != null) {
            builder.setThreadName(logMsg.getTh());
        }
        if (logMsg.getEpochMs() != null) {
            builder.setDateMillis(logMsg.getEpochMs());
        }
        if (logMsg.getLevel() != null) {
            builder.setLevel(logMsg.getLevel());
        }
        if (logMsg.getTransId() != null) {
            builder.setTransactionId(logMsg.getTransId());
        }
        if (logMsg.getSrcMethod() != null) {
            builder.setSourceMethod(logMsg.getSrcMethod());
        }
        if (logMsg.getSrcLine() != null) {
            builder.setSourceLine(logMsg.getSrcLine());
        }
        if (logMsg.getId() != null) {
            builder.setId(logMsg.getId());
        }
        if (logMsg.getTags() != null) {
            builder.addAllTags(logMsg.getTags());
        }

        return builder.build();
    }

    private static StackifyProto.LogGroup.Container convert(@NonNull final Container container) {
        StackifyProto.LogGroup.Container.Builder builder = StackifyProto.LogGroup.Container.newBuilder();

        if (container.getImageId() != null) {
            builder.setImageId(container.getImageId());
        }
        if (container.getImageRepository() != null) {
            builder.setImageRepository(container.getImageRepository());
        }
        if (container.getImageTag() != null) {
            builder.setImageTag(container.getImageTag());
        }
        if (container.getContainerId() != null) {
            builder.setContainerId(container.getContainerId());
        }
        if (container.getContainerName() != null) {
            builder.setContainerName(container.getContainerName());
        }

        return builder.build();
    }

    private static StackifyProto.LogGroup.Kubernetes convert(@NonNull final Kubernetes kubernetes) {
        StackifyProto.LogGroup.Kubernetes.Builder builder = StackifyProto.LogGroup.Kubernetes.newBuilder();

        if (kubernetes.getPodName() != null) {
            builder.setPodName(kubernetes.getPodName());
        }
        if (kubernetes.getPodNamespace() != null) {
            builder.setPodNamespace(kubernetes.getPodNamespace());
        }
        if (kubernetes.getClusterName() != null) {
            builder.setClusterName(kubernetes.getClusterName());
        }

        return builder.build();
    }

    public static StackifyProto.LogGroup convert(@NonNull final LogMsgGroup logMsgGroup) {
        StackifyProto.LogGroup.Builder builder = StackifyProto.LogGroup.newBuilder();

        if (logMsgGroup.getEnv() != null) {
            builder.setEnvironment(logMsgGroup.getEnv());
        }
        if (logMsgGroup.getServerName() != null) {
            builder.setServerName(logMsgGroup.getServerName());
        }
        if (logMsgGroup.getAppName() != null) {
            builder.setApplicationName(logMsgGroup.getAppName());
        }
        if (logMsgGroup.getAppLoc() != null) {
            builder.setApplicationLocation(logMsgGroup.getAppLoc());
        }
        if (logMsgGroup.getLogger() != null) {
            builder.setLogger(logMsgGroup.getLogger());
        }
        if (logMsgGroup.getPlatform() != null) {
            builder.setPlatform(logMsgGroup.getPlatform());
        }
        if (logMsgGroup.getContainer() != null) {
            builder.setContainer(convert(logMsgGroup.getContainer()));
        }
        if (logMsgGroup.getKubernetes() != null) {
            builder.setKubernetes(convert(logMsgGroup.getKubernetes()));
        }
        List<StackifyProto.LogGroup.Log> logMsgs = new ArrayList<StackifyProto.LogGroup.Log>();
        if (logMsgGroup.getMsgs() != null && logMsgGroup.getMsgs().size() > 0) {
            for (LogMsg logMsg : logMsgGroup.getMsgs()) {
                logMsgs.add(convert(logMsg));
            }
            builder.addAllLogs(logMsgs);
        }

        return builder.build();
    }


}
