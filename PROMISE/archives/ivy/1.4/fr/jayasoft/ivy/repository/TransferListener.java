package fr.jayasoft.ivy.repository;

import java.util.EventListener;


/**
 * Listen to repository transfer
 */
public interface TransferListener extends EventListener {
    void transferProgress(TransferEvent evt);
}
