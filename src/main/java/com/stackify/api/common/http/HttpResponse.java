package com.stackify.api.common.http;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Darin Howard
 */
@AllArgsConstructor
@Data
public class HttpResponse {
    private int statusCode;
    private String responseBody;
}