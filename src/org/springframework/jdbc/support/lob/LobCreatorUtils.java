/*
 * Copyright 2002-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.jdbc.support.lob;

import javax.transaction.Status;
import javax.transaction.TransactionManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Compatibility helper retained for the Hibernate 3 LOB user types.
 * Spring 5 removed this Spring 3-era utility, while the original RISS2
 * Hibernate mappings still use it to close temporary LOB resources.
 */
public abstract class LobCreatorUtils {

	private static final Log logger = LogFactory.getLog(LobCreatorUtils.class);

	public static void registerTransactionSynchronization(
			LobCreator lobCreator, TransactionManager jtaTransactionManager) throws IllegalStateException {

		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			logger.debug("Registering Spring transaction synchronization for LobCreator");
			TransactionSynchronizationManager.registerSynchronization(
					new SpringLobCreatorSynchronization(lobCreator));
		}
		else {
			if (jtaTransactionManager != null) {
				try {
					int jtaStatus = jtaTransactionManager.getStatus();
					if (jtaStatus == Status.STATUS_ACTIVE || jtaStatus == Status.STATUS_MARKED_ROLLBACK) {
						logger.debug("Registering JTA transaction synchronization for LobCreator");
						jtaTransactionManager.getTransaction().registerSynchronization(
								new JtaLobCreatorSynchronization(lobCreator));
						return;
					}
				}
				catch (Throwable ex) {
					throw new TransactionSystemException(
							"Could not register synchronization with JTA TransactionManager", ex);
				}
			}
			throw new IllegalStateException("Active Spring transaction synchronization or active " +
					"JTA transaction with specified [javax.transaction.TransactionManager] required");
		}
	}
}
