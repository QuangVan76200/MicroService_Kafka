package com.service.acountservice.accountservice.model;

import com.service.commonservice.common.IReqRespModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse<T> implements IReqRespModel<T> {

	private T data;
	private String token;

	@Override
	public T getData() {
		// TODO Auto-generated method stub
		return this.data;
	}

	@Override
	public String geToken() {
		// TODO Auto-generated method stub
		return this.token;
	}
}

