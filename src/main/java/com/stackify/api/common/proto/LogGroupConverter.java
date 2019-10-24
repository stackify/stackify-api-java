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
 * Handles mapping/converting StackifyProto.LogGroup (protobuf) to com.stackify.api.LogMsgGroup (api).
 */
public class LogGroupConverter {

    private static TraceFrame convert(@NonNull final StackifyProto.LogGroup.Log.Error.ErrorItem.TraceFrame traceFrame) {
        return TraceFrame.newBuilder()
                .codeFileName(traceFrame.getCodeFilename())
                .lineNum(traceFrame.getLineNumber())
                .method(traceFrame.getMethod())
                .build();
    }

    private static ErrorItem convert(@NonNull final StackifyProto.LogGroup.Log.Error.ErrorItem errorItem) {
        ErrorItem.Builder builder = ErrorItem.newBuilder()
                .message(errorItem.getMessage())
                .errorType(errorItem.getErrorType())
                .errorTypeCode(errorItem.getErrorTypeCode())
                .sourceMethod(errorItem.getSourceMethod())
                .innerError(errorItem.hasInnerError() ? convert(errorItem.getInnerError()) : null);

        if (errorItem.getDataMap().size() > 0) {
            builder.data(errorItem.getDataMap());
        }

        if (errorItem.getStacktraceList().size() > 0) {
            List<TraceFrame> stackTrace = new ArrayList<>();
            for (com.stackify.api.common.proto.StackifyProto.LogGroup.Log.Error.ErrorItem.TraceFrame traceFrame : errorItem.getStacktraceList()) {
                stackTrace.add(convert(traceFrame));
            }
            builder.stackTrace(stackTrace);
        }

        return builder.build();
    }

    private static WebRequestDetail convert(@NonNull final StackifyProto.LogGroup.Log.Error.WebRequestDetail webRequestDetail) {
        return WebRequestDetail.newBuilder()
                .userIpAddress(webRequestDetail.getUserIpAddress())
                .httpMethod(webRequestDetail.getHttpMethod())
                .requestProtocol(webRequestDetail.getRequestProtocol())
                .requestUrl(webRequestDetail.getRequestUrl())
                .requestUrlRoot(webRequestDetail.getRequestUrlRoot())
                .referralUrl(webRequestDetail.getReferralUrl())
                .headers(webRequestDetail.getHeadersMap())
                .cookies(webRequestDetail.getCookiesMap())
                .queryString(webRequestDetail.getQuerystringMap())
                .postData(webRequestDetail.getPostDataMap())
                .sessionData(webRequestDetail.getSessionDataMap())
                .postDataRaw(webRequestDetail.getPostDataRaw())
                .mvcAction(webRequestDetail.getMvcAction())
                .mvcController(webRequestDetail.getMvcController())
                .mvcArea(webRequestDetail.getMvcArea())
                .build();
    }

    private static EnvironmentDetail convert(@NonNull final StackifyProto.LogGroup.Log.Error.EnvironmentDetail environmentDetail) {
        return EnvironmentDetail.newBuilder()
                .deviceName(environmentDetail.getDeviceName())
                .appName(environmentDetail.getApplicationName())
                .appLocation(environmentDetail.getApplicationLocation())
                .configuredAppName(environmentDetail.getConfiguredApplicationName())
                .configuredEnvironmentName(environmentDetail.getConfiguredEnvironmentName())
                .build();
    }

    private static StackifyError convert(@NonNull final StackifyProto.LogGroup.Log.Error error) {
        return StackifyError.newBuilder()
                .environmentDetail(error.hasEnvironmentDetail() ? convert(error.getEnvironmentDetail()) : null)
                .occurredEpochMillis(error.getDateMillis())
                .error(error.hasErrorItem() ? convert(error.getErrorItem()) : null)
                .webRequestDetail(error.hasWebRequestDetail() ? convert(error.getWebRequestDetail()) : null)
                .serverVariables(error.getServerVariablesMap())
                .customerName(error.getCustomerName())
                .userName(error.getUsername())
                .build();
    }

    private static LogMsg convert(@NonNull final StackifyProto.LogGroup.Log log) {
        return LogMsg.newBuilder()
                .msg(log.getMessage())
                .data(log.getData())
                .ex(log.hasError() ? convert(log.getError()) : null)
                .th(log.getThreadName())
                .epochMs(log.getDateMillis())
                .level(log.getLevel())
                .transId(log.getTransactionId())
                .srcMethod(log.getSourceMethod())
                .srcLine(log.getSourceLine())
                .id(log.getId())
                .tags(log.getTagsList())
                .build();
    }

    private static Kubernetes convert(@NonNull final StackifyProto.LogGroup.Kubernetes kubernetes) {
        return Kubernetes.newBuilder()
                .podName(kubernetes.getPodName())
                .podNamespace(kubernetes.getPodNamespace())
                .clusterName(kubernetes.getClusterName())
                .build();
    }

    private static Container convert(@NonNull final StackifyProto.LogGroup.Container container) {
        return Container.newBuilder()
                .imageId(container.getImageId())
                .imageRepository(container.getImageRepository())
                .imageTag(container.getImageTag())
                .containerId(container.getContainerId())
                .containerName(container.getContainerName())
                .build();
    }

    public static LogMsgGroup convert(@NonNull final StackifyProto.LogGroup logGroup) {
        LogMsgGroup.Builder builder = LogMsgGroup.newBuilder()
                .env(logGroup.getEnvironment())
                .serverName(logGroup.getServerName())
                .appName(logGroup.getApplicationName())
                .appLoc(logGroup.getApplicationLocation())
                .logger(logGroup.getLogger())
                .platform(logGroup.getPlatform());

        if (logGroup.getContainer() != null) {
            builder.container(convert(logGroup.getContainer()));
        }

        if (logGroup.getKubernetes() != null) {
            builder.kubernetes(convert(logGroup.getKubernetes()));
        }

        List<LogMsg> logMsgs = new ArrayList<>();
        if (logGroup.getLogsCount() > 0) {
            for (StackifyProto.LogGroup.Log log : logGroup.getLogsList()) {
                logMsgs.add(convert(log));
            }
            builder.msgs(logMsgs);
        }

        return builder.build();
    }


}
