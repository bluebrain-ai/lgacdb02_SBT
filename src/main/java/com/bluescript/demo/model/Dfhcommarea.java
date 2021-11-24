package com.bluescript.demo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import java.util.*;

import org.springframework.stereotype.Component;

@Data
@Component

public class Dfhcommarea {
    private String d2RequestId;
    private int d2ReturnCode;
    private long d2CustomerNum;
    private String d2CustsecrPass;
    private String d2CustsecrCount;
    private String d2CustsecrState;
    private String d2CustsecrData;

    public String toFixedWidthString() {
        return d2RequestId + d2ReturnCode + d2CustomerNum + d2CustsecrPass + d2CustsecrCount + d2CustsecrState
                + d2CustsecrData;
    }

}