/*
 * Copyright 2019 Lightbend Inc.
 */

package shopping.product;

import com.akkaserverless.javasdk.Reply;
import com.akkaserverless.javasdk.ServiceCallRef;
// tag::annotation[]
import com.akkaserverless.javasdk.action.Action;
import com.akkaserverless.javasdk.action.ActionContext;
import com.akkaserverless.javasdk.action.Handler;
import com.google.protobuf.Any;
import com.google.protobuf.Empty;
// end::annotation[]
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shopping.cart.domain.ShoppingCartDomain;
import shopping.product.api.ProductPopularityApi;
// tag::annotation[]

@Action
public class ToProductPopularityAction {
  // end::annotation[]
  private static final Logger LOG = LoggerFactory.getLogger(ToProductPopularityAction.class);

  // tag::forwardRemoved[]
  private static final String POPULARITY_SERVICE = "shopping.product.api.ProductPopularityService";
  // end::forwardRemoved[]

  // tag::methods[]
  @Handler
  public Reply<Empty> forwardAdded(ShoppingCartDomain.ItemAdded in, ActionContext ctx) {
    // end::methods[]

    ProductPopularityApi.IncreasePopularity increase =
        ProductPopularityApi.IncreasePopularity.newBuilder()
            .setProductId(in.getItem().getProductId())
            .setQuantity(in.getItem().getQuantity())
            .build();

    LOG.info("Received: '{}', publishing: {}", in, increase);
    ServiceCallRef<ProductPopularityApi.IncreasePopularity> call =
        ctx.serviceCallFactory()
            .lookup(POPULARITY_SERVICE, "Increase", ProductPopularityApi.IncreasePopularity.class);
    return Reply.forward(call.createCall(increase));
    // tag::methods[]
  }
  // tag::forwardRemoved[]

  @Handler
  public Reply<Empty> forwardRemoved(ShoppingCartDomain.ItemRemoved in, ActionContext ctx) { // <1>
    // end::methods[]

    ProductPopularityApi.DecreasePopularity decrease = // ... <2>
        // end::forwardRemoved[]
        ProductPopularityApi.DecreasePopularity.newBuilder()
            .setProductId(in.getProductId())
            .setQuantity(in.getQuantity())
            .build();

    LOG.info("Received: '{}', publishing: {}", in, decrease);
    // tag::forwardRemoved[]
    ServiceCallRef<ProductPopularityApi.DecreasePopularity> decreaseRef =
        ctx.serviceCallFactory()
            .lookup(
                POPULARITY_SERVICE, // <3>
                "Decrease", // <4>
                ProductPopularityApi.DecreasePopularity.class // <5>
                );
    return Reply.forward(decreaseRef.createCall(decrease)); // <6>
    // tag::methods[]
  }
  // end::forwardRemoved[]
  // end::methods[]

  @Handler
  public Empty catchOthers(Any in) {
    return Empty.getDefaultInstance();
  }
}