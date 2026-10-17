/*
 * Copyright 2002-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0.
 */

package org.springframework.jdbc.support.lob;

import javax.transaction.Synchronization;

import org.springframework.util.Assert;

/** Compatibility callback for closing a LobCreator after a JTA transaction. */
public class JtaLobCreatorSynchronization implements Synchronization {

	private final LobCreator lobCreator;
	private boolean beforeCompletionCalled = false;

	public JtaLobCreatorSynchronization(LobCreator lobCreator) {
		Assert.notNull(lobCreator, "LobCreator must not be null");
		this.lobCreator = lobCreator;
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
