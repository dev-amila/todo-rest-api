package com.irusri.todo_rest_api.util;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StandardResponse{
    private int code;
    private String message;
    private Object data;
}
