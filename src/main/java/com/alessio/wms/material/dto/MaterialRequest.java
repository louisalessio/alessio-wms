package com.alessio.wms.material.dto;

import java.math.BigDecimal;

public record MaterialRequest(String code, String description, BigDecimal cost) {
}
