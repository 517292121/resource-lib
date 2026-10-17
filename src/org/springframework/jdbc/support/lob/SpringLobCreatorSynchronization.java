/*
 * Copyright 2002-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0.
 */

package org.springframework.jdbc.support.lob;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.util.Assert;

/** Compatibility callback for closing a LobCreator after a Spring transaction. */
public class SpringLobCreatorSynchronization extends TransactionSynchronizationAdapter {

	public static final int LOB_CREATOR_SYNCHRONIZATION_ORDER =
			DataSourceUtils.CONNECTION_SYNCHRONIZATION_ORDER - 200;

	private final LobCreator lobCreator;
	private boolean beforeCompletionCalled = false;

	public SpringLobCreatorSynchronization(LobCreator lobCreator) {
		Assert.notNull(lobCreator, "LobCreator must not be null");
		this.lobCreator = lobCreator;
	}

	@Override
	public int getOrder() {
		return LOB_CREATOR_SYNCHRONIZATION_ORDER;
	}

	@Override
	public void beforeCompletion() {
		this.beforeCompletionCalled = true;
		this.lobCreator.close();
	}

	@Override
	public void afterCompletion(int status) {
		if (!this.beforeCompletionCalled) {
			this.lobCreator.close();
		}
	}
}
