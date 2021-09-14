/* This code was generated by Akka Serverless tooling.
 * As long as this file exists it will not be re-generated.
 * You are free to make changes to this file.
 */

package shopping.cart.actions;

import com.akkaserverless.javasdk.action.ActionCreationContext;
import com.google.protobuf.Empty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shopping.cart.domain.ShoppingCartDomain;

/**
 * This action illustrates the consumption from a topic (shopping-cart-events) Incoming messages are
 * sent to log with no further processing.
 */
public class ShoppingCartAnalyticsServiceAction extends AbstractShoppingCartAnalyticsServiceAction {

  private static final Logger LOG = LoggerFactory.getLogger(ShoppingCartAnalyticsServiceAction.class);

  public ShoppingCartAnalyticsServiceAction(ActionCreationContext creationContext) {}

  /** Handler for "ProcessAdded". */
  @Override
  public Effect<Empty> processAdded(ShoppingCartDomain.ItemAdded itemAdded) {
    LOG.info("Analytics: item added '{}'", itemAdded);
    return effects().noReply();
  }
  /** Handler for "ProcessRemoved". */
  @Override
  public Effect<Empty> processRemoved(ShoppingCartDomain.ItemRemoved itemRemoved) {
    LOG.info("Analytics: item removed '{}'", itemRemoved);
    return effects().noReply();
  }
  /** Handler for "ProcessCheckedOut". */
  @Override
  public Effect<Empty> processCheckedOut(ShoppingCartDomain.CheckedOut checkedOut) {
    LOG.info("Analytics: cart checked out '{}'", checkedOut);
    return effects().noReply();
  }
}