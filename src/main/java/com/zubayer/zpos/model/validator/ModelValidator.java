package com.zubayer.zpos.model.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.zubayer.zpos.entity.Zbusiness;
import com.zubayer.zpos.service.ZSessionManager;

import jakarta.validation.Validator;

/**
 * @author Zubayer Ahamed
 * @since May 10, 2024
 * CSE202401068
 */
@Component
public class ModelValidator extends ConstraintValidator {

	@Autowired private ZSessionManager sessionManager;


	public void validateZbusiness(Zbusiness zbusiness, Errors errors, Validator validator) {
		if(zbusiness == null) return;
		super.validate(zbusiness, errors, validator);
	}
}
