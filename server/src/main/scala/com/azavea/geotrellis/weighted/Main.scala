/*
 * Copyright (c) 2016 Azavea.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.azavea.geotrellis.weighted

import akka.actor.ActorSystem
import akka.actor.Props
import akka.event.Logging
import akka.io.IO
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

object AkkaSystem {
  implicit val system = ActorSystem("libya-system")
  implicit val materializer = ActorMaterializer()

  trait LoggerExecutor {
    protected implicit val log = Logging(system, "app")
  }
}

object Main extends Router {

  val config = ConfigFactory.load()
  val staticPath = config.getString("geotrellis.server.static-path")
  val port = config.getInt("geotrellis.port")
  val host = config.getString("geotrellis.hostname")

  val dataModel =
    new DataModel(config)

  def main(args: Array[String]): Unit = {
    import AkkaSystem._

    Http().bindAndHandle(routes, host, port)
  }
}
