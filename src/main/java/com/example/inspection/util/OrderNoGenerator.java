package com.example.inspection.util;

import com.example.inspection.mapper.RepairOrderMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 工单编号生成器：RO + yyyyMMdd + 3 位流水
 */
@Component
public class OrderNoGenerator {

    private final RepairOrderMapper repairOrderMapper;

    public OrderNoGenerator(RepairOrderMapper repairOrderMapper) {
        this.repairOrderMapper = repairOrderMapper;
    }

    public String next() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String maxNo = repairOrderMapper.selectMaxOrderNo();
        int seq = 0;
        if (maxNo != null && maxNo.length() >= 15) {
            try {
                seq = Integer.parseInt(maxNo.substring(12));
            } catch (NumberFormatException ignored) {
            }
        }
        return "RO" + datePart + String.format("%03d", seq + 1);
    }
}
