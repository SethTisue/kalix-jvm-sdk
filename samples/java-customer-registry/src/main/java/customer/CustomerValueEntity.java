/*
 * Copyright 2019 Lightbend Inc.
 */

package customer;

import com.akkaserverless.javasdk.valueentity.CommandContext;
import com.akkaserverless.javasdk.valueentity.CommandHandler;
import com.akkaserverless.javasdk.valueentity.ValueEntity;
import com.google.protobuf.Empty;
import customer.api.CustomerApi;
import customer.domain.CustomerDomain;

@ValueEntity(entityType = "customers")
public class CustomerValueEntity {

  @CommandHandler
  public CustomerApi.Customer getCustomer(
      CustomerApi.GetCustomerRequest request,
      CommandContext<CustomerDomain.CustomerState> context) {
    CustomerDomain.CustomerState state =
        context.getState().orElseGet(CustomerDomain.CustomerState::getDefaultInstance);
    CustomerApi.Address address = CustomerApi.Address.getDefaultInstance();
    if (state.hasAddress()) {
      address =
          CustomerApi.Address.newBuilder()
              .setStreet(state.getAddress().getStreet())
              .setCity(state.getAddress().getCity())
              .build();
    }
    return CustomerApi.Customer.newBuilder()
        .setCustomerId(state.getCustomerId())
        .setEmail(state.getEmail())
        .setName(state.getName())
        .setAddress(address)
        .build();
  }

  @CommandHandler
  public Empty create(
      CustomerApi.Customer customer, CommandContext<CustomerDomain.CustomerState> context) {
    CustomerDomain.Address address = CustomerDomain.Address.getDefaultInstance();
    if (customer.hasAddress()) {
      address =
          CustomerDomain.Address.newBuilder()
              .setStreet(customer.getAddress().getStreet())
              .setCity(customer.getAddress().getCity())
              .build();
    }
    CustomerDomain.CustomerState state =
        CustomerDomain.CustomerState.newBuilder()
            .setCustomerId(customer.getCustomerId())
            .setEmail(customer.getEmail())
            .setName(customer.getName())
            .setAddress(address)
            .build();
    context.updateState(state);
    return Empty.getDefaultInstance();
  }

  @CommandHandler
  public Empty changeName(
      CustomerApi.ChangeNameRequest request, CommandContext<CustomerDomain.CustomerState> context) {
    if (context.getState().isEmpty())
      throw context.fail("Customer must be created before name can be changed.");
    CustomerDomain.CustomerState updatedState =
        context.getState().get().toBuilder().setName(request.getNewName()).build();
    context.updateState(updatedState);
    return Empty.getDefaultInstance();
  }
}
