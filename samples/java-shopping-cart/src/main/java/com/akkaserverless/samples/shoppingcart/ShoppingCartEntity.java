/*
 * Copyright 2019 Lightbend Inc.
 */

package com.akkaserverless.samples.shoppingcart;

import com.akkaserverless.javasdk.EntityId;
import com.akkaserverless.javasdk.valueentity.CommandContext;
import com.akkaserverless.javasdk.valueentity.CommandHandler;
import com.akkaserverless.javasdk.valueentity.ValueEntity;
import com.example.valueentity.shoppingcart.Shoppingcart;
import com.example.valueentity.shoppingcart.persistence.Domain;
import com.google.protobuf.Empty;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/** A value based entity. */
@ValueEntity(entityType = "shopping-cart")
public class ShoppingCartEntity {

  private final String entityId;

  public ShoppingCartEntity(@EntityId String entityId) {
    this.entityId = entityId;
  }

  @CommandHandler
  public Shoppingcart.Cart getCart(CommandContext<Domain.Cart> ctx) {
    Domain.Cart cart = ctx.getState().orElse(Domain.Cart.newBuilder().build());
    List<Shoppingcart.LineItem> allItems =
        cart.getItemsList().stream()
            .map(this::convert)
            .sorted(Comparator.comparing(Shoppingcart.LineItem::getProductId))
            .collect(Collectors.toList());
    return Shoppingcart.Cart.newBuilder().addAllItems(allItems).build();
  }

  @CommandHandler
  public Empty addItem(Shoppingcart.AddLineItem item, CommandContext<Domain.Cart> ctx) {
    if (item.getQuantity() <= 0) {
      ctx.fail("Cannot add negative quantity of to item " + item.getProductId());
    }

    Domain.Cart cart = ctx.getState().orElse(Domain.Cart.newBuilder().build());
    Domain.LineItem lineItem = updateItem(item, cart);
    List<Domain.LineItem> lineItems = removeItemByProductId(cart, item.getProductId());
    lineItems.add(lineItem);
    lineItems.sort(Comparator.comparing(Domain.LineItem::getProductId));
    ctx.updateState(Domain.Cart.newBuilder().addAllItems(lineItems).build());
    return Empty.getDefaultInstance();
  }

  @CommandHandler
  public Empty removeItem(Shoppingcart.RemoveLineItem item, CommandContext<Domain.Cart> ctx) {
    Domain.Cart cart = ctx.getState().orElse(Domain.Cart.newBuilder().build());
    Optional<Domain.LineItem> lineItem = findItemByProductId(cart, item.getProductId());

    if (!lineItem.isPresent()) {
      ctx.fail("Cannot remove item " + item.getProductId() + " because it is not in the cart.");
    }

    List<Domain.LineItem> items = removeItemByProductId(cart, item.getProductId());
    items.sort(Comparator.comparing(Domain.LineItem::getProductId));
    ctx.updateState(Domain.Cart.newBuilder().addAllItems(items).build());
    return Empty.getDefaultInstance();
  }

  @CommandHandler
  public Empty removeCart(Shoppingcart.RemoveShoppingCart cart, CommandContext<Domain.Cart> ctx) {
    ctx.deleteState();
    return Empty.getDefaultInstance();
  }

  private Domain.LineItem updateItem(Shoppingcart.AddLineItem item, Domain.Cart cart) {
    return findItemByProductId(cart, item.getProductId())
        .map(li -> li.toBuilder().setQuantity(li.getQuantity() + item.getQuantity()).build())
        .orElse(newItem(item));
  }

  private Domain.LineItem newItem(Shoppingcart.AddLineItem item) {
    return Domain.LineItem.newBuilder()
        .setProductId(item.getProductId())
        .setName(item.getName())
        .setQuantity(item.getQuantity())
        .build();
  }

  private Optional<Domain.LineItem> findItemByProductId(Domain.Cart cart, String productId) {
    Predicate<Domain.LineItem> lineItemExists =
        lineItem -> lineItem.getProductId().equals(productId);
    return cart.getItemsList().stream().filter(lineItemExists).findFirst();
  }

  private List<Domain.LineItem> removeItemByProductId(Domain.Cart cart, String productId) {
    return cart.getItemsList().stream()
        .filter(lineItem -> !lineItem.getProductId().equals(productId))
        .collect(Collectors.toList());
  }

  private Shoppingcart.LineItem convert(Domain.LineItem item) {
    return Shoppingcart.LineItem.newBuilder()
        .setProductId(item.getProductId())
        .setName(item.getName())
        .setQuantity(item.getQuantity())
        .build();
  }
}