package com.gmail.amalcaraz89.partnership.func;

import com.gmail.amalcaraz89.partnership.model.Partner;
import com.gmail.amalcaraz89.partnership.model.Payout;
import com.gmail.amalcaraz89.partnership.model.Polling;
import io.nuls.contract.sdk.Address;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public final class Utils {

    public static BigInteger getNulsPrecision(double amount) {

        return BigInteger.valueOf((long) (amount * 100000000));

    }

    static <T> boolean contains(List<T> list, T o) {

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(o)) {
                return true;
            }
        }
        return false;

    }

    static boolean containsPartner(List<Partner> list, Address address) {

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getAddress().equals(address)) {
                return true;
            }
        }
        return false;

    }

    static Partner findPartner(List<Partner> list, Address address) {

        for (int i = 0; i < list.size(); i++) {
            Partner partner = list.get(i);
            if (partner.getAddress().equals(address)) {
                return partner;
            }
        }
        return null;

    }

    static Payout findPayout(List<Payout> list, long date) {

        for (int i = 0; i < list.size(); i++) {
            Payout payout = list.get(i);
            if (payout.getDate() == date) {
                return payout;
            }
        }
        return null;

    }

    static Polling findPolling(Map<Long, Polling> pollingList, Address address, Address initiator, int status, int type, int result) {

        long size = pollingList.size();

        for (long i = 1; i <= size; i++) {

            Polling polling = pollingList.get(i);

            boolean statusCond = (status == -1 || polling.getStatus() == status);
            boolean typeCond = (type == -1 || polling.getType() == type);
            boolean resultCond = (result == -1 || polling.getResult() == result);

            boolean addressCond = (address == null || polling.getPartner().getAddress().equals(address));
            boolean initiatorCond = (initiator == null || polling.getInitiator().equals(initiator));

            if (statusCond && typeCond && resultCond && addressCond && initiatorCond) {
                return polling;
            }
        }

        return null;
    }

}
