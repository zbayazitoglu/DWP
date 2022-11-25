import org.junit.Test;
import org.mockito.InjectMocks;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.Assert.*;

public class TicketServiceImplTest {

    @InjectMocks
    TicketServiceImpl ticketService = new TicketServiceImpl();

    @Test (expected = InvalidPurchaseException.class)
    public void purchaseTickets_InvalidId() {
        try
        {
            TicketTypeRequest ticketTypeRequest1 = new TicketTypeRequest(Type.ADULT, 4);
            TicketTypeRequest ticketTypeRequest2 = new TicketTypeRequest(Type.CHILD, 5);
            TicketTypeRequest ticketTypeRequest3 = new TicketTypeRequest(Type.INFANT, 1);

            Long accountId = 0L;
            ticketService.purchaseTickets(accountId,ticketTypeRequest1,ticketTypeRequest2,ticketTypeRequest3);
        }
        catch(InvalidPurchaseException e)
        {
            String message = "Invalid Id!";
            assertEquals(message, e.getMessage());
            throw e;
        }
    }

    @Test (expected = InvalidPurchaseException.class)
    public void purchaseTickets_AdultNotPresent() {
        try
        {
            TicketTypeRequest ticketTypeRequest2 = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 5);
            TicketTypeRequest ticketTypeRequest3 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);

            Long accountId = 123L;
            ticketService.purchaseTickets(accountId,ticketTypeRequest2,ticketTypeRequest3);
        }
        catch(InvalidPurchaseException e)
        {
            String message = "At least one adult must be present!";
            assertEquals(message, e.getMessage());
            throw e;
        }
    }

    @Test (expected = InvalidPurchaseException.class)
    public void purchaseTickets_InvalidTicketCount() {
        try
        {
            TicketTypeRequest ticketTypeRequest1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 4);
            TicketTypeRequest ticketTypeRequest2 = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 20);
            TicketTypeRequest ticketTypeRequest3 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);

            Long accountId = 123L;
            ticketService.purchaseTickets(accountId,ticketTypeRequest1,ticketTypeRequest2,ticketTypeRequest3);
        }
        catch(InvalidPurchaseException e)
        {
            String message = "Can not book more than 20 tickets!";
            assertEquals(message, e.getMessage());
            throw e;
        }
    }
}
