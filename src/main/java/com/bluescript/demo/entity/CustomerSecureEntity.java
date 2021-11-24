package com.bluescript.demo.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Component
@Entity
@Table(name = "CUSTOMER_SECURE")
@Getter
@Setter
@Data
@RequiredArgsConstructor
// Schema : CUSTOMER_SERVICE
public class CustomerSecureEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CUSTOMERNUMBER")
    private String customerNumber;
    @Column(name = "CUSTOMERPASS")
    private String customerpass;
    @Column(name = "STATE_INDICATOR")
    private String stateIndicator;
    @Column(name = "PASS_CHANGES")
    private String passChanges;
}
