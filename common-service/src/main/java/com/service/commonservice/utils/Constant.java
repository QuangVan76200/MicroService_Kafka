package com.service.commonservice.utils;

public class Constant {

	public static final String PROFILE_ONBOARDING_TOPIC = "profileOnboarding";
	public static final String PROFILE_ONBOARDED_TOPIC = "profileOnboarded";

	public static final String STATUS_PROFILE_PENDING = "PENDING";
	public static final String STATUS_PROFILE_ACTIVE = "ACTIVE";
	
	public static final String PROFILE_CREATION_FAILED_TOPIC = "PROFILE_CREATION_FAILED_TOPIC";
	
	
	public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5 * 60 * 60;

	public static final String SIGNING_KEY = "devglan123r";

	public static final String AUTHORITIES_KEY = "scopes";
	
	public static final String STATUS_PAYMENT_CREATING = "CREATING";
    public static final String STATUS_PAYMENT_REJECTED = "REJECTED";
    public static final String STATUS_PAYMENT_PROCESSING = "PROCESSING";
    public static final String STATUS_PAYMENT_SUCCESSFUL = "SUCCESSFUL";

    public static final String PAYMENT_REQUEST_TOPIC = "paymentRequest";
    public static final String PAYMENT_CREATED_TOPIC = "paymentCreated";
    public static final String PAYMENT_COMPLETED_TOPIC = "paymentCompleted";
}
