package ru.sshibko.AccountsManager.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedDataDto<Account> implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Account> data;

    private long total;
}
