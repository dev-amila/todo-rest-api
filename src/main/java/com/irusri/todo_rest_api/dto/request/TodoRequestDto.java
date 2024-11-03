package com.irusri.todo_rest_api.dto.request;

import java.util.Date;

public record TodoRequestDto(String task, Date deadline) {
}
