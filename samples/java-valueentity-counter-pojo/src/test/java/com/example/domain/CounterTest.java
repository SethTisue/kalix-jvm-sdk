/* This code was generated by Akka Serverless tooling.
 * As long as this file exists it will not be re-generated.
 * You are free to make changes to this file.
 */
package com.example.domain;

// tag::sample-unit-test[]
import com.akkaserverless.javasdk.testkit.ValueEntityResult;
import com.example.CounterApi;
import com.google.protobuf.Empty;
import org.junit.Test;
import static org.junit.Assert.*;

public class CounterTest {

  @Test
  public void increaseCounterTest() {
    CounterTestKit testKit = CounterTestKit.of(Counter::new);

    CounterApi.IncreaseValue increaseValueCommand = CounterApi.IncreaseValue.newBuilder()
        .setValue(1)
        .build();
    ValueEntityResult<Empty> result1 = testKit.increase(increaseValueCommand);
    assertEquals(Empty.getDefaultInstance(), result1.getReply());
    assertEquals(1, testKit.getState().value);

    // one more time
    ValueEntityResult<Empty> result2 = testKit.increase(increaseValueCommand);
    assertEquals(Empty.getDefaultInstance(), result2.getReply());
    assertEquals(2, testKit.getState().value);
  }
  // end::sample-unit-test[]

  @Test
  public void decreaseCounterTest() {
    CounterTestKit testKit = CounterTestKit.of(Counter::new);

    CounterApi.IncreaseValue increaseValueCommand = CounterApi.IncreaseValue.newBuilder()
        .setValue(1)
        .build();
    ValueEntityResult<Empty> result1 = testKit.increase(increaseValueCommand);
    assertEquals(Empty.getDefaultInstance(), result1.getReply());
    assertEquals(1, testKit.getState().value);

    CounterApi.DecreaseValue decreaseValueCommand = CounterApi.DecreaseValue.newBuilder()
        .setValue(-1)
        .build();
    ValueEntityResult<Empty> result2 = testKit.decrease(decreaseValueCommand);
    assertEquals(Empty.getDefaultInstance(), result2.getReply());

    assertEquals(0, testKit.getState().value);
  }


  @Test
  public void resetCounterTest() {
    CounterTestKit testKit = CounterTestKit.of(Counter::new);

    CounterApi.IncreaseValue increaseValueCommand = CounterApi.IncreaseValue.newBuilder()
        .setValue(1)
        .build();
    ValueEntityResult<Empty> result1 = testKit.increase(increaseValueCommand);
    assertEquals(Empty.getDefaultInstance(), result1.getReply());
    assertEquals(1, testKit.getState().value);

    CounterApi.ResetValue resetCommand = CounterApi.ResetValue.getDefaultInstance();
    ValueEntityResult<Empty> resetResult = testKit.reset(resetCommand);
    assertEquals(Empty.getDefaultInstance(), resetResult.getReply());

    assertEquals(0, testKit.getState().value);
  }

}