/* This code was generated by Akka Serverless tooling.
 * As long as this file exists it will not be re-generated.
 * You are free to make changes to this file.
 */
package com.example.domain

import com.akkaserverless.scalasdk.testkit.ValueEntityResult
import com.akkaserverless.scalasdk.valueentity.ValueEntity
import com.example
import com.google.protobuf.empty.Empty
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CounterSpec
    extends AnyWordSpec
    with Matchers {

  "Counter" must {

    "have example test that can be removed" in {
      val testKit = CounterTestKit(new Counter(_))
      // use the testkit to execute a command
      // and verify final updated state:
      // val result = testKit.someOperation(SomeRequest)
      // verify the response
      // val actualResponse = result.getReply()
      // actualResponse shouldBe expectedResponse
      // verify the final state after the command
      // testKit.getState() shouldBe expectedState
    }

    "handle command Increase" in {
      val testKit = CounterTestKit(new Counter(_))
      // val result = testKit.increase(example.IncreaseValue(...))
    }

    "handle command Decrease" in {
      val testKit = CounterTestKit(new Counter(_))
      // val result = testKit.decrease(example.DecreaseValue(...))
    }

    "handle command Reset" in {
      val testKit = CounterTestKit(new Counter(_))
      // val result = testKit.reset(example.ResetValue(...))
    }

    "handle command GetCurrentCounter" in {
      val testKit = CounterTestKit(new Counter(_))
      // val result = testKit.getCurrentCounter(example.GetCounter(...))
    }

  }
}
