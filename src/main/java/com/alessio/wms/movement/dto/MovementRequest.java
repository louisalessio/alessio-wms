package com.alessio.wms.movement.dto;

public record MovementRequest(String materialCode, Integer quantity, String operator, String siteName) {
}