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
 override def myServiceActionImpl: Components.MyServiceActionImplCalls =
   new MyServiceActionImplCallsImpl();


 private final class MyServiceActionImplCallsImpl extends Components.MyServiceActionImplCalls {
   override def simpleMethod(command: _root_.org.example.service.MyRequest): DeferredCall[_root_.org.example.service.MyRequest, _root_.com.google.protobuf.empty.Empty] =
     ScalaDeferredCallAdapter(
       command,
       Metadata.empty,
       "org.example.service.MyServiceAction",
       "simpleMethod",
       () => getGrpcClient(classOf[_root_.org.example.service.MyServiceAction]).simpleMethod(command)
     )
 }

}
