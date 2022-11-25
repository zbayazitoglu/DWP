package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.HashMap;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */
    HashMap<Type,Integer> priceMap = new HashMap<>();
    private int totalSeat = 0;
    private int totalPrice = 0;
    boolean adultPresent = false;
    private final TicketPaymentServiceImpl ticketPaymentService = new TicketPaymentServiceImpl();
    private final SeatReservationServiceImpl seatReservationService = new SeatReservationServiceImpl();
    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        createPriceMap();
        if(accountId > 0) {
            calculateTotalSeatAndPrice(ticketTypeRequests);
            if(adultPresent) {
                if(totalSeat <= 20){
                    reserveSeat(accountId);
                    makePayment(accountId);
                } else throw new InvalidPurchaseException("Can not book more than 20 tickets!");
            } else throw new InvalidPurchaseException("At least one adult must be present!");
        } else throw new InvalidPurchaseException("Invalid Id!");
    }
    private void createPriceMap() {
        priceMap.put(Type.ADULT,20);
        priceMap.put(Type.CHILD,10);
        priceMap.put(Type.INFANT,0);
    }
    private void calculateTotalSeatAndPrice(TicketTypeRequest... ticketTypeRequests) {
        for(TicketTypeRequest t:ticketTypeRequests) {
            if(Type.ADULT.equals(t.getTicketType()))
                adultPresent = true;
            totalPrice += priceMap.get(t.getTicketType());
            if(!Type.INFANT.equals(t.getTicketType()))
                totalSeat += t.getNoOfTickets();
        }
    }
    private void reserveSeat(Long accountId) {
        seatReservationService.reserveSeat(accountId, totalSeat);
    }
    private void makePayment(Long accountId) {
        ticketPaymentService.makePayment(accountId, totalPrice);
    }
}
