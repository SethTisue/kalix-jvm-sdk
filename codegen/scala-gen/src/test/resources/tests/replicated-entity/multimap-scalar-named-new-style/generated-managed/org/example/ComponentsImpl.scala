package org.example

import kalix.scalasdk.Context
import kalix.scalasdk.DeferredCall
import kalix.scalasdk.Metadata
import kalix.scalasdk.impl.InternalContext
import kalix.scalasdk.impl.ScalaDeferredCallAdapter


// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

/**
 * Not intended for direct instantiation, called by generated code, use Action.components() to access
 */
final class ComponentsImpl(context: InternalContext) extends Components {

  def this(context: Context) =
    this(context.asInstanceOf[InternalContext])

  private def getGrpcClient[T](serviceClass: Class[T]): T =
    context.getComponentGrpcClient(serviceClass)

 @Override
 override def someMultiMap: Components.SomeMultiMapCalls =
   new SomeMultiMapCallsImpl();


 private final class SomeMultiMapCallsImpl extends Components.SomeMultiMapCalls {
   override def put(command: _root_.com.example.replicated.multimap.PutValue): DeferredCall[_root_.com.example.replicated.multimap.PutValue, _root_.com.google.protobuf.empty.Empty] =
     ScalaDeferredCallAdapter(
       command,
       Metadata.empty,
       "com.example.replicated.multimap.MultiMapService",
       "Put",
       () => getGrpcClient(classOf[_root_.com.example.replicated.multimap.MultiMapService]).put(command)
     )
 }

}
