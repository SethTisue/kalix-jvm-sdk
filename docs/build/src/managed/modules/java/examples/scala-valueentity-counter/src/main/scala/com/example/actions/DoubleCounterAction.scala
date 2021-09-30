/* This code was generated by Akka Serverless tooling.
 * As long as this file exists it will not be re-generated.
 * You are free to make changes to this file.
 */
package com.example.actions

import com.akkaserverless.scalasdk.SideEffect
import com.akkaserverless.scalasdk.action.Action
import com.akkaserverless.scalasdk.action.ActionCreationContext
import com.example.IncreaseValue
import com.google.protobuf.empty.Empty

// tag::controller-forward[]
// tag::controller-side-effect[]
/** An action. */
class DoubleCounterAction(creationContext: ActionCreationContext) extends AbstractDoubleCounterAction {

  private val increaseCallRef =
    creationContext.serviceCallFactory.lookup( // <1>
      "com.example.CounterService",
      "Increase",
      classOf[IncreaseValue])

  /** Handler for "Increase". */
  override def increase(increaseValue: IncreaseValue): Action.Effect[Empty] = {
    // end::controller-side-effect[]
    val doubled = increaseValue.value * 2
    val increaseValueDoubled = increaseValue.copy(value = doubled) // <2>

    effects.forward(increaseCallRef.createCall(increaseValueDoubled)) // <3>
  }

  // end::controller-forward[]
  def increaseWithSideEffect(increaseValue: IncreaseValue): Action.Effect[Empty] = {
    // tag::controller-side-effect[]
    val doubled = increaseValue.value * 2
    val increaseValueDoubled = increaseValue.copy(value = doubled) // <2>

    effects.reply(Empty.defaultInstance) // <3>
      .addSideEffect( // <4>
        SideEffect(increaseCallRef.createCall(increaseValueDoubled)))
  }
  // tag::controller-forward[]

}
// end::controller-forward[]
// end::controller-side-effect[]