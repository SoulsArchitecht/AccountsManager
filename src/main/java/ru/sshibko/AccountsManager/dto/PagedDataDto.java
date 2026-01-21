package ru.sshibko.AccountsManager.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Abstract layer for paged data")
public class PagedDataDto<Account> implements Serializable {

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @Schema(description = "Any type data", example = "User data")
    private List<Account> data;

    @Schema(description = "Total pages count", example = "10")
    private long total;
}
