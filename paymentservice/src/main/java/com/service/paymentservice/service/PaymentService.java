package com.service.paymentservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.service.commonservice.common.CommonException;
import com.service.commonservice.model.AccountDTO;
import com.service.commonservice.utils.Constant;
import com.service.paymentservice.dao.IPaymentDao;
import com.service.paymentservice.model.PaymentDTO;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class PaymentService {

	private IPaymentDao paymentDao;

	private WebClient webClientAccount;

	public PaymentService(IPaymentDao paymentDao, WebClient webClientAccount) {
		this.paymentDao = paymentDao;
		this.webClientAccount = webClientAccount;
	}

	public Flux<PaymentDTO> getAllPayment(Long id) {
		return paymentDao.findByAccountId(id).map(PaymentDTO::entityToDTO).switchIfEmpty(
				Mono.error(new CommonException("P02", "Account don't have payment", HttpStatus.NOT_FOUND)));
	}

	public Mono<PaymentDTO> makePayment(PaymentDTO paymentDTO) {
		return webClientAccount.get().uri("/checkBalance/" + paymentDTO.getAccountId()).retrieve().bodyToMono(AccountDTO.class)
				.flatMap(accountDTO -> {
					if (paymentDTO.getAmount() < accountDTO.getBalance()) {
						paymentDTO.setStatus(Constant.STATUS_PAYMENT_CREATING);
					}
					else {
						throw new CommonException("P01", "Balance is not enoungh", HttpStatus.BAD_REQUEST);
					}
					return createNewPayment(paymentDTO);
				});
	}
	
	public Mono<PaymentDTO> createNewPayment(PaymentDTO paymentDTO){
		return Mono.just(paymentDTO)
				.map(PaymentDTO::dtoToEntity)
				.flatMap(payment ->  paymentDao.save(payment))
				.map(PaymentDTO::entityToDTO)
				.doOnError(throwable -> log.error(throwable.getMessage()));
		
	}
 
}
