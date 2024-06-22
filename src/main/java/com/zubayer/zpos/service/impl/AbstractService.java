package com.zubayer.zpos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.zubayer.zpos.service.ZSessionManager;

/**
 * @author Zubayer Ahamed
 * @since Jun 22, 2024
 */
public abstract class AbstractService {

	@Autowired protected ZSessionManager sessionManager;
}
