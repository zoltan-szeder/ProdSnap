package org.szederz.prodsnap.services;

import org.szederz.prodsnap.entities.Basket;

public interface BasketTransferService {
    void send(Basket basket);
}
