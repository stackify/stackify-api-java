package com.stackify.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder(builderClassName = "Builder", builderMethodName = "newBuilder", toBuilder = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Container {

    @JsonProperty("ImageId")
    private String imageId;

    @JsonProperty("ImageRepository")
    private String imageRepository;

    @JsonProperty("ImageTag")
    private String imageTag;

    @JsonProperty("ContainerId")
    private String containerId;

    @JsonProperty("ContainerName")
    private String containerName;
}
