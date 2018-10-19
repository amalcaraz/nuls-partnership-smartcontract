package com.gmail.amalcaraz89.partnership.func;

import io.nuls.contract.sdk.Address;
import io.nuls.contract.sdk.Msg;

import static io.nuls.contract.sdk.Utils.require;

public class Owner {
    protected Address owner;

    Owner(Address owner) {
        require(owner != null, "Owner's address can not be empty");
        this.owner = owner;
    }

    boolean isOwner(Address address) {
        return this.owner.equals(address);
    }

    void requireOwner(Address address) {
        this.requireOwner(address, "Only owners are allowed");
    }

    void requireOwner(Address address, String errorMessage) {
        require(this.isOwner(address), errorMessage);
    }

    void setOwner(Address address) {
        require(this.owner.equals(Msg.sender()), "Only owner can grant a new owner");
        this.owner = address;
    }
}
