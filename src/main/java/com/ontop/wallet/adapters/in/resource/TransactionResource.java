package com.ontop.wallet.adapters.in.resource;

import com.ontop.wallet.adapters.in.resource.request.TransactionRequest;
import com.ontop.wallet.adapters.in.resource.response.TransactionResponse;
import com.ontop.wallet.application.ports.in.CreateTransactionUseCase;
import com.ontop.wallet.application.ports.in.FindTransactionsUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/v1/transaction")
public class TransactionResource {

    private CreateTransactionUseCase createTransactionUseCase;
    private FindTransactionsUseCase findTransactionsUseCase;

    public TransactionResource(CreateTransactionUseCase createTransactionUseCase, FindTransactionsUseCase findTransactionsUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.findTransactionsUseCase = findTransactionsUseCase;
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(
            @Valid @RequestBody TransactionRequest transactionRequest) {

        return new TransactionResponse().fromDomain(createTransactionUseCase.execute(transactionRequest.toEntity()));

    }

    @GetMapping
    public Page<TransactionResponse> findTransactions(
            @PageableDefault(size = 20, direction = Sort.Direction.DESC, sort = "createdAt") Pageable pageable,
            @RequestParam("amount") Optional<BigDecimal> amount,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<LocalDate> date,
            @RequestParam("userId") Long userId
    ) {
        return new TransactionResponse().transactionResponsePageFromDomain(findTransactionsUseCase.execute(userId, amount, date, pageable));
    }
}
