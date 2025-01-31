/*
 * Copyright 2021 Lightbend Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kalix.scalasdk.impl.view

import akka.stream.Materializer
import kalix.javasdk
import kalix.javasdk.view.ViewOptions
import kalix.scalasdk.Metadata
import kalix.scalasdk.impl.MetadataConverters
import kalix.scalasdk.view.UpdateContext
import kalix.scalasdk.view.View
import kalix.scalasdk.view.ViewCreationContext
import kalix.scalasdk.view.ViewProvider
import com.google.protobuf.Descriptors

import java.util.Optional
import scala.jdk.CollectionConverters.SetHasAsJava
import scala.jdk.OptionConverters._

private[scalasdk] final class JavaViewAdapter[S](scalaSdkView: View[S]) extends javasdk.view.View[S] {
  override def emptyState(): S = scalaSdkView.emptyState

  override def _internalSetUpdateContext(context: Optional[javasdk.view.UpdateContext]): Unit =
    scalaSdkView._internalSetUpdateContext(context.map(new ScalaUpdateContextAdapter(_)).toScala)
}

private[scalasdk] final class JavaViewProviderAdapter[S, V <: View[S]](scalaSdkProvider: ViewProvider[S, V])
    extends javasdk.view.ViewProvider[S, javasdk.view.View[S]] {
  override def serviceDescriptor(): Descriptors.ServiceDescriptor =
    scalaSdkProvider.serviceDescriptor

  override def viewId(): String =
    scalaSdkProvider.viewId

  override def options(): ViewOptions =
    javasdk.impl.view.ViewOptionsImpl(scalaSdkProvider.options.forwardHeaders.asJava)

  override def newRouter(
      context: javasdk.view.ViewCreationContext): javasdk.impl.view.ViewRouter[S, javasdk.view.View[S]] = {
    val scalaSdkRouter = scalaSdkProvider
      .newRouter(new ScalaViewCreationContextAdapter(context))
      .asInstanceOf[ViewRouter[S, View[S]]]
    new JavaViewRouterAdapter[S](new JavaViewAdapter[S](scalaSdkRouter.view), scalaSdkRouter)
  }

  override def additionalDescriptors(): Array[Descriptors.FileDescriptor] =
    scalaSdkProvider.additionalDescriptors.toArray
}

private[scalasdk] class JavaViewRouterAdapter[S](
    javaSdkView: javasdk.view.View[S],
    scalaSdkHandler: ViewRouter[S, View[S]])
    extends javasdk.impl.view.ViewRouter[S, javasdk.view.View[S]](javaSdkView) {

  override def handleUpdate(commandName: String, state: S, event: Any): javasdk.view.View.UpdateEffect[S] = {
    scalaSdkHandler.handleUpdate(commandName, state, event) match {
      case effect: ViewUpdateEffectImpl.PrimaryUpdateEffect[S] => effect.toJavaSdk
    }
  }
}

private[scalasdk] final class ScalaViewCreationContextAdapter(javaSdkContext: javasdk.view.ViewCreationContext)
    extends ViewCreationContext {
  override def viewId: String =
    javaSdkContext.viewId()

  override def materializer(): Materializer = javaSdkContext.materializer()
}

private[scalasdk] final class ScalaUpdateContextAdapter(val javaSdkContext: javasdk.view.UpdateContext)
    extends UpdateContext {
  override def eventSubject: Option[String] =
    javaSdkContext.eventSubject().toScala

  override def eventName: String =
    javaSdkContext.eventName()

  override def metadata: Metadata =
    MetadataConverters.toScala(javaSdkContext.metadata())

  override def viewId: String =
    javaSdkContext.viewId()

  override def materializer(): Materializer = javaSdkContext.materializer()
}
