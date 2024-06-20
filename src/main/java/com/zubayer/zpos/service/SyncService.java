package com.zubayer.zpos.service;

import com.zubayer.zpos.enums.SyncMethod;

/**
 * @author Zubayer Ahamed
 * @since Jun 4, 2024
 */
public interface SyncService {

	void syncBusiness(Integer business, Integer outlet, Integer shop, Integer terminal, SyncMethod syncMethod);

	void syncMasterData(Integer business, Integer outlet, Integer shop, Integer terminal, SyncMethod syncMethod);

}
