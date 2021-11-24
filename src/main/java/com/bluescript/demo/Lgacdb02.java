package com.bluescript.demo;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import io.swagger.annotations.ApiResponses;
import com.bluescript.demo.jpa.IInsertCustomerSecureJpa;
import com.bluescript.demo.model.WsHeader;
import com.bluescript.demo.model.ErrorMsg;
import com.bluescript.demo.model.EmVariable;
import com.bluescript.demo.model.Db2Customer;
import com.bluescript.demo.model.Dfhcommarea;

@Getter
@Setter
@RequiredArgsConstructor
@Log4j2
@Component

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 400, message = "This is a bad request, please follow the API documentation for the proper request format"),
        @io.swagger.annotations.ApiResponse(code = 401, message = "Due to security constraints, your access request cannot be authorized"),
        @io.swagger.annotations.ApiResponse(code = 500, message = "The server/Application is down. Please contact support team.") })
        
public class Lgacdb02 {

    @Autowired
    private WsHeader wsHeader;
    @Autowired
    private ErrorMsg errorMsg;
    @Autowired
    private EmVariable emVariable;
    @Autowired
    private Db2Customer db2Customer;
    @Autowired
    private Dfhcommarea dfhcommarea;
    private String wsTime;
    private String wsDate;
    private String caData;
    private int db2CustomernumInt;
    private int db2CustomercntInt;
    @Autowired
    private IInsertCustomerSecureJpa InsertCustomerSecureJpa;
    private String wsAbstime;

    private int eibcalen;
    private String caErrorMsg;

    @Value("${api.LGSTSQ.host}")
    private String LGSTSQ_HOST;
    @Value("${api.LGSTSQ.uri}")
    private String LGSTSQ_URI;

    @PostMapping("/lgacdb02")
    public ResponseEntity<Dfhcommarea> mainline(@RequestBody Dfhcommarea payload) {
        log.debug("Methodmainlinestarted..");
        // if(eibcalen == 0 )
        // {
        // errorMsg.setEmVariable(" NO COMMAREA RECEIVED");writeErrorMessage();
        // log.error("Error code :", LGCA);
        // throw new LGCAException("LGCA");

        // }
        BeanUtils.copyProperties(payload, dfhcommarea);
        dfhcommarea.setD2ReturnCode(00);
        switch (dfhcommarea.getD2RequestId()) {
        case "02ACUS":
            db2CustomernumInt = (int) dfhcommarea.getD2CustomerNum();
            db2CustomercntInt = Integer.parseInt(dfhcommarea.getD2CustsecrCount());
            insertCustomerPassword();
            break;
        default:
            dfhcommarea.setD2ReturnCode(99); /* return */
        } /* return */

        log.debug("Method mainline completed..");
        return new ResponseEntity<>(dfhcommarea, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public void insertCustomerPassword() {
        log.debug("MethodinsertCustomerPasswordstarted..");
        emVariable.setEmSqlreq(" INSERT SECURITY");
        try {
            InsertCustomerSecureJpa.insertCustomerSecureForDb2CustomernumIntAndD2CustsecrPassAndD2CustsecrState(
                    db2CustomernumInt, dfhcommarea.getD2CustsecrPass(), dfhcommarea.getD2CustsecrState(),
                    db2CustomercntInt);

            /* return */

        } catch (Exception e) {
            dfhcommarea.setD2ReturnCode(98);
            log.error(e);
            writeErrorMessage();

        }

        log.debug("Method insertCustomerPassword completed..");
    }

    public void writeErrorMessage() {
        log.info("MethodwriteErrorMessagestarted..");
        wsAbstime = LocalTime.now().toString();
        wsAbstime = LocalTime.now().toString();
        wsDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMDDYYYY"));
        wsTime = LocalTime.now().toString();
        errorMsg.setEmDate(wsDate.substring(0, 8));
        errorMsg.setEmTime(wsTime.substring(0, 6));
        WebClient webclientBuilder = WebClient.create(LGSTSQ_HOST);

        try {
            Mono<ErrorMsg> lgstsqResp = webclientBuilder.post().uri(LGSTSQ_URI)
                    .body(Mono.just(errorMsg), ErrorMsg.class).retrieve().bodyToMono(ErrorMsg.class)
                    .timeout(Duration.ofMillis(10_000));
            errorMsg = lgstsqResp.block();
        } catch (Exception e) {
            log.error(e);
        }
        if (eibcalen > 0) {
            if (eibcalen < 91) {
                caData = dfhcommarea.toFixedWidthString();
                try {
                    Mono<String> lgstsqResp = webclientBuilder.post().uri(LGSTSQ_URI)
                            .body(Mono.just(caErrorMsg), String.class).retrieve().bodyToMono(String.class)
                            .timeout(Duration.ofMillis(10_000));
                    caErrorMsg = lgstsqResp.block();
                } catch (Exception e) {
                    log.error(e);
                }

            } else {
                caData = (dfhcommarea.getD2RequestId() + dfhcommarea.getD2ReturnCode() + dfhcommarea.getD2CustomerNum()
                        + dfhcommarea.getD2CustsecrPass() + dfhcommarea.getD2CustsecrCount()
                        + dfhcommarea.getD2CustsecrState() + dfhcommarea.getD2CustsecrData().substring(0, 35));
                try {
                    Mono<String> lgstsqResp = webclientBuilder.post().uri(LGSTSQ_URI)
                            .body(Mono.just(caErrorMsg), String.class).retrieve().bodyToMono(String.class)
                            .timeout(Duration.ofMillis(10_000));
                    caErrorMsg = lgstsqResp.block();
                } catch (Exception e) {
                    log.error(e);
                }

            }

        }

        log.debug("Method writeErrorMessage completed..");
    }

    /* End of program */
}
